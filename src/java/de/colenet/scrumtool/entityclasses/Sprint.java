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
@Table(name = "sprint")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sprint.findAll", query = "SELECT s FROM Sprint s"),
    @NamedQuery(name = "Sprint.findBySprintID", query = "SELECT s FROM Sprint s WHERE s.sprintID = :sprintID")})
public class Sprint implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sprintID")
    private Integer sprintID;

    public Sprint() {
    }

    public Sprint(Integer sprintID) {
        this.sprintID = sprintID;
    }

    public Integer getSprintID() {
        return sprintID;
    }

    public void setSprintID(Integer sprintID) {
        this.sprintID = sprintID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sprintID != null ? sprintID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sprint)) {
            return false;
        }
        Sprint other = (Sprint) object;
        if ((this.sprintID == null && other.sprintID != null) || (this.sprintID != null && !this.sprintID.equals(other.sprintID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.colenet.scrumtool.entityclasses.Sprint[ sprintID=" + sprintID + " ]";
    }
    
}
