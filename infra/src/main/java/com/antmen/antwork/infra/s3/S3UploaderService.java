package com.antmen.antwork.infra.s3;

import com.antmen.antwork.domain.user.dto.ManagerIdFileDto;
import com.antmen.antwork.domain.user.port.UserFileUploaderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3UploaderService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Override
    public String upload(MultipartFile file, String folder) throws IOException {
        // ...
    }

    @Override
    public ManagerIdFileDto uploadWithMeta(MultipartFile file, String folder) throws IOException {
        // ...
    }

    @Override
    public void deleteFile(String fileUrl) {
        // ...
    }
}
