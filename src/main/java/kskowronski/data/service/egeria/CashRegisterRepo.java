package kskowronski.data.service.egeria;

import kskowronski.data.entity.egeria.CashRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface CashRegisterRepo extends JpaRepository<CashRegister, BigDecimal> {



}
