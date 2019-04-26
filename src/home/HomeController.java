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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

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
     @FXML
    private Label profitLabel;
     
     @FXML
     private Circle homeProfile;
     
     @FXML
     private ImageView imageView;
    
    //queries
     private final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("Business_ManagerPU");
    
    private final EntityManager entityManager = 
            entityManagerFactory.createEntityManager();
    
    //Query query = entityManager.createNamedQuery("SELECT t FROM Invoices t ORDER BY t.id DESC");
    
    private final TypedQuery<Invoices> getInvoiceAmount = 
        entityManager.createQuery("SELECT i FROM Invoices i ORDER BY i.id DESC", Invoices.class);
    
    private final TypedQuery<Expenses> getExpenseAmount = 
            entityManager.createQuery("SELECT e FROM Expenses e ORDER BY e.id DESC", Expenses.class);
    
    private final TypedQuery<Invoices> getAllInvoices = 
            entityManager.createNamedQuery("Invoices.findAll", Invoices.class);
    
    private final TypedQuery<Expenses> getAllExpenses = 
            entityManager.createNamedQuery("Expenses.findAll", Expenses.class);
    
        
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //INCOME
        queryAndDisplayIncome();
       
       //EXPENSES
       queryAndDisplayExpenses();
       
       //PROFITS
       
       
       System.out.println(getExpenseAmount);
       queryAndDisplayProfits();
       
       //PROFILE IMAGE
       //Image image = new Image("initial.png");
       imageView.setImage(new Image(getClass().getResourceAsStream("initial.png")));
       //homeProfile.setFill(new ImagePattern(new Image("initial.png")));
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
       
       List<Invoices> incomeSubTotals = getInvoiceAmount.setMaxResults(1).getResultList();
       if(incomeSubTotals != null){
       incomeSubTotals.forEach(invoice -> {
           incomeLabel.setText(currency.format(invoice.getRunningTotal().setScale(
                2, RoundingMode.HALF_UP)));
           System.out.println(invoice.getRunningTotal());});
       }
    }
    
    private void queryAndDisplayExpenses(){
        expensesLabel.setText(currency.format(BigDecimal.ZERO.setScale(
                2, RoundingMode.HALF_UP))); //display zero unless expensesSubTotal below is true
       List<Expenses> expensesSubTotal = getExpenseAmount.setMaxResults(1).getResultList();
       if(expensesSubTotal != null){
           expensesSubTotal.forEach(expense -> {
               expensesLabel.setText(currency.format(expense.getRunningTotal()));//.setScale(0, RoundingMode.CEILING)));
           });
       }

    }
    
    private void queryAndDisplayProfits(){
        List<Invoices> invoicesList = getAllInvoices.getResultList();
        List<Expenses> expensesList = getAllExpenses.getResultList();
        List<BigDecimal> wages = new ArrayList<>();
        List<BigDecimal> expenses = new ArrayList<>();
        //System.out.println("Invoices List: "+ invoicesList);
        invoicesList.forEach(invoice -> {wages.add(invoice.getTotal());});
        expensesList.forEach(expense -> {expenses.add(expense.getTotal());});
        wages.forEach(wage -> System.out.println("Wages list: "+ wage));
        com.bukola.Profits profits = new com.bukola.Profits(wages, expenses);
        profits.calculateEarnings();
        //profits.getTotalEarnings();
        
        //display
        profitLabel.setText(currency.format(BigDecimal.ZERO.setScale(
                2, RoundingMode.HALF_UP)));
        if(profits.getEarningsList() != null)
            profitLabel.setText(currency.format(profits.getTotalEarnings()));
    }
    
}
