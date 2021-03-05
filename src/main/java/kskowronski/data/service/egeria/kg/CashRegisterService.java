package kskowronski.data.service.egeria.kg;

import kskowronski.data.entity.egeria.kg.CashRegister;
import kskowronski.data.entity.egeria.kg.CashRegisterDTO;
import kskowronski.data.service.global.EatFirmaRepo;
import kskowronski.data.service.global.GlobalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CashRegisterService  extends CrudService<CashRegister, BigDecimal> {

    private CashRegisterRepo repo;

    public CashRegisterService(@Autowired CashRegisterRepo repo) {
        this.repo = repo;
    }

    @Override
    protected CashRegisterRepo getRepository() {
        return repo;
    }

    @Autowired
    EatFirmaRepo eatFirmaRepo;

    @Autowired
    GlobalDataService globalDataService;

    public List<CashRegisterDTO> getAllCashRegister(){
        return globalDataService.cashRegistersDTO;
    }




}
