/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity_classes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author BUKOLA
 */
@Entity
@Table(name = "TAX")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tax.findAll", query = "SELECT t FROM Tax t")
    , @NamedQuery(name = "Tax.findByWeekEnding", query = "SELECT t FROM Tax t WHERE t.weekEnding = :weekEnding")
    , @NamedQuery(name = "Tax.findByIncome", query = "SELECT t FROM Tax t WHERE t.income = :income")
    , @NamedQuery(name = "Tax.findByExpenses", query = "SELECT t FROM Tax t WHERE t.expenses = :expenses")
    , @NamedQuery(name = "Tax.findByProfit", query = "SELECT t FROM Tax t WHERE t.profit = :profit")
    , @NamedQuery(name = "Tax.findByNi2", query = "SELECT t FROM Tax t WHERE t.ni2 = :ni2")
    , @NamedQuery(name = "Tax.findByNi4", query = "SELECT t FROM Tax t WHERE t.ni4 = :ni4")
    , @NamedQuery(name = "Tax.findByIncomeTax", query = "SELECT t FROM Tax t WHERE t.incomeTax = :incomeTax")
    , @NamedQuery(name = "Tax.findByTotal", query = "SELECT t FROM Tax t WHERE t.total = :total")
    , @NamedQuery(name = "Tax.findByRunningTotal", query = "SELECT t FROM Tax t WHERE t.runningTotal = :runningTotal")})
public class Tax implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "WEEK_ENDING")
    @Temporal(TemporalType.DATE)
    private Date weekEnding;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "INCOME")
    private BigDecimal income;
    @Basic(optional = false)
    @Column(name = "EXPENSES")
    private BigDecimal expenses;
    @Basic(optional = false)
    @Column(name = "PROFIT")
    private BigDecimal profit;
    @Basic(optional = false)
    @Column(name = "NI2")
    private BigDecimal ni2;
    @Basic(optional = false)
    @Column(name = "NI4")
    private BigDecimal ni4;
    @Basic(optional = false)
    @Column(name = "INCOME_TAX")
    private BigDecimal incomeTax;
    @Basic(optional = false)
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Basic(optional = false)
    @Column(name = "RUNNING_TOTAL")
    private BigDecimal runningTotal;

    public Tax() {
    }

    public Tax(Date weekEnding) {
        this.weekEnding = weekEnding;
    }

    public Tax(Date weekEnding, BigDecimal income, BigDecimal expenses, BigDecimal profit, BigDecimal ni2, BigDecimal ni4, BigDecimal incomeTax, BigDecimal total, BigDecimal runningTotal) {
        this.weekEnding = weekEnding;
        this.income = income;
        this.expenses = expenses;
        this.profit = profit;
        this.ni2 = ni2;
        this.ni4 = ni4;
        this.incomeTax = incomeTax;
        this.total = total;
        this.runningTotal = runningTotal;
    }

    public Date getWeekEnding() {
        return weekEnding;
    }

    public void setWeekEnding(Date weekEnding) {
        this.weekEnding = weekEnding;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getNi2() {
        return ni2;
    }

    public void setNi2(BigDecimal ni2) {
        this.ni2 = ni2;
    }

    public BigDecimal getNi4() {
        return ni4;
    }

    public void setNi4(BigDecimal ni4) {
        this.ni4 = ni4;
    }

    public BigDecimal getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(BigDecimal incomeTax) {
        this.incomeTax = incomeTax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getRunningTotal() {
        return runningTotal;
    }

    public void setRunningTotal(BigDecimal runningTotal) {
        this.runningTotal = runningTotal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (weekEnding != null ? weekEnding.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tax)) {
            return false;
        }
        Tax other = (Tax) object;
        if ((this.weekEnding == null && other.weekEnding != null) || (this.weekEnding != null && !this.weekEnding.equals(other.weekEnding))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity_classes.Tax[ weekEnding=" + weekEnding + " ]";
    }
    
}
