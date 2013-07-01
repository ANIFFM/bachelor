/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.colenet.scrumtool.entityclasses;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author TAL
 */
@Entity
@Table(name = "burndown")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Burndown.findAll", query = "SELECT b FROM Burndown b"),
    @NamedQuery(name = "Burndown.findByBurndownID", query = "SELECT b FROM Burndown b WHERE b.burndownID = :burndownID")})
public class Burndown implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "burndownID")
    private Integer burndownID;

    public Burndown() {
    }

    public Burndown(Integer burndownID) {
        this.burndownID = burndownID;
    }

    public Integer getBurndownID() {
        return burndownID;
    }

    public void setBurndownID(Integer burndownID) {
        this.burndownID = burndownID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (burndownID != null ? burndownID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Burndown)) {
            return false;
        }
        Burndown other = (Burndown) object;
        if ((this.burndownID == null && other.burndownID != null) || (this.burndownID != null && !this.burndownID.equals(other.burndownID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.colenet.scrumtool.entityclasses.Burndown[ burndownID=" + burndownID + " ]";
    }
    
}
