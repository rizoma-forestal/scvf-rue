
package ar.gob.ambiente.sacvefor.rue.mb;

import ar.gob.ambiente.sacvefor.rue.enitites.Domicilio;
import ar.gob.ambiente.sacvefor.rue.enitites.Persona;
import ar.gob.ambiente.sacvefor.rue.enitites.TipoEntidad;
import ar.gob.ambiente.sacvefor.rue.enitites.TipoSociedad;
import ar.gob.ambiente.sacvefor.rue.enitites.Usuario;
import ar.gob.ambiente.sacvefor.rue.enitites.Vehiculo;
import ar.gob.ambiente.sacvefor.rue.facades.PersonaFacade;
import ar.gob.ambiente.sacvefor.rue.facades.TipoEntidadFacade;
import ar.gob.ambiente.sacvefor.rue.facades.TipoSociedadFacade;
import ar.gob.ambiente.sacvefor.rue.facades.UsuarioFacade;
import ar.gob.ambiente.sacvefor.rue.facades.VehiculoFacade;
import ar.gob.ambiente.sacvefor.rue.territorial.clases.CentroPoblado;
import ar.gob.ambiente.sacvefor.rue.territorial.clases.Departamento;
import ar.gob.ambiente.sacvefor.rue.territorial.clases.Provincia;
import ar.gob.ambiente.sacvefor.rue.territorial.clientes.CentroPobladoClient;
import ar.gob.ambiente.sacvefor.rue.territorial.clientes.DepartamentoClient;
import ar.gob.ambiente.sacvefor.rue.territorial.clientes.ProvinciaClient;
import ar.gob.ambiente.sacvefor.rue.tipos.TipoPersona;
import ar.gob.ambiente.sacvefor.rue.util.EntidadServicio;
import ar.gob.ambiente.sacvefor.rue.util.JsfUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.ValidatorException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Bean de respaldo para la administración de Personas y Domicilios
 * @author rincostante
 */
public class MbPersona {

    /**
     * Variable privada: Persona Entidad que se gestiona mediante el bean
     */
    private Persona persona;
    
    /**
     * Variable privada: List<Persona> listado de las Personas físicas registradas que compone la tabla para su gestión.
     */
    private List<Persona> lstPerFisicas;
    
    /**
     * Variable privada: List<Persona> listado de las Personas jurídicas registradas que compone la tabla para su gestión.
     */
    private List<Persona> lstPerJuridicas;
    
    /**
     * Variable privada: listado para el filtrado de las tablas de Personas
     */
    private List<Persona> lstFilters;
    
    /**
     * Variable privada: List<TipoEntidad> listado de los Tipos de Entidad disponibles para asignar a una Persona al insertar o actualizar
     */
    private List<TipoEntidad> lstTiposEntidad;
    
    /**
     * Variable privada: List<TipoSociedad> listado de los Tipos de Sociedad disponibles para asignar a una Persona jurídica al insertar o actualizar
     */
    private List<TipoSociedad> lstTipoSociedad;
    
    /**
     * Variable privada: boolean que indica que el formulario mostrado es de una vista detalle de la entidad
     */
    private boolean view;
    
    /**
     * Variable privada: boolean que indica que el formulario mostrado es de una vista de edición de una entidad existente
     */
    private boolean edit;
    
    /**
     * Variable privada: Domicilio entidad inyectada en la Persona que contiene los datos correspondientes a su domicilio
     */
    private Domicilio domicilio;
    
    /**
     * Variable privada: Logger logger que registra el log del server con resultados de operaciones de servicios
     */
    private static final Logger logger = Logger.getLogger(Persona.class.getName());
    
    /**
     * Variabla privada: List<Persona> listado de las revisiones de una persona, para su auditoría.
     */
    private List<Persona> lstRevisions;
    
    /**
     * Variable privada: Long cuit con el que se identifica a la Persona a auditar
     */
    private Long cuitAud;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Persona
     */  
    @EJB
    private PersonaFacade personaFacade;
    
    
    @EJB
    private TipoEntidadFacade tipoEntidadFacade;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de TipoSociedad
     */  
    @EJB
    private TipoSociedadFacade tipoSocFacade;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Usuario
     */  
    @EJB
    private UsuarioFacade usuarioFacade;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Vehiculo
     */  
    @EJB
    private VehiculoFacade vehiculoFacade;
    
    /**********************************************************
     * Clientes REST para la selección de datos territoriales *
     **********************************************************/

    /**
     * Variable privada: ProvinciaClient Cliente para la API REST de Provincias del servicio de organización territorial
     */
    private ProvinciaClient provClient;   
    
    /**
     * Variable privada: DepartamentoClient Cliente para la API REST de Departamentos del servicio de organización territorial
     */
    private DepartamentoClient deptoClient;
    
    /**
     * Variable privada: CentroPobladoClient Cliente para la API REST de Localidades del servicio de organización territorial
     */
    private CentroPobladoClient centroPobClient;
    
    /***************************************************************************************
     * Campos para la gestión de los elementos territoriales en los combos del formulario. *
     * Las Entidades de servicio se componen de un par {id | nombre} ***********************
     ***************************************************************************************/
    
    /**
     * Variable privada: List<EntidadServicio> Listado de entidades de servicio con el id y nombre para las Provincias
     */
    private List<EntidadServicio> listProvincias;
    
    /**
     * Variable privada: EntidadServicio Entidad de servicio para setear los datos de la Provincia seleccionada del combo
     */
    private EntidadServicio provSelected;
    
    /**
     * Variable privada: List<EntidadServicio> Listado de entidades de servicio con el id y nombre para los Departamentos
     */
    private List<EntidadServicio> listDepartamentos;
    
    /**
     * Variable privada: EntidadServicio Entidad de servicio para setear los datos del Departamento seleccionado del combo
     */
    private EntidadServicio deptoSelected;
    
    /**
     * Variable privada: List<EntidadServicio> Listado de entidades de servicio con el id y nombre para las Localidades
     */
    private List<EntidadServicio> listLocalidades;
    
    /**
     * Variable privada: EntidadServicio Entidad de servicio para setear los datos de la Localidad seleccionado del combo
     */
    private EntidadServicio localSelected;      
    
    /**
     * Constructor
     */
    public MbPersona() {
    }
     
    /**********************
     * Métodos de acceso **
     **********************/      
    public List<Persona> getLstRevisions() {
        return lstRevisions;
    }

    public void setLstRevisions(List<Persona> lstRevisions) {
        this.lstRevisions = lstRevisions;
    }

    public Long getCuitAud() {
        return cuitAud;
    }
   
    public void setCuitAud(Long cuitAud) {
        this.cuitAud = cuitAud;
    }

    public boolean isEdit() {
        return edit;
    }
    
    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public List<EntidadServicio> getListProvincias() {
        return listProvincias;
    }

    public void setListProvincias(List<EntidadServicio> listProvincias) {
        this.listProvincias = listProvincias;
    }

    public EntidadServicio getProvSelected() {
        return provSelected;
    }

    public void setProvSelected(EntidadServicio provSelected) {
        this.provSelected = provSelected;
    }

    public List<EntidadServicio> getListDepartamentos() {
        return listDepartamentos;
    }

    public void setListDepartamentos(List<EntidadServicio> listDepartamentos) {
        this.listDepartamentos = listDepartamentos;
    }

    public EntidadServicio getDeptoSelected() {
        return deptoSelected;
    }

    public void setDeptoSelected(EntidadServicio deptoSelected) {
        this.deptoSelected = deptoSelected;
    }

    public List<EntidadServicio> getListLocalidades() {
        return listLocalidades;
    }

    public void setListLocalidades(List<EntidadServicio> listLocalidades) {
        this.listLocalidades = listLocalidades;
    }

    public EntidadServicio getLocalSelected() {
        return localSelected;
    }

    public void setLocalSelected(EntidadServicio localSelected) {
        this.localSelected = localSelected;
    }

    /**
     * Método que obtiene las personas jurídicas disponibles para poblar el listado correspondiente
     * @return List<Persona> listado de personas jurídicas
     */
    public List<Persona> getLstPerJuridicas() {
        lstPerJuridicas = personaFacade.getJuridicas();
        return lstPerJuridicas;
    }

    public void setLstPerJuridicas(List<Persona> lstPerJuridicas) {
        this.lstPerJuridicas = lstPerJuridicas;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    /**
     * Método que obtiene las personas físicas disponibles para poblar el listado correspondiente
     * @return List<Persona> listado de personas físicas
     */
    public List<Persona> getLstPerFisicas() {
        lstPerFisicas = personaFacade.getFisicas();
        return lstPerFisicas;
    }

    public void setLstPerFisicas(List<Persona> lstPerFisicas) {
        this.lstPerFisicas = lstPerFisicas;
    }

    public List<Persona> getLstFilters() {
        return lstFilters;
    }

    public void setLstFilters(List<Persona> lstFilters) {
        this.lstFilters = lstFilters;
    }

    public List<TipoEntidad> getLstTiposEntidad() {
        return lstTiposEntidad;
    }

    public void setLstTiposEntidad(List<TipoEntidad> lstTiposEntidad) {
        this.lstTiposEntidad = lstTiposEntidad;
    }

    public List<TipoSociedad> getLstTipoSociedad() {
        return lstTipoSociedad;
    }

    public void setLstTipoSociedad(List<TipoSociedad> lstTipoSociedad) {
        this.lstTipoSociedad = lstTipoSociedad;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public TipoPersona[] getTiposPersona() {
            return TipoPersona.values();
    }    
    
    
    /***********************
     * Mátodos operativos **
     ***********************/
    
    /**
     * Método que se ejecuta luego de instanciada la clase e inicializa las entidades a gestionar, 
     * el listado de Tipos de Entidades disponibles, el de Tipos de sociedades disponibles y carga las provincias para su selección
     */     
    @PostConstruct
    public void init(){
        persona = new Persona();
        domicilio = new Domicilio();
        lstTiposEntidad = tipoEntidadFacade.findAll();
        lstTipoSociedad = tipoSocFacade.findAll();
        cargarProvincias();
    }      
    
    /**
     * Método para actualizar el listado de departamentos según la Provincia seleccionada
     */    
    public void provinciaChangeListener(){
        getDepartamentosSrv(provSelected.getId());
    }   
    
    /**
     * Método para actualizar el listado de localidades según el Departamento seleccionado
     */    
    public void deptoChangeListener(){
        getLocalidadesSrv(deptoSelected.getId());
    }        
    
    /**
     * Método para guardar la Persona, sea inserción o edición. Previa validación.
     * Dado que el registro se realiza mediante la interfaz administrativa, no registro el id de la Provincia
     * @param tipo Integer parámetro que indica si es una Persona física o jurídica
     */      
    public void savePersona(Integer tipo){
        boolean valida = true;
        try{
            Persona perExistente = personaFacade.getExistente(persona.getCuit());
            if(perExistente != null){
                if(persona.getId() != null){
                    // si edita, no habilito si no es el mismo
                    if(!perExistente.equals(persona)) valida = false;
                }else{
                    // si no edita no habilito de ninguna manera
                    valida = false;
                }
            }
            if(!validarDomicilio()){
                valida = false;
            }
            if(valida){
                // actualizo lo que va en mayúsculas
                if(tipo == 0){ // FISICA
                    String tempNombreCompleto = persona.getNombreCompleto();
                    persona.setNombreCompleto(tempNombreCompleto.toUpperCase());
                }else{ // JURIDICA
                    String tempRazSoc = persona.getRazonSocial();
                    persona.setRazonSocial(tempRazSoc.toUpperCase());
                }
                // seteo el Usuario de registro
                Usuario us = usuarioFacade.find(Long.valueOf(7));
                persona.setUsuario(us);
                persona.setStrUsuario(us.getNombreCompleto());
                
                // si hay domicilio lo seteo
                if(!domicilio.getCalle().equals("") && !domicilio.getNumero().equals("") && localSelected != null){
                    // pongo la calle en mayúsculas
                    String tempCalle = domicilio.getCalle();
                    domicilio.setCalle(tempCalle.toUpperCase());
                    // seteo el idLocalidad y los campos cacheados
                    domicilio.setIdLocalidadGt(localSelected.getId());
                    domicilio.setLocalidad(localSelected.getNombre());
                    domicilio.setDepartamento(deptoSelected.getNombre());
                    domicilio.setProvincia(provSelected.getNombre());
                    // seteo el usuario
                    domicilio.setUsuario(us);
                    domicilio.setStrUsuario(us.getNombreCompleto());
                    // agrego el domicilio a la persona
                    persona.setDomicilio(domicilio);
                }
 
                // continúo según sea edición o creación
                if(persona.getId() != null){
                    personaFacade.edit(persona);
                    JsfUtil.addSuccessMessage("La Persona fue actualizada con exito.");
                }else{
                    // seteo la fecha de alta
                    Date fechaAlta = new Date();
                    persona.setFechaAlta(fechaAlta);
                    // seteo la condición de habilitado
                    persona.setHabilitado(true);
                    
                    // seteo el tipo
                    if(tipo == 0){ // FISICA
                        persona.setTipo(TipoPersona.FISICA);
                    }else{ // JURIDICA
                        persona.setTipo(TipoPersona.JURIDICA);
                    }

                    // creo el usuario
                    personaFacade.create(persona);
                    JsfUtil.addSuccessMessage("La Persona fue registrada con exito.");
                }
                limpiarForm();
            }else{
                JsfUtil.addErrorMessage("Ya existe otra Persona con el mismo CUIT, por favor verifique los datos ingresados.");
            }
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error al procesando la Persona: " + ex.getMessage());
        }
    } 
    
    /**
     * Método que setea los datos territoriales según el id de la localidad correspondiente que la Persona tenga seteado para editar sus datos
     */
    public void prepareEdit(){
        // si la Persona tiene un Domicilio, cargo las entidades territoriales
        if(persona.getDomicilio() != null){
            cargarEntidadesSrv(persona.getDomicilio().getIdLocalidadGt());
            domicilio = persona.getDomicilio();
        }
        view = false;
        edit = true;
    }    
    
    /**
     * Método para habilitar la vista detalle del formulario
     */
    public void prepareVIew(){
        if(persona.getDomicilio() != null){
            domicilio = persona.getDomicilio();
        }
        view = true;
        edit = false;
    }   
    
    /**
     * Método para habilitar la muestra nuevo del formulario
     */
    public void prepareNew(){
        view = false;
        edit = true;
        persona = new Persona();
        domicilio = new Domicilio();
    }    

    /**
     * Método para deshabilitar una Persona. Modificará su condición de habilitado a false.
     * Las Personas deshabilitadas no estarán disponibles para su selección desde el servicio.
     * Si tienen Vehículos, los mismos se deshabilitarán también.
     */
    public void deshabilitarPersona(){
        // seteo el usuario [DEBERA ACTUALIZARSE PARA TOMAR EL USUARIO LOGEADO]
        Usuario us = usuarioFacade.find(Long.valueOf(7));
        persona.setUsuario(us);
        persona.setStrUsuario(us.getNombreCompleto());
        persona.setHabilitado(false);
        
        // deshabilito la Persona
        try{
            // primero verifico si tiene Vehículos para deshabilitar
            List<Vehiculo> lstVehiculos;
            lstVehiculos = vehiculoFacade.getByTitular(persona.getCuit());

            // si tiene los deshabilito
            for(Vehiculo veh : lstVehiculos){
                deshabilitarVehiculo(veh);
            }

            personaFacade.edit(persona);
            limpiarForm();
            JsfUtil.addSuccessMessage("La Persona fue deshabilitada con exito");
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error deshabilitando a la Persona: " + ex.getMessage());
        }
    }    
    
    /**
     * Metodo para volver a las Personas a su condición normal.
     * Si tenía Vehículos deshabilitados, se habilitarán también
     */
    public void habilitarPersona(){
        // seteo el usuario [DEBERA ACTUALIZARSE PARA TOMAR EL USUARIO LOGEADO]
        Usuario us = usuarioFacade.find(Long.valueOf(7));
        persona.setUsuario(us);
        persona.setStrUsuario(us.getNombreCompleto());
        persona.setHabilitado(true);
        
        // habilito la Persona
        try{
            // primero verifico si tiene Vehículos para habilitar
            List<Vehiculo> lstVehiculos;
            lstVehiculos = vehiculoFacade.getByTitular(persona.getCuit());

            // si tiene los habilito
            for(Vehiculo veh : lstVehiculos){
                habilitarVehiculo(veh);
            }
            personaFacade.edit(persona);
            limpiarForm();
            JsfUtil.addSuccessMessage("La Persona fue habilitada con exito");
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error habilitando e la Persona: " + ex.getMessage());
        }        
    }  
    
    /**
     * Método para validar el CUIT ingresado. Se valida la cantidad de dígitos y el algoritmo de creación según el DNI
     * @param arg0 FacesContext Contexto
     * @param arg1 UIComponent Componente
     * @param arg2 Object Elemento a validar 
     */
    public void validarCuit(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        String cuit = String.valueOf(arg2);
        // dejo solo números
        cuit = cuit.replaceAll("[^\\d]", "");
        // valido la cantidad de números
        if(cuit.length() == 11){
            // armo un array de caracteres
            char[] cuitArray = cuit.toCharArray();
            // inicializo un array de enterios por cada uno de los cuales se multiplicará cada uno de los dígitos del cuit a validar
            Integer[] serie = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
            // instancio una variable auxiliar para guardar los resultados del calculo del número validador
            Integer aux = 0;
            // recorro el ambos arrays y opero
            for (int i=0; i<10; i++){
                aux += Character.getNumericValue(cuitArray[i]) * serie[i];
            }
            // ejecuto la especificación (Módulo 11): 11 menos el resto de de la división de la suma de productos anterior por 11
            aux = 11 - (aux % 11);
            //Si el resultado anterior es 11 el código es 0
            if (aux == 11){
                aux = 0;
            }
            //Si el resultado anterior es 10 el código no tiene que validar, cosa que de todas formas pasa
            //en la siguiente comparación.
            //Devuelve verdadero si son iguales, falso si no lo son
            //(Paso todo a Object para prevenir errores, se puede usar: Integer.valueOf(cuitArray[10]) == aux;)
            Object oUltimo = (Integer)Character.getNumericValue(cuitArray[10]);
            Object oAux = aux;
            
            if(!oUltimo.equals(oAux)){
                throw new ValidatorException(new FacesMessage("El CUIT ingresado es incorrecto."));
            }
        }else{
            // envío mensaje de error
            throw new ValidatorException(new FacesMessage("El CUIT debe tener 11 dígitos sin contar otros caracteres."));
        }
    }
    
    /**
     * Método para validar la existencia del Nombre completo de la persona en el caso que sea física
     * @throws ValidatorException Excepción a dispararse
     */
    public void validarNombreCompleto(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        if(String.valueOf(arg2).equals("")){
            throw new ValidatorException(new FacesMessage("Debe ingresar el Nombre completo."));
        }
    }
    
    /**
     * Método para validar la existencia de la Razón social de la persona en el caso que sea jurídica
     * @throws ValidatorException Excepción a dispararse
     */ 
    public void validarRazonSocial(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        if(String.valueOf(arg2).equals("")){
            throw new ValidatorException(new FacesMessage("Debe ingresar la Razón Social."));
        }
    }     
    
    /**
     * Método para limpiar el formulario con los datos de la Persona
     */
    public void limpiarForm() {
        persona = new Persona();
        domicilio = new Domicilio();
        provSelected = new EntidadServicio();
        deptoSelected = new EntidadServicio();
        localSelected = new EntidadServicio();
    }  
    

    /*********************
     * Métodos privados **
     *********************/
    /**
     * Método para poblar el listado de Departamentos según la Provincia seleccionada del servicio REST de centros poblados.
     */    
    private void getDepartamentosSrv(Long idProv) {
        EntidadServicio depto;
        List<Departamento> listSrv;
        
        try{
            // instancio el cliente para la selección de las provincias
            provClient = new ProvinciaClient();
            // obtngo el listado
            GenericType<List<Departamento>> gType = new GenericType<List<Departamento>>() {};
            Response response = provClient.findByProvincia_JSON(Response.class, String.valueOf(idProv));
            listSrv = response.readEntity(gType);
            // lleno el listado de los combos
            listDepartamentos = new ArrayList<>();
            for(Departamento dpt : listSrv){
                depto = new EntidadServicio(dpt.getId(), dpt.getNombre());
                listDepartamentos.add(depto);
            }
            
            provClient.close();
        }catch(ClientErrorException ex){
            // muestro un mensaje al usuario
            JsfUtil.addErrorMessage("Hubo un error obteniendo los Departamentos de la Provincia seleccionada. " + ex.getMessage());
            logger.log(Level.SEVERE, "{0} - {1}", new Object[]{"Hubo un error obteniendo los Departamentos por Provincia "
                    + "del servicio REST de centros poblados", ex.getMessage()});
        }
    }
    
    /**
     * Método para poblar el listado de Localidades según el Departamento seleccionado del servicio REST de centros poblados.
     */
    private void getLocalidadesSrv(Long idDepto){
        EntidadServicio local;
        List<CentroPoblado> listSrv;
        
        try{
            // instancio el cliente para la selección de las provincias
            deptoClient = new DepartamentoClient();
            // obtngo el listado
            GenericType<List<CentroPoblado>> gType = new GenericType<List<CentroPoblado>>() {};
            Response response = deptoClient.findByDepto_JSON(Response.class, String.valueOf(idDepto));
            listSrv = response.readEntity(gType);
            // lleno el listado de los combos
            listLocalidades = new ArrayList<>();
            for(CentroPoblado loc : listSrv){
                local = new EntidadServicio(loc.getId(), loc.getNombre());
                listLocalidades.add(local);
            }
            
            deptoClient.close();
        }catch(ClientErrorException ex){
            // muestro un mensaje al usuario
            JsfUtil.addErrorMessage("Hubo un error obteniendo los Centros poblados del Departamento seleccionado. " + ex.getMessage());
            logger.log(Level.SEVERE, "{0} - {1}", new Object[]{"Hubo un error obteniendo los Centros poblados por Departamento "
                    + "del servicio REST de centros poblados", ex.getMessage()});
        }
    }  
    
    /**
     * Método privado para cargar entidades de servicio con los datos territoriales y los listados correspondientes, 
     * para actualizar el Domicilio de la Persona. Consumido por prepareEdit()
     */
    private void cargarEntidadesSrv(Long idLocalidad){
        CentroPoblado cp;
        
        try{
            // instancio el cliente para la selección de las provincias
            centroPobClient = new CentroPobladoClient();
            cp = centroPobClient.find_JSON(CentroPoblado.class, String.valueOf(idLocalidad));
            // cierro el cliente
            centroPobClient.close();
            // instancio las Entidades servicio
            localSelected = new EntidadServicio(cp.getId(), cp.getNombre());
            deptoSelected = new EntidadServicio(cp.getDepartamento().getId(), cp.getDepartamento().getNombre());
            provSelected = new EntidadServicio(cp.getDepartamento().getProvincia().getId(), cp.getDepartamento().getProvincia().getNombre());
            // cargo el listado de provincias
            cargarProvincias();
            // cargo el listado de Departamentos
            getDepartamentosSrv(provSelected.getId());
            // cargo el listado de Localidades
            getLocalidadesSrv(deptoSelected.getId());

        }catch(ClientErrorException ex){
            // muestro un mensaje al usuario
            JsfUtil.addErrorMessage("Hubo un error obteniendo los datos territoriales del Domicilio de la Persona. " + ex.getMessage());
            logger.log(Level.SEVERE, "{0} - {1}", new Object[]{"Hubo un error obteniendo el Centro poblado por id desde el "
                    + "servicio REST de centros poblados", ex.getMessage()});
        }
    }    

    /**
     * Método privado para deshabilitar los vehículos que pudiera tener asociados la Persona que se está deshabilitando.
     * Un Vehículo deshabilitado no podrá seleccionarse para asignarlo a una Guía desde el Servicio.
     * Consumido por deshabilitarPersona()
     * @param veh Vehiculo Vehículo a deshabilitar
     */
    private void deshabilitarVehiculo(Vehiculo veh) {
        // seteo el usuario [DEBERA ACTUALIZARSE PARA TOMAR EL USUARIO LOGEADO]
        Usuario us = usuarioFacade.find(Long.valueOf(7));
        veh.setUsuario(us);
        veh.setStrUsuario(us.getNombreCompleto());
        veh.setHabilitado(false);
        vehiculoFacade.edit(veh);
        limpiarForm();
    }

    /**
     * Método privado para habilitar un Vehículo deshabilitado, si la Persona que se está habilitando tuviera vehíulos relacionados.
     * Consumido por habilitarPersona()
     * @param veh Vehiculo Vehículo a habilitar
     */
    private void habilitarVehiculo(Vehiculo veh) {
        // seteo el usuario [DEBERA ACTUALIZARSE PARA TOMAR EL USUARIO LOGEADO]
        Usuario us = usuarioFacade.find(Long.valueOf(7));
        veh.setUsuario(us);
        veh.setStrUsuario(us.getNombreCompleto());
        veh.setHabilitado(true);
        vehiculoFacade.edit(veh);
        limpiarForm();
    }

    /**
     * Método privado para validar un Domicilio al registrar una Persona o editar los datos correspondientes a una existente.
     * Consumido por savePersona()
     * @return boolean true o false según el domicilio sea validado o no.
     */
    private boolean validarDomicilio() {
        boolean result = true;
        if(domicilio.getCalle().equals("") && !domicilio.getNumero().equals("")) result = false;
        else if(!domicilio.getCalle().equals("") && domicilio.getNumero().equals("")) result = false;
        else if(domicilio.getIdLocalidadGt() != null && (domicilio.getCalle().equals("") || domicilio.getNumero().equals(""))) result = false;
        return result;
    }

    /**
     * Método priviado para obtener las Provincias mediante el servicio REST de Centros poblados.
     * Obtiene las provincias y por cada una crea una EntidadServicio con el id y nombre.
     * Luego las incluye en el listado listProvincias.
     * Consumido por cargarEntidadesSrv() y init()
     */
    private void cargarProvincias() {
        EntidadServicio provincia;
        List<Provincia> listSrv;
        
        try{
            // instancio el cliente para la selección de las provincias
            provClient = new ProvinciaClient();
            // obtengo el listado de provincias 
            GenericType<List<Provincia>> gType = new GenericType<List<Provincia>>() {};
            Response response = provClient.findAll_JSON(Response.class);
            listSrv = response.readEntity(gType);
            // lleno el list con las provincias como un objeto Entidad Servicio
            listProvincias = new ArrayList<>();
            for(Provincia prov : listSrv){
                provincia = new EntidadServicio(prov.getId(), prov.getNombre());
                listProvincias.add(provincia);
                //provincia = null;
            }
            // cierro el cliente
            provClient.close();
        }catch(ClientErrorException ex){
            // muestro un mensaje al usuario
            JsfUtil.addErrorMessage("Hubo un error cargando el listado de Provincias para su selección.");
            // lo escribo en el log del server
            logger.log(Level.SEVERE, "{0} - {1}", new Object[]{"Hubo un error cargando las Provincias desde "
                    + "el servicio REST de Centros poblados.", ex.getMessage()});
        }
    }

    /**
     * Método privado que recupera una Persona según su id
     * @param key Long id de la entidad persistida
     * @return Object la entidad correspondiente
     */
    private Object getPersona(Long key) {
        return personaFacade.find(key);
    }
    
    /****************************
    ** Converter para Persona  **
    *****************************/    
    @FacesConverter(forClass = Persona.class)
    public static class PersonaConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbPersona controller = (MbPersona) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbPersona");
            return controller.getPersona(getKey(value));
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
            if (object instanceof Persona) {
                Persona o = (Persona) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Persona.class.getName());
            }
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
            lstRevisions = personaFacade.findRevisions(cuitAud);
            if(lstRevisions.isEmpty()){
                JsfUtil.addErrorMessage("El CUIT ingresado no pertenece a ninguna Persona registrada.");
            }
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error obteniendo las revisiones de la Persona: " + ex.getMessage());
        }
    }       
}
