/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private ObjectProperty<BigDecimal> price;
    private ObjectProperty<BigDecimal> quantity;
    private ObjectProperty<BigDecimal> total;
    private ObjectProperty<BigDecimal> runningTotal;
    
    public RateBean(String description, BigDecimal quantity, BigDecimal price) {
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleObjectProperty<>(price);
        this.quantity = new SimpleObjectProperty<>(quantity);
        total = new SimpleObjectProperty(new BigDecimal(0));
        runningTotal = new SimpleObjectProperty(BigDecimal.ZERO);
            }
    
    public String getDescription(){
        return description.get();
    }

    public void setDescription(String description){
        this.description.set(description);
    }

    public BigDecimal getPrice(){
        return price.get();
    }

    public void setPrice(BigDecimal price){
        this.price.set(price);
    }

    
    public BigDecimal getQuantity(){
        return quantity.get();
    }

    public void setQuantity(BigDecimal quantity){
        this.quantity.set(quantity);
    }
    
    public BigDecimal getTotal(){
        //total = new SimpleObjectProperty(new BigDecimal(0));
        ObjectBinding<BigDecimal> sumUp = new ObjectBinding(){
            //{this.bind(quantity, price);} 
            @Override
            protected BigDecimal computeValue() {
                return quantity.get().multiply(price.get());
            }
        };
        total.bind(sumUp);

        return total.getValue().setScale(2, RoundingMode.HALF_UP);
    }
    
}
