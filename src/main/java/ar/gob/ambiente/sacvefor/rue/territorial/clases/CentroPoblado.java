

package ar.gob.ambiente.sacvefor.rue.territorial.clases;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase para manipular los Tipos de Centros poblados provenientes del servicio territorial
 * @author rincostante
 */
@XmlRootElement(name = "centropoblado")
public class CentroPoblado implements Serializable {

    private Long id;
    private String nombre;
    private CentroPobladoTipo centroPobladoTipo;
    private Departamento departamento;    

    /******************
     * Constructores **
     ******************/
    public CentroPoblado(){
        this.id = Long.valueOf(0);
        this.nombre = "dafault";
        this.departamento = new Departamento();
        this.centroPobladoTipo = new CentroPobladoTipo();
    }
    
    public CentroPoblado(Long id, String nombre, Departamento departamento, CentroPobladoTipo centroPobladoTipo){
        this.id = id;
        this.nombre = nombre;
        this.departamento = departamento;
        this.centroPobladoTipo = centroPobladoTipo;
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

    public CentroPobladoTipo getCentroPobladoTipo() {
        return centroPobladoTipo;
    }

    public void setCentroPobladoTipo(CentroPobladoTipo centroPobladoTipo) {
        this.centroPobladoTipo = centroPobladoTipo;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
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
        if (!(object instanceof CentroPoblado)) {
            return false;
        }
        CentroPoblado other = (CentroPoblado) object;
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
                append(nombre).
                append(" centroPobladoTipo: ").
                append(centroPobladoTipo.getNombre()).
                append(" departamento: ").
                append(departamento.getNombre()).toString();
    }

}
