package kskowronski.data.service.egeria;

import kskowronski.data.entity.egeria.CashRegister;
import kskowronski.data.entity.egeria.CashRegisterDTO;
import kskowronski.data.entity.global.EatFirma;
import kskowronski.data.service.global.ConsolidationService;
import kskowronski.data.service.global.EatFirmaRepo;
import kskowronski.data.service.global.GlobalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    ConsolidationService consolidationService;

    @Autowired
    EatFirmaRepo eatFirmaRepo;

    @Autowired
    GlobalDataService globalDataService;

    private List<EatFirma> companies;

    public List<CashRegisterDTO> getAllCashRegister(){
        consolidationService.setConsolidateCompany();
        List<CashRegisterDTO> cashRegistersDTO = new ArrayList<>();
        List<CashRegister> cashRegisters = this.repo.findAll(Sort.by("casName").ascending());

        cashRegisters.stream().forEach( item -> cashRegistersDTO.add(mapperCashRegister(item)));

        return cashRegistersDTO;
    }

    private CashRegisterDTO mapperCashRegister(CashRegister cr){
        CashRegisterDTO cashRegisterDTO = new CashRegisterDTO();
        cashRegisterDTO.setCasId(cr.getCasId());
        cashRegisterDTO.setCasName(cr.getCasName());
        cashRegisterDTO.setCasFrmId(cr.getCasFrmId());
        cashRegisterDTO.setCasDesc(cr.getCasDesc());
        cashRegisterDTO.setFrmName( globalDataService.companies.stream()
                .filter(item -> item.getFrmId().toString().equals(cr.getCasFrmId().toString()))
                .findAny().get().getFrmNazwa() );
        return cashRegisterDTO;
    }


}
