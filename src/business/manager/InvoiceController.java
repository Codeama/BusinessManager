/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.manager;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author BUKOLA
 */
public class InvoiceController implements Initializable, ScreenHandler {

    ScreenController screenController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
        @Override
    public void setParentScreen(ScreenController currentPage) {
        screenController = currentPage;
    }
    
    @FXML
    public void goToHome(){
        screenController.setScreen(BusinessManager.homeID);
    }

    
}
