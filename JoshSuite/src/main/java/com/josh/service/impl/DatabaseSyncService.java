package com.josh.service.impl;

import com.josh.entity.file.FileMetaData;
import com.josh.repo.session.FileMetaDataSQLiteRepository;
import com.josh.repo.user.FileMetaDataMySQLRepository;
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
    public void syncFileMetaData() {
        List<FileMetaData> unsyncedFiles = sqliteRepo.findBySyncedFalse();

        for (FileMetaData meta : unsyncedFiles) {
            try {
                saveToMySQL(meta);         // ✅ Save using MySQL transaction
                markAsSyncedInSQLite(meta); // ✅ Update using SQLite transaction

                log.info("Synced metadata for file: {}", meta.getFilename());

            } catch (Exception e) {
                log.error("Failed to sync file metadata: {}", meta.getFilename(), e);
            }
        }
    }

    @Transactional(transactionManager = "mysqlTransactionManager")
    public void saveToMySQL(FileMetaData meta) {
        mysqlRepo.save(meta);
    }

    @Transactional(transactionManager = "sqliteTransactionManager")
    public void markAsSyncedInSQLite(FileMetaData meta) {
        meta.setSynced(true);
        sqliteRepo.save(meta);
    }
}
