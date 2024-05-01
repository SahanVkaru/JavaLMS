/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms_lecture;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DUVINDU
 */
public class DB_Connection {

    private String JDBC_URL = "jdbc:mysql://localhost:3306/teclms_new";
    private String USERNAME = "root";
    private String PASSWORD = "1234";

    public Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException ex) {
            System.out.println("JDBC Driver not found: " + ex.getMessage());
        } catch (SQLException es) {
            System.out.println("Failed to connect to the database: " + es.getMessage());
        }

        return conn;
    }

    /*public static void main(String[] args) {
        DB_Connection db_connection = new DB_Connection();
        Connection connection = db_connection.getConnection();

        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from the database.");
            } catch (SQLException e) {
                System.out.println("Failed to close the connection: " + e.getMessage());
            }
        }
    }*/
}
