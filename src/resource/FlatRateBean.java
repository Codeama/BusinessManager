/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author BUKOLA
 */
    //class for data testing in TableView
    public class FlatRateBean{
        private SimpleStringProperty description;
        private SimpleIntegerProperty price;
        //private SimpleIntegerProperty quantity;
        
        public FlatRateBean(String description, Integer price){
            this.description = new SimpleStringProperty(description);
            this.price = new SimpleIntegerProperty(price);
        }
        
//        public FlatRateBean(String description, Integer quantity, Integer price){
//            this.description = new SimpleStringProperty(description);
//            this.price = new SimpleIntegerProperty(price);
//            this.quantity = new SimpleIntegerProperty(quantity);
//        }
        
        public String getDescription(){
            return description.get();
        }
        
        public void setDescription(String description){
            this.description.set(description);
        }
        
        public int getPrice(){
            return price.get();
        }
        
        public void setPrice(Integer amount){
            this.price.set(amount);
        }
        
//        public int getQuantity(){
//            return quantity.get();
//        }
//        
//        public void setQuantity(Integer quantity){
//            this.quantity.set(quantity);
//        }
    }
