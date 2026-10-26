package br.com.fiap.guardioesdaluz.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = env("DB_URL", "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl");
    private static final String USER = env("DB_USER", null);
    private static final String PASSWORD = env("DB_PASSWORD", null);

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do Oracle nao encontrado no classpath", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null ? value : defaultValue;
    }

}
