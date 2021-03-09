package kskowronski.data.services.egeria.ek;

import org.springframework.data.jpa.repository.JpaRepository;
import kskowronski.data.entity.egeria.ek.Worker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WorkerRepo extends JpaRepository<Worker, BigDecimal> {

    Optional<Worker> findById(BigDecimal prcId);

    Optional<Worker> findByUsername(String username);

    @Query("select w from Worker w where w.prcNazwisko like %:word% or w.prcImie like %:word%")
    Optional<List<Worker>> findFastWorker(@Param("word") String word);
}
