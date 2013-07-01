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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author TAL
 */
@Entity
@Table(name = "userstory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Userstory.findAll", query = "SELECT u FROM Userstory u"),
    @NamedQuery(name = "Userstory.findByUserstoryID", query = "SELECT u FROM Userstory u WHERE u.userstoryID = :userstoryID"),
    @NamedQuery(name = "Userstory.findByUserstoryName", query = "SELECT u FROM Userstory u WHERE u.userstoryName = :userstoryName"),
    @NamedQuery(name = "Userstory.findByUserstoryDescription", query = "SELECT u FROM Userstory u WHERE u.userstoryDescription = :userstoryDescription"),
    @NamedQuery(name = "Userstory.findByUserstoryPriority", query = "SELECT u FROM Userstory u WHERE u.userstoryPriority = :userstoryPriority")})
public class Userstory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "userstoryID")
    private Integer userstoryID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "userstoryName")
    private String userstoryName;
    @Size(max = 50)
    @Column(name = "userstoryDescription")
    private String userstoryDescription;
    @Column(name = "userstoryPriority")
    private Integer userstoryPriority;
    @JoinColumn(name = "userstoryBelongsToProductBacklog", referencedColumnName = "productBacklogID")
    @ManyToOne
    private Productbacklog userstoryBelongsToProductBacklog;

    public Userstory() {
    }

    public Userstory(Integer userstoryID) {
        this.userstoryID = userstoryID;
    }

    public Userstory(Integer userstoryID, String userstoryName) {
        this.userstoryID = userstoryID;
        this.userstoryName = userstoryName;
    }

    public Integer getUserstoryID() {
        return userstoryID;
    }

    public void setUserstoryID(Integer userstoryID) {
        this.userstoryID = userstoryID;
    }

    public String getUserstoryName() {
        return userstoryName;
    }

    public void setUserstoryName(String userstoryName) {
        this.userstoryName = userstoryName;
    }

    public String getUserstoryDescription() {
        return userstoryDescription;
    }

    public void setUserstoryDescription(String userstoryDescription) {
        this.userstoryDescription = userstoryDescription;
    }

    public Integer getUserstoryPriority() {
        return userstoryPriority;
    }

    public void setUserstoryPriority(Integer userstoryPriority) {
        this.userstoryPriority = userstoryPriority;
    }
    
    public Productbacklog getUserstoryBelongsToProductBacklog() {
        return userstoryBelongsToProductBacklog;
    }

    public void setUserstoryBelongsToProductBacklog(Productbacklog userstoryBelongsToProductBacklog) {
        this.userstoryBelongsToProductBacklog = userstoryBelongsToProductBacklog;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userstoryID != null ? userstoryID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Userstory)) {
            return false;
        }
        Userstory other = (Userstory) object;
        if ((this.userstoryID == null && other.userstoryID != null) || (this.userstoryID != null && !this.userstoryID.equals(other.userstoryID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.colenet.scrumtool.entityclasses.Userstory[ userstoryID=" + userstoryID + " ]";
    }
    
}
