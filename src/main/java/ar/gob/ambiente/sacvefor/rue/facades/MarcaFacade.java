
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Marca;
import ar.gob.ambiente.sacvefor.rue.enitites.Modelo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 * Clase que implementa la abstracta para el acceso a datos de la entidad Marca.
 * @author rincostante
 */
@Stateless
public class MarcaFacade extends AbstractFacade<Marca> {

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
    public MarcaFacade() {
        super(Marca.class);
    }  
    
    /**
     * Método para verificar la existencia de Marcas referidas por algún Modelo.
     * Solo para la app
     * @param marca Marca de la cual se buscan los Modelos vinculados
     * @return boolean Si no existen ninguna devuelve false, si no true
     */
    public boolean esReferenciada(Marca marca){
        List<Modelo> lstModelos;
        em = getEntityManager();
        
        String queryString = "SELECT modelo FROM Modelo modelo "
                + "WHERE modelo.marca = :marca";
        Query q = em.createQuery(queryString)
                .setParameter("marca", marca);
        lstModelos = q.getResultList();
        return !lstModelos.isEmpty();        
    }
    
    /**
     * Metodo sobreescrito para obtener todas las Marcas registradas pero ordenadas alfabéticamente
     * @return List<Marca> listado de las marcas registradas
     */
    @Override
    public List<Marca> findAll(){
        em = getEntityManager();
        String queryString = "SELECT marca FROM Marca marca "
                + "ORDER BY marca.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }

    /**
     * Método para obtener una Marca según su nombre, si existe
     * @param nombre String Nombre de la Marca que se busca
     * @return Marca Marca existente
     */
    public Marca getExistente(String nombre) {
        List<Marca> lstMarcas;
        em = getEntityManager();
        
        String queryString = "SELECT marca FROM Marca marca "
                + "WHERE marca.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        lstMarcas = q.getResultList();
        if(lstMarcas.isEmpty()){
            return null;
        }else{
            return lstMarcas.get(0);
        }
    }
    
    /**
     * Método para obtener los modelos vinculados a una Marca
     * Solo para la API
     * @param id Long Identificacor único de la Marca
     * @return List<Modelo> listado de los Modelos registrados para la Marca en cuestión
     */
    public List<Modelo> getModelos(Long id){
        em = getEntityManager();
        String queryString = "SELECT modelo FROM Modelo modelo "
                + "WHERE modelo.marca.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList();
    }
}
