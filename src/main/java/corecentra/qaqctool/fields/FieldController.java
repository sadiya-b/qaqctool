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
    @Autowired
    private ResultFieldRepository resultFieldRepository;

    @GetMapping("/fields")
    public String viewFieldMapping() {
        return "fields";
    }

    //region - Project Fields
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
    //endregion

    //region - Result Fields
    @GetMapping("/viewresult")
    public String viewResultFields(Model model) {
        List<ResultFields> resultFieldsList = resultFieldRepository.findAll();
        model.addAttribute("result", resultFieldsList);

        return "viewresult";
    }

    @PostMapping("/update/resultfields")
    public String updateResultFields(@ModelAttribute("result") ResultFields fields, Model model) {
        model.addAttribute("rowResult", fields);
        return "resultUpdate";
    }

    @RequestMapping("/resultUpdate")
    public String resultRowUpdate(@ModelAttribute(value = "result") ResultFields fields, Model model){
        fields.setFieldtype(fields.getFieldtype());
        fields.setFieldlimit(fields.getFieldlimit());
        resultFieldRepository.save(fields);

        return "redirect:viewresult";
    }
    //endregion
}
