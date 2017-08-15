
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Persona;
import ar.gob.ambiente.sacvefor.rue.enitites.TipoEntidad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author rincostante
 */
@Stateless
public class TipoEntidadFacade extends AbstractFacade<TipoEntidad> {

    @PersistenceContext(unitName = "svf_ruePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoEntidadFacade() {
        super(TipoEntidad.class);
    }
    
    /**
     * Método para verificar la existencia de TipoEntidad referidas por alguna Persona.
     * Solo para la app
     * @param entidad
     * @return Si no existen ninguna devuelve false, si no true
     */
    public boolean esReferenciada(TipoEntidad entidad){
        List<Persona> lstPersonas;
        em = getEntityManager();
        
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.entidad = :entidad";
        Query q = em.createQuery(queryString)
                .setParameter("entidad", entidad);
        lstPersonas = q.getResultList();
        return !lstPersonas.isEmpty();        
    }  
    
    @Override
    public List<TipoEntidad> findAll(){
        em = getEntityManager();
        String queryString = "SELECT entidad FROM TipoEntidad entidad "
                + "ORDER BY entidad.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método para obtener un TipoEntidad según su nombre, si existe
     * @param nombre
     * @return 
     */
    public TipoEntidad getExistente(String nombre) {
        List<TipoEntidad> lstTipoEntidades;
        em = getEntityManager();
        
        String queryString = "SELECT entidad FROM TipoEntidad entidad "
                + "WHERE entidad.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        lstTipoEntidades = q.getResultList();
        if(lstTipoEntidades.isEmpty()){
            return null;
        }else{
            return lstTipoEntidades.get(0);
        }
    }        
    
    /**
     * Método para obtener las Personas vinculadas a un Tipo de Entidad
     * Solo para la API
     * @param id
     * @return 
     */
    public List<Persona> getPersonas(Long id){
        em = getEntityManager();
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.entidad.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList();
    }      
}
