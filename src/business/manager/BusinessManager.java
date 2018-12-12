/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.manager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
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

    
    @Override
    public void start(Stage primaryStage) {
        ScreenController screenController = new ScreenController();
        screenController.loadScreen(homeID, homeFxml);
        screenController.loadScreen(invoiceID, invoiceFxml);
        
        screenController.setScreen(homeID);
        
        Group root = new Group();
        root.getChildren().addAll(screenController);
        Scene scene = new Scene(root, 780, 543);
        
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
