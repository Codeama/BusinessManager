/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expenses;

import java.math.BigDecimal;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author BUKOLA
 */
public class TotalExpensesBean {
    private SimpleObjectProperty<BigDecimal> totalExpenses;
    private BigDecimal total = new BigDecimal(0);
    
    public TotalExpensesBean(){
        totalExpenses = new SimpleObjectProperty(BigDecimal.ZERO);
        
    }
    
    /**
     * 
     * @return total value of an invoice
     */
    public BigDecimal getTotalExpenses(){
        return totalExpenses.get();
    }
    
    /**
     * 
     * @param itemTotal is the total of each row item
     * @see RateBean#getTotal() 
     * 
     */
    public void addToExpenses(BigDecimal itemTotal){
        ObjectBinding<BigDecimal> sumTotal= new ObjectBinding(){
            @Override
            protected BigDecimal computeValue() {
                total = total.add(itemTotal);
                return total;
            }
        };
        totalExpenses.bind(sumTotal);
       // return invoiceTotal.get();
    }
    
    public void removeFromExpenses(BigDecimal itemTotal){
        ObjectBinding<BigDecimal> subtractTotal = new ObjectBinding(){
            @Override
            protected BigDecimal computeValue() {
                total = total.subtract(itemTotal);
                return total;
            }
        };
        totalExpenses.bind(subtractTotal);
       // return invoiceTotal.get();
    }
    
}
