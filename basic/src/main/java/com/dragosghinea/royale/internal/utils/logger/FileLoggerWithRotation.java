package com.dragosghinea.royale.internal.utils.logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.*;
import java.util.zip.GZIPOutputStream;

public class FileLoggerWithRotation {

    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final int MAX_FILES = 10;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private File logFile;
    private BufferedWriter logWriter;
    private final BlockingQueue<String> logQueue;
    private final ExecutorService logExecutor;
    private final File logDirectory;
    private final String logFileName;

    private LocalDate today;

    private final Thread shutdownHook = new Thread(this::shutdown);

    public FileLoggerWithRotation(File logDirectory, String logFileName) throws IOException {
        this.logDirectory = logDirectory;
        this.logQueue = new LinkedBlockingQueue<>();
        this.logExecutor = Executors.newSingleThreadExecutor();
        this.logFileName = logFileName;

        setupLogger();

        logExecutor.submit(this::processLogQueue);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    private void setupLogger() throws IOException {
        Path logDirPath = logDirectory.toPath();
        if (!Files.exists(logDirPath)) {
            Files.createDirectories(logDirPath);
        }

        today = LocalDate.now();
        String logFileName = generateLogFileName(today);
        logFile = new File(logDirectory, logFileName);

        int duplicateIndex = 1;
        while (logFile.exists() || new File(logFile.getAbsolutePath() + ".gz").exists()) {
            logFile = new File(logDirectory, generateLogFileName(today, duplicateIndex));
            duplicateIndex++;
        }

        logWriter = new BufferedWriter(new FileWriter(logFile, true));
    }

    private void processLogQueue() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String message = logQueue.take();
                synchronized (logWriter) {
                    checkDateRotation();

                    logWriter.write(message);
                    logWriter.flush();

                    checkSizeRotation();
                }
            }
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
        } finally {
            try {
                logWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkDateRotation() throws IOException {
        LocalDate now = LocalDate.now();
        if (!now.equals(today)) {
            today = now;
            rotateLogs();
            setupLogger();
        }
    }

    private void checkSizeRotation() throws IOException {
        if (logFile.length() >= MAX_FILE_SIZE) {
            rotateLogs();
            setupLogger();
        }
    }

    private void rotateLogs() throws IOException {
        logWriter.close();
        gzipFile(logFile);

        File[] logFiles = logDirectory.listFiles((dir, name) -> name.endsWith(".log") || name.endsWith(".log.gz"));

        if (logFiles == null) return;

        Arrays.sort(logFiles, Comparator.comparingLong(File::lastModified).reversed());

        for (int i = 0; i < logFiles.length; i++) {
            File logFile = logFiles[i];

            if (i >= MAX_FILES - 1) {
                Files.delete(logFile.toPath());
                continue;
            }

            if (logFile.getName().endsWith(".log"))
                gzipFile(logFile);
        }
    }

    private void gzipFile(File file) throws IOException {
        String gzipFileName = file.getAbsolutePath() + ".gz";
        try (FileInputStream fis = new FileInputStream(file);
             FileOutputStream fos = new FileOutputStream(gzipFileName);
             GZIPOutputStream gzipOS = new GZIPOutputStream(fos)) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                gzipOS.write(buffer, 0, len);
            }
        }
        Files.delete(file.toPath());
    }

    private String generateLogFileName(LocalDate date, int duplicateIndex) {
        return logFileName + "-" + DATE_FORMATTER.format(date)+ "-(" + duplicateIndex + ")" + ".log";
    }

    private String generateLogFileName(LocalDate date) {
        return logFileName + "-" + DATE_FORMATTER.format(date) + ".log";
    }

    private void shutdown() {
        try {
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logExecutor.shutdownNow();
    }

    public boolean isQueueEmpty() {
        return logQueue.isEmpty();
    }

    public void manuallyShutdown() {
        shutdown();
        Runtime.getRuntime().removeShutdownHook(shutdownHook);
    }

    public void refreshFile() {
        try {
            rotateLogs();
            setupLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(Level level, String message) {
        String logMessage = String.format("[%s] %s - %s%n", level, LocalDateTime.now().format(DATE_TIME_FORMATTER), message);
        logQueue.offer(logMessage);
    }
}
