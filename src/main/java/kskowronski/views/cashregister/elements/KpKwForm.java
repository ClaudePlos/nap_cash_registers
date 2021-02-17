package kskowronski.views.cashregister.elements;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entity.egeria.Document;
import kskowronski.data.entity.egeria.KpKwType;
import kskowronski.data.entity.egeria.ckk.Client;
import kskowronski.data.service.egeria.ckk.ClientService;
import kskowronski.views.components.ClientDialog;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@UIScope
public class KpKwForm extends FormLayout {

    private String txtFindClient = "Znajdź Klienta";
    private String txtClient = "Klient";

    private Binder<Document> binder = new Binder<>(Document.class);
    private TextField docOwnNumber = new TextField("Numer");
    private ComboBox<KpKwType> docRdocCode = new ComboBox<>("Kod rodzaju");
    private DatePicker docFrom = new DatePicker("Data wyst.");
    private BigDecimalField docAmount = new BigDecimalField("Kwota");
    private BigDecimalField docKlKodPod = new BigDecimalField("Kod Klienta");
    private Button butFindClient = new Button(txtFindClient);
    private Label labCompanyName = new Label();

    private Button save = new Button("Save");
    private Button butAccept = new Button("Zatwierdź Dokument");
    private Button butClose = new Button("Zamknij");
    private CashKpKwView cashKpKwView;



    public KpKwForm(CashKpKwView cashKpKwView, ClientService clientService) {
        this.cashKpKwView = cashKpKwView;
        docOwnNumber.setEnabled(false);
        docKlKodPod.setEnabled(false);
        docKlKodPod.setWidth("100px");
        docRdocCode.setItems(KpKwType.values());
        editableElements(false);
        //docAmount.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);

        butFindClient.addClickListener( e ->{
            if (butFindClient.getText().equals(txtFindClient)){
                ClientDialog clientDialog = new ClientDialog(clientService, this);
                clientDialog.open();
            } else {
                if (docKlKodPod.getValue() != null ){
                    Optional<Client> client = clientService.getClientByKlKod(docKlKodPod.getValue());
                    if ( client.isPresent()){
                        Notification.show(client.get().getKldNazwa() + " " + client.get().getKldCity() + " " + client.get().getKldNip()
                                ,3000,  Notification.Position.MIDDLE);
                    }
                } else {
                    Notification.show("Dokument nie ma kodu klienta",3000,  Notification.Position.MIDDLE);
                }
            }
        });

        butClose.addClickListener( e -> cashKpKwView.close());

        HorizontalLayout buttons = new HorizontalLayout(save, butAccept, butClose);
        HorizontalLayout clientDiv = new HorizontalLayout(butFindClient, butFindClient, labCompanyName);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(docOwnNumber, docRdocCode, docFrom, docAmount, docKlKodPod, clientDiv, buttons);

        binder.bindInstanceFields(this);

        save.addClickListener(event -> save());
    }



    public void setDocument(Document doc) {
        binder.setBean(doc);
        labCompanyName.setText("");

        if (doc == null) {
            setVisible(false);
        } else {
            setVisible(true);
            docRdocCode.focus();
            if (doc.getDocApproved().equals("N")){
                editableElements(true);
                butFindClient.setText(txtFindClient);
            } else {
                editableElements(false);
                butFindClient.setText(txtClient);
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
        save.setEnabled(status);
        butAccept.setEnabled(status);
    }

    public void setClient(Client client){
        docKlKodPod.setValue(client.getKlKod());
        labCompanyName.setText(client.getKldNazwa());
    }
}
