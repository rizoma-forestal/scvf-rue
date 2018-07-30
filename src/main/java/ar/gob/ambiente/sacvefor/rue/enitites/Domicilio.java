
package ar.gob.ambiente.sacvefor.rue.enitites;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.envers.Audited;

/**
 * Encapsula los datos correspondientes al domicilio de la persona.
 * Compone a la entidad Persona
 * @author rincostante
 */
@Entity
@Audited
@XmlRootElement
public class Domicilio implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Variable privada: Identificador único
     */  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Variable privada: Nombre de la calle
     */ 
    @Column (length=50)
    @Size(message = "El campo calle no puede tener más de 50 caracteres", max = 50)
    private String calle;
    
    /**
     * Variable privada: Número de puerta
     */
    @Column (length=10)
    @Size(message = "El campo numero no puede tener más de 10 caracteres", max = 10)
    private String numero;
    
    /**
     * Variable privada: Piso del edificio, si correspondiera
     */
    @Column (length=10)
    @Size(message = "El campo piso no puede tener más de 10 caracteres", max = 10)
    private String piso;    
    
    /**
     * Variable privada: Identificación del departamento, si correspondiera
     */
    @Column (length=5)
    @Size(message = "El campo depto no puede tener más de 5 caracteres", max = 5)
    private String depto;   

    /**
     * Variable privada: Identificación de la localidad en la que se ubica el domicilio.
     * Esta identificación hará referencia a la localidad del
     * servicio gestion territorial.
     */
    @Column
    private Long idLocalidadGt;   
    
    /**
     * Variable privada: Nombre de la localidad cacheado del servicio gestion territorial.
     */
    @Column (nullable=false, length=50)
    @NotNull(message = "El campo localidad no puede ser nulo")
    @Size(message = "El campo localidad no puede tener más de 50 caracteres", min = 1, max = 50)
    private String localidad;

    /**
     * Variable privada: Nombre del departamento cacheado del servicio gestion territorial.
     */
    @Column (nullable=false, length=50)
    @NotNull(message = "El campo departamento no puede ser nulo")
    @Size(message = "El campo departamento no puede tener más de 50 caracteres", min = 1, max = 50)
    private String departamento;

    /**
     * Variable privada: Nombre de la provincia cacheado del servicio gestion territorial.
     */
    @Column (nullable=false, length=70)
    @NotNull(message = "El campo provincia no puede ser nulo")
    @Size(message = "El campo provincia no puede tener más de 70 caracteres", min = 1, max = 70)
    private String provincia;    
    
    /**
     * Variable privada: Usuario que gestiona al Domicilio, tanto en el alta como en las modificaciones que pudiera tener
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="usuario_id")
    private Usuario usuario; 

    /**
     * Variable privada: campo que cachea el nombre del usuario de gestión
     */    
    private String strUsuario;
    
    
    public Domicilio(){
        super();
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

    /**
     * Método que devuelve el Usuario de gestión
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

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public Long getIdLocalidadGt() {
        return idLocalidadGt;
    }

    public void setIdLocalidadGt(Long idLocalidadGt) {
        this.idLocalidadGt = idLocalidadGt;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
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
        if (!(object instanceof Domicilio)) {
            return false;
        }
        Domicilio other = (Domicilio) object;
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
        return "ar.gob.ambiente.sacvefor.rue.enitites.Domicilio[ id=" + id + " ]";
    }
    
}
