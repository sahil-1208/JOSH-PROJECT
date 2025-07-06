package com.josh.repo.file;

import com.josh.entity.file.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Long> {

    Optional<FileMetaData> findByFilenameAndUserId(String filename, String userId);
}
