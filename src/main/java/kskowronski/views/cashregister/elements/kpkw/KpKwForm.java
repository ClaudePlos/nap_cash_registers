package kskowronski.views.cashregister.elements.kpkw;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entities.egeria.kg.Document;
import kskowronski.data.entities.egeria.kg.KpKwType;
import kskowronski.data.entities.egeria.ckk.Client;
import kskowronski.data.entities.egeria.ek.Worker;
import kskowronski.data.entities.egeria.kg.TransactionType;
import kskowronski.data.services.egeria.ckk.ClientService;
import kskowronski.data.services.egeria.ek.WorkerService;
import kskowronski.views.components.ClientDialog;
import kskowronski.views.components.MyNotification;
import kskowronski.views.components.WorkerDialog;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@UIScope
@CssImport("./styles/views/cashregister/elements/kpkw-form.css")
public class KpKwForm extends FormLayout {

    private String txtIncome = "Utarg";
    private String txtBank = "Bank";
    private String txtCashInvoice = "Faktura gotówkowa";
    private String txtTransfer = "Przekaz";
    private String txtCommission = "Prowizja";

    private String txtFindClient = "Znajdź Klienta";
    private String txtClient = "Klient";

    private String txtFindWorker = "Znajdź Pracownika";
    private String txtWorker = "Zaliczka dla";

    private Binder<Document> binder = new Binder<>(Document.class);
    private TextField docOwnNumber = new TextField("Numer");
    private ComboBox<KpKwType> docRdocCode = new ComboBox<>("Kod rodzaju");
    private DatePicker docDateFrom = new DatePicker("Data wyst.");
    private BigDecimalField docAmount = new BigDecimalField("Kwota");
    private BigDecimalField docPrcIdPod = new BigDecimalField("Numer Pracownika");
    private Button butFindWorker = new Button(txtFindWorker);
    private BigDecimalField docKlKodPod = new BigDecimalField("Kod Klienta");
    private Button butFindClient = new Button(txtFindClient);
    private Label labCompanyName = new Label();
    private Label labWorkerName = new Label();

    private Button save = new Button("Zapisz");
    private Button butAccept = new Button("Zatwierdź Dokument");
    private Button butClose = new Button("Zamknij");
    private CashKpKwView cashKpKwView;
    private RadioButtonGroup<String> radioWorkerClient = new RadioButtonGroup<>();

    private TextField docDef0 = new TextField("Dodatkowe Info.");
    private TextField docDef1 = new TextField("Transfer");

    private HorizontalLayout divIncome = new HorizontalLayout();
    private HorizontalLayout divBank = new HorizontalLayout();
    private HorizontalLayout divCashInvoice = new HorizontalLayout();
    private HorizontalLayout divTransfer = new HorizontalLayout();
    private HorizontalLayout divCommission = new HorizontalLayout();
    private HorizontalLayout divWorker =  new HorizontalLayout();
    private HorizontalLayout divClient = new HorizontalLayout();

    public KpKwForm(CashKpKwView cashKpKwView, ClientService clientService, WorkerService workerService) {
        this.cashKpKwView = cashKpKwView;
        docOwnNumber.setEnabled(false);
        docKlKodPod.setEnabled(false);
        docPrcIdPod.setEnabled(false);
        docDef0.setEnabled(false);
        docDef1.setEnabled(false);
        docKlKodPod.setWidth("100px");
        docRdocCode.setItems(KpKwType.KP, KpKwType.KW);
        docRdocCode.setClassName("docRdocCode");
        editableElements(false);

        butFindWorker.addClickListener( e ->{
            if (butFindWorker.getText().equals(txtFindWorker)){
                WorkerDialog workerDialog = new WorkerDialog(workerService, this);
                workerDialog.open();
            } else {
                if (docPrcIdPod.getValue() != null ){
                    Optional<Worker> worker = workerService.findById(docPrcIdPod.getValue());
                    if ( worker.isPresent()){
                        Notification.show(worker.get().getNazwImie(),3000,  Notification.Position.MIDDLE);
                    }
                } else {
                    MyNotification.openAlert("Dokument nie ma numeru pracownika",3000,  Notification.Position.MIDDLE);
                }
            }

        });

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
                    MyNotification.openAlert("Dokument nie ma kodu klienta",3000,  Notification.Position.MIDDLE);
                }
            }
        });

        butAccept.addClickListener( e -> {
            Optional<Document> document = cashKpKwView.documentService.acceptKpKw(binder.getBean().getDocId(), binder.getBean().getDocDocIdZap(), binder.getBean().getDocFrmId());
            if ( document.isPresent()){
                setDocument(document.get());
                cashKpKwView.updateList(document.get().getDocNo().intValue()-1);
                Notification.show("Zatwierdzono",1000, Notification.Position.MIDDLE);
            }
        });

        butClose.addClickListener( e -> cashKpKwView.close());

        HorizontalLayout buttons = new HorizontalLayout(save, butAccept, butClose);
        buttons.setClassName("buttonsFooter");

        HorizontalLayout divTypeAndDate = new HorizontalLayout(docRdocCode, docDateFrom);
        divTypeAndDate.setClassName("divTypeAndDate");

        divIncome.setVisible(true);
        divIncome.setClassName("divIncome");

        divBank.setVisible(false);
        divBank.setClassName("divBank");

        divCashInvoice.setVisible(false);
        divCashInvoice.setClassName("divCashInvoice");

        divTransfer.setVisible(false);
        divTransfer.setClassName("divTransfer");

        divCommission.setVisible(false);
        divCommission.setClassName("divCommission");

        divClient.add(docKlKodPod, butFindClient);
        divClient.setClassName("divClient");
        divClient.setVisible(false);

        divWorker.add(docPrcIdPod, butFindWorker);
        divWorker.setClassName("divWorker");
        divWorker.setVisible(false);

        HorizontalLayout divAccount =  new HorizontalLayout(docDef0, docDef1);
        divAccount.setClassName("divAccount");

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        radioWorkerClient.setLabel("Transakcja:");
        radioWorkerClient.setItems(txtIncome, txtBank, txtCashInvoice, txtTransfer, txtCommission, txtWorker, txtClient);
        radioWorkerClient.setValue(txtIncome);
        radioWorkerClient.addValueChangeListener(event -> onChangeTransaction(mapperTransaction(radioWorkerClient.getValue())));

        add(docOwnNumber, divTypeAndDate, docAmount, radioWorkerClient
                , divIncome, divBank, divCashInvoice, divTransfer, divCommission, divWorker, divClient
                , labCompanyName, labWorkerName
                , divAccount
                , buttons);

        binder.bindInstanceFields(this);

        save.addClickListener(event -> save());
    }



    public void setDocument(Document doc) {
        binder.setBean(doc);
        labCompanyName.setText("");
        labWorkerName.setText("");

        if (doc == null) {
            setVisible(false);
        } else {
            setVisible(true);
            docRdocCode.focus();
            if (doc.getDocApproved().equals("N")){
                editableElements(true);
                butFindClient.setText(txtFindClient);
                butFindWorker.setText(txtFindWorker);
            } else {
                editableElements(false);
                butFindClient.setText(txtClient);
                butFindWorker.setText(txtWorker);
            }

            onChangeTransaction(doc.getDocDef1());

            //TODO
            if (doc.getDocPrcIdPod() != null)
                radioWorkerClient.setValue(txtWorker);

            if (doc.getDocKlKodPod() != null)
                radioWorkerClient.setValue(txtClient);

        }

    }

    private void save() {
        Document doc = binder.getBean();
        if (doc.getDocDateFrom() == null){
            MyNotification.openAlert("Brak daty wystawienia. Wypełnij.", 2000, Notification.Position.MIDDLE);
            return;
        }
        Optional<Document> docReturned = cashKpKwView.documentService.updateKpKw(doc);
        cashKpKwView.updateList(doc.getDocNo().intValue()-1);
        if (docReturned.isPresent()){
            setDocument(docReturned.get());
        } else {
            setDocument(doc);
        }
        Notification.show("Zapisano", 1000, Notification.Position.MIDDLE);
    }

    public void editableElements(Boolean status){
        radioWorkerClient.setEnabled(status);
        docRdocCode.setEnabled(status);
        docDateFrom.setEnabled(status);
        docAmount.setEnabled(status);
        save.setEnabled(status);
        butAccept.setEnabled(status);
    }

    public void setClient(Client client){
        docKlKodPod.setValue(client.getKlKod());
        labCompanyName.setText(client.getKldNazwa());
    }

    public void setWorker(Worker worker){
        docPrcIdPod.setValue(worker.getPrcId());
        labWorkerName.setText(worker.getNazwImie());
    }

    private void onChangeTransaction(String transaction){
        //Div transaction update
        divIncome.setVisible( checkTransaction(transaction, TransactionType.INCOME.name()) );
        divBank.setVisible( checkTransaction(transaction, TransactionType.BANK.name()) );
        divCashInvoice.setVisible( checkTransaction(transaction, TransactionType.CASH_INVOICE.name()) );
        divTransfer.setVisible( checkTransaction(transaction, TransactionType.TRANSFER.name()) );
        divCommission.setVisible( checkTransaction(transaction, TransactionType.COMMISSION.name()) );
        divWorker.setVisible( checkTransaction(transaction, TransactionType.CASH_ADVANCE.name()) );
        divClient.setVisible( checkTransaction(transaction, TransactionType.CLIENT.name()) );
    }

    private String mapperTransaction(String radioButtonName){
         String transaction =
               checkTransaction(radioButtonName,"Utarg") ? TransactionType.INCOME.name() :
                checkTransaction(radioButtonName,"Bank") ? TransactionType.BANK.name() :
                 checkTransaction(radioButtonName,"Faktura gotówkowa") ? TransactionType.CASH_INVOICE.name() :
                  checkTransaction(radioButtonName,"Przekaz") ? TransactionType.TRANSFER.name() :
                   checkTransaction(radioButtonName,"Prowizja") ? TransactionType.COMMISSION.name() :
                    checkTransaction(radioButtonName,"Zaliczka dla") ? TransactionType.CASH_ADVANCE.name() :
                     checkTransaction(radioButtonName,"Klient") ? TransactionType.CLIENT.name() : null;
        docDef1.setValue(transaction);
        return transaction;
    }

    private Boolean checkTransaction(String t1, String t2){
        return t1.equals(t2);
    }
}