/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.colenet.scrumtool.sessionbeans;

import de.colenet.scrumtool.entityclasses.Tasks;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author TAL
 */
@Stateless
public class TasksFacade extends AbstractFacade<Tasks> {
    @PersistenceContext(unitName = "ScrumWebManagementPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TasksFacade() {
        super(Tasks.class);
    }
    
}
