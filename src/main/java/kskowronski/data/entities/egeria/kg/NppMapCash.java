package kskowronski.data.entities.egeria.kg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "npp_mapowania_kas")
public class NppMapCash {

    @Id
    @Column(name = "ID")
    private BigDecimal id;

    @Column(name = "KOD_KASY")
    private String cashCode;

    @Column(name = "UTARGI")
    private String incomeCode;

    @Column(name = "FV_GOT")
    private String cashInvoiceCode;

    @Column(name = "WPLATA_DO_BANKU")
    private String bankCode;

    @Column(name = "FIRMA")
    private String companyName;

    public NppMapCash() {
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCashCode() {
        return cashCode;
    }

    public void setCashCode(String cashCode) {
        this.cashCode = cashCode;
    }

    public String getIncomeCode() {
        return incomeCode;
    }

    public void setIncomeCode(String incomeCode) {
        this.incomeCode = incomeCode;
    }

    public String getCashInvoiceCode() {
        return cashInvoiceCode;
    }

    public void setCashInvoiceCode(String cashInvoiceCode) {
        this.cashInvoiceCode = cashInvoiceCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
