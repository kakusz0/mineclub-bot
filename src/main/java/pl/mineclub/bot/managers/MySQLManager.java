package pl.mineclub.bot.managers;


import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public class MySQLManager {

    private final HikariDataSource hikariDataSource;

    public MySQLManager(String host, int port, String user, String password, String database) {
        this.hikariDataSource = new HikariDataSource();
        this.hikariDataSource.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        this.hikariDataSource.setUsername(user);
        if (!password.isEmpty()) {
            this.hikariDataSource.setPassword(password);
        }

        this.hikariDataSource.addDataSourceProperty("cachePrepStmts", true);
        this.hikariDataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        this.hikariDataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        this.hikariDataSource.addDataSourceProperty("useServerPrepStmts", true);
        this.hikariDataSource.addDataSourceProperty("rewriteBatchedStatements", true);
        //this.hikariDataSource.setConnectionTimeout(15000L);
        this.hikariDataSource.setMaximumPoolSize(5);
        this.hikariDataSource.setKeepaliveTime(1000L);
        this.initializeDatabase();
    }

    private void initializeDatabase() {
        String createTableQuery = """
                CREATE TABLE IF NOT EXISTS `mineclub_bot` (
                    `number` BIGINT NOT NULL PRIMARY KEY
                );
            """;

        try (Connection connection = this.hikariDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(createTableQuery)) {

            // Tworzenie tabeli, jeśli nie istnieje
            statement.executeUpdate();

            // Dodawanie kolumny, jeśli nie istnieje
            addColumnIfNotExists(connection, "mineclub_bot", "number", "BIGINT NOT NULL");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void addColumnIfNotExists(Connection connection, String tableName, String columnName, String columnDefinition) {
        try {
            if (!columnExists(connection, tableName, columnName)) {
                String addColumnQuery = "ALTER TABLE `" + tableName + "` "
                        + "ADD COLUMN `" + columnName + "` " + columnDefinition;
                try (PreparedStatement statement = connection.prepareStatement(addColumnQuery)) {
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean columnExists(Connection connection, String tableName, String columnName) {
        try {

            String query = "SHOW COLUMNS FROM `" + tableName + "` LIKE ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, columnName);
                return statement.executeQuery().next();
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }

    public void disconnectDatabase() {
        hikariDataSource.close();
    }

    public ResultSet executeQuery(String query) {
        try (Connection connection = this.hikariDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void executeUpdate(String query) {
        try (Connection connection = this.hikariDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            if (statement == null) {
                return;
            }
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}