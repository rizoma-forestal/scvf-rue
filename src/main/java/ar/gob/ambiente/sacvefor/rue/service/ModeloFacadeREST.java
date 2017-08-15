
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.enitites.Marca;
import ar.gob.ambiente.sacvefor.rue.enitites.Modelo;
import ar.gob.ambiente.sacvefor.rue.enitites.Vehiculo;
import ar.gob.ambiente.sacvefor.rue.facades.MarcaFacade;
import ar.gob.ambiente.sacvefor.rue.facades.ModeloFacade;
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
 * Servicio que implementa los métodos expuestos por la API REST para la entidad Modelo
 * @author rincostante
 */
@Stateless
@Path("vehiculos_modelos")
public class ModeloFacadeREST {
    
    @EJB
    private ModeloFacade modeloFacade; 
    @EJB
    private MarcaFacade marcaFacade;
    
    @Context
    UriInfo uriInfo;        

    /**
     * Método para crear un Modelo.
     * @param entity: La Marca a persistir
     * @return 
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(ar.gob.ambiente.sacvefor.servicios.rue.Modelo entity) {
        // instancio la entidad
        Modelo modelo = new Modelo();
        modelo.setNombre(entity.getNombre());
        // obtengo la Marca asociada al modelo
        try{
            Marca marca = marcaFacade.find(entity.getMarca().getId());
            modelo.setMarca(marca);
            modeloFacade.create(modelo);
            // armo la respuesta exitosa
            UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
            URI uri = uriBuilder.path(modelo.getId().toString()).build();
            return Response.created(uri).build();
        }catch(IllegalArgumentException | UriBuilderException ex){
            // armo la respuesta de error
            return Response.status(400).entity("Hubo un error procesado la inserción en el Registro Unico. " + ex.getMessage()).build();
        }
    }

    /**
     * Método para editar un Modelo existente
     * @param id
     * @param entity 
     * @return  
     */    
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") Long id, ar.gob.ambiente.sacvefor.servicios.rue.Modelo entity) {
        // instancio la entidad
        Modelo modelo = modeloFacade.find(entity.getId());
        modelo.setNombre(entity.getNombre());
        // obtengo la Marca asociada al modelo
        try{
            Marca marca = marcaFacade.find(entity.getMarca().getId());
            modelo.setMarca(marca);
            modeloFacade.edit(modelo);
            // armo la respuesta exitosa
            return Response.ok().build();
        }catch(IllegalArgumentException | UriBuilderException ex){
            // armo la respuesta de error
            return Response.notModified().entity("Hubo un error procesado la actualización en el Registro Unico. " + ex.getMessage()).build();
        }
    }

    /**
     * Método para eliminar un Modelo.
     * Esto solo se podrá hacer si previamente se verificó que el Modelo no esté asociado a ningún vehículo
     * @param id: Id de la Marca a eliminar. Se la obtiene y se la elimina
     */    
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        modeloFacade.remove(modeloFacade.find(id));
    }

    /**
     * Método para obtener el Modelo correspondiente al id recibido
     * Ej: [PATH]/vehiculos_modelos/1
     * @param id: id del Modelo a obtener
     * @return
     */    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Modelo find(@PathParam("id") Long id) {
        return modeloFacade.find(id);
    }
    
    /**
     * Método que, si existe un Modelo con el nombre recibido como parámetro, lo retorna
     * Ej: [PATH]/vehiculos_modelos/query?name=4 RUNNER
     * @param name
     * @return 
     */    
    @GET
    @Path("/query")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Modelo findByName(@QueryParam("name") String name) {
        return modeloFacade.getExistente(name);
    }      
    
    /**
     * Método que devuelve todos los Vehículos correspondientes al Modelo cuyo id se recibe como parámetro
     * Ej: [PATH]/vehiculos_modelos/1/vehiculos
     * @param id
     * @return 
     */    
    @GET
    @Path("{id}/vehiculos")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findModelosByMarca(@PathParam("id") Long id){
        return modeloFacade.getVehiculos(id);
    }    

    /**
     * Método que retorna todas los Modelos registradas
     * Ej: [PATH]/vehiculos_modelos
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Modelo> findAll() {
        return modeloFacade.findAll();
    }

    /**
     * Método que obtiene un listado de Modelos cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/vehiculos_modelos/1/10
     * @param from: parámetro 'desde' el cual se inicia el listado
     * @param to: parámetro 'hasta' el cual se completa el listado
     * @return 
     */    
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Modelo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return modeloFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de los Modelos registrados
     * Ej: [PATH]/vehiculos_modelos/count
     * @return 
     */    
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(modeloFacade.count());
    }
}
