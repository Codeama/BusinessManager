/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.manager;

import java.math.BigDecimal;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author BUKOLA
 */
public class RateBean{
    private StringProperty description;
    private ObjectProperty price;
    private ObjectProperty quantity;
    private ObjectProperty total;
    
    public RateBean(String description, BigDecimal quantity, BigDecimal price) {
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleObjectProperty<>(price);
        this.quantity = new SimpleObjectProperty<>(quantity);
            }
    
    public String getDescription(){
        return description.get();
    }

    public void setDescription(String description){
        this.description.set(description);
    }

    public Object getPrice(){
        return price.get();
    }

    public void setPrice(BigDecimal price){
        this.price.set(price);
    }

    
    public Object getQuantity(){
        return quantity.get();
    }

    public void setQuantity(BigDecimal quantity){
        this.quantity.set(quantity);
    }
    
    public Object getTotal(){
        total = new SimpleObjectProperty();
        total.bind(Bindings.multiply(getQuantity(), getPrice()));
        //ObjectBinding<BigDecimal> amount = quantity.//.multiply(price);
        this.total.set(amount.getValue().doubleValue());
        return total.get();
    }
    
    
}
