package kskowronski.data.entities.egeria.kg;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NZT_KASY")
public class CashRegister implements Serializable {

    @Id
    @Column(name = "kas_Id", nullable = false)
    private BigDecimal casId;

    @Column(name="kas_nazwa")
    private String casName;

    @Column(name="kas_opis")
    private String casDesc;

    @Column(name="kas_frm_id")
    private BigDecimal casFrmId;

    public CashRegister() {
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
}
