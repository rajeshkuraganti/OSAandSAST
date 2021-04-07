package com.cx.automation.adk.database;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by: iland
 * Date: 7/1/2015
 */
public class MySqlClient {

    private static final Logger log = LoggerFactory.getLogger(MySqlClient.class);

    private static final int DEFAULT_PORT = 3306;

    public Connection conn;
    private String host;
    private Integer port;
    private String user;
    private String password;
    private String dbName;

    public MySqlClient(String host, Integer port, String user, String password, String dbName) {
        this.host = host;
        this.port = port != null ? port : DEFAULT_PORT;
        this.user = user;
        this.password = password;
        this.dbName = dbName;

        connect();
    }


    private Connection connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbName, user, password);
            log.info("Connected to the database");
            return conn;
        } catch (Exception e) {
            log.error("Fail to connect.", e);
            return null;
        }
    }

    public void disconnect() {
        try {
            conn.close();
            log.info("Disconnected from database");
        } catch (SQLException e) {
            log.error("Fail to disconnect.", e);
        }
    }

    public ResultSet executeQuery(String sql) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            log.error("Query failed to execute.", e);
            return null;
        }
    }

    public Integer executeUpdate(String sql) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Query failed to execute.", e);
            return null;
        }
    }

}
