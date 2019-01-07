/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.manager;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

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
    @FXML private TableColumn<RateBean, Integer> amountCol;
    @FXML private TableColumn<RateBean, Integer> priceCol;
    @FXML private TableColumn<RateBean, Integer> quantityCol;
    @FXML private TextField flatRateDescription;
    @FXML private StackPane formStack;
    ObservableList<RateBean> content = FXCollections.observableArrayList();
    @FXML ComboBox<String> flatRateComboBox;
    ObservableList<String> flatComboItems = FXCollections.observableArrayList(
            "flat rate", "by hour", "by item");
    
    FlatRateBean flatRate;
    RateBean unitRate;
    RateBeanWrapper invoiceContent;
    
    Double runningTotal = new Double(0);
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
            /**must validate Quantity field is not empty**/
            unitPrice.textProperty().addListener((obv, oldValue, newValue) ->{
                if(!newValue.equals("")){
                   createUnitRateBean();
                   //runningTotal += unitRate.getTotal();
                   unitTotal.setText(String.valueOf(unitRate.getTotal()));
                }
            });

            
            
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
    public void addToInvoice(){
        //if(!flatRateDescription.getText().equals("")){
        //identitfy flatRate form
        if(gridStack.getChildren().contains(flatRateGrid)){
            createFlatRateBean();
            content.add(unitRate);
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
            tableView.setItems(content);
            //display running total
            runningTotal += unitRate.getTotal();
            invoiceTotal.setText(String.valueOf(runningTotal));

            flatRateDescription.clear();
            flatRateAmount.clear();
        }
        
        else  if(gridStack.getChildren().contains(unitGrid)){
            createUnitRateBean();
            content.add(unitRate);
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            tableView.setItems(content);
            //display running total of invoice
            runningTotal += unitRate.getTotal();
            invoiceTotal.setText(String.valueOf(runningTotal));

            
            unitDescription.clear();
            unitQuantity.clear();
            unitPrice.clear();
            unitTotal.clear();
        }
    }
    
    private void createFlatRateBean(){
        unitRate = new RateBean(flatRateDescription.getText(), 
                    1, new Integer(flatRateAmount.getText()));
    }
    
    private void createUnitRateBean(){
        unitRate = new RateBean(unitDescription.getText(), 
                    new Integer(unitQuantity.getText()), new Integer(unitPrice.getText()));
    }
    
    private void addToRunningTotal(){
        
    }
    
    private void subtractFromRunningTotal(){
        
    }
    
    @FXML
    public void deleteSelection(){
        RateBean row = tableView.getSelectionModel().getSelectedItem();
        tableView.getItems().remove(row);
        runningTotal -= row.getTotal();
        invoiceTotal.setText(String.valueOf(runningTotal));
    }
    
    //to be revisited; conflicting cell editing due to two different input grids
//    @FXML
//    public void onEdit(CellEditEvent<InvoiceBean, String> description){
//        FlatRateBean invoiceContent = tableView.getSelectionModel().getSelectedItem();
//        invoiceContent.setDescription(description.getNewValue());
//    }
    
}
