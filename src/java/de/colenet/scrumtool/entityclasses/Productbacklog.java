/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.colenet.scrumtool.entityclasses;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author TAL
 */
@Entity
@Table(name = "productbacklog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productbacklog.findAll", query = "SELECT p FROM Productbacklog p"),
    @NamedQuery(name = "Productbacklog.findByProductBacklogID", query = "SELECT p FROM Productbacklog p WHERE p.productBacklogID = :productBacklogID"),
    @NamedQuery(name = "Productbacklog.findByProductBacklogName", query = "SELECT p FROM Productbacklog p WHERE p.productBacklogName = :productBacklogName"),
    @NamedQuery(name = "Productbacklog.findByProductBacklogDescription", query = "SELECT p FROM Productbacklog p WHERE p.productBacklogDescription = :productBacklogDescription")})
public class Productbacklog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "productBacklogID")
    private Integer productBacklogID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "productBacklogName")
    private String productBacklogName;
    @Size(max = 45)
    @Column(name = "productBacklogDescription")
    private String productBacklogDescription;
    @OneToMany(mappedBy = "userstoryBelongsToProductBacklog")
    private Collection<Userstory> userstoryCollection;
    @JoinColumn(name = "productBacklogBelongsToProject", referencedColumnName = "projectID")
    @ManyToOne
    private Project productBacklogBelongsToProject;

    public Productbacklog() {
    }

    public Productbacklog(Integer productBacklogID) {
        this.productBacklogID = productBacklogID;
    }

    public Productbacklog(Integer productBacklogID, String productBacklogName) {
        this.productBacklogID = productBacklogID;
        this.productBacklogName = productBacklogName;
    }

    public Integer getProductBacklogID() {
        return productBacklogID;
    }

    public void setProductBacklogID(Integer productBacklogID) {
        this.productBacklogID = productBacklogID;
    }

    public String getProductBacklogName() {
        return productBacklogName;
    }

    public void setProductBacklogName(String productBacklogName) {
        this.productBacklogName = productBacklogName;
    }

    public String getProductBacklogDescription() {
        return productBacklogDescription;
    }

    public void setProductBacklogDescription(String productBacklogDescription) {
        this.productBacklogDescription = productBacklogDescription;
    }

    @XmlTransient
    public Collection<Userstory> getUserstoryCollection() {
        return userstoryCollection;
    }

    public void setUserstoryCollection(Collection<Userstory> userstoryCollection) {
        this.userstoryCollection = userstoryCollection;
    }

    public Project getProductBacklogBelongsToProject() {
        return productBacklogBelongsToProject;
    }

    public void setProductBacklogBelongsToProject(Project productBacklogBelongsToProject) {
        this.productBacklogBelongsToProject = productBacklogBelongsToProject;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productBacklogID != null ? productBacklogID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productbacklog)) {
            return false;
        }
        Productbacklog other = (Productbacklog) object;
        if ((this.productBacklogID == null && other.productBacklogID != null) || (this.productBacklogID != null && !this.productBacklogID.equals(other.productBacklogID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.colenet.scrumtool.entityclasses.Productbacklog[ productBacklogID=" + productBacklogID + " ]";
    }
    
}
