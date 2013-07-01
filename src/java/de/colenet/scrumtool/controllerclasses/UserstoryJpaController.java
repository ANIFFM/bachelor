/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.colenet.scrumtool.controllerclasses;

import de.colenet.scrumtool.controllerclasses.exceptions.NonexistentEntityException;
import de.colenet.scrumtool.controllerclasses.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import de.colenet.scrumtool.entityclasses.Productbacklog;
import de.colenet.scrumtool.entityclasses.Userstory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author TAL
 */
public class UserstoryJpaController implements Serializable {

    public UserstoryJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Userstory userstory) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Productbacklog userstoryBelongsToProductBacklog = userstory.getUserstoryBelongsToProductBacklog();
            if (userstoryBelongsToProductBacklog != null) {
                userstoryBelongsToProductBacklog = em.getReference(userstoryBelongsToProductBacklog.getClass(), userstoryBelongsToProductBacklog.getProductBacklogID());
                userstory.setUserstoryBelongsToProductBacklog(userstoryBelongsToProductBacklog);
            }
            em.persist(userstory);
            if (userstoryBelongsToProductBacklog != null) {
                userstoryBelongsToProductBacklog.getUserstoryCollection().add(userstory);
                userstoryBelongsToProductBacklog = em.merge(userstoryBelongsToProductBacklog);
            }
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

    public void edit(Userstory userstory) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Userstory persistentUserstory = em.find(Userstory.class, userstory.getUserstoryID());
            Productbacklog userstoryBelongsToProductBacklogOld = persistentUserstory.getUserstoryBelongsToProductBacklog();
            Productbacklog userstoryBelongsToProductBacklogNew = userstory.getUserstoryBelongsToProductBacklog();
            if (userstoryBelongsToProductBacklogNew != null) {
                userstoryBelongsToProductBacklogNew = em.getReference(userstoryBelongsToProductBacklogNew.getClass(), userstoryBelongsToProductBacklogNew.getProductBacklogID());
                userstory.setUserstoryBelongsToProductBacklog(userstoryBelongsToProductBacklogNew);
            }
            userstory = em.merge(userstory);
            if (userstoryBelongsToProductBacklogOld != null && !userstoryBelongsToProductBacklogOld.equals(userstoryBelongsToProductBacklogNew)) {
                userstoryBelongsToProductBacklogOld.getUserstoryCollection().remove(userstory);
                userstoryBelongsToProductBacklogOld = em.merge(userstoryBelongsToProductBacklogOld);
            }
            if (userstoryBelongsToProductBacklogNew != null && !userstoryBelongsToProductBacklogNew.equals(userstoryBelongsToProductBacklogOld)) {
                userstoryBelongsToProductBacklogNew.getUserstoryCollection().add(userstory);
                userstoryBelongsToProductBacklogNew = em.merge(userstoryBelongsToProductBacklogNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userstory.getUserstoryID();
                if (findUserstory(id) == null) {
                    throw new NonexistentEntityException("The userstory with id " + id + " no longer exists.");
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
            Userstory userstory;
            try {
                userstory = em.getReference(Userstory.class, id);
                userstory.getUserstoryID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userstory with id " + id + " no longer exists.", enfe);
            }
            Productbacklog userstoryBelongsToProductBacklog = userstory.getUserstoryBelongsToProductBacklog();
            if (userstoryBelongsToProductBacklog != null) {
                userstoryBelongsToProductBacklog.getUserstoryCollection().remove(userstory);
                userstoryBelongsToProductBacklog = em.merge(userstoryBelongsToProductBacklog);
            }
            em.remove(userstory);
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

    public List<Userstory> findUserstoryEntities() {
        return findUserstoryEntities(true, -1, -1);
    }

    public List<Userstory> findUserstoryEntities(int maxResults, int firstResult) {
        return findUserstoryEntities(false, maxResults, firstResult);
    }

    private List<Userstory> findUserstoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Userstory.class));
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

    public Userstory findUserstory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Userstory.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserstoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Userstory> rt = cq.from(Userstory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
