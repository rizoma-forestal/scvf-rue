
package ar.gob.ambiente.sacvefor.rue.enitites;

import ar.gob.ambiente.sacvefor.rue.tipos.TipoPersona;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.envers.Audited;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * Entidad principal del modelo, gestiona todo tipo de personas
 * físicas o jurídicas que puedan tener vículo con el proceso de gestión
 * de productos forestales:
 * Productores, intermediarios, transportistas, gestores, exportadores, etc.
 * Esta entidad será auditada
 * @author rincostante
 */
@Entity
@Audited
@XmlRootElement
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Identificación de la provincia que gestionó (alta o modificaciones)
     * al vehículo. Esta identificación hará referencia a la provincia del
     * servicio gestion territorial.
     */
    @Column
    private Long idProvinciaGt;
    
    /**
     * Nombre de la Provincia de origen de la carga.
     * Proveniente del usuario de registro
     */
    @Column (length=30)
    @Size(message = "El campo provinciaGestion no puede tener más de 30 caracteres", max = 30)
    private String provinciaGestion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoPersona tipo;    
    
    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name="tiposociedad_id")
    private TipoSociedad tipoSociedad;     

    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name="tipoentidad_id", nullable=false)
    @NotNull(message = "Debe existir un TipoEntidad")
    private TipoEntidad entidad; 
    
    /**
     * En los casos de las razones sociales será el domicilio legal, en el caso de 
     * productores y titulares que sean personas físicas, será su domicilio real,
     * en el caso de choferes, por ejemplo, no será necesario domicilio alguno.
     */
    @OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
    private Domicilio domicilio;      
    
    @Column (length=100)
    @Size(message = "El campo nombreCompleto no puede tener más de 100 caracteres", max = 100)
    private String nombreCompleto;  
    
    @Column (length=200)
    @Size(message = "El campo razonSocial no puede tener más de 200 caracteres", max = 200)
    private String razonSocial;   
    
    @Column (nullable=false, unique=true)
    @NotNull(message = "El campo cuit no puede ser nulo")
    private Long cuit;
    
    @Column (length=100)
    @Size(message = "El campo correoElectronico no puede tener más de 100 caracteres", max = 100)
    private String correoElectronico;       
    
    @OneToMany(mappedBy="empresa")
    private List<Vehiculo> vehiculos;    
    
    /**
     * Usuario que gestiona la persona, tanto en el alta como en las modificaciones que pudiera tener
     */
    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario; 
    
    /**
     * campo que cachea el nombre del usuario de gestión
     */
    private String strUsuario;

    /**
     * Solo se insertará al dar de alta a la persona, no volverá a actualizarse
     * La fecha de actualizaciones de la persona se registrarán en la entidad de auditría
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAlta; 
    
    @Transient
    private Date fechaRevision;     

    @Column
    private boolean habilitado;    
    
    public Persona() {
        vehiculos = new ArrayList<>();
    }    

    @XmlTransient
    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    @XmlTransient
    public String getStrUsuario() {
        return strUsuario;
    }

    public void setStrUsuario(String strUsuario) {
        this.strUsuario = strUsuario;
    }

    public TipoPersona getTipo() {
        return tipo;
    }

    public void setTipo(TipoPersona tipo) {
        this.tipo = tipo;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
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

    public TipoSociedad getTipoSociedad() {
        return tipoSociedad;
    }

    public void setTipoSociedad(TipoSociedad tipoSociedad) {
        this.tipoSociedad = tipoSociedad;
    }

    public TipoEntidad getEntidad() {
        return entidad;
    }

    public void setEntidad(TipoEntidad entidad) {
        this.entidad = entidad;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Long getCuit() {
        return cuit;
    }

    public void setCuit(Long cuit) {
        this.cuit = cuit;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @XmlTransient
    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
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
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gob.ambiente.sacvefor.rue.enitites.Persona[ id=" + id + " ]";
    }
    
}
