package kskowronski.data.services.inap;

import kskowronski.data.entities.egeria.kg.NppMapCash;
import org.springframework.data.jpa.repository.JpaRepository;


import java.math.BigDecimal;

public interface NppMapCashRepo extends JpaRepository<NppMapCash, BigDecimal> {

}
