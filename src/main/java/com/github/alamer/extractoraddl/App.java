package com.github.alamer.extractoraddl;

import com.beust.jcommander.JCommander;
import com.github.alamer.extractoraddl.controller.OracleExportController;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class App {


    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        Args args1 = new Args();
        JCommander.newBuilder()
                .addObject(args1)
                .build()
                .parse(args);
        String[] tables = args1.getTableNames().split(",");
        Config conf = ConfigFactory.parseFile(new File(args1.getConfig()));
        String server = conf.getString("database-cfg.server");
        String login = conf.getString("database-cfg.login");
        String password = conf.getString("database-cfg.password");
        for (String tableName : tables) {
            OracleExportController exportController = new OracleExportController(login, password, server);
            String resultString = exportController.exportDdlByTableName(tableName);
            Path path = Paths.get("out/" + tableName + "_DDL.sql");
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(resultString);
            }
        }

        if (args1.getViewNames() != null) {
            String[] views = args1.getViewNames().split(",");
            for (String tableName : tables) {
                OracleExportController exportController = new OracleExportController(login, password, server);
                String resultString = exportController.exportDdlByViewName(tableName);
                Path path = Paths.get("out/view/" + tableName + "_DDL.sql");
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    writer.write(resultString);
                }
            }

        }
    }
}