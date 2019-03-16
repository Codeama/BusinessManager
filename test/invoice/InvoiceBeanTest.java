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
    
   
    InvoiceBean invoiceBean;
    
    @Before
    public void setUp(){
        invoiceBean = new InvoiceBean();
    }
    
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
        BigDecimal expected = new BigDecimal(55);
        invoiceBean.addToInvoice(new BigDecimal(55));
        BigDecimal actual = invoiceBean.getTotalInvoice();
        assertEquals(actual, expected);
    }
    
    /**
     * Test of addToInvoice method, of class InvoiceBean.
     */
    @Test
    public void testAddToInvoice2() {
        BigDecimal expected = new BigDecimal(110);
        BigDecimal actual = invoiceBean.addToInvoice(new BigDecimal(55));
         //invoiceBean.getTotalInvoice();
        assertEquals(invoiceBean.getTotalInvoice(), expected);
    }

    /**
     * Test of removeFromInvoice method, of class InvoiceBean.
     */
    @Test
    public void testRemoveFromInvoice() {
    }
    
}
