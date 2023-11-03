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
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/BeeSmart", //
                    "root", //
                    "beesmart");

//            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3310/beesmart", //
//                    "root", //
//                    "Cedup");
        }
        return connection;
    }
}
