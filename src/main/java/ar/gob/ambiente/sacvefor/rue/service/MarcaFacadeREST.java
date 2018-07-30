
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.annotation.Secured;
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
     * @api {post} /vehiculos_marcas Registrar una marca
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X POST -d [PATH_SERVER]/rue/rest/vehiculos_marcas -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PostMarca
     * @apiGroup Marca
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.Marca} entity Objeto java del paquete paqRue.jar con los datos de la marca a registrar
     * @apiParamExample {java} Ejemplo de Marca
     *      {"entity": {
     *          "id": "0",
     *          "nombre": "FIAT"
     *          }
     *      }
     * @apiDescription Método para registrar una nueva Marca de Vehículo. Instancia una entidad a persistir Marca local y la crea mediante el método local create(Marca marca) 
     * @apiSuccess {String} Location url de acceso mediante GET a la Marca registrada.
     * @apiSuccessExample Response exitosa:
     *     HTTP/1.1 201 OK
     *     {
     *       {
     *          "Location": "[PATH_SERVER]/rue/rest/vehiculos_marcas/:id"
     *       }
     *     }
     *
     * @apiError MarcaNoRegistrada No se registró la Marca.
     * @apiErrorExample Respuesta de Error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "Hubo un error procesando la inserción en el Registro Unico"
     *     }
     */            
    @POST
    @Secured
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
            return Response.status(400).entity("Hubo un error procesando la inserción en el Registro Unico. " + ex.getMessage()).build();
        }
    }

    /**
     * @api {put} /vehiculos_marcas/:id Actualizar una marca existente
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X PUT -d [PATH_SERVER]/rue/rest/vehiculos_marcas/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PutMarca
     * @apiGroup Marca
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.Marca} entity Objeto java del paquete paqRue.jar con los datos de la marca a actualizar
     * @apiParam {Long} Id Identificador único de la Marca a actualizar
     * @apiParamExample {java} Ejemplo de Marca
     *      {"entity": {
     *          "id": "2",
     *          "nombre": "TOYOTA"
     *          }
     *      }
     * @apiParamExample {json} Emplo de id
     *      {
     *          "id": "2"
     *      }
     * @apiDescription Método para actualizar una Marca de Vehículo existente. Obtiene la Marca correspondiente al id recibido 
     * mediante el método local find(Long id), actualiza sus datos según los de la entidad recibida y la edita mediante 
     * el método local edit(Marca marca).
     * @apiSuccessExample Response exitosa:
     *     HTTP/1.1 200 OK
     *     {}
     * @apiError MarcaNoActualizada No se actualizó la Marca.
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

    @DELETE
    @Path("{id}")
    @Secured
    public void remove(@PathParam("id") Long id) {
        marcaFacade.remove(marcaFacade.find(id));
    }

    /**
     * @api {get} /vehiculos_marcas/:id Ver una Marca
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/vehiculos_marcas/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetMarca
     * @apiGroup Marca
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único de la Marca
     * @apiDescription Método para obtener una Marca de un Vehículo existente según el id remitido.
     * Obtiene la marca mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Marca} Marca Detalle de la marca registrada.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       {
     *          "id": "2",
     *          "nombre": "TOYOTA"
     *       }
     *     }
     * @apiError MarcaNotFound No existe marca registrada con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay marca registrada con el id recibido"
     *     }
     */       
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Marca find(@PathParam("id") Long id) {
        return marcaFacade.find(id);
    }

    /**
     * @api {get} /vehiculos_marcas Ver todas las Marcas
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/vehiculos_marcas -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetMarcas
     * @apiGroup Marca
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de las Marcas existentes.
     * Obtiene las marcas mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Marca} Marcas Listado con todas las Marcas registradas.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "marcas": [
     *          {"id": "2",
     *          "nombre": "TOYOTA"},
     *          {"id": "3",
     *          "nombre": "BEDFORD"}
     *          ]
     *     }
     * @apiError MarcasNotFound No existen marcas registradas.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay Marcas registradas"
     *     }
     */         
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Marca> findAll() {
        return marcaFacade.findAll();
    }

    /**
     * @api {get} /vehiculos_marcas/query?name=:name Ver Marca según su nombre
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/vehiculos_marcas/query?name=TOYOTA -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetMarcaQuery
     * @apiGroup Marca
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     }
     * @apiParam {String} name Nombre de la Marca
     * @apiDescription Método para obtener una Marca según su nombre.
     * Obtiene una marca mediante el método local getExistente(String name)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Marca} Marca Detalle de la marca registrada.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       {
     *          "id": "2",
     *          "nombre": "TOYOTA"
     *       }
     *     }
     * @apiError MarcaNotFound No existe marca registrada con ese nombre.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay marca registrada con el nombre recibido"
     *     }
     */       
    @GET
    @Path("/query")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Marca findByName(@QueryParam("name") String name) {
        return marcaFacade.getExistente(name);
    }  

    /**
     * @api {get} /vehiculos_marcas/:id/modelos Ver los Modelos de una Marca
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/vehiculos_marcas/2/modelos -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetModelos
     * @apiGroup Marca
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador de la Marca cuyos Modelos se quiere obtener
     * @apiDescription Método para obtener los Modelos asociados a una Marca existente según el id remitido.
     * Obtiene los modelos mediante el método local getModelos(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Modelo} Modelo Listado de los Modelos registrados vinculados a la Marca cuyo id se recibió.
     * @apiSuccessExample Respuesta existosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "Modelos": [
     *          {"id": "3",
     *          "nombre": "4 RUNNER",
     *          "marca": 
     *              {
     *                  "id": "2",
     *                  "nombre": "TOYOTA"
     *              }
     *          }
     *       ]
     *     }
     *
     * @apiError ModelosNotFound No existen modelos registrados vinculados a la id de la marca.
     *
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay modelos registrados vinculados al id de la marca recibida."
     *     }
     */           
    @GET
    @Path("{id}/modelos")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Modelo> findModelosByMarca(@PathParam("id") Long id){
        return marcaFacade.getModelos(id);
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Marca> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return marcaFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(marcaFacade.count());
    }
}
