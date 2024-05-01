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
public class TimeTable {
 
    Connection conn = null;
    MyDbConnector mdc = new MyDbConnector();
   // StudentMenu student_no=new StudentMenu();
   // int student_id=student_no.stuId;
    
    
   
    
    /**
     *
     */
    public TimeTable() {
        

        try {
            conn = mdc.getMyConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            
        }
    }

public ResultSet getTimeTable(int Student_id) {
    String Timetable = "SELECT * FROM timetable,student_course where student_course.Course_id=timetable.course_Course_id AND Student_id=?";
    ResultSet result = null;
    PreparedStatement ps = null;
    try {
        ps = conn.prepareStatement(Timetable);
        ps.setInt(1, Student_id);
        result = ps.executeQuery();
    } catch (SQLException e) {
        String message = "SQL Error: " + e.getMessage();
        System.out.println(message);
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    return result;
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
