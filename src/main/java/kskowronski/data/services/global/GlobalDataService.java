package kskowronski.data.services.global;

import kskowronski.data.entities.egeria.kg.CashRegister;
import kskowronski.data.entities.egeria.kg.CashRegisterDTO;
import kskowronski.data.entities.egeria.kg.TransactionDTO;
import kskowronski.data.entities.global.EatFirma;
import kskowronski.data.entities.inap.User;
import kskowronski.data.services.egeria.kg.CashRegisterRepo;
import kskowronski.data.services.inap.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class GlobalDataService {

    @Autowired
    EatFirmaRepo eatFirmaRepo;

    @Autowired
    CashRegisterRepo cashRegisterRepo;

    @Autowired
    ConsolidationService consolidationService;

    @Autowired
    UserService userService;

    private List<EatFirma> companies;
    public List<CashRegisterDTO> cashRegistersDTO;
    public List<TransactionDTO> transactions = new ArrayList<>();

    public void getGlobalData(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities ){

        //1.
        List<CashRegister> crList;
        cashRegistersDTO = new ArrayList<>();
        companies = eatFirmaRepo.findAll();

        if (authorities.isEmpty()) {
            return;
        }

        // get cash registers
        consolidationService.setConsolidateCompany();
        if (authorities.contains(new SimpleGrantedAuthority("USER"))){
            User user = userService.findByUsername(userDetails.getUsername());
            crList = cashRegisterRepo.findAllUserCash(user.getUzId());
        } else {
            crList = cashRegisterRepo.findAll(Sort.by("casName").ascending());
        }
        crList.stream().forEach( item -> cashRegistersDTO.add(mapperCashRegister(item)));

        //2.
        if (transactions.isEmpty()){
            TransactionDTO t =  new TransactionDTO(BigDecimal.valueOf(1L), "Utarg", "INCOME");
            TransactionDTO t1 = new TransactionDTO(BigDecimal.valueOf(2L), "Utarg Karta", "INCOME_CARD");
            TransactionDTO t2 = new TransactionDTO(BigDecimal.valueOf(3L), "Zaliczka pracownika", "CASH_ADVANCE");
            TransactionDTO t3 = new TransactionDTO(BigDecimal.valueOf(4L), "Klient", "CLIENT");
            TransactionDTO t4 = new TransactionDTO(BigDecimal.valueOf(5L), "Przekaz", "TRANSFER");

            TransactionDTO t5 = new TransactionDTO(BigDecimal.valueOf(6L), "Konwój", "BANK");
            TransactionDTO t6 = new TransactionDTO(BigDecimal.valueOf(7L), "Faktura got.", "CASH_INVOICE");
            TransactionDTO t7 = new TransactionDTO(BigDecimal.valueOf(8L), "Prowizja", "COMMISSION");
            TransactionDTO t8 = new TransactionDTO(BigDecimal.valueOf(9L), "Wynagrodzenie", "SALARY");


            transactions.add(t);
            transactions.add(t1);
            transactions.add(t2);
            transactions.add(t3);
            transactions.add(t4);
            transactions.add(t5);
            transactions.add(t6);
            transactions.add(t7);
            transactions.add(t8);
        }
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
