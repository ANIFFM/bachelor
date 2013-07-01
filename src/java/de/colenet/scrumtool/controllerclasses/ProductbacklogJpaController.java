/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.colenet.scrumtool.controllerclasses;

import de.colenet.scrumtool.controllerclasses.exceptions.NonexistentEntityException;
import de.colenet.scrumtool.controllerclasses.exceptions.RollbackFailureException;
import de.colenet.scrumtool.entityclasses.Productbacklog;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import de.colenet.scrumtool.entityclasses.Project;
import de.colenet.scrumtool.entityclasses.Userstory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author TAL
 */
public class ProductbacklogJpaController implements Serializable {

    public ProductbacklogJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Productbacklog productbacklog) throws RollbackFailureException, Exception {
        if (productbacklog.getUserstoryCollection() == null) {
            productbacklog.setUserstoryCollection(new ArrayList<Userstory>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Project productBacklogBelongsToProject = productbacklog.getProductBacklogBelongsToProject();
            if (productBacklogBelongsToProject != null) {
                productBacklogBelongsToProject = em.getReference(productBacklogBelongsToProject.getClass(), productBacklogBelongsToProject.getProjectID());
                productbacklog.setProductBacklogBelongsToProject(productBacklogBelongsToProject);
            }
            Collection<Userstory> attachedUserstoryCollection = new ArrayList<Userstory>();
            for (Userstory userstoryCollectionUserstoryToAttach : productbacklog.getUserstoryCollection()) {
                userstoryCollectionUserstoryToAttach = em.getReference(userstoryCollectionUserstoryToAttach.getClass(), userstoryCollectionUserstoryToAttach.getUserstoryID());
                attachedUserstoryCollection.add(userstoryCollectionUserstoryToAttach);
            }
            productbacklog.setUserstoryCollection(attachedUserstoryCollection);
            em.persist(productbacklog);
            if (productBacklogBelongsToProject != null) {
                productBacklogBelongsToProject.getProductbacklogCollection().add(productbacklog);
                productBacklogBelongsToProject = em.merge(productBacklogBelongsToProject);
            }
            for (Userstory userstoryCollectionUserstory : productbacklog.getUserstoryCollection()) {
                Productbacklog oldUserstoryBelongsToProductBacklogOfUserstoryCollectionUserstory = userstoryCollectionUserstory.getUserstoryBelongsToProductBacklog();
                userstoryCollectionUserstory.setUserstoryBelongsToProductBacklog(productbacklog);
                userstoryCollectionUserstory = em.merge(userstoryCollectionUserstory);
                if (oldUserstoryBelongsToProductBacklogOfUserstoryCollectionUserstory != null) {
                    oldUserstoryBelongsToProductBacklogOfUserstoryCollectionUserstory.getUserstoryCollection().remove(userstoryCollectionUserstory);
                    oldUserstoryBelongsToProductBacklogOfUserstoryCollectionUserstory = em.merge(oldUserstoryBelongsToProductBacklogOfUserstoryCollectionUserstory);
                }
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

    public void edit(Productbacklog productbacklog) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Productbacklog persistentProductbacklog = em.find(Productbacklog.class, productbacklog.getProductBacklogID());
            Project productBacklogBelongsToProjectOld = persistentProductbacklog.getProductBacklogBelongsToProject();
            Project productBacklogBelongsToProjectNew = productbacklog.getProductBacklogBelongsToProject();
            Collection<Userstory> userstoryCollectionOld = persistentProductbacklog.getUserstoryCollection();
            Collection<Userstory> userstoryCollectionNew = productbacklog.getUserstoryCollection();
            if (productBacklogBelongsToProjectNew != null) {
                productBacklogBelongsToProjectNew = em.getReference(productBacklogBelongsToProjectNew.getClass(), productBacklogBelongsToProjectNew.getProjectID());
                productbacklog.setProductBacklogBelongsToProject(productBacklogBelongsToProjectNew);
            }
            Collection<Userstory> attachedUserstoryCollectionNew = new ArrayList<Userstory>();
            for (Userstory userstoryCollectionNewUserstoryToAttach : userstoryCollectionNew) {
                userstoryCollectionNewUserstoryToAttach = em.getReference(userstoryCollectionNewUserstoryToAttach.getClass(), userstoryCollectionNewUserstoryToAttach.getUserstoryID());
                attachedUserstoryCollectionNew.add(userstoryCollectionNewUserstoryToAttach);
            }
            userstoryCollectionNew = attachedUserstoryCollectionNew;
            productbacklog.setUserstoryCollection(userstoryCollectionNew);
            productbacklog = em.merge(productbacklog);
            if (productBacklogBelongsToProjectOld != null && !productBacklogBelongsToProjectOld.equals(productBacklogBelongsToProjectNew)) {
                productBacklogBelongsToProjectOld.getProductbacklogCollection().remove(productbacklog);
                productBacklogBelongsToProjectOld = em.merge(productBacklogBelongsToProjectOld);
            }
            if (productBacklogBelongsToProjectNew != null && !productBacklogBelongsToProjectNew.equals(productBacklogBelongsToProjectOld)) {
                productBacklogBelongsToProjectNew.getProductbacklogCollection().add(productbacklog);
                productBacklogBelongsToProjectNew = em.merge(productBacklogBelongsToProjectNew);
            }
            for (Userstory userstoryCollectionOldUserstory : userstoryCollectionOld) {
                if (!userstoryCollectionNew.contains(userstoryCollectionOldUserstory)) {
                    userstoryCollectionOldUserstory.setUserstoryBelongsToProductBacklog(null);
                    userstoryCollectionOldUserstory = em.merge(userstoryCollectionOldUserstory);
                }
            }
            for (Userstory userstoryCollectionNewUserstory : userstoryCollectionNew) {
                if (!userstoryCollectionOld.contains(userstoryCollectionNewUserstory)) {
                    Productbacklog oldUserstoryBelongsToProductBacklogOfUserstoryCollectionNewUserstory = userstoryCollectionNewUserstory.getUserstoryBelongsToProductBacklog();
                    userstoryCollectionNewUserstory.setUserstoryBelongsToProductBacklog(productbacklog);
                    userstoryCollectionNewUserstory = em.merge(userstoryCollectionNewUserstory);
                    if (oldUserstoryBelongsToProductBacklogOfUserstoryCollectionNewUserstory != null && !oldUserstoryBelongsToProductBacklogOfUserstoryCollectionNewUserstory.equals(productbacklog)) {
                        oldUserstoryBelongsToProductBacklogOfUserstoryCollectionNewUserstory.getUserstoryCollection().remove(userstoryCollectionNewUserstory);
                        oldUserstoryBelongsToProductBacklogOfUserstoryCollectionNewUserstory = em.merge(oldUserstoryBelongsToProductBacklogOfUserstoryCollectionNewUserstory);
                    }
                }
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
                Integer id = productbacklog.getProductBacklogID();
                if (findProductbacklog(id) == null) {
                    throw new NonexistentEntityException("The productbacklog with id " + id + " no longer exists.");
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
            Productbacklog productbacklog;
            try {
                productbacklog = em.getReference(Productbacklog.class, id);
                productbacklog.getProductBacklogID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productbacklog with id " + id + " no longer exists.", enfe);
            }
            Project productBacklogBelongsToProject = productbacklog.getProductBacklogBelongsToProject();
            if (productBacklogBelongsToProject != null) {
                productBacklogBelongsToProject.getProductbacklogCollection().remove(productbacklog);
                productBacklogBelongsToProject = em.merge(productBacklogBelongsToProject);
            }
            Collection<Userstory> userstoryCollection = productbacklog.getUserstoryCollection();
            for (Userstory userstoryCollectionUserstory : userstoryCollection) {
                userstoryCollectionUserstory.setUserstoryBelongsToProductBacklog(null);
                userstoryCollectionUserstory = em.merge(userstoryCollectionUserstory);
            }
            em.remove(productbacklog);
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

    public List<Productbacklog> findProductbacklogEntities() {
        return findProductbacklogEntities(true, -1, -1);
    }

    public List<Productbacklog> findProductbacklogEntities(int maxResults, int firstResult) {
        return findProductbacklogEntities(false, maxResults, firstResult);
    }

    private List<Productbacklog> findProductbacklogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Productbacklog.class));
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

    public Productbacklog findProductbacklog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Productbacklog.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductbacklogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Productbacklog> rt = cq.from(Productbacklog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
