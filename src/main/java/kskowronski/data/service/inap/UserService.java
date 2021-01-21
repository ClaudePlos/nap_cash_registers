package kskowronski.data.service.inap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import kskowronski.data.entity.inap.User;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService extends CrudService<User, BigDecimal> {

    private UserRepo repo;

    public UserService(@Autowired UserRepo repo) {
        this.repo = repo;
    }

    @Override
    protected UserRepo getRepository() {
        return repo;
    }

    public Optional<User> findById(BigDecimal prcId){
        return repo.findById(prcId);
    }

    public Optional<User> findByPassword(String pesel){ return repo.findByPassword(pesel);};

}
