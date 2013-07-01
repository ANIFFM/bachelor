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
@Table(name = "taskboard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Taskboard.findAll", query = "SELECT t FROM Taskboard t"),
    @NamedQuery(name = "Taskboard.findByTaskboardID", query = "SELECT t FROM Taskboard t WHERE t.taskboardID = :taskboardID")})
public class Taskboard implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "taskboardID")
    private Integer taskboardID;

    public Taskboard() {
    }

    public Taskboard(Integer taskboardID) {
        this.taskboardID = taskboardID;
    }

    public Integer getTaskboardID() {
        return taskboardID;
    }

    public void setTaskboardID(Integer taskboardID) {
        this.taskboardID = taskboardID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskboardID != null ? taskboardID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Taskboard)) {
            return false;
        }
        Taskboard other = (Taskboard) object;
        if ((this.taskboardID == null && other.taskboardID != null) || (this.taskboardID != null && !this.taskboardID.equals(other.taskboardID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.colenet.scrumtool.entityclasses.Taskboard[ taskboardID=" + taskboardID + " ]";
    }
    
}
