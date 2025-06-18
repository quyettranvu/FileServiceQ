package com.file.servicer.POJO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaData {
    private String fileId;
    private String originalFileName;
    private String path;
    private LocalDateTime lastDownloaded;
}
