
package ar.gob.ambiente.sacvefor.rue.mb;

import ar.gob.ambiente.sacvefor.rue.enitites.Modelo;
import ar.gob.ambiente.sacvefor.rue.enitites.Persona;
import ar.gob.ambiente.sacvefor.rue.enitites.Usuario;
import ar.gob.ambiente.sacvefor.rue.enitites.Vehiculo;
import ar.gob.ambiente.sacvefor.rue.facades.ModeloFacade;
import ar.gob.ambiente.sacvefor.rue.facades.PersonaFacade;
import ar.gob.ambiente.sacvefor.rue.facades.UsuarioFacade;
import ar.gob.ambiente.sacvefor.rue.facades.VehiculoFacade;
import ar.gob.ambiente.sacvefor.rue.util.JsfUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 * Bean de respaldo para la administración de Vehículos de transporte
 * @author rincostante
 */
public class MbVehiculo {

    /**
     * Variable privada: Vehiculo Entidad que se gestiona mediante el bean
     */  
    private Vehiculo vehiculo;
    
    /**
     * Variable privada: List<Vehiculo> listado de los Vehiculos registrados que compone la tabla para su gestión
     */
    private List<Vehiculo> lstVehiculos;
    
    /**
     * Variable privada: List<Vehiculo> listado para el filtrado de la tabla
     */
    private List<Vehiculo> lstFilters;
    
    /**
     * Variable privada: List<Vehiculo> listado de las revisiones de un vehículo, para su auditoría.
     */
    private List<Vehiculo> lstRevisions;
    
    /**
     * Variable privada: List<Modelo> listado de los Modelos disponibles para asignarlos a un vehículo al registrarlo o editar sus datos.
     */
    private List<Modelo> lstModelos;   
    
    /**
     * Variable privada: List<Persona> listado de las Perosnas disponibles para asignarlos a un vehículo como su titular 
     * al registrarlo o editar sus datos, si corresponde.
     */
    private List<Persona> lstEmpresas;
    
    /**
     * Variable privada: boolean que indica que el formulario mostrado es de una vista detalle de la entidad
     */
    private boolean view;   
    
    /**
     * Variable privada: boolean que indica que el formulario mostrado es de una vista de edición de una entidad existente
     */
    private boolean edit;
    
    /**
     * Variable privada: Long id del Vehículo
     */
    private Long idVehiculo;
    
    /**
     * Variable privada: String matrícula del vehículo para su aditoría
     */
    private String matVehAud;
    
    
    private List<Vehiculo> lstBorrados;
    
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Vehiculo
     */  
    @EJB
    private VehiculoFacade vehiculoFacade; 
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Modelo
     */  
    @EJB
    private ModeloFacade modeloFacade;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Persona
     */ 
    @EJB
    private PersonaFacade personaFacade;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Usuario
     */ 
    @EJB
    private UsuarioFacade usuarioFacade;
    
    /**
     * Constructor
     */
    public MbVehiculo() {
    }
     
    /**********************
     * Métodos de acceso **
     **********************/    
    public String getMatVehAud() {
        return matVehAud;
    }

    public void setMatVehAud(String matVehAud) {
        this.matVehAud = matVehAud;
    }

    public List<Vehiculo> getLstBorrados() {
        return lstBorrados;
    }

    public void setLstBorrados(List<Vehiculo> lstBorrados) {
        this.lstBorrados = lstBorrados;
    }

    public List<Vehiculo> getLstRevisions() {
        return lstRevisions;
    }

    public void setLstRevisions(List<Vehiculo> lstRevisions) {
        this.lstRevisions = lstRevisions;
    }

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public List<Modelo> getLstModelos() {
        return lstModelos;
    }

    public void setLstModelos(List<Modelo> lstModelos) {
        this.lstModelos = lstModelos;
    }

    public List<Persona> getLstEmpresas() {
        return lstEmpresas;
    }
     
    public void setLstEmpresas(List<Persona> lstEmpresas) {
        this.lstEmpresas = lstEmpresas;
    }

    public boolean isEdit() {
        return edit;
    }
    
    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    /**
     * Método para obtener el listado de los vehículos existentes
     * @return List<Vehiculo> listado de vehículos existentes
     */
    public List<Vehiculo> getLstVehiculos() {
        lstVehiculos = vehiculoFacade.findAll();
        return lstVehiculos;
    }

    public void setLstVehiculos(List<Vehiculo> lstVehiculos) {
        this.lstVehiculos = lstVehiculos;
    }

    public List<Vehiculo> getLstFilters() {
        return lstFilters;
    }

    public void setLstFilters(List<Vehiculo> lstFilters) {
        this.lstFilters = lstFilters;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }
 
 
    /***********************
     * Mátodos operativos **
     ***********************/
    
    /**
     * Método que se ejecuta luego de instanciada la clase e inicializa la entidad a gestionar, 
     * el listado de Modelos disponibles, y el listado de las Empresas de transporte disponibles
     */     
    @PostConstruct
    public void init(){
        vehiculo = new Vehiculo();
        lstModelos = modeloFacade.findAll();
        lstEmpresas = personaFacade.getEmpresasTransporteHab();
    } 

    /**
     * Método para guardar el Vehiculo, sea inserción o edición.
     * Previa validación
     */      
    public void saveVehiculo(){
        boolean valida = true;
        try{
            Vehiculo vehExitente = vehiculoFacade.getExistente(vehiculo.getMatricula());
            if(vehExitente != null){
                if(vehiculo.getId() != null){
                    // si edita, no habilito si no es el mismo
                    if(!vehExitente.equals(vehiculo)) valida = false;
                }else{
                    // si no edita no habilito de ninguna manera
                    valida = false;
                }
            }
            if(valida){
                // seteo el usuario [DEBERA ACTUALIZARSE PARA TOMAR EL USUARIO LOGEADO]
                Usuario us = usuarioFacade.find(Long.valueOf(7));
                vehiculo.setUsuario(us);
                vehiculo.setStrUsuario(us.getNombreCompleto());
                String tempMatricula = vehiculo.getMatricula();
                vehiculo.setMatricula(tempMatricula.toUpperCase());
                
                if(vehiculo.getId() != null){
                    // Si es una edición, en cualquier caso no toco la contraseña
                    vehiculoFacade.edit(vehiculo);
                    JsfUtil.addSuccessMessage("El Vehículo fue actualizado con exito");
                }else{
                    // seteo la fecha de alta
                    Date fechaAlta = new Date();
                    vehiculo.setFechaAlta(fechaAlta);
                    // seteo la condición de habilitado
                    vehiculo.setHabilitado(true);

                    // creo el usuario
                    vehiculoFacade.create(vehiculo);
                    JsfUtil.addSuccessMessage("El Vehículo fue registrado con exito.");
                }
            }else{
                JsfUtil.addErrorMessage("Ya existe un Vehículo con la matrícula que está tratando de persisitir, por favor verifique los datos ingresados.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error al procesando el Vehículo: " + ex.getMessage());
        }
    }    
    
    /**
     * Método para limpiar el formulario de registro o edición del objeto
     */
    public void limpiarForm(){
        vehiculo = new Vehiculo();
    }  
    
    /**
     * Método que prepara al Vehículo para su edición
     */
    public void prepareEdit(){
        view = false;
        edit = true;
    }    
    
    /**
     * Método para habilitar la vista detalle del formulario
     */
    public void prepareVIew(){
        view = true;
        edit = false;
    }    
    
    /**
     * Método para habilitar la muestra nuevo del formulario
     */
    public void prepareNew(){
        view = false;
        edit = true;
    }    
    
    /**
     * Método para deshabilitar un Vehículo. Modificará su condición de habilitado a false.
     * Los Vehículos deshabilitados no podrán ser seleccionados para registrarlos en una Guía
     */
    public void deshabilitarVehiculo(){
        // seteo el usuario [DEBERA ACTUALIZARSE PARA TOMAR EL USUARIO LOGEADO]
        Usuario us = usuarioFacade.find(Long.valueOf(7));
        vehiculo.setUsuario(us);
        vehiculo.setStrUsuario(us.getNombreCompleto());
        vehiculo.setHabilitado(false);
        try{
            vehiculoFacade.edit(vehiculo);
            limpiarForm();
            JsfUtil.addSuccessMessage("El Vehículo fue deshabilitado con exito");
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error deshabilitando el Vehículo: " + ex.getMessage());
        }
    }    
    
    /**
     * Metodo para volver a los Vehículos a su condición normal
     */
    public void habilitarVehiculo(){
        // seteo el usuario [DEBERA ACTUALIZARSE PARA TOMAR EL USUARIO LOGEADO]
        Usuario us = usuarioFacade.find(Long.valueOf(7));
        vehiculo.setUsuario(us);
        vehiculo.setStrUsuario(us.getNombreCompleto());
        vehiculo.setHabilitado(true);
        try{
            vehiculoFacade.edit(vehiculo);
            limpiarForm();
            JsfUtil.addSuccessMessage("El Vehículo fue habilitado con exito");
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error habilitando el Vehículo: " + ex.getMessage());
        }
    } 
    
    /**
     * Método para listar las revisiones de un Vehículo 
     */
    public void listarRevisones(){
        view = false;
        edit = true;
        lstRevisions = new ArrayList<>();
        try{
            lstRevisions = vehiculoFacade.findRevisions(matVehAud.toUpperCase());
            if(lstRevisions.isEmpty()){
                JsfUtil.addErrorMessage("La matrícula ingresada no pertenece a ningún vehículo registrado.");
            }
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error obteniendo las revisiones del Vehículo: " + ex.getMessage());
        }
    }     
}
