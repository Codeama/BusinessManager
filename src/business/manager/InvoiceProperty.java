/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.manager;

import java.math.BigDecimal;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author BUKOLA
 */
public class InvoiceProperty {
    private SimpleObjectProperty<BigDecimal> invoiceTotal;
    private BigDecimal totalProperty = new BigDecimal(0);
    
    public InvoiceProperty(){
        invoiceTotal = new SimpleObjectProperty(BigDecimal.ZERO);
        
    }
    
    /**
     * 
     * @param total is the total of each row item
     * @see RateBean#getTotal() 
     * @return total value of an invoice
     */
    public BigDecimal getInvoiceTotal(BigDecimal total){
        //total = bean.getTotal();
        //this.itemTotal = new SimpleObjectProperty(total);
        ObjectBinding<BigDecimal> sumTotal= new ObjectBinding(){
            @Override
            protected BigDecimal computeValue() {
                totalProperty = totalProperty.add(total);
                return totalProperty;
            }
        };
        invoiceTotal.bind(sumTotal);
        return invoiceTotal.get();
    }
    
//    public static void main(String[] args){
//        InvoiceProperty invoice = new InvoiceProperty();
//        BigDecimal total3= invoice.getInvoiceTotal(BigDecimal.ONE);
//        System.out.println("First sum: "+ total3);
//        BigDecimal total4= invoice.getInvoiceTotal(new BigDecimal(9));
//        System.out.println("Second: "+ total4);
//    }
    
    
    
}
