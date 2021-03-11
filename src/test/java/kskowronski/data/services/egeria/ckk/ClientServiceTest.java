package kskowronski.data.services.egeria.ckk;

import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ClientServiceTest {

    @Mock
    private ClientRepo clientRepo;


    /*
     before test change in repository code on:
            @Autowired
            private ClientRepo repo;
     */
//    @InjectMocks
//    private ClientService clientService = new ClientService();
//
//    @BeforeEach
//    private void setUpRepository(){
//        // given
//        Client client1 = new Client();
//        client1.setKldNazwa("Anfix");
//        Client client2 = new Client();
//        client2.setKldNazwa("Selgros");
//        Iterable<Client> all = Arrays.asList(client1, client2);
//        Mockito.doReturn(all).when(clientRepo).findAll();
//    }
//
//    @Test
//    //@DisplayName("Should Return Selected Company")
//    public void shouldReturnSelectedCompany(){
//
//        // when
//        List<Client> actual = clientService.findFastClientForTest("Anfix");
//
//        //then
//        Assertions.assertEquals("Anfix", actual.get(0).getKldNazwa());
//    }
//
//    @Test
//    public void shouldNoReturnAnyCompany(){
//
//        // when
//        List<Client> actual = clientService.findFastClientForTest("Anfix22");
//
//        //then
//        assertTrue(actual.isEmpty());
//    }

}