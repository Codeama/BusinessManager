/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.manager;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author BUKOLA
 */
public class BusinessManager extends Application {
    public static String homeID = "home";
    public static String homeFxml = "Home.fxml";
    public static String invoiceID = "invoice";
    public static String invoiceFxml = "Invoice.fxml";
    public static String expensesID = "expenses";
    public static String expensesFxml = "Expenses.fxml";

    
    @Override
    public void start(Stage primaryStage) {
        ScreenHandler screenController = new ScreenHandler();
        screenController.loadScreen(homeID, homeFxml);
        screenController.loadScreen(invoiceID, invoiceFxml);
        screenController.loadScreen(expensesID, expensesFxml);
        
        screenController.setScreen(homeID);
        //screenController.setScreen(expensesID);
        
        Group root = new Group();
        root.getChildren().addAll(screenController);
        Scene scene = new Scene(root, 1000, 650);
        
        primaryStage.setTitle("Business Account Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
