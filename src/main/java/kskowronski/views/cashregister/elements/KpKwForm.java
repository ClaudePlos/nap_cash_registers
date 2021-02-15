package kskowronski.views.cashregister.elements;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import kskowronski.data.entity.egeria.Document;
import kskowronski.data.entity.egeria.KpKwType;


public class KpKwForm extends FormLayout {
    private TextField docOwnNumber = new TextField("Numer");
    private ComboBox<KpKwType> docRdocCode = new ComboBox<>("Kod rodzaju");
    private DatePicker docFrom = new DatePicker("Data wyst.");

    private Button save = new Button("Save");
    private Binder<Document> binder = new Binder<>(Document.class);
    private CashKpKwView cashKpKwView;

    public KpKwForm(CashKpKwView cashKpKwView) {
        this.cashKpKwView = cashKpKwView;
        docRdocCode.setItems(KpKwType.values());

        HorizontalLayout buttons = new HorizontalLayout(save);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(docOwnNumber, docRdocCode, docFrom, buttons);

        binder.bindInstanceFields(this);

        save.addClickListener(event -> save());
    }



    public void setDocument(Document doc) {
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
        setDocument(null);
    }
}
