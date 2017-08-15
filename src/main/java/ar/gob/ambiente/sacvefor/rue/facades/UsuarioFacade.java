
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Usuario;
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
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "svf_ruePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    /**
     * Método para verificar la existencia de Usuarios referidos por algún Vehículo, Persona o Domicilio.
     * @param usuario
     * @return Si no existen ninguna devuelve false, si no true
     */
    public boolean esReferenciado(Usuario usuario){
        boolean result = false;
        em = getEntityManager();
        String queryString = "";
        Query q;
        
        // Vehículos
        queryString = "SELECT veh FROM Vehiculo veh "
                + "WHERE veh.usuario = :usuario";
        q = em.createQuery(queryString)
                .setParameter("usuario", usuario);
        if(!q.getResultList().isEmpty()){
            result = true;
        }

        // Personas
        queryString = "SELECT per FROM Persona per "
                + "WHERE per.usuario = :usuario";
        q = em.createQuery(queryString)
                .setParameter("usuario", usuario);
        if(!q.getResultList().isEmpty()){
            result = true;
        }
        
        // Domicilio
        queryString = "SELECT dom FROM Domicilio dom "
                + "WHERE dom.usuario = :usuario";
        q = em.createQuery(queryString)
                .setParameter("usuario", usuario);
        if(!q.getResultList().isEmpty()){
            result = true;
        }
        
        return result;        
    }    
    
    /**
     * Método para obtener un Modelo según su nombre, si existe
     * @param nombre
     * @return 
     */
    public Usuario getExistente(String nombre) {
        List<Usuario> lstUsuarios;
        em = getEntityManager();
        
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        lstUsuarios = q.getResultList();
        if(lstUsuarios.isEmpty()){
            return null;
        }else{
            return lstUsuarios.get(0);
        }
    }
    
    /**
     * Método que devuelve las Usuarios habilitados
     * Sin distinción del tipo.
     * @return 
     */
    public List<Usuario> getHabilitados(){
        List<Usuario> lstUsuarios;
        em = getEntityManager();
        
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.habilitado = true "
                + "ORDER BY us.nombreCompleto";
        Query q = em.createQuery(queryString);
        lstUsuarios = q.getResultList();
        return lstUsuarios;
    }    
    
    @Override
    public List<Usuario> findAll(){
        em = getEntityManager();
        String queryString = "SELECT us FROM Usuario us "
                + "ORDER BY us.nombreCompleto";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    } 
    
    /**
     * Método para obtener el Usuario para la API REST de la Provincia cuyo id se recibe
     * @param idProv : id de la Provincia en el Servicio Territorial
     * @return 
     */
    public Usuario getByProvincia(Long idProv){
        List<Usuario> lstUsuarios;
        em = getEntityManager();
        
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.idProvincia = :idProv";
        Query q = em.createQuery(queryString)
                .setParameter("idProv", idProv);
        lstUsuarios = q.getResultList();
        if(lstUsuarios.isEmpty()){
            return null;
        }else{
            return lstUsuarios.get(0);
        } 
    }
    
    /**
     * Método para obtener todas las revisiones de la entidad
     * @param nombreAud
     * @return 
     */
    public List<Usuario> findRevisions(String nombreAud){
        List<Usuario> lstClientes = new ArrayList<>();
        Usuario us = this.getExistente(nombreAud);
        if(us != null){
            Long id = this.getExistente(nombreAud).getId();
            AuditReader reader = AuditReaderFactory.get(getEntityManager());
            List<Number> revisions = reader.getRevisions(Usuario.class, id);
            for (Number n : revisions) {
                Usuario cli = reader.find(Usuario.class, id, n);
                cli.setFechaRevision(reader.getRevisionDate(n));
                Hibernate.initialize(cli.getRol());
                lstClientes.add(cli);
            }
        }
        return lstClientes;
    }      
}
