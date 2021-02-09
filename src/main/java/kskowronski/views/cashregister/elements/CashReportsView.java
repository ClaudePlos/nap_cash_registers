package kskowronski.views.cashregister.elements;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entity.egeria.Document;
import kskowronski.data.service.egeria.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@UIScope
public class CashReportsView extends VerticalLayout {

    private transient DocumentService documentService;

    private Grid<Document> gridCashReports;
    private DatePicker from = new DatePicker();
    private DatePicker to = new DatePicker();

    private BigDecimal casId;
    private BigDecimal frmId;
    private BigDecimal lp;
    private BigDecimal initialValue = BigDecimal.ZERO;


    @Autowired
    public CashReportsView(DocumentService documentService) {
        this.documentService = documentService;
    }

    public VerticalLayout openReports(){

        HorizontalLayout hlReportsHeader = new HorizontalLayout();
        hlReportsHeader.setClassName("hlReportsHeader");

        Button butAdd = new Button("Dodaj Raport Kasowy", e -> addNewReportItem());
        butAdd.setClassName("butAdd");

        LocalDate now = LocalDate.now();
        from.setValue(now);
        to.setValue(now);

        this.gridCashReports = new Grid<>(Document.class);
        gridCashReports.setClassName("gridCashReports");
        gridCashReports.setColumns();
        Grid.Column<Document> docNo = gridCashReports.addColumn("docNo").setHeader("Lp");
        gridCashReports.addColumn("docOwnNumber").setHeader("Numer raportu");
        gridCashReports.addColumn("docFrom").setHeader("Od dnia");
        gridCashReports.addColumn("docTo").setHeader("Do dnia");
        gridCashReports.addColumn("docInitialState").setHeader("Stan początkowy");
        gridCashReports.addColumn("docWn").setHeader("WN");
        gridCashReports.addColumn("docMa").setHeader("MA");

        GridSortOrder<Document> order = new GridSortOrder<>(docNo, SortDirection.DESCENDING);
        gridCashReports.sort(Arrays.asList(order));

        hlReportsHeader.add(from, to, butAdd);

        add(hlReportsHeader, gridCashReports);
        return this;
    }

    public void setItems(List<Document> reports, BigDecimal casId, BigDecimal frmId){
        this.casId = casId;
        this.frmId = frmId;
        gridCashReports.setItems(reports);

        //Document doc = reports.get(reports.size()-1); // last row
        Document doc = reports.get(0);
        lp = doc.getDocNo();
        if (doc.getDocWn() != null && doc.getDocMa() != null){
            initialValue = doc.getDocInitialState().subtract(doc.getDocWn().subtract(doc.getDocMa()));
        }
    }

    private void addNewReportItem(){
        // nzp_obj_rk.wstaw
        Document doc = documentService.addNewCashReport(casId, frmId, lp.add(BigDecimal.ONE), from.getValue(), to.getValue(), initialValue );
        gridCashReports.setItems(doc);
        gridCashReports.getDataProvider().refreshAll();
    }
}
