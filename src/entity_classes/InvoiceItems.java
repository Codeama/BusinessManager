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
    , @NamedQuery(name = "InvoiceItems.findByDescription", query = "SELECT i FROM InvoiceItems i WHERE i.description = :description")
    , @NamedQuery(name = "InvoiceItems.findByQuantity", query = "SELECT i FROM InvoiceItems i WHERE i.quantity = :quantity")
    , @NamedQuery(name = "InvoiceItems.findByPrice", query = "SELECT i FROM InvoiceItems i WHERE i.price = :price")
    , @NamedQuery(name = "InvoiceItems.findByAmount", query = "SELECT i FROM InvoiceItems i WHERE i.amount = :amount")})
public class InvoiceItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ITEM_NO")
    private Integer itemNo;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "QUANTITY")
    private Integer quantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "PRICE")
    private BigDecimal price;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne(optional = false)
    private Customers customerId;
    @JoinColumn(name = "INVOICE_NO", referencedColumnName = "INVOICE_NO")
    @ManyToOne(optional = false)
    private Invoices invoiceNo;

    public InvoiceItems() {
    }

    public InvoiceItems(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public InvoiceItems(Integer itemNo, BigDecimal price, BigDecimal amount) {
        this.itemNo = itemNo;
        this.price = price;
        this.amount = amount;
    }

    public Integer getItemNo() {
        return itemNo;
    }

    public void setItemNo(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public Customers getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customers customerId) {
        this.customerId = customerId;
    }

    public Invoices getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(Invoices invoiceNo) {
        this.invoiceNo = invoiceNo;
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
