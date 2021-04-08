package kskowronski.views.cashregister.elements;

import com.google.gson.Gson;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entities.egeria.kg.Document;
import kskowronski.data.services.egeria.kg.DocumentService;
import kskowronski.views.cashregister.elements.kpkw.CashKpKwView;
import kskowronski.views.components.MyNotification;
import kskowronski.views.components.PeriodLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
@UIScope
@JavaScript("./js/cash_report.js")
public class CashReportsView extends VerticalLayout {

    private transient DocumentService documentService;

    private Grid<Document> gridCashReports;
    private PeriodLayout period = new PeriodLayout();

    private transient List<Document> reports;
    private BigDecimal casId;
    private String cashCode;
    private BigDecimal frmId;
    private BigDecimal endValue = BigDecimal.ZERO;

    private transient Document selectedDocument = null;

    Button butAdd = new Button("Dodaj Raport Kasowy", e -> addNewReportItem());
    Button butAcceptReport = new Button("Zatwierdź", e -> acceptReportItem());

    @Autowired
    private CashKpKwView cashKpKwView;

    @Autowired
    public CashReportsView(DocumentService documentService) {
        this.documentService = documentService;

        HorizontalLayout hlReportsHeader = new HorizontalLayout();
        hlReportsHeader.setClassName("hlReportsHeader");

        butAdd.setClassName("butAdd");
        butAcceptReport.setEnabled(false);
        butAdd.setClassName("butAcceptReport");

        this.gridCashReports = new Grid<>(Document.class);
        gridCashReports.setClassName("gridCashReports");
        gridCashReports.setColumns();
        Grid.Column<Document> docNo = gridCashReports.addColumn("docNo").setHeader("Lp").setWidth("30px");
        gridCashReports.addColumn("docOwnNumber").setHeader("Numer raportu").setWidth("200px");
        gridCashReports.addColumn("docDateFrom").setHeader("Od dnia");
        gridCashReports.addColumn("docDateTo").setHeader("Do dnia");
        gridCashReports.addColumn("docInitialState").setHeader("Stan początkowy");
        gridCashReports.addColumn("docWn").setHeader("Wpłaty");
        gridCashReports.addColumn("docMa").setHeader("Wypłaty");
        gridCashReports.addComponentColumn(item -> createEndState(item)).setHeader("Stan końcowy");
        gridCashReports.addColumn(new NativeButtonRenderer<Document>("KP/KW",
                item -> {
                    VerticalLayout vertical = new VerticalLayout ();
                    cashKpKwView.cashCode = cashCode;
                    cashKpKwView.openKpKwView(item);
                    cashKpKwView.add(vertical);
                    cashKpKwView.open();
                }
        )).setWidth("50px");
        gridCashReports.addColumn(new NativeButtonRenderer<Document>("Raport",
                item -> {
                    try {
                        generateCashReport(item.getDocId(), item.getDocOwnNumber(), item.getDocFrmId(), item.getDocInitialState());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        )).setWidth("50px");

        gridCashReports.addSelectionListener( e -> {
            if ( e.getFirstSelectedItem().isPresent() ){
                selectedDocument = e.getFirstSelectedItem().get();
                if ( selectedDocument.getDocApproved().equals("N") )
                    butAcceptReport.setEnabled(true);
                else
                    butAcceptReport.setEnabled(false);
            }
        });

        GridSortOrder<Document> order = new GridSortOrder<>(docNo, SortDirection.DESCENDING);
        gridCashReports.sort(Arrays.asList(order));

        hlReportsHeader.add(period, butAdd, butAcceptReport);

        add(hlReportsHeader, gridCashReports);

    }

    private Label createEndState(Document item) {
        return new Label(item.docEndState().toString());
    }

    public VerticalLayout openReports(){
        return this;
    }

    public void setItems(List<Document> reports, BigDecimal casId, String cashCode, BigDecimal frmId){
        gridCashReports.deselectAll();
        this.reports = reports;
        this.casId = casId;
        this.cashCode = cashCode;
        this.frmId = frmId;
        gridCashReports.setItems(this.reports);
    }

    private void addNewReportItem(){
        if (this.reports == null){
            MyNotification.openAlert("Bark wybranej kasy",3000,  Notification.Position.MIDDLE);
            return;
        }
        //Calculate data
        Document docCal = this.reports.get(0);
        LocalDate from = period.getFirstDayOfPeriod();
        LocalDate to = period.getLastDayOfPeriod();
        if (docCal.getDocDateTo().isAfter(from) || docCal.getDocDateTo().isEqual(from)){
            MyNotification.openAlert("Nowy okres nie może być wcześniej niż ostatni !",3000,  Notification.Position.MIDDLE);
            return;
        }
        BigDecimal lp = docCal.getDocNo();
        if (docCal.getDocWn() != null && docCal.getDocMa() != null){
            endValue = docCal.docEndState();
        }
        // nzp_obj_rk.wstaw
        Optional<Document> doc = documentService.addNewCashReport(casId, frmId, lp.add(BigDecimal.ONE), from, to, endValue );
        if (doc.isPresent()){
            this.reports.add(doc.get());
            this.reports.sort(Comparator.comparing(Document::getDocNo).reversed()); //order by desc
            gridCashReports.getDataProvider().refreshAll();
        }
    }

    private void acceptReportItem(){
        Optional<Document> doc = documentService.acceptDocument(selectedDocument.getDocId(), selectedDocument.getDocId(), selectedDocument.getDocFrmId());
        if (doc.isPresent()){
            Document document = (Document) gridCashReports.getDataProvider().getId(selectedDocument);
            document.setDocOwnNumber(doc.get().getDocOwnNumber());
            document.setDocApproved(doc.get().getDocApproved());
            document.setDocInitialState(doc.get().getDocInitialState());
            document.setDocWn(doc.get().getDocWn());
            document.setDocMa(doc.get().getDocMa());
            gridCashReports.getDataProvider().refreshAll();
            butAcceptReport.setEnabled(false);
            Notification.show("Zatwierdzono",1000, Notification.Position.MIDDLE);
        }
    }

    private void generateCashReport(BigDecimal docId, String docNumber, BigDecimal docFrmId, BigDecimal valueInitialState) throws IOException {
        Gson gson = new Gson();
        List<Document> listDocKpKw = documentService.getAllCashKpKw(docId, docFrmId);
        //Run js
        //listDocKpKw.get().sort(Comparator.comparing(Document::getDocNo)); //asc
        String initFunction = "generateCashReport($0, $1, $2, $3, $4, $5);";
        UI.getCurrent().getPage().executeJs(initFunction, this,
                cashCode, docNumber, period.getPeriod(), gson.toJson(listDocKpKw), valueInitialState.toString());

    }
}
