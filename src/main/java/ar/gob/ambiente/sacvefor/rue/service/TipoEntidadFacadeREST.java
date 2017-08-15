
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.enitites.Persona;
import ar.gob.ambiente.sacvefor.rue.enitites.TipoEntidad;
import ar.gob.ambiente.sacvefor.rue.facades.TipoEntidadFacade;
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
import javax.ws.rs.core.MediaType;

/**
 * Servicio que implementa los métodos expuestos por la API REST para la entidad TipoEntidad
 * @author rincostante
 */
@Stateless
@Path("tipoentidad")
public class TipoEntidadFacadeREST {

    @EJB
    private TipoEntidadFacade tipoEntFacade;

    /**
     * Método para crear un Tipo de Entidad.
     * Este método se podrá ejecutar si previamente findByName() devuelve nulo
     * @param entity: El Tipo de Entidad a persistir
     */    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(TipoEntidad entity) {
        tipoEntFacade.create(entity);
    }

    /**
     * Método para editar un Tipo de Entidad existente
     * Este método se podrá ejecutar si findByName() previamente devuelve nulo o
     * un Tipo de Entidad con el mismo id de la que se está editando
     * @param id
     * @param entity 
     */    
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, TipoEntidad entity) {
        tipoEntFacade.edit(entity);
    }

    /**
     * Método para eliminar un Tipo de Entidad.
     * Esto solo se podrá hacer si previamente se verificó que no esté asociada a ninguna Persona
     * @param id: Id del Tipo de Entidad a eliminar. Se la obtiene y se la elimina
     */    
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        tipoEntFacade.remove(tipoEntFacade.find(id));
    }

    /**
     * Método para obtener el Tipo de Entidad correspondiente al id recibido
     * Ej: [PATH]/tipoentidad/1
     * @param id: id del Tipo de Entidad a obtener
     * @return
     */    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TipoEntidad find(@PathParam("id") Long id) {
        return tipoEntFacade.find(id);
    }

    /**
     * Método que retorn todas los Tipos de Entidades registrados
     * Ej: [PATH]/tipoentidad
     * @return 
     */    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoEntidad> findAll() {
        return tipoEntFacade.findAll();
    }
    
    /**
     * Método que, si existe un Tipo de Entidad con el nombre recibido como parámetro, lo retorna
     * Ej: [PATH]/tipoentidad/query?name=PRODUCTOR
     * @param name
     * @return 
     */    
    @GET
    @Path("/query")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TipoEntidad findByName(@QueryParam("name") String name) {
        return tipoEntFacade.getExistente(name);
    }     
    
    /**
     * Método que devuelve todas las Personas correspondientes al Tipo de Entidad cuyo id se recibe como parámetro
     * Ej: [PATH]/tipoentidad/1/personas
     * @param id
     * @return 
     */    
    @GET
    @Path("{id}/personas")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findPersonasByEntidad(@PathParam("id") Long id){
        return tipoEntFacade.getPersonas(id);
    }        

    /**
     * Método que obtiene un listado de Tipos de Entidad cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/tipoentidad/1/10
     * @param from: parámetro 'desde' el cual se inicia el listado
     * @param to: parámetro 'hasta' el cual se completa el listado
     * @return 
     */    
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoEntidad> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return tipoEntFacade.findRange(new int[]{from, to});
    }
    
    /**
     * Método que devuelve un entero con la totalidad de los Tipos de Entidad registrados
     * Ej: [PATH]/tipoentidad/count
     * @return 
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(tipoEntFacade.count());
    }
}
