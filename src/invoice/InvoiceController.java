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
import entity_classes.InvoiceItems;
import entity_classes.Invoices;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

import org.controlsfx.control.textfield.TextFields;
import com.bukola.*; //Tax Calculator API
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
    private TextField clientEmail;

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
    private final InvoiceTotalBean invoiceTotalBean = new InvoiceTotalBean();
    //for aggregating invoices
    private final InvoiceTotalBean runningTotal = new InvoiceTotalBean();
    
    private final NumberFormat currency = NumberFormat.getCurrencyInstance();
    
    private final InvoicePDFTemplate document = new InvoicePDFTemplate();
    
            //**********ENTITY MANAGER**********
    private final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("Business_ManagerPU");
    
    private final EntityManager entityManager = 
            entityManagerFactory.createEntityManager();
    
    private final TypedQuery<Customers> findCustomerByName = 
            entityManager.createNamedQuery(
            "Customers.findAll", Customers.class);
    
    private final TypedQuery<Invoices> getInvoiceAmount = 
            entityManager.createNamedQuery("Invoices.findAll", Invoices.class);
    
    Customers autoCustomer;
    
   // TypedQuery<Customers> findOneCustomer = entityManager.createNamedQuery(
   //         "Customers.findByCustomerName", Customers.class);
    
    boolean isAutoCompleted;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            //*****auto-complete recipient fields(Controlsfx)****
        if( findCustomerByName!= null){
        List<Customers> names = findCustomerByName.getResultList();
        TextFields.bindAutoCompletion(clientName, names.stream()
                .map(Customers::getCustomerName).collect(Collectors.toList()))
                .setOnAutoCompleted(event -> autoCompleteRecipient());
        }
        
        
        
            //****auto-generate invoice number*********
        invoiceNo.setText("INV"+(currentDateTime()));
        
            //******set current date*********
        invoiceDate.setValue(LocalDate.now());
        
            //*****ComboBox for Flat Rate Form****
        flatRateComboBox.setItems(flatComboItems);
        flatRateComboBox.getSelectionModel().select("flat rate");
            //******observe selection changes*****
        flatRateComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obsValue, oldValue, newValue) -> 
                        switchForm(obsValue, oldValue, newValue));            

            //*****ComboBox for Unit Rate Form****
        unitComboBox.setItems(unitComboItems);
        unitComboBox.getSelectionModel().select("by hour");
        
            //****observe selection changes*******
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
        
        System.out.println(invoiceTotalBean.getTotalInvoice());
    }
    
    @FXML
    public void removeItem(){
        if(!tableView.getSelectionModel().isEmpty()){
            RateBean row = tableView.getSelectionModel().getSelectedItem();
            BigDecimal amount = row.getTotal();
            tableView.getItems().remove(row);
            tableView.refresh();
            invoiceTotalBean.removeFromInvoice(amount);
            BigDecimal total = invoiceTotalBean.getTotalInvoice();
            invoiceTotal.setText(currency.format(total));//String.valueOf(itemTotal));
        }
    }
    
    @FXML
    public void saveAsPDF() throws IOException{

        ObservableList<RateBean> text = tableView.getItems();
        //load PDF content with invoice data
        InvoicePDFContent invoiceContent = new InvoicePDFContent();
        String sender = invoiceContent.getSender();
        String receiver = invoiceContent.addRecipient(clientName.getText(),
                clientAddress.getText(), clientCity.getText(), 
                clientPostCode.getText(), clientEmail.getText());
        String invoiceDetails = invoiceContent.addInvoiceDetails(
                invoiceNo.getText(), invoiceDate.getValue().toString());
        String invoiceItems = invoiceContent.addInvoiceItems(text);
        //add contents to PDF document template
        document.createDocument(sender, receiver, invoiceDetails, 
                invoiceItems, invoiceNo.getText());
        
        System.out.println(document.getFilePath());
    }
    
    /**
     * creates new RateBean object for flatRate entry from user,
     *      initializing Quantity with value of 1.
     * So whilst the TableView doesn't reflect quantity, the item quantity is
     *      default of 1. 
     * This behaviour (on the TableView which the user sees) changes as soon
     *      as user selects a different item unit rating
     * @see #createUnitRateBean() i.e. "by hour"/"by item".
     * This time, even flatRate quantity automatically shows its
     *      default value of 1. It is only cosmetic as
     * @see #createDefaultQuantityBean() does the same job
     * 
     */
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
    
    /**
     * creates and initializes a new RateBean object with the aim of 
     *      allowing user to enter desired number of units for each record.
     * Quantity cannot be zero if this option is selected.
     */
    private void createUnitRateBean(){
        try{
            rateBean = new RateBean(unitDescription.getText(), 
                        new BigDecimal(unitQuantity.getText()),
                        new BigDecimal(unitPrice.getText()));
        }
        catch(Exception exception){ //NumberFormat | IllegalArgumentException
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
    
    //This is a very cosmetic/convenience method ---not really necessary
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
            invoiceTotalBean.addToInvoice(rateBean.getTotal());
            BigDecimal total = invoiceTotalBean.getTotalInvoice();
            invoiceTotal.setText(currency.format(total));
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
            invoiceTotalBean.addToInvoice(rateBean.getTotal());
            BigDecimal total = invoiceTotalBean.getTotalInvoice();
            invoiceTotal.setText(currency.format(total));
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
    
    public void autoCompleteRecipient(){
        TypedQuery<Customers> findByName = 
         entityManager.createNamedQuery(
            "Customers.findByCustomerName", Customers.class);
        findByName.setParameter("customerName", clientName.getText());
        findByName.getResultList().forEach(customer -> {
            clientAddress.setText(customer.getAddressLine1());
            clientCity.setText(customer.getCity());
            clientPostCode.setText(customer.getPostCode());
            isAutoCompleted = true;
            autoCustomer = customer;
        });
    }
    
    public String currentDateTime(){
       LocalDate date = LocalDate.now();
       DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
       String dateString = date.format(dateFormatter);
       LocalTime time = LocalTime.now();
       DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("Hmmss");
       String timeString = time.format(timeFormatter);
      
        return dateString + timeString;
    }
    
    public void saveInvoice(){
        //check client details don't already exist in database(Customers) i.e.
        //compare all entries are not the same; if name and/or email are the same
        //if false, create new customer entry
        //1) if getNewCustomerId == null {save new details to customers table}
            //else if getNewCustomerId != null {check all data fields are the same; if so
                    //do nothing in customer table
                    //otherwise create new entry for Customer
                    
        //2) record invoice date, getNewCustomerId (might need this in initialize so I have an ID ready before saving?),
            //invoice no, file_path (create FileManager), status (draft),invoice total, on table Invoices
        
        //3) record invoice items on table Invoice Items
    }
    //this only marks as issued and saves a PDF copy
    //TODO send via email as web application
    public void sendInvoice(){
        Wages wage = new Wages();
        if(getInvoiceAmount!=null){
            List<Invoices> subTotal = getInvoiceAmount.getResultList();
            subTotal.forEach(invoice -> {wage.addPay(invoice.getTotal());});
        }
        //
        //runningTotal +=total
        //Customers customer = new Customers();
        Invoices invoice = new Invoices(); //entity object
        
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            saveAsPDF();
        invoice.setDate(java.sql.Date.valueOf(invoiceDate.getValue()));
        invoice.setInvoiceNo(invoiceNo.getText());
        invoice.setCustomerId(autoCustomer);
        invoice.setFilePath(document.getFilePath());
        invoice.setStatus("ISSUED"); //to be reviewed with Enums
        invoice.setTotal(invoiceTotalBean.getTotalInvoice());
        //fetch last running total?
        wage.addPay(invoice.getTotal());//add current invoice total to wage
        runningTotal.addToInvoice(BigDecimal.ZERO);
        invoice.setRunningTotal(wage.getTotalPayToDate()); //add up invoices to date
        entityManager.persist(invoice);
        
        
        ObservableList<RateBean> allItems = tableView.getItems();//
        allItems.forEach(item ->{
            InvoiceItems invoiceItem = new InvoiceItems();
            invoiceItem.setInvoiceNo(invoiceNo.getText());
            invoiceItem.setCustomerId(autoCustomer);
            invoiceItem.setDescription(item.getDescription());
            invoiceItem.setQuantity(item.getQuantity());
            invoiceItem.setPrice(item.getPrice());
            invoiceItem.setAmount(item.getTotal());
            
             entityManager.persist(invoiceItem);
             
             transaction.begin();
             transaction.commit();
        });
        
         
        // try{
             //transaction.begin();
             //entityManager.persist(invoice);
            
             //transaction.commit();
             displayAlert(AlertType.INFORMATION, 
                     "Invoice Status", 
                     "Invoice sent!");
         }catch(Exception e){
             displayAlert(AlertType.ERROR, "Send Failed", 
            "Unable to send invoice: " + e);
         }
        
//        if(isAutoCompleted){
//            //customer = autoCustomer;
//            try {
//                saveAsPDF();
//            } catch (IOException ex) {
//               ex.printStackTrace();
//            }
//            recordInvoiceDetails(customer, "ISSUED");
//            recordInvoiceItems(customer, invoice);
//        }
//        else{
//            addNewCustomer();
//            try {
//                saveAsPDF();
//            } catch (IOException ex) {
//                Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            recordInvoiceDetails(customer, "ISSUED");
//            recordInvoiceItems(customer, invoice);
//
//        }
        
    }
    
    private void recordInvoiceDetails(Customers customer, String invoiceStatus){
        Invoices invoice = new Invoices();
        invoice.setDate(java.sql.Date.valueOf(invoiceDate.getValue()));
        invoice.setInvoiceNo(invoiceNo.getText());
        invoice.setCustomerId(customer);
        invoice.setFilePath(document.getFilePath());
        invoice.setStatus(invoiceStatus);
        invoice.setTotal(BigDecimal.TEN);
        //TODO set runningTotal here
        
        EntityTransaction transaction = entityManager.getTransaction();
         try{
             transaction.begin();
             entityManager.persist(invoice);
             transaction.commit();
             displayAlert(AlertType.INFORMATION, 
                     "Invoice Status", 
                     "Invoice sent!");
         }catch(Exception e){
             displayAlert(AlertType.ERROR, "Send Failed", 
            "Unable to send invoice: " + e);
         }
    }
    
    private void recordInvoiceItems(Customers customer, Invoices invoice){
        ObservableList<RateBean> allItems = tableView.getItems();
        allItems.forEach(item ->{
            InvoiceItems invoiceItem = new InvoiceItems();
            invoiceItem.setInvoiceNo(invoiceNo.getText());
            invoiceItem.setCustomerId(customer);
            invoiceItem.setDescription(item.getDescription());
            invoiceItem.setQuantity(item.getQuantity());
            invoiceItem.setPrice(item.getPrice());
            invoiceItem.setAmount(item.getTotal());
            
            EntityTransaction transaction = entityManager.getTransaction();
         try{
             transaction.begin();
             entityManager.persist(invoiceItem);
             transaction.commit();
             System.out.println("Invoice item recorded");
         }catch(Exception e){
             displayAlert(AlertType.ERROR, "Problem recording item", 
            "Unable to record item: " + e);
         }
            
        });
        
        
    }
    
    
    //to be revisited; conflicting cell editing due to two different input grids
//    @FXML
//    public void onEdit(CellEditEvent<InvoiceBean, String> description){
//        FlatRateBean invoiceContent = tableView.getSelectionModel().getSelectedItem();
//        invoiceContent.setDescription(description.getNewValue());
//    }
    
}
