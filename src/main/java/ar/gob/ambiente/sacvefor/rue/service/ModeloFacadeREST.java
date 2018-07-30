
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.annotation.Secured;
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
     * @api {post} /vehiculos_modelos Registrar un Modelo
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X POST -d [PATH_SERVER]/rue/rest/vehiculos_modelos -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PostModelo
     * @apiGroup Modelo
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.Modelo} entity Objeto java del paquete paqRue.jar con los datos del modelo a registrar
     * @apiParamExample {java} Ejemplo de Modelo
     *      {"entity": {
     *          "id": "0",
     *          "nombre": "4 RUNNER",
     *          "marca": {
     *                  "id": "2",
     *                  "nombre": "TOYOTA"
     *              }
     *          }
     *      }
     * @apiDescription Método para registrar un nuevo Modelo de una Marca de vehículos. Instancia una entidad a persistir Modelo local y la crea mediante el método local create(Modelo modelo) 
     * @apiSuccess {String} Location url de acceso mediante GET a la Modelo registrado.
     * @apiSuccessExample Resuesta exitosa:
     *     HTTP/1.1 201 OK
     *     {
     *       {
     *          "Location": "[PATH_SERVER]/rue/rest/vehiculos_modelos/:id"
     *       }
     *     }
     *
     * @apiError ModeloNoRegistrado No se registró el Modelo.
     * @apiErrorExample Resuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "Hubo un error procesando la inserción en el Registro Unico"
     *     }
     */          
    @POST
    @Secured
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
     * @api {put} /vehiculos_modelos/:id Actualizar un Modelo existente
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X PUT -d [PATH_SERVER]/rue/rest/vehiculos_modelos/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PutModelos
     * @apiGroup Modelo
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.Modelo} entity Objeto java del paquete paqRue.jar con los datos del modelo a actualizar
     * @apiParam {Long} Id Identificador único del Modelo a actualizar
     * @apiParamExample {java} Ejemplo de Modelo
     *      {"entity": {
     *          "id": "3",
     *          "nombre": "4 RUNNER",
     *          "marca": {
     *                  "id": "2",
     *                  "nombre": "TOYOTA"
     *              }
     *          }
     *      }
     * @apiParamExample {json} Emplo de id
     *      {
     *          "id": "2"
     *      }
     * @apiDescription Método para actualizar un Modelo de una Marca de vehículos existente. Obtiene el Modelo correspondiente al id recibido 
     * mediante el método local find(Long id), actualiza sus datos según los de la entidad recibida y la edita mediante 
     * el método local edit(Modelo modelo).
     * @apiSuccessExample Resuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {}
     * @apiError MarcaNoActualizada No se actualizó el Modelo.
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

    @DELETE
    @Path("{id}")
    @Secured
    public void remove(@PathParam("id") Long id) {
        modeloFacade.remove(modeloFacade.find(id));
    }

    /**
     * @api {get} /vehiculos_modelos/:id Ver un Modelo
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/vehiculos_modelos/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetModelo
     * @apiGroup Modelo
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único del Modelo
     * @apiDescription Método para obtener un Modelo de una Marca de Vehículo existente según el id remitido.
     * Obtiene el modelo mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Modelo} Modelo Detalle del modelo registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *      {
     *          "id": "3",
     *          "nombre": "4 RUNNER",
     *          "marca": {
     *                  "id": "2",
     *                  "nombre": "TOYOTA"
     *              }
     *      }
     * @apiError ModeloNotFound No existe modelo registrado con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay modelo registrado con el id recibido"
     *     }
     */     
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Modelo find(@PathParam("id") Long id) {
        return modeloFacade.find(id);
    }
    
    /**
     * @api {get} /vehiculos_modelos/query?name=:name Ver Modelo según su nombre
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/vehiculos_modelos/query?name=4 RUNNER -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetModeloQuery
     * @apiGroup Modelo
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     }
     * @apiParam {String} name Nombre del Modelo
     * @apiDescription Método para obtener una Modelo según su nombre.
     * Obtiene un modelo mediante el método local getExistente(String name)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Modelo} Modelo Detalle del modelo registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *      {
     *          "id": "3",
     *          "nombre": "4 RUNNER",
     *          "marca": {
     *                  "id": "2",
     *                  "nombre": "TOYOTA"
     *              }
     *      }
     * @apiError ModeloNotFound No existe modelo registrado con ese nombre.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay modelo registrado con el nombre recibido"
     *     }
     */   
    @GET
    @Path("/query")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Modelo findByName(@QueryParam("name") String name) {
        return modeloFacade.getExistente(name);
    }      
    
    /**
     * @api {get} /vehiculos_modelos/:id/vehiculos Ver los Vehículos de un Modelo determinado
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/vehiculos_modelos/2/vehiculos -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetVehiculos
     * @apiGroup Modelo
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador del Modelo cuyos Vehículos se quiere obtener
     * @apiDescription Método para obtener los Vehículos asociados a un Modelo existente según el id remitido.
     * Obtiene los vehículos mediante el método local getVehiculos(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Vehiculo} Vehiculo Listado de los Vehículos registrados vinculados al Modelo cuyo id se recibió.
     * @apiSuccessExample Respuesta existosa:
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
     * @apiError ModelosNotFound No existen vehículos registrados vinculados a la id del modelo.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay vehículos registrados vinculados al id del modelo recibido."
     *     }
     */  
    @GET
    @Path("{id}/vehiculos")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findVehiculosByModelo(@PathParam("id") Long id){
        return modeloFacade.getVehiculos(id);
    }    

    /**
     * @api {get} /vehiculos_modelos Ver todos los Modelos
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/vehiculos_modelos -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetModelos
     * @apiGroup Modelo
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de los Modelos existentes.
     * Obtiene los modelos mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Modelo} Modelo Listado con todas los Modelos registrados.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "modelos": [
     *          {
     *              "id": "3",
     *              "nombre": "4 RUNNER",
     *              "marca": {
     *                      "id": "2",
     *                      "nombre": "TOYOTA"
     *                  }
     *          },
     *          {
     *              "id": "7",
     *              "nombre": "VIEJO",
     *              "marca": {
     *                      "id": "4",
     *                      "nombre": "VIEJO"
     *                  }
     *          }
     *        ]
     *     }
     * @apiError MarcasNotFound No existen modelos registrados.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay Modelos registrados"
     *     }
     */    
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Modelo> findAll() {
        return modeloFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Modelo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return modeloFacade.findRange(new int[]{from, to});
    }
  
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(modeloFacade.count());
    }
}
