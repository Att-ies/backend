package com.sptp.backend.aws.service;

import org.springframework.stereotype.Service;

@Service
public class FileService {

    public String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}