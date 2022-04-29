package corecentra.qaqctool.fileupload;

import corecentra.qaqctool.client.ClientRepository;
import corecentra.qaqctool.project.ProjectController;
import corecentra.qaqctool.project.ProjectLookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Controller
public class FileUploadController {
    @Autowired
    private ClientRepository clientRepository;

    private String fileLocation = "src/main/java/corecentra/qaqctool/storage/";

    @GetMapping("/uploadFiles")
    public String viewUploadPage(){
        return "uploadFiles";
    }

    @GetMapping("/lookup/download")
    public ResponseEntity<Resource> download() throws IOException {
        File file = new File(fileLocation + "lookup_schema" + ".xlsx");

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lookupschema.xlsx");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @PostMapping("/upload/lookup")
    public RedirectView uploadLookup(@RequestParam("lookupfile") MultipartFile file, RedirectAttributes redirectAttribute, Model model) throws IOException{

        if (file.isEmpty()) {
            redirectAttribute.addFlashAttribute("Alert", "Empty File");
            return new RedirectView("/uploadFiles",true);
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileLocation + file.getName().concat("_"+getCurrentUser()).concat(".xlsx"));
            Files.write(path, bytes);
            redirectAttribute.addFlashAttribute("Success", "Successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException ioException) {
            redirectAttribute.addFlashAttribute("Alert", "Upload failed'" + file.getOriginalFilename() + "'");
            ioException.printStackTrace();
        }

        return new RedirectView("/uploadFiles",true);
    }

    @PostMapping("/upload/project")
    public RedirectView uploadProject(@RequestParam("project") MultipartFile file, RedirectAttributes redirectAttribute, Model model) throws IOException{

        if(!isLookupUploaded(String.format("lookupfile".concat("_"+getCurrentUser()).concat(".xlsx")))){
            redirectAttribute.addFlashAttribute("Alert", "Lookup File doesn't exist. Please Upload lookup file before proceeding");
            return new RedirectView("/uploadFiles",true);
        }

        if (file.isEmpty()) {
            redirectAttribute.addFlashAttribute("Alert", "Empty File");
            return new RedirectView("/uploadFiles",true);
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileLocation + file.getName().concat("_"+getCurrentUser()).concat(".xlsx"));
            Files.write(path, bytes);
            redirectAttribute.addFlashAttribute("Success", "Successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException ioException) {
            redirectAttribute.addFlashAttribute("Alert", "Upload failed'" + file.getOriginalFilename() + "'");
            ioException.printStackTrace();
        }

        return new RedirectView("/uploadFiles",true);
    }

    @PostMapping("/upload/result")
    public RedirectView uploadResult(@RequestParam("result") MultipartFile file, RedirectAttributes redirectAttribute, Model model) throws IOException{

        if(!isLookupUploaded(String.format("lookupfile".concat("_"+getCurrentUser()).concat(".xlsx")))){
            redirectAttribute.addFlashAttribute("Alert", "Lookup File doesn't exist. Please Upload lookup file before proceeding");
            return new RedirectView("/uploadFiles",true);
        }

        if (file.isEmpty()) {
            redirectAttribute.addFlashAttribute("Alert", "Empty File");
            return new RedirectView("/uploadFiles",true);
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileLocation + file.getName().concat("_"+getCurrentUser()).concat(".xlsx"));
            Files.write(path, bytes);
            redirectAttribute.addFlashAttribute("Success", "Successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException ioException) {
            redirectAttribute.addFlashAttribute("Alert", "Upload failed'" + file.getOriginalFilename() + "'");
            ioException.printStackTrace();
        }

        return new RedirectView("/uploadFiles",true);
    }

    @PostMapping("/upload/impact")
    public RedirectView uploadImpact(@RequestParam("impact") MultipartFile file, RedirectAttributes redirectAttribute, Model model) throws IOException{

        if(!isLookupUploaded(String.format("lookupfile".concat("_"+getCurrentUser()).concat(".xlsx")))){
            redirectAttribute.addFlashAttribute("Alert", "Lookup File doesn't exist. Please Upload lookup file before proceeding");
            return new RedirectView("/uploadFiles",true);
        }

        if (file.isEmpty()) {
            redirectAttribute.addFlashAttribute("Alert", "Empty File");
            return new RedirectView("/uploadFiles",true);
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileLocation + file.getName().concat("_"+getCurrentUser()).concat(".xlsx"));
            Files.write(path, bytes);
            redirectAttribute.addFlashAttribute("Success", "Successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException ioException) {
            redirectAttribute.addFlashAttribute("Alert", "Upload failed'" + file.getOriginalFilename() + "'");
            ioException.printStackTrace();
        }

        return new RedirectView("/uploadFiles",true);
    }

    @PostMapping("/upload/task")
    public RedirectView uploadTask(@RequestParam("task") MultipartFile file, RedirectAttributes redirectAttribute, Model model) throws IOException{

        if(!isLookupUploaded(String.format("lookupfile".concat("_"+getCurrentUser()).concat(".xlsx")))){
            redirectAttribute.addFlashAttribute("Alert", "Lookup File doesn't exist. Please Upload lookup file before proceeding");
            return new RedirectView("/uploadFiles",true);
        }

        if (file.isEmpty()) {
            redirectAttribute.addFlashAttribute("Alert", "Empty File");
            return new RedirectView("/uploadFiles",true);
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileLocation + file.getName().concat("_"+getCurrentUser()).concat(".xlsx"));
            Files.write(path, bytes);
            redirectAttribute.addFlashAttribute("Success", "Successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException ioException) {
            redirectAttribute.addFlashAttribute("Alert", "Upload failed'" + file.getOriginalFilename() + "'");
            ioException.printStackTrace();
        }

        return new RedirectView("/uploadFiles",true);
    }

    @PostMapping("/upload/donor")
    public RedirectView uploadDonor(@RequestParam("donor") MultipartFile file, RedirectAttributes redirectAttribute, Model model) throws IOException{

        if(!isLookupUploaded(String.format("lookupfile".concat("_"+getCurrentUser()).concat(".xlsx")))){
            redirectAttribute.addFlashAttribute("Alert", "Lookup File doesn't exist. Please Upload lookup file before proceeding");
            return new RedirectView("/uploadFiles",true);
        }

        if (file.isEmpty()) {
            redirectAttribute.addFlashAttribute("Alert", "Empty File");
            return new RedirectView("/uploadFiles",true);
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileLocation + file.getName().concat("_"+getCurrentUser()).concat(".xlsx"));
            Files.write(path, bytes);
            redirectAttribute.addFlashAttribute("Success", "Successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException ioException) {
            redirectAttribute.addFlashAttribute("Alert", "Upload failed'" + file.getOriginalFilename() + "'");
            ioException.printStackTrace();
        }

        return new RedirectView("/uploadFiles",true);
    }

    @PostMapping("/upload/contribution")
    public RedirectView uploadContribution(@RequestParam("contribution") MultipartFile file, RedirectAttributes redirectAttribute, Model model) throws IOException{

        if(!isLookupUploaded(String.format("lookupfile".concat("_"+getCurrentUser()).concat(".xlsx")))){
            redirectAttribute.addFlashAttribute("Alert", "Lookup File doesn't exist. Please Upload lookup file before proceeding");
            return new RedirectView("/uploadFiles",true);
        }

        if (file.isEmpty()) {
            redirectAttribute.addFlashAttribute("Alert", "Empty File");
            return new RedirectView("/uploadFiles",true);
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileLocation + file.getName().concat("_"+getCurrentUser()).concat(".xlsx"));
            Files.write(path, bytes);
            redirectAttribute.addFlashAttribute("Success", "Successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException ioException) {
            redirectAttribute.addFlashAttribute("Alert", "Upload failed'" + file.getOriginalFilename() + "'");
            ioException.printStackTrace();
        }

        return new RedirectView("/uploadFiles",true);
    }

    //region - common methods
    private boolean isLookupUploaded(String fileName) {
        return Files.exists(Path.of(fileLocation.concat(fileName)));
    }

    public String getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        return currentUserName;
    }
    //endregion

}
