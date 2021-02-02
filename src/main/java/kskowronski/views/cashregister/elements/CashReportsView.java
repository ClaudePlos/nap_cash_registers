package kskowronski.views.cashregister.elements;

import com.vaadin.flow.component.button.Button;
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
import java.util.Arrays;
import java.util.List;

@Component
@UIScope
public class CashReportsView extends VerticalLayout {

    private DocumentService documentService;

    private Grid<Document> gridCashReports;

    private List<Document> reports;

    @Autowired
    public CashReportsView(DocumentService documentService) {
        this.documentService = documentService;
    }

    public VerticalLayout openReports(){

        HorizontalLayout hlReportsHeader = new HorizontalLayout();
        hlReportsHeader.setClassName("hlReportsHeader");

        Button butAdd = new Button("Dodaj Raport Kasowy", e ->{ addNewReportItem(); });
        butAdd.setClassName("butAdd");

        this.gridCashReports = new Grid<>(Document.class);
        gridCashReports.setClassName("gridCashReports");
        gridCashReports.setColumns();
        Grid.Column<Document> docNo = gridCashReports.addColumn("docNo");
        gridCashReports.addColumn("docOwnNumber");

        gridCashReports.addColumn("docInitialState");
        gridCashReports.addColumn("docWn");
        gridCashReports.addColumn("docMa");

        GridSortOrder<Document> order = new GridSortOrder<>(docNo, SortDirection.DESCENDING);
        gridCashReports.sort(Arrays.asList(order));

        hlReportsHeader.add(butAdd);

        add(hlReportsHeader, gridCashReports);
        return this;
    }

    public void setItems(List<Document> reports){
        gridCashReports.setItems(reports);
    }

    private void addNewReportItem(){
        Document report = new Document();

        // nzp_obj_rk.wstaw
        BigDecimal docId = documentService.addNewCashReport();

        report.setDocNo(docId);

        reports.add(report);
        gridCashReports.getDataProvider().refreshAll();
    }
}
