package kskowronski.data.service.global;

import kskowronski.data.entity.egeria.kg.CashRegister;
import kskowronski.data.entity.egeria.kg.CashRegisterDTO;
import kskowronski.data.entity.global.EatFirma;
import kskowronski.data.entity.inap.User;
import kskowronski.data.service.egeria.kg.CashRegisterRepo;
import kskowronski.data.service.inap.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    public List<CashRegisterDTO> cashRegistersDTO = new ArrayList<>();

    public void getGlobalData(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities ){
        List<CashRegister> crList = new ArrayList<>();

        companies = eatFirmaRepo.findAll();

        // get cash registers
        consolidationService.setConsolidateCompany();
        if (authorities.contains(new SimpleGrantedAuthority("USER"))){
            Optional<User> user = userService.findByUsername(userDetails.getUsername());
            if (user.isPresent())
                crList = cashRegisterRepo.findAllUserCash(user.get().getUzId());
        } else {
            crList = cashRegisterRepo.findAll(Sort.by("casName").ascending());
        }
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
