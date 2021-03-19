package kskowronski.data.services.inap;

import kskowronski.data.entities.egeria.kg.NppMapCash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class NppMapCashService extends CrudService<NppMapCash, BigDecimal> {

    private NppMapCashRepo repo;

    public NppMapCashService(@Autowired NppMapCashRepo repo) {
        this.repo = repo;
    }

    @Override
    protected NppMapCashRepo getRepository() {
        return repo;
    }

    public List<NppMapCash> findAll(){return repo.findAll();}


}
