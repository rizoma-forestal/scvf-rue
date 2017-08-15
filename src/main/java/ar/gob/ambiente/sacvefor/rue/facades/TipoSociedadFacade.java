
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Persona;
import ar.gob.ambiente.sacvefor.rue.enitites.TipoSociedad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author rincostante
 */
@Stateless
public class TipoSociedadFacade extends AbstractFacade<TipoSociedad> {

    @PersistenceContext(unitName = "svf_ruePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoSociedadFacade() {
        super(TipoSociedad.class);
    }
    
    /**
     * Método para verificar la existencia de TipoSociedad referidas por alguna Persona.
     * Solo para la app
     * @param tipoSociedad
     * @return Si no existen ninguna devuelve false, si no true
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
    
    @Override
    public List<TipoSociedad> findAll(){
        em = getEntityManager();
        String queryString = "SELECT tipoSoc FROM TipoSociedad tipoSoc "
                + "ORDER BY tipoSoc.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    

    /**
     * Método para obtener un TipoEntidad según su nombre, si existe
     * @param nombre
     * @return 
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
     * @param id
     * @return 
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
