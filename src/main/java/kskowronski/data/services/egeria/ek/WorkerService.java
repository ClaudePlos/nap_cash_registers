package kskowronski.data.services.egeria.ek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import kskowronski.data.entity.egeria.ek.Worker;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerService extends CrudService<Worker, BigDecimal> {

    private WorkerRepo repo;

    public WorkerService(@Autowired WorkerRepo repo) {
        this.repo = repo;
    }

    @Override
    protected WorkerRepo getRepository() {
        return repo;
    }

    public Optional<Worker> findById(BigDecimal prcId){ return repo.findById(prcId); }

    public Optional<Worker> findByUsername(String username){ return repo.findByUsername(username);}

    public Optional<List<Worker>> findFastWorker(String word){ return repo.findFastWorker(word.toUpperCase()); }

    public List<Worker> findAll(){ return repo.findAll();}

}
