package com.social.Tumblr.security.services.serviceImp;

import com.social.Tumblr.posts.services.service.GoogleCloudStorageService;
import com.social.Tumblr.security.services.service.ImagesService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class ImagesServiceImpl implements ImagesService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;


    @Override
    public String uploadImage(MultipartFile imageFile) {

        String imageFileName = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                File convFile = new File(Objects.requireNonNull(imageFile.getOriginalFilename()));
                convFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(convFile);
                fos.write(imageFile.getBytes());
                fos.close();

                imageFileName = googleCloudStorageService.uploadFile(convFile, imageFile.getContentType());
                logger.debug("Image saved successfully");

            } catch (IOException e) {
                throw new RuntimeException("Image upload failed");
            }
        }
        return imageFileName;
    }

}
