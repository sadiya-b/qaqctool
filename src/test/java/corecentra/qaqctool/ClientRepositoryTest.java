package corecentra.qaqctool;

import corecentra.qaqctool.client.Client;
import corecentra.qaqctool.client.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ClientRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository repo;

    @Test
    public void testCreateUser() {
        Client client = new Client();
        client.setName("test Client 1");
        client.setPassword("test1");

        Client savedUser = repo.save(client);

        Client existUser = entityManager.find(Client.class, savedUser.getId());

        assertThat(client.getName()).isEqualTo(existUser.getName());

    }
}
