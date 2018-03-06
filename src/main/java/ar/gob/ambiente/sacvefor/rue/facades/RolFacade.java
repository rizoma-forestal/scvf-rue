
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Rol;
import ar.gob.ambiente.sacvefor.rue.enitites.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase que implementa la abstracta para el acceso a datos de la entidad Rol.
 * @author rincostante
 */
@Stateless
public class RolFacade extends AbstractFacade<Rol> {

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
    public RolFacade() {
        super(Rol.class);
    }
    
    /**
     * Método para verificar la existencia de Roles referidas por algún Usuario.
     * @param rol Rol cuyos usuarios se consultan
     * @return boolen Si no existen ninguna devuelve false, si no true
     */
    public boolean esReferenciada(Rol rol){
        List<Usuario> lstUsuarios;
        em = getEntityManager();
        
        String queryString = "SELECT usuario FROM Usuario usuario "
                + "WHERE usuario.rol = :rol";
        Query q = em.createQuery(queryString)
                .setParameter("rol", rol);
        lstUsuarios = q.getResultList();
        return !lstUsuarios.isEmpty();        
    }    
    
    /**
     * Método para obtener un Rol según su nombre, si existe
     * @param nombre String nombre del usuario consultado
     * @return Rol Rol cuyo nombre se busca
     */
    public Rol getExistente(String nombre) {
        List<Rol> lstRoles;
        em = getEntityManager();
        
        String queryString = "SELECT rol FROM Rol rol "
                + "WHERE rol.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        lstRoles = q.getResultList();
        if(lstRoles.isEmpty()){
            return null;
        }else{
            return lstRoles.get(0);
        }
    }    
}
