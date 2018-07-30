
package ar.gob.ambiente.sacvefor.rue.facades;

import ar.gob.ambiente.sacvefor.rue.enitites.Persona;
import ar.gob.ambiente.sacvefor.rue.tipos.TipoPersona;
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
 * Clase que implementa la abstracta para el acceso a datos de la entidad Persona.
 * @author rincostante
 */
@Stateless
public class PersonaFacade extends AbstractFacade<Persona> {

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
    public PersonaFacade() {
        super(Persona.class);
    }
    
    /**
     * Método para obtener una Persona según su CUIT, si existe
     * @param cuit Long cuit de cuya Persona se consulta
     * @return Persona Persona vinculada al cuit recibido
     */
    public Persona getExistente(Long cuit) {
        List<Persona> lstPersonas;
        em = getEntityManager();
        
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.cuit = :cuit";
        Query q = em.createQuery(queryString)
                .setParameter("cuit", cuit);
        lstPersonas = q.getResultList();
        if(lstPersonas.isEmpty()){
            return null;
        }else{
            return lstPersonas.get(0);
        }
    }   
    
    /**
     * Método que devuelve las Personas habilitadas
     * Sin distinción del tipo.
     * @return List<Persona> listado de las personas habilitadas
     */
    public List<Persona> getHabilitadas(){
        List<Persona> lstPersonas;
        em = getEntityManager();
        
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.habilitado = true "
                + "ORDER BY per.nombreCompleto, per.razonSocial";
        Query q = em.createQuery(queryString);
        lstPersonas = q.getResultList();
        return lstPersonas;
    }
    
    /**
     * Método que devuleve las Personas Físicas ordenadas por nombre completo
     * @return List<Persona> listado de las Personas físicas registradas.
     */
    public List<Persona> getFisicas(){
        List<Persona> lstPersonas;
        em = getEntityManager();
        
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.tipo = :tipo "
                + "ORDER BY per.nombreCompleto";
        Query q = em.createQuery(queryString)
                .setParameter("tipo", TipoPersona.FISICA);
        lstPersonas = q.getResultList();
        return lstPersonas;
    }
    
    /**
     * Método que devuleve las Personas Físicas habilitadas ordenadas por nombre completo
     * @return List<Persona> listado de los Personas físicas habilitadas
     */
    public List<Persona> getFisicasHab(){
        List<Persona> lstPersonas;
        em = getEntityManager();
        
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.tipo = :tipo "
                + "AND per.habilitado = true "
                + "ORDER BY per.nombreCompleto";
        Query q = em.createQuery(queryString)
                .setParameter("tipo", TipoPersona.FISICA);
        lstPersonas = q.getResultList();
        return lstPersonas;
    }    
    
    /**
     * Método que devuleve las Personas Jurídicas ordenadas por razón social
     * @return List<Persona> listado de las Personas jurídicas
     */
    public List<Persona> getJuridicas(){
        List<Persona> lstPersonas;
        em = getEntityManager();
        
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.tipo = :tipo "
                + "ORDER BY per.razonSocial";
        Query q = em.createQuery(queryString)
                .setParameter("tipo", TipoPersona.JURIDICA);
        lstPersonas = q.getResultList();
        return lstPersonas;
    }    
    
    /**
     * Método que devuleve las Personas Jurídicas habilitadas ordenadas por razón social
     * @return List<Persona> listado de las Personas jurídicas habilitadas
     */
    public List<Persona> getJuridicasHab(){
        List<Persona> lstPersonas;
        em = getEntityManager();
        
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.tipo = :tipo "
                + "AND per.habilitado = true "
                + "ORDER BY per.razonSocial";
        Query q = em.createQuery(queryString)
                .setParameter("tipo", TipoPersona.JURIDICA);
        lstPersonas = q.getResultList();
        return lstPersonas;
    }     
    
    /**
     * Método que devuelve las Personas físicas o jurídicas cuya entidad sea "EMPRESA DE TRANSPORTE"
     * Solo para la app porque desde la API está el método getPersonas(Long id) que devuelve las Personas
     * del Tipo de Entidad cuyo id se recibe como parámetro.
     * @return List<Persona> listado de las Empresas de transporte
     */
    public List<Persona> getEmpresasTransporteHab(){
        List<Persona> lstPersonas;
        em = getEntityManager();
        
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.entidad.nombre = 'EMPRESA DE TRANSPORTE' "
                + "AND per.habilitado = true "
                + "ORDER BY per.nombreCompleto, per.razonSocial";
        Query q = em.createQuery(queryString);
        lstPersonas = q.getResultList();
        return lstPersonas;
    }
    
    /**
     * Método para obtener todas las revisiones de la entidad
     * @param cuitAud Long cuit de la Persona auditada
     * @return List<Persona> listado de las revisiones de una Persona
     */
    public List<Persona> findRevisions(Long cuitAud){
        List<Persona> lstClientes = new ArrayList<>();
        Persona per = this.getExistente(cuitAud);
        if(per != null){
            Long id = this.getExistente(cuitAud).getId();
            AuditReader reader = AuditReaderFactory.get(getEntityManager());
            List<Number> revisions = reader.getRevisions(Persona.class, id);
            for (Number n : revisions) {
                Persona cli = reader.find(Persona.class, id, n);
                cli.setFechaRevision(reader.getRevisionDate(n));
                Hibernate.initialize(cli.getTipo());
                Hibernate.initialize(cli.getTipoSociedad());
                Hibernate.initialize(cli.getEntidad());
                Hibernate.initialize(cli.getDomicilio());
                lstClientes.add(cli);
            }
        }
        return lstClientes;
    }       
}
