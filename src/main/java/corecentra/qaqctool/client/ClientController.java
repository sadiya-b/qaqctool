package corecentra.qaqctool.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ClientController {
    @Autowired
    private ClientRepository clientRepo;

    @GetMapping("/index")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/createClient")
    public String createClientForm(Model model){
        model.addAttribute("client", new Client());
        return "createClient";
    }

    @PostMapping("/processClient")
    public String processNewClient(Client client){
        client.setName(client.getName());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(client.getPassword());
        client.setPassword(encodedPassword);

        clientRepo.save(client);

        return "createSuccess";
    }

    @GetMapping("/clientlist")
    public String listClients(Model model){
        List<Client> list = clientRepo.findAll();
        model.addAttribute("clients",list);

        return "clientlist";
    }


}
