/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.colenet.scrumtool.sessionbeans;

import de.colenet.scrumtool.entityclasses.Theme;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author TAL
 */
@Stateless
public class ThemeFacade extends AbstractFacade<Theme> {
    @PersistenceContext(unitName = "ScrumWebManagementPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ThemeFacade() {
        super(Theme.class);
    }
    
}
