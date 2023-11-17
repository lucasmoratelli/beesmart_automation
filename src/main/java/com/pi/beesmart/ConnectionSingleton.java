package com.pi.beesmart;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Component
@Scope("singleton")
public class ConnectionSingleton {

    private static Connection connection;

    public Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/beesmart", //
                    "root", //
                    "beesmart");
        }
        return connection;
    }
}
