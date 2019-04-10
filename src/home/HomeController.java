/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import business.manager.BusinessManager;
import business.manager.ScreenChangeListener;
import business.manager.ScreenHandler;
import entity_classes.Expenses;
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
    @FXML
    private Label expensesLabel;
    
    //queries
     private final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("Business_ManagerPU");
    
    private final EntityManager entityManager = 
            entityManagerFactory.createEntityManager();
    
    //Query query = entityManager.createNamedQuery("SELECT t FROM Invoices t ORDER BY t.id DESC");
    
    private final TypedQuery<Invoices> getInvoiceAmount = 
        entityManager.createQuery("SELECT t FROM Invoices t ORDER BY t.id DESC", Invoices.class);
    
    private final TypedQuery<Expenses> getExpenseAmount = 
            entityManager.createQuery("SELECT e FROM Expenses e ORDER BY e.id DESC", Expenses.class);
        
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //INCOME
        queryAndDisplayIncome();
       
       //EXPENSES
       queryAndDisplayExpenses();
    }    

    @Override
    public void setParentScreen(ScreenHandler currentPage) {
        screenController = currentPage;
    }
    
    @FXML
    public void goToInvoice(){
        screenController.setScreen(BusinessManager.invoiceID);
    }
    
    @FXML
    public void goToExpenses(){
        screenController.setScreen(BusinessManager.expensesID);
    }
    
    private void queryAndDisplayIncome(){
       incomeLabel.setText(currency.format(BigDecimal.ZERO.setScale(
                2, RoundingMode.HALF_UP))); //display zero unless subTotals below is true
       
       List<Invoices> subTotals = getInvoiceAmount.setMaxResults(1).getResultList();
       if(subTotals != null){
       subTotals.forEach(invoice -> {
           incomeLabel.setText(currency.format(invoice.getRunningTotal().setScale(
                2, RoundingMode.HALF_UP)));
           System.out.println(invoice.getRunningTotal());});
       }
    }
    
    private void queryAndDisplayExpenses(){
        expensesLabel.setText(currency.format(BigDecimal.ZERO.setScale(
                2, RoundingMode.HALF_UP)));
       List<Expenses> expensesSubTotal = getExpenseAmount.setMaxResults(1).getResultList();
       if(expensesSubTotal != null){
           expensesSubTotal.forEach(expense -> {
               expensesLabel.setText(currency.format(expense.getRunningTotal()));//.setScale(0, RoundingMode.CEILING)));
           });
       }

    }
    
}
