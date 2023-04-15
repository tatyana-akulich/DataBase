package by.teachmeskills.db.client;

import by.teachmeskills.db.util.PropertiesLoader;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.List;
import java.util.Properties;

@Log4j2
public class DBClient {

    protected Connection connection = null;
    protected Statement statement = null;

    public void connect() {
        Properties properties = PropertiesLoader.loadProperties();
        try {
            connection = DriverManager.getConnection(properties.getProperty("DB_URL"), properties.getProperty("user"),
                    properties.getProperty("password"));
            statement = connection.createStatement();
        } catch (SQLException exception) {
            log.error(exception.getMessage());
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            return statement.executeQuery(query);
        } catch (SQLException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }

    public void executeUpdate(String query) {
        try {
            int i = statement.executeUpdate(query);
            log.info("Rows affected count: " + i);
        } catch (SQLException exception) {
            log.error(exception.getMessage());
        }
    }

    public void deleteRows(String tableName, String condition) {
        executeUpdate(String.format("DELETE FROM %s WHERE %s", tableName, condition));
    }

    public void updateRows(String tableName, String newValues, String condition) {
        executeUpdate(String.format("UPDATE %s SET %s WHERE %s", tableName, newValues, condition));
    }

    public ResultSet selectAllRows(String tableName) {
        return executeQuery(String.format("SELECT * FROM %s;", tableName));
    }

    public void close() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            log.error(exception.getMessage());
        }
    }
}