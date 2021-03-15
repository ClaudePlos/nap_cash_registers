package kskowronski.data.services.egeria.ckk;

import kskowronski.data.entities.egeria.ckk.Client;
import kskowronski.data.services.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ClientService extends CrudService<Client, BigDecimal> {

    @Autowired
    private ClientRepo repo;

//    public ClientService(@Autowired ClientRepo repo) {
//        this.repo = repo;
//    }

    @Override
    protected ClientRepo getRepository() {
        return repo;
    }

    public List<Client>findAllClients(){ return repo.findAll(); }

    public Optional<Client> getClientByKlKod(BigDecimal klKod){ return repo.getClientByKlKod(klKod); }

    public Optional<List<Client>> findFastClient(String word){ return repo.findFastClient(word.toUpperCase()); }

    public List<Client> findFastClientForRest(String word){
        return repo.findFastClient(word.toUpperCase()).orElseThrow(()-> new EntityNotFoundException("No found with word: " + word));
    }

    public List<Client> findFastClientForTest(String word){
        Iterable<Client> all = repo.findAll();
        return StreamSupport.stream(all.spliterator(), true)
                .filter(element -> element.getKldNazwa().equals(word))
                .collect(Collectors.toList());
    }

}
