package com.dragosghinea.royale.internal.utils.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class FileLoggerWithRotationTest {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final File logsDirectory = new File("logs");

    private void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }

        directoryToBeDeleted.delete();
    }

    @BeforeEach
    public void setUp() {
        if (!logsDirectory.exists()) {
            logsDirectory.mkdir();
        }
    }

    @AfterEach
    public void tearDown() {
        deleteDirectory(logsDirectory);
    }

    @Test
    @DisplayName("Test log content")
    public void testLogContent() throws IOException {
        FileLoggerWithRotation logger = new FileLoggerWithRotation(logsDirectory, "test");
        logger.log(Level.INFO, "Test log message");
        logger.log(Level.WARN, "Test log message 2");
        logger.log(Level.ERROR, "Test log message 3");
        logger.log(Level.FATAL, "Test log message 4");
        logger.log(Level.DEBUG, "Test log message 5");

        while(!logger.isQueueEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }

        logger.manuallyShutdown();

        assertNotEquals(logsDirectory.listFiles().length, 0);

        List<String> lines = Files.readAllLines(logsDirectory.listFiles()[0].toPath());
        assertEquals(5, lines.size());
        assertTrue(lines.get(0).contains("INFO") && lines.get(0).contains("Test log message"));
        assertTrue(lines.get(1).contains("WARN") && lines.get(1).contains("Test log message 2"));
        assertTrue(lines.get(2).contains("ERROR") && lines.get(2).contains("Test log message 3"));
        assertTrue(lines.get(3).contains("FATAL") && lines.get(3).contains("Test log message 4"));
        assertTrue(lines.get(4).contains("DEBUG") && lines.get(4).contains("Test log message 5"));
    }

    @Test
    @DisplayName("Test log rotation")
    public void testLogRotation() throws IOException, InterruptedException {
        List<String> fileNames = new ArrayList<>(10);

        for (int i = 14; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String fileName = "test-" + DATE_FORMATTER.format(date) + ".log";
            File logFile = new File(logsDirectory, fileName);
            logFile.createNewFile();
            Thread.sleep(10); // need them to be created a bit apart
            if (i < 7) {
                fileNames.add(fileName);
            }
        }

        FileLoggerWithRotation logger = new FileLoggerWithRotation(logsDirectory, "test");
        logger.refreshFile();

        List<String> directoryFileNames = Arrays.stream(logsDirectory.listFiles()).map(File::getName).collect(Collectors.toList());
        assertTrue(fileNames.stream().allMatch(file -> directoryFileNames.contains(file+".gz")));
        assertEquals(10, directoryFileNames.size());

        logger.manuallyShutdown();
    }
}
