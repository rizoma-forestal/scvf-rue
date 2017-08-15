
package ar.gob.ambiente.sacvefor.rue.mb;

import ar.gob.ambiente.sacvefor.rue.enitites.Marca;
import ar.gob.ambiente.sacvefor.rue.facades.MarcaFacade;
import ar.gob.ambiente.sacvefor.rue.util.JsfUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Bean de respaldo para la administración de Marcas de vehículos
 * @author rincostante
 */
public class MbMarca {
    
    private Marca marca;
    private List<Marca> lstMarcas;
    private List<Marca> lstFilters;
    
    @EJB
    private MarcaFacade marcaFacade;     
    
    public MbMarca() {
    }
    
    /**
     * Métodos de acceso
     */        
    public List<Marca> getLstFilters() {
        return lstFilters;
    }

    public void setLstFilters(List<Marca> lstFilters) {
        this.lstFilters = lstFilters;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public List<Marca> getLstMarcas() {
        lstMarcas = marcaFacade.findAll();
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
        marca = new Marca();
    }    
    
    /**
     * Método para guardar la Marca, sea inserción o edición.
     * Previa validación
     */
    public void saveMarca(){
        boolean valida = true;
        try{
            Marca mrcExitente = marcaFacade.getExistente(marca.getNombre().toUpperCase());
            if(mrcExitente != null){
                if(marca.getId() != null){
                    // si edita, no habilito si no es el mismo
                    if(!mrcExitente.equals(marca)) valida = false;
                }else{
                    // si no edita no habilito de ninguna manera
                    valida = false;
                }
            }
            if(valida){
                String tempNombre = marca.getNombre();
                marca.setNombre(tempNombre.toUpperCase());
                if(marca.getId() != null){
                    marcaFacade.edit(marca);
                    JsfUtil.addSuccessMessage("La Marca fue guardada con exito");
                }else{
                    marcaFacade.create(marca);
                    JsfUtil.addSuccessMessage("La Marca fue registrada con exito");
                }   
            }else{
                JsfUtil.addErrorMessage("La Marca que está tratando de persisitir ya existe, por favor verifique los datos ingresados.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error al procesando la Marca: " + ex.getMessage());
        }
    }
    
    /**
     * Método para eliminar la Marca, previa validación
     */
    public void deleteMarca(){
        try{
            if(!marcaFacade.esReferenciada(marca)){
                marcaFacade.remove(marca);
                JsfUtil.addSuccessMessage("La Marca fue removida con exito");   
            }else{
                JsfUtil.addErrorMessage("La Marca que está tratando de eliminar está referida a algún Modelo, "
                             + "para eliminarla debe desvincularla de los Modelos que hagan referencia a ella.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error eliminando la Marca: " + ex.getMessage());
        }
    }
    
    /**
     * Método para limpiar el objeto del formulario
     */
    public void limpiarForm(){
        marca = new Marca();
    }

    
    /*********************
     * Métodos privados **
     *********************/
    
    private Object getMarca(Long key) {
        return marcaFacade.find(key);
    }
    

    /***************
    ** Converter  **
    ****************/    
    @FacesConverter(forClass = Marca.class)
    public static class MarcaConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbMarca controller = (MbMarca) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbMarca");
            return controller.getMarca(getKey(value));
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
            if (object instanceof Marca) {
                Marca o = (Marca) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Marca.class.getName());
            }
        }
    }    
}
