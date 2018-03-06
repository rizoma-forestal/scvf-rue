
package ar.gob.ambiente.sacvefor.rue.mb;

import ar.gob.ambiente.sacvefor.rue.enitites.Rol;
import ar.gob.ambiente.sacvefor.rue.enitites.Usuario;
import ar.gob.ambiente.sacvefor.rue.facades.RolFacade;
import ar.gob.ambiente.sacvefor.rue.facades.UsuarioFacade;
import ar.gob.ambiente.sacvefor.rue.territorial.clases.Provincia;
import ar.gob.ambiente.sacvefor.rue.territorial.clientes.ProvinciaClient;
import ar.gob.ambiente.sacvefor.rue.util.CriptPass;
import ar.gob.ambiente.sacvefor.rue.util.JsfUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Bean de respaldo para la administración de Usuarios
 * @author rincostante
 */
public class MbUsuario {

    /**
     * Variable privada: Usuario Entidad que se gestiona mediante el bean
     */  
    private Usuario usuario;
    
    /**
     * Variable privada: List<Usuario> listado de los Usuarios registrados que compone la tabla para su gestión
     */
    private List<Usuario> lstUsuarios;
    
    /**
     * Variable privada: List<Usuario> listado para el filtrado de la tabla
     */
    private List<Usuario> lstFilters;
    
    /**
     * Variable privada: List<Rol> listado de los Roles disponibles para asignarlos a un Usuario al registrarlo o editar sus datos.
     */
    private List<Rol> lstRoles;    
    
    /**
     * Variable privada: String clave a asignar al usuario
     */
    private String pass;
    
    /**
     * Variable privada: Provincia provincia a la cual pertenece el usuario, si corresponde.
     */
    private Provincia provincia;
    
    /**
     * Variable privada: List<Provincia> listado de las Provincias disponibles para asignarla a un Usuario al registrarlo o editar sus datos.
     * Si corresponde.
     */
    private List<Provincia> lstProvincias;
    
    /**
     * Variable privada: boolean que indica que el formulario mostrado es de una vista detalle de la entidad
     */
    private boolean view;
    
    /**
     * Variable privada: List<Usuario> listado de las revisiones de un usuario, para su auditoría.
     */
    private List<Usuario> lstRevisions;
    
    /**
     * Variable privada: String nombre con el que se identifica al Usuario a auditar
     */
    private String nombreAud;   
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Usuario
     */  
    @EJB
    private UsuarioFacade usuarioFacade;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Rol
     */  
    @EJB
    private RolFacade rolFacade;     
    
    /**
     * Variable privada: Session mail sesion del servidor para gestionar el envío de correos electrónicos
     */
    @Resource(mappedName ="java:/mail/ambientePrueba")
    private Session mailSesion;
    
    /**
     * Variable privada: String mensaje a enviar por correo electrónico a los usuarios
     */
    private Message mensaje;
    
    /**
     * Variable privada: ProvinciaClient Cliente para la API REST de Provincias del servicio de organización territorial
     */    
    private ProvinciaClient client;
   
    /**
     * Constructor
     */
    public MbUsuario() {
    }
      
    /**********************
     * Métodos de acceso **
     **********************/     
    public String getNombreAud() {
        return nombreAud;
    }
      
    public void setNombreAud(String nombreAud) {
        this.nombreAud = nombreAud;
    }

    public List<Usuario> getLstRevisions() {
        return lstRevisions;
    }
   
    public void setLstRevisions(List<Usuario> lstRevisions) {
        this.lstRevisions = lstRevisions;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public Provincia getProvincia() {
        return provincia;
    }
   
    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public List<Provincia> getLstProvincias() {
        return lstProvincias;
    }
       
    public void setLstProvincias(List<Provincia> lstProvincias) {
        this.lstProvincias = lstProvincias;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Método para obtener los usuarios registrados y poblar el listado correspondiente
     * @return 
     */
    public List<Usuario> getLstUsuarios() {
        lstUsuarios = usuarioFacade.findAll();
        return lstUsuarios;
    }

    public void setLstUsuarios(List<Usuario> lstUsuarios) {
        this.lstUsuarios = lstUsuarios;
    }

    public List<Usuario> getLstFilters() {
        return lstFilters;
    }

    public void setLstFilters(List<Usuario> lstFilters) {
        this.lstFilters = lstFilters;
    }

    public List<Rol> getLstRoles() {
        return lstRoles;
    }

    public void setLstRoles(List<Rol> lstRoles) {
        this.lstRoles = lstRoles;
    }
    
    
    /***********************
     * Mátodos operativos **
     ***********************/
    
    /**
     * Método que se ejecuta luego de instanciada la clase e inicializa las entidades a gestionar, 
     * el listado de Roles disponibles, el cliente para el API REST de Centros poblados,
     * y obtiene las Provincias de la API para luego poblar el listado correspondiente para su selección
     */     
    @PostConstruct
    public void init(){
        usuario = new Usuario();
        lstRoles = rolFacade.findAll();
        // instancio el cliente para la selección de las provincias
        client = new ProvinciaClient();
        // obtengo el listado de provincias 
        provincia = new Provincia();
        GenericType<List<Provincia>> gType = new GenericType<List<Provincia>>() {};
        Response response = client.findAll_JSON(Response.class);
        lstProvincias = response.readEntity(gType);
    }      
    
    /**
     * Método que se ejecuta luego de cerrarse la clase. Cierra el cliente de la API de Centros poblados para la obtención de las Provincias
     */
    @PreDestroy
    public void cerrar(){
        client.close();
    }
    
    /**
     * Método para guardar el Usuario, sea inserción o edición.
     * Una vez generado envía un correo electrónico al usuario con las credenciales de acceso
     * Previa validación
     */      
    public void saveUsuario(){
        boolean valida = true;
        boolean correoEnviado = false;
        String passEncrpitpado;
        try{
            Usuario usExitente = usuarioFacade.getExistente(usuario.getNombre());
            if(usExitente != null){
                if(usuario.getId() != null){
                    // si edita, no habilito si no es el mismo
                    if(!usExitente.equals(usuario)) valida = false;
                }else{
                    // si no edita no habilito de ninguna manera
                    valida = false;
                }
            }
            if(valida){
                String tempNombreCompleto = usuario.getNombreCompleto();
                usuario.setNombreCompleto(tempNombreCompleto.toUpperCase());
                
                // seteo el idProvincia, si lo tiene
                if(provincia!= null){
                    usuario.setIdProvincia(provincia.getId());
                    usuario.setProvincia(provincia.getNombre());
                }
                // seteo el usuario [DEBERA ACTUALIZARSE PARA TOMAR EL USUARIO LOGEADO]
                Usuario us = usuarioFacade.find(Long.valueOf(7));
                usuario.setUsuario(us);
                usuario.setStrUsuario(us.getNombreCompleto());
                
                if(usuario.getId() != null){
                    // Si es una edición, en cualquier caso no toco la contraseña
                    usuarioFacade.edit(usuario);
                    JsfUtil.addSuccessMessage("El Usuario fue guardado con exito");
                }else{
                    // generar contraseña solo si lo estoy creando y si el rol es API
                    if(usuario.getRol().getNombre().equals("API")){
                        pass = CriptPass.generar();
                        passEncrpitpado = CriptPass.encriptar(pass);
                        // asigno la contraseña encriptada
                        usuario.setPass(passEncrpitpado);
                        // envío correo
                        if(!enviarCorreo(usuario.getEmail(), "Se ha creado una cuenta de acceso a la API del Registro Unico de Entidades (RUE) del SACVeFor")){
                            correoEnviado = false;
                            JsfUtil.addErrorMessage("Hubo un error enviando el correo al usuario");
                        }else{
                            correoEnviado = true;
                            JsfUtil.addSuccessMessage("El correo fue enviado exitosamente al usuario");
                        }
                    }else{
                        correoEnviado = true;
                    }
                    // creo el usuario si se envió el correo
                    if(correoEnviado){
                        // seteo la fecha de alta
                        Date fechaAlta = new Date();
                        usuario.setFechaAlta(fechaAlta);
                        // seteo la condición de habilitado
                        usuario.setHabilitado(true);

                        // creo el usuario
                        usuarioFacade.create(usuario);
                        JsfUtil.addSuccessMessage("El Usuario fue registrado con exito.");
                    }else{
                        JsfUtil.addErrorMessage("No se ha podido registrar el Usuario.");
                    }
                }   
            }else{
                JsfUtil.addErrorMessage("El Usuario que está tratando de persisitir ya existe, por favor verifique los datos ingresados.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error al procesando el Usuario: " + ex.getMessage());
        }
    }
    
    /**
     * Método para eliminar un Usuario, previa validación
     */    
    public void deleteUsuario(){
        try{
            if(!usuarioFacade.esReferenciado(usuario)){
                usuarioFacade.remove(usuario);
                JsfUtil.addSuccessMessage("El Usuario fue removido con exito");
            }else{
                JsfUtil.addErrorMessage("El Usuario que está tratando de eliminar está referido a algún Vehículo, Persona o Domicilio, "
                             + "para eliminarlo debe desvincularlo de las Entidades que hagan referencia a él.");
            }
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error eliminando el Usuario: " + ex.getMessage());
        }
    }
    
    /**
     * Método para limpiar el formulario de registro o edición del objeto
     */
    public void limpiarForm(){
        usuario = new Usuario();
        provincia = new Provincia();
    }
    
    /**
     * Método que setea la provincia según el id correspondiente que el usuario tenga seteado para ser editado
     */
    public void prepareEdit(){
        if(usuario.getIdProvincia() != null){
            provincia = client.find_JSON(Provincia.class, String.valueOf(usuario.getIdProvincia()));
        }
    }
    
    /**
     * Método para habilitar la vista detalle del formulario
     */
    public void prepareVIew(){
        view = true;
    }
    
    /**
     * Método para habilitar la muestra nuevo del formulario
     */
    public void prepareNew(){
        view = false;
    }
    
    /**
     * Método para deshabilitar un Usuario. Modificará su condición de habilitado a false.
     * Los Usuarios ADMIN deshabilitados no podrán acceder al sistema
     * Las aplicaciones con Usuarios API deshabilitados no podrán acceder a los servicios.
     */
    public void deshabilitarUsuario(){
        // seteo el usuario [DEBERA ACTUALIZARSE PARA TOMAR EL USUARIO LOGEADO]
        Usuario us = usuarioFacade.find(Long.valueOf(7));
        usuario.setUsuario(us);
        usuario.setStrUsuario(us.getNombreCompleto());
        usuario.setHabilitado(false);
        try{
            usuarioFacade.edit(usuario);
            limpiarForm();
            JsfUtil.addSuccessMessage("El Usuario fue deshabilitado con exito");
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error deshabilitando el Usuario: " + ex.getMessage());
        }
    }
    
    /**
     * Metodo para volver a los Usuarios a su condición normal
     */
    public void habilitarUsuario(){
        // seteo el usuario [DEBERA ACTUALIZARSE PARA TOMAR EL USUARIO LOGEADO]
        Usuario us = usuarioFacade.find(Long.valueOf(7));
        usuario.setUsuario(us);
        usuario.setStrUsuario(us.getNombreCompleto());
        usuario.setHabilitado(true);
        try{
            usuarioFacade.edit(usuario);
            limpiarForm();
            JsfUtil.addSuccessMessage("El Usuario fue habilitado con exito");
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error habilitando el Usuario: " + ex.getMessage());
        }
    }
    
    /**
     * Método para blanquear la contraseña del Usuario
     * Genera una nueva y la envía por correo al destinatario.
     */
    public void cambiarPass(){
        String passEncrpitpado;
        boolean correoEnviado = false;
        // genero la nueva
        pass = CriptPass.generar();
        passEncrpitpado = CriptPass.encriptar(pass);
        // asigno la contraseña encriptada
        usuario.setPass(passEncrpitpado);
        // envío correo
        if(!enviarCorreo(usuario.getEmail(), "Se ha modificado la contraseña de la cuenta de acceso a la API del Registro Unico de Entidades (RUE) del SACVeFor")){
            correoEnviado = false;
            JsfUtil.addErrorMessage("Hubo un error enviando el correo al usuario.");
        }else{
            correoEnviado = true;
            JsfUtil.addSuccessMessage("Se envió un correo electrónico al destinatario con las nuevas credenciales.");
        }
        // cambio la contraseña si se envió el correo
        if(correoEnviado){
            usuarioFacade.edit(usuario);
            JsfUtil.addSuccessMessage("La contraseña se modificó con exito");
        }else{
            JsfUtil.addErrorMessage("No se ha cambiado la contraseña del Usuario.");
        }
    }
    
    /**
     * Método para listar las revisiones de un Usuario 
     */
    public void listarRevisones(){
        view = false;
        lstRevisions = new ArrayList<>();
        try{
            lstRevisions = usuarioFacade.findRevisions(nombreAud);
            if(lstRevisions.isEmpty()){
                JsfUtil.addErrorMessage("El Nombre ingresado no pertenece a ningún Usuario registrado.");
            }
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ex, "Hubo un error obteniendo las revisiones del Usuario: " + ex.getMessage());
        }
    }      
    
    
    /*********************
     * Métodos privados **
     *********************/
    
    /**
     * Método privado para enviar los correos electrónicos.
     * Consume el mail sesion del servidor.
     * Consumido por saveUsuario() y por cambiarPass()
     * @param correo String dirección de correo electrónico
     * @param motivo String motivo del envío
     * @return boolean true si el envío es exitoso y flase si hubo errores.
     */
    private boolean enviarCorreo(String correo, String motivo){  
        boolean result;
        String bodyMessage;
        mensaje = new MimeMessage(mailSesion);
        bodyMessage = "<p>Estimado/a</p> "
                + "<p>" + motivo
                + " para la provincia de " + usuario.getProvincia() + ".</p> "
                + "<p>Las credenciales de acceso son las siguientes:</p> "
                + "<p><strong>usuario:</strong> " + usuario.getNombre() + "<br/> "
                + "<strong>contraseña:</strong> " + pass + "</p> "
                + "<p>Deberá configurar la aplicación de producción local del SACVeFor con las credenciales recibidas "
                + "para que pueda acceder a la API del RUE</p>"
                + "<p>Por favor, no responda este correo. No divulgue ni comparta las credenciales de acceso.</p> "
                + "<p>Saludos cordiales</p> "
                + "<p>Dirección de Bosques</p> "
                + "<p>Dirección Nacional de Bosques, Ordenamiento Territorial y Suelos<br/> "
                + "Subsecretaría de Planificación y Ordenamiento Ambiental de Territorio<br/> "
                + "Secretaría de Política Ambiental, Cambio Climático y Desarrollo Sustentable<br/> "
                + "Ministerio de Ambiente y Desarrollo Sustentable<br/> "
                + "Presidencia de la Nación<br/> "
                + "Reconquista 555 - CABA<br/> "
                + "Teléfono: (54) (11) 4348-8662<br /> "
                + "Correo electrónico: <a href=\"mailto:rincostante@ambiente.gob.ar\">rincostante@ambiente.gob.ar </a></p>";     
        
        try{
            mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(correo));
            mensaje.setSubject("SACVeFor - RUE: " + usuario.getProvincia() + " - Credenciales de acceso" );
            mensaje.setContent(bodyMessage, "text/html; charset=utf-8");
            
            Date timeStamp = new Date();
            mensaje.setSentDate(timeStamp);
            
            Transport.send(mensaje);
            
            result = true;
            
        }catch(MessagingException ex){
            result = false;
            System.out.println("Hubo un error enviando el correo de registro de usuario" + ex.getMessage());
        }
        
        return result;
    }    

    /**
     * Método privado para obtener una Provincia según si id
     * @param key Long id de la Provincia
     * @return Provincia Provincia seleccionada
     */
    private Provincia getProvincia(Long key) {
        Provincia result = new Provincia();
        for(Provincia prov : lstProvincias){
            if(Objects.equals(prov.getId(), key)){
                result = prov;
            }
        }
        return result;
    }

    /******************************
    ** Converter para Provincia  **
    *******************************/    
    @FacesConverter(forClass = Provincia.class)
    public static class ProvinciaConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbUsuario controller = (MbUsuario) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbUsuario");
            return controller.getProvincia(getKey(value));
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
            if (object instanceof Provincia) {
                Provincia o = (Provincia) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Provincia.class.getName());
            }
        }
    }           
}
