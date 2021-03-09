package kskowronski.data.services.egeria.kg;

import kskowronski.data.entity.egeria.kg.CashRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CashRegisterRepo extends JpaRepository<CashRegister, BigDecimal> {

    @Query(value = "SELECT * from NZT_KASY where KAS_ID in (SELECT cash_Id FROM naprzod2.NPP_USERS_CASH  where user_Id = ?1)", nativeQuery = true)
    List<CashRegister> findAllUserCash(BigDecimal userId);

}
