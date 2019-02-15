/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity_classes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author BUKOLA
 */
@Entity
@Table(name = "INVOICES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoices.findAll", query = "SELECT i FROM Invoices i")
    , @NamedQuery(name = "Invoices.findByDate", query = "SELECT i FROM Invoices i WHERE i.date = :date")
    , @NamedQuery(name = "Invoices.findByInvoiceNo", query = "SELECT i FROM Invoices i WHERE i.invoiceNo = :invoiceNo")
    , @NamedQuery(name = "Invoices.findByFilePath", query = "SELECT i FROM Invoices i WHERE i.filePath = :filePath")
    , @NamedQuery(name = "Invoices.findByStatus", query = "SELECT i FROM Invoices i WHERE i.status = :status")
    , @NamedQuery(name = "Invoices.findByTotal", query = "SELECT i FROM Invoices i WHERE i.total = :total")
    , @NamedQuery(name = "Invoices.findByRunningTotal", query = "SELECT i FROM Invoices i WHERE i.runningTotal = :runningTotal")})
public class Invoices implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Id
    @Basic(optional = false)
    @Column(name = "INVOICE_NO")
    private String invoiceNo;
    @Basic(optional = false)
    @Column(name = "FILE_PATH")
    private String filePath;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Basic(optional = false)
    @Column(name = "RUNNING_TOTAL")
    private BigDecimal runningTotal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoiceNo")
    private Collection<InvoiceItems> invoiceItemsCollection;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne(optional = false)
    private Customers customerId;

    public Invoices() {
    }

    public Invoices(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Invoices(String invoiceNo, Date date, String filePath, String status, BigDecimal total, BigDecimal runningTotal) {
        this.invoiceNo = invoiceNo;
        this.date = date;
        this.filePath = filePath;
        this.status = status;
        this.total = total;
        this.runningTotal = runningTotal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getRunningTotal() {
        return runningTotal;
    }

    public void setRunningTotal(BigDecimal runningTotal) {
        this.runningTotal = runningTotal;
    }

    @XmlTransient
    public Collection<InvoiceItems> getInvoiceItemsCollection() {
        return invoiceItemsCollection;
    }

    public void setInvoiceItemsCollection(Collection<InvoiceItems> invoiceItemsCollection) {
        this.invoiceItemsCollection = invoiceItemsCollection;
    }

    public Customers getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customers customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceNo != null ? invoiceNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoices)) {
            return false;
        }
        Invoices other = (Invoices) object;
        if ((this.invoiceNo == null && other.invoiceNo != null) || (this.invoiceNo != null && !this.invoiceNo.equals(other.invoiceNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity_classes.Invoices[ invoiceNo=" + invoiceNo + " ]";
    }
    
}
