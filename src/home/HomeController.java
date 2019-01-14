/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import business.manager.BusinessManager;
import business.manager.ScreenChangeListener;
import business.manager.ScreenHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author BUKOLA
 */
public class HomeController implements Initializable, ScreenChangeListener {

    ScreenHandler screenController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void setParentScreen(ScreenHandler currentPage) {
        screenController = currentPage;
    }
    
    @FXML
    public void goToInvoice(){
        screenController.setScreen(BusinessManager.invoiceID);
    }
    
}
