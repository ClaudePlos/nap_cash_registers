package kskowronski.views.cashregister.elements;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import kskowronski.data.entity.egeria.Document;
import kskowronski.data.entity.egeria.KpKwType;

import java.math.BigDecimal;


public class KpKwForm extends FormLayout {
    private Binder<Document> binder = new Binder<>(Document.class);
    private TextField docOwnNumber = new TextField("Numer");
    private ComboBox<KpKwType> docRdocCode = new ComboBox<>("Kod rodzaju");
    private DatePicker docFrom = new DatePicker("Data wyst.");
    private BigDecimalField docAmount = new BigDecimalField("Kwota");

    private Button save = new Button("Save");
    private CashKpKwView cashKpKwView;

    public KpKwForm(CashKpKwView cashKpKwView) {
        this.cashKpKwView = cashKpKwView;
        docOwnNumber.setEnabled(false);
        docRdocCode.setItems(KpKwType.values());
        editableElements(false);
        //docAmount.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);

        HorizontalLayout buttons = new HorizontalLayout(save);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(docOwnNumber, docRdocCode, docFrom, docAmount, buttons);

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
            if (doc.getDocApproved().equals("N")){
                editableElements(true);
            } else {
                editableElements(false);
            }
        }

    }

    private void save() {
        Document doc = binder.getBean();
        cashKpKwView.documentService.save(doc);
        cashKpKwView.updateList();
        setDocument(null);
    }

    public void editableElements(Boolean status){
        docRdocCode.setEnabled(status);
        docFrom.setEnabled(status);
        docAmount.setEnabled(status);
    }
}
