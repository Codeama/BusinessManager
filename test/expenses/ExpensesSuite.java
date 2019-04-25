/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expenses;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author BUKOLA
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({expenses.ExpensesControllerTest.class, expenses.ExpensesBeanTest.class, expenses.TotalExpenseBeanTest.class})
public class ExpensesSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    
}
