/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import varastonhallinta.domain.EntityClass;
import varastonhallinta.domain.ValidationException;
import varastonhallinta.logic.exceptions.NonexistentEntityException;
import varastonhallinta.ui.exceptions.AddEntityException;

/**
 *
 * @author tanel
 * @param <E>
 */
public abstract class JPAController<E extends EntityClass> implements Serializable {
    
    protected EntityManagerFactory emf = null;
    private Class<? extends E> classObject;

    public JPAController(Class<? extends E> classObject, EntityManagerFactory emf) {
        this.classObject = classObject;
        this.emf = emf;
    }

    /**
     *
     * @return EntityManager
     */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     *
     * @param entity
     * @throws varastonhallinta.domain.ValidationException
     */
    public void create(E entity) throws ValidationException, AddEntityException {
        System.out.println("CREATE " + entity);
        EntityManager em = null;
        entity.validate();
        
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            System.out.println("CREATE SUCCESSFULL " + entity);
        }catch(Exception e){
            throw new AddEntityException(e.getMessage());
        } 
        finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     *
     * @param entity
     * @throws NonexistentEntityException
     * @throws Exception
     */
    public void edit(E entity) throws NonexistentEntityException, ValidationException {
        System.out.println("EDIT " + entity);
        EntityManager em = null;
        try {
            entity.validate();
            em = getEntityManager();
            em.getTransaction().begin();
            entity = em.merge(entity);
            em.getTransaction().commit();
            System.out.println("EDIT SUCCESSFULL " + entity);
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = entity.getID();
                if (findEntity(id) == null) {
                    throw new NonexistentEntityException("The entity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     *
     * @param id
     * @throws NonexistentEntityException
     */
    public void destroy(int id) throws NonexistentEntityException {
        System.out.println("DESTROY id:" + id);
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            E entity;
            try {
                entity = em.getReference(classObject, id);
                em.remove(entity);
                em.getTransaction().commit();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entity with id " + id + " no longer exists.");
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     *
     * @return
     */
    public List<E> findEntities() {
        return findEntityEntities(true, -1, -1);
    }

    /**
     *
     * @param maxResults
     * @param firstResult
     * @return
     */
    public List<E> findEntityEntities(int maxResults, int firstResult) {
        return findEntityEntities(false, maxResults, firstResult);
    }

    protected List<E> findEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(classObject));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public E findEntity(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(classObject, id);
        } finally {
            em.close();
        }
    }
    

    /**
     *
     * @return
     */
    public int getEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<E> rt = cq.from(classObject);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
