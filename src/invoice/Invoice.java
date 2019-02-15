/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import entity_classes.Invoices;
import entity_classes.Invoices;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author BUKOLA
 */
public class Invoice {
    EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("Business_ManagerPU");
    
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    
    public void addEntry(){
        Invoices invoice = new Invoices();
        //Date date = new Date();
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        invoice.setDate(date);
        invoice.setInvoiceNo("INV0002");
        invoice.setFilePath("nothingSaved2");
        invoice.setStatus("SENT");
        invoice.setTotal(new BigDecimal(250.75));
        invoice.setRunningTotal(new BigDecimal(250));
        EntityTransaction transaction = entityManager.getTransaction();
        try
      {
         transaction.begin(); // start transaction
         entityManager.persist(invoice); // store new entry
         transaction.commit(); // commit changes to the database
         System.out.println("Entry Added, " +
            "New entry successfully added.");
      }
      catch (Exception e) // if transaction failed
      {
         transaction.rollback(); // undo database operations 
         System.out.println("Entry Not Added. " + 
            "Unable to add entry: " + e);
      }
    }
    
    public void displayInvoice(){
        //Invoices invoice = new Invoices();
        TypedQuery<Invoices> displayAll = entityManager.createNamedQuery(
                "Invoices.findAll", Invoices.class);
        displayAll.getResultList().forEach(item -> {
            System.out.println(item.getDate());
            System.out.println(item.getInvoiceNo());
            System.out.println(item.getFilePath());
            System.out.println(item.getStatus());
            System.out.println(item.getTotal());
            System.out.println(item.getRunningTotal());
                });
//        Invoices inv = new Invoices();
//        
//            System.out.printf("InvNo:%s%n Date: %s%n FilePath: "
//                    + "%s%n Status: %s%n Total: %s%n RunningTotal: %s%n", 
//                    inv.getDate(), inv.getInvoiceNo(), inv.getFilepath(),
//                    inv.getStatus(), inv.getTotal(), inv.getRunningtotal());
//        //}
    }
    
    public static void main(String[] args){
        Invoice inv = new Invoice();
        inv.addEntry();
        inv.displayInvoice();
    }
    
}
