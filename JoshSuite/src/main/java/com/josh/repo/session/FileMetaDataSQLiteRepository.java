package com.josh.repo.session;


import com.josh.entity.file.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileMetaDataSQLiteRepository extends JpaRepository<FileMetaData, Long> {

    Optional<FileMetaData> findByFilenameAndUserId(String filename, String userId);
    List<FileMetaData> findBySyncedFalse();
}
