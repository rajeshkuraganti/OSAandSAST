package com.cx.automation.adk.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

/**
 * Created by: iland.
 * Date: 7/12/2015.
 */
public class MsSqlClient {

    private static final Logger log = LoggerFactory.getLogger(MsSqlClient.class);

    private static final int DEFAULT_PORT = 1433;

    public Connection conn;
    private String host;
    private Integer port;
    private String user;
    private String password;
    private String dbName;
    public String password;

    public MsSqlClient(String host, Integer port, String user, String password, String dbName, boolean sqlExpress) throws Exception {
        this.host = host;
        this.port = port != null ? port : DEFAULT_PORT;
        this.user = user;
        this.password = password;
        this.dbName = dbName;

        connect(sqlExpress);
    }

    private Connection connect(boolean sqlExpress) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        String url;
        if (sqlExpress) {
            url = "jdbc:jtds:sqlserver://" + host + ":" + port + "/" + dbName + ";instance=SQLEXPRESS";
        } else {
            url = "jdbc:jtds:sqlserver://" + host + ":" + port + "/" + dbName;
        }
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, user, password);
            log.info("Connected to the database");
            return conn;
        } catch (SQLException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            log.error("Fail to connect to SQL client with user " + user + " and password " + password + "\nurl= " + url, e);
            throw e;
        }
    }



//    String url = "jdbc:sqlserver://MYPC\\SQLEXPRESS;databaseName=MYDB;integratedSecurity=true";
//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//    Connection conn = DriverManager.getConnection(url);

    public void disconnect() {
        try {
            conn.close();
            log.info("Disconnected from database");
        } catch (SQLException e) {
            log.error("Fail to disconnect.", e);
        }
    }

    public ResultSet executeQuery(String sql) {
        log.info(String.format("Executing SQL Command: [%s]", sql));
        if (conn == null) {
            log.error("Can't execute query! SQL client is not connected.");
            return null;
        }
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setEscapeProcessing(true);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            log.error("Query failed to execute.", e);
            return null;
        }
    }

    public int updateRows(String sql) {
        if (conn == null) {
            log.error("Can't run query! SQL client is not connected.");
            return 0;
        }
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setEscapeProcessing(true);
            preparedStatement.setQueryTimeout(240);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Query failed!", e);
            return 0;
        }
    }

    public int[] executeLargeUpdate(String sql) {
        if (conn == null) {
            log.error("Can't run query! SQL client is not connected.");
            return new int[0];
        }
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setEscapeProcessing(true);
            preparedStatement.setQueryTimeout(240);
            return preparedStatement.executeBatch();
        } catch (SQLException e) {
            log.error("Query failed!", e);
            return new int[0];
        }
    }

    public boolean execute(String sql) {
        log.info(String.format("Executing SQL Command: [%s]", sql));
        if (conn == null) {
            log.error("Can't run query! SQL client is not connected.");
            return false;
        }
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            return preparedStatement.execute();
        } catch (SQLException e) {
            log.error("Query failed!", e);
            return false;
        }
    }

    public void runScript(InputStream in, String delimiterRegex) {
        Scanner s = new Scanner(in);
//        s.useDelimiter("((?m)^GO )|(--\n)");
        s.useDelimiter(delimiterRegex);
        Statement st = null;
        try {
            st = conn.createStatement();
            while (s.hasNext()) {
                String line = s.next();
                if (line.startsWith("!") && line.endsWith("")) {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " ".length());
                }

                if (line.trim().length() > 0) {
                    log.debug(line);
                    st.execute(line);
                }
            }
        } catch (SQLException e) {
            log.error("Fail", e);
        } finally {
            if (st != null) try {
                st.close();
            } catch (SQLException e) {
                log.error("Fail", e);
            }
        }
    }

}
