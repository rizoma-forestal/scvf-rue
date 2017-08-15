
package ar.gob.ambiente.sacvefor.rue.mb;

import ar.gob.ambiente.sacvefor.rue.enitites.TipoEntidad;
import ar.gob.ambiente.sacvefor.rue.facades.TipoEntidadFacade;
import ar.gob.ambiente.sacvefor.rue.util.JsfUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Bean de respaldo para la administración de la paramétrica TipoEntidad
 * @author rincostante
 */
public class MbTipoEntidad {

    private TipoEntidad tipoEntidad;
    private List<TipoEntidad> lstTipoEntidades;
    
    @EJB
    private TipoEntidadFacade tipoEntidadFacade;
    
    public MbTipoEntidad() {
    }
     
    /**********************
     * Métodos de acceso **
     **********************/    
    public TipoEntidad getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(TipoEntidad tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public List<TipoEntidad> getLstTipoEntidades() {
        lstTipoEntidades = tipoEntidadFacade.findAll();
        return lstTipoEntidades;
    }

    public void setLstTipoEntidades(List<TipoEntidad> lstTipoEntidades) {
        this.lstTipoEntidades = lstTipoEntidades;
    }

    /***********************
     * Mátodos operativos **
     ***********************/
    @PostConstruct
    public void init() {
        tipoEntidad = new TipoEntidad();
    }
    
    /**
     * Método para guardar el TipoEntidad, sea inserción o edición.
     * Previa validación
     */    
    public void saveTipoEntidad(){
        boolean valida = true;
        try{
            TipoEntidad tipoEntExistente = tipoEntidadFacade.getExistente(tipoEntidad.getNombre().toUpperCase());
            if(tipoEntExistente != null){
                if(tipoEntidad.getId() != null){
                    // si edita, no habilito si no es el mismo
                    if(!tipoEntExistente.equals(tipoEntidad)) valida = false;
                }else{
                    // si no edita no habilito de ninguna manera
                    valida = false;
                }
            }
            if(valida){
                String tempNombre = tipoEntidad.getNombre();
                tipoEntidad.setNombre(tempNombre.toUpperCase());
                if(tipoEntidad.getId() != null){
                    tipoEntidadFacade.edit(tipoEntidad);
                    JsfUtil.addSuccessMessage("El Tipo de Entidad fue guardado con exito");
                }else{
                    tipoEntidadFacade.create(tipoEntidad);
                    JsfUtil.addSuccessMessage("El Tipo de Entidad fue registrado con exito");
                }   
            }else{
                JsfUtil.addErrorMessage("El Tipo de Entidad que está tratando de persisitir ya existe, por favor verifique los datos ingresados.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error al procesando el Tipo de Entidad: " + ex.getMessage());
        }
    }    
    
    /**
     * Método para eliminar el TipoEntidad, previa validación
     */    
    public void deleteTipoEntidad(){
        try{
            if(!tipoEntidadFacade.esReferenciada(tipoEntidad)){
                tipoEntidadFacade.remove(tipoEntidad);
                JsfUtil.addSuccessMessage("El Tipo de Entidad fue removido con exito");   
            }else{
                JsfUtil.addErrorMessage("El Tipo de Entidad que está tratando de eliminar está referida a alguna Persona, "
                             + "para eliminarlo debe desvincularlo de las Personas que hagan referencia a él.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error eliminando el Tipo de Entidad: " + ex.getMessage());
        }
    }    
    
    /**
     * Método para limpiar el objeto del formulario
     */    
    public void limpiarForm(){
        tipoEntidad = new TipoEntidad();
    }    

    /*********************
     * Métodos privados **
     *********************/     
    private Object getTipoEntidad(Long key) {
        return tipoEntidadFacade.find(key);
    }
 
    
    /********************************
    ** Converter para TipoEntidad  **
    *********************************/    
    @FacesConverter(forClass = TipoEntidad.class)
    public static class TipoEntidadConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbTipoEntidad controller = (MbTipoEntidad) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbTipoEntidad");
            return controller.getTipoEntidad(getKey(value));
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
            if (object instanceof TipoEntidad) {
                TipoEntidad o = (TipoEntidad) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TipoEntidad.class.getName());
            }
        }
    }       
}
