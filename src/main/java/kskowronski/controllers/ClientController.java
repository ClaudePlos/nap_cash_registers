package kskowronski.controllers;

import kskowronski.data.entities.egeria.ckk.Client;
import kskowronski.data.services.egeria.ckk.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, path = "/api/clients")
@RestController
public class ClientController {

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) { this.clientService = clientService; }

    @GetMapping
    public List<Client> getClients() { return clientService.findAllClients(); }

    @GetMapping(value = "/filter")
    public List<Client> getClientsByText(@RequestParam String text) { return clientService.findFastClientForRest(text); }

    @GetMapping(value = "/{klKod}")
    public Client getClientsByKlKod(@PathVariable BigDecimal klKod) { return clientService.getClientByKlKod(klKod).get(); }
}
