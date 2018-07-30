
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.annotation.Secured;
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
     * @api {post} /vehiculos Registrar un Vehículo
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X POST -d [PATH_SERVER]/rue/rest/vehiculos -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PostVehiculo
     * @apiGroup Vehiculos
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.Vehiculo} entity Objeto java del paquete paqRue.jar con los datos del vehículo a registrar
     * @apiParamExample {java} Ejemplo de Vehículo
     *      {"entity": {
     *          "id": "0",
     *          "anio": "1991",
     *          "fechaalta": "17-10-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "22",
     *          "matricula": "ABC-123",
     *          "provinciagestion": "Santiago del Estero",
     *          "usuario": "ADMINISTRADOR CENTRAL"
     *          "modelo": 
     *              {
     *                  "id": "3",
     *                  "nombre": "4 RUNNER",
     *                  "marca": {
     *                          "id": "2",
     *                          "nombre": "TOYOTA"
     *                      }
     *              }
     *          }
     *      }
     * @apiDescription Método para registrar un nuevo Vehiculo. Instancia una entidad a persistir Vehiculo local y la crea mediante el método local create(Modelo modelo) 
     * @apiSuccess {String} Location url de acceso mediante GET a la Vehiculo registrado.
     * @apiSuccessExample Resuesta exitosa:
     *     HTTP/1.1 201 OK
     *     {
     *       {
     *          "Location": "[PATH_SERVER]/rue/rest/vehiculos/:id"
     *       }
     *     }
     *
     * @apiError VehiculoNoRegistrado No se registró el Vehiculo.
     * @apiErrorExample Resuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "Hubo un error procesando la inserción en el Registro Unico"
     *     }
     */     
    @POST
    @Secured
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
     * @api {put} /vehiculos/:id Actualizar un Vehiculo existente
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X PUT -d [PATH_SERVER]/rue/rest/vehiculos/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PutVehiculos
     * @apiGroup Vehiculos
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.Vehiculo} entity Objeto java del paquete paqRue.jar con los datos del vehículos a actualizar
     * @apiParam {Long} Id Identificador único del Vehiculo a actualizar
     * @apiParamExample {java} Ejemplo de Vehiculo
     *      {"entity": {
     *          "id": "2",
     *          "anio": "1993",
     *          "fechaalta": "24-11-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "22",
     *          "matricula": "ABC-125",
     *          "provinciagestion": "Santiago del Estero",
     *          "usuario": "ADMINISTRADOR CENTRAL"
     *          "modelo": 
     *              {
     *                  "id": "7",
     *                  "nombre": "VIEJO",
     *                  "marca": {
     *                          "id": "4",
     *                          "nombre": "BEDFORD"
     *                      }
     *              }
     *          }
     *      }
     * @apiParamExample {json} Emplo de id
     *      {
     *          "id": "2"
     *      }
     * @apiDescription Método para actualizar un Vehículo existente. Obtiene el Vehículo correspondiente al id recibido 
     * mediante el método local find(Long id), actualiza sus datos según los de la entidad recibida y la edita mediante 
     * el método local edit(Vehiculo vehiculo).
     * @apiSuccessExample Resuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {}
     * @apiError MarcaNoActualizada No se actualizó el Vehículo.
     * @apiErrorExample Resuesta de error:
     *     HTTP/1.1 400 Not Modified
     *     {
     *       "error": "Hubo un error procesado la actualización en el Registro Unico."
     *     }
     */           
    @PUT
    @Path("{id}")
    @Secured
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
     * @api {get} /vehiculos/:id Ver un Vehiculo
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/vehiculos/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetVehiculo
     * @apiGroup Vehiculos
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único del Vehículo
     * @apiDescription Método para obtener un Vehículo existente según el id remitido.
     * Obtiene el vehículo mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Vehiculo} Vehiculo Detalle del vehículo registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *      {
     *          "id": "2",
     *          "anio": "1993",
     *          "fechaalta": "24-11-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "22",
     *          "matricula": "ABC-125",
     *          "provinciagestion": "Santiago del Estero",
     *          "usuario": "ADMINISTRADOR CENTRAL"
     *          "modelo": 
     *              {
     *                  "id": "7",
     *                  "nombre": "VIEJO",
     *                  "marca": {
     *                          "id": "4",
     *                          "nombre": "BEDFORD"
     *                      }
     *              }
     *      }
     * @apiError MarcaNotFound No existe vehículo registrado con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay vehículo registrado con el id recibido"
     *     }
     */      
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Vehiculo find(@PathParam("id") Long id) {
        return vehiculoFacade.find(id);
    }

    /**
     * @api {get} /vehiculos Ver todos los Vehículos
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/vehiculos -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetVehiculos
     * @apiGroup Vehiculos
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de los Vehículos existentes.
     * Obtiene los vehículos mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Vehiculo} Vehiculo Listado con todas los Vehículos registrados.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "Vehiculos": [
     *          {"id": "1",
     *          "anio": "1991",
     *          "fechaalta": "17-10-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "22",
     *          "matricula": "ABC-123",
     *          "provinciagestion": "Santiago del Estero",
     *          "usuario": "ADMINISTRADOR CENTRAL"
     *          "modelo": 
     *              {
     *                  "id": "3",
     *                  "nombre": "4 RUNNER",
     *                  "marca": {
     *                          "id": "2",
     *                          "nombre": "TOYOTA"
     *                      }
     *              }
     *          },
     *          {"id": "2",
     *          "anio": "1993",
     *          "fechaalta": "24-11-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "22",
     *          "matricula": "ABC-125",
     *          "provinciagestion": "Santiago del Estero",
     *          "usuario": "ADMINISTRADOR CENTRAL"
     *          "modelo": 
     *              {
     *                  "id": "7",
     *                  "nombre": "VIEJO",
     *                  "marca": {
     *                          "id": "4",
     *                          "nombre": "BEDFORD"
     *                      }
     *              }
     *          }
     *       ]
     *     }
     * @apiError MarcasNotFound No existen vehículos registrados.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay Vehículos registrados"
     *     }
     */   
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findAll() {
        return vehiculoFacade.findAll();
    }

    /**
     * @api {get} /vehiculos_modelos/query?matricula=:matricula,hab=:hab,cuit=:cuit Ver vehículos según parámetros.
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/vehiculos_modelos/query?name=4 RUNNER -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetVehiculoQuery
     * @apiGroup Vehiculos
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     }
     * @apiParam {String} matricula Matrícula del Vehículo
     * @apiParam {String} hab Habilitación del Vehículo
     * @apiParam {String} cuit cuit del Titular del Vehículo
     * @apiDescription Método para obtener una Vehículo según su matrícula o según si está o no habilitado, o según el cuit de su titular.
     * Solo uno de los parámetros tendrá un valor y los restantes nulos.
     * Según el caso, obtiene el o los vehículos en cuestión con los métodos locales getExistente(String matricula), getHabilitadas() o getByTitular(Long cuit)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Vehiculo} Vehículo Detalle del vehículo registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "Vehiculos": [
     *          {"id": "1",
     *          "anio": "1991",
     *          "fechaalta": "17-10-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "22",
     *          "matricula": "ABC-123",
     *          "provinciagestion": "Santiago del Estero",
     *          "usuario": "ADMINISTRADOR CENTRAL"
     *          "modelo": 
     *              {
     *                  "id": "3",
     *                  "nombre": "4 RUNNER",
     *                  "marca": {
     *                          "id": "2",
     *                          "nombre": "TOYOTA"
     *                      }
     *              }
     *          },
     *          {"id": "2",
     *          "anio": "1993",
     *          "fechaalta": "24-11-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "22",
     *          "matricula": "ABC-125",
     *          "provinciagestion": "Santiago del Estero",
     *          "usuario": "ADMINISTRADOR CENTRAL"
     *          "modelo": 
     *              {
     *                  "id": "7",
     *                  "nombre": "VIEJO",
     *                  "marca": {
     *                          "id": "4",
     *                          "nombre": "BEDFORD"
     *                      }
     *              }
     *          }
     *       ]
     *     }
     * @apiError MarcaNotFound No existe vehículo registrado con esos parámetros.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay vehículo registrado con los parámetros recibidos"
     *     }
     */       
    @GET
    @Path("/query")
    @Secured
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
    
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return vehiculoFacade.findRange(new int[]{from, to});
    }
    
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
