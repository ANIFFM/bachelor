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
@Table(name = "retrospective")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Retrospective.findAll", query = "SELECT r FROM Retrospective r"),
    @NamedQuery(name = "Retrospective.findByRetrospectiveID", query = "SELECT r FROM Retrospective r WHERE r.retrospectiveID = :retrospectiveID")})
public class Retrospective implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "retrospectiveID")
    private Integer retrospectiveID;

    public Retrospective() {
    }

    public Retrospective(Integer retrospectiveID) {
        this.retrospectiveID = retrospectiveID;
    }

    public Integer getRetrospectiveID() {
        return retrospectiveID;
    }

    public void setRetrospectiveID(Integer retrospectiveID) {
        this.retrospectiveID = retrospectiveID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (retrospectiveID != null ? retrospectiveID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Retrospective)) {
            return false;
        }
        Retrospective other = (Retrospective) object;
        if ((this.retrospectiveID == null && other.retrospectiveID != null) || (this.retrospectiveID != null && !this.retrospectiveID.equals(other.retrospectiveID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.colenet.scrumtool.entityclasses.Retrospective[ retrospectiveID=" + retrospectiveID + " ]";
    }
    
}
