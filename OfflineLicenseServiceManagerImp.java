package com.cx.automation.installation.licenseGenerator.impl;

import com.cx.automation.adk.io.FileUtil;
import com.cx.automation.adk.io.FileUtilCrossPlatforms;
import com.cx.automation.adk.sikuli.imagerecognition.PatternElement;
import com.cx.automation.adk.sikuli.imagerecognition.PatternElementFactory;
import com.cx.automation.adk.sikuli.imagerecognition.exception.ElementFindException;
import com.cx.automation.adk.sikuli.imagerecognition.utils.PatternServices;
import com.cx.automation.adk.sikuli.keys.KeysServices;
import com.cx.automation.adk.winservices.WinServices;
import com.cx.automation.installation.licenseGenerator.CXLicenseImages;
import com.cx.automation.installation.licenseGenerator.OfflineLicenseServiceManager;
import com.cx.automation.installation.wix.desktop.ExtractResourcesUtil;
import com.cx.automation.installation.wix.desktop.PageBase;
import com.cx.automation.installer.offlineLicense.OfflineLicenseFields;
import com.cx.automation.installer.offlineLicense.OfflineLicenseServer;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;

import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_TAB;

/**
 * Created by tall on 25/05/2017.
 */

public class  OfflineLicenseServiceManagerImp implements OfflineLicenseServiceManager {
    private static Logger log = LoggerFactory.getLogger(OfflineLicenseServiceManagerImp.class);

    public static final String OSA_TOKEN = "\\\\storage\\QA\\Automatic tests\\Licenses\\OfflineLicense\\PartnerToken.reg";
    public static final String OFFLINE_LICENSE = "\"\\\\storage\\QA\\Automatic tests\\Licenses\\OfflineLicense\\OfflineLicenseGenerator.exe\"";
    public static final String OFFLINE_LICENSE_EXPORTED_FILE = "c:\\OfflineLicenseGenerator\\license.cxl";
    public static final String OFFLINE_LICENSE_EXPORTED_ROOT = "c:\\OfflineLicenseGenerator\\";
    public static final String LICENSE_TEMP_FOLDER = "OfflineLicenseAutomationFiles";
    public static final String LICENSE_REGISTRY_PATH ="HKEY_LOCAL_MACHINE\\SOFTWARE\\Checkmarx\\" ;
    public static final String RELATIVE_PATH_IMAGES_OLDUI = "Windows2008LicenseImages";
    public static final String RELATIVE_PATH_IMAGES_WIN7 = "Windows7LicenseImages";
    public static final String RELATIVE_PATH_IMAGES_WIN10 = "Windows10LicenseImages";
    public static final String IMAGES_ROOT_FOLDER = System.getProperty("user.home") + "\\" + LICENSE_TEMP_FOLDER;
    public static final String GET_OS_VERSION=System.getProperty("os.name");
    public static final String HID_ZIP_FILE = "\\\\storage\\QA\\Automatic tests\\Licenses\\HID\\HID.zip";
    public static final String HID_FOLDER_NAME = "HIDTempFolder";
    public static final String HID_LOCATION = "C:\\" + HID_FOLDER_NAME + "\\";
    public static final String EXPORTED_LICENSE_FOLDER_PATH = "c:\\LIC\\";
    public static final int TIMEOUT_BEFORE_CLICK = 30;
    public static final String PROCESS_NAME = "Offline License Generator";
    public static final int TIMEOUT_CMD=5000;


    public OfflineLicenseServiceManagerImp() {
    }

    public static void main(String[] args){

        String licenseEdition = System.getProperty("licenseGenerator.licenseEdition");
        String licenseTotalProject = System.getProperty("licenseGenerator.totalProjects");
        String licenseConcurrentScans = System.getProperty("licenseGenerator.concurrentScans");
        String licenseTotalUsers = System.getProperty("licenseGenerator.totalUsers");
        String licenseTotalAuditUsers = System.getProperty("licenseGenerator.totalAuditUsers");
        String licenseTotalLoc = System.getProperty("licenseGenerator.totalLoc");
        String licenseOsa = System.getProperty("licenseGenerator.osaEnabled");
        String licenseExpirationDay = System.getProperty("licenseGenerator.expirationDay");
        String licenseExpirationMonth = System.getProperty("licenseGenerator.expirationMonth");
        String licenseExpirationYear = System.getProperty("licenseGenerator.expirationYear");

        if (licenseEdition == null) {
            licenseEdition = "2";
        }
        if (licenseTotalProject == null) {
            licenseTotalProject = String.valueOf("3");
        }
        if (licenseConcurrentScans == null) {
            licenseConcurrentScans = String.valueOf("3");
        }
        if (licenseTotalUsers == null) {
            licenseTotalUsers = String.valueOf("2");
        }
        if (licenseTotalAuditUsers == null) {
            licenseTotalAuditUsers = String.valueOf("1");
        }
        if (licenseTotalLoc == null) {
            licenseTotalLoc = String.valueOf("12");
        }
        if (licenseOsa == null) {
            licenseOsa = String.valueOf("4");
        }
        if (licenseExpirationDay == null) {
            licenseExpirationDay = String.valueOf("2");
        }
        if (licenseExpirationMonth == null) {
            licenseExpirationMonth = String.valueOf("3");
        }
        if (licenseExpirationYear == null) {
            licenseExpirationYear = String.valueOf("1");
        }
        LocalDate licenseExpirationDate = new LocalDate(Integer.parseInt(licenseExpirationYear), Integer.parseInt(licenseExpirationMonth), Integer.parseInt(licenseExpirationDay));
        OfflineLicenseFields licenseFields = new OfflineLicenseFields(Integer.parseInt(licenseConcurrentScans), Boolean.parseBoolean(licenseOsa), Integer.parseInt(licenseTotalUsers), Integer.parseInt(licenseTotalAuditUsers), licenseEdition, Integer.parseInt(licenseTotalLoc), Integer.parseInt(licenseTotalProject), licenseExpirationDate);
        OfflineLicenseServiceManagerImp.generateLicenseLocalServer(licenseFields);
        OfflineLicenseServiceManagerImp.copyLicenseToLocalServer();
        OfflineLicenseServiceManagerImp.cleanupOfflineLicenseLocalFiles();
    }

    @Override
    public void generateLicense(OfflineLicenseFields licenseFields, OfflineLicenseServer serverDetails) {
        try {
            if (GET_OS_VERSION.equals("Windows 10")) {
                ExtractResourcesUtil.unzipAllFilesToOneFolder(RELATIVE_PATH_IMAGES_WIN10, LICENSE_TEMP_FOLDER);
            }
            if (GET_OS_VERSION.equals("Windows 7")) {
                ExtractResourcesUtil.unzipAllFilesToOneFolder(RELATIVE_PATH_IMAGES_WIN7, LICENSE_TEMP_FOLDER);
            }
            else {
                ExtractResourcesUtil.unzipAllFilesToOneFolder(RELATIVE_PATH_IMAGES_OLDUI, LICENSE_TEMP_FOLDER);
            }
            copyHIDZipFileToRemoteServer(serverDetails);
            openOfflineLicense();
            setAllfields(licenseFields, serverDetails);
        } catch (IOException e) {
            log.error("Error generating license: " + e.getMessage());
        }
    }


    public static void generateLicenseLocalServer(OfflineLicenseFields licenseFields) {
        if (GET_OS_VERSION.equals("Windows 10")) {
            copyImagesFromResourceToLocalFolder(RELATIVE_PATH_IMAGES_WIN10, LICENSE_TEMP_FOLDER);
        }
        if (GET_OS_VERSION.equals("Windows 7")) {
            copyImagesFromResourceToLocalFolder(RELATIVE_PATH_IMAGES_WIN7, LICENSE_TEMP_FOLDER);
        }
        else {
            copyImagesFromResourceToLocalFolder(RELATIVE_PATH_IMAGES_OLDUI, LICENSE_TEMP_FOLDER);
        }
        copyHIDZipFileToLocalServer();
        openOfflineLicense();
        setAllfieldsStandAlone(licenseFields);
    }


    private static void copyHIDZipFileToRemoteServer(OfflineLicenseServer serverDetails) {
        String hidRemoteRootPath = "\\\\" + serverDetails.getLicenseServerIP() + "\\c$\\";
        String hidRemoteFolderPath = hidRemoteRootPath + HID_FOLDER_NAME + "\\";
        String hidRemotePathSmb = serverDetails.getLicenseServerIP() + "/c$/" + HID_FOLDER_NAME + "/";
        if (FileUtil.isRemoteFileExists(serverDetails.getLicenseUserName(), serverDetails.getLicenseDomainName(), serverDetails.getLicensePassword(), hidRemoteFolderPath)) {
            log.info(HID_FOLDER_NAME + " Folder exist, deleting folder before execution.");
            deleteHIDRemoteFolder(serverDetails);
        }
        try {
            createFolderOnRemoteServer(HID_FOLDER_NAME, hidRemoteRootPath, serverDetails);
            FileUtilCrossPlatforms.copyLocalFileToRemoteDir(serverDetails.getLicenseUserName(), serverDetails.getLicenseDomainName(), serverDetails.getLicensePassword(), hidRemotePathSmb, HID_ZIP_FILE);
            FileUtil.extractZipFromRemote(serverDetails.getLicenseUserName(), serverDetails.getLicenseDomainName(), serverDetails.getLicensePassword(), hidRemoteFolderPath + "HID.zip", hidRemoteFolderPath);
            log.info("HID.zip was copied and extracted on remote server successfully.");
        } catch (IOException e) {
            log.error("Error generating license File to Remote server: " + e.getMessage());
        }
    }


    private static void copyHIDZipFileToLocalServer() {
        String localHIDFolder = "c:\\" + HID_FOLDER_NAME + "\\";
        try {
            FileUtils.copyFileToDirectory(new File(HID_ZIP_FILE), new File(localHIDFolder), false);
            FileUtil.extractFile(localHIDFolder + "HID.zip", localHIDFolder);
            log.info("HID.zip was copied and extracted on remote server successfully.");
        } catch (IOException | ZipException e) {
            log.error("Error generating license File to Remote server: " + e.getMessage());
        }
    }


    private static void copyImagesFromResourceToLocalFolder(String relativePathToResource, String targetFolderName) {
        try {
            File sourceLocation = new File(OfflineLicenseServiceManagerImp.class.getProtectionDomain().getCodeSource().getLocation().getPath() + relativePathToResource);
            File targetLocation = new File(System.getProperty("user.home") + "\\" + targetFolderName + "\\");
            if (sourceLocation.isDirectory()) {
                if (!targetLocation.exists()) {
                    targetLocation.mkdir();
                    log.debug("Folder doesn't exist, creating folder " + targetLocation);
                }
                File[] files = sourceLocation.listFiles();
                for (File file : files) {
                    InputStream in = new FileInputStream(file);
                    OutputStream out = new FileOutputStream(targetLocation + "/" + file.getName());
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    log.debug("Copy of Offline license image " + file + " have completed successfully.");
                    out.close();
                }
            }
        } catch (IOException e) {
            log.error("Error copying file: " + e.getMessage());
        }
    }


    private static void setAllfieldsStandAlone(OfflineLicenseFields licenseFields) {
        setNumberOfEngines(licenseFields.getNumberOfEngines());
        licenseFields.setHIDNumber(getHidFromLocalCXServer());
        setDateTimeField(licenseFields);
        setOsaField(licenseFields.isOsaEnabled(),licenseFields);
        setLicenseEdition(licenseFields.getLicenseEdition());
        setAuditUsers(licenseFields.getNumberOfAuditUsers());
        setNumberUsers(licenseFields.getNumberOfUsers());
        setNumberOfLoc(licenseFields.getNumberOfLoc());
        setNumberOfProjects(licenseFields.getNumberOfProjects());
        setHIDfield(licenseFields.getHIDNumber());
        clickOnCreate();
        ClickOnOK();
        clickOnClose();
    }


    private static void setAllfields(OfflineLicenseFields licenseFields, OfflineLicenseServer serverDetails) {
        setNumberOfEngines(licenseFields.getNumberOfEngines());
        licenseFields.setHIDNumber(getHidFromRemoteCXServer(serverDetails));
        setDateTimeField(licenseFields);
        setOsaField(licenseFields.isOsaEnabled(),licenseFields);
        setLicenseEdition(licenseFields.getLicenseEdition());
        setAuditUsers(licenseFields.getNumberOfAuditUsers());
        setNumberUsers(licenseFields.getNumberOfUsers());
        setNumberOfLoc(licenseFields.getNumberOfLoc());
        setNumberOfProjects(licenseFields.getNumberOfProjects());
        setHIDfield(licenseFields.getHIDNumber());
        clickOnCreate();
        ClickOnOK();
        clickOnClose();
        log.info("License was generated Successfully.");
    }


    private static void openOfflineLicense() {
        try {
            String commandToRun = null;
            if (!OFFLINE_LICENSE.isEmpty()) {
                commandToRun = String.format("cmd /c %s", OFFLINE_LICENSE);
                setForegroundWindow(PROCESS_NAME);
            }
            log.info("Running: " + commandToRun);
            Runtime.getRuntime().exec(commandToRun);
        } catch (IOException e) {
            log.error("Offline license - Can't open Offline License generator: " + e.getMessage());
        }
    }


    private static void createFolderOnRemoteServer(String folderPath, String path, OfflineLicenseServer serverDetails) {
        FileUtil.createRemoteFolder(folderPath, path, serverDetails.getLicenseUserName(), serverDetails.getLicenseDomainName(), serverDetails.getLicensePassword());
    }

    private static void setNumberOfLoc(int numberOfLoc) {
        try {
            PatternElement elemenEngine = PatternElementFactory.create(CXLicenseImages.ALLOWED_LOC.getLicensefileName(), IMAGES_ROOT_FOLDER);
            PatternServices.typeTextInPatternRightOffset(elemenEngine, 120, String.valueOf(numberOfLoc));
            log.info("Offline license - set Allowed LOC of users completed.");
        } catch (ElementFindException e) {
            log.error("Offline license - Can't find Allowed LOC text box: " + e.getMessage());
        }
    }


    private static void setLicenseEdition(String licenseEdition) {
        try {
            PatternElement firstElement = PatternElementFactory.create(CXLicenseImages.LICENSE_EDITION.getLicensefileName(), IMAGES_ROOT_FOLDER);
            PatternServices.clickButton(firstElement, TIMEOUT_BEFORE_CLICK);
            PatternElement secondElement = PatternElementFactory.create(CXLicenseImages.LICENSE_EDITION2.getLicensefileName(), IMAGES_ROOT_FOLDER);
            PatternServices.typeTextInPatternRightOffset(secondElement,15,licenseEdition);
            PatternServices.keyChooseDropDownPattern(secondElement,TIMEOUT_BEFORE_CLICK,1);
        } catch (ElementFindException e) {
            log.error("Offline license - Can't find Audit users text box: " + e.getMessage());
        }

    }

    private static void setNumberOfProjects(int numberOfProjects) {
        try {
            PatternElement elemenEngine = PatternElementFactory.create(CXLicenseImages.PROJECT_NUMBER.getLicensefileName(), IMAGES_ROOT_FOLDER);
            PatternServices.typeTextInPatternRightOffset(elemenEngine, 150, String.valueOf(numberOfProjects));
            log.info("Offline license - set number of project completed.");
        } catch (ElementFindException e) {
            log.error("Offline license - Can't find number of project text box: " + e.getMessage());
        }
    }


    private static void setNumberUsers(int numberUsers) {
        try {
            PatternElement elemenEngine = PatternElementFactory.create(CXLicenseImages.NUMBER_OF_USERS.getLicensefileName(), IMAGES_ROOT_FOLDER);
            PatternServices.typeTextInPatternRightOffset(elemenEngine, 25, String.valueOf(numberUsers));
            log.info("Offline license - set number of users completed.");
        } catch (ElementFindException e) {
            log.error("Offline license - Can't find users text box: " + e.getMessage());
        }
    }

    private static void setAuditUsers(int numberOfAuditUsers) {
        try {
            PatternElement elemenEngine = PatternElementFactory.create(CXLicenseImages.AUDIT_USERS.getLicensefileName(), IMAGES_ROOT_FOLDER);
            PatternServices.typeTextInPatternRightOffset(elemenEngine, 25, String.valueOf(numberOfAuditUsers));
            log.info("Offline license - set Audit users completed.");
        } catch (ElementFindException e) {
            log.error("Offline license - Can't find Audit users text box: " + e.getMessage());
        }
    }


    private static void setNumberOfEngines(int numberOfEngines) {
        try {
            PatternElement elemenEngine = PatternElementFactory.create(CXLicenseImages.NUMBER_OF_ENGINES.getLicensefileName(), IMAGES_ROOT_FOLDER);
            PatternServices.clickButton(elemenEngine, TIMEOUT_BEFORE_CLICK);
            PatternServices.keyChooseDropDownPattern(elemenEngine, TIMEOUT_BEFORE_CLICK, numberOfEngines-1);
            log.info("Offline license - set number of Engine completed.");
        } catch (ElementFindException e) {
            log.error("Offline license - Can't find Engine text box: " + e.getMessage());
        }
    }



    private static void clickOnClose() {
        try {
            PatternElement closeElement = PatternElementFactory.create(CXLicenseImages.LICENSE_CLOSE_BUTTON.getLicensefileName(), IMAGES_ROOT_FOLDER);
            PatternServices.clickButton(closeElement, TIMEOUT_BEFORE_CLICK);
            log.info("Offline license close windows - completed successfully.");
        } catch (ElementFindException e) {
            log.error("Error when trying to close Offline License: " + e.getMessage());
        }
    }


    private static void ClickOnOK() {
        try {
            if (isGenerated()) {
                PatternElement ElementOKButton = PatternElementFactory.create(CXLicenseImages.LICENSE_OK.getLicensefileName(), IMAGES_ROOT_FOLDER);
                PatternServices.clickButton(ElementOKButton, TIMEOUT_BEFORE_CLICK);
                log.info("Offline license Click OK button - completed successfully.");
            }
        } catch (ElementFindException e) {
            log.error("Offline License - Can't find generated license textbox: " + e.getMessage());
        }
    }


    private static void enableOsaInRegistry(OfflineLicenseFields licenseFields) {
        try {
            if (licenseFields.isOsaEnabled()) {
                String[] cmd = {"cmd","/c","regedit.exe", "/s", OSA_TOKEN};
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
                log.info("OSA Registry has been added successfully.");
            }
        } catch (InterruptedException | IOException e) {
            log.error("Error executing workaround for OSA: " + e.getMessage());
        }
    }


    private static void setOsaField(boolean osaEnabled,OfflineLicenseFields licenseFields) {
        try {
            if (!osaEnabled) {
                PatternElement osaElement = PatternElementFactory.create(CXLicenseImages.OSA_ENABLED.getLicensefileName(), IMAGES_ROOT_FOLDER);
                PatternServices.keyChooseDropDownPattern(osaElement, 10, 1);
                log.info("Offline license - OSA was disabled.");
            }
            else{
                enableOsaInRegistry(licenseFields);
                log.info("OSA should be enabled - executing workaround in Registry.");
            }
        } catch (ElementFindException e) {
            log.error("Offline license - Can't find OSA textbox: " + e.getMessage());
        }
    }


    private static void clickOnCreate() {
        try {
            PatternElement createElement = PatternElementFactory.create(CXLicenseImages.CREATE_BUTTON.getLicensefileName(), IMAGES_ROOT_FOLDER);
            PatternServices.clickButton(createElement, TIMEOUT_BEFORE_CLICK);
            log.info("Offline license - Click on Create completed.");
        } catch (ElementFindException e) {
            log.error("Offline license - Can't find Create textbox: " + e.getMessage());
        }
    }


    private static boolean isGenerated() {
        PatternElement createElement = PatternElementFactory.create(CXLicenseImages.LICENSE_WAS_GENERATED.getLicensefileName(), IMAGES_ROOT_FOLDER);
        return PatternServices.isPatternExists(createElement, TIMEOUT_BEFORE_CLICK);
    }


    private static void setHIDfield(String HIDLicenseNumber) {
        try {
            PatternElement element = PatternElementFactory.create(CXLicenseImages.HID_BUTTON.getLicensefileName(), IMAGES_ROOT_FOLDER);
            PatternServices.clickButton(element, TIMEOUT_BEFORE_CLICK);
            PatternServices.typeTextInPatternRightOffset(element, 2000, HIDLicenseNumber);
            log.info("Offline license - Set number of HID field completed.");
        } catch (ElementFindException e) {
            log.error("Offline license - Can't find HID textbox: " + e.getMessage());
        }
    }

    private static void setDayOfExpirationDate(OfflineLicenseFields licenseFields) {
        try {
            Robot robot = new Robot();
            robot.keyRelease(VK_TAB);
            robot.keyPress(VK_RIGHT);
            robot.keyRelease(VK_RIGHT);
            KeysServices.typeText(String.valueOf(licenseFields.getExpirationdDate().getDayOfMonth()));
            log.info("Expiration Date, set Day of the Week: " + licenseFields.getExpirationdDate().getDayOfMonth());
        } catch (AWTException e) {
            log.error("Offline license - Can't set Day text box: " + e.getMessage());
        }
    }

    private static void setMonthOfExpirationDate(OfflineLicenseFields licenseFields) {
        try {
            PatternElement elementDate = PatternElementFactory.create(CXLicenseImages.HID_BUTTON.getLicensefileName(), IMAGES_ROOT_FOLDER);
            PatternServices.clickButton(elementDate, TIMEOUT_BEFORE_CLICK);
            Robot robot = new Robot();
            robot.keyPress(VK_TAB);
            KeysServices.typeText(String.valueOf(licenseFields.getExpirationdDate().getMonthOfYear()));
            log.info("Expiration Date, set Month: " + licenseFields.getExpirationdDate().getMonthOfYear());
        } catch ( ElementFindException | AWTException e) {
            log.error("Offline license - Can't set Month text box: " + e.getMessage());
        }

    }

    private static void setYearOfExpirationDate(OfflineLicenseFields licenseFields) {
        try {
            Robot robot = new Robot();
            robot.keyPress(VK_RIGHT);
            robot.keyRelease(VK_RIGHT);
            KeysServices.typeText(String.valueOf(licenseFields.getExpirationdDate().getYear()));
            log.info("Expiration Date, set Year: " + licenseFields.getExpirationdDate().getYear());
        } catch (AWTException e) {
            log.error("Offline license - Can't set Year text box: " + e.getMessage());
        }
    }

    private static void setDateTimeField(OfflineLicenseFields licenseField) {
        setMonthOfExpirationDate(licenseField);
        setDayOfExpirationDate(licenseField);
        setYearOfExpirationDate(licenseField);
    }
    
    private static String getHidFromRemoteCXServer(OfflineLicenseServer serverDetails) {
        try {
            String output = FileUtil.executeRemoteFile(serverDetails.getLicenseServerIP(),serverDetails.getLicenseUserName(),serverDetails.getLicensePassword(),HID_LOCATION+"HID.exe", TIMEOUT_CMD);
            return output.substring(output.indexOf('#'), output.indexOf('_'));
        } catch (IOException | InterruptedException e) {
            log.error("Error retriving HID number from remote server. " +  e.getMessage());
        }
        return null;
    }

    private static String getHidFromLocalCXServer() {
        String localHIDFolder = "c:\\" + HID_FOLDER_NAME + File.separator + "HID.exe";
        try {
            String output =  FileUtil.executeFile(localHIDFolder,TIMEOUT_CMD);
            return output.substring(output.indexOf('#'), output.indexOf('_'));
        } catch (IOException | InterruptedException e) {
            log.error("Error retriving HID number from local server. " + e.getMessage());
        }
        return null;
    }


    private static String getLicensePathFromRemoteCXServer(OfflineLicenseServer serverDetails) {
        try {
            String output = WinServices.getRegistryKeyFromRemoteServer(serverDetails.getLicenseServerIP(), LICENSE_REGISTRY_PATH);
            String[] outputSplit = output.trim().split("WORKDIR");
            outputSplit = outputSplit[1].split("\\r\\n");
            return outputSplit[0].substring(14);
        } catch (IOException | InterruptedException e) {
            log.error("Fail to get License path by registry: " + e.getMessage());
        }
        return null;
    }


    public static void replaceLicenseToRemoteServer(OfflineLicenseServer serverDetails) {
        String output = getLicensePathFromRemoteCXServer(serverDetails);
        output = output.replace("\\", "/").replace(":","$");
        log.info("output registry value: "+  output+ " '\\' and ':' were replaced");
        try {
            String licenseRemotePath = serverDetails.getLicenseServerIP() + "/";
            FileUtilCrossPlatforms.copyLocalFileToRemoteDir(serverDetails.getLicenseUserName(), serverDetails.getLicenseDomainName(), serverDetails.getLicensePassword(), licenseRemotePath + output+"//", OFFLINE_LICENSE_EXPORTED_FILE);
            log.info("Offline License was copied to remote server successfully, folder: " + licenseRemotePath + output);
        } catch (IOException e) {
            log.error("Error generating license File to Remote server: " + e.getMessage());
        }
    }

    public static void copyLicenseToLocalServer() {
        try {
            FileUtils.copyFileToDirectory(new File(OFFLINE_LICENSE_EXPORTED_FILE), new File(EXPORTED_LICENSE_FOLDER_PATH), false);
            log.info("Offline License was copied to local server successfully, folder: " + EXPORTED_LICENSE_FOLDER_PATH);
        } catch (IOException e) {
            log.error("Error generating license File to Remote server: " + e.getMessage());
        }
    }

    public static void cleanupOfflineLicenseLocalFiles(){
        localFolderCleanup(OFFLINE_LICENSE_EXPORTED_ROOT);
        localFolderCleanup(IMAGES_ROOT_FOLDER);
        localFolderCleanup(HID_LOCATION);
    }

    public static void cleanupOfflineLicenseRemoteHID(OfflineLicenseServer serverDetails) {
        localFolderCleanup(OFFLINE_LICENSE_EXPORTED_ROOT);
        localFolderCleanup(IMAGES_ROOT_FOLDER);
        deleteHIDRemoteFolder(serverDetails);
    }

    private static void localFolderCleanup(String path) {
        try {
            FileUtils.deleteDirectory(new File(path));
            log.info(path + " directory was deleted successfully. ");
        } catch (IOException e) {
            log.error("Error when trying to delete Folder: " + path + e.getMessage());
        }
    }

    private static void deleteHIDRemoteFolder(OfflineLicenseServer serverDetails) {
        try {
            String localHidFolderPath = "\\\\" + serverDetails.getLicenseServerIP() + "\\c$\\" + HID_FOLDER_NAME + "\\";
            if (FileUtil.isRemoteFileExists(serverDetails.getLicenseUserName(), serverDetails.getLicenseDomainName(), serverDetails.getLicensePassword(), localHidFolderPath)) {
                FileUtil.deleteRemoteDirectory(serverDetails.getLicenseUserName(), serverDetails.getLicenseDomainName(), serverDetails.getLicensePassword(), new File(localHidFolderPath));
                log.info(localHidFolderPath + " directory was deleted successfully.");
            }
        } catch (IOException e) {
            log.error("Error deleting HID Remote folder: " + e.getMessage());
        }
    }
    public static boolean setForegroundWindow(String windowName) {
        User32 user32 = User32.instance;
        WinDef.HWND hWnd = user32.FindWindow(null, windowName);
        user32.ShowWindow(hWnd, User32.SW_SHOW);
        if (user32.SetForegroundWindow(hWnd)) {
            return true;
        } else {
            try {
                Thread.sleep(2555);
                user32.ShowWindow(hWnd, User32.SW_SHOW);
            } catch (InterruptedException e) {
                log.error("Fail to show window.", e);
            }
        }

        return user32.SetForegroundWindow(hWnd);
    }
    private interface User32 extends W32APIOptions {
        User32 instance = (User32) Native.loadLibrary("user32", User32.class, DEFAULT_OPTIONS);
        int SW_SHOW = 1;


        boolean ShowWindow(WinDef.HWND hWnd, int nCmdShow);

        boolean SetForegroundWindow(WinDef.HWND hWnd);

        WinDef.HWND FindWindow(String winClass, String title);

    }

}