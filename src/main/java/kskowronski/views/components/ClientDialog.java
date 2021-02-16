package kskowronski.views.components;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entity.egeria.ckk.Client;
import kskowronski.data.service.egeria.ckk.ClientService;
import kskowronski.views.cashregister.elements.KpKwForm;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@UIScope
@CssImport("./styles/views/cashregister/cash-kpkw-view.css")
public class ClientDialog extends Dialog {

    private transient ClientService clientService;

    private Grid<Client> gridClient;

    private Html text = new Html("<div> Proszę wyszukać klienta.</div>");
    private TextField filterText = new TextField();

    public ClientDialog(ClientService clientService, KpKwForm kpKwForm) {
        setWidth("900px");
        this.clientService = clientService;
        filterText.setPlaceholder("Search...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        gridClient = new Grid<>(Client.class);
        gridClient.setClassName("gridClient");
        gridClient.setColumns();
        gridClient.addColumn("klKod").setWidth("30px");
        gridClient.addColumn(TemplateRenderer.<Client> of(
                "<div class=\"gridClient\">[[item.client]]</div>")
                .withProperty("client", Client::getKldNazwa))
                .setHeader("Klient").setWidth("300px");
        gridClient.addColumn("kldCity").setWidth("100px");
        gridClient.addColumn("kldNip");
        gridClient.addColumn("kldRegon");
        gridClient.addItemDoubleClickListener( e -> {
            kpKwForm.setClient(e.getItem());
            this.close();
        });

        VerticalLayout v01 = new VerticalLayout(text, filterText, gridClient);
        add(v01);
    }

    private void updateList() {
        if (filterText.getValue().length() > 2) {
            Optional<List<Client>> clients = clientService.findFastClient(filterText.getValue());
            if ( clients.isPresent()){
                gridClient.setItems(clients.get());
            } else {
                gridClient.setItems();
            }
        } else {
            gridClient.setItems();
        }
    }
}
