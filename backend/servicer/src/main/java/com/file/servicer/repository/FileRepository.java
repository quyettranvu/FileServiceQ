package com.file.servicer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.file.servicer.POJO.FileMetaData;

public interface FileRepository extends JpaRepository<FileMetaData, String> {
    
}
