package kskowronski.views.components;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entity.egeria.ek.Worker;
import kskowronski.data.service.egeria.ek.WorkerService;
import kskowronski.views.cashregister.elements.kpkw.KpKwForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@UIScope
@CssImport("./styles/views/components/worker-dialog.css")
public class WorkerDialog extends Dialog {

    private transient WorkerService workerService;

    private Grid<Worker> gridUsers;

    private Html text = new Html("<div> Proszę wyszukać pracownika.</div>");
    private TextField filterText = new TextField();

    @Autowired
    public WorkerDialog(WorkerService workerService, KpKwForm kpKwForm) {
        setWidth("900px");
        this.workerService = workerService;
        filterText.setPlaceholder("Search...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        gridUsers = new Grid<>(Worker.class);
        gridUsers.setClassName("gridClient");
        gridUsers.setColumns();
        gridUsers.addColumn("prcNumer").setWidth("50px");
        gridUsers.addColumn("prcNazwisko").setWidth("150px");
        gridUsers.addColumn("prcImie").setWidth("150px");


    }

    private void updateList() {

    }
}
