
package ar.gob.ambiente.sacvefor.rue.enitites;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Paramétrica que gestiona los tipos de sociedad que una persona jurìdica puede constituir
 * Sociedad Anónima
 * Sociedad de Responsabilidad Limitada
 * Sociedad de hecho
 * etc
 * @author rincostante
 */
@Entity
@XmlRootElement
public class TipoSociedad implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Variable privada: Identificador único
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Variable privada: Nombre del Tipo de Sociedad
     */ 
    @Column (nullable=false, length=50, unique=true)
    @NotNull(message = "El campo nombre no puede ser nulo")
    @Size(message = "El campo nombre no puede tener más de 50 caracteres", min = 1, max = 50)
    private String nombre;
    
    /**
     * Variable privada: Sigla que indica el tipo de sociedad.
     * Ej: SA, SRL, etc
     */ 
    @Column (nullable=false, length=10)
    @NotNull(message = "El campo sigla no puede ser nulo")
    @Size(message = "El campo sigla no puede tener más de 10 caracteres", min = 1, max = 10)
    private String sigla;    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
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
        if (!(object instanceof TipoSociedad)) {
            return false;
        }
        TipoSociedad other = (TipoSociedad) object;
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
        return "ar.gob.ambiente.sacvefor.rue.enitites.TipoPersona[ id=" + id + " ]";
    }
    
}
