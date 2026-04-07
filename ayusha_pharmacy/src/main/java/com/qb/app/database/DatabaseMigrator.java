package com.qb.app.database;

import org.flywaydb.core.Flyway;


public class DatabaseMigrator {

    public static void migrate(String url, String user, String password) {

        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .locations("classpath:db/migration")
                .load();

        flyway.migrate();
    }
}
