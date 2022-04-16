package corecentra.qaqctool.fileupload;
import java.io.*;
import java.nio.file.*;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        try  {
            Files.copy(multipartFile.getInputStream(),uploadPath.resolve(fileName),StandardCopyOption.REPLACE_EXISTING);
            //byte[] bytes = multipartFile.getBytes();
            //Path filePath = uploadPath.resolve(fileName);
            //Files.write(filePath, bytes);
        } catch (IOException ioe) {
            throw new IOException("Could not save excel file: " + fileName, ioe);
        }
    }
}