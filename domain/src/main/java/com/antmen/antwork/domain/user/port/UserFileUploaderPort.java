package com.antmen.antwork.domain.user.port;

import com.antmen.antwork.domain.user.dto.ManagerIdFileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserFileUploaderPort {
    String upload(MultipartFile file, String folder) throws IOException;
    ManagerIdFileDto uploadWithMeta(MultipartFile file, String folder) throws IOException;
    void deleteFile(String fileUrl);
} 