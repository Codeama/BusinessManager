/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import java.math.BigDecimal;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author BUKOLA
 */
public class TotalInvoiceBean {
    private SimpleObjectProperty<BigDecimal> invoiceTotal;
    private BigDecimal total = new BigDecimal(0);
    
    public TotalInvoiceBean(){
        invoiceTotal = new SimpleObjectProperty(BigDecimal.ZERO);
        
    }
    
    /**
     * 
     * @return total value of an invoice
     */
    public BigDecimal getTotalInvoice(){
        return invoiceTotal.get();
    }
    
    /**
     * 
     * @param itemTotal is the total of each row item
     * @see RateBean#getTotal() 
     * 
     */
    public void addToInvoice(BigDecimal itemTotal){
        ObjectBinding<BigDecimal> sumTotal= new ObjectBinding(){
            @Override
            protected BigDecimal computeValue() {
                total = total.add(itemTotal);
                return total;
            }
        };
        invoiceTotal.bind(sumTotal);
       // return invoiceTotal.get();
    }
    
    public void removeFromInvoice(BigDecimal itemTotal){
        ObjectBinding<BigDecimal> subtractTotal = new ObjectBinding(){
            @Override
            protected BigDecimal computeValue() {
                total = total.subtract(itemTotal);
                return total;
            }
        };
        invoiceTotal.bind(subtractTotal);
       // return invoiceTotal.get();
    }
    
}
