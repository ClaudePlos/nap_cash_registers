package kskowronski.data.service.global;

import kskowronski.data.entity.egeria.kg.CashRegister;
import kskowronski.data.entity.egeria.kg.CashRegisterDTO;
import kskowronski.data.entity.global.EatFirma;
import kskowronski.data.service.egeria.kg.CashRegisterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GlobalDataService {

    @Autowired
    EatFirmaRepo eatFirmaRepo;

    @Autowired
    CashRegisterRepo cashRegisterRepo;

    @Autowired
    ConsolidationService consolidationService;

    public List<EatFirma> companies;
    public List<CashRegisterDTO> cashRegistersDTO = new ArrayList<>();

    public void getGlobalData(){
        // get companies
        companies = eatFirmaRepo.findAll();

        // get cash registers
        consolidationService.setConsolidateCompany();
        List<CashRegister> crList = cashRegisterRepo.findAll(Sort.by("casName").ascending());
        crList.stream().forEach( item -> cashRegistersDTO.add(mapperCashRegister(item)));
    }

    private CashRegisterDTO mapperCashRegister(CashRegister cr){
        CashRegisterDTO cashRegisterDTO = new CashRegisterDTO();
        cashRegisterDTO.setCasId(cr.getCasId());
        cashRegisterDTO.setCasName(cr.getCasName());
        cashRegisterDTO.setCasFrmId(cr.getCasFrmId());
        cashRegisterDTO.setCasDesc(cr.getCasDesc());
        cashRegisterDTO.setFrmName( companies.stream()
                .filter(item -> item.getFrmId().toString().equals(cr.getCasFrmId().toString()))
                .findAny().get().getFrmName() );
        return cashRegisterDTO;
    }

}
