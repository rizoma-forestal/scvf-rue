
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
    
    /**
     * Variable privada: Identificador único
     */  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Variable privada: Identificación de la provincia que gestionó (alta o modificaciones)
     * al vehículo. Esta identificación hará referencia a la provincia del
     * servicio gestion territorial.
     */
    @Column
    private Long idProvinciaGt;
    
    /**
     * Variable privada: Nombre de la Provincia de origen de la carga.
     * Proveniente del usuario de registro
     */
    @Column (length=30)
    @Size(message = "El campo provinciaGestion no puede tener más de 30 caracteres", max = 30)
    private String provinciaGestion;

    /**
     * Variable privada: Tipo de persona (FISICA o JURIDICA)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoPersona tipo;    
    
    /**
     * Variable privada: Tipo de sociedad, solo para las personas jurídicas
     * Ej. Sociedad anónima, Sociedad de Responsabilidad limitada, etc.
     */
    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name="tiposociedad_id")
    private TipoSociedad tipoSociedad;     

    /**
     * Variable privada: Tipo de Entidad, se refiere al rol de la Persona en el componente local que lo registra
     * Ej: Productor, Empresa de transporte, Empresa exportadora, etc.
     */
    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name="tipoentidad_id", nullable=false)
    @NotNull(message = "Debe existir un TipoEntidad")
    private TipoEntidad entidad; 
    
    /**
     * Variable privada: En los casos de las razones sociales será el domicilio legal, en el caso de 
     * productores y titulares que sean personas físicas, será su domicilio real,
     * en el caso de choferes, por ejemplo, no será necesario domicilio alguno.
     * Solo para las personas de tipo "Transformador" los domicilios serán obligatorios, dado que son destinatarios de Guías.
     */
    @OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
    private Domicilio domicilio;      
    
    /**
     * Variable privada: Nombres y apellidos completos de la persona.
     * Solo para las personas físicas
     */
    @Column (length=100)
    @Size(message = "El campo nombreCompleto no puede tener más de 100 caracteres", max = 100)
    private String nombreCompleto;  
    
    /**
     * Variable privada: Nombre de la Razón social, solo para personas jurídicas
     */
    @Column (length=200)
    @Size(message = "El campo razonSocial no puede tener más de 200 caracteres", max = 200)
    private String razonSocial;   
    
    /**
     * Variable privada: Cuit de la persona
     */
    @Column (nullable=false, unique=true)
    @NotNull(message = "El campo cuit no puede ser nulo")
    private Long cuit;
    
    /**
     * Variable privada: correo electrónico de la persona
     */
    @Column (length=100)
    @Size(message = "El campo correoElectronico no puede tener más de 100 caracteres", max = 100)
    private String correoElectronico;       
    
    /**
     * Variable privada: Listado de vehículos vinculados a la persona.
     * Solo para las empresas de transporte.
     */
    @OneToMany(mappedBy="empresa")
    private List<Vehiculo> vehiculos;    
    
    /**
     * Variable privada: Usuario que gestiona la persona, tanto en el alta como en las modificaciones que pudiera tener
     */
    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario; 
    
    /**
     * Variable privada: campo que cachea el nombre del usuario de gestión
     */
    private String strUsuario;

    /**
     * Variable privada: Fecha de registro de la persona.
     * Solo se insertará al dar de alta a la persona, no volverá a actualizarse
     * La fecha de actualizaciones de la persona se registrarán en la entidad de auditoría
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAlta; 
    
    /**
     * Variable privada no persistida: Muestra la fecha de la revisión 
     * para cada item del listado de revisiones de una Persona.
     */
    @Transient
    private Date fechaRevision;     

    /**
     * Variable privada: Condición de habilitado
     */
    @Column
    private boolean habilitado;    
    
    public Persona() {
        vehiculos = new ArrayList<>();
    }    
      
    /**
     * Método que devuelve la fecha de una revisión de la Persona.
     * No disponible para la API rest
     * @return Date fecha de la revisión
     */
    @XmlTransient
    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

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

    /**
     * Método que devuelve el listado de los Vehículos vinculados a la Persona
     * No disponible para la API rest
     * @return List<Vehiculo> Listado de Vehículos
     */        
    @XmlTransient
    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    /**
     * Método que devuelve el usuario de gestión de la Persona
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
     * Método que indica si la Persona está o no habilitada
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
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
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
        return "ar.gob.ambiente.sacvefor.rue.enitites.Persona[ id=" + id + " ]";
    }
    
}
