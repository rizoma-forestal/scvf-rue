
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Persona;
import ar.gob.ambiente.sacvefor.rue.enitites.TipoSociedad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase que implementa la abstracta para el acceso a datos de la entidad TipoSociedad.
 * @author rincostante
 */
@Stateless
public class TipoSociedadFacade extends AbstractFacade<TipoSociedad> {

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
    public TipoSociedadFacade() {
        super(TipoSociedad.class);
    }
    
    /**
     * Método para verificar la existencia de TipoSociedad referidas por alguna Persona jurídica.
     * Solo para la app
     * @param tipoSociedad TipoSociedad Tipo de Sociedad de la cual se buscan Personas jurídicas
     * @return boolean Si no existen ninguna devuelve false, si no true
     */
    public boolean esReferenciada(TipoSociedad tipoSociedad){
        List<Persona> lstPersonas;
        em = getEntityManager();
        
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.tipoSociedad = :tipoSociedad";
        Query q = em.createQuery(queryString)
                .setParameter("tipoSociedad", tipoSociedad);
        lstPersonas = q.getResultList();
        return !lstPersonas.isEmpty();        
    }    
    
    /**
     * Metodo sobreescrito para obtener todas los TipoSociedad registrados pero ordenados alfabéticamente
     * @return List<TipoSociedad> listado de los tipos de entidades registrados
     */
    @Override
    public List<TipoSociedad> findAll(){
        em = getEntityManager();
        String queryString = "SELECT tipoSoc FROM TipoSociedad tipoSoc "
                + "ORDER BY tipoSoc.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    

    /**
     * Método para obtener un TipoSociedad según su nombre, si existe
     * @param nombre String nombre del tipo de sociedad buscado
     * @return TipoSociedad Tipo de sociedad con el nombre buscado
     */
    public TipoSociedad getExistente(String nombre) {
        List<TipoSociedad> lstTipoRoles;
        em = getEntityManager();
        
        String queryString = "SELECT tipoSoc FROM TipoSociedad tipoSoc "
                + "WHERE tipoSoc.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        lstTipoRoles = q.getResultList();
        if(lstTipoRoles.isEmpty()){
            return null;
        }else{
            return lstTipoRoles.get(0);
        }
    }       

    /**
     * Método para obtener las Personas vinculadas a un Tipo de Sociedad
     * Solo para la API
     * @param id Long identificador del tipo de sociedad de la cual se buscan sus Personas jurídicas
     * @return List<Persona> listado de las personas jurídicas del tipo de sociedad buscada.
     */
    public List<Persona> getPersonas(Long id){
        em = getEntityManager();
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.tipoSociedad.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList();
    }      
}
