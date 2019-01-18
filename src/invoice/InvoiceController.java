/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import business.manager.BusinessManager;
import business.manager.ScreenChangeListener;
import business.manager.ScreenHandler;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author BUKOLA
 */
public class InvoiceController implements Initializable, ScreenChangeListener {

    ScreenHandler screenController;
    ScreenHandler invoiceGridController = new ScreenHandler();
    @FXML private StackPane formStack;
    @FXML private GridPane flatRateForm;
    @FXML private GridPane unitRateForm;
    @FXML private TextField unitDescription;
    @FXML private TextField unitQuantity;
    @FXML private TextField unitPrice;
    @FXML private TextField unitTotal;
    @FXML private ComboBox<String> unitComboBox;
    ObservableList<String> unitComboItems = 
            FXCollections.observableArrayList(
            "flat rate", "by hour", "by item");
    @FXML private TextField flatRateAmount;
    @FXML private TextField invoiceTotal;
    @FXML private TableView<RateBean> tableView;
    @FXML private TableColumn<RateBean, String> descriptionCol; 
    @FXML private TableColumn<RateBean, BigDecimal> amountCol;
    @FXML private TableColumn<RateBean, BigDecimal> priceCol;
    @FXML private TableColumn<RateBean, BigDecimal> quantityCol;
    @FXML private TextField flatRateDescription;
    ObservableList<RateBean> rowContent = FXCollections.observableArrayList();
    @FXML ComboBox<String> flatRateComboBox;
    ObservableList<String> flatComboItems = FXCollections.observableArrayList(
            "flat rate", "by hour", "by item");
    
    private RateBean rateBean = null;
    private InvoiceBean invoice = new InvoiceBean();
    private NumberFormat currency = NumberFormat.getCurrencyInstance();
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            //initialize switch by flat rate from ComboBox
            flatRateComboBox.setItems(flatComboItems);
            flatRateComboBox.getSelectionModel().select("flat rate");
            flatRateComboBox.getSelectionModel().selectedItemProperty()
                    .addListener((obsValue, oldValue, newValue) -> 
                            switchForm(obsValue, oldValue, newValue));            

            //initialize switch by hour/item from comboBox
            unitComboBox.setItems(unitComboItems);
            unitComboBox.getSelectionModel().select("by hour");
            unitComboBox.getSelectionModel().selectedItemProperty()
                    .addListener((obsValue, oldValue, newValue) -> 
                            switchForm(obsValue, oldValue, newValue));
            
            //set description texfield for editing
            descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());

            //set default value for unit total
            unitTotal.setText(currency.format(BigDecimal.ZERO.setScale(
                    2, RoundingMode.HALF_UP)));

            
            //set default value for runningTotal
            invoiceTotal.setText(currency.format(BigDecimal.ZERO.setScale(
                    2, RoundingMode.HALF_UP))); 
            
            //compute total when values change
            /**add listener to quantity too!**/
            unitPrice.textProperty().addListener((obv, oldValue, newValue) ->
                computePriceChange(obv, oldValue, newValue));
            unitQuantity.textProperty().addListener((obv, oldValue, newValue)->
                computeQuantityChange(obv, oldValue, newValue));

            //validate user input
            
    }
    
    
    @Override
    public void setParentScreen(ScreenHandler currentPage) {
        screenController = currentPage;
    }
    
    @FXML
    public void goToHome(){
        screenController.setScreen(BusinessManager.homeID);
    }
    
    @FXML
    public void addItem(){
        //if(!flatRateDescription.getText().equals("")){
        //identitfy flatRate form
        if(formStack.getChildren().contains(flatRateForm)){
           // if(rateBean == null)
                createFlatRateBean();
            rowContent.add(rateBean);
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
            tableView.setItems(rowContent);
            invoiceTotal.setText(currency.format(invoice.addToInvoice(rateBean.getTotal())));//String.valueOf(invoice.addToInvoice(rateBean.getTotal())));
            
            //clear flat rate form
            flatRateDescription.clear();
            flatRateAmount.clear();
        }
        
        else  if(formStack.getChildren().contains(unitRateForm)){
            //createUnitRateBean(); //if rateBean values are not null?
            rowContent.add(rateBean);
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            tableView.setItems(rowContent);
            invoiceTotal.setText(currency.format(invoice.addToInvoice(rateBean.getTotal())));//String.valueOf(invoice.addToInvoice(rateBean.getTotal())));

            //clear unit rate form
            unitDescription.clear();
            unitQuantity.clear();
            unitPrice.clear();
            unitTotal.clear();
        }
    }
    
    private void createFlatRateBean(){
        rateBean = new RateBean(flatRateDescription.getText(), 
                    BigDecimal.ONE, new BigDecimal(flatRateAmount.getText()));
    }
    
    private void createUnitRateBean(){
        rateBean = new RateBean(unitDescription.getText(), 
                    new BigDecimal(unitQuantity.getText()), new BigDecimal(unitPrice.getText()));
    }
    
    private void createDefaultQuantityBean(){
        rateBean = new RateBean(unitDescription.getText(), 
                BigDecimal.ONE, new BigDecimal(unitPrice.getText()));
    }
    
    @FXML
    public void removeItem(){
        if(!tableView.getSelectionModel().isEmpty()){
            RateBean row = tableView.getSelectionModel().getSelectedItem();
            BigDecimal amount = row.getTotal();
            tableView.getItems().remove(row);
            BigDecimal itemTotal = invoice.removeFromInvoice(amount);
            invoiceTotal.setText(currency.format(itemTotal));//String.valueOf(itemTotal));
        }
    }
    
    private void switchForm(ObservableValue<? extends String>
            obsValue, String oldValue, String newValue){
        
        if(newValue.equals("by hour")){
            formStack.getChildren().removeAll(flatRateForm, unitRateForm);
            formStack.getChildren().add(unitRateForm);
            unitComboBox.getSelectionModel().select("by hour");
        }
        if(newValue.equals("by item")){
            formStack.getChildren().removeAll(flatRateForm, unitRateForm);
            formStack.getChildren().add(unitRateForm);
            unitComboBox.getSelectionModel().select("by item");
        }

        if(newValue.equals("flat rate")){
            formStack.getChildren().removeAll(flatRateForm, unitRateForm);
            formStack.getChildren().add(flatRateForm);
            flatRateComboBox.getSelectionModel().select("flat rate");
        }
    }
    
    private void computePriceChange(ObservableValue<? extends String> obsValue, 
            String oldPrice, String newPrice){

        setUnitTotalToZero();

        if(unitQuantity.getText().equals("") & !newPrice.equals("")){
            createDefaultQuantityBean();
            unitTotal.setText(currency.format(rateBean.getTotal()));  
        }
        else if(newPrice.equals("") & unitQuantity.getText().equals("")){
            setUnitTotalToZero();
        }
        //if they both contain values
        if(!newPrice.equals("") & !unitQuantity.getText().equals("")){
            createUnitRateBean();
            unitTotal.setText(currency.format(rateBean.getTotal()));

        }
    }

    private void computeQuantityChange(ObservableValue<? extends String> obsValue, 
            String oldQuantity, String newQuantity){

        setUnitTotalToZero();

        if(!newQuantity.equals("") & !unitPrice.getText().equals("")){
            createUnitRateBean();
           unitTotal.setText(currency.format(rateBean.getTotal()));
        }
        
        if(newQuantity.equals("") & !unitPrice.getText().equals("")){
           
           createDefaultQuantityBean();
           unitTotal.setText(currency.format(rateBean.getTotal()));
        }
        
    }
    
    private void setUnitTotalToZero(){
        unitTotal.setText(currency.format(BigDecimal.ZERO.setScale(
            2, RoundingMode.HALF_UP)));

    }
    
    //to be revisited; conflicting cell editing due to two different input grids
//    @FXML
//    public void onEdit(CellEditEvent<InvoiceBean, String> description){
//        FlatRateBean invoiceContent = tableView.getSelectionModel().getSelectedItem();
//        invoiceContent.setDescription(description.getNewValue());
//    }
    
}
