package com.cx.automation.installation.old.tfsManagment;

import com.cx.automation.adk.io.FileUtil;
import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.build.IBuildServer;
import com.microsoft.tfs.core.httpclient.Credentials;
import com.microsoft.tfs.core.httpclient.DefaultNTCredentials;
import com.microsoft.tfs.core.httpclient.UsernamePasswordCredentials;
import com.microsoft.tfs.core.util.CredentialsUtils;
import com.microsoft.tfs.core.util.URIUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by: iland.
 * Date: 8/10/2015.
 */
public abstract class ConnectionClient {

    private static final Logger log = LoggerFactory.getLogger(ConnectionClient.class);

    private static final String NATIVE_REF = "com/cx/devops/native";
    private static final String NATIVE_FOLDER = "C:\\tfsnative";

    protected TFSTeamProjectCollection tfsCon;

    public ConnectionClient(String tfsUser, String password, String collectionUrl) {
//        URI uri;
//        String nativeDir = null;
//        try {
//            uri = ConnectionClient.class.getResource("/" + NATIVE_REF).toURI();
//            String scheme = uri.getScheme();
//            if (scheme.equals("jar")) {
//                Map<String, String> env = new HashMap<>();
//                env.put("create", "true");
//                FileSystem fs = FileSystems.newFileSystem(uri, env, ConnectionClient.class.getClassLoader());
//                String absolutePath = fs.toString();
//                nativeDir = extractNativeResources(absolutePath, NATIVE_FOLDER);
//                fs.close();
//            } else {
//                nativeDir = FileUtil.getAbsolutePath(NATIVE_REF);
//            }
//        } catch (URISyntaxException | IOException e) {
//            log.error("Fail to get jar path.", e);
//        }
//
//        System.setProperty("com.microsoft.tfs.jni.native.base-directory", nativeDir);
        connectToTFS(tfsUser, password, collectionUrl);
    }

    /**
     * Connect to TFS using a set of credentials that uses the currently logged
     * user in case no user name was provided otherwise it uses the provided
     * credentials
     *
     * @return The Team Project Collection connected to
     */
    protected void connectToTFS(String userName, String password, String collectionUrl) {
        Credentials credentials;

        // In case no username is provided and the current platform supports
        // default credentials, use default credentials
        if ((StringUtils.isEmpty(userName)) && CredentialsUtils.supportsDefaultCredentials()) {
            credentials = new DefaultNTCredentials();
        } else {
            credentials = new UsernamePasswordCredentials(userName, password);
        }

        SimpleConnectionAdvisor connectionAdvisor = new SimpleConnectionAdvisor(null);

        tfsCon = new TFSTeamProjectCollection(URIUtils.newURI(collectionUrl), credentials, connectionAdvisor);
    }

    /**
     * Checks if the build server version is older than TFS2010 and prints an
     * error message
     *
     * @param buildServer The build server to check its version
     * @return boolean true if the build server is older than TFS2010, false
     * otherwise
     */
    protected boolean isLessThanV3BuildServer(IBuildServer buildServer) {
        if (buildServer.getBuildServerVersion().isLessThanV3()) {
            log.error("This sample does not support TFS servers older than TFS2010");
            return true;
        }
        return false;
    }

    public void closeConnection() {
        tfsCon.close();
    }

    private String extractNativeResources(String jarFilePath, String destDir) {
        FileUtil.createFolder(destDir);
        JarFile jar;
        try {
            jar = new JarFile(jarFilePath);
            Enumeration enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                JarEntry file = (JarEntry) enumEntries.nextElement();
                File f = new File(destDir + File.separator + file.getName());
                if (file.isDirectory()) {
                    String folderName = file.getName();
                    if (folderName.equals("com/") ||
                            folderName.equals("com/cx/") ||
                            folderName.equals("com/cx/devops/") ||
                            folderName.contains("com/cx/devops/native/")) {
                        f.mkdir();
                    }
                } else if (file.getName().contains("com/cx/devops/native/")) {
                    InputStream is = jar.getInputStream(file);
                    FileOutputStream fos = new FileOutputStream(f);
                    while (is.available() > 0) {
                        fos.write(is.read());
                    }
                    fos.close();
                    is.close();
                }
            }
        } catch (IOException e) {
            log.error("Fail to extract Jar file: " + jarFilePath, e);
        }
        return destDir + "\\com\\cx\\devops\\native";
    }

}
