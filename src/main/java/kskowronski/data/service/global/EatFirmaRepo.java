package kskowronski.data.service.global;

import org.springframework.data.jpa.repository.JpaRepository;
import kskowronski.data.entity.global.EatFirma;

import java.math.BigDecimal;
import java.util.Optional;

public interface EatFirmaRepo extends JpaRepository<EatFirma, BigDecimal> {

    Optional<EatFirma> findById(BigDecimal frmId);

    Optional<EatFirma> findByFrmNazwa(String frmName);

}
