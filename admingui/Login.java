/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package admingui;

/**
 *
 * @author lakin
 */
//import static com.mysql.cj.conf.PropertyKey.PASSWORD;
import java.sql.*;
import javax.swing.JPasswordField;

public class Login {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/teclms_new";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";
    private static int userId;
    private static String table = "";

    // Method to handle login
    public static String login(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); Statement stmt = conn.createStatement()) {
            String role = null;
            userId = Integer.parseInt(username.substring(2)); // Remove first two characters from username
            table = "";
            String sql = "";
            if (username.startsWith("TG")) {
                table = "student";
                sql = "SELECT password FROM " + table + " WHERE Reg_no = " + userId;
            } else if (username.startsWith("TE")) {
                table = "technicalofficer";
                sql = "SELECT password FROM " + table + " WHERE staff_no = " + userId;
            } else if (username.startsWith("LE")) {
                table = "lecture";
                sql = "SELECT password FROM " + table + " WHERE staff_no = " + userId;
            } else if (username.startsWith("AD")) {
                table = "admin";
                sql = "SELECT password FROM " + table + " WHERE staff_no = " + userId;
            } else {
                // Invalid username format

                System.out.println("Invalid username format.");
                return null;
            }
            // Check respective table for username and password match

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if (dbPassword.equals(password)) {
                    // Password match, return "Success"
                    return "Success";
                }
            }
            // Password mismatch or user not found
            return "Failed";
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return null; // Return null if an exception occurred
        }
    }

    public int getUid() {

        return userId;
    }

    public String getTable() {

        return table;
    }

}
