/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import entity_classes.Customers;
import java.math.BigDecimal;
import java.util.Date;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author BUKOLA
 */
public class InvoicesDAOBean {

    private SimpleObjectProperty<Date> date;
    //private SimpleStringProperty filePath;
    private SimpleStringProperty status;
    private SimpleObjectProperty<BigDecimal> total;
    //private SimpleObjectProperty<BigDecimal> runningTotal;
    //private SimpleIntegerProperty id;
    private SimpleStringProperty invoiceNo;
    private SimpleObjectProperty<Customers> customerId;
    
    
    public InvoicesDAOBean(Date date, String status, BigDecimal total, 
            String invoiceNo, Customers customerId) {
        this.date = new SimpleObjectProperty(date);
        this.status = new SimpleStringProperty(status);
        this.total = new SimpleObjectProperty(total);
        this.invoiceNo = new SimpleStringProperty(invoiceNo);
        this.customerId = new SimpleObjectProperty(customerId);
    }
    
    

    

    public Date getDate() {
        return date.get();
    }
    
    public SimpleObjectProperty getDateProperty() {
        return date;
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public String getStatus() {
        return status.get();
    }
    
    public SimpleStringProperty getStatusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public BigDecimal getTotal() {
        return total.get();
    }
    
    public SimpleObjectProperty getTotalProperty() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total.set(total);
    }

    public String getInvoiceNo() {
        return invoiceNo.get();
    }
    
    public SimpleStringProperty getInvoiceNoProperty() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo.set(invoiceNo);
    }

    public Customers getCustomerId() {
        return customerId.get();
    }

    public void setCustomerId(Customers customerId) {
        this.customerId.set(customerId);
    }
    
    public SimpleObjectProperty getCustomerIdProperty() {
        return customerId;
    }
    

    
}
