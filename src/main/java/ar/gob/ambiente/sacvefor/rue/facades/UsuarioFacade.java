
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
 * Clase que implementa la abstracta para el acceso a datos de la entidad Usuario.
 * @author rincostante
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

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
     * Cosntructor
     */
    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    /**
     * Método para verificar la existencia de Usuarios referidos por algún Vehículo, Persona o Domicilio.
     * @param usuario Usuario del cual se buscan las referencias
     * @return boolean Si no existen ninguna devuelve false, si no true
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
     * Método para obtener un Usuario según su nombre, si existe
     * @param nombre String nombre del Usuario
     * @return Usuario el Usuario buscado o null, en caso de no existir
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
     * @return List<Usuario> listado de los usuarios habilitados
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
    
    /**
     * Método de AbstractFacade sobreescrito para que devuelva todos los usurios ordenados alfabeticamente por su nombre completo
     * @return List<Usuario> listado de todos los usuarios ordenados
     */
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
     * @param idProv Long id de la Provincia en el Servicio Territorial
     * @return Usuario Usuario de la Provincia cuyo id se recibe o null si no existe
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
     * Método que valida que el usuario recibido está registrado como usuario de la API
     * @param nombre String Nombre del usuario recibido, enviado por le cliente.
     * @return boolean Verdadero o falso según el caso
     */
    public boolean validarUsuarioApi(String nombre){
        em = getEntityManager();
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.nombre = :nombre "
                + "AND us.rol.nombre = 'API'";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return !q.getResultList().isEmpty();
    }    
    
    /**
     * Método para obtener todas las revisiones de la entidad
     * @param nombreAud String nombre del usuario a consultar sus revisiones
     * @return List<Usuario> Listado de las revisiones del Usuario cuyo nombre se recibió
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
