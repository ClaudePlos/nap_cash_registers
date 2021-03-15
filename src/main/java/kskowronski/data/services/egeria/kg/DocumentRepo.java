package kskowronski.data.services.egeria.kg;

import kskowronski.data.entities.egeria.kg.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DocumentRepo extends JpaRepository<Document, BigDecimal> {

    @Query("select d from Document d where d.docCasId = :casId and d.docFrmId = :frmId order by d.docNo desc")
    Optional<List<Document>> findByDocCasIdAndFrm(@Param("casId") BigDecimal casId, @Param("frmId") BigDecimal frmId);

    @Query("select d from Document d where d.docDocIdZap = :docId and d.docFrmId = :frmId order by d.docNo asc")
    Optional<List<Document>> findByDocKpKwAndFrm(@Param("docId") BigDecimal docId, @Param("frmId") BigDecimal frmId);

}
