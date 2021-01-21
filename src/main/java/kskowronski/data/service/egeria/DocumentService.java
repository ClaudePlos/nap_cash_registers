package kskowronski.data.service.egeria;

import kskowronski.data.entity.egeria.Document;
import kskowronski.data.service.global.ConsolidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService extends CrudService<Document, BigDecimal> {

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

    public Optional<List<Document>> getAllCashReports(BigDecimal casId, BigDecimal frmId){
        consolidationService.setConsolidateCompany();
        Optional<List<Document>> cashReports = this.repo.findByDocCasIdAndFrm(casId, frmId);
        return cashReports;
    }

}
