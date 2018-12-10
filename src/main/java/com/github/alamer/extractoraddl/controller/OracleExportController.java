package com.github.alamer.extractoraddl.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class OracleExportController {

    private String userName;
    private String password;
    private String connectionUrl;

    public OracleExportController(String userName, String password, String connectionUrl) {
        this.userName = userName;
        this.password = password;
        this.connectionUrl = connectionUrl;
    }

    public String exportDdlByTableName(String tableName) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        StringBuilder b=new StringBuilder();
        try(Connection conn=getConnection()) {
            b.append("--TABLE DEFINITION ").append(tableName).append(System.lineSeparator());
            b.append(getTable(conn,tableName));
            b.append("--TABLE INDEX ").append(tableName).append(System.lineSeparator());
            b.append(getIndex(conn,tableName));
            b.append("--TABLE COMMENTS ").append(tableName).append(System.lineSeparator());
            b.append(getColComments(conn,tableName));
            return  b.toString();
        }
    }


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl,userName,password);
    }

    private String getColComments(Connection conn,String tableName) throws IOException, SQLException {
        StringBuilder builder=new StringBuilder();
        String sql = new String(Files.readAllBytes(Paths.get("sql/exportComment.sql")));
        return executeExport(conn,tableName,sql);
    }

    private String getTable(Connection conn,String tableName) throws IOException, SQLException {
        StringBuilder builder=new StringBuilder();
        String sql = new String(Files.readAllBytes(Paths.get("sql/exportTable.sql")));
        return executeExport(conn,tableName,sql);
    }

    private String getIndex(Connection conn,String tableName) throws IOException, SQLException {
        StringBuilder builder=new StringBuilder();
        String sql = new String(Files.readAllBytes(Paths.get("sql/exportIndex.sql")));
        return executeExport(conn,tableName,sql);
    }

    private String executeExport(Connection conn,String tableName,String sql) throws SQLException {
        StringBuilder builder=new StringBuilder();
        try(PreparedStatement st=conn.prepareStatement(sql)) {
            st.setString(1,tableName);
            try(ResultSet rs=st.executeQuery()){
                while (rs.next()){
                    String exportLine=rs.getString(1);
                    builder.append(exportLine).append(System.lineSeparator());
                }
                return builder.toString();
            }
        }
    }

}
