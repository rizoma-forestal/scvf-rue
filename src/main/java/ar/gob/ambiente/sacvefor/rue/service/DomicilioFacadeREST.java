
package ar.gob.ambiente.sacvefor.rue.service;

import ar.gob.ambiente.sacvefor.rue.annotation.Secured;
import ar.gob.ambiente.sacvefor.rue.enitites.Domicilio;
import ar.gob.ambiente.sacvefor.rue.facades.DomicilioFacade;
import ar.gob.ambiente.sacvefor.rue.facades.UsuarioFacade;
import java.net.URI;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import javax.ws.rs.core.UriInfo;

/**
 * Servicio que implementa los métodos expuestos por la API REST para la entidad Domicilio
 * Requerido por el CGL para la emisión de guías remotas (Integración con SICMA)
 * En prinicipio solo tendrá el método 'create'
 * @author rincostante
 */
@Stateless
@Path("domicilios")
public class DomicilioFacadeREST {
    @EJB
    private DomicilioFacade domFacade;
    @EJB
    private UsuarioFacade usFacade;
    
    @Context
    UriInfo uriInfo;    

    @POST
    @Secured
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(ar.gob.ambiente.sacvefor.servicios.rue.Domicilio entity) {
        // instancio la entidad
        Domicilio dom = new Domicilio();
        dom.setCalle(entity.getCalle());
        dom.setDepartamento(entity.getDepartamento());
        if(!entity.getDepto().equals("default")){
            dom.setDepto(entity.getDepto());
        }
        dom.setIdLocalidadGt(entity.getIdLocalidadGt());
        dom.setLocalidad(entity.getLocalidad());
        dom.setNumero(entity.getNumero());
        if(!entity.getPiso().equals("default")){
            dom.setPiso(entity.getPiso());
        }
        dom.setProvincia(entity.getProvincia());
        dom.setStrUsuario("ADMINISTRADOR CENTRAL");
        // asigno el usuario de CHACO
        dom.setUsuario(usFacade.getByProvincia(Long.valueOf(6)));
        try{
            // persisto el domicilio
            domFacade.create(dom);
            // armo la respuesta exitosa
            UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
            URI uri = uriBuilder.path(dom.getId().toString()).build();
            return Response.created(uri).build();
        }catch(IllegalArgumentException | UriBuilderException ex){
            // armo la respuesta de error
            return Response.status(400).entity("Hubo un error procesado la inserción del Domicilio en el Registro Unico. " + ex.getMessage()).build();
        }
    }    
}
