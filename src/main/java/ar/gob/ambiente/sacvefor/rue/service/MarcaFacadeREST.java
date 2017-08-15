
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.enitites.Marca;
import ar.gob.ambiente.sacvefor.rue.enitites.Modelo;
import ar.gob.ambiente.sacvefor.rue.facades.MarcaFacade;
import java.net.URI;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import javax.ws.rs.core.UriInfo;

/**
 * Servicio que implementa los métodos expuestos por la API REST para la entidad Marca
 * @author rincostante
 */
@Stateless
@Path("vehiculos_marcas")
public class MarcaFacadeREST {

    @EJB
    private MarcaFacade marcaFacade;
    
    @Context
    UriInfo uriInfo;     
    
    /**
     * Método para crear una Marca.
     * @param entity: La Marca a persistir
     * @return 
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(ar.gob.ambiente.sacvefor.servicios.rue.Marca entity) {
        // instancio la entidad
        Marca marca = new Marca();
        marca.setNombre(entity.getNombre());
        // persisto
        try{
            marcaFacade.create(marca);
            // armo la respuesta exitosa
            UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
            URI uri = uriBuilder.path(marca.getId().toString()).build();
            return Response.created(uri).build();
        }catch(IllegalArgumentException | UriBuilderException ex){
            // armo la respuesta de error
            return Response.status(400).entity("Hubo un error procesado la inserción en el Registro Unico. " + ex.getMessage()).build();
        }
    }

    /**
     * Método para editar una Marca existente
     * @param id
     * @param entity 
     * @return  
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") Long id, ar.gob.ambiente.sacvefor.servicios.rue.Marca entity) {
        Marca marca = marcaFacade.find(id);
        marca.setNombre(entity.getNombre());
        try{
            marcaFacade.edit(marca);
            // armo la respuesta exitosa
            return Response.ok().build();
        }catch(IllegalArgumentException | UriBuilderException ex){
            // armo la respuesta de error
            return Response.notModified().entity("Hubo un error procesado la actualización en el Registro Unico. " + ex.getMessage()).build();
        }
    }

    /**
     * Método para eliminar una Marca.
     * Esto solo se podrá hacer si previamente se verificó que los Modelos de la Marca
     * no están asociados a ningún Vehículo
     * @param id: Id de la Marca a eliminar. Se la obtiene y se la elimina
     */
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        marcaFacade.remove(marcaFacade.find(id));
    }

    /**
     * Método para obtener la Marca correspondiente al id recibido
     * Ej: [PATH]/vehiculos_marcas/1
     * @param id: id de la Marca a obtener
     * @return
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Marca find(@PathParam("id") Long id) {
        return marcaFacade.find(id);
    }

    /**
     * Método que retorn todas las Marcas registradas
     * Ej: [PATH]/vehiculos_marcas
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Marca> findAll() {
        return marcaFacade.findAll();
    }
    
    /**
     * Método que, si existe una Marca con el nombre recibido como parámetro, la retorna
     * Ej: [PATH]/vehiculos_marcas/query?name=TOYOTA
     * @param name
     * @return 
     */
    @GET
    @Path("/query")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Marca findByName(@QueryParam("name") String name) {
        return marcaFacade.getExistente(name);
    }  
    
    /**
     * Método que devuelve todos los Modelos correspondientes a la Marca cuyo id se recibe como parámetro
     * Ej: [PATH]/vehiculos_marcas/1/modelos
     * @param id
     * @return 
     */
    @GET
    @Path("{id}/modelos")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Modelo> findModelosByMarca(@PathParam("id") Long id){
        return marcaFacade.getModelos(id);
    }

    /**
     * Método que obtiene un listado de Marcas cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/vehiculos_marcas/1/10
     * @param from: parámetro 'desde' el cual se inicia el listado
     * @param to: parámetro 'hasta' el cual se completa el listado
     * @return 
     */
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Marca> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return marcaFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de las Marcas registradas
     * Ej: [PATH]/vehiculos_marcas/count
     * @return 
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(marcaFacade.count());
    }
}
