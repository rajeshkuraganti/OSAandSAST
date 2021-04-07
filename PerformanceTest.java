//package com.cx.automation.adk;
//
//import autoitx4java.AutoItX;
//import com.cx.automation.adk.io.ThreadingUtil;
//import com.jacob.com.LibraryLoader;
//
//import java.io.File;
//import java.util.concurrent.TimeUnit;
//
//
/**
 * Created by aviat.
 * Date: 14/06/2017.
 */


//public class PerformanceTest {
//
//
//    public static void main(String[] args) {
//        final String USER_LOGIN = "admin@cx";
//        final String USER_PASSWORD = "Cx123456!";
//        final String JAVA_PROJECT = "\\\\storage\\QA\\Auto_Tests\\Projects\\JAVA\\Small\\Code_Injection";
//
//
//        File file = new File("c:\\cxdev\\Automation\\ADK\\src\\main\\resources\\com\\cx\\automation\\adk\\autoit\\jacob-1.18-x64.dll");
//        System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
//        AutoItX x = new AutoItX();
//
//        loginToAudit(x, USER_LOGIN, USER_PASSWORD);
//        mainScreen(x);
//        newLocalProject(x,JAVA_PROJECT);
//        executeQuery(x);
//        saveQuery(x);
//
////        closeAudit(x);
//    }
//
//    private static void saveQuery(AutoItX x){
//        String projectTitle = "CxAudit - [admin@cx: Connected to";
//        String saveProjectTitle = "Save Results";
//        String projectSettingTitle="Project Settings";
//        x.controlSend(projectTitle, "", "", "!r");
//        x.controlSend(projectTitle, "", "", "{ENTER}");
//        x.winWaitActive(saveProjectTitle);
//        x.controlSend(saveProjectTitle, "", "", "[NAME:btnSave]");
//        x.winWaitActive(projectSettingTitle);
//        x.ControlSetText(projectSettingTitle, "", "[NAME:projectNameTextBox]", "CxAudit-POC");
//        x.controlSend(projectSettingTitle, "", "", "[NAME:OK]");
//
//    }
//
//    private static void executeQuery(AutoItX x) {
//        String projectTitle = "CxAudit - [admin@cx: Connected to";
//        String projectTitle2 = "Run Multiple Queries";
//        x.controlSend(projectTitle, "", "", "{F6}");
//        x.winWaitActive(projectTitle2);
//        x.controlClick(projectTitle2, "", "[NAME:btnExecute]");
//        x.controlSend(projectTitle2, "", "", "{ENTER}");
//        ThreadingUtil.sleep(TimeUnit.SECONDS, 20);
//    }
//
//
//    private static void loginToAudit(AutoItX x, String username, String password) {
//        String APPLICATION_TITLE = "Login to Audit";
//        String APPLICATION_EXE = "C:\\Program Files\\Checkmarx\\Checkmarx Audit\\CxAudit.exe";
////        x.winMinimizeAll();
//        x.run(APPLICATION_EXE, "", AutoItX.SW_RESTORE);
//        x.winWaitActive(APPLICATION_TITLE);
//        x.winSetOnTop(APPLICATION_TITLE, "", true);
//        x.ControlSetText(APPLICATION_TITLE, "", "[NAME:userNameTextBox]", username);
//        x.ControlSetText(APPLICATION_TITLE, "", "[NAME:passwordTextBox]", password);
//        ThreadingUtil.sleep(TimeUnit.SECONDS, 2);
//        x.controlClick(APPLICATION_TITLE, "", "[Class:WindowsForms10.BUTTON.app.0.329445b_r9_ad1;Instance:1]");
//        x.winWaitActive("CxAudit");
//        ThreadingUtil.sleep(TimeUnit.SECONDS, 2);
//    }
//
//    private static void mainScreen(AutoItX x) {
//        String projectTitle = "CxAudit";
//        ThreadingUtil.sleep(TimeUnit.SECONDS, 1);
//        x.controlSend(projectTitle, "", "", "!f");
//        ThreadingUtil.sleep(TimeUnit.SECONDS, 1);
//        x.controlSend(projectTitle, "", "", "{ENTER}");
//        x.controlSend(projectTitle, "", "", "{ENTER}");
//    }
//
//    private static void newLocalProject(AutoItX x,String projectPath) {
//        String projectTitle = "New Local Project";
//        ThreadingUtil.sleep(TimeUnit.SECONDS, 1);
//        x.controlSend(projectTitle,"","[CLASS:Edit; INSTANCE:1]",projectPath);
//        x.controlClick(projectTitle,"","[NAME:ScanButton]");
//        x.winWaitActive("CxAudit - [admin@cx: Connected to http://localhost:80/, Project: Code_Injection]");
//        ThreadingUtil.sleep(TimeUnit.SECONDS, 20);
//    }
//
//    private static void closeAudit(AutoItX x) {
//        x.winClose("Login to Audit");
//        System.out.println("Close audit");
//    }
//
//
//}
