/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author BUKOLA
 */
public class InvoiceBeanTest {
    
   
    InvoiceBean invoiceBean = new InvoiceBean();
    
    
    @Test
    public void checkInitialValueIsZero(){
        BigDecimal expected = new BigDecimal(0);
        BigDecimal actual = invoiceBean.getTotalInvoice();
        assertEquals(actual, expected);
    }

    /**
     * Test of addToInvoice method, of class InvoiceBean.
     */
    @Test
    public void testAddToInvoice() {
        invoiceBean.addToInvoice(new BigDecimal(55)); //add 55
        BigDecimal expected = new BigDecimal(55);
        BigDecimal actual = invoiceBean.getTotalInvoice();
        assertEquals(expected, actual);
        
        invoiceBean.addToInvoice(new BigDecimal(22)); //add 22
        BigDecimal expected2 = new BigDecimal(77);
        BigDecimal actual2 = invoiceBean.getTotalInvoice();
        assertEquals(expected2, actual2);
    }
    

    /**
     * Test of removeFromInvoice method, of class InvoiceBean.
     */
    @Test
    public void testRemoveFromInvoice() {
        invoiceBean.addToInvoice(new BigDecimal(25)); //add 25
        BigDecimal expected = new BigDecimal(25);
        BigDecimal actual = invoiceBean.getTotalInvoice();
        assertEquals(expected, actual);
        
        invoiceBean.removeFromInvoice(new BigDecimal(25)); //subtract 35
        BigDecimal expected2 = new BigDecimal(0);
        BigDecimal actual2 = invoiceBean.getTotalInvoice();
        assertEquals(expected2, actual2);
    }
    
}
