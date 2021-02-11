package kskowronski.views.cashregister.elements;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import kskowronski.data.entity.egeria.Document;


public class KpKwForm extends FormLayout {
    private TextField docRdocCode = new TextField("Kod rodzaju");
    private DatePicker docFrom = new DatePicker("Data wyst.");

    private Button save = new Button("Save");
    private Binder<Document> binder = new Binder<>(Document.class);
    private CashKpKwView cashKpKwView;

    public KpKwForm(CashKpKwView cashKpKwView) {
        this.cashKpKwView = cashKpKwView;

        HorizontalLayout buttons = new HorizontalLayout(save);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(docRdocCode, docFrom, buttons);

        binder.bindInstanceFields(this);

        save.addClickListener(event -> save());
    }



    public void setCustomer(Document doc) {
        binder.setBean(doc);

        if (doc == null) {
            setVisible(false);
        } else {
            setVisible(true);
            docRdocCode.focus();
        }
    }

    private void save() {
        Document doc = binder.getBean();
        cashKpKwView.documentService.save(doc);
        cashKpKwView.updateList();
        setCustomer(null);
    }
}
