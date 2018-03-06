
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Persona;
import ar.gob.ambiente.sacvefor.rue.enitites.TipoEntidad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase que implementa la abstracta para el acceso a datos de la entidad TipoEntidad.
 * @author rincostante
 */
@Stateless
public class TipoEntidadFacade extends AbstractFacade<TipoEntidad> {

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
    public TipoEntidadFacade() {
        super(TipoEntidad.class);
    }
    
    /**
     * Método para verificar la existencia de TipoEntidad referidas por alguna Persona.
     * Solo para la app
     * @param entidad TipoEntidad tipo de entidad de la cual se buscan las referencias
     * @return boolean Si no existen ninguna devuelve false, si no true
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
    
    /**
     * Metodo sobreescrito para obtener todas los TipoEntidad registrados pero ordenados alfabéticamente
     * @return List<TipoEntidad> listado de los tipos de entidades registrados
     */
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
     * @param nombre String nombre del tipo de entidad buscado
     * @return TipoEntidad Tipo de entidad con el nombre buscado
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
     * @param id Long identificador único del Tipo de entidad
     * @return List<Persona> listado de las Personas del tipo de entidad buscado
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
