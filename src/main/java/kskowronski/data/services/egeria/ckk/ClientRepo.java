package kskowronski.data.services.egeria.ckk;

import org.springframework.data.jpa.repository.JpaRepository;
import kskowronski.data.entities.egeria.ckk.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ClientRepo  extends JpaRepository<Client, BigDecimal> {

    Optional<Client> getClientByKlKod(BigDecimal klKod);


    @Query("select c from Client c where c.kldZatwierdzony = 'T' and (trim(replace(c.kldNip,'-','')) like %:word% " +
            "or upper(c.kldNazwa) like %:word% " +
            "or to_char(c.klKod) like %:word% " +
            "or upper(c.kldCity) like %:word%)")
    Optional<List<Client>> findFastClient(@Param("word") String word);
}
