package kskowronski.data.services.egeria.kg;

import com.vaadin.flow.component.notification.Notification;
import kskowronski.data.entities.egeria.kg.Document;
import kskowronski.data.services.global.ConsolidationService;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService extends CrudService<Document, BigDecimal> {

    @PersistenceContext
    private EntityManager em;

    private DocumentRepo repo;

    public DocumentService(@Autowired DocumentRepo repo) {
        this.repo = repo;
    }

    @Override
    protected DocumentRepo getRepository() {
        return repo;
    }

    @Autowired
    ConsolidationService consolidationService;

    public List<Document> findAll(){
        return repo.findAll();
    }

    public Optional<List<Document>> getAllCashReports(BigDecimal casId, BigDecimal frmId){
        consolidationService.setConsolidateCompany();
        return this.repo.findByDocCasIdAndFrm(casId, frmId);
    }

    public List<Document> getAllCashKpKw(BigDecimal docId, BigDecimal frmId){
        consolidationService.setConsolidateCompany();
        Optional<List<Document>> lKpKw = this.repo.findByDocKpKwAndFrm(docId, frmId);
        if (lKpKw.isPresent()){
            return lKpKw.get();
        }
        return new ArrayList<>();
    }

    public List<Document> getAllCashKpKwWithoutIncomeCard(BigDecimal docId, BigDecimal frmId){
        consolidationService.setConsolidateCompany();
        Optional<List<Document>> lKpKw = this.repo.findByDocKpKwAndFrmWithoutIncomeCard(docId, frmId);
        if (lKpKw.isPresent()){
            return lKpKw.get();
        }
        return new ArrayList<>();
    }

    public List<Document> getAllCashKpKwOnlyIncomeCard(BigDecimal docId, BigDecimal frmId){
        consolidationService.setConsolidateCompany();
        Optional<List<Document>> lKpKw = this.repo.findByDocKpKwAndFrmOnlyIncomeCard(docId, frmId);
        if (lKpKw.isPresent()){
            return lKpKw.get();
        }
        return new ArrayList<>();
    }

    public Optional<Document> addNewCashReport(BigDecimal casId, BigDecimal frmId, BigDecimal lp, LocalDate from, LocalDate to, BigDecimal initialValue){
        Session session = em.unwrap( Session.class );
        Integer docId = null;
        try{
            docId = session.doReturningWork(
                connection -> {
                    try (CallableStatement function = connection
                            .prepareCall(
                                    "{ ? = call NAPRZOD2.NPP_CASH_REPORTS.fn_add_cash_reports(?,?,?,?,?,?) }" )) {
                        function.registerOutParameter( 1, Types.INTEGER );
                        function.setBigDecimal( 2, casId );
                        function.setBigDecimal( 3, frmId );
                        function.setBigDecimal( 4, lp );
                        function.setDate( 5, java.sql.Date.valueOf(from));
                        function.setDate( 6, java.sql.Date.valueOf(to));
                        function.setBigDecimal( 7, initialValue );
                        function.execute();
                        return function.getInt( 1 );
                    }
                });
        } catch (JDBCException ex){
            Notification.show(ex.getSQLException().getMessage(),5000, Notification.Position.MIDDLE);
        }
        return repo.findById(BigDecimal.valueOf(docId));
    }

    public Optional<Document> insertKpKw(BigDecimal CashReportDocId, BigDecimal frmId){
        Session session = em.unwrap( Session.class );
        Integer docId = null;
        try {
            docId = session.doReturningWork(
                    connection -> {
                        try (CallableStatement function = connection
                                .prepareCall(
                                        "{ ? = call NAPRZOD2.NPP_CASH_REPORTS.fn_insert_kpkw(?,?) }")) {
                            function.registerOutParameter(1, Types.INTEGER);
                            function.setBigDecimal(2, CashReportDocId);
                            function.setBigDecimal(3, frmId);
                            function.execute();
                            return function.getInt(1);
                        }
                    });
        } catch (JDBCException ex){
            Notification.show(ex.getSQLException().getMessage(),5000, Notification.Position.MIDDLE);
        }
        return repo.findById(BigDecimal.valueOf(docId));
    }


    public Optional<Document> updateKpKw(Document document){
        Session session = em.unwrap( Session.class );
        try {
            Integer docId = session.doReturningWork(
                    connection -> {
                        try (CallableStatement function = connection
                                .prepareCall(
                                        "{ ? = call NAPRZOD2.NPP_CASH_REPORTS.fn_update_kpkw(?,?,?,?,?,?,?,?,?,?,?,?,?) }")) {
                            function.registerOutParameter(1, Types.INTEGER);
                            function.setBigDecimal(2, document.getDocId());
                            function.setString(3, document.getDocRdocCode().toString());
                            function.setDate(4, java.sql.Date.valueOf(document.getDocDateFrom()));
                            function.setBigDecimal(5, document.getDocAmount());
                            function.setBigDecimal(6, document.getDocKlKodPod());
                            function.setBigDecimal(7, document.getDocPrcIdPod());
                            function.setString(8, document.getDocDef0());
                            function.setString(9, document.getDocDef1());
                            function.setString(10, document.getDocDef2());
                            function.setString(11, document.getDocDef3());
                            function.setString(12, document.getDocSettlement());
                            function.setBigDecimal(13, document.getDocFrmId());
                            function.setString(14, document.getDocDescription());
                            function.execute();
                            return function.getInt(1);
                        }
                    });
        } catch (JDBCException ex){
            Notification.show(ex.getSQLException().getMessage(),5000, Notification.Position.MIDDLE);
        }
        return repo.findById(document.getDocId());
    }

    public Optional<Document> acceptKpKw(BigDecimal docId, BigDecimal casRapId, BigDecimal docFrmId){
        Optional<Document> docs =  acceptDocument(docId, casRapId, docFrmId);
        return docs;
    }

    public Optional<Document> unAcceptKpKw(BigDecimal docId, BigDecimal casRapId, BigDecimal docFrmId){
        Optional<Document> docs =  unAcceptDocument(docId, casRapId, docFrmId);
        return docs;
    }

    public String deleteKpKw(BigDecimal docId, BigDecimal casRapId, BigDecimal docFrmId){
        String ret =  deleteDocument(docId, casRapId, docFrmId);
        return ret;
    }

    public Optional<Document> acceptDocument(BigDecimal docId, BigDecimal casRapId,BigDecimal docFrmId){
        Session session = em.unwrap( Session.class );
        try {
            session.doReturningWork(
                    connection -> {
                        try (CallableStatement function = connection
                                .prepareCall(
                                        "{ ? = call NAPRZOD2.NPP_CASH_REPORTS.fn_accept_document(?,?,?) }")) {
                            function.registerOutParameter(1, Types.INTEGER);
                            function.setBigDecimal(2, docId);
                            function.setBigDecimal(3, casRapId);
                            function.setBigDecimal(4, docFrmId);
                            function.execute();
                            return function.getInt(1);
                        }
                    });
        } catch (JDBCException ex){
            Notification.show(ex.getSQLException().getMessage(),5000, Notification.Position.MIDDLE);
            return Optional.empty();
        }
        return repo.findById(docId);
    }

    private Optional<Document> unAcceptDocument(BigDecimal docId, BigDecimal casRapId,BigDecimal docFrmId){
        Session session = em.unwrap( Session.class );
        try {
            session.doReturningWork(
                    connection -> {
                        try (CallableStatement function = connection
                                .prepareCall(
                                        "{ ? = call NAPRZOD2.NPP_CASH_REPORTS.fn_unaccept_document(?,?,?) }")) {
                            function.registerOutParameter(1, Types.INTEGER);
                            function.setBigDecimal(2, docId);
                            function.setBigDecimal(3, casRapId);
                            function.setBigDecimal(4, docFrmId);
                            function.execute();
                            return function.getInt(1);
                        }
                    });
        } catch (JDBCException ex){
            Notification.show(ex.getSQLException().getMessage(),5000, Notification.Position.MIDDLE);
            return Optional.empty();
        }
        return repo.findById(docId);
    }

    private String deleteDocument(BigDecimal docId, BigDecimal casRapId,BigDecimal docFrmId){
        Session session = em.unwrap( Session.class );
        try {
            session.doReturningWork(
                    connection -> {
                        try (CallableStatement function = connection
                                .prepareCall(
                                        "{ ? = call NAPRZOD2.NPP_CASH_REPORTS.fn_delete_document(?,?,?) }")) {
                            function.registerOutParameter(1, Types.INTEGER);
                            function.setBigDecimal(2, docId);
                            function.setBigDecimal(3, casRapId);
                            function.setBigDecimal(4, docFrmId);
                            function.execute();
                            return function.getInt(1);
                        }
                    });
        } catch (JDBCException ex){
            Notification.show(ex.getSQLException().getMessage(),5000, Notification.Position.MIDDLE);
            return null;
        }
        return "OK";
    }


    public void save(Document document){ repo.save(document);}

}
