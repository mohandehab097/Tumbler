package com.social.Tumblr.security.services.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImagesService {

    public String uploadImage(String path, MultipartFile file);

    public void deleteImage(String path, String fileName);

    }
