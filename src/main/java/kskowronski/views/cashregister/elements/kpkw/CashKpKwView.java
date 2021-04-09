package kskowronski.views.cashregister.elements.kpkw;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entities.egeria.kg.Document;
import kskowronski.data.entities.egeria.kg.KpKwType;
import kskowronski.data.services.egeria.kg.DocumentService;
import kskowronski.data.services.egeria.ckk.ClientService;
import kskowronski.data.services.egeria.ek.WorkerService;
import kskowronski.data.services.egeria.kg.NppMapCashService;
import kskowronski.data.services.global.GlobalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
@UIScope
@CssImport("./styles/views/cashregister/cash-kpkw-view.css")
public class CashKpKwView extends Dialog {

    private Html title = new Html("<div>KP/KW </div>");
    private Label labCash = new Label("-");
    static Logger logger = Logger.getLogger(CashKpKwView.class.getName());

    private Grid<Document> gridCashKpKw;

    public transient DocumentService documentService;

    private transient List<Document> listDocKpKw;
    private transient Document cashReportItem;

    private Button butAddNewKpKw = new Button("Dodaj KP/KW");
    private KpKwForm formKpKw;
    public String cashCode;
    private Label cashInCase = new Label();
    @Autowired
    public CashKpKwView(DocumentService documentService, ClientService clientService, WorkerService workerService, GlobalDataService globalDataService, NppMapCashService nppMapCashService) {
        logger.log(Level.INFO, "Constructor CashKpKwView");
        this.documentService = documentService;
        this.formKpKw = new KpKwForm(this, clientService, workerService, globalDataService, nppMapCashService);
        setWidth("750px");
        setHeight("680px");

        butAddNewKpKw.setEnabled(false);
        // Test 1. INSERT NEW item KP or KW
        butAddNewKpKw.addClickListener( e -> {
            Optional<Document> newDocKpKw = documentService.insertKpKw(cashReportItem.getDocId(), cashReportItem.getDocFrmId());
            if (newDocKpKw.isPresent()){
                newDocKpKw.get().setDocDef0("147-"+cashCode);
                newDocKpKw.get().setDocDef1(globalDataService.transactions.get(0).getCode());
                newDocKpKw.get().setDocSettlement("N");
                newDocKpKw.get().setDocDescription("Utarg");
                formKpKw.docRdocCode.setValue(KpKwType.KP);
                newDocKpKw.get().setDocDateFrom(LocalDate.now());
                listDocKpKw.add(newDocKpKw.get());
                gridCashKpKw.getDataProvider().refreshAll();
                gridCashKpKw.select(newDocKpKw.get());
                gridCashKpKw.asSingleSelect().addValueChangeListener(event ->
                        formKpKw.setDocument(gridCashKpKw.asSingleSelect().getValue()));
                formKpKw.docRdocCode.setEnabled(false);
                documentService.updateKpKw(newDocKpKw.get());
            } else {
                updateList(BigDecimal.ZERO);
            }
        });

        this.gridCashKpKw = new Grid<>(Document.class);
        gridCashKpKw.setClassName("gridCashKpKw");
        formKpKw.setClassName("formKpKw");
        gridCashKpKw.setColumns();
        Grid.Column<Document> docNo = gridCashKpKw.addColumn("docNo").setHeader("Lp").setWidth("10px");
        gridCashKpKw.addColumn("docOwnNumber").setHeader("Numer dokumentu");
        VerticalLayout v01 = new VerticalLayout();
        HorizontalLayout h00 = new HorizontalLayout(title, labCash, cashInCase);
        HorizontalLayout h01 = new HorizontalLayout(gridCashKpKw, formKpKw);
        v01.add(butAddNewKpKw, h01);

        add(h00, v01);

        gridCashKpKw.asSingleSelect().addValueChangeListener(event ->
                formKpKw.setDocument(gridCashKpKw.asSingleSelect().getValue()));
    }

    public void openKpKwView(Document item){
        gridCashKpKw.setItems();
        this.cashReportItem = item;
        this.cashInCase.setText( item.getDocInitialState() + "");

        if (this.cashReportItem.getDocApproved().equals("T"))
            butAddNewKpKw.setEnabled(false);
        else
            butAddNewKpKw.setEnabled(true);
        labCash.setText(cashCode);
        formKpKw.setDocument(null);
        updateList(BigDecimal.ZERO);
    }

    public void updateList(BigDecimal docId) {
        listDocKpKw = documentService.getAllCashKpKw(cashReportItem.getDocId(), cashReportItem.getDocFrmId());
        gridCashKpKw.setItems(listDocKpKw);
        if (!listDocKpKw.isEmpty()) {
            if (docId.equals(BigDecimal.ZERO)) // update the first element from the grid
                formKpKw.setDocument(listDocKpKw.get(0) );
            else
                formKpKw.setDocument(listDocKpKw.stream().filter( document -> document.getDocId().equals(docId)).collect(Collectors.toList()).get(0) );
        }
    }

    public void deleteDocFromList(Document doc){
        listDocKpKw.remove(doc);
        gridCashKpKw.getDataProvider().refreshAll();
    }

}
