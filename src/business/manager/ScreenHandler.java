/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.manager;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**
 * Class for storing fxml files for each menu item.
 * It uses a HashMap for this purpose identifying each node
 * by the fxml file name, i.e.<String, Node>
 * @author BUKOLA
 */
class ScreenHandler extends StackPane{
    private HashMap<String, Node> screens = new HashMap<>();
    
    public ScreenHandler(){
        super();
    }
    
    public void addScreen(String name, Node screen){
        screens.put(name, screen);
    }
    
    public Node getScreen(String name){
        return screens.get(name);
    }
    
    //used by main class
    public boolean loadScreen(String name, String resource){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource(resource));
            Parent parent = (Parent)fxmlLoader.load();
            ScreenChangeListener screenHandler = (ScreenChangeListener)fxmlLoader
                    .getController();
            System.out.printf("ScreenHandler fxml: %s%n", screenHandler);
            screenHandler.setParentScreen(this);
            addScreen(name, parent);
            return true;
        }catch(IOException exception){
            System.out.println(exception.getMessage());
        }
        return false;
    }
    
    //used for navigation between each fxml screens
    public boolean setScreen(final String name){
        //checks if screen already exists in HashMap
        if(screens.get(name)!= null){
            //and if stackPane has loaded screen(s)
            if(!getChildren().isEmpty()){
                getChildren().remove(0); //remove topmost
                getChildren().add(0, screens.get(name)); //then add selected screen
                
            }
            else{
                getChildren().add(screens.get(name));//add screen to stack
            }
            return true;
        }
        else{
            System.out.println("Screen hasn't been loaded!");
            return false;
        }
    }
    
    public boolean unloadScreen(String name){
        if(screens.remove(name) == null){
            System.out.println("Screen doesn't exist!");
            return false;
        }
        else{
            return true;
        }
    }
}
