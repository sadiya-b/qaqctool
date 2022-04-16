package corecentra.qaqctool.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ClientRepository extends JpaRepository<Client,Long> {
    //gets client based on the id
    Client findById(long id);

    //to get all the clients in the db
    List<Client> findAll();

    Client findByName(String name);
}
