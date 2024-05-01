/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Course {
    Connection conn = null;
    MyDbConnector mdc = new MyDbConnector();
 
   
    
    /**
     *
     */
    public Course() {
        

        try {
            conn = mdc.getMyConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            
        }
    }

public ResultSet getcouse(int stuId, int year,int Semester) {
    String StudentFollowCourse = "SELECT * FROM student_course,course,student WHERE Student_id=? and Level=? and Semester=? and Student_id=Reg_no and course.Course_id=student_course.Course_id";
    ResultSet result = null;
    PreparedStatement ps = null;
    try {
        ps = conn.prepareStatement(StudentFollowCourse);
        ps.setInt(1, stuId);
        ps.setInt(2, year);
        ps.setInt(3,Semester);
        result = ps.executeQuery();
    } catch (SQLException e) {
        String message = "SQL Error: " + e.getMessage();
        System.out.println(message);
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    return result;
}



public ResultSet getcouse(int stuId) throws SQLException{

String getcouseidStuid="SELECT * FROM student_course,course WHERE Student_id=? and student_course.Course_id=course.Course_id";
ResultSet result=null;
PreparedStatement ps=null;

ps=conn.prepareStatement(getcouseidStuid);
ps.setInt(1,stuId);
result=ps.executeQuery();




return result;
}










public ResultSet getmetrical(String course_name,String week) {

    ResultSet result=null;
    PreparedStatement ps=null;
        try {
            String getmetialCouse="SELECT * FROM course_metrial WHERE Describition=? and week=? ";
            
            
            
            ps=conn.prepareStatement(getmetialCouse);
            ps.setString(1, course_name);
            System.out.println(course_name+week);
            ps.setString(2, week);
            result = ps.executeQuery();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
   return result;     
}



    
    
    /* public DefaultTableModel getcourseTableModel(int stuID, String code) {
        String sqlStudentMarks = "SELECT * FROM student_mark WHERE student_Reg_no = ? AND course_Course_id = ?";
        DefaultTableModel model = new DefaultTableModel();

        try (PreparedStatement pstmt = conn.prepareStatement(sqlStudentMarks)) {
            pstmt.setInt(1, stuID);
            pstmt.setString(2, code);
            try (ResultSet result = pstmt.executeQuery()) {
               
                ResultSetMetaData metaData = (ResultSetMetaData) result.getMetaData();
                int columnCount = metaData.getColumnCount();

                
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    model.addColumn(metaData.getColumnLabel(columnIndex));
                }

                
                while (result.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        rowData[i] = result.getObject(i + 1);
                    }
                    model.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return model;
    }
    */
    
    
    
    
    
    
    
    
    
    
    
    
    
}

