package com.josh.service.impl;

import com.josh.entity.file.ChunkInfo;
import com.josh.entity.file.FileMetaData;
import com.josh.exception.FileDownloadException;
import com.josh.exception.FileNotFoundException;
import com.josh.exception.FileUploadException;
import com.josh.repo.file.FileMetaDataRepository;
import com.josh.service.FileService;
import com.josh.utils.CryptoUtil;
import com.josh.utils.SSHUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final CryptoUtil cryptoUtil;
    private final FileMetaDataRepository metaRepo;
    private final SSHUtil sshUtil;
    private final Environment env;

    private final int CHUNK_SIZE = 1024 * 1024; // 1MB

    @Override
    public void uploadFile(MultipartFile file, String userId) {
        try {
            byte[] fileBytes = file.getBytes();
            byte[] encryptedBytes = cryptoUtil.encrypt(fileBytes);
            int chunks = (int) Math.ceil((double) encryptedBytes.length / CHUNK_SIZE);

            List<ChunkInfo> chunkInfoList = IntStream.range(0, chunks)
                    .mapToObj(i -> {
                        int start = i * CHUNK_SIZE;
                        int end = Math.min(encryptedBytes.length, start + CHUNK_SIZE);
                        byte[] chunk = Arrays.copyOfRange(encryptedBytes, start, end);

                        String container = "comp20081-files-container" + ((i % 4) + 1);
                        String remotePath = "/home/ntu-user/" + UUID.randomUUID() + ".chunk";

                        try {
                            sshUtil.sendFileToContainer(chunk, container, remotePath);
                            return new ChunkInfo(i, container, remotePath);
                        } catch (Exception e) {
                            throw new FileUploadException("Failed to upload chunk " + i + " to container: " + e.getMessage());
                        }
                    })
                    .toList();

            FileMetaData meta = FileMetaData.builder()
                    .filename(file.getOriginalFilename())
                    .userId(userId)
                    .fileHash(DigestUtils.sha256Hex(fileBytes))
                    .totalChunks(chunks)
                    .uploadedAt(LocalDateTime.now())
                    .chunks(chunkInfoList)
                    .build();

            metaRepo.save(meta);

        } catch (Exception exception) {
            throw new FileUploadException("Failed to upload file: " + exception.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String filename, String userId) {
        FileMetaData meta = metaRepo.findByFilenameAndUserId(filename, userId)
                .orElseThrow(() -> new FileNotFoundException("File not found for user: " + userId));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        meta.getChunks().forEach(chunk -> {
            try {
                byte[] encryptedChunk = sshUtil.fetchFileFromContainer(chunk.getContainerName(), chunk.getRemotePath());
                outputStream.writeBytes(encryptedChunk);
            } catch (Exception e) {
                throw new FileDownloadException("Failed to fetch chunk from container: " + chunk.getContainerName());
            }
        });

        try {
            return cryptoUtil.decrypt(outputStream.toByteArray());
        } catch (Exception exception) {
            throw new FileDownloadException("Failed to decrypt file: " + exception.getMessage());
        }
    }
}
