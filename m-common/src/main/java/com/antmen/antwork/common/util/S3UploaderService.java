package com.antmen.antwork.common.util;

import com.antmen.antwork.common.api.response.account.ManagerIdFileDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploaderService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.static-url}")
    private String staticUrl;

    @Value("${aws.s3.endpoint}")
    private String endpoint;


    @PostConstruct
    public void listBuckets() {
        log.info("📦 MinIO 버킷 목록: {}", s3Client.listBuckets().buckets());
    }

    @PostConstruct
    public void checkS3Client() {
        log.info("✅ S3Client 확인 - endpoint: {}, bucket: {}", endpoint, bucketName);
    }

    public String upload(MultipartFile file, String folder) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String key = folder + "/" + UUID.randomUUID() + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();
        log.info("📤 업로드 요청 - bucket: {}, key: {}", bucketName, key);

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        return staticUrl + key;
    }

    // 파일정보 포함해서 저장
    public ManagerIdFileDto uploadWithMeta(MultipartFile file, String folder) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uuid = UUID.randomUUID().toString();
        String uuidFileName = uuid + extension;
        String key = folder + "/" + uuidFileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        String s3Url = staticUrl + key;

        return ManagerIdFileDto.builder()
                .originalFileName(originalFilename)
                .uuidFileName(uuidFileName)
                .extension(extension)
                .contentType(file.getContentType())
                .managerFileUrl(s3Url)
                .build();
    }

    public void deleteFile(String fileUrl) {
        if (!fileUrl.startsWith(staticUrl)) {
            throw new IllegalArgumentException("올바른 S3 URL이 아닙니다.");
        }
        String key = fileUrl.substring(staticUrl.length());
        s3Client.deleteObject(b -> b.bucket(bucketName).key(key));
    }
}