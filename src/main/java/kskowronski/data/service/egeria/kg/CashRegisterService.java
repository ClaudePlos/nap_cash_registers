package kskowronski.data.service.egeria.kg;

import kskowronski.data.entity.egeria.kg.CashRegister;
import kskowronski.data.entity.egeria.kg.CashRegisterDTO;
import kskowronski.data.entity.inap.Role;
import kskowronski.data.service.global.EatFirmaRepo;
import kskowronski.data.service.global.GlobalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.artur.helpers.CrudService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CashRegisterService  extends CrudService<CashRegister, BigDecimal> {

    @PersistenceContext
    private EntityManager em;

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

    public List<CashRegister> findAll(){ return this.repo.findAll(Sort.by(Sort.Direction.ASC, "casName"));}

    public List<CashRegister> findAllUserCash(BigDecimal userId){ return repo.findAllUserCash(userId);}

    @Transactional
    public void deleteSetting(BigDecimal userId, BigDecimal casId) {
        this.em.createNativeQuery("delete from naprzod2.NPP_USERS_CASH where user_id = " + userId + " and cash_id = " + casId)
                .executeUpdate();
    }

    @Transactional
    public void saveSetting(BigDecimal userId, BigDecimal casId) {
        this.em.createNativeQuery("insert into naprzod2.NPP_USERS_CASH(user_id, cash_id) values (" + userId + ", " + casId + ")"  )
                .executeUpdate();
    }

}
