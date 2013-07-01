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
import de.colenet.scrumtool.entityclasses.Project;
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
public class ProjectJpaController implements Serializable {

    public ProjectJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Project project) throws RollbackFailureException, Exception {
        if (project.getProductbacklogCollection() == null) {
            project.setProductbacklogCollection(new ArrayList<Productbacklog>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Productbacklog> attachedProductbacklogCollection = new ArrayList<Productbacklog>();
            for (Productbacklog productbacklogCollectionProductbacklogToAttach : project.getProductbacklogCollection()) {
                productbacklogCollectionProductbacklogToAttach = em.getReference(productbacklogCollectionProductbacklogToAttach.getClass(), productbacklogCollectionProductbacklogToAttach.getProductBacklogID());
                attachedProductbacklogCollection.add(productbacklogCollectionProductbacklogToAttach);
            }
            project.setProductbacklogCollection(attachedProductbacklogCollection);
            em.persist(project);
            for (Productbacklog productbacklogCollectionProductbacklog : project.getProductbacklogCollection()) {
                Project oldProductBacklogBelongsToProjectOfProductbacklogCollectionProductbacklog = productbacklogCollectionProductbacklog.getProductBacklogBelongsToProject();
                productbacklogCollectionProductbacklog.setProductBacklogBelongsToProject(project);
                productbacklogCollectionProductbacklog = em.merge(productbacklogCollectionProductbacklog);
                if (oldProductBacklogBelongsToProjectOfProductbacklogCollectionProductbacklog != null) {
                    oldProductBacklogBelongsToProjectOfProductbacklogCollectionProductbacklog.getProductbacklogCollection().remove(productbacklogCollectionProductbacklog);
                    oldProductBacklogBelongsToProjectOfProductbacklogCollectionProductbacklog = em.merge(oldProductBacklogBelongsToProjectOfProductbacklogCollectionProductbacklog);
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

    public void edit(Project project) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Project persistentProject = em.find(Project.class, project.getProjectID());
            Collection<Productbacklog> productbacklogCollectionOld = persistentProject.getProductbacklogCollection();
            Collection<Productbacklog> productbacklogCollectionNew = project.getProductbacklogCollection();
            Collection<Productbacklog> attachedProductbacklogCollectionNew = new ArrayList<Productbacklog>();
            for (Productbacklog productbacklogCollectionNewProductbacklogToAttach : productbacklogCollectionNew) {
                productbacklogCollectionNewProductbacklogToAttach = em.getReference(productbacklogCollectionNewProductbacklogToAttach.getClass(), productbacklogCollectionNewProductbacklogToAttach.getProductBacklogID());
                attachedProductbacklogCollectionNew.add(productbacklogCollectionNewProductbacklogToAttach);
            }
            productbacklogCollectionNew = attachedProductbacklogCollectionNew;
            project.setProductbacklogCollection(productbacklogCollectionNew);
            project = em.merge(project);
            for (Productbacklog productbacklogCollectionOldProductbacklog : productbacklogCollectionOld) {
                if (!productbacklogCollectionNew.contains(productbacklogCollectionOldProductbacklog)) {
                    productbacklogCollectionOldProductbacklog.setProductBacklogBelongsToProject(null);
                    productbacklogCollectionOldProductbacklog = em.merge(productbacklogCollectionOldProductbacklog);
                }
            }
            for (Productbacklog productbacklogCollectionNewProductbacklog : productbacklogCollectionNew) {
                if (!productbacklogCollectionOld.contains(productbacklogCollectionNewProductbacklog)) {
                    Project oldProductBacklogBelongsToProjectOfProductbacklogCollectionNewProductbacklog = productbacklogCollectionNewProductbacklog.getProductBacklogBelongsToProject();
                    productbacklogCollectionNewProductbacklog.setProductBacklogBelongsToProject(project);
                    productbacklogCollectionNewProductbacklog = em.merge(productbacklogCollectionNewProductbacklog);
                    if (oldProductBacklogBelongsToProjectOfProductbacklogCollectionNewProductbacklog != null && !oldProductBacklogBelongsToProjectOfProductbacklogCollectionNewProductbacklog.equals(project)) {
                        oldProductBacklogBelongsToProjectOfProductbacklogCollectionNewProductbacklog.getProductbacklogCollection().remove(productbacklogCollectionNewProductbacklog);
                        oldProductBacklogBelongsToProjectOfProductbacklogCollectionNewProductbacklog = em.merge(oldProductBacklogBelongsToProjectOfProductbacklogCollectionNewProductbacklog);
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
                Integer id = project.getProjectID();
                if (findProject(id) == null) {
                    throw new NonexistentEntityException("The project with id " + id + " no longer exists.");
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
            Project project;
            try {
                project = em.getReference(Project.class, id);
                project.getProjectID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The project with id " + id + " no longer exists.", enfe);
            }
            Collection<Productbacklog> productbacklogCollection = project.getProductbacklogCollection();
            for (Productbacklog productbacklogCollectionProductbacklog : productbacklogCollection) {
                productbacklogCollectionProductbacklog.setProductBacklogBelongsToProject(null);
                productbacklogCollectionProductbacklog = em.merge(productbacklogCollectionProductbacklog);
            }
            em.remove(project);
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

    public List<Project> findProjectEntities() {
        return findProjectEntities(true, -1, -1);
    }

    public List<Project> findProjectEntities(int maxResults, int firstResult) {
        return findProjectEntities(false, maxResults, firstResult);
    }

    private List<Project> findProjectEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Project.class));
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

    public Project findProject(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Project.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjectCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Project> rt = cq.from(Project.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
