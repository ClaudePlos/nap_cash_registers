package kskowronski.data.service.global;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

@Service
public class ConsolidationService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void setConsolidateCompany() {
        this.em.createNativeQuery("BEGIN eap_globals.USTAW_konsolidacje('T'); END;")
                //.setParameter("inParam1", inParam1)
                .executeUpdate();
    }


    @Transactional
    public void setConsolidateCompanyOnCompany(BigDecimal frmId) {
        this.em.createNativeQuery("BEGIN eap_globals.USTAW_firme(" + frmId + "); eap_globals.USTAW_konsolidacje('N'); END;")
                .executeUpdate();
    }

}
