/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expenses;

import java.math.BigDecimal;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author BUKOLA
 */
public class ExpensesBean {
    private StringProperty account;
    private StringProperty category;
    private StringProperty description;
    private StringProperty receipt;
    private ObjectProperty<BigDecimal> amount;
    
    public ExpensesBean(){
        this("", "", "", "", BigDecimal.ZERO);
    }
    
    public ExpensesBean(String account, String category, 
            String description, String receipt, BigDecimal amount){
        this.account = new SimpleStringProperty(account);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.receipt= new SimpleStringProperty(receipt);
        this.amount = new SimpleObjectProperty(BigDecimal.ZERO);
    }
    
    public String getAccount(){
        return account.get();
    }
    
    public void setAccount(String account){
        this.account.set(account);
    }
    
     public String getCategory(){
        return category.get();
    }
    
    public void setCategory(String category){
        this.category.set(category);
    }
    
     public String getDescription(){
        return description.get();
    }
    
    public void setDescription(String description){
        this.description.set(description);
    }
    
     public String getReciept(){
        return receipt.get();
    }
    
    public void setReceipt(String receipt){
        this.receipt.set(receipt);
    }
    
     public BigDecimal getAmount(){
        return amount.get();
    }
    
    public void setAmount(BigDecimal amount){
        this.amount.set(amount);
    }
}
