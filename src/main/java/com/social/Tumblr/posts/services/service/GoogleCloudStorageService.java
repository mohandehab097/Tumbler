package com.social.Tumblr.posts.services.service;

import java.io.IOException;

public interface GoogleCloudStorageService {

    public String uploadFile(java.io.File filePath, String mimeType) throws IOException ;

    public String getFileUrl(String fileId);

    }
