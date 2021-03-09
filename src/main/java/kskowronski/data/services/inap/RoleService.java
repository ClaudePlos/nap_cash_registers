package kskowronski.data.services.inap;

import kskowronski.data.entity.inap.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Service
public class RoleService extends CrudService<Role, BigDecimal> {

    @PersistenceContext
    private EntityManager em;

    private RoleRepo repo;

    public RoleService(@Autowired RoleRepo repo) {
        this.repo = repo;
    }

    @Override
    protected RoleRepo getRepository() {
        return repo;
    }

    public List<Role> findAll(){return repo.findAll();}

    public List<Role> findAllUserRoles(BigDecimal userId){ return repo.findAllUserRoles(userId);}

}
