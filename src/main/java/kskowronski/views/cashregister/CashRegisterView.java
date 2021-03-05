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
import kskowronski.data.entity.egeria.kg.CashRegisterDTO;
import kskowronski.data.entity.egeria.kg.Document;
import kskowronski.data.service.egeria.kg.CashRegisterService;
import kskowronski.data.service.egeria.kg.DocumentService;
import kskowronski.views.cashregister.elements.CashReportsView;
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

    private transient CashRegisterService  cashRegisterService;
    private transient DocumentService documentService;

    private Grid<CashRegisterDTO> gridCashRegisters;

    private transient List<CashRegisterDTO> cashRegisters;
    private transient List<Document> reports;

    CashReportsView cashReportsView;

    @Autowired
    public CashRegisterView(CashRegisterService cashRegisterService, DocumentService documentService, CashReportsView cashReportsView) {
        this.cashRegisterService = cashRegisterService;
        this.documentService = documentService;
        this.cashReportsView = cashReportsView;
        setId("cash-register-view");
        setHeight("95%");

        HorizontalLayout h01 = new HorizontalLayout();
        h01.setClassName("h01");

        VerticalLayout vlReports = new VerticalLayout();
        vlReports.setClassName("vlReports");


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
        gridCashRegisters.addItemClickListener( event -> getCashReports(event.getItem().getCasId(), event.getItem().getCasFrmId()) );

        //second grid
        vlReports = cashReportsView.openReports();

        h01.add(gridCashRegisters, vlReports);
        add(h01);
        getCashRegisters();
    }

    private void getCashRegisters(){
        cashRegisters = cashRegisterService.getAllCashRegister();
        gridCashRegisters.setItems(cashRegisters);
    }

    private void getCashReports(BigDecimal casId, BigDecimal frmId){
        Optional<List<Document>> reportsDB = documentService.getAllCashReports(casId, frmId);
        if ( reportsDB.isPresent() ){
            reports = reportsDB.get();
            cashReportsView.setItems(reports, casId, frmId);
        } else {
            Notification.show("Brak raprotów dla tej kasy", 3000, Notification.Position.MIDDLE);
        }
    }


}
