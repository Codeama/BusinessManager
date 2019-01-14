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
import java.util.ResourceBundle;
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
    @FXML private StackPane gridStack;
    @FXML private GridPane flatRateGrid;
    @FXML private GridPane unitGrid;
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
    @FXML private ListView listView;
    @FXML private TableView<RateBean> tableView;
    @FXML private TableColumn<RateBean, String> descriptionCol; 
    @FXML private TableColumn<RateBean, BigDecimal> amountCol;
    @FXML private TableColumn<RateBean, BigDecimal> priceCol;
    @FXML private TableColumn<RateBean, BigDecimal> quantityCol;
    @FXML private TextField flatRateDescription;
    @FXML private StackPane formStack;
    ObservableList<RateBean> content = FXCollections.observableArrayList();
    @FXML ComboBox<String> flatRateComboBox;
    ObservableList<String> flatComboItems = FXCollections.observableArrayList(
            "flat rate", "by hour", "by item");
    
    RateBean rateBean;
    InvoiceProperty invoice = new InvoiceProperty();
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            // flat rate ComboBox
            flatRateComboBox.setItems(flatComboItems);
            flatRateComboBox.getSelectionModel().select("flat rate");
            flatRateComboBox.getSelectionModel().selectedItemProperty()
                    .addListener((obv, oldValue, newValue) ->{
                if(newValue.equals("by hour")){
                    gridStack.getChildren().removeAll(flatRateGrid, unitGrid);
                    gridStack.getChildren().add(unitGrid);
                    unitComboBox.getSelectionModel().select("by hour");
                    //System.out.println(gridStack.getChildren().size());
                }
                if(newValue.equals("by item")){
                    gridStack.getChildren().removeAll(flatRateGrid, unitGrid);
                    gridStack.getChildren().add(unitGrid);
                    unitComboBox.getSelectionModel().select("by item");
                }
            });
            
            //by hour/item comboBox
            unitComboBox.setItems(unitComboItems);
            unitComboBox.getSelectionModel().select("by hour");
            unitComboBox.getSelectionModel().selectedItemProperty()
                    .addListener((obv, oldValue, newValue) ->{
                if(newValue.equals("flat rate")){
                    gridStack.getChildren().removeAll(flatRateGrid, unitGrid);
                    gridStack.getChildren().add(flatRateGrid);
                    flatRateComboBox.getSelectionModel().select("flat rate");
                }
                if(newValue.equals("by item")){
                    gridStack.getChildren().removeAll(flatRateGrid, unitGrid);
                    gridStack.getChildren().add(unitGrid);
                    flatRateComboBox.getSelectionModel().select("by item");
                }

            });
            
            //set texfield for editing
            descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
            
            //show totals concurrently
            /**add listener to quantity too!**/
            unitPrice.textProperty().addListener((obv, oldValue, newValue) ->{
                unitTotal.setText("0.00");
                if(!newValue.equals("")){
                   createUnitRateBean();
                   unitTotal.setText(String.valueOf(rateBean.getTotal()));
                }
            });
            
            //set default value for runningTotal
            invoiceTotal.setText(String.valueOf(BigDecimal.ZERO.setScale(
                    2, RoundingMode.HALF_UP)));
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
        if(gridStack.getChildren().contains(flatRateGrid)){
            createFlatRateBean();
            content.add(rateBean);
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
            tableView.setItems(content);
            invoiceTotal.setText(String.valueOf(invoice.addToInvoice(rateBean.getTotal())));

            flatRateDescription.clear();
            flatRateAmount.clear();
        }
        
        else  if(gridStack.getChildren().contains(unitGrid)){
            createUnitRateBean();
            content.add(rateBean);
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            tableView.setItems(content);
            invoiceTotal.setText(String.valueOf(invoice.addToInvoice(rateBean.getTotal())));

            
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
    
    @FXML
    public void removeItem(){
        if(!tableView.getSelectionModel().isEmpty()){
            RateBean row = tableView.getSelectionModel().getSelectedItem();
            BigDecimal amount = row.getTotal();
            tableView.getItems().remove(row);
            BigDecimal itemTotal = invoice.removeFromInvoice(amount);
            invoiceTotal.setText(String.valueOf(itemTotal));
        }
    }
    
    //to be revisited; conflicting cell editing due to two different input grids
//    @FXML
//    public void onEdit(CellEditEvent<InvoiceBean, String> description){
//        FlatRateBean invoiceContent = tableView.getSelectionModel().getSelectedItem();
//        invoiceContent.setDescription(description.getNewValue());
//    }
    
}