/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author BUKOLA
 */
public class RateBeanTest {
    
    RateBean rateBean;
    

    /**
     * Test of getDescription method, of class RateBean.
     */
    @Test
    public void testGetDescription() {
        rateBean = new RateBean("Pencil", BigDecimal.ONE, BigDecimal.ZERO);
        assertEquals(rateBean.getDescription(), "Pencil");
    }


    /**
     * Test of getPrice method, of class RateBean.
     */
    @Test
    public void testGetPrice() {
        rateBean = new RateBean("", BigDecimal.ONE, new BigDecimal(1.50));
        BigDecimal expected = new BigDecimal(1.50);
        BigDecimal actual = rateBean.getPrice();
        assertEquals(actual, expected);
    }

     /**
     * Test of getQuantity method, of class RateBean.
     */
    
    @Test
    public void testGetQuantity() {
        rateBean = new RateBean("", BigDecimal.ONE, BigDecimal.ZERO);
        BigDecimal expected = new BigDecimal(1);
        BigDecimal actual = rateBean.getQuantity();
        assertEquals(actual, expected);
    }
    

    /**
     * Test of setQuantity method, of class RateBean.
     */
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Test
    public void testSetQuantityIsNotZero() throws IllegalArgumentException{
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Quantity cannot be <= ZERO");
        rateBean = new RateBean("", BigDecimal.TEN, BigDecimal.ONE);
        rateBean.setQuantity(BigDecimal.ZERO);
    }

    /**
     * Test of getTotal method, of class RateBean.
     */
    @Test
    public void testGetTotal() {
        rateBean = new RateBean("Bartending", new BigDecimal(5), new BigDecimal(3.25));
        BigDecimal expected = new BigDecimal(16.25);
        BigDecimal actual = rateBean.getTotal();
        assertEquals(actual, expected);
    }
    
}
