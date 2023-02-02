package com.sptp.backend.aws.service;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Component
public class FileManager {

    private final String storageUrl;
    private final AwsService awsService;

    public FileManager(@Value("${aws.storage.url}") String storageUrl, AwsService awsService) {
        this.storageUrl = storageUrl;
        this.awsService = awsService;
    }

    public String extractExtension(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }

    public String getFullPath(String url) {
        if (Strings.isBlank(url)) {
            return null;
        }

        return storageUrl + url;
    }

    public String storeImageFile(String base64EncodedImage) {
        byte[] decodedImage = Base64.getDecoder().decode(base64EncodedImage);
        String storeFileName = UUID.randomUUID() + ".png";

        try {
            awsService.uploadImage(storeFileName, decodedImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return storeFileName;
    }
}