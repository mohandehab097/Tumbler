package com.social.Tumblr.security.services.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImagesService {

    public String uploadImage(MultipartFile imageFile);

    }
