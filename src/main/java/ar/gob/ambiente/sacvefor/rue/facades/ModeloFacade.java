
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Modelo;
import ar.gob.ambiente.sacvefor.rue.enitites.Vehiculo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase que implementa la abstracta para el acceso a datos de la entidad Modelo.
 * @author rincostante
 */
@Stateless
public class ModeloFacade extends AbstractFacade<Modelo> {

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
    public ModeloFacade() {
        super(Modelo.class);
    }
    
    /**
     * Método para verificar la existencia de Modelos referidos por algún Vehículo.
     * Solo para la app
     * @param modelo Modelo del cual se consulta sobre la existencia de vehículos vinculados
     * @return boolean Si no existen ninguna devuelve false, si no true
     */
    public boolean esReferenciado(Modelo modelo){
        List<Modelo> lstModelos;
        em = getEntityManager();
        
        String queryString = "SELECT veh FROM Vehiculo veh "
                + "WHERE veh.modelo = :modelo";
        Query q = em.createQuery(queryString)
                .setParameter("modelo", modelo);
        lstModelos = q.getResultList();
        return !lstModelos.isEmpty();        
    }
    
    /**
     * Metodo sobreescrito para obtener todas los Modelos registrados pero ordenados alfabéticamente
     * @return List<Modelo> listado de los modelos registrados
     */    
    @Override
    public List<Modelo> findAll(){
        em = getEntityManager();
        String queryString = "SELECT modelo FROM Modelo modelo "
                + "ORDER BY modelo.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }    

    /**
     * Método para obtener un Modelo según su nombre, si existe
     * @param nombre String Nombre del Modelo a buscar
     * @return Modelo Modelo cuyo nombre se consultó
     */
    public Modelo getExistente(String nombre) {
        List<Modelo> lstModelos;
        em = getEntityManager();
        
        String queryString = "SELECT modelo FROM Modelo modelo "
                + "WHERE modelo.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        lstModelos = q.getResultList();
        if(lstModelos.isEmpty()){
            return null;
        }else{
            return lstModelos.get(0);
        }
    }    
    
    /**
     * Método para obtener los Vehículos vinculados a un Modelo según si id
     * Solo para la API
     * @param id Long identificador único del Modelo del cual se consultan los vehículos registrados.
     * @return List<Vehiculo> listado de los vehículos vinculados al Modelo.
     */
    public List<Vehiculo> getVehiculos(Long id){
        em = getEntityManager();
        String queryString = "SELECT veh FROM Vehiculo veh "
                + "WHERE veh.modelo.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList();
    }    
}
