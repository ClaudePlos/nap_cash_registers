package kskowronski.data.service.inap;

import kskowronski.data.entity.egeria.ckk.Client;
import kskowronski.data.entity.inap.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService extends CrudService<Role, BigDecimal> {

    private RoleRepo repo;

    public RoleService(@Autowired RoleRepo repo) {
        this.repo = repo;
    }

    @Override
    protected RoleRepo getRepository() {
        return repo;
    }

    public List<Role> findAll(){return repo.findAll();}


}
