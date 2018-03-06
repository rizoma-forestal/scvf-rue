
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.annotation.Secured;
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
     * @api {post} /personas Registrar una Persona
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X POST -d [PATH_SERVER]/rue/rest/personas -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PostPersona
     * @apiGroup Persona
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.Persona} entity Objeto java del paquete paqRue.jar con los datos de la Persona a registrar
     * @apiParamExample {java} Ejemplo de Persona
     *      {"entity":{"id": "0",
     *          "correoelectronico": "persona_2@correo.com",
     *          "cuit": "27031902222",
     *          "fechaalta": "17-10-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "22",
     *          "nombrecompleto": "",
     *          "provinciagestion": "SANTIAGO DEL ESTERO",
     *          "razonsocial": "LAS GRAPIAS S.A.",
     *          "strusuario": "ADMINISTRADOR CENTRAL",
     *          "tipo": "JURIDICA",
     *          "domicilio":
     *              {
     *                  "id": "5",
     *                  "calle": "OTILIAS",
     *                  "departamento": "COMUNA 10",
     *                  "depto": "B",
     *                  "idlocalidadgt": "10835",
     *                  "localidad": "VERSALLES - BARRIO",
     *                  "numero": "569",
     *                  "piso": "3",
     *                  "provincia": "CIUDAD AUTONOMA DE BUENOS AIRES",
     *                  "strusuario": "ADMINISTRADOR CENTRAL"
     *              },
     *          "tipoentidad":
     *              {
     *                  "id": "2",
     *                  "nombre": "PRODUCTOR"
     *              },
     *          "tiposociedad":
     *              {
     *                  "id": "1",
     *                  "nombre": "SOCIEDAD ANONIMA",
     *                  "sigla": "SA"
     *              }
     *          }
     *      }
     * @apiDescription Método para registrar una nueva Persona. Instancia una entidad a persistir Persona local y la crea mediante el método local create(Persona persona) 
     * @apiSuccess {String} Location url de acceso mediante GET al Persona registrada.
     * @apiSuccessExample Response exitosa:
     *     HTTP/1.1 201 OK
     *     {
     *       {
     *          "Location": "[PATH_SERVER]/rue/rest/personas/:id"
     *       }
     *     }
     *
     * @apiError PersonaNoRegistrado No se registró la Persona.
     * @apiErrorExample Respuesta de Error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "Hubo un error procesando la inserción en el Registro Unico"
     *     }
     */        
    @POST
    @Secured
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
     * @api {put} /personas/:id Actualizar una Persona existente
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X PUT -d [PATH_SERVER]/rue/rest/personas/1 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PutPersona
     * @apiGroup Persona
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.Persona} entity Objeto java del paquete paqRue.jar con los datos de la Persona a actualizar
     * @apiParam {Long} Id Identificador único de la Persona a actualizar
     * @apiParamExample {java} Ejemplo de Persona
     *      {"entity": {"id": "1",
     *          "correoelectronico": "persona@correo.com",
     *          "cuit": "27031901111",
     *          "fechaalta": "09-10-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "10",
     *          "nombrecompleto": "TROILO, ANIBAL CARMELO",
     *          "provinciagestion": "JUJUY",
     *          "razonsocial": "",
     *          "strusuario": "ADMINISTRADOR CENTRAL",
     *          "tipo": "FISICA",
     *          "domicilio":
     *              {},
     *          "tipoentidad":
     *              {
     *                  "id": "2",
     *                  "nombre": "PRODUCTOR"
     *              },
     *          "tiposociedad":
     *              {}
     *          }
     *      }
     * @apiParamExample {json} Emplo de id
     *      {
     *          "id": "1"
     *      }
     * @apiDescription Método para actualizar una Persona existente. Obtiene la Persona correspondiente al id recibido 
     * mediante el método local find(Long id), actualiza sus datos según los de la entidad recibida y la edita mediante 
     * el método local edit(Persona persona).
     * @apiSuccessExample Response exitosa:
     *     HTTP/1.1 200 OK
     *     {}
     * @apiError PersonaNoActualizada No se actualizó la Persona.
     * @apiErrorExample Respuesta de Error:
     *     HTTP/1.1 400 Not Modified
     *     {
     *       "error": "Hubo un error procesado la actualización en el Registro Unico."
     *     }
     */       
    @PUT
    @Path("{id}")
    @Secured
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
     * @api {get} /personas/:id Ver una Persona
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/personas/1 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetPersona
     * @apiGroup Persona
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único de la Persona
     * @apiDescription Método para obtener una Persona existente según el id remitido.
     * Obtiene la Persona mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Persona} Persona Detalle de la persona registrada.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *      {
     *          "id": "1",
     *          "correoelectronico": "persona@correo.com",
     *          "cuit": "27031901111",
     *          "fechaalta": "09-10-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "10",
     *          "nombrecompleto": "TROILO, ANIBAL CARMELO",
     *          "provinciagestion": "JUJUY",
     *          "razonsocial": "",
     *          "strusuario": "ADMINISTRADOR CENTRAL",
     *          "tipo": "FISICA",
     *          "domicilio":
     *              {},
     *          "tipoentidad":
     *              {
     *                  "id": "2",
     *                  "nombre": "PRODUCTOR"
     *              },
     *          "tiposociedad":
     *              {}
     *      }
     * @apiError MarcaNotFound No existe persona registrada con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay persona registrada con el id recibido"
     *     }
     */     
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Persona find(@PathParam("id") Long id) {
        return personaFacade.find(id);
    }

    /**
     * @api {get} /personas Ver todas las Personas
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/personas -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetPersonas
     * @apiGroup Persona
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de las Personas existentes.
     * Obtiene las marcas mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Persona} Persona Listado con todos las Personas registradas.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "Personas": [
     *          {"id": "1",
     *          "correoelectronico": "persona@correo.com",
     *          "cuit": "27031901111",
     *          "fechaalta": "09-10-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "10",
     *          "nombrecompleto": "TROILO, ANIBAL CARMELO",
     *          "provinciagestion": "JUJUY",
     *          "razonsocial": "",
     *          "strusuario": "ADMINISTRADOR CENTRAL",
     *          "tipo": "FISICA",
     *          "domicilio":
     *              {},
     *          "tipoentidad":
     *              {
     *                  "id": "2",
     *                  "nombre": "PRODUCTOR"
     *              },
     *          "tiposociedad":
     *              {}
     *          },
     *          {"id": "1",
     *          "correoelectronico": "persona_2@correo.com",
     *          "cuit": "27031902222",
     *          "fechaalta": "17-10-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "22",
     *          "nombrecompleto": "",
     *          "provinciagestion": "SANTIAGO DEL ESTERO",
     *          "razonsocial": "LAS GRAPIAS S.A.",
     *          "strusuario": "ADMINISTRADOR CENTRAL",
     *          "tipo": "JURIDICA",
     *          "domicilio":
     *              {
     *                  "id": "5",
     *                  "calle": "OTILIAS",
     *                  "departamento": "COMUNA 10",
     *                  "depto": "B",
     *                  "idlocalidadgt": "10835",
     *                  "localidad": "VERSALLES - BARRIO",
     *                  "numero": "569",
     *                  "piso": "3",
     *                  "provincia": "CIUDAD AUTONOMA DE BUENOS AIRES",
     *                  "strusuario": "ADMINISTRADOR CENTRAL"
     *              },
     *          "tipoentidad":
     *              {
     *                  "id": "2",
     *                  "nombre": "PRODUCTOR"
     *              },
     *          "tiposociedad":
     *              {
     *                  "id": "1",
     *                  "nombre": "SOCIEDAD ANONIMA",
     *                  "sigla": "SA"
     *              }
     *          }
     *       ]
     *     }
     * @apiError PersonasNotFound No existen Personas registradas.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay Personas registradas"
     *     }
     */         
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findAll() {
        return personaFacade.findAll();
    }

    /**
     * @api {get} /personas/query?cuit=:cuit,hab=:hab,tipo=:tipo Ver Persona según parámetros.
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/personas/query?cuit=27031902222 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetPersonaQuery
     * @apiGroup Persona
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     }
     * @apiParam {String} cuit Cuit de la Persona
     * @apiParam {String} hab Habilitación de la Persona
     * @apiParam {String} tipo tipo de Persona (FISICA o JURIDICA)
     * @apiDescription Método para obtener una Persona según su cuit o según si está o no habilitado, o según el tipo (FISICA o JURIDICA).
     * Solo uno de los parámetros tendrá un valor y los restantes nulos.
     * Según el caso, obtiene el o los vehículos en cuestión con los métodos locales getExistente(Long cuit), getHabilitadas() o getJuridicasHab()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Persona} Persona Detalle de la persona registrada.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "Vehiculos": [
     *          {"id": "1",
     *          "correoelectronico": "persona_2@correo.com",
     *          "cuit": "27031902222",
     *          "fechaalta": "17-10-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "22",
     *          "nombrecompleto": "",
     *          "provinciagestion": "SANTIAGO DEL ESTERO",
     *          "razonsocial": "LAS GRAPIAS S.A.",
     *          "strusuario": "ADMINISTRADOR CENTRAL",
     *          "tipo": "JURIDICA",
     *          "domicilio":
     *              {
     *                  "id": "5",
     *                  "calle": "OTILIAS",
     *                  "departamento": "COMUNA 10",
     *                  "depto": "B",
     *                  "idlocalidadgt": "10835",
     *                  "localidad": "VERSALLES - BARRIO",
     *                  "numero": "569",
     *                  "piso": "3",
     *                  "provincia": "CIUDAD AUTONOMA DE BUENOS AIRES",
     *                  "strusuario": "ADMINISTRADOR CENTRAL"
     *              },
     *          "tipoentidad":
     *              {
     *                  "id": "2",
     *                  "nombre": "PRODUCTOR"
     *              },
     *          "tiposociedad":
     *              {
     *                  "id": "1",
     *                  "nombre": "SOCIEDAD ANONIMA",
     *                  "sigla": "SA"
     *              }
     *          }
     *       ]
     *     }
     * @apiError MarcaNotFound No existe persona registrado con esos parámetros.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay persona registrada con los parámetros recibidos"
     *     }
     */       
    @GET
    @Path("/query")
    @Secured
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

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return personaFacade.findRange(new int[]{from, to});
    }

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
