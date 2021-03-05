package kskowronski.data.service.egeria.kg;

import kskowronski.data.entity.egeria.kg.CashRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface CashRegisterRepo extends JpaRepository<CashRegister, BigDecimal> {
}
