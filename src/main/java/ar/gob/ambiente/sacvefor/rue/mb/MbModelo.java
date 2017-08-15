
package ar.gob.ambiente.sacvefor.rue.mb;

import ar.gob.ambiente.sacvefor.rue.enitites.Marca;
import ar.gob.ambiente.sacvefor.rue.enitites.Modelo;
import ar.gob.ambiente.sacvefor.rue.facades.MarcaFacade;
import ar.gob.ambiente.sacvefor.rue.facades.ModeloFacade;
import ar.gob.ambiente.sacvefor.rue.util.JsfUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Bean de respaldo para la administración de Modelos de vehículos
 * @author rincostante
 */
public class MbModelo {

    private Modelo modelo;
    private List<Modelo> lstModelos;
    private List<Modelo> lstFilters;
    private List<Marca> lstMarcas;
    
    @EJB
    private ModeloFacade modeloFacade;
    @EJB
    private MarcaFacade marcaFacade;       
    
    public MbModelo() {
    }

    
    /**********************
     * Métodos de acceso **
     **********************/    
    public List<Modelo> getLstFilters() {
        return lstFilters;
    }

    public void setLstFilters(List<Modelo> lstFilters) {    
        this.lstFilters = lstFilters;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public List<Modelo> getLstModelos() {
        lstModelos = modeloFacade.findAll();
        return lstModelos;
    }

    public void setLstModelos(List<Modelo> lstModelos) {
        this.lstModelos = lstModelos;
    }

    public List<Marca> getLstMarcas() {
        return lstMarcas;
    }

    public void setLstMarcas(List<Marca> lstMarcas) {
        this.lstMarcas = lstMarcas;
    }

    
    /***********************
     * Mátodos operativos **
     ***********************/
    @PostConstruct
    public void init(){
        modelo = new Modelo();
        lstMarcas = marcaFacade.findAll();
    }  
    
    /**
     * Método para guardar el Modelo, sea inserción o edición.
     * Previa validación
     */    
    public void saveModelo(){
        boolean valida = true;
        
        try{
            Modelo mdlExitente = modeloFacade.getExistente(modelo.getNombre().toUpperCase());
            if(mdlExitente != null){
                if(modelo.getId() != null){
                    // si edita, no habilito si no es el mismo
                    if(!mdlExitente.equals(modelo)) valida = false;
                }else{
                    // si no edita no habilito de ninguna manera
                    valida = false;
                }
            }
            if(valida){
                String tempNombre = modelo.getNombre();
                modelo.setNombre(tempNombre.toUpperCase());
                if(modelo.getId() != null){
                    modeloFacade.edit(modelo);
                    JsfUtil.addSuccessMessage("El Modelo fue guardado con exito");
                }else{
                    modeloFacade.create(modelo);
                    JsfUtil.addSuccessMessage("El Modelo fue registrado con exito");
                }   
            }else{
                JsfUtil.addErrorMessage("El Modelo que está tratando de persisitir ya existe, por favor verifique los datos ingresados.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error al procesando el Modelo: " + ex.getMessage());
        }
    }
    
    /**
     * Método para eliminar un Modelo, previa validación
     */
    public void deleteModelo(){
        try{
            if(!modeloFacade.esReferenciado(modelo)){
                modeloFacade.remove(modelo);
                JsfUtil.addSuccessMessage("El Modelo fue removido con exito");
            }else{
                JsfUtil.addErrorMessage("El Modelo que está tratando de eliminar está referido a algún Vehículo, "
                             + "para eliminarlo debe desvincularlo de los Vehículos que hagan referencia a él.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error eliminando el Modelo: " + ex.getMessage());
        }
    }
    
    /**
     * Método para limpiar el formulario de registro o edición del objeto
     */
    public void limpiarForm() {
        modelo = new Modelo();
    }    
    
    /*********************
     * Métodos privados **
     *********************/    
    private Object getModelo(Long key) {
        return modeloFacade.find(key);
    }
    
    
    /***************
    ** Converter  **
    ****************/    
    @FacesConverter(forClass = Modelo.class)
    public static class ModeloConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbModelo controller = (MbModelo) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbModelo");
            return controller.getModelo(getKey(value));
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
            if (object instanceof Modelo) {
                Modelo o = (Modelo) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Modelo.class.getName());
            }
        }
    }      
}
