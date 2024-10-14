package com.dragosghinea.royale.internal.utils.logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

public class FileLoggerWithRotation {

    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final int MAX_FILES = 10;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Pattern DATE_PATTERN_IN_FILE = Pattern.compile(".*-(\\d{4}-\\d{2}-\\d{2})(?:-\\((\\d+)\\))?\\.log(?:\\.gz)?");

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

        archiveExistingLogs();
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
        if (logWriter != null) {
            logWriter.close();
        }

        if (logFile != null) {
            gzipFile(logFile);
        }

        archiveExistingLogs();
    }

    private void archiveExistingLogs() throws IOException {
        File[] logFiles = logDirectory.listFiles((dir, name) -> name.endsWith(".log") || name.endsWith(".log.gz"));

        if (logFiles == null) return;

        Arrays.sort(logFiles, (file1, file2) -> {
            String name1 = file1.getName();
            String name2 = file2.getName();

            Matcher matcher1 = DATE_PATTERN_IN_FILE.matcher(name1);
            Matcher matcher2 = DATE_PATTERN_IN_FILE.matcher(name2);

            if (matcher1.matches() && matcher2.matches()) {
                // Parse dates
                LocalDate date1 = parseDate(matcher1.group(1), DATE_FORMATTER);
                LocalDate date2 = parseDate(matcher2.group(1), DATE_FORMATTER);
                int dateComparison = date2.compareTo(date1); // descending order for dates

                if (dateComparison != 0) {
                    return dateComparison;
                }

                // Parse and compare the optional numbers in descending order
                String numStr1 = matcher1.group(2);
                String numStr2 = matcher2.group(2);

                if (numStr1 != null && numStr2 != null) {
                    int num1 = Integer.parseInt(numStr1);
                    int num2 = Integer.parseInt(numStr2);
                    return Integer.compare(num2, num1); // descending order for numbers
                } else if (numStr1 != null) {
                    return -1; // file1 has a number, file2 does not, so file1 goes first
                } else if (numStr2 != null) {
                    return 1; // file2 has a number, file1 does not, so file2 goes first
                }
            }

            // Fallback to last modified if patterns do not match
            return Long.compare(file2.lastModified(), file1.lastModified());
        });

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

    private LocalDate parseDate(String dateStr, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Failed to parse date: " + dateStr);
            return LocalDate.MIN; // Return a minimum date if parsing fails
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
        return logFileName + "-" + DATE_FORMATTER.format(date) + "-(" + duplicateIndex + ")" + ".log";
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
