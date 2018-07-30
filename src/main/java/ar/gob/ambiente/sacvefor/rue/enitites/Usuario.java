
package ar.gob.ambiente.sacvefor.rue.enitites;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.envers.Audited;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * Gestiona los usuarios que operan la aplicación, 
 * ya sea desde la interface administrativa o
 * desde un cliente.
 * Esta entidad será auditada
 * @author rincostante
 */
@Entity
@Audited
@XmlRootElement
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Variable privada: Identificador único
     */     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Variable privada: indica el login del usuario
     */   
    @Column (nullable=false, length=20, unique=true)
    @NotNull(message = "El campo nombre no puede ser nulo")
    @Size(message = "El campo nombre no puede tener más de 20 caracteres", min = 1, max = 20)
    private String nombre;  
    
    /**
     * Variable privada: clave única, solo para los usuarios de las provincias
     */
    @Column (length=30)
    @Size(message = "El campo pass no puede tener más de 30 caracteres", max = 30)
    private String pass;    
    
    
    @Column (length=250)
    @Size(message = "El campo token no puede tener más de 8 caracteres", max = 250)
    private String token;        
    
    /**
     * Variable privada: indica el nombre completo del usuario
     */      
    @Column (nullable=false, length=100)
    @NotNull(message = "El campo nombre no puede ser nulo")
    @Size(message = "El campo nombreCompleto no puede tener más de 100 caracteres", min = 1, max = 100)
    private String nombreCompleto;    
    
    /**
     * Variable privada: correo electrónico del usuario. Solo para los usuarios de las provincias
     */
    @Column (length=100)
    @Size(message = "El campo nombreCompleto no puede tener más de 100 caracteres", max = 100)
    private String email;      
    
    /**
     * Variable privada: rol al que pertenece el usuario. No auditada
     */
    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne()
    @JoinColumn(name="rol_id", nullable=false)
    @NotNull(message = "Debe existir una Rol")
    private Rol rol;   
    
    /**
     * Variable privada: Identificador único de la Provincia cacheado del servicio de gestión territorial.
     * Solo para los usuarios con rol API
     */
    @Column
    private Long idProvincia;
    
    /**
     * Variable privada: Nombre de la Provincia a la que pertenece el Usuario.
     * Solo para los usuarios con rol API
     */
    @Column (length=100)
    @Size(message = "El campo provincia no puede tener más de 100 caracteres", max = 100)
    private String provincia;  
    
    /**
     * Variable privada: Fecha de registro del Usuario.
     * La fecha de actualizaciones del usuario se registrarán en la entidad de auditría
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAlta;    
    
    /**
     * Variable privada no persistida: Muestra la fecha de la revisión 
     * para cada item del listado de revisiones de un Usuario.
     */
    @Transient
    private Date fechaRevision;         
    
    /**
     * Variable privada: Usuario de gestión del Usuario
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="usuario_id")
    private Usuario usuario; 
    
    /**
     * Variable privada: Nombre del usuario de gestión
     */
    private String strUsuario;    
    
    /**
     * Variable privada: Condición de habilitado del Usuario
     */
    @Column
    private boolean habilitado;

    /**
     * Método que devuelve la fecha de una revisión del Usuario.
     * @return Date fecha de la revisión
     */
    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public String getStrUsuario() {
        return strUsuario;
    }

    public void setStrUsuario(String strUsuario) {
        this.strUsuario = strUsuario;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Long getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(Long idProvincia) {
        this.idProvincia = idProvincia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /**
     * Método que devuelve el usuario de gestión
     * No disponible para la API rest
     * @return Usuario usuario de gestión
     */  
    @XmlTransient
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Método que crea un hash con a partir de la id de la entidad
     * @return int Un entero con el hash
     */        
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Método que compara una instancia de esta entidad con otra según su id
     * @param object La instancia de entidad a comparar con la presente
     * @return boolean Verdadero si son iguales, falso si son distintas
     */       
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Método que devuelve un String con el id de la entidad
     * @return String id de la entidad en formato String
     */     
    @Override
    public String toString() {
        return "ar.gob.ambiente.sacvefor.rue.enitites.Usuario[ id=" + id + " ]";
    }
    
}
