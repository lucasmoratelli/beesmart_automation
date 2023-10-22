package com.pi.beesmart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton {

    private static Connection connection;

    private ConnectionSingleton() {
        //singleton class
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/beesmart", //
                    "root", //
                    "beesmart");
        }
        return connection;
    }
}
