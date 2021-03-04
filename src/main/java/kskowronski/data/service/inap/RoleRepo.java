package kskowronski.data.service.inap;

import kskowronski.data.entity.egeria.ckk.Client;
import kskowronski.data.entity.inap.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, BigDecimal> {

//    @Query("select r from Role r where r.id")
//    List<Role> getRolesForUser(@Param("userId") BigDecimal userId);

}
