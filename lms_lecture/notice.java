/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms_lecture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author DUVINDU
 */
public class notice {
    DB_Connection connection = new DB_Connection();
    java.sql.Connection conn = connection.getConnection();
        ResultSet getAllnotice() {

        String sql = "SELECT * FROM notice";
        ResultSet result = null;
        try {
            Statement stmt = conn.createStatement();

            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("");
        }
        return result;
    }
}
