package kskowronski.data.services.egeria.kg;

import kskowronski.data.entities.egeria.kg.NppMapCash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.util.Optional;

public interface NppMapCashRepo extends JpaRepository<NppMapCash, BigDecimal> {

    @Query("select c from NppMapCash c where c.cashCode = :cashCode")
    Optional<NppMapCash> findByCashCode(@Param("cashCode") String cashCode);

}
