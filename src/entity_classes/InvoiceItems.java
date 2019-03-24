/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity_classes;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author BUKOLA
 */
@Entity
@Table(name = "INVOICE_ITEMS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvoiceItems.findAll", query = "SELECT i FROM InvoiceItems i")
    , @NamedQuery(name = "InvoiceItems.findByItemNo", query = "SELECT i FROM InvoiceItems i WHERE i.itemNo = :itemNo")
    , @NamedQuery(name = "InvoiceItems.findByInvoiceNo", query = "SELECT i FROM InvoiceItems i WHERE i.invoiceNo = :invoiceNo")
    , @NamedQuery(name = "InvoiceItems.findByDescription", query = "SELECT i FROM InvoiceItems i WHERE i.description = :description")
    , @NamedQuery(name = "InvoiceItems.findByPrice", query = "SELECT i FROM InvoiceItems i WHERE i.price = :price")
    , @NamedQuery(name = "InvoiceItems.findByAmount", query = "SELECT i FROM InvoiceItems i WHERE i.amount = :amount")
    , @NamedQuery(name = "InvoiceItems.findByQuantity", query = "SELECT i FROM InvoiceItems i WHERE i.quantity = :quantity")})
public class InvoiceItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ITEM_NO")
    private Integer itemNo;
    @Basic(optional = false)
    @Column(name = "INVOICE_NO")
    private String invoiceNo;
    @Column(name = "DESCRIPTION")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "PRICE")
    private BigDecimal price;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne(optional = false)
    private Customers customerId;

    public InvoiceItems() {
    }

    public InvoiceItems(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public InvoiceItems(Integer itemNo, String invoiceNo, BigDecimal price, BigDecimal amount) {
        this.itemNo = itemNo;
        this.invoiceNo = invoiceNo;
        this.price = price;
        this.amount = amount;
    }

    public Integer getItemNo() {
        return itemNo;
    }

    public void setItemNo(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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
        hash += (itemNo != null ? itemNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceItems)) {
            return false;
        }
        InvoiceItems other = (InvoiceItems) object;
        if ((this.itemNo == null && other.itemNo != null) || (this.itemNo != null && !this.itemNo.equals(other.itemNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity_classes.InvoiceItems[ itemNo=" + itemNo + " ]";
    }
    
}
