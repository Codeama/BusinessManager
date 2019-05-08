/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expenses;

import business.manager.BusinessManager;
import business.manager.ScreenChangeListener;
import business.manager.ScreenHandler;
import entity_classes.Expenses;
import entity_classes.Invoices;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
//import com.bukola.*;

/**
 * FXML Controller class
 *
 * @author BUKOLA
 */
public class ExpensesController implements Initializable, ScreenChangeListener {
    
    ScreenHandler screenController;
    @FXML private Circle expensesProfile;
    
    @FXML private DatePicker dateField;

    @FXML private TableView<ExpensesBean> tableView;

    @FXML private TextField account;

    @FXML private TextField category;

    @FXML private TextField description;

    @FXML private TextField receipt;

    @FXML private TextField amount;

    @FXML private Button addButton;

    @FXML private TextField total;

    @FXML private Button saveButton;

    @FXML private Button deleteButton;
    
    @FXML private TableColumn<ExpensesBean, String> accountCol;

    @FXML private TableColumn<ExpensesBean, String> categoryCol;

    @FXML private TableColumn<ExpensesBean, String> descriptionCol;

    @FXML private TableColumn<ExpensesBean, String> receiptCol;

    @FXML private TableColumn<ExpensesBean, BigDecimal> amountCol;
    
    private ObservableList<ExpensesBean> expenseRow = 
            FXCollections.observableArrayList();
    
    private ExpensesBean expensesBean; 
    
    private TotalExpensesBean totalExpensesBean = new TotalExpensesBean();
    
    private final NumberFormat currency = NumberFormat.getCurrencyInstance();
    
    //******ENITTY MANAGER
        private final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("Business_ManagerPU");
    
    private final EntityManager entityManager = 
            entityManagerFactory.createEntityManager();
    
    private final TypedQuery<Expenses> getExpenseAmount = 
            entityManager.createNamedQuery("Expenses.findAll", Expenses.class);


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        expensesProfile.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("profile.jpg"))));
        // TODO
        dateField.setValue(LocalDate.now());
        
    }    

    @Override
    public void setParentScreen(ScreenHandler currentPage) {
        screenController = currentPage;
    }
    
    //===Menu NAVIGATION======
    @FXML public void goToHome(){
        screenController.setScreen(BusinessManager.homeID);
    }
    
    @FXML public void goToInvoice(){
        screenController.setScreen(BusinessManager.invoiceID);
    }
    
    @FXML public void addToExpenses(){
        if(account.getText() != null && description.getText() != null 
                && amount.getText()!= null){
            try{
            expensesBean = new ExpensesBean();
            expensesBean.setAccount(account.getText());
            expensesBean.setCategory(category.getText());
            expensesBean.setDescription(description.getText());
            expensesBean.setReceipt(receipt.getText());
            expensesBean.setAmount(new BigDecimal(amount.getText()));
            //display on TableView
            expenseRow.add(expensesBean);
            accountCol.setCellValueFactory(new PropertyValueFactory<>("account"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
            receiptCol.setCellValueFactory(new PropertyValueFactory<>("receipt"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
            amountCol.setCellFactory(tableColumn -> addCurrency());

            tableView.setItems(expenseRow);

            totalExpensesBean.addToExpenses(expensesBean.getAmount());
            //display on total textfield
            total.setText(currency.format(totalExpensesBean.getTotalExpenses()));

            //clear text fields
            account.clear();
            category.clear();
            description.clear();
            receipt.clear();
            }
            catch(NumberFormatException exception){
                amount.setText("invalid");
                amount.requestFocus();
                amount.onMouseClickedProperty().set(event -> amount.setText(""));
                expensesBean = null;
            }
        }
    }
    
    private TableCell<ExpensesBean, BigDecimal> addCurrency(){
        TableCell<ExpensesBean, BigDecimal> tableCell = new TableCell<ExpensesBean, BigDecimal>(){
            @Override
            protected void updateItem(BigDecimal value, boolean empty){
                super.updateItem(value, empty);
                if (!empty) {
                    setText(currency.format(value));
                }
            }
        };
        return tableCell;
    }
    
    @FXML public void removeFromExpenses(){
        if(!tableView.getSelectionModel().isEmpty()){
            ExpensesBean row = tableView.getSelectionModel().getSelectedItem();
            BigDecimal rowAmount = row.getAmount();
            tableView.getItems().remove(row);
            tableView.refresh();
            totalExpensesBean.removeFromExpenses(rowAmount);
            BigDecimal newTotal = totalExpensesBean.getTotalExpenses();
            this.total.setText(currency.format(newTotal));//String.valueOf(itemTotal));
        }
    }
    
    @FXML public void save(){
        EntityTransaction transaction = entityManager.getTransaction();
        ObservableList<ExpensesBean> allExpenses = tableView.getItems();
        
        com.bukola.Expenses calculator = new com.bukola.Expenses(); //create expense calculator
//        if(getExpenseAmount!=null){
//                List<Expenses> subTotal = getExpenseAmount.getResultList();
//                subTotal.forEach(totalExpense -> {
//                calculator.addWeeklyExpense(totalExpense.getTotal());
//            });
//        }
        try{
            allExpenses.forEach(expense -> {
                if(getExpenseAmount!=null){
                List<Expenses> subTotal = getExpenseAmount.getResultList();
                subTotal.forEach(totalExpense -> {
                calculator.addExpense(totalExpense.getTotal());
            });
        }
                Expenses expenses = new Expenses();  //creates new instance of Expenses entity
                expenses.setDate(java.sql.Date.valueOf(dateField.getValue()));
                expenses.setAccount(expense.getAccount()); //inserts observable list items into entity class
                expenses.setDescription(expense.getDescription());
                expenses.setCategory(expense.getCategory());
                expenses.setReceipt(expense.getReciept());
                expenses.setTotal(expense.getAmount());
                expenses.setRunningTotal(calculator.getTotalExpenses().add(expenses.getTotal()));
                
                

                entityManager.persist(expenses);
                transaction.begin();
                transaction.commit();
            });
            displayAlert(Alert.AlertType.INFORMATION, 
                         "Expenses Status", 
                         "Expenses saved!");
        }
        catch(Exception e){
             displayAlert(Alert.AlertType.ERROR, "Save Expenses Failed", 
            "Unable to save expenses: " + e);
        }
    }
    private void displayAlert(
      Alert.AlertType type, String title, String message) {
      Alert alert = new Alert(type);
      alert.setTitle(title);
      alert.setContentText(message);
      alert.showAndWait();
   }
    
}
