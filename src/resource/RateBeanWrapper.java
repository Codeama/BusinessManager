/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import invoice.RateBean;

/**
 *
 * @author BUKOLA
 */
public class RateBeanWrapper {
    FlatRateBean flatRate;
    RateBean unitRate;
    
    public RateBeanWrapper(FlatRateBean flatRate, RateBean unitRate){
        this.flatRate = flatRate;
        this.unitRate = unitRate;
    }
    
    public FlatRateBean getInvoice(){
        return flatRate;
    }
    
    public RateBean getInvoice2(){
        return unitRate;
    }
}
