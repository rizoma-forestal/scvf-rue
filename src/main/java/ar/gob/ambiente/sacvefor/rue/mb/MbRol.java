
package ar.gob.ambiente.sacvefor.rue.mb;

import ar.gob.ambiente.sacvefor.rue.enitites.Rol;
import ar.gob.ambiente.sacvefor.rue.facades.RolFacade;
import ar.gob.ambiente.sacvefor.rue.util.JsfUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Bean de respaldo para la administración de Roles de Usuarios
 * @author rincostante
 */
public class MbRol {

    /**
     * Variable privada: Rol Entidad que se gestiona mediante el bean
     */
    private Rol rol;
    
    /**
     * Variable privada: List<Rol> listado de los Roles registrados que compone la tabla para su gestión
     */
    private List<Rol> lstRoles;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Rol
     */  
    @EJB
    private RolFacade rolFacade;
    
    /**
     * Constructor
     */
    public MbRol() {
    }
    
    /**********************
     * Métodos de acceso **
     **********************/
    public Rol getRol(){
        return rol;
    }     

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Método para obtener los roles registrados y poblar el listado correspondiente
     * @return List<Rol> listado de roles existentes
     */
    public List<Rol> getLstRoles() {
        lstRoles = rolFacade.findAll();
        return lstRoles;
    }

    public void setLstRoles(List<Rol> lstRoles) {    
        this.lstRoles = lstRoles;
    }

    /***********************
     * Mátodos operativos **
     ***********************/
    
    /**
     * Método que se ejecuta luego de instanciada la clase e inicializa la entidad a gestionar
     */    
    @PostConstruct
    public void init() {
        rol = new Rol();
    }
    
    /**
     * Método para guardar el Rol, sea inserción o edición.
     * Previa validación
     */    
    public void saveRol(){
        boolean valida = true;
        try{
            Rol rolExitente = rolFacade.getExistente(rol.getNombre().toUpperCase());
            if(rolExitente != null){
                if(rol.getId() != null){
                    // si edita, no habilito si no es el mismo
                    if(!rolExitente.equals(rol)) valida = false;
                }else{
                    // si no edita no habilito de ninguna manera
                    valida = false;
                }
            }
            if(valida){
                String tempNombre = rol.getNombre();
                rol.setNombre(tempNombre.toUpperCase());
                if(rol.getId() != null){
                    rolFacade.edit(rol);
                    JsfUtil.addSuccessMessage("El Rol fue guardado con exito");
                }else{
                    rolFacade.create(rol);
                    JsfUtil.addSuccessMessage("El Rol fue registrado con exito");
                }   
            }else{
                JsfUtil.addErrorMessage("El Rol que está tratando de persisitir ya existe, por favor verifique los datos ingresados.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error al procesando el Rol: " + ex.getMessage());
        }
    }
    
    /**
     * Método para eliminar el Rol, previa validación
     */    
    public void deleteRol(){
        try{
            if(!rolFacade.esReferenciada(rol)){
                rolFacade.remove(rol);
                JsfUtil.addSuccessMessage("El Rol fue removido con exito");   
            }else{
                JsfUtil.addErrorMessage("El Rol que está tratando de eliminar está referida a algún Usuario, "
                             + "para eliminarlo debe desvincularlo de los Usuarios que hagan referencia a él.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error eliminando el Rol: " + ex.getMessage());
        }
    }
    
    /**
     * Método para limpiar el objeto del formulario
     */    
    public void limpiarForm(){
        rol = new Rol();
    }
    
    /*********************
     * Métodos privados **
     *********************/
    
    /**
     * Método privado que recupera un Rol según su id
     * @param key Long id de la entidad persistida
     * @return Object la entidad correspondiente
     */
    private Object getRol(Long key) {
        return rolFacade.find(key);
    }
    
    
    /***************
    ** Converter  **
    ****************/    
    @FacesConverter(forClass = Rol.class)
    public static class RolConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbRol controller = (MbRol) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbRol");
            return controller.getRol(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Rol) {
                Rol o = (Rol) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Rol.class.getName());
            }
        }
    }       
}
