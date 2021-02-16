package kskowronski.views.components;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entity.egeria.ckk.Client;
import kskowronski.data.service.egeria.ckk.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@UIScope
public class ClientDialog extends Dialog {

    private transient ClientService clientService;

    private Grid<Client> gridClient;

    private Html text = new Html("<div> Proszę wyszukać klienta.</div>");
    private TextField filterText = new TextField();

    public ClientDialog(ClientService clientService) {
        setWidth("600px");
        this.clientService = clientService;
        filterText.setPlaceholder("Search...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        gridClient = new Grid<>(Client.class);
        gridClient.setClassName("gridClient");
        gridClient.setColumns("klKod","kldNazwa","kldCity","kldNip","kldRegon");

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
