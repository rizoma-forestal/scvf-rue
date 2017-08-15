
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (nullable=false, length=20, unique=true)
    @NotNull(message = "El campo matricula no puede ser nulo")
    @Size(message = "El campo matricula no puede tener más de 20 caracteres", min = 1, max = 20)
    private String matricula;    
    
    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne()
    @JoinColumn(name="modelo_id", nullable=false)
    @NotNull(message = "Debe existir una Modelo")
    private Modelo modelo; 
    
    @Column
    private int anio;
    
    /**
     * Si el vehículo pertenece a una empresa de transporte, la registra mediante este campo
     */
    @ManyToOne
    @JoinColumn(name="persona_id")
    private Persona empresa; 
    
    /**
     * Usuario que gestiona al vehículo, tanto en el alta como en las modificaciones que pudiera tener
     */
    @ManyToOne()
    @NotNull(message = "Debe existir una Usuario")
    @JoinColumn(name="usuario_id", nullable=false)
    private Usuario usuario; 
    
    /**
     * campo que cachea el nombre del usuario de gestión
     */
    private String strUsuario;    

    /**
     * Solo se insertará al dar de alta al vehículo, no volverá a actualizarse
     * La fecha de actualizaciones del vehículo se registrarán en la entidad de auditría
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAlta; 

    @Column
    private boolean habilitado;

    /**
     * Identificación de la provincia que gestionó (alta o modificaciones)
     * al vehículo. Esta identificación hará referencia a la provincia del
     * servicio gestion territorial.
     */
    @Column
    private Long idProvinciaGt;
    
    /**
     * Nombre de la provincia cacheado del servicio gestion territorial.
     */
    @Column (length=30)
    @Size(message = "El campo provinciaGestion no puede tener más de 30 caracteres", max = 30)
    private String provinciaGestion;
    
    @Transient
    private Date fechaRevision; 

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

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

    @Override
    public String toString() {
        return "ar.gob.ambiente.sacvefor.rue.enitites.Vehiculo[ id=" + id + " ]";
    }
    
}
