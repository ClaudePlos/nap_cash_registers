package kskowronski.data.services.egeria.ckk;

import kskowronski.data.entity.egeria.ckk.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ClientServiceTest {

    @Mock
    private ClientRepo clientRepo;

    @InjectMocks
    private ClientService clientService = new ClientService();

    @Test
    //@DisplayName("Should Return Selected Company")
    public void shouldReturnSelectedCompany(){
        // given
        Client client1 = new Client();
        client1.setKldNazwa("Anfix");
        Client client2 = new Client();
        client2.setKldNazwa("Selgros");
        Iterable<Client> all = Arrays.asList(client1, client2);
        Mockito.doReturn(all).when(clientRepo).findAll();

        // when
        List<Client> actual = clientService.findFastClientForTest("Anfix");

        //then
        Assertions.assertEquals("Anfix", actual.get(0).getKldNazwa());
    }

}