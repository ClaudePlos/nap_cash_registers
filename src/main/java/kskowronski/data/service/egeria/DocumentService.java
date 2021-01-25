package kskowronski.data.service.egeria;

import kskowronski.data.entity.egeria.Document;
import kskowronski.data.service.global.ConsolidationService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.Doc;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;
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
        Optional<List<Document>> cashReports = this.repo.findByDocCasIdAndFrm(casId, frmId);
        return cashReports;
    }

    public BigDecimal addNewCashReport(){

        Session session = em.unwrap( Session.class );

        Integer docId = session.doReturningWork(
                connection -> {
                    try (CallableStatement function = connection
                            .prepareCall(
                                    "{ ? = call NAPRZOD2.NPP_CASH_REPORTS.fn_add_cash_reports(?) }" )) {
                        function.registerOutParameter( 1, Types.INTEGER );
                        function.setInt( 2, 1 );
                        function.execute();
                        return function.getInt( 1 );
                    }
                });

        return BigDecimal.valueOf(docId);
    }

}
