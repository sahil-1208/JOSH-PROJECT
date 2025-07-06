package com.josh.repo.file;

import com.josh.entity.file.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetaDataMySQLRepository extends JpaRepository<FileMetaData, Long> {
}
