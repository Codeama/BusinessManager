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
 * Each @Test cycle resets invoiceTotal to 0;
 *          not the intended result AND this
 *          would be different with JUnit5
 */
public class InvoiceTotalBeanTest {
    
   
    TotalInvoiceBean invoiceTotalBean = new TotalInvoiceBean();
    
    
    @Test
    public void checkInitialValueIsZero(){
        BigDecimal expected = new BigDecimal(0);
        BigDecimal actual = invoiceTotalBean.getTotalInvoice();
        assertEquals(actual, expected);
    }

    /**
     * Test of addToInvoice method, of class TotalInvoiceBean.
     */
    @Test
    public void testAddToInvoice() {
        invoiceTotalBean.addToInvoice(new BigDecimal(55)); //add 55
        BigDecimal expected = new BigDecimal(55);
        BigDecimal actual = invoiceTotalBean.getTotalInvoice();
        assertEquals(expected, actual);
        
        invoiceTotalBean.addToInvoice(new BigDecimal(22)); //add 22
        BigDecimal expected2 = new BigDecimal(77);
        BigDecimal actual2 = invoiceTotalBean.getTotalInvoice();
        assertEquals(expected2, actual2);
    }
    

    /**
     * Test of removeFromInvoice method, of class TotalInvoiceBean.
     */
    @Test
    public void testRemoveFromInvoice() {
        invoiceTotalBean.addToInvoice(new BigDecimal(25)); //add 25
        BigDecimal expected = new BigDecimal(25);
        BigDecimal actual = invoiceTotalBean.getTotalInvoice();
        assertEquals(expected, actual);
        
        invoiceTotalBean.removeFromInvoice(new BigDecimal(25)); //subtract 35
        BigDecimal expected2 = new BigDecimal(0);
        BigDecimal actual2 = invoiceTotalBean.getTotalInvoice();
        assertEquals(expected2, actual2);
    }
    
}
