
package ar.gob.ambiente.sacvefor.rue.facades;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * Clase abstracta que es implementada por todas las facade de acceso a datos para todas las entidades.
 * @author rincostante
 */
public abstract class AbstractFacade<T> {

    /**
     * Variable privada: Clase para gestionar su acceso a datos
     */    
    private Class<T> entityClass;

    /**
     * Constructor, recibe la clase para gestionar su acceso a datos
     * @param entityClass 
     */ 
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Método abstracto a implementar todas las facade de acceso a datos para todas las entidades
     * para obtener el EntityManager
     * @return EntityManager para acceder a datos
     */      
    protected abstract EntityManager getEntityManager();

    /**
     * Método para crear una entidad en la base de datos
     * @param entity Entidad a crear
     */      
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Método para editar una entidad existente
     * @param entity Entidad a editar
     */    
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Método para eliminar una entidad existente
     * @param entity Entidad a eliminar
     */    
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     * Método para buscar una entidad según su id
     * @param id Long identificador único de la entidad
     * @return Entidad encontrada o null
     */    
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Método para obtener todas las entidades del mismo tipo registradas en la base de datos
     * @return List<T> Listado con las entidades obtenidas
     */    
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Método para obtener el conjunto de entidades de un mismo tipo cuyas id se encuentran dentro del rango especificado
     * @param range Array de enteros que especifican el rango dentro del cual se ubicarán las id de las entidades a leer
     * @return List<T> Listado con las entidades obtenidas
     */    
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * Método que obtiene la cantidad de entidades de un mismo tipo registradas en la base de datos
     * @return Entero que indica el número de entidades leídas
     */    
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
