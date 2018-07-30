
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.annotation.Secured;
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
 * El Tipo de Entidad se refiere, fundamentalmente, al rol de la Persona registrada
 * @author rincostante
 */
@Stateless
@Path("tipoentidad")
public class TipoEntidadFacadeREST {

    @EJB
    private TipoEntidadFacade tipoEntFacade;

    /**
     * @api {post} /tipoentidad Registrar un Tipo de Entidad
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X POST -d [PATH_SERVER]/rue/rest/tipoentidad -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PostTipoEntidad
     * @apiGroup TipoEntidad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.TipoEntidad} entity Objeto java del paquete paqRue.jar con los datos del Tipo de Entidad a registrar
     * @apiParamExample {java} Ejemplo de Tipo de Entidad
     *      {"entity": {
     *          "id": "0",
     *          "nombre": "PRODUCTOR"
     *          }
     *      }
     * @apiDescription Método para registrar un nuevo Tipo de Entidad. Instancia una entidad a persistir TipoEntidad local y la crea mediante el método local create(TipoEntidad tipo) 
     * @apiSuccess {String} Location url de acceso mediante GET al TipoEntidad registrada.
     * @apiSuccessExample Response exitosa:
     *     HTTP/1.1 201 OK
     *     {
     *       {
     *          "Location": "[PATH_SERVER]/rue/rest/tipoentidad/:id"
     *       }
     *     }
     *
     * @apiError TipoEntidadNoRegistrado No se registró el Tipo de Entidad.
     * @apiErrorExample Respuesta de Error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "Hubo un error procesando la inserción en el Registro Unico"
     *     }
     */      
    @POST
    @Secured
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(TipoEntidad entity) {
        tipoEntFacade.create(entity);
    }

    /**
     * @api {put} /tipoentidad/:id Actualizar un Tipo de Entidad existente
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X PUT -d [PATH_SERVER]/rue/rest/tipoentidad/3 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PutTipoEntidad
     * @apiGroup TipoEntidad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.TipoEntidad} entity Objeto java del paquete paqRue.jar con los datos del Tipo de Entidad a actualizar
     * @apiParam {Long} Id Identificador único del Tipo de Entidad a actualizar
     * @apiParamExample {java} Ejemplo de TipoEntidad
     *      {"entity": {
     *          "id": "2",
     *          "nombre": "TRANSFORMADOR"
     *          }
     *      }
     * @apiParamExample {json} Emplo de id
     *      {
     *          "id": "3"
     *      }
     * @apiDescription Método para actualizar un Tipo de Entidad existente. Obtiene el Tipo de Entidad correspondiente al id recibido 
     * mediante el método local find(Long id), actualiza sus datos según los de la entidad recibida y la edita mediante 
     * el método local edit(TipoEntidad tipo).
     * @apiSuccessExample Response exitosa:
     *     HTTP/1.1 200 OK
     *     {}
     * @apiError MarcaNoActualizada No se actualizó el Tipo de Entidad.
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
    public void edit(@PathParam("id") Long id, TipoEntidad entity) {
        tipoEntFacade.edit(entity);
    }
  
    @DELETE
    @Path("{id}")
    @Secured
    public void remove(@PathParam("id") Long id) {
        tipoEntFacade.remove(tipoEntFacade.find(id));
    }

    /**
     * @api {get} /tipoentidad/:id Ver un Tipo de Entidad
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/tipoentidad/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetTipoEntidad
     * @apiGroup TipoEntidad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único del Tipo de Entidad
     * @apiDescription Método para obtener un Tipo de Entidad existente según el id remitido.
     * Obtiene el Tipo de Entidad mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.TipoEntidad} TipoEntidad Detalle del tipo de entidad registrada.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       {
     *          "id": "2",
     *          "nombre": "PRODUCTOR"
     *       }
     *     }
     * @apiError MarcaNotFound No existe tipod de entidad registrado con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay tipo de entidad registrado con el id recibido"
     *     }
     */     
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TipoEntidad find(@PathParam("id") Long id) {
        return tipoEntFacade.find(id);
    }

    /**
     * @api {get} /tipoentidad Ver todos los Tipos de Entidad
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/tipoentidad -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetTipoEntidad
     * @apiGroup TipoEntidad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de los Tipos de Entidad existentes.
     * Obtiene las marcas mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.TipoEntidad} TipoEntidad Listado con todos los Tipos de Entidad registrados.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "marcas": [
     *          {"id": "1",
     *          "nombre": "EMPRESA DE TRANSPORTE"},
     *          {"id": "2",
     *          "nombre": "PRODUCTOR"},
     *          {"id": "3",
     *          "nombre": "TRANSFORMADOR"}
     *          ]
     *     }
     * @apiError MarcasNotFound No existen Tipos de Entidad registrados.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay Tipos de Entidad registrados"
     *     }
     */       
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoEntidad> findAll() {
        return tipoEntFacade.findAll();
    }

    /**
     * @api {get} /tipoentidad/query?name=:name Ver Tipo de Entidad según su nombre
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/tipoentidad/query?name=TRANSFORMADOR -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetTipoEntidadQuery
     * @apiGroup TipoEntidad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     }
     * @apiParam {String} name Nombre del Tipo de Entidad
     * @apiDescription Método para obtener un Tipo de Entidad según su nombre.
     * Obtiene una marca mediante el método local getExistente(String name)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.TipoEntidad} TipoEntidad Detalle del Tipo de Entidad registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       {
     *          "id": "3",
     *          "nombre": "TRANSFORMADOR"
     *       }
     *     }
     * @apiError MarcaNotFound No existe Tipo de Entidad registrado con ese nombre.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay Tipo de Entidad registrado con el nombre recibido"
     *     }
     */      
    @GET
    @Path("/query")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TipoEntidad findByName(@QueryParam("name") String name) {
        return tipoEntFacade.getExistente(name);
    }     

    /**
     * @api {get} /tipoentidad/:id/personas Ver las Personas según el Tipo
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/tipoentidad/2/personas -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetPersonas
     * @apiGroup TipoEntidad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador dl Tipo de Entidad cuyas Personas se quiere obtener
     * @apiDescription Método para obtener las Personas cuyo rol es el Tipo de Entidad existente según el id remitido.
     * Obtiene las personas mediante el método local findPersonasByEntidad(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Persona} Persona Listado de las Personas registradas vinculadas al Tipo de Entidad cuyo id se recibió.
     * @apiSuccessExample Respuesta existosa:
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
     *
     * @apiError ModelosNotFound No existen personas registradas vinculadas a la id del Tipo de Entidad.
     *
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay personas registradas vinculadas al id del Tipo de Entidad recibido."
     *     }
     */      
    @GET
    @Path("{id}/personas")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findPersonasByEntidad(@PathParam("id") Long id){
        return tipoEntFacade.getPersonas(id);
    }        

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoEntidad> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return tipoEntFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(tipoEntFacade.count());
    }
}
