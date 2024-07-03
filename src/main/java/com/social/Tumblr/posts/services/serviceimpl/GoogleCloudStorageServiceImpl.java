package com.social.Tumblr.posts.services.serviceimpl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.social.Tumblr.posts.services.service.GoogleCloudStorageService;
import com.social.Tumblr.security.services.serviceImp.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleCloudStorageServiceImpl implements GoogleCloudStorageService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private Drive driveService;
    
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public GoogleCloudStorageServiceImpl(ResourceLoader resourceLoader) throws IOException, GeneralSecurityException {
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new FileInputStream("/etc/secrets/cloud-storage-config.json")
            ).createScoped(Collections.singleton(DriveScopes.DRIVE));


            driveService = new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    new HttpCredentialsAdapter(credentials))
                    .build();
        } catch (GoogleJsonResponseException e) {
            // Handle specific Google API errors
            System.err.println("Google API Error: " + e.getDetails().getMessage());
            throw new RuntimeException("Error initializing Google Drive service", e);
        } catch (IOException | GeneralSecurityException e) {
            // Handle general errors
            System.err.println("Error initializing Google Drive service: " + e.getMessage());
            throw new RuntimeException("Error initializing Google Drive service", e);
        }
    }

    public String uploadFile(java.io.File filePath, String mimeType) throws IOException {
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(filePath.getName());
        String folderId = "1a--ZIfDTT7_P0Y03OFHpnZUNwuEcIJIg";
        fileMetadata.setParents(Collections.singletonList(folderId));
        FileContent mediaContent = new FileContent(mimeType, filePath);
        com.google.api.services.drive.model.File file = driveService
                .files()
                .create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        String imageUrl = "https://drive.google.com/uc?export=view&id=" + file.getId();
        logger.debug("image id is {}", file.getId());
        return file.getId();
    }

    public String getFileUrl(String fileId) {
        return "https://drive.google.com/uc?export=view&id=" + fileId;
    }

}
