/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import business.manager.BusinessManager;
import business.manager.ScreenChangeListener;
import business.manager.ScreenHandler;
import entity_classes.Customers;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.BaseFont;
import rst.pdfbox.layout.text.Indent;
import rst.pdfbox.layout.text.SpaceUnit;

import org.controlsfx.control.textfield.TextFields;
/**
 * FXML Controller class
 *
 * @author BUKOLA
 */
public class InvoiceController implements Initializable, ScreenChangeListener {

    ScreenHandler screenController;
    ScreenHandler invoiceGridController = new ScreenHandler();
    @FXML
    private TextField clientName;

    @FXML
    private TextField clientAddress;

    @FXML
    private TextField clientCity;

    @FXML
    private TextField clientPostCode;

    @FXML
    private TextField invoiceNo;

    @FXML
    private DatePicker invoiceDate;

    @FXML
    private DatePicker invoiceDueDate;
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
    
    private InvoicePDF document = new InvoicePDF();
    
    //****************ENTITY MANAGER*****************
    EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("Business_ManagerPU");
    
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    
    TypedQuery<Customers> findCustomerByName = entityManager.createNamedQuery(
            "Customers.findAll", Customers.class);
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //*************************************\\
        //findCustomerByName.setParameter("customerName", clientName.getText());
        if( findCustomerByName!= null){
        List<Customers> names = findCustomerByName.getResultList();
        TextFields.bindAutoCompletion(clientName, names.stream()
                .map(Customers::getCustomerName).collect(Collectors.toList()));
        }
        //initialize invoice number in textfield
        
        //clientCity.setPromptText("City");
        
        //=======ComboBox for Flat Rate Form==========
        flatRateComboBox.setItems(flatComboItems);
        flatRateComboBox.getSelectionModel().select("flat rate");
        //=======observe selection changes=======
        flatRateComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obsValue, oldValue, newValue) -> 
                        switchForm(obsValue, oldValue, newValue));            

        //======ComboBox for Unit Rate Form======
        unitComboBox.setItems(unitComboItems);
        unitComboBox.getSelectionModel().select("by hour");
        //======observe selection changes========
        unitComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obsValue, oldValue, newValue) -> 
                        switchForm(obsValue, oldValue, newValue));
        
        
        //=====Description TextField editing mode====
        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());

        //=====Unit Rate Form Total (default)=====
        unitTotal.setText(currency.format(BigDecimal.ZERO.setScale(
                2, RoundingMode.HALF_UP)));

        //=====Invoice Running Total==============
        invoiceTotal.setText(currency.format(BigDecimal.ZERO.setScale(
                2, RoundingMode.HALF_UP))); 

        //=====for changes in Price TextField for Unit Rate Form===
        unitPrice.textProperty().addListener((obv, oldValue, newValue) ->
            computePriceChange(obv, oldValue, newValue));
        //=====for changes in Quantity TextField for Unit Rate Form==
        unitQuantity.textProperty().addListener((obv, oldValue, newValue)->
            computeQuantityChange(obv, oldValue, newValue));
        
        //tableView editing mode (delete)
        //amountCol.setCellFactory(ButtonTableCell.forTableColumn());
    }

    
    private void switchForm(ObservableValue<? extends String>
            obsValue, String oldValue, String newValue){
        
        if(newValue.equals("by hour")){
            copyDescriptionToUnitForm();
            formStack.getChildren().removeAll(flatRateForm, unitRateForm);
            formStack.getChildren().add(unitRateForm);
            unitComboBox.getSelectionModel().select("by hour");
        }
        if(newValue.equals("by item")){
            copyDescriptionToUnitForm();
            formStack.getChildren().removeAll(flatRateForm, unitRateForm);
            formStack.getChildren().add(unitRateForm);
            unitComboBox.getSelectionModel().select("by item");
        }

        if(newValue.equals("flat rate")){
            copyDescriptionToFlatForm();
            formStack.getChildren().removeAll(flatRateForm, unitRateForm);
            formStack.getChildren().add(flatRateForm);
            flatRateComboBox.getSelectionModel().select("flat rate");
        }
    }
    
    private void copyDescriptionToUnitForm(){
        //if flat rate description form has text, copy to unit rate description
        if(!flatRateDescription.getText().equals("")){
            unitDescription.setText(flatRateDescription.getText());
            flatRateDescription.clear();
            flatRateAmount.clear();
        }
    }
    
    private void copyDescriptionToFlatForm(){
        if(!unitDescription.getText().equals("")){
            flatRateDescription.setText(unitDescription.getText());
            unitDescription.clear();
            unitQuantity.clear();
            unitPrice.clear();
        }
    }
    
    private void computePriceChange(ObservableValue<? extends String> obsValue, 
            String oldPrice, String newPrice){

        setUnitTotalToZero();
            if(unitQuantity.getText().equals("") & !newPrice.equals("")){
                createDefaultQuantityBean();
                if(rateBean != null)
                unitTotal.setText(currency.format(rateBean.getTotal()));  
            }
            else if(newPrice.equals("") & unitQuantity.getText().equals("")){
                setUnitTotalToZero();
            }
            //if they both contain values
            else if(!newPrice.equals("") & !unitQuantity.getText().equals("")){
                createUnitRateBean();
                if(rateBean != null)
                unitTotal.setText(currency.format(rateBean.getTotal()));
            }
    }

    private void computeQuantityChange(ObservableValue<? extends String> obsValue, 
            String oldQuantity, String newQuantity){

        setUnitTotalToZero();
        
        if(!newQuantity.equals("") & !unitPrice.getText().equals("")){
            createUnitRateBean();
            if(rateBean != null)
           unitTotal.setText(currency.format(rateBean.getTotal()));
        }

        if(newQuantity.equals("") & !unitPrice.getText().equals("")){

           createDefaultQuantityBean();
           if(rateBean != null)
           unitTotal.setText(currency.format(rateBean.getTotal()));
        }
    }
    
    private void setUnitTotalToZero(){
        unitTotal.setText(currency.format(BigDecimal.ZERO.setScale(
            2, RoundingMode.HALF_UP)));

    }

    
    //===Menu NAVIGATION======
    @Override
    public void setParentScreen(ScreenHandler currentPage) {
        screenController = currentPage;
    }
    
    //===Menu NAVIGATION======
    @FXML
    public void goToHome(){
        screenController.setScreen(BusinessManager.homeID);
    }
    
    @FXML
    public void addItem(){
        //identitfy flatRate form
        if(formStack.getChildren().contains(flatRateForm)
                & !flatRateAmount.getText().equals("")){
            createFlatRateBean();
            loadFlatRateData();
        }
        
        else  if(formStack.getChildren().contains(unitRateForm)
                & !unitPrice.getText().equals("")){
                  loadUnitRateData();
        }
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
    
    @FXML
    public void saveAsPDF() throws IOException{
        final StringBuilder content = new StringBuilder();
        
        String sender = "94 Willmore Road, Birmingham B20 3JJ";
        StringBuilder receiver = new StringBuilder();
        StringBuilder invoiceDetails = new StringBuilder();
        receiver.append(clientName.getText());
        receiver.append(System.lineSeparator());
        receiver.append(clientAddress.getText());
        
        invoiceDetails.append(invoiceNo.getText());
        invoiceDetails.append(System.lineSeparator());
        invoiceDetails.append("23/01/2019");
        
        ObservableList<RateBean> text = tableView.getItems();
        //Iterator textIterator = text.iterator();
        //while(textIterator.hasNext()){
        //StringBuilder content = new StringBuilder();
        text.forEach((item) -> {
            System.out.printf("%s \t %s \t %s \t %s%n", item.getDescription(),
                    item.getQuantity(), item.getPrice(), item.getTotal());
            content.append(item.getDescription());
            content.append("      ");
            content.append(item.getQuantity());
            content.append("      ");
            //content.append(currency);
            content.append(currency.format(item.getPrice()));
            content.append(item.getTotal());
            content.append(System.lineSeparator());
        });
        System.out.println(content.toString());

        
        document.createDocument(sender, receiver.toString(), invoiceDetails.toString(), content.toString());
        
//        ObservableList<RateBean> text = tableView.getItems();
//        Iterator textIterator = text.iterator();
//        //while(textIterator.hasNext()){
//        //StringBuilder content = new StringBuilder();
//        text.forEach((item) -> {
//            System.out.printf("%s \t %s \t %s \t %s%n", item.getDescription(),
//                    item.getQuantity(), item.getPrice(), item.getTotal());
//            content.append(item.getDescription());
//            content.append(" ");
//            content.append(item.getQuantity());
//            content.append(" ");
//            //content.append(currency);
//            content.append(currency.format(item.getPrice()));
//            content.append(item.getTotal());
//        });
//        System.out.println(content.toString());
        //}
//        document.createDocument(content.toString());
    }
    
    private void addInvoiceItems() throws IOException{
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Indent("DESCRIPTION", 170, SpaceUnit.pt, 11, PDType1Font.HELVETICA_BOLD));
        paragraph.addText("QUANTITY     ", 11, PDType1Font.HELVETICA_BOLD);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addText("PRICE        ", 11, PDType1Font.HELVETICA_BOLD);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addMarkup("*TOTAL*", 11, BaseFont.Helvetica);

    }
    
    private void createFlatRateBean(){
        try{
            rateBean = new RateBean(flatRateDescription.getText(), 
                        BigDecimal.ONE, new BigDecimal(flatRateAmount.getText()));
        }
        catch(NumberFormatException exception){
            flatRateAmount.setText("invalid");
            flatRateAmount.requestFocus();
            flatRateAmount.onMousePressedProperty().set(event -> flatRateAmount.setText(""));
            rateBean = null;
        }
    }
    
    private void createUnitRateBean(){
        try{
            rateBean = new RateBean(unitDescription.getText(), 
                        new BigDecimal(unitQuantity.getText()),
                        new BigDecimal(unitPrice.getText()));
        }
        catch(NumberFormatException exception){
            if(!unitQuantity.getText().equals("")){
                unitQuantity.setText("invalid");
                unitQuantity.requestFocus();
                unitQuantity.onMouseClickedProperty().set(event -> unitQuantity.setText(""));
            }
            if(!unitPrice.getText().equals("")){
                unitPrice.setText("invalid");
                unitPrice.requestFocus();
                unitPrice.onMouseClickedProperty().set(event -> unitPrice.setText(""));
            }
            rateBean = null;
        }
    }
    
    private void createDefaultQuantityBean(){
        try{
            rateBean = new RateBean(unitDescription.getText(), 
                    BigDecimal.ONE, new BigDecimal(unitPrice.getText()));
        }
        catch(NumberFormatException exception){
            unitPrice.setText("invalid");
            unitPrice.requestFocus();
            unitPrice.onMouseClickedProperty().set(event -> unitPrice.setText(""));
            rateBean = null;
        }
    }
    
    private void loadFlatRateData(){
        if(rateBean != null){ //null from exception handling @see createFlatRateBean()
            rowContent.add(rateBean);
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
            amountCol.setCellFactory(tableColumn -> addCurrency());
            priceCol.setCellFactory(tableColumn -> addCurrency());
            tableView.setItems(rowContent);
            invoiceTotal.setText(currency.format(invoice.addToInvoice(rateBean.getTotal())));
            //clear flat rate form
            flatRateDescription.clear();
            flatRateAmount.clear();

        }   
    }
    
    
    private void loadUnitRateData(){
        if(rateBean != null){ //null from exception handling
            rowContent.add(rateBean);
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            priceCol.setCellFactory(tableColumn -> addCurrency());
            amountCol.setCellFactory(tableColumn -> addCurrency());
            tableView.setItems(rowContent);
            invoiceTotal.setText(currency.format(invoice.addToInvoice(rateBean.getTotal())));
            //clear unit rate form
            unitDescription.clear();
            unitQuantity.clear();
            unitPrice.clear();
            unitTotal.setText(currency.format(BigDecimal.ZERO));
        }
    }
    
    private TableCell<RateBean, BigDecimal> addCurrency(){
    TableCell<RateBean, BigDecimal> tableCell = new TableCell<RateBean, BigDecimal>(){
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
    
    

    public void addNewCustomer(){
        //get recipient details and save in customer table
        //ObservableList<Customers> customerName = FXCollections.observableArrayList();
        Customers customer = new Customers();
    
       // TextFields.bindAutoCompletion(clientName, findCustomerByName.getResultList());
        customer.setCustomerName(clientName.getText());
        customer.setAddressLine1(clientAddress.getText());
        customer.setPostCode(clientPostCode.getText());
        customer.setCity(clientCity.getText());
        //customer.setPhoneNumber(clientPhoneNumber);
        
         EntityTransaction transaction = entityManager.getTransaction();
         try{
             transaction.begin();
             entityManager.persist(customer);
             transaction.commit();
             displayAlert(AlertType.INFORMATION, 
                     "Address Book updated", 
                     "New customer added to Address Book");
         }catch(Exception e){
             displayAlert(AlertType.ERROR, "Address Book Not Updated", 
            "Unable to update: " + e);
         }
        
        //
    }
    
    private void displayAlert(
      Alert.AlertType type, String title, String message) {
      Alert alert = new Alert(type);
      alert.setTitle(title);
      alert.setContentText(message);
      alert.showAndWait();
   }
    
    
    
    //to be revisited; conflicting cell editing due to two different input grids
//    @FXML
//    public void onEdit(CellEditEvent<InvoiceBean, String> description){
//        FlatRateBean invoiceContent = tableView.getSelectionModel().getSelectedItem();
//        invoiceContent.setDescription(description.getNewValue());
//    }
    
}
