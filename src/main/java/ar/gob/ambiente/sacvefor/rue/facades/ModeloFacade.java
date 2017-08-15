
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Modelo;
import ar.gob.ambiente.sacvefor.rue.enitites.Vehiculo;
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
public class ModeloFacade extends AbstractFacade<Modelo> {

    @PersistenceContext(unitName = "svf_ruePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ModeloFacade() {
        super(Modelo.class);
    }
    
    /**
     * Método para verificar la existencia de Modelos referidos por algún Vehículo.
     * Solo para la app
     * @param modelo
     * @return Si no existen ninguna devuelve false, si no true
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
     * @param nombre
     * @return 
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
     * Método para obtener los Vehículos vinculados a un Modelo
     * Solo para la API
     * @param id
     * @return 
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
