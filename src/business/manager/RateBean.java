/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.manager;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author BUKOLA
 */
public class RateBean{
    private SimpleStringProperty description;
    private SimpleIntegerProperty price;
    private SimpleIntegerProperty quantity;
    private DoubleProperty total;
    
    public RateBean(String description, Integer quantity, Integer price) {
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleIntegerProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
            }
    
    public String getDescription(){
        return description.get();
    }

    public void setDescription(String description){
        this.description.set(description);
    }

    public int getPrice(){
        return price.get();
    }

    public void setPrice(Integer price){
        this.price.set(price);
    }

    
    public int getQuantity(){
        return quantity.get();
    }

    public void setQuantity(Integer quantity){
        this.quantity.set(quantity);
    }
    
    public double getTotal(){
        total = new SimpleDoubleProperty();
        NumberBinding amount = quantity.multiply(price);
        this.total.set(amount.getValue().doubleValue());
        return total.get();
    }
    
    
}
