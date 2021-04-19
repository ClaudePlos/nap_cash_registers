package kskowronski.data.entities.egeria.ek;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "EK_PRACOWNICY")
public class Worker {

    @Id
    @Column(name = "PRC_ID")
    private BigDecimal prcId;

    @Column(name = "PRC_NUMER", insertable=false, updatable=false)
    private String username;

    @Column(name = "PRC_NUMER")
    private BigDecimal prcNumer;

    @Column(name = "PRC_IDENTYFIKACJA")
    private String password;

    @Column(name = "PRC_PESEL")
    private String prcPesel;

    @Column(name = "PRC_IMIE")
    private String prcImie;

    @Column(name = "PRC_NAZWISKO")
    private String prcNazwisko;

    @Column(name = "PRC_DG_KOD_EK")
    private String prcDgKodEk;

    public String getNazwImie () {
        return prcNazwisko + " " + prcImie;
    }

    public Worker() {
    }

    public BigDecimal getPrcId() {
        return prcId;
    }

    public void setPrcId(BigDecimal prcId) {
        this.prcId = prcId;
    }

    public BigDecimal getPrcNumer() {
        return prcNumer;
    }

    public void setPrcNumer(BigDecimal prcNumer) {
        this.prcNumer = prcNumer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrcImie() {
        return prcImie;
    }

    public void setPrcImie(String prcImie) {
        this.prcImie = prcImie;
    }

    public String getPrcNazwisko() {
        return prcNazwisko;
    }

    public void setPrcNazwisko(String prcNazwisko) {
        this.prcNazwisko = prcNazwisko;
    }

    public String getPrcPesel() {
        return prcPesel;
    }

    public void setPrcPesel(String prcPesel) {
        this.prcPesel = prcPesel;
    }

    public String getPrcDgKodEk() {
        return prcDgKodEk;
    }

    public void setPrcDgKodEk(String prcDgKodEk) {
        this.prcDgKodEk = prcDgKodEk;
    }



}
