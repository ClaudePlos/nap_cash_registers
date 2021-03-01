package kskowronski.data.service.egeria.ek;

import org.springframework.data.jpa.repository.JpaRepository;
import kskowronski.data.entity.egeria.ek.Worker;

import java.math.BigDecimal;
import java.util.Optional;

public interface WorkerRepo extends JpaRepository<Worker, BigDecimal> {

    Optional<Worker> findById(BigDecimal prcId);

    Optional<Worker> findByUsername(String username);
}
