package com.file.servicer.POJO;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "file_metadata")
public class FileMetaData {
    @Id
    private String fileId;

    private String originalFileName;
    private String path;
    private LocalDateTime lastDownloaded;
    private int downloadCount = 0;
    private String uploadedBy;
}
