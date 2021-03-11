package kskowronski.data.services.inap;

import kskowronski.data.entities.inap.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface RoleRepo extends JpaRepository<Role, BigDecimal> {

    @Query(value = "SELECT * from naprzod2.NPP_ROLES where ROLE_ID in (SELECT role_Id FROM naprzod2.NPP_USERS_ROLES  where user_Id = ?1)", nativeQuery = true)
    List<Role> findAllUserRoles(BigDecimal userId);

}
