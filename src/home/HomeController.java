/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import business.manager.BusinessManager;
import business.manager.ScreenChangeListener;
import business.manager.ScreenHandler;
import entity_classes.Invoices;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
/**
 * FXML Controller class
 *
 * @author BUKOLA
 */
public class HomeController implements Initializable, ScreenChangeListener {

    ScreenHandler screenController;
    private final NumberFormat currency = NumberFormat.getCurrencyInstance();
    @FXML private Label incomeLabel;
    
    //queries
     private final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("Business_ManagerPU");
    
    private final EntityManager entityManager = 
            entityManagerFactory.createEntityManager();
    
    //Query query = entityManager.createNamedQuery("SELECT t FROM Invoices t ORDER BY t.id DESC");
    
        private final TypedQuery<Invoices> getInvoiceAmount = 
            entityManager.createQuery("SELECT t FROM Invoices t ORDER BY t.id DESC", Invoices.class);
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        incomeLabel.setText(currency.format(BigDecimal.ZERO.setScale(
                2, RoundingMode.HALF_UP)));
        
        //BigDecimal current = query.setMaxResults(1).getSingleResult();
        
       List<Invoices> subTotals = getInvoiceAmount.setMaxResults(1).getResultList();
       subTotals.forEach(invoice -> {
           incomeLabel.setText(currency.format(invoice.getRunningTotal().setScale(
                2, RoundingMode.HALF_UP)));
           System.out.println(invoice.getRunningTotal());});

        //System.out.println(getInvoiceAmount.getSingleResult().getRunningTotal());
        //System.out.println(getInvoiceAmount.getResultList());
    }    

    @Override
    public void setParentScreen(ScreenHandler currentPage) {
        screenController = currentPage;
    }
    
    @FXML
    public void goToInvoice(){
        screenController.setScreen(BusinessManager.invoiceID);
    }
    
}
