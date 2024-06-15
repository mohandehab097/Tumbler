package com.social.Tumblr.security.services.serviceImp;

import com.social.Tumblr.security.services.service.ImagesService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Transactional
public class ImagesServiceImpl implements ImagesService {


    @Override
    public String uploadImage(String path, MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            String filePath = path + File.separator + uniqueFilename;

            File f = new File(path);

            if (!f.exists()) {
                f.mkdir();
            }

            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            return uniqueFilename;

        } catch (IOException e) {
            throw new RuntimeException("error in uploading image");
        }
    }

    public void deleteImage(String path, String fileName) {
        try {
            Path filePath = Paths.get(path + File.separator + fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting image");
        }
    }

}
