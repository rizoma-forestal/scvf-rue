
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
 *
 * @author rincostante
 */
@Stateless
public class VehiculoFacade extends AbstractFacade<Vehiculo> {

    @PersistenceContext(unitName = "svf_ruePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VehiculoFacade() {
        super(Vehiculo.class);
    }
    
    /**
     * Método para obtener una Persona según su CUIT, si existe
     * @param matricula
     * @return 
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
     * @return 
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
     * @param matricula
     * @return 
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
