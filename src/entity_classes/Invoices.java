/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity_classes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

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
    , @NamedQuery(name = "Invoices.findByFilePath", query = "SELECT i FROM Invoices i WHERE i.filePath = :filePath")
    , @NamedQuery(name = "Invoices.findByStatus", query = "SELECT i FROM Invoices i WHERE i.status = :status")
    , @NamedQuery(name = "Invoices.findByTotal", query = "SELECT i FROM Invoices i WHERE i.total = :total")
    , @NamedQuery(name = "Invoices.findByRunningTotal", query = "SELECT i FROM Invoices i WHERE i.runningTotal = :runningTotal")
    , @NamedQuery(name = "Invoices.findById", query = "SELECT i FROM Invoices i WHERE i.id = :id")
    , @NamedQuery(name = "Invoices.findByInvoiceNo", query = "SELECT i FROM Invoices i WHERE i.invoiceNo = :invoiceNo")})
public class Invoices implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "INVOICE_NO")
    private String invoiceNo;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne(optional = false)
    private Customers customerId;

    public Invoices() {
    }

    public Invoices(Integer id) {
        this.id = id;
    }

    public Invoices(Integer id, Date date, String filePath, String status, BigDecimal total, BigDecimal runningTotal, String invoiceNo) {
        this.id = id;
        this.date = date;
        this.filePath = filePath;
        this.status = status;
        this.total = total;
        this.runningTotal = runningTotal;
        this.invoiceNo = invoiceNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoices)) {
            return false;
        }
        Invoices other = (Invoices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity_classes.Invoices[ id=" + id + " ]";
    }
    
}
