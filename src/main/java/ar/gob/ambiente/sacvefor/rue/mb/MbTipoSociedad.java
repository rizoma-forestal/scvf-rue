
package ar.gob.ambiente.sacvefor.rue.mb;

import ar.gob.ambiente.sacvefor.rue.enitites.TipoSociedad;
import ar.gob.ambiente.sacvefor.rue.facades.TipoSociedadFacade;
import ar.gob.ambiente.sacvefor.rue.util.JsfUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Bean de respaldo para la administración de la paramétrica TipoSociedad
 * @author rincostante
 */
public class MbTipoSociedad {

    /**
     * Variable privada: TipoSociedad Entidad que se gestiona mediante el bean
     */    
    private TipoSociedad tipoSociedad;
    
    /**
     * Variable privada: List<TipoSociedad> listado de los tipos de entidades registrados que compone la tabla para su gestión
     */
    private List<TipoSociedad> lstTipoSociedades;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de TipoSociedad
     */  
    @EJB
    private TipoSociedadFacade tipoSociedadFacade;
    
    /**
     * Constructor
     */
    public MbTipoSociedad() {
    }

    /**********************
     * Métodos de acceso **
     **********************/      
    public TipoSociedad getTipoSociedad() {
        return tipoSociedad;
    }

    public void setTipoSociedad(TipoSociedad tipoSociedad) {
        this.tipoSociedad = tipoSociedad;
    }

    /**
     * Método para obtener los Tipos de Sociedades existentes y poblar el listado correspondiente
     * @return List<TipoSociedad> listado con los Tipos de Sociedades existentes
     */
    public List<TipoSociedad> getLstTipoSociedades() {
        lstTipoSociedades = tipoSociedadFacade.findAll();
        return lstTipoSociedades;
    }

    public void setLstTipoSociedades(List<TipoSociedad> lstTipoSociedades) {
        this.lstTipoSociedades = lstTipoSociedades;
    }

    
    /***********************
     * Mátodos operativos **
     ***********************/
    
    /**
     * Método que se ejecuta luego de instanciada la clase e inicializa la entidad a gestionar
     */ 
    @PostConstruct
    public void init() {
        tipoSociedad = new TipoSociedad();
    }    
    
    /**
     * Método para guardar el TipoSociedad, sea inserción o edición.
     * Previa validación
     */    
    public void saveTipoSociedad(){
        boolean valida = true;
        try{
            TipoSociedad tipoSocExistente = tipoSociedadFacade.getExistente(tipoSociedad.getNombre().toUpperCase());
            if(tipoSocExistente != null){
                if(tipoSociedad.getId() != null){
                    // si edita, no habilito si no es el mismo
                    if(!tipoSocExistente.equals(tipoSociedad)) valida = false;
                }else{
                    // si no edita no habilito de ninguna manera
                    valida = false;
                }
            }
            if(valida){
                String tempNombre = tipoSociedad.getNombre();
                tipoSociedad.setNombre(tempNombre.toUpperCase());
                String tempSigla = tipoSociedad.getSigla();
                tipoSociedad.setSigla(tempSigla.toUpperCase());
                if(tipoSociedad.getId() != null){
                    tipoSociedadFacade.edit(tipoSociedad);
                    JsfUtil.addSuccessMessage("El Tipo de Sociedad fue guardado con exito");
                }else{
                    tipoSociedadFacade.create(tipoSociedad);
                    JsfUtil.addSuccessMessage("El Tipo de Sociedad fue registrado con exito");
                }   
            }else{
                JsfUtil.addErrorMessage("El Tipo de Sociedad que está tratando de persisitir ya existe, por favor verifique los datos ingresados.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error al procesando el Tipo de Sociedad: " + ex.getMessage());
        }
    }    
    
    /**
     * Método para eliminar el TipoSociedad, previa validación
     */    
    public void deleteTipoSociedad(){
        try{
            if(!tipoSociedadFacade.esReferenciada(tipoSociedad)){
                tipoSociedadFacade.remove(tipoSociedad);
                JsfUtil.addSuccessMessage("El Tipo de Sociedad fue removido con exito");   
            }else{
                JsfUtil.addErrorMessage("El Tipo de Sociedad que está tratando de eliminar está referida a alguna Persona, "
                             + "para eliminarlo debe desvincularlo de las Personas que hagan referencia a él.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error eliminando el Tipo de Sociedad: " + ex.getMessage());
        }
    }    
    
    /**
     * Método para limpiar el objeto del formulario
     */    
    public void limpiarForm(){
        tipoSociedad = new TipoSociedad();
    }      
    
    /*********************
     * Métodos privados **
     *********************/   
    
    /**
     * Método privado que recupera un TipoSociedad según su id
     * @param key Long id de la entidad persistida
     * @return Object la entidad correspondiente
     */  
    private Object getTipoSociedad(Long key) {
        return tipoSociedadFacade.find(key);
    }

    /**************
    ** Converter **
    ***************/   
    @FacesConverter(forClass = TipoSociedad.class)
    public static class TipoSociedadConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbTipoSociedad controller = (MbTipoSociedad) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbTipoSociedad");
            return controller.getTipoSociedad(getKey(value));
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
            if (object instanceof TipoSociedad) {
                TipoSociedad o = (TipoSociedad) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TipoSociedad.class.getName());
            }
        }
    }      
}
