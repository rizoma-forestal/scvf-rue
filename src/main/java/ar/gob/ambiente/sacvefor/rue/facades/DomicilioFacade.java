
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Domicilio;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Clase que implementa la abstracta para el acceso a datos de la entidad Domicilio.
 * @author rincostante
 */
@Stateless
public class DomicilioFacade extends AbstractFacade<Domicilio> {

    /**
     * Variable privada: EntityManager al que se le indica la unidad de persistencia mediante la cual accederá a la base de datos
     */    
    @PersistenceContext(unitName = "svf_ruePU")
    private EntityManager em;

    /**
     * Método que implementa el abstracto para la obtención del EntityManager
     * @return EntityManager para acceder a datos
     */      
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Constructor
     */    
    public DomicilioFacade() {
        super(Domicilio.class);
    }
    
}
