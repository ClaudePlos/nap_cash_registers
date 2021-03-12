package kskowronski.views.cashregister.elements.kpkw;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entities.egeria.kg.Document;
import kskowronski.data.services.egeria.kg.DocumentService;
import kskowronski.data.services.egeria.ckk.ClientService;
import kskowronski.data.services.egeria.ek.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@UIScope
@CssImport("./styles/views/cashregister/cash-kpkw-view.css")
public class CashKpKwView extends Dialog {
    Html title = new Html("<div>KP/KW</div>");
    static Logger logger = Logger.getLogger(CashKpKwView.class.getName());

    private Grid<Document> gridCashKpKw;

    public transient DocumentService documentService;

    private transient Optional<List<Document>> listDocKpKw;
    private transient Document cashReportItem;

    private Button butAddNewKpKw = new Button("Dodaj KP/KW");
    private KpKwForm formKpKw;
    public String cashCode;

    @Autowired
    public CashKpKwView(DocumentService documentService, ClientService clientService, WorkerService workerService) {
        logger.log(Level.INFO, "Constructor CashKpKwView");
        this.documentService = documentService;
        this.formKpKw = new KpKwForm(this, clientService, workerService);
        setWidth("750px");
        setHeight("630px");

        butAddNewKpKw.setEnabled(false);
        butAddNewKpKw.addClickListener( e -> {
            Optional<Document> newKpKw = documentService.insertKpKw(cashReportItem.getDocId(), cashReportItem.getDocFrmId());
            if (newKpKw.isPresent() && listDocKpKw.isPresent()){
                listDocKpKw.get().add(newKpKw.get());
                gridCashKpKw.getDataProvider().refreshAll();
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
        HorizontalLayout h01 = new HorizontalLayout(gridCashKpKw, formKpKw);
        v01.add(butAddNewKpKw, h01);

        add(title, v01);


        gridCashKpKw.asSingleSelect().addValueChangeListener(event ->
                formKpKw.setDocument(gridCashKpKw.asSingleSelect().getValue()));
    }

    public void openKpKw(Document item){
        gridCashKpKw.setItems();
        this.cashReportItem = item;

        if (this.cashReportItem.getDocApproved().equals("T"))
            butAddNewKpKw.setEnabled(false);
        else
            butAddNewKpKw.setEnabled(true);

        formKpKw.setDocument(null);
        updateList(0);
    }

    public void updateList(int index) {
        listDocKpKw = documentService.getAllCashKpKw(cashReportItem.getDocId(), cashReportItem.getDocFrmId());
        if (listDocKpKw.isPresent()) {
            this.listDocKpKw.get().sort(Comparator.comparing(Document::getDocNo)); //order by asc
            gridCashKpKw.setItems(listDocKpKw.get());
            formKpKw.setDocument(listDocKpKw.get().get(index));
        }
    }


}
