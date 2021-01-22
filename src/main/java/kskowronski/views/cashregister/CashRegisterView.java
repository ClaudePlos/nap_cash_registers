package kskowronski.views.cashregister;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import kskowronski.data.entity.egeria.CashRegister;
import kskowronski.data.entity.egeria.CashRegisterDTO;
import kskowronski.data.entity.egeria.Document;
import kskowronski.data.service.egeria.CashRegisterService;
import kskowronski.data.service.egeria.DocumentService;
import kskowronski.views.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Route(value = "cashregister", layout = MainView.class)
@PageTitle("Cash Register")
@CssImport("./styles/views/cashregister/cash-register-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class CashRegisterView extends HorizontalLayout {

    private CashRegisterService cashRegisterService;
    private DocumentService documentService;

    private Grid<CashRegisterDTO> gridCashRegisters;
    private Grid<Document> gridCashReports;

    @Autowired
    public CashRegisterView(CashRegisterService cashRegisterService, DocumentService documentService) {
        this.cashRegisterService = cashRegisterService;
        this.documentService = documentService;
        setId("cash-register-view");
        setHeight("95%");

        HorizontalLayout h01 = new HorizontalLayout();
        h01.setClassName("h01");

        //first grid
        this.gridCashRegisters = new Grid<>(CashRegisterDTO.class);
        gridCashRegisters.setClassName("gridCashRegisters");
        gridCashRegisters.setColumns();
        gridCashRegisters.addColumn(TemplateRenderer.<CashRegisterDTO>of(
                "<div title='[[item.casDesc]] Firma:[[item.frmName]]'>[[item.casName]]</div>")
                .withProperty("frmName", CashRegisterDTO::getFrmName)
                .withProperty("casName", CashRegisterDTO::getCasName)
                .withProperty("casDesc", CashRegisterDTO::getCasDesc)
                ).setHeader("KASA");
        gridCashRegisters.addItemClickListener( event -> {
             getCashReports(event.getItem().getCasId(), event.getItem().getCasFrmId());
        });


        //second grid
        this.gridCashReports = new Grid<>(Document.class);
        gridCashReports.setClassName("gridCashReports");
        gridCashReports.setColumns();
        gridCashReports.addColumn("docNo");
        gridCashReports.addColumn("docOwnNumber");

        gridCashReports.addColumn("docInitialState");
        gridCashReports.addColumn("docWn");
        gridCashReports.addColumn("docMa");


        h01.add(gridCashRegisters,gridCashReports);
        add(h01);
        getCashRegisters();
    }

    private void getCashRegisters(){
        List<CashRegisterDTO> cashRegisters = cashRegisterService.getAllCashRegister();
        gridCashRegisters.setItems(cashRegisters);
    }

    private void getCashReports(BigDecimal casId, BigDecimal frmId){
        gridCashReports.setItems();
        Optional<List<Document>> reports = documentService.getAllCashReports(casId, frmId);
        if ( reports.get().size() == 0 ){
            Notification.show("Brak raprotów dla tej kasy", 3000, Notification.Position.MIDDLE);
        }
        gridCashReports.setItems(reports.get());
    }
}
