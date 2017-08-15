
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.enitites.Persona;
import ar.gob.ambiente.sacvefor.rue.enitites.TipoSociedad;
import ar.gob.ambiente.sacvefor.rue.facades.TipoSociedadFacade;
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
 * Servicio que implementa los métodos expuestos por la API REST para la entidad TipoSociedad
 * @author rincostante
 */
@Stateless
@Path("tiposociedad")
public class TipoSociedadFacadeREST {

    @EJB
    private TipoSociedadFacade tipoSocFacade;
    
    /**
     * Método para crear un Tipo de Sociedad.
     * Este método se podrá ejecutar si previamente findByName() devuelve nulo
     * @param entity: El Tipo de Sociedad a persistir
     */     
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(TipoSociedad entity) {
        tipoSocFacade.create(entity);
    }

    /**
     * Método para editar un Tipo de Sociedad existente
     * Este método se podrá ejecutar si findByName() previamente devuelve nulo o
     * un Tipo de Sociedad con el mismo id de la que se está editando
     * @param id
     * @param entity 
     */       
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, TipoSociedad entity) {
        tipoSocFacade.edit(entity);
    }

    /**
     * Método para eliminar un Tipo de Sociedad.
     * Esto solo se podrá hacer si previamente se verificó que no esté asociada a ninguna Persona
     * @param id: Id del Tipo de Sociedad a eliminar. Se la obtiene y se la elimina
     */        
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        tipoSocFacade.remove(tipoSocFacade.find(id));
    }

    /**
     * Método para obtener el Tipo de Sociedad correspondiente al id recibido
     * Ej: [PATH]/tiposociedad/1
     * @param id: id del Tipo de Sociedad a obtener
     * @return
     */        
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TipoSociedad find(@PathParam("id") Long id) {
        return tipoSocFacade.find(id);
    }

    /**
     * Método que retorn todas los Tipos de Sociedades registrados
     * Ej: [PATH]/tiposociedad
     * @return 
     */       
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoSociedad> findAll() {
        return tipoSocFacade.findAll();
    }
    
    /**
     * Método que, si existe un Tipo de Sociedad con el nombre recibido como parámetro, lo retorna
     * Ej: [PATH]/tiposociedad/query?name=SOCIEDAD ANONIMA
     * @param name
     * @return 
     */        
    @GET
    @Path("/query")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TipoSociedad findByName(@QueryParam("name") String name) {
        return tipoSocFacade.getExistente(name);
    }       
    
    /**
     * Método que devuelve todas las Personas correspondientes al Tipo de Sociedad cuyo id se recibe como parámetro
     * Ej: [PATH]/tiposociedad/1/personas
     * @param id
     * @return 
     */        
    @GET
    @Path("{id}/personas")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findPersonasByEntidad(@PathParam("id") Long id){
        return tipoSocFacade.getPersonas(id);
    }          

    /**
     * Método que obtiene un listado de Tipos de Sociedad cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/tiposociedad/1/10
     * @param from: parámetro 'desde' el cual se inicia el listado
     * @param to: parámetro 'hasta' el cual se completa el listado
     * @return 
     */       
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoSociedad> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return tipoSocFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de los Tipos de Sociedad registrados
     * Ej: [PATH]/tiposociedad/count
     * @return 
     */    
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(tipoSocFacade.count());
    }
}
