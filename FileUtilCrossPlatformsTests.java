package com.cx.automation.adk;

//import com.cx.automation.adk.io.FileUtilCrossPlatforms;
import com.cx.automation.adk.rest.RESTClient;
import com.cx.automation.adk.selenium.BrowserType;
import com.cx.automation.adk.selenium.WebDriverFactory;
import com.cx.automation.adk.selenium.WebDriverFactoryImpl;
import com.cx.automation.adk.systemtest.common.TestInfo;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

import static com.cx.automation.adk.systemtest.common.TestInfo.TestCategory.NONE;

/**
 * Created by idana on 5/17/2017.
 */

public class FileUtilCrossPlatformsTests {
    private static final String USER_NAME = "administrator";
    private static final String PASSWORD = "cx123456";
    private static final String SHARED_PATH_LINUX = "10.31.1.107/test/";
    private static final String SHARED_PATH_WIN = "10.31.1.71/c$/test/";
    private static final String LOCAL_WIN_FOLDER = "c:\\temp\\";
    private static final String FILE_NAME = "envp";

    @Ignore
    @TestInfo(description = "", category = {NONE}, id = 0, link = "")
    @Test
    public void test() {
//        try {
//            //copying file from local machine to a remote linux machine
//            FileUtilCrossPlatforms.copyLocalFileToRemoteDir(USER_NAME, ".", PASSWORD, SHARED_PATH_LINUX, LOCAL_WIN_FOLDER + FILE_NAME);
//            //copying file from local machine to a remote windows  machine
//            FileUtilCrossPlatforms.copyLocalFileToRemoteDir(USER_NAME, ".", PASSWORD, SHARED_PATH_WIN, LOCAL_WIN_FOLDER + FILE_NAME);
//            //checking if file exists on remote linux machine
//            boolean remoteFileExists = FileUtilCrossPlatforms.isRemoteFileExists(USER_NAME, ".", PASSWORD, SHARED_PATH_LINUX + FILE_NAME);
//            Assert.assertTrue("Failed to copy local file to remote directory", remoteFileExists);
//            //deleting file from a remote linux machine
//            FileUtilCrossPlatforms.deleteRemoteFile(USER_NAME, ".", PASSWORD, SHARED_PATH_LINUX + FILE_NAME);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    @Test
    public void testa(){
        Assert.assertEquals(1,2);

    }
    @Test
    public void testb(){
        Assert.assertEquals(2,3);

    }
    @Test
    public void testc(){
        Assert.fail("Idan , thanks for the coffee");

    }
}
