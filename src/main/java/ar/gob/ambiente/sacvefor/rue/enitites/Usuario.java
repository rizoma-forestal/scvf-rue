
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (nullable=false, length=20, unique=true)
    @NotNull(message = "El campo nombre no puede ser nulo")
    @Size(message = "El campo nombre no puede tener más de 20 caracteres", min = 1, max = 20)
    private String nombre;  
    
    /**
     * Solo para los usuarios de las provincias
     */
    @Column (length=30)
    @Size(message = "El campo pass no puede tener más de 30 caracteres", max = 30)
    private String pass;    
    
    /**
     * Solo para los usuarios de las provincias
     */
    @Column (length=250)
    @Size(message = "El campo token no puede tener más de 8 caracteres", max = 250)
    private String token;        
    
    @Column (nullable=false, length=100)
    @NotNull(message = "El campo nombre no puede ser nulo")
    @Size(message = "El campo nombreCompleto no puede tener más de 100 caracteres", min = 1, max = 100)
    private String nombreCompleto;    
    
    /**
     * Solo para los usuarios de las provincias
     */
    @Column (length=100)
    @Size(message = "El campo nombreCompleto no puede tener más de 100 caracteres", max = 100)
    private String email;      
    
    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne()
    @JoinColumn(name="rol_id", nullable=false)
    @NotNull(message = "Debe existir una Rol")
    private Rol rol;   
    
    /**
     * Campo que guarda el id de la provincia a la que pertenece el Usuario, referida al servicio de gestión territorial.
     * Solo para los usuarios con rol API
     */
    @Column
    private Long idProvincia;
    
    /**
     * Campo que guarda el nombre de la Provincia a la que pertenece el Usuario.
     * Solo para los usuarios con rol API
     */
    @Column (length=100)
    @Size(message = "El campo provincia no puede tener más de 100 caracteres", max = 100)
    private String provincia;  
    
    /**
     * Solo se insertará al dar de alta al usuario, no volverá a actualizarse
     * La fecha de actualizaciones del usuario se registrarán en la entidad de auditría
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAlta;    
    
    @Transient
    private Date fechaRevision;         
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="usuario_id")
    private Usuario usuario; 
    
    /**
     * campo que cachea el nombre del usuario de gestión
     */
    private String strUsuario;    
    
    @Column
    private boolean habilitado;

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

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

    @Override
    public String toString() {
        return "ar.gob.ambiente.sacvefor.rue.enitites.Usuario[ id=" + id + " ]";
    }
    
}
