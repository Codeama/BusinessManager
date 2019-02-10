/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity_classes;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author BUKOLA
 */
@Entity
@Table(name = "EXPENSES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Expenses.findAll", query = "SELECT e FROM Expenses e")
    , @NamedQuery(name = "Expenses.findById", query = "SELECT e FROM Expenses e WHERE e.id = :id")
    , @NamedQuery(name = "Expenses.findByDate", query = "SELECT e FROM Expenses e WHERE e.date = :date")
    , @NamedQuery(name = "Expenses.findByDescription", query = "SELECT e FROM Expenses e WHERE e.description = :description")
    , @NamedQuery(name = "Expenses.findByCategory", query = "SELECT e FROM Expenses e WHERE e.category = :category")
    , @NamedQuery(name = "Expenses.findByReceipt", query = "SELECT e FROM Expenses e WHERE e.receipt = :receipt")
    , @NamedQuery(name = "Expenses.findByTotal", query = "SELECT e FROM Expenses e WHERE e.total = :total")
    , @NamedQuery(name = "Expenses.findByRunningtotal", query = "SELECT e FROM Expenses e WHERE e.runningtotal = :runningtotal")})
public class Expenses implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "RECEIPT")
    private String receipt;
    @Basic(optional = false)
    @Column(name = "TOTAL")
    private long total;
    @Basic(optional = false)
    @Column(name = "RUNNINGTOTAL")
    private long runningtotal;

    public Expenses() {
    }

    public Expenses(Integer id) {
        this.id = id;
    }

    public Expenses(Integer id, Date date, String description, long total, long runningtotal) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.total = total;
        this.runningtotal = runningtotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getRunningtotal() {
        return runningtotal;
    }

    public void setRunningtotal(long runningtotal) {
        this.runningtotal = runningtotal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Expenses)) {
            return false;
        }
        Expenses other = (Expenses) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity_classes.Expenses[ id=" + id + " ]";
    }
    
}
