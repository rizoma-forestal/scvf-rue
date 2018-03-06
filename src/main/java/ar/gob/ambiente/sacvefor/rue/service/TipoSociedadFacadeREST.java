
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.annotation.Secured;
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
     * @api {post} /tiposociedad Registrar un Tipo de Sociedad
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X POST -d [PATH_SERVER]/rue/rest/tiposociedad -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PostTipoSociedad
     * @apiGroup TipoSociedad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.TipoSociedad} entity Objeto java del paquete paqRue.jar con los datos del Tipo de Sociedad a registrar
     * @apiParamExample {java} Ejemplo de Tipo de Sociedad
     *      {"entity": {
     *              "id": "1",
     *              "nombre": "SOCIEDAD ANONIMA",
     *              "sigla": "SA"
     *          }
     *      }
     * @apiDescription Método para registrar un nuevo Tipo de Sociedad. Instancia una entidad a persistir TipoSociedad local y la crea mediante el método local create(TipoSociedad tipo) 
     * @apiSuccess {String} Location url de acceso mediante GET al TipoSociedad registrado.
     * @apiSuccessExample Response exitosa:
     *     HTTP/1.1 201 OK
     *     {
     *       {
     *          "Location": "[PATH_SERVER]/rue/rest/tiposociedad/:id"
     *       }
     *     }
     *
     * @apiError TipoSociedadNoRegistrado No se registró el Tipo de Sociedad.
     * @apiErrorExample Respuesta de Error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "Hubo un error procesando la inserción en el Registro Unico"
     *     }
     */        
    @POST
    @Secured
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(TipoSociedad entity) {
        tipoSocFacade.create(entity);
    }

    /**
     * @api {put} /tiposociedad/:id Actualizar un Tipo de Sociedad existente
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X PUT -d [PATH_SERVER]/rue/rest/tiposociedad/3 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PutTipoSociedad
     * @apiGroup TipoSociedad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.rue.TipoSociedad} entity Objeto java del paquete paqRue.jar con los datos del Tipo de Sociedad a actualizar
     * @apiParam {Long} Id Identificador único del Tipo de Sociedad a actualizar
     * @apiParamExample {java} Ejemplo de TipoSociedad
     *      {"entity": {
     *          "id": "2",
     *          "nombre": "SOCIEDAD DE RESPONSABILIDAD LIMITADA",
     *          "sigla": "SRL"
     *          }
     *      }
     * @apiParamExample {json} Emplo de id
     *      {
     *          "id": "3"
     *      }
     * @apiDescription Método para actualizar un Tipo de Sociedad existente. Obtiene el Tipo de Sociedad correspondiente al id recibido 
     * mediante el método local find(Long id), actualiza sus datos según los de la entidad recibida y la edita mediante 
     * el método local edit(TipoSociedad tipo).
     * @apiSuccessExample Response exitosa:
     *     HTTP/1.1 200 OK
     *     {}
     * @apiError MarcaNoActualizada No se actualizó el Tipo de Sociedad.
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
    public void edit(@PathParam("id") Long id, TipoSociedad entity) {
        tipoSocFacade.edit(entity);
    }

    @DELETE
    @Path("{id}")
    @Secured
    public void remove(@PathParam("id") Long id) {
        tipoSocFacade.remove(tipoSocFacade.find(id));
    }

    /**
     * @api {get} /tiposociedad/:id Ver un Tipo de Sociedad
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/tiposociedad/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetTipoSociedad
     * @apiGroup TipoSociedad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único del Tipo de Sociedad
     * @apiDescription Método para obtener un Tipo de Sociedad existente según el id remitido.
     * Obtiene el Tipo de Sociedad mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.TipoSociedad} TipoSociedad Detalle del tipo de sociedad registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": "2",
     *          "nombre": "SOCIEDAD DE RESPONSABILIDAD LIMITADA",
     *          "sigla": "SRL"
     *     }
     * @apiError MarcaNotFound No existe tipod de sociedad registrado con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay tipo de sociedad registrado con el id recibido"
     *     }
     */        
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TipoSociedad find(@PathParam("id") Long id) {
        return tipoSocFacade.find(id);
    }

    /**
     * @api {get} /tiposociedad Ver todos los Tipos de Sociedad
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/tiposociedad -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetTipoSociedad
     * @apiGroup TipoSociedad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de los Tipos de Sociedad existentes.
     * Obtiene las marcas mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.TipoSociedad} TipoSociedad Listado con todos los Tipos de Sociedad registrados.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "tiposociedad": [
     *          {"id": "1",
     *          "nombre": "SOCIEDAD ANONIMA",
     *          "sigla": "SA"},
     *          {"id": "2",
     *          "nombre": "SOCIEDAD DE RESPONSABILIDAD LIMITADA",
     *          "sigla": "SRL"},
     *          {"id": "3",
     *          "nombre": "SOCIEDAD DE HECHO",
     *          "sigla": "SH"}
     *          ]
     *     }
     * @apiError MarcasNotFound No existen Tipos de Sociedad registrados.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay Tipos de Sociedad registrados"
     *     }
     */       
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoSociedad> findAll() {
        return tipoSocFacade.findAll();
    }

    /**
     * @api {get} /tiposociedad/query?name=:name Ver Tipo de Sociedad según su nombre
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/tiposociedad/query?name=SOCIEDAD ANONIMA -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetTipoSociedadQuery
     * @apiGroup TipoSociedad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     }
     * @apiParam {String} name Nombre del Tipo de Sociedad
     * @apiDescription Método para obtener un Tipo de Sociedad según su nombre.
     * Obtiene una marca mediante el método local getExistente(String name)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.TipoSociedad} TipoSociedad Detalle del Tipo de Sociedad registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       {"id": "1",
     *          "nombre": "SOCIEDAD ANONIMA",
     *          "sigla": "SA"}
     *     }
     * @apiError MarcaNotFound No existe Tipo de Sociedad registrado con ese nombre.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay Tipo de Sociedad registrado con el nombre recibido"
     *     }
     */     
    @GET
    @Path("/query")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TipoSociedad findByName(@QueryParam("name") String name) {
        return tipoSocFacade.getExistente(name);
    }       

    /**
     * @api {get} /tiposociedad/:id/personas Ver las Personas jurídicas según el Tipo de Sociedad
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/rue/rest/tiposociedad/2/personas -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetPersonas
     * @apiGroup TipoSociedad
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador dl Tipo de Sociedad cuyas Personas jurídicas se quiere obtener
     * @apiDescription Método para obtener las Personas jurídicas cuyo rol es el Tipo de Sociedad existente según el id remitido.
     * Obtiene las personas mediante el método local findPersonasByEntidad(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.rue.Persona} Persona Listado de las Personas jurídicas registradas vinculadas al Tipo de Sociedad cuyo id se recibió.
     * @apiSuccessExample Respuesta existosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "Personas": [
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
     *          },
     *          {"id": "16",
     *          "correoelectronico": "persona_3@correo.com",
     *          "cuit": "27031900000",
     *          "fechaalta": "23-11-2017",
     *          "habilitado": "true",
     *          "idprovinciagt": "17",
     *          "nombrecompleto": "",
     *          "provinciagestion": "SALTA",
     *          "razonsocial": "G. F. CIGARRA   ",
     *          "strusuario": "ADMINISTRADOR CENTRAL",
     *          "tipo": "JURIDICA",
     *          "domicilio":
     *              {
     *                  "id": "9",
     *                  "calle": "JURAMENTO",
     *                  "departamento": "CAPITAL",
     *                  "depto": "of. D",
     *                  "idlocalidadgt": "1877",
     *                  "localidad": "SALTA - CIUDAD",
     *                  "numero": "111",
     *                  "piso": "1",
     *                  "provincia": "SALTA",
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
     * @apiError ModelosNotFound No existen personas jurídicas registradas vinculadas a la id del Tipo de Sociedad.
     *
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay personas jurídicas registradas vinculadas al id del Tipo de Sociedad recibido."
     *     }
     */     
    @GET
    @Path("{id}/personas")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findPersonasByEntidad(@PathParam("id") Long id){
        return tipoSocFacade.getPersonas(id);
    }          
 
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoSociedad> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return tipoSocFacade.findRange(new int[]{from, to});
    }
 
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(tipoSocFacade.count());
    }
}
