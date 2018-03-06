

package ar.gob.ambiente.sacvefor.rue.territorial.clases;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase para manipular las Provincias provenientes del servicio territorial
 * @author rincostante
 */
@XmlRootElement(name = "provincia")
public class Provincia implements Serializable {

    /**
     * Variable privada: Long identificación de la Provincia
     */    
    private Long id;
    
    /**
     * Variable privada: String nombre de la Provincia
     */
    private String nombre;      
    
    /******************
     * Constructores **
     ******************/   
    
    /**
     * Constructor con los valores por defecto
     */
    public Provincia(){
        this.id = Long.valueOf(0);
        this.nombre = "dafault";
    }   
    
    /**
     * Constructor que instancia la clase con parámetros
     * @param id Long identificador de la Provincia
     * @param nombre String nombre de la Provincia
     */
    public Provincia(Long id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }
    
    /**********************
     * Métodos de acceso **
     **********************/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**************************
     * Métodos sobreescritos **
     **************************/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Provincia)) {
            return false;
        }
        Provincia other = (Provincia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new StringBuffer(" id: ").
                append(id).
                append(" nombre: ").
                append(nombre).toString();
    }
    
}
