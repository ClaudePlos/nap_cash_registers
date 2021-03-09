package kskowronski.data.services.egeria.ckk;

import kskowronski.data.entity.egeria.ckk.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService extends CrudService<Client, BigDecimal> {

    private ClientRepo repo;

    public ClientService(@Autowired ClientRepo repo) {
        this.repo = repo;
    }

    @Override
    protected ClientRepo getRepository() {
        return repo;
    }

    public Optional<Client> getClientByKlKod(BigDecimal klKod){ return repo.getClientByKlKod(klKod); }

    public Optional<List<Client>> findFastClient(String word){ return repo.findFastClient(word.toUpperCase()); }

}
