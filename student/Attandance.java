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


/**
 *
 * @author Genshin
 */
public class Attandance {
 
    Connection conn = null;
    MyDbConnector mdc = new MyDbConnector();

    
    
   
    
    /**
     *
     */
    public Attandance() {
        

        try {
            conn = mdc.getMyConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            
        }
    }

/*public ResultSet getAttandanceAll(int Student_id) {
    String StudentAttanceAll = "SELECT * FROM attendance WHERE student_id=?";
    ResultSet result = null;
    PreparedStatement ps = null;
    try {
        ps = conn.prepareStatement(StudentAttanceAll);
        ps.setInt(1, Student_id);
        result = ps.executeQuery();
    } catch (SQLException e) {
        String message = "SQL Error: " + e.getMessage();
        System.out.println(message);
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    return result;
}
*/



public  ResultSet getSpecificAttandance(int Student_id,String course_id){
    
ResultSet result=null;
PreparedStatement ps=null;
        try {
            String SinglecouseAttandance="SELECT * FROM attendance WHERE student_id=? and course_code=?";
            
            
            ps=conn.prepareStatement(SinglecouseAttandance);
            ps.setInt(1, Student_id);
            ps.setString(2,course_id);
            result=ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Attandance.class.getName()).log(Level.SEVERE, null, ex);
        }
       



return result;


}







    
}