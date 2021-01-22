package kskowronski.data.service.global;

import kskowronski.data.entity.global.EatFirma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalDataService {

    @Autowired
    EatFirmaRepo eatFirmaRepo;

    public List<EatFirma> companies;

    public void getGlobalData(){
        companies = eatFirmaRepo.findAll();
    }

}
