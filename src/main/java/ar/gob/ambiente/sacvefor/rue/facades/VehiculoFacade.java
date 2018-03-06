
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Vehiculo;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

/**
 * Clase que implementa la abstracta para el acceso a datos de la entidad Vehículo.
 * @author rincostante
 */
@Stateless
public class VehiculoFacade extends AbstractFacade<Vehiculo> {

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
    public VehiculoFacade() {
        super(Vehiculo.class);
    }
    
    /**
     * Método para obtener una Persona según su CUIT, si existe
     * @param matricula String matrícula del vehículo a obtener
     * @return Vehículo vehículo consultado
     */
    public Vehiculo getExistente(String matricula) {
        List<Vehiculo> lstVehiculos;
        em = getEntityManager();
        
        String queryString = "SELECT veh FROM Vehiculo veh "
                + "WHERE veh.matricula = :matricula";
        Query q = em.createQuery(queryString)
                .setParameter("matricula", matricula);
        lstVehiculos = q.getResultList();
        if(lstVehiculos.isEmpty()){
            return null;
        }else{
            return lstVehiculos.get(0);
        }
    }       
    
    /**
     * Método que obtiene uno o más vehículos según su titular
     * @param cuit Long cuit del titular del vehículo
     * @return List<Vehiculo> Vehículos vinculados al titular cuyo cuit se recibió
     */
    public List<Vehiculo> getByTitular(Long cuit){
        List<Vehiculo> lstVehiculos;
        em = getEntityManager();
        
        String queryString = "SELECT veh FROM Vehiculo veh "
                + "WHERE veh.empresa.cuit = :cuit "
                + "ORDER BY veh.modelo.marca.nombre";
        Query q = em.createQuery(queryString)
                .setParameter("cuit", cuit);
        lstVehiculos = q.getResultList();
        return lstVehiculos;
    }
    
    /**
     * Método que devuelve los Vehículos habilitados
     * Sin distinción del tipo.
     * @return List<Vehiculo> Listado de los vehículos habilitados
     */
    public List<Vehiculo> getHabilitadas(){
        List<Vehiculo> lstVehiculos;
        em = getEntityManager();
        
        String queryString = "SELECT veh FROM Vehiculo veh "
                + "WHERE veh.habilitado = true "
                + "ORDER BY veh.modelo.marca.nombre";
        Query q = em.createQuery(queryString);
        lstVehiculos = q.getResultList();
        return lstVehiculos;
    }   
    
    /**
     * Método sobreescrito de la AbstractFacade que obtiene todos los vehículos ordenados alfabéticamente por su Marca
     * @return List<Vehiculo> Listado de los vehículos ordenados por su Marca
     */
    @Override
    public List<Vehiculo> findAll(){
        em = getEntityManager();
        String queryString = "SELECT veh FROM Vehiculo veh "
                + "ORDER BY veh.modelo.marca.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }    
    
    /**
     * Método para obtener todas las revisiones de la entidad
     * @param matricula String matrícula del vehículo a consultar sus revisiones
     * @return List<Vehiculo> Listado de las revisiones del Vehículo cuya matrícula se recibió
     */
    public List<Vehiculo> findRevisions(String matricula){
        List<Vehiculo> lstClientes = new ArrayList<>();
        Vehiculo veh = this.getExistente(matricula);
        if(veh != null){
            Long id = this.getExistente(matricula).getId();
            AuditReader reader = AuditReaderFactory.get(getEntityManager());
            List<Number> revisions = reader.getRevisions(Vehiculo.class, id);
            for (Number n : revisions) {
                Vehiculo cli = reader.find(Vehiculo.class, id, n);
                cli.setFechaRevision(reader.getRevisionDate(n));
                Hibernate.initialize(cli.getModelo());
                Hibernate.initialize(cli.getEmpresa());
                lstClientes.add(cli);
            }
        }
        return lstClientes;
    }   
}
