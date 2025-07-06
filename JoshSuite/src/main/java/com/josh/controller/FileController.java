package com.josh.controller;

import com.josh.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file,
                                         @RequestParam String userId) throws Exception {
        fileService.uploadFile(file, userId);
        return ResponseEntity.ok("File uploaded");
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String filename,
                                           @RequestParam String userId) throws Exception {
        byte[] file = fileService.downloadFile(filename, userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(file);
    }
}
