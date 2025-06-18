package com.file.servicer.task;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.file.servicer.service.FileService;

@Component
public class FileCleanerScheduler {
    @Autowired
    private FileService fileService;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight: second, minute, hour, day of month, month, day of week
    public void cleanOldFiles() {
        fileService.deleteOldFiles(Duration.ofDays(30));
    }
}
