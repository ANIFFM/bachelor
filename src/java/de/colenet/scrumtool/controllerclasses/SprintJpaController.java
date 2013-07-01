/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.colenet.scrumtool.controllerclasses;

import de.colenet.scrumtool.controllerclasses.exceptions.NonexistentEntityException;
import de.colenet.scrumtool.controllerclasses.exceptions.RollbackFailureException;
import de.colenet.scrumtool.entityclasses.Sprint;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author TAL
 */
public class SprintJpaController implements Serializable {

    public SprintJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sprint sprint) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(sprint);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sprint sprint) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            sprint = em.merge(sprint);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sprint.getSprintID();
                if (findSprint(id) == null) {
                    throw new NonexistentEntityException("The sprint with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Sprint sprint;
            try {
                sprint = em.getReference(Sprint.class, id);
                sprint.getSprintID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sprint with id " + id + " no longer exists.", enfe);
            }
            em.remove(sprint);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sprint> findSprintEntities() {
        return findSprintEntities(true, -1, -1);
    }

    public List<Sprint> findSprintEntities(int maxResults, int firstResult) {
        return findSprintEntities(false, maxResults, firstResult);
    }

    private List<Sprint> findSprintEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sprint.class));
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

    public Sprint findSprint(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sprint.class, id);
        } finally {
            em.close();
        }
    }

    public int getSprintCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sprint> rt = cq.from(Sprint.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
