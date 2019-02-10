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
@Table(name = "INVOICES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoices.findAll", query = "SELECT i FROM Invoices i")
    , @NamedQuery(name = "Invoices.findByDate", query = "SELECT i FROM Invoices i WHERE i.date = :date")
    , @NamedQuery(name = "Invoices.findByInvoiceNo", query = "SELECT i FROM Invoices i WHERE i.invoiceNo = :invoiceNo")
    , @NamedQuery(name = "Invoices.findByFilepath", query = "SELECT i FROM Invoices i WHERE i.filepath = :filepath")
    , @NamedQuery(name = "Invoices.findByStatus", query = "SELECT i FROM Invoices i WHERE i.status = :status")
    , @NamedQuery(name = "Invoices.findByTotal", query = "SELECT i FROM Invoices i WHERE i.total = :total")
    , @NamedQuery(name = "Invoices.findByRunningtotal", query = "SELECT i FROM Invoices i WHERE i.runningtotal = :runningtotal")})
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
    @Column(name = "FILEPATH")
    private String filepath;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @Column(name = "TOTAL")
    private long total;
    @Basic(optional = false)
    @Column(name = "RUNNINGTOTAL")
    private long runningtotal;

    public Invoices() {
    }

    public Invoices(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Invoices(String invoiceNo, Date date, String filepath, String status, long total, long runningtotal) {
        this.invoiceNo = invoiceNo;
        this.date = date;
        this.filepath = filepath;
        this.status = status;
        this.total = total;
        this.runningtotal = runningtotal;
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

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
