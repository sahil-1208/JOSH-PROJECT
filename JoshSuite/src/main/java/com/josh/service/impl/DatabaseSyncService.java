package com.josh.service.impl;



import com.josh.entity.file.FileMetaData;
import com.josh.repo.file.FileMetaDataMySQLRepository;
import com.josh.repo.file.FileMetaDataSQLiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseSyncService {

    private final FileMetaDataSQLiteRepository sqliteRepo;
    private final FileMetaDataMySQLRepository mysqlRepo;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void syncFileMetaData() {
        List<FileMetaData> unsyncedFiles = sqliteRepo.findBySyncedFalse();

        for (FileMetaData meta : unsyncedFiles) {
            try {
                mysqlRepo.save(meta);
                meta.setSynced(true);
                sqliteRepo.save(meta);

                log.info("Synced metadata for file: {}", meta.getFilename());

            } catch (Exception e) {
                log.error("Failed to sync file metadata: {}", meta.getFilename(), e);
            }
        }
    }
}
