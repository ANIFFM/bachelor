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
@Table(name = "sprintbacklog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sprintbacklog.findAll", query = "SELECT s FROM Sprintbacklog s"),
    @NamedQuery(name = "Sprintbacklog.findBySprintbacklogID", query = "SELECT s FROM Sprintbacklog s WHERE s.sprintbacklogID = :sprintbacklogID")})
public class Sprintbacklog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sprintbacklogID")
    private Integer sprintbacklogID;

    public Sprintbacklog() {
    }

    public Sprintbacklog(Integer sprintbacklogID) {
        this.sprintbacklogID = sprintbacklogID;
    }

    public Integer getSprintbacklogID() {
        return sprintbacklogID;
    }

    public void setSprintbacklogID(Integer sprintbacklogID) {
        this.sprintbacklogID = sprintbacklogID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sprintbacklogID != null ? sprintbacklogID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sprintbacklog)) {
            return false;
        }
        Sprintbacklog other = (Sprintbacklog) object;
        if ((this.sprintbacklogID == null && other.sprintbacklogID != null) || (this.sprintbacklogID != null && !this.sprintbacklogID.equals(other.sprintbacklogID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.colenet.scrumtool.entityclasses.Sprintbacklog[ sprintbacklogID=" + sprintbacklogID + " ]";
    }
    
}
