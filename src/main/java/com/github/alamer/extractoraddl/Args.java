package com.github.alamer.extractoraddl;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = "-tables", description = "Comma-separated list of table names to be extract DDL")
    private String tableNames;

    @Parameter(names = "-views", description = "Comma-separated list of view names to be extract DDL")
    private String viewNames;

    @Parameter(names = "-config", description = "Configuration file")
    private String config;

    public String getTableNames() {
        return tableNames;
    }

    public String getConfig() {
        return config;
    }

    public String getViewNames() {
        return viewNames;
    }
}
