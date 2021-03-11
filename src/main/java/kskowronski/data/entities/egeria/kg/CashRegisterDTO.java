package kskowronski.data.entities.egeria.kg;

import java.math.BigDecimal;

public class CashRegisterDTO {

    private BigDecimal casId;
    private String casName;
    private String casDesc;
    private BigDecimal casFrmId;
    private String frmName;

    public CashRegisterDTO() {
    }

    public BigDecimal getCasId() {
        return casId;
    }

    public void setCasId(BigDecimal casId) {
        this.casId = casId;
    }

    public String getCasName() {
        return casName;
    }

    public void setCasName(String casName) {
        this.casName = casName;
    }

    public String getCasDesc() {
        return casDesc;
    }

    public void setCasDesc(String casDesc) {
        this.casDesc = casDesc;
    }

    public BigDecimal getCasFrmId() {
        return casFrmId;
    }

    public void setCasFrmId(BigDecimal casFrmId) {
        this.casFrmId = casFrmId;
    }

    public String getFrmName() {
        return frmName;
    }

    public void setFrmName(String frmName) {
        this.frmName = frmName;
    }
}
