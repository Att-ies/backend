package com.sptp.backend.aws.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AwsService {

    private final AmazonS3Client amazonS3Client;
    private final FileService fileService;

    @Value("${aws.storage.url}")
    private String storageUrl;

    private String S3Bucket = "atties-bucket";

    public void uploadImage(MultipartFile image, String uuid) throws IOException {

        String ext = fileService.extractExt(image.getOriginalFilename());
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

    public String getOriginImageUrl(String url) {

        if (Strings.isBlank(url)) {
            return null;
        }

        return storageUrl + url;
    }
}