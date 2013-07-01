/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.colenet.scrumtool.sessionbeans;

import de.colenet.scrumtool.entityclasses.Retrospective;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author TAL
 */
@Stateless
public class RetrospectiveFacade extends AbstractFacade<Retrospective> {
    @PersistenceContext(unitName = "ScrumWebManagementPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RetrospectiveFacade() {
        super(Retrospective.class);
    }
    
}
