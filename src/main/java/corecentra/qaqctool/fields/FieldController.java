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
    @Autowired
    private ImpactFieldRepository impactFieldRepository;
    @Autowired
    private TaskFieldRepository taskFieldRepository;
    @Autowired
    private DonorFieldRepository donorFieldRepository;
    @Autowired
    private ContributionFieldRepository contributionFieldRepository;

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

    //region - impact Fields
    @GetMapping("/viewimpact")
    public String viewImpactFields(Model model) {
        List<ImpactFields> impactFieldsFieldsList = impactFieldRepository.findAll();
        model.addAttribute("impact", impactFieldsFieldsList);

        return "viewimpact";
    }

    @PostMapping("/update/impactfields")
    public String updateImpactFields(@ModelAttribute("impact") ImpactFields fields, Model model) {
        model.addAttribute("rowResult", fields);
        return "impactUpdate";
    }

    @RequestMapping("/impactUpdate")
    public String impactRowUpdate(@ModelAttribute(value = "impact") ImpactFields fields, Model model){
        fields.setFieldtype(fields.getFieldtype());
        fields.setFieldlimit(fields.getFieldlimit());
        impactFieldRepository.save(fields);

        return "redirect:viewimpact";
    }
    //endregion

    //region - task Fields
    @GetMapping("/viewtask")
    public String viewTaskFields(Model model) {
        List<TaskFields> taskFieldsFieldsList = taskFieldRepository.findAll();
        model.addAttribute("task", taskFieldsFieldsList);

        return "viewtask";
    }

    @PostMapping("/update/taskfields")
    public String updateTaskFields(@ModelAttribute("task") TaskFields fields, Model model) {
        model.addAttribute("rowResult", fields);
        return "taskUpdate";
    }

    @RequestMapping("/taskUpdate")
    public String taskRowUpdate(@ModelAttribute(value = "task") TaskFields fields, Model model){
        fields.setFieldtype(fields.getFieldtype());
        fields.setFieldlimit(fields.getFieldlimit());
        taskFieldRepository.save(fields);

        return "redirect:viewtask";
    }
    //endregion

    //region - donor Fields
    @GetMapping("/viewdonor")
    public String viewDonorFields(Model model) {
        List<DonorFields> donorFieldsFieldsList = donorFieldRepository.findAll();
        model.addAttribute("donor", donorFieldsFieldsList);

        return "viewdonor";
    }

    @PostMapping("/update/donorfields")
    public String updateDonorFields(@ModelAttribute("donor") DonorFields fields, Model model) {
        model.addAttribute("rowResult", fields);
        return "donorUpdate";
    }

    @RequestMapping("/donorUpdate")
    public String donorRowUpdate(@ModelAttribute(value = "donor") DonorFields fields, Model model){
        fields.setFieldtype(fields.getFieldtype());
        fields.setFieldlimit(fields.getFieldlimit());
        donorFieldRepository.save(fields);

        return "redirect:viewdonor";
    }
    //endregion

    //region - contribution Fields
    @GetMapping("/viewcontribution")
    public String viewContributionFields(Model model) {
        List<ContributionFields> contriFieldsFieldsList = contributionFieldRepository.findAll();
        model.addAttribute("contribution", contriFieldsFieldsList);

        return "viewcontribution";
    }

    @PostMapping("/update/contributionfields")
    public String updateContributionFields(@ModelAttribute("contribution") ContributionFields fields, Model model) {
        model.addAttribute("rowResult", fields);
        return "contributionUpdate";
    }

    @RequestMapping("/contributionUpdate")
    public String contributionRowUpdate(@ModelAttribute(value = "contribution") ContributionFields fields, Model model){
        fields.setFieldtype(fields.getFieldtype());
        fields.setFieldlimit(fields.getFieldlimit());
        contributionFieldRepository.save(fields);

        return "redirect:viewcontribution";
    }
    //endregion
}
