package corecentra.qaqctool.project;

import com.poiji.bind.Poiji;
import corecentra.qaqctool.CustomError;
import corecentra.qaqctool.fileupload.FileUploadController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Controller
public class ProjectController {

    private String fileLocation = "src/main/java/corecentra/qaqctool/storage/";
    FileUploadController fileUploadController;

    public ProjectController() {}

    public ArrayList<ProjectLookup> getLookupData(){
        fileUploadController = new FileUploadController();
        String filePath =
                fileLocation + "lookupfile_" + fileUploadController.getCurrentUser() + ".xlsx";

        ArrayList<ProjectLookup> lookupData =
                (ArrayList<ProjectLookup>) Poiji.fromExcel(new File(filePath), ProjectLookup.class);


        return lookupData;
    }

    public boolean contains(ArrayList<ProjectLookup> list , ProjectLookup b){
       return list.stream().anyMatch(x->x.getNodeID() == b.nodeID
               && x.getOrganizationName().equals(b.organizationName)
               && x.getTheme().equals(b.theme)
               && x.getProgram().equals(b.program)
               && x.getProjectID().equals(b.projectID)
               && x.getProjectName().equals(b.projectName));
    }

    @GetMapping("/result/project")
    public String evaluateProject(Model model){
    List<CustomError> result = new ArrayList<>();

    ArrayList<ProjectLookup> lookupData = getLookupData();

    //check if data matches
    ArrayList<ProjectLookup> lookup1 = new ArrayList<>();
    lookup1.add(new ProjectLookup(385,"level 4", "Education","Event","PRJ0120","test project 3"));
    lookup1.add(new ProjectLookup(386,"level 4", "Education","Event","PRJ0120","test project 3"));

    for(ProjectLookup i : lookup1)
    {
    boolean ans = contains(lookupData,i);
    System.out.println(ans);
    }

    result.add(new CustomError("nodeid",1,"error"));

    model.addAttribute("error",result);

    return "projectResult";
    }

}
