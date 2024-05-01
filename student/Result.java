/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Genshin
 */
public class Result {
 
    Connection conn = null;
    MyDbConnector mdc = new MyDbConnector();

    
    
   
    
    /**
     *
     */
    public Result() {
        

        try {
            conn = mdc.getMyConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            
        }
    }

public ResultSet getResult(int Student_id,int year) {
    String AllResult = "SELECT * FROM student_mark,course,student WHERE student_Reg_no=? and Level=? and student_Reg_no=Reg_no and course_Course_id=Course_id";
    ResultSet result = null;
    PreparedStatement ps = null;
    try {
        ps = conn.prepareStatement(AllResult);
        ps.setInt(1, Student_id);
        ps.setInt(2,year);
        result = ps.executeQuery();
    } catch (SQLException e) {
        String message = "SQL Error: " + e.getMessage();
        System.out.println(message);
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    return result;
}
    
    
 
    
    
    
    
    
    
    
    
    
    
    
    
    
}
