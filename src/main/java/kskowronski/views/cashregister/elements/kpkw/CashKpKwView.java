package kskowronski.views.cashregister.elements.kpkw;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entity.egeria.Document;
import kskowronski.data.service.egeria.DocumentService;
import kskowronski.data.service.egeria.ckk.ClientService;
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

    static Logger logger = Logger.getLogger(CashKpKwView.class.getName());

    private Grid<Document> gridCashKpKw;

    public transient DocumentService documentService;

    private transient Optional<List<Document>> listDocKpKw;
    private transient Document cashReportItem;

    private TextField filterText = new TextField();
    private Button butAddNewKpKw = new Button("Dodaj KP/KW");
    private KpKwForm formKpKw;

    @Autowired
    public CashKpKwView(DocumentService documentService, ClientService clientService) {
        logger.log(Level.INFO, "Constructor CashKpKwView");
        this.documentService = documentService;
        this.formKpKw = new KpKwForm(this, clientService);
        setWidth("800px");
        setHeight("600px");
        filterText.setPlaceholder("Search..");

        butAddNewKpKw.addClickListener( e -> {
            Optional<Document> newKpKw = documentService.insertKpKw(cashReportItem.getDocId(), cashReportItem.getDocFrmId());
            if (newKpKw.isPresent()){
                listDocKpKw.get().add(newKpKw.get());
                gridCashKpKw.getDataProvider().refreshAll();
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
        v01.add(filterText, butAddNewKpKw, h01);

        add(v01);


        gridCashKpKw.asSingleSelect().addValueChangeListener(event ->
                formKpKw.setDocument(gridCashKpKw.asSingleSelect().getValue()));
    }

    public void openKpKw(Document item){
        gridCashKpKw.setItems();
        this.cashReportItem = item;
        formKpKw.setDocument(null);
        updateList();
    }

    public void updateList() {
        listDocKpKw = documentService.getAllCashKpKw(cashReportItem.getDocId(), cashReportItem.getDocFrmId());
        if (listDocKpKw.isPresent()) {
            this.listDocKpKw.get().sort(Comparator.comparing(Document::getDocNo)); //order by asc
            gridCashKpKw.setItems(listDocKpKw.get());
            formKpKw.setDocument(listDocKpKw.get().get(0));
        }
    }

    public void updateDocNOwnNumber(String docOwnNumber){
        this.cashReportItem.setDocOwnNumber(docOwnNumber);
    }


}
