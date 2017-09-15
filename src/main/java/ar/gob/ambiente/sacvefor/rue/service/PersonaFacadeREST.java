
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.enitites.Domicilio;
import ar.gob.ambiente.sacvefor.rue.enitites.Persona;
import ar.gob.ambiente.sacvefor.rue.enitites.TipoEntidad;
import ar.gob.ambiente.sacvefor.rue.enitites.TipoSociedad;
import ar.gob.ambiente.sacvefor.rue.enitites.Usuario;
import ar.gob.ambiente.sacvefor.rue.facades.PersonaFacade;
import ar.gob.ambiente.sacvefor.rue.facades.UsuarioFacade;
import ar.gob.ambiente.sacvefor.rue.tipos.TipoPersona;
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
 * Servicio que implementa los métodos expuestos por la API REST para la entidad Persona
 * @author rincostante
 */
@Stateless
@Path("personas")
public class PersonaFacadeREST {

    @EJB
    private PersonaFacade personaFacade; 
    @EJB
    private UsuarioFacade usFacade;
    
    @Context
    UriInfo uriInfo;    

    /**
     * Método para crear una Persona.
     * Este método se podrá ejecutar si previamente findByQuery() con el cuit como parámentro, devuelve nulo
     * @param entity: La Persona a persistir proveniente del paqRue compartido
     * @return : la uri de acceso a la Persona generada
     */    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(ar.gob.ambiente.sacvefor.servicios.rue.Persona entity) {
        // instancio la entidad
        Persona per = instanciarPersona(entity, null);

        // persisto la entidad instanciada
        try{
            personaFacade.create(per);
            // armo la respuesta exitosa
            UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
            URI uri = uriBuilder.path(per.getId().toString()).build();
            return Response.created(uri).build();
        }catch(IllegalArgumentException | UriBuilderException ex){
            // armo la respuesta de error
            return Response.status(400).entity("Hubo un error procesado la inserción en el Registro Unico. " + ex.getMessage()).build();
        }
    }

    /**
     * Método para editar una Persona existente
     * Este método se podrá ejecutar si findByQuery() con el cuit como parámentro, previamente devuelve nulo o
     * una Persona con el mismo id de la que se está editando
     * @param id : id de la Persona a actualizar
     * @param entity : La Persona a actualizar proveniente del paqRue compartido
     * @return 
     */    
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") Long id, ar.gob.ambiente.sacvefor.servicios.rue.Persona entity) {
        // instancio la entidad
        Persona per = instanciarPersona(entity, id);

        // persisto la entidad instanciada
        try{
            personaFacade.edit(per);
            // armo la respuesta exitosa
            return Response.ok().build();
        }catch(IllegalArgumentException | UriBuilderException ex){
            // armo la respuesta de error
            return Response.notModified().entity("Hubo un error procesado la actualización en el Registro Unico. " + ex.getMessage()).build();
        }
    }

    /**
     * Método para obtener la Persona correspondiente al id recibido
     * Ej: [PATH]/personas/1
     * @param id: id de la Marca a obtener
     * @return
     */    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Persona find(@PathParam("id") Long id) {
        return personaFacade.find(id);
    }

    /**
     * Método que retorn todas las Personas registradas
     * Ej: [PATH]/personas
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findAll() {
        return personaFacade.findAll();
    }
    
    /**
     * Método que, según los parámetros recibidos ejecuta uno u otro método
     * @param cuit: con el CUIT devuelve un listado con la Persona correspondiente al CUIT ingresado:
     * Ej: [PATH]/personas/query?cuit=20339210315
     * @param hab: con hab en true, si tiene también 'tipo' con algún valor trae las Personas habilitadas del tipo que corresponda:
     * Ej: [PATH]/personas/query?hab=true&tipo=FISICA
     * @param tipo: con el parámetro tipo, devuelve todas las Personas (habilitadas o no), del Tipo recibido como parámetro.
     * Ej: [PATH]/personas/query?tipo=FISICA
     * @return 
     */    
    @GET
    @Path("/query")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findByQuery(@QueryParam("cuit") Long cuit, @QueryParam("hab") boolean hab, @QueryParam("tipo") String tipo) {
        List<Persona> result = new ArrayList<>();
        if(cuit != null){
            Persona per = personaFacade.getExistente(cuit);
            if(per != null){
                result.add(per);
            }
        }else if(hab){
            if(tipo != null){
                if(tipo.equals("FISICA")){
                    result = personaFacade.getFisicasHab();
                }else{
                    result = personaFacade.getJuridicasHab();
                }
            }else{
                result = personaFacade.getHabilitadas();
            }
        }else if(tipo != null){
            if(tipo.equals("FISICA")){
                result = personaFacade.getFisicas();
            }else{
                result = personaFacade.getJuridicas();
            }
        }
        
        return result;
    }

    /**
     * Método que obtiene un listado de Personas cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/personas/1/10
     * @param from: parámetro 'desde' el cual se inicia el listado
     * @param to: parámetro 'hasta' el cual se completa el listado
     * @return 
     */    
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return personaFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de las Personas registradas
     * Ej: [PATH]/personas/count
     * @return 
     */    
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(personaFacade.count());
    }

    /**
     * Método que instancia una Persona (Entity) con los datos de la Persona (POJO) obtenida.
     * Sea para editar o para insertar
     * @param entity 
     */
    private Persona instanciarPersona(ar.gob.ambiente.sacvefor.servicios.rue.Persona entity, Long id) {
        Persona per;
        // obtengo el usuario de la provincia
        Usuario us = usFacade.getByProvincia(entity.getIdProvinciaGt());        
        // verifico si edito o inserto
        if(id == null){
            // instancio la Persona local para su persistencia
            per = new Persona();
            per.setFechaAlta(entity.getFechaAlta());
            per.setIdProvinciaGt(entity.getIdProvinciaGt());
            per.setProvinciaGestion(entity.getProvinciaGestion());
            
            // instancio el tipo de persona
            if("Persona Física".equals(entity.getTipo().getNombre())){
                per.setTipo(TipoPersona.FISICA);
                per.setNombreCompleto(entity.getNombreCompleto());
            }else{
                per.setTipo(TipoPersona.JURIDICA);
                TipoSociedad tipoSoc = new TipoSociedad();
                tipoSoc.setId(entity.getTipoSociedad().getId());
                tipoSoc.setNombre(entity.getTipoSociedad().getNombre());
                per.setTipoSociedad(tipoSoc);
                per.setRazonSocial(entity.getRazonSocial());
            }
            
            // si viene con un domicilio seteado, lo asigno a la Persona
            if(entity.getDomicilio() != null){
                per = setearDomicilio(entity, us, per);
            }
            
        }else{
            // edito la persona existente
            per = personaFacade.find(id);
            // seteo nombre o razón social según corresponda
            if(per.getTipo().toString().equals("Persona Física")){
                per.setNombreCompleto(entity.getNombreCompleto());
            }else{
                per.setRazonSocial(entity.getRazonSocial());
                // en este caso, también seteo el Tipo de Sociedad
                TipoSociedad tipoSoc = new TipoSociedad();
                tipoSoc.setId(entity.getTipoSociedad().getId());
                tipoSoc.setNombre(entity.getTipoSociedad().getNombre());
                per.setTipoSociedad(tipoSoc);
            }
            
            // vrifico si tenía un domicilio previamente persistido
            if(per.getDomicilio() != null){
                // si tenía un Domicilio previamente
                if(entity.getDomicilio() == null){
                    // si viene vacío, seteo en null el persistido
                    per.setDomicilio(null);
                }else{
                    // si viene completo lo seteo
                    per = setearDomicilio(entity, us, per);
                }
            }else{
                // si no tenía un Domicilio previamente
                if(entity.getDomicilio() != null){
                    // si viene completo lo seteo
                    per = setearDomicilio(entity, us, per);
                }
            }
        }
        
        // continúo con el seteo
        per.setCorreoElectronico(entity.getCorreoElectronico());
        per.setCuit(entity.getCuit());

        // instancio el Tipo de Entidad
        TipoEntidad tipoEnt = new TipoEntidad();
        tipoEnt.setId(entity.getEntidad().getId());
        tipoEnt.setNombre(entity.getEntidad().getNombre());
        per.setEntidad(tipoEnt);
        
        // completo
        per.setUsuario(us);
        per.setHabilitado(true);
        per.setStrUsuario(us.getStrUsuario());        
        return per;
    }

    /**
     * Método para setear un Domicilio según la Persona tenga o no tenga Domicilio seteado.
     * Si no tiene un Domicilio, lo setea y lo asigna a la Persona aue recibe como Parámetro.
     * Si ya tiene, resetea el Domicilio existente con los datos recibidos en la entity.
     * @param entity : entidad del paquete de la API-RUE Persona que recibión desde el CGL
     * @param us : Usuario que registra la operación
     * @param per : Persona que se insertará o editará, a la cual se le asigna el domicilio.
     * @return : la Persona con el Domicilio asignado
     */
    private Persona setearDomicilio(ar.gob.ambiente.sacvefor.servicios.rue.Persona entity, Usuario us, Persona per) {
        if(per.getDomicilio() == null){
            Domicilio dom = new Domicilio();
            dom.setCalle(entity.getDomicilio().getCalle());
            dom.setDepartamento(entity.getDomicilio().getDepartamento());
            dom.setDepto(entity.getDomicilio().getDepto());
            dom.setIdLocalidadGt(entity.getDomicilio().getIdLocalidadGt());
            dom.setLocalidad(entity.getDomicilio().getLocalidad());
            dom.setNumero(entity.getDomicilio().getNumero());
            dom.setPiso(entity.getDomicilio().getPiso());
            dom.setProvincia(entity.getDomicilio().getProvincia());
            dom.setUsuario(us);
            dom.setStrUsuario(us.getStrUsuario());
            // asigno el domicilio
            per.setDomicilio(dom);
        }else{
            per.getDomicilio().setCalle(entity.getDomicilio().getCalle());
            per.getDomicilio().setDepartamento(entity.getDomicilio().getDepartamento());
            per.getDomicilio().setDepto(entity.getDomicilio().getDepto());
            per.getDomicilio().setIdLocalidadGt(entity.getDomicilio().getIdLocalidadGt());
            per.getDomicilio().setLocalidad(entity.getDomicilio().getLocalidad());
            per.getDomicilio().setNumero(entity.getDomicilio().getNumero());
            per.getDomicilio().setPiso(entity.getDomicilio().getPiso());
            per.getDomicilio().setProvincia(entity.getDomicilio().getProvincia());
            per.getDomicilio().setUsuario(us);
            per.getDomicilio().setStrUsuario(us.getStrUsuario());
        }
        return per;
    }
}
