package corecentra.qaqctool.fields;

import corecentra.qaqctool.client.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class FieldController {

    @Autowired
    private ProjectFieldRepository projectFieldRepository;


    @GetMapping("/fields")
    public String viewFieldMapping() {
        return "fields";
    }

    @GetMapping("/viewproject")
    public String viewProjectFields(Model model) {
        List<ProjectFields> projectFieldsList = projectFieldRepository.findAll();
        model.addAttribute("project", projectFieldsList);

        return "viewproject";
    }


    @PostMapping("/update/projectfields")
    public String updateProjectFields(@ModelAttribute("project") ProjectFields fields, Model model) {
        model.addAttribute("rowProj", fields);

        return "projUpdate";
    }

    @RequestMapping("/projUpdate")
    public String projectRowUpdate(@ModelAttribute(value = "project") ProjectFields fields, Model model){
        fields.setFieldtype(fields.getFieldtype());
        fields.setFieldlimit(fields.getFieldlimit());
        projectFieldRepository.save(fields);

        return "redirect:viewproject";
    }
}
