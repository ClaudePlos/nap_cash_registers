package kskowronski.data.service.inap;

import org.springframework.data.jpa.repository.JpaRepository;
import kskowronski.data.entity.inap.User;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, BigDecimal> {

    Optional<User> findById(BigDecimal prcId);

    Optional<User> findByUsername(String username);

    Optional<User> findByPassword(String pesel);

}
