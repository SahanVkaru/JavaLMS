/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Genshin
 */
public class Medical {
 
    Connection conn = null;
    MyDbConnector mdc = new MyDbConnector();

    
    
   
    
    /**
     *
     */
    public Medical() {
        

        try {
            conn = mdc.getMyConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            
        }
    }

public ResultSet getMedical(int Student_id) {
    String medical = "SELECT * FROM medical WHERE student_Reg_no=?";
    ResultSet result = null;
    PreparedStatement ps = null;
    try {
        ps = conn.prepareStatement(medical);
        ps.setInt(1, Student_id);
        result = ps.executeQuery();
    } catch (SQLException e) {
        String message = "SQL Error: " + e.getMessage();
        System.out.println(message);
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    return result;
}
    

public ResultSet getMedical(int Student_id,String course_id,String date) {
    String medical = "SELECT * FROM medical WHERE student_Reg_no=? AND Course_id=? And date=?" ;
    ResultSet result = null;
    PreparedStatement ps = null;
    try {
        ps = conn.prepareStatement(medical);
        ps.setInt(1, Student_id);
        ps.setString(2,course_id);
        ps.setString(3,date);
        result = ps.executeQuery();
    } catch (SQLException e) {
        String message = "SQL Error: " + e.getMessage();
        System.out.println(message);
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    return result;
}









}