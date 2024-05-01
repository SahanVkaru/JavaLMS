/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package student;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Genshin
 */
public class Student {

    /**
     * @param args the command line arguments
     * 
     * 
     */
    Connection conn = null;
    MyDbConnector mdc=new MyDbConnector();
    
    
    public ResultSet getmarkgrade(int stuid,String couse_id) throws SQLException{
        
        
        ResultSet result=null;
        
        try {
            conn=mdc.getMyConnection();
            PreparedStatement ps=null;
            
            String marksgrade="Select * from student_mark where student_Reg_no=? course_Course_id=?";
            
            ps=conn.prepareStatement(marksgrade);
            ps.setInt(1, stuid);
            ps.setString(2,couse_id);
            
            result=ps.executeQuery();            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    return result;
    
    }
    
  
    
    public double calculategrade(int stuid) throws ClassNotFoundException, SQLException{
    
        double point = 1.0;
        double totalpoint=1.0;
        ResultSet result=null;
       
        try {
            
            PreparedStatement ps=null;
            conn=mdc.getMyConnection();
            String selectsql="SELECT * FROM student_mark, course WHERE student_mark.student_Reg_no=? AND course.Course_id=student_mark.course_Course_id";
            ps=conn.prepareStatement(selectsql);
            ps.setInt(1,stuid);
            result=ps.executeQuery(); 
            
         while (result.next()) {
    String grade = result.getString("Grade");
    double couse_credit=result.getDouble("Credits");
    
    if (null == grade) {
        point= 0.0;
    } else {
switch (grade) {
    case "A+" -> totalpoint += couse_credit * 4.3;
    case "A" -> totalpoint += couse_credit * 4.0;
    case "A-" -> totalpoint += couse_credit * 3.7;
    case "B+" -> totalpoint += couse_credit * 3.3;
    case "B" -> totalpoint += couse_credit * 3.0;
    case "B-" -> totalpoint += couse_credit * 2.7;
    case "C+" -> totalpoint += couse_credit * 2.3;
    case "C" -> totalpoint += couse_credit * 2.0;
    case "C-" -> totalpoint += couse_credit * 1.7;
    case "D+" -> totalpoint += couse_credit * 1.3;
    case "D" -> totalpoint += couse_credit * 1.0;
    case "F" -> totalpoint += couse_credit * 0.0;
    default -> totalpoint += couse_credit * 0.0;
}
    }
}
   
            
            
          
            
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }

    return totalpoint;  
    }
    
    
    
    
    
    
    public int calulatecousecradit(int stuid) throws SQLException, ClassNotFoundException{
    
     int totalcredit = 0;
     int credit=0;
     ResultSet result=null;
    
     conn=mdc.getMyConnection();
        //System.out.println(stuid);
         
            String selectsql = "SELECT * FROM student_course, course WHERE student_course.Student_id=? AND course.Course_id=student_course.Course_id";   
            PreparedStatement ps=null;
            conn=mdc.getMyConnection();
            ps=conn.prepareStatement(selectsql);
            ps.setInt(1,stuid);
            result=ps.executeQuery(); 
            
            
            
            
            while (result.next()){
     
                credit=result.getInt("Credits");
                
                totalcredit=totalcredit+credit;
  
            }
    return totalcredit;
    }
    
    
    
    
    
    
    
    
    public double studentgpa(double grade,int totalcredit){
    
    double  studentgpa=grade/totalcredit;
   
    return studentgpa;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
        
    
    
    
    
    
    
    
    
  
    
}
