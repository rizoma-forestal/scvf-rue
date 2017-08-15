
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.enitites.Modelo;
import ar.gob.ambiente.sacvefor.rue.enitites.Persona;
import ar.gob.ambiente.sacvefor.rue.enitites.Usuario;
import ar.gob.ambiente.sacvefor.rue.enitites.Vehiculo;
import ar.gob.ambiente.sacvefor.rue.facades.ModeloFacade;
import ar.gob.ambiente.sacvefor.rue.facades.PersonaFacade;
import ar.gob.ambiente.sacvefor.rue.facades.UsuarioFacade;
import ar.gob.ambiente.sacvefor.rue.facades.VehiculoFacade;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
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
 * Servicio que implementa los métodos expuestos por la API REST para la entidad Vehículo
 * @author rincostante
 */
@Stateless
@Path("vehiculos")
public class VehiculoFacadeREST {

    @EJB
    private VehiculoFacade vehiculoFacade;  
    @EJB
    private UsuarioFacade usFacade;
    @EJB
    private ModeloFacade modeloFacade;
    @EJB
    private PersonaFacade personaFacade;
    
    @Context
    UriInfo uriInfo;    

    /**
     * Método para crear un Vehículo.
     * Este método se podrá ejecutar si previamente findByQuery() con el cuit como parámentro, devuelve nulo
     * @param entity: El Vehículo a persistir
     * @return : la uri de acceso a la Persona generada
     */       
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(ar.gob.ambiente.sacvefor.servicios.rue.Vehiculo entity) {
        // instancio la entidad
        Vehiculo veh = instanciarVehiculo(entity, null);

        // persisto la entidad instanciada
        try{
            vehiculoFacade.create(veh);
            // armo la respuesta exitosa
            UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
            URI uri = uriBuilder.path(veh.getId().toString()).build();
            return Response.created(uri).build();
        }catch(IllegalArgumentException | UriBuilderException ex){
            // armo la respuesta de error
            return Response.status(400).entity("Hubo un error procesado la inserción en el Registro Unico. " + ex.getMessage()).build();
        }
    }

    /**
     * Método para editar un Vehículo existente
     * Este método se podrá ejecutar si findByQuery() con el cuit como parámentro, previamente devuelve nulo o
     * un Vehículo con el mismo id de la que se está editando
     * @param id : id del Vehículo a actualizar
     * @param entity : El Vehículo a actualizar proveniente del paqRue compartido
     * @return  
     */        
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") Long id, ar.gob.ambiente.sacvefor.servicios.rue.Vehiculo entity) {
        // instancio la entidad
        Vehiculo veh = instanciarVehiculo(entity, id);

        // persisto la entidad instanciada
        try{
            vehiculoFacade.edit(veh);
            // armo la respuesta exitosa
            return Response.ok().build();
        }catch(IllegalArgumentException | UriBuilderException ex){
            // armo la respuesta de error
            return Response.notModified().entity("Hubo un error procesado la actualización en el Registro Unico. " + ex.getMessage()).build();
        }
    }

    /**
     * Método para obtener el Vehículo correspondiente al id recibido
     * Ej: [PATH]/vehiculos/1
     * @param id: id de la Marca a obtener
     * @return
     */      
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Vehiculo find(@PathParam("id") Long id) {
        return vehiculoFacade.find(id);
    }

    /**
     * Método que retorn todas los Vehículo registrados
     * Ej: [PATH]/vehiculos
     * @return 
     */    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findAll() {
        return vehiculoFacade.findAll();
    }
    
    /**
     * Método que, según los parámetros recibidos ejecuta uno u otro método
     * @param matricula: con la matrícula devuelve un listado con el Vehículo correspondiente a la matrícula ingresada:
     * Ej: [PATH]/vehiculos/query?matricula=RJS-374
     * @param hab: con hab en true, trae los Vehículos habilitados:
     * Ej: [PATH]/vehiculos/query?hab=true
     * @param cuit: con el parámetro cuit, devuelve el Vehículo cuyo titular/empresa correspondiente al CUIT ingresado
     * Ej: [PATH]/vehiculos/query?cuit=20339210315
     * @return 
     */        
    @GET
    @Path("/query")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findByQuery(@QueryParam("matricula") String matricula, @QueryParam("hab") boolean hab, @QueryParam("cuit") Long cuit) {
        List<Vehiculo> result = new ArrayList<>();
        if(matricula != null){
            Vehiculo veh = vehiculoFacade.getExistente(matricula);
            if(veh != null){
                result.add(veh);
            }
        }else if(hab){
            return vehiculoFacade.getHabilitadas();
        }else if(cuit != null){
            return vehiculoFacade.getByTitular(cuit);
        }
        return result;
    }   
    
    /**
     * Método que obtiene un listado de Vehículos cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/vehiculos/1/10
     * @param from: parámetro 'desde' el cual se inicia el listado
     * @param to: parámetro 'hasta' el cual se completa el listado
     * @return 
     */  
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return vehiculoFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de las Vehúculos registrados
     * Ej: [PATH]/vehiculos/count
     * @return 
     */       
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(vehiculoFacade.count());
    }

    /**
     * Método que instancia un Vehículo (Entity) con los datos del Vehículo (POJO) obtenido.
     * Sea para editar o para insertar
     * @param entity 
     */    
    private Vehiculo instanciarVehiculo(ar.gob.ambiente.sacvefor.servicios.rue.Vehiculo entity, Long id) {
        Vehiculo veh;
        // obtengo el usuario de la provincia
        Usuario us = usFacade.getByProvincia(entity.getIdProvinciaGt());  
        // verifico si edito o inserto
        if(id == null){
            // instancio el Vehículo local para su persistencia
            veh = new Vehiculo();
            veh.setFechaAlta(entity.getFechaAlta());
            veh.setIdProvinciaGt(entity.getIdProvinciaGt());
            veh.setProvinciaGestion(entity.getProvinciaGestion());
            // instancio la matrícula, que solo vendrá si estoy creando
            veh.setMatricula(entity.getMatricula());
        }else{
            veh = vehiculoFacade.find(id);
        }
        
        // obtengo el modelo y lo seteo
        Modelo mod = modeloFacade.find(entity.getModelo().getId());
        veh.setModelo(mod);
        // obtengo el Titular y lo seteo
        Persona titular = personaFacade.find(entity.getIdTitular());
        veh.setEmpresa(titular);
        // seteo el año
        veh.setAnio(entity.getAnio());
        // completo
        veh.setUsuario(us);
        veh.setHabilitado(true);
        veh.setStrUsuario(us.getStrUsuario()); 
        
        return veh;
    }
}
