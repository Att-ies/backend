package com.sptp.backend.aws.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class AwsService {

    private final AmazonS3Client amazonS3Client;

    @Value("${aws.storage.name}")
    private String S3Bucket; // develop "atties-dev-storage", main "atties-bucket"

    public void uploadImage(MultipartFile image, String uuid) throws IOException {

        String originalFilename = image.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        long size = image.getSize();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(size);

        // S3 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(S3Bucket, uuid + "." + ext, image.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }

    public void uploadImage(String fileName, byte[] imageBytes) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageBytes.length);

        amazonS3Client.putObject(new PutObjectRequest(S3Bucket, fileName, inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }
}