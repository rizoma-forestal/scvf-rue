/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.gob.ambiente.sacvefor.rue.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author rincostante
 */
@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ar.gob.ambiente.sacvefor.rue.service.MarcaFacadeREST.class);
        resources.add(ar.gob.ambiente.sacvefor.rue.service.ModeloFacadeREST.class);
        resources.add(ar.gob.ambiente.sacvefor.rue.service.PersonaFacadeREST.class);
        resources.add(ar.gob.ambiente.sacvefor.rue.service.TipoEntidadFacadeREST.class);
        resources.add(ar.gob.ambiente.sacvefor.rue.service.TipoSociedadFacadeREST.class);
        resources.add(ar.gob.ambiente.sacvefor.rue.service.VehiculoFacadeREST.class);
    }
    
}
