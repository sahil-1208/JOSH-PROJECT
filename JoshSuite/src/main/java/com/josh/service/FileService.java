package com.josh.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
    void uploadFile(MultipartFile file, String userId) throws Exception;

    byte[] downloadFile(String filename, String userId) throws Exception;
}
