/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import business.manager.BusinessManager;
import business.manager.ScreenChangeListener;
import business.manager.ScreenHandler;
import entity_classes.*;
import java.io.IOException;
import java.math.*;
import java.net.URL;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javax.persistence.*;

import org.controlsfx.control.textfield.TextFields;
import com.bukola.*; //Tax Calculator API
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.StageStyle;
/**
 * FXML Controller class
 *
 * @author BUKOLA
 */
public class InvoiceController implements Initializable, ScreenChangeListener {

    ScreenHandler screenController;
    ScreenHandler invoiceGridController = new ScreenHandler();
    @FXML Circle invoicesProfile;
    @FXML private TextField clientName;
    @FXML private TextField clientAddress;
    @FXML private TextField clientCity;
    @FXML private TextField clientPostCode;
    @FXML private TextField clientEmail;
    @FXML private TextField invoiceNo;
    @FXML private DatePicker invoiceDate;
    @FXML private DatePicker invoiceDueDate;
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
    private TotalInvoiceBean totalInvoiceBean = new TotalInvoiceBean();
    //for aggregating invoices
    //private final TotalInvoiceBean runningTotal = new TotalInvoiceBean();
    
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
    
    private final TypedQuery<Invoices> getAllInvoices = 
            entityManager.createNamedQuery("Invoices.findAll", Invoices.class);
    
    Customers autoCustomer;
    
   // TypedQuery<Customers> findOneCustomer = entityManager.createNamedQuery(
   //         "Customers.findByCustomerName", Customers.class);
    
    boolean isAutoCompleted; //to check autocompletion of Invoice recipient
    
    //MANAGE INVOICES
    @FXML private Pagination pagination;
    @FXML TableView<InvoicesDAO> paginationTableView;
    ObservableList<InvoicesDAO> invoiceList = FXCollections.observableArrayList();
    @FXML private TableColumn<InvoicesDAO, Date> dateCreated;
    @FXML private TableColumn<InvoicesDAO, String> invoiceNumber;
    @FXML private TableColumn<InvoicesDAO, String> recipient;
    @FXML private TableColumn<InvoicesDAO, String> status;
    @FXML private TableColumn<InvoicesDAO, ComboBox<String>> action;
    @FXML private TableColumn<InvoicesDAO, BigDecimal> amount;
    @FXML private TableColumn<InvoicesDAO, BigDecimal> total;
    ComboBox<String> sentInvoiceCombo;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //PROFILE PICTURE
        invoicesProfile.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("profile.jpg"))));

        
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
                .addListener(this::switchForm);            

            //*****ComboBox for Unit Rate Form****
        unitComboBox.setItems(unitComboItems);
        unitComboBox.getSelectionModel().select("by hour");
        
            //****observe selection changes*******
        unitComboBox.getSelectionModel().selectedItemProperty()
                .addListener(this::switchForm);
        
        
        //=====Description TextField editing mode====
        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());

        //=====Unit Rate Form Total (default)=====
        unitTotal.setText(currency.format(BigDecimal.ZERO.setScale(
                2, RoundingMode.HALF_UP)));

        //=====Invoice Running Total==============
        invoiceTotal.setText(currency.format(BigDecimal.ZERO.setScale(
                2, RoundingMode.HALF_UP))); 

        //=====for changes in Price TextField for Unit Rate Form===
        unitPrice.textProperty().addListener(this::computePriceChange);
        //=====for changes in Quantity TextField for Unit Rate Form==
        unitQuantity.textProperty().addListener(this::computeQuantityChange);
        
        //tableView editing mode (delete)
        //amountCol.setCellFactory(ButtonTableCell.forTableColumn());
        
        //Print all invoices
        getAllInvoices.getResultList().stream().forEach((invoice) -> {
            System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s%n", invoice.getDate(), invoice.getInvoiceNo(),
                    invoice.getCustomerId().getCustomerName(), invoice.getStatus(),
                    invoice.getFilePath(), invoice.getTotal(), invoice.getRunningTotal() );
        });
        
        
         ObservableList<String> sentInvoiceItems = 
            FXCollections.observableArrayList(
            "Record Payment", "PDF", "Print");
//        ComboBox<String> sentInvoiceCombo = new ComboBox<>();
//        sentInvoiceCombo.setItems(sentInvoiceItems);
//        sentInvoiceCombo.getSelectionModel().selectedItemProperty().addListener(this::changeInvoiceStatus);

        //copy data from JPA to FX Beans
        getAllInvoices.getResultList().stream().forEach(invoice -> {
            sentInvoiceCombo = new ComboBox<>();
            sentInvoiceCombo.setItems(sentInvoiceItems);
            sentInvoiceCombo.getSelectionModel().selectedItemProperty().addListener(this::changeInvoiceStatus);
            invoiceList.add(new InvoicesDAO(invoice.getId(),invoice.getDate(), 
                    invoice.getInvoiceNo(), invoice.getCustomerId().getEmailAddress(),
                    invoice.getStatus(), invoice.getTotal(), invoice.getRunningTotal(),sentInvoiceCombo
            ));//handle comboBox events
            //TableColumn<InvoicesDAOBean, ComboBox<String>> column = action.get
            //Invoices invoice = action.getTableView();
        });
        
        paginationTableView.setItems(invoiceList);
        dateCreated.setCellValueFactory(cell -> cell.getValue().getDateProperty());
        invoiceNumber.setCellValueFactory(cell -> cell.getValue().getInvoiceNoProperty());
        recipient.setCellValueFactory(cell -> cell.getValue().getEmailAddressProperty());
        status.setCellValueFactory(cell -> cell.getValue().getStatusProperty());
        amount.setCellValueFactory(cell -> cell.getValue().getTotalProperty());
        total.setCellValueFactory(cell -> cell.getValue().getRunningTotalProperty());
        action.setCellValueFactory(cell -> cell.getValue().getActionsProperty());
        amount.setCellFactory(tableColumn -> displayCurrency());
        total.setCellFactory(tableColumn -> displayCurrency());
        dateCreated.setCellFactory(tableColumn -> dateFormat());
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
    
    private void getPaidInvoices(){
        TypedQuery<Invoices> getPaid = 
            entityManager.createNamedQuery("Invoices.findByStatus", Invoices.class);
        getPaid.setParameter("status", "PAID");
        getPaid.getResultList().forEach(invoice -> System.out.println("Paid invoice : "+ invoice.getTotal()));
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
    public void goToExpenses(){
        screenController.setScreen(BusinessManager.expensesID);
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
        
        System.out.println(totalInvoiceBean.getTotalInvoice());
    }
    
    
    @FXML
    public void removeItem(){
        if(!tableView.getSelectionModel().isEmpty()){
            RateBean row = tableView.getSelectionModel().getSelectedItem();
            BigDecimal amount = row.getTotal();
            tableView.getItems().remove(row);
            tableView.refresh();
            totalInvoiceBean.removeFromInvoice(amount);
            BigDecimal total = totalInvoiceBean.getTotalInvoice();
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
    
    /**
     * This is a very cosmetic/convenience method ---not really necessary (to be removed)
     * @see #createFlatRateBean
     */
    
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
            rowContent.add(rateBean); //add data to observableList
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));//display data ontableView
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
            amountCol.setCellFactory(tableColumn -> addCurrency());
            priceCol.setCellFactory(tableColumn -> addCurrency());
            tableView.setItems(rowContent);
            //add to running total
            totalInvoiceBean.addToInvoice(rateBean.getTotal());
            BigDecimal total = totalInvoiceBean.getTotalInvoice();
            //display on total text field
            invoiceTotal.setText(currency.format(total));
            //clear flat rate form
            flatRateDescription.clear();
            flatRateAmount.clear();

        }   
    }
    
    
    private void loadUnitRateData(){
        if(rateBean != null){ //might be null from exception handling
            rowContent.add(rateBean); //load content on to ObservableList
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            priceCol.setCellFactory(tableColumn -> addCurrency());
            amountCol.setCellFactory(tableColumn -> addCurrency());
            tableView.setItems(rowContent);
            totalInvoiceBean.addToInvoice(rateBean.getTotal());
            BigDecimal total = totalInvoiceBean.getTotalInvoice();
            invoiceTotal.setText(currency.format(total));
            //clear unit rate form
            unitDescription.clear();
            unitQuantity.clear();
            unitPrice.clear();
            unitTotal.setText(currency.format(BigDecimal.ZERO));
        }
    }
    
    //utility method for displaying currency symbol in tableView cells
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
    /**This method is useful for the Address Book interface (to be implemented)
     * To avoid duplicate email address and in sync with UNIQUE constraint
     * on the Email_Address column of the Customer table, 
     * this checks new email entry whether the email address already exists
     * @return returns true or false
     */
    private boolean customerExists(){
        TypedQuery<Customers> findByEmail = 
         entityManager.createNamedQuery(
            "Customers.findByEmailAddress", Customers.class);
        findByEmail.setParameter("emailAddress", clientEmail.getText());
        //findByEmail.getResultList().stream().forEach(email-> {System.out.println(email.getEmailAddress());});
        return !findByEmail.getResultList().isEmpty();//checks if returned list isn't empty
    }
    /**
     * checks invoice form isn't empty when user clicks "create invoice"
     * tests rateBean for value as that is the data model which only gets
     * initialized with value when an entry is made
     * @see #createUnitRateBean()
     * @return returns true or false
    */
    public boolean isInvoiceEmpty(){
        return rateBean == null;
    } 
    
    /**
     * checks if invoice no. already exists if customer wants to create 
     * invoices consecutively
     * @return returns true or false
     */
    public boolean invoiceNoExists(){
        TypedQuery<Invoices> findInvoiceNo = 
         entityManager.createNamedQuery(
            "Invoices.findByInvoiceNo", Invoices.class);
        findInvoiceNo.setParameter("invoiceNo", invoiceNo.getText());
        //System.out.println("Invoice no exist? : "+ findInvoiceNo.getResultList().isEmpty());
        return !findInvoiceNo.getResultList().isEmpty(); //checks if list isn't empty
    }
    
    

    public Customers addNewCustomer(){
        //get recipient details and save in customer table
        Customers customer = new Customers();
        customer.setCustomerName(clientName.getText());
        customer.setAddressLine1(clientAddress.getText());
        customer.setPostCode(clientPostCode.getText());
        customer.setCity(clientCity.getText());
        customer.setEmailAddress(clientEmail.getText());
        return customer;
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
            clientEmail.setText(customer.getEmailAddress());
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
    
    /**
     * Method to create invoice and persist data in the database. It also checks
     *          and  validates invoice number is unique
     */
    public void createInvoice(){
        //handle empty invoice when user clicks "create button"
        if(isInvoiceEmpty()){
            displayAlert(AlertType.WARNING, 
                         "Invoice Form", 
                         "You can't send an empty invoice");
        }
        //handle duplicate invoice number
        else if(invoiceNoExists()){
            displayAlert(AlertType.WARNING, 
                         "Invoice Form", 
                         "Invoice Number has already been used.");
            invoiceNo.requestFocus();
        }
        else{
            EntityTransaction transaction = entityManager.getTransaction();
            Customers customer;
            //checks if recipient detail is filled from Address book
            //otherewise, records new customer
            if(isAutoCompleted){customer = autoCustomer;}
            else{customer = addNewCustomer();
            }
            entityManager.persist(customer);

            //calculate running total for each invoice entry using the Tax_Calculator API
            Wages wageCalculator = new Wages();
            TypedQuery<Invoices> getPaidInvoices = 
                entityManager.createNamedQuery("Invoices.findByStatus", Invoices.class);
            getPaidInvoices.setParameter("status", "PAID");

            if(getAllInvoices!=null){
                List<Invoices> subTotal = getAllInvoices.getResultList();
                subTotal.forEach(invoice -> {wageCalculator.addPay(invoice.getTotal());});
            }
            try{
                saveAsPDF();
                Invoices invoice = recordInvoiceDetails(customer); //invoice no., date, etc
                //fetch last record's runningTotal
                wageCalculator.addPay(invoice.getTotal());//add current invoice total to wageCalculator
                invoice.setRunningTotal(wageCalculator.getTotalPayToDate()); //add up all invoices to date
                entityManager.persist(invoice);


                ObservableList<RateBean> allItems = tableView.getItems();//
                allItems.forEach(item ->{
                    InvoiceItems invoiceItem = new InvoiceItems();
                    invoiceItem.setInvoiceNo(invoiceNo.getText()); 
                    invoiceItem.setCustomerId(customer);
                    invoiceItem.setDescription(item.getDescription());
                    invoiceItem.setQuantity(item.getQuantity());
                    invoiceItem.setPrice(item.getPrice());
                    invoiceItem.setAmount(item.getTotal());

                     entityManager.persist(invoiceItem);

                     transaction.begin();
                     transaction.commit();
                });
                //entityManager.close();

                displayAlert(AlertType.INFORMATION, 
                             "Invoice Status", 
                             "Invoice created!");
             }catch(Exception e){
                 displayAlert(AlertType.ERROR, "Create Invoice Failed", 
                "Unable to create invoice: " + e);
             }
        
       refreshPage();}
    }
    
    private Invoices recordInvoiceDetails(Customers customer){
        Invoices invoice = new Invoices();
        invoice.setDate(java.sql.Date.valueOf(invoiceDate.getValue()));
        invoice.setInvoiceNo(invoiceNo.getText());
        invoice.setCustomerId(customer);
        invoice.setFilePath(document.getFilePath());
        invoice.setStatus("ISSUED"); //to be reviewed with Enums
        invoice.setTotal(totalInvoiceBean.getTotalInvoice());
        
        return invoice;
    }
    
    public void refreshPage(){
        clientName.clear();
        clientAddress.clear();
        clientPostCode.clear();
        clientCity.clear();
        invoiceTotal.clear();
        invoiceNo.setText("INV"+(currentDateTime()));
        tableView.getItems().clear();
        totalInvoiceBean = new TotalInvoiceBean();//reinitialize
    }
    
        //utility method for displaying currency symbol in tableView cells
    private TableCell<InvoicesDAO, Date> dateFormat(){
        TableCell<InvoicesDAO, Date> tableCell = new TableCell<InvoicesDAO, Date>(){
            private SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
            @Override
            protected void updateItem(Date dateOnRecord, boolean empty){
                super.updateItem(dateOnRecord, empty);
                if (!empty) {
                    setText(format.format(dateOnRecord));
                }
            }
        };
        return tableCell;
    }
    
    private TableCell<InvoicesDAO, BigDecimal> displayCurrency(){
        TableCell<InvoicesDAO, BigDecimal> tableCell = new TableCell<InvoicesDAO, BigDecimal>(){
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
    
    /**
     * method to listen to changes in the ComboBox column of the Manage Invoices column
     *         and effect them accordingly. This only currently changes the status of an invoice to paid
     *         User experience needs working on as the ComboBox changes do not currently work without 
     *         a table row being highlighted. So any way to grab the value of a row from 
     *         a ComboBox (column) should change this behaviour. Method use is in initialize.
     * @param obsValue The value that is being observed for changes which is the initial state of the ComboBox item. 
     *          This is currently null.
     * @param oldValue The old value of the ComboBox item selected
     * @param newValue  The new value that the user selects
     */
    private void changeInvoiceStatus(ObservableValue<? extends String>
            obsValue, String oldValue, String newValue){
        if(newValue.equals("Record Payment")){
            InvoicesDAO selectedInvoice = paginationTableView.getSelectionModel().getSelectedItem();
            //show alert
            if(selectedInvoice != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
                        "Mark invoice " +selectedInvoice.getInvoiceNo()+ " as paid?", ButtonType.OK, ButtonType.CANCEL);
                alert.setHeaderText(null);
                alert.setTitle("Confirm Paid Invoice");
                alert.initStyle(StageStyle.UTILITY);

                alert.showAndWait().ifPresent(response -> {
                    if(response == ButtonType.OK){
                        //change Status to PAID, update record in database
                        //first find selected invoice

                        EntityTransaction transaction = entityManager.getTransaction();
                        //sentInvoiceCombo.a
    //                    InvoicesDAO selectedInvoice = paginationTableView.getSelectionModel().getSelectedItem();
                        System.out.println("Selected invoice no: " + selectedInvoice.getId());
                        Invoices invoice = entityManager.find(Invoices.class, selectedInvoice.getId());
                        invoice.setStatus("PAID");
                        System.out.println("Invoice number: "+ invoice);
                        try{
                        transaction.begin();
                        entityManager.merge(invoice);
                        transaction.commit(); // commit changes to the database
                        displayAlert(AlertType.INFORMATION, "Entry Updated", 
                        "Entry was successfully updated.");
                        paginationTableView.refresh();
                        }catch(Exception e){
                            transaction.rollback(); // undo database operations 
                            displayAlert(AlertType.ERROR, "Entry Not Updated", 
                            "Unable to update entry: " + e);
                        }

                    }
                } );
            }
        }
    }


    
    //to be revisited; conflicting cell editing due to two different input grids
//    @FXML
//    public void onEdit(CellEditEvent<InvoiceBean, String> description){
//        FlatRateBean invoiceContent = tableView.getSelectionModel().getSelectedItem();
//        invoiceContent.setDescription(description.getNewValue());
//    }

    
}
