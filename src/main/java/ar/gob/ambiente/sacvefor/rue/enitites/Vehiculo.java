
package ar.gob.ambiente.sacvefor.rue.enitites;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * Entidad de tipo vehículo para camiones y acoplados transportistas.
 * Esta entidad será auditada
 * @author rincostante
 */
@Entity
@Audited
@XmlRootElement
public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Variable privada: Identificador único
     */  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Variable privada: Matrícula del vehículo.
     */
    @Column (nullable=false, length=20, unique=true)
    @NotNull(message = "El campo matricula no puede ser nulo")
    @Size(message = "El campo matricula no puede tener más de 20 caracteres", min = 1, max = 20)
    private String matricula;    
    
    /**
     * Variable privada: Modelo del vehículo
     */
    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne()
    @JoinColumn(name="modelo_id", nullable=false)
    @NotNull(message = "Debe existir una Modelo")
    private Modelo modelo; 
    
    /**
     * Variable privada: año de fabricación del vehículo
     */
    @Column
    private int anio;
    
    /**
     * Variable privada: Empresa a la que pertenece el vehículo, si correspondiera
     */
    @ManyToOne
    @JoinColumn(name="persona_id")
    private Persona empresa; 
    
    /**
     * Variable privada: Usuario que gestiona al vehículo, tanto en el alta como en las modificaciones que pudiera tener
     */
    @ManyToOne()
    @NotNull(message = "Debe existir una Usuario")
    @JoinColumn(name="usuario_id", nullable=false)
    private Usuario usuario; 
    
    /**
     * Variable privada: campo que cachea el nombre del usuario de gestión
     */
    private String strUsuario;    

    /**
     * Variable privada: Fecha de registro del Vehículo. 
     * Solo se insertará al dar de alta al vehículo, no volverá a actualizarse.
     * La fecha de actualizaciones del vehículo se registrarán en la entidad de auditría.
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAlta; 

    /**
     * Variable privada: Condición de habilitado
     */    
    @Column
    private boolean habilitado;

    /**
     * Variable privada: Identificación de la provincia que gestionó (alta o modificaciones)
     * al vehículo. Esta identificación hará referencia a la provincia del
     * servicio gestion territorial.
     */
    @Column
    private Long idProvinciaGt;
    
    /**
     * Variable privada: Nombre de la provincia cacheado del servicio gestion territorial.
     */
    @Column (length=30)
    @Size(message = "El campo provinciaGestion no puede tener más de 30 caracteres", max = 30)
    private String provinciaGestion;
    
    /**
     * Variable privada no persistida: Muestra la fecha de la revisión 
     * para cada item del listado de revisiones de un Vehículo.
     */
    @Transient
    private Date fechaRevision; 

    /**
     * Método que devuelve el nombre del usuario de gestión
     * No disponible para la API rest
     * @return String nombre del usuario
     */  
    @XmlTransient
    public String getStrUsuario() {
        return strUsuario;
    }

    public void setStrUsuario(String strUsuario) {
        this.strUsuario = strUsuario;
    }

    public Persona getEmpresa() {
        return empresa;
    }

    /**
     * Método que devuelve la fecha de una revisión del Vehículo.
     * @return Date fecha de la revisión
     */    
    @XmlTransient
    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public void setEmpresa(Persona empresa) {
        this.empresa = empresa;
    }

    /**
     * Método que devuelve el usuario de gestión del Vehículo
     * No disponible para la API rest
     * @return Usuario Usuario de gestión
     */           
    @XmlTransient
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /**
     * Método que indica si el Vehículo está o no habilitado
     * No disponible para la API rest
     * @return boolean true o false, según corresponda
     */           
    @XmlTransient
    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Long getIdProvinciaGt() {
        return idProvinciaGt;
    }

    public void setIdProvinciaGt(Long idProvinciaGt) {
        this.idProvinciaGt = idProvinciaGt;
    }

    public String getProvinciaGestion() {
        return provinciaGestion;
    }

    public void setProvinciaGestion(String provinciaGestion) {
        this.provinciaGestion = provinciaGestion;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
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
        if (!(object instanceof Vehiculo)) {
            return false;
        }
        Vehiculo other = (Vehiculo) object;
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
        return "ar.gob.ambiente.sacvefor.rue.enitites.Vehiculo[ id=" + id + " ]";
    }
    
}
