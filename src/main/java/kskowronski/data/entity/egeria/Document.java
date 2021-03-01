package kskowronski.data.entity.egeria;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "kgt_dokumenty")
public class Document {

    @Id
    @Column(name = "DOK_ID")
    private BigDecimal docId;

    @Column(name = "DOK_KAS_ID")
    private BigDecimal docCasId;

    @Column(name = "DOK_NUMER_WLASNY")
    private String docOwnNumber;

    @Column(name = "DOK_NUMER_SORT")
    private String docSortNumber;

    @Column(name = "DOK_NUMER_KOLEJNY")
    private BigDecimal docNextNumber;

    @Column(name = "DOK_KWOTA")
    private BigDecimal docAmount;

    @Column(name = "DOK_NUMER_W_PACZCE")
    private BigDecimal docNumberInPackage;

    @Column(name = "DOK_STAN_POCZATKOWY")
    private BigDecimal docInitialState;

    @Column(name = "DOK_OBROTY_MA")
    private BigDecimal docMa;

    @Column(name = "DOK_OBROTY_WN")
    private BigDecimal docWn;

    @Column(name = "DOK_OKSP_RP_ROK")
    private BigDecimal docYear;

    @Column(name = "DOK_OKSP_MIESIAC")
    private BigDecimal docMonth;

    @Column(name = "DOK_LP")
    private BigDecimal docNo;

    @Column(name = "DOK_FRM_ID")
    private BigDecimal docFrmId;

    @Column(name = "DOK_DATA_OPERACJI")
    private LocalDate docDateFrom;

    @Column(name = "DOK_DATA_DO")
    private LocalDate docDateTo;

    @Column(name = "DOK_DOK_ID_ZAP")
    private BigDecimal docDocIdZap;

    @Enumerated(EnumType.STRING)
    @Column(name = "DOK_RDOK_KOD")
    private KpKwType docRdocCode;

    @Column(name = "DOK_F_ZATWIERDZONY")
    private String docApproved;

    @Column(name = "DOK_KL_KOD_POD")
    private BigDecimal docKlKodPod;

    @Column(name = "DOK_PRC_ID_POD")
    private BigDecimal docPrcIdPod;

    public Document() {
    }

    public BigDecimal docEndState(){
        return docInitialState.add(docWn.subtract(docMa));
    }

    public BigDecimal getDocId() {
        return docId;
    }

    public void setDocId(BigDecimal docId) {
        this.docId = docId;
    }

    public BigDecimal getDocCasId() {
        return docCasId;
    }

    public void setDocCasId(BigDecimal docCasId) {
        this.docCasId = docCasId;
    }

    public String getDocOwnNumber() {
        return docOwnNumber;
    }

    public void setDocOwnNumber(String docOwnNumber) {
        this.docOwnNumber = docOwnNumber;
    }

    public String getDocSortNumber() {
        return docSortNumber;
    }

    public void setDocSortNumber(String docSortNumber) {
        this.docSortNumber = docSortNumber;
    }

    public BigDecimal getDocNextNumber() {
        return docNextNumber;
    }

    public void setDocNextNumber(BigDecimal docNextNumber) {
        this.docNextNumber = docNextNumber;
    }

    public BigDecimal getDocNumberInPackage() {
        return docNumberInPackage;
    }

    public void setDocNumberInPackage(BigDecimal docNumberInPackage) {
        this.docNumberInPackage = docNumberInPackage;
    }

    public BigDecimal getDocInitialState() {
        return docInitialState;
    }

    public void setDocInitialState(BigDecimal docInitialState) {
        this.docInitialState = docInitialState;
    }

    public BigDecimal getDocMa() {
        return docMa;
    }

    public void setDocMa(BigDecimal docMa) {
        this.docMa = docMa;
    }

    public BigDecimal getDocWn() {
        return docWn;
    }

    public void setDocWn(BigDecimal docWn) {
        this.docWn = docWn;
    }

    public BigDecimal getDocYear() {
        return docYear;
    }

    public void setDocYear(BigDecimal docYear) {
        this.docYear = docYear;
    }

    public BigDecimal getDocMonth() {
        return docMonth;
    }

    public void setDocMonth(BigDecimal docMonth) {
        this.docMonth = docMonth;
    }

    public BigDecimal getDocNo() {
        return docNo;
    }

    public void setDocNo(BigDecimal docNo) {
        this.docNo = docNo;
    }

    public BigDecimal getDocFrmId() {
        return docFrmId;
    }

    public void setDocFrmId(BigDecimal docFrmId) {
        this.docFrmId = docFrmId;
    }

    public LocalDate getDocDateFrom() {
        return docDateFrom;
    }

    public void setDocDateFrom(LocalDate docDateFrom) {
        this.docDateFrom = docDateFrom;
    }

    public LocalDate getDocDateTo() {
        return docDateTo;
    }

    public void setDocDateTo(LocalDate docDateTo) {
        this.docDateTo = docDateTo;
    }

    public BigDecimal getDocDocIdZap() {
        return docDocIdZap;
    }

    public void setDocDocIdZap(BigDecimal docDocIdZap) {
        this.docDocIdZap = docDocIdZap;
    }

    public KpKwType getDocRdocCode() {
        return docRdocCode;
    }

    public void setDocRdocCode(KpKwType docRdocCode) {
        this.docRdocCode = docRdocCode;
    }

    public String getDocApproved() {
        return docApproved;
    }

    public void setDocApproved(String docApproved) {
        this.docApproved = docApproved;
    }

    public BigDecimal getDocAmount() {
        return docAmount;
    }

    public void setDocAmount(BigDecimal docAmount) {
        this.docAmount = docAmount;
    }

    public BigDecimal getDocKlKodPod() {
        return docKlKodPod;
    }

    public void setDocKlKodPod(BigDecimal docKlKodPod) {
        this.docKlKodPod = docKlKodPod;
    }

    public BigDecimal getDocPrcIdPod() {
        return docPrcIdPod;
    }

    public void setDocPrcIdPod(BigDecimal docPrcIdPod) {
        this.docPrcIdPod = docPrcIdPod;
    }
}
