/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author BUKOLA
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({invoice.InvoiceBeanTest.class, invoice.InvoicePDFTemplateTest.class, invoice.InvoiceTest.class, invoice.InvoicePDFContentTest.class, invoice.InvoiceControllerTest.class, invoice.RateBeanTest.class})
public class InvoiceSuite {
    
}
