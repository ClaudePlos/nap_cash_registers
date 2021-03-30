package kskowronski.data.services.egeria.kg;

import kskowronski.data.entities.egeria.kg.NppMapCash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    public NppMapCash findByCashCode(String cashCode){
        Optional<NppMapCash> nppMapCash = repo.findByCashCode(cashCode);
        if (nppMapCash.isPresent()){
            return nppMapCash.get();
        } else {
            return new NppMapCash(null,"","","","","");
        }
    }


}
