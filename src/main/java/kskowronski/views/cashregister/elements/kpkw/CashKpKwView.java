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
import kskowronski.data.entities.egeria.kg.TransactionDTO;
import kskowronski.data.entities.egeria.kg.TransactionType;
import kskowronski.data.services.egeria.kg.DocumentService;
import kskowronski.data.services.egeria.ckk.ClientService;
import kskowronski.data.services.egeria.ek.WorkerService;
import kskowronski.data.services.global.GlobalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@UIScope
@CssImport("./styles/views/cashregister/cash-kpkw-view.css")
public class CashKpKwView extends Dialog {

    private Html title = new Html("<div>KP/KW </div>");
    private Label labCash = new Label("-");
    static Logger logger = Logger.getLogger(CashKpKwView.class.getName());

    private Grid<Document> gridCashKpKw;

    public transient DocumentService documentService;

    private transient Optional<List<Document>> listDocKpKw;
    private transient Document cashReportItem;

    private Button butAddNewKpKw = new Button("Dodaj KP/KW");
    private KpKwForm formKpKw;
    public String cashCode;

    @Autowired
    public CashKpKwView(DocumentService documentService, ClientService clientService, WorkerService workerService, GlobalDataService globalDataService) {
        logger.log(Level.INFO, "Constructor CashKpKwView");
        this.documentService = documentService;
        this.formKpKw = new KpKwForm(this, clientService, workerService, globalDataService);
        setWidth("750px");
        setHeight("680px");

        butAddNewKpKw.setEnabled(false);
        // Test 1. INSERT NEW item KP or KW
        butAddNewKpKw.addClickListener( e -> {
            Optional<Document> newDocKpKw = documentService.insertKpKw(cashReportItem.getDocId(), cashReportItem.getDocFrmId());
            if (newDocKpKw.isPresent() && listDocKpKw.isPresent()){
                newDocKpKw.get().setDocDef0("147-"+cashCode);
                newDocKpKw.get().setDocDef1(globalDataService.transactions.get(0).getCode());
                newDocKpKw.get().setDocSettlement("N");
                newDocKpKw.get().setDocDescription("Utarg");
                formKpKw.docRdocCode.setValue(KpKwType.KP);
                newDocKpKw.get().setDocDateFrom(LocalDate.now());
                listDocKpKw.get().add(newDocKpKw.get());
                gridCashKpKw.getDataProvider().refreshAll();
                gridCashKpKw.select(newDocKpKw.get());
                gridCashKpKw.asSingleSelect().addValueChangeListener(event ->
                        formKpKw.setDocument(gridCashKpKw.asSingleSelect().getValue()));
                formKpKw.docRdocCode.setEnabled(false);
                documentService.updateKpKw(newDocKpKw.get());
            } else {
                updateList(0);
            }
        });

        this.gridCashKpKw = new Grid<>(Document.class);
        gridCashKpKw.setClassName("gridCashKpKw");
        formKpKw.setClassName("formKpKw");
        gridCashKpKw.setColumns();
        Grid.Column<Document> docNo = gridCashKpKw.addColumn("docNo").setHeader("Lp").setWidth("10px");
        gridCashKpKw.addColumn("docOwnNumber").setHeader("Numer dokumentu");
        VerticalLayout v01 = new VerticalLayout();
        HorizontalLayout h00 = new HorizontalLayout(title, labCash);
        HorizontalLayout h01 = new HorizontalLayout(gridCashKpKw, formKpKw);
        v01.add(butAddNewKpKw, h01);

        add(h00, v01);

        gridCashKpKw.asSingleSelect().addValueChangeListener(event ->
                formKpKw.setDocument(gridCashKpKw.asSingleSelect().getValue()));
    }

    public void openKpKwView(Document item){
        gridCashKpKw.setItems();
        this.cashReportItem = item;

        if (this.cashReportItem.getDocApproved().equals("T"))
            butAddNewKpKw.setEnabled(false);
        else
            butAddNewKpKw.setEnabled(true);
        labCash.setText(cashCode);
        formKpKw.setDocument(null);
        updateList(0);
    }

    public void updateList(int index) {
        listDocKpKw = documentService.getAllCashKpKw(cashReportItem.getDocId(), cashReportItem.getDocFrmId());
        if (listDocKpKw.isPresent()) {
            //this.listDocKpKw.get().sort(Comparator.comparing(Document::getDocNo)); //order by asc
            gridCashKpKw.setItems(listDocKpKw.get());
            formKpKw.setDocument(listDocKpKw.get().get(index));
        }
    }


}
