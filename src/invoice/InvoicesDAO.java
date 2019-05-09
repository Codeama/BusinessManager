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
import javafx.scene.control.ComboBox;

/**
 *
 * @author BUKOLA
 */
public class InvoicesDAO {
    //unique autogenerated ID for Invoices entity
    private SimpleObjectProperty<Integer> id;
    private SimpleObjectProperty<Date> date;
    //private SimpleStringProperty filePath;
    private SimpleStringProperty status;
    private SimpleObjectProperty<BigDecimal> total;
    private SimpleObjectProperty<BigDecimal> runningTotal;
    private SimpleStringProperty invoiceNo;
    private SimpleObjectProperty<Customers> customerId;
    private SimpleStringProperty emailAddress;
    private SimpleObjectProperty<ComboBox<String>> actions;
    
    public InvoicesDAO(Integer id, Date date, String invoiceNo, String emailAddress, 
             String status, BigDecimal total, BigDecimal runningTotal, ComboBox<String> actions) {
        this.date = new SimpleObjectProperty(date);
        this.status = new SimpleStringProperty(status);
        this.total = new SimpleObjectProperty(total);
        this.invoiceNo = new SimpleStringProperty(invoiceNo);
        this.emailAddress = new SimpleStringProperty(emailAddress);
        this.actions = new SimpleObjectProperty(actions);
        //this.customerId = new SimpleObjectProperty(customerId);
        this.id = new SimpleObjectProperty(id);
        this.runningTotal = new SimpleObjectProperty(runningTotal);
    }
    
    public Integer getId() {
        return id.get();
    }
    
    public SimpleObjectProperty getIdProperty() {
        return id;
    }

    public void setId(Integer id) {
        this.id.set(id);
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

    public BigDecimal getRunningTotal() {
        return runningTotal.get();
    }
    
    public SimpleObjectProperty getRunningTotalProperty() {
        return runningTotal;
    }

    public void setRunningTotal(BigDecimal runningTotal) {
        this.runningTotal.set(runningTotal);
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

    public String getEmailAddress() {
        return emailAddress.get();
    }
    
    public SimpleStringProperty getEmailAddressProperty() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress.set(emailAddress);
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
    
    public ComboBox<String> getActions() {
        return actions.get();
    }
    
    public SimpleObjectProperty getActionsProperty() {
        return actions;
    }

    public void setActions(ComboBox<String> actions) {
        this.actions.set(actions);
    }
    

    
}