package kskowronski.data.service.inap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Service
public class UserRolesService  {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void delete(BigDecimal userId, BigDecimal roleId) {
        this.em.createNativeQuery("delete from naprzod2.NPP_USERS_ROLES where user_id = " + userId + " and role_id = " + roleId)
                .executeUpdate();
    }

    @Transactional
    public void save(BigDecimal userId, BigDecimal roleId) {
        this.em.createNativeQuery("insert into naprzod2.NPP_USERS_ROLES(user_id, role_id) values (" + userId + ", " + roleId + ")"  )
                .executeUpdate();
    }

}
