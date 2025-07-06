package com.josh.repo.file;


import com.josh.entity.file.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileMetaDataSQLiteRepository extends JpaRepository<FileMetaData, Long> {
    List<FileMetaData> findBySyncedFalse();
}
