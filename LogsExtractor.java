package com.cx.automation.installation.dataretention;

import com.cx.automation.adk.io.FileUtil;
import com.google.common.io.Files;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by idana on 8/14/2017.
 */

/**
 * Extracts all logs from the checkmarx folder to
 */
public class LogsExtractor {
    private static final Logger log = LoggerFactory.getLogger(LogsExtractor.class);

    private static final String DEST_FOLDER = "\\\\storage\\QA\\CI-logs";
    private static final String APP_LOGS_FOLDER = "C:\\Program Files\\Checkmarx\\Logs";
    private static final String ENG_LOGS_FOLDER = "C:\\Program Files\\Checkmarx\\EngineScanLog";
    private static final String SCAN_LOGS = "C:\\Program Files\\Checkmarx\\Checkmarx Engine Server";
    private static final String CHECKMARX_FOLDER = "C:\\Program Files\\Checkmarx\\";
    private static final String USER = "tfs";
    private static final String PASSWORD = "Tfs12345";
    private static final String DOMAIN = "dm";

    public static void main(String[] args) {
        String filename = System.getProperty("filename");
        String workspaceRoot = System.getProperty("root");

        DateFormat dateFormat = new SimpleDateFormat("ddMMM_HHmm");
        Calendar cal = dateFormat.getCalendar();

        if (filename != null) {
            log.info(String.format("Extracting checkmarx logs to %s\\%s.zip", DEST_FOLDER, filename));
            compressFilesToRemoteDirByExtension(DEST_FOLDER, CHECKMARX_FOLDER, filename + "_" + dateFormat.format(cal.getTime()), "log", "zip");
            if (workspaceRoot != null) {
                compressFilesToRemoteDirByExtension(DEST_FOLDER, workspaceRoot, filename + "_" + dateFormat.format(cal.getTime()) + "_errorVidAndImages", "jpg", "avi");
            }
        }
    }

    private static void compressFilesToRemoteDirByExtension(String destDir, String sourceDir, String filename, String... extension) {
        List<File> files = FileUtil.getFilesByExtension(sourceDir, extension);
        File tempDir = Files.createTempDir();
        File tempDirChild = new File(tempDir.getPath() + "\\" + "logs");

        try {
            tempDirChild.mkdir();
            for (File file : files) {
                FileUtils.copyFileToDirectory(file, tempDirChild);
            }
            FileUtil.sendAsZipToRemoteFolder(tempDirChild.getPath(), destDir, USER, PASSWORD, DOMAIN, filename);
            FileUtils.deleteDirectory(tempDir);
        } catch (IOException | ZipException e) {
            log.error(e.getMessage());
        }
    }
}

