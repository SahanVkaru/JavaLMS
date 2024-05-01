package lms_lecture;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DUVINDU
 *
 */
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Student {

    DB_Connection connection = new DB_Connection();

    java.sql.Connection conn = connection.getConnection();

    ResultSet getAllstudent() {

        String sql = "SELECT * FROM student";
        ResultSet result = null;
        try {
            Statement stmt = conn.createStatement();

            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("");
        }
        return result;
    }
       ResultSet getstudentById(int ID) {

        String sql = "SELECT * FROM student WHERE Reg_no = ? ";
        ResultSet result = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ID);
            result = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("");
        }
        return result;
    }

    ResultSet getETstudent() {

        String sql = "SELECT * FROM student WHERE department_name ='ET' ";
        ResultSet result = null;
        try {
            Statement stmt = conn.createStatement();

            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("");
        }
        return result;
    }

    ResultSet getBSTstudent() {

        String sql = "SELECT * FROM student WHERE department_name ='BST' ";
        ResultSet result = null;
        try {
            Statement stmt = conn.createStatement();

            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("");
        }
        return result;
    }

    ResultSet getICTstudent() {

        String sql = "SELECT * FROM student WHERE department_name ='ICT' ";
        ResultSet result = null;
        try {
            Statement stmt = conn.createStatement();

            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("");
        }
        return result;
    }

    ResultSet searchStudentByName(String str) {

        String sql = "SELECT * FROM student WHERE Name LIKE ? ";
        ResultSet result = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + str + "%");

            result = pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println("");
        }
        return result;
    }

    ResultSet getLecturerCourseStudents(String course_id) {
        CourseList cl = new CourseList();

        String sqlStudent_course = "SELECT * FROM student_course WHERE Course_id = ?";
        String sqlALLStudent_course = "SELECT * FROM student_course";

        ResultSet StuCoursesList = null;
        PreparedStatement pstmt = null;
        try {
            if ("".equals(course_id)) {
                pstmt = conn.prepareStatement(sqlALLStudent_course);

            } else {
                pstmt = conn.prepareStatement(sqlStudent_course);
                pstmt.setString(1, course_id);
            }

            StuCoursesList = pstmt.executeQuery();

        } catch (SQLException e) {
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return StuCoursesList;
    }

    ResultSet getStudentMarks() {
        String sqlStudent_marks = "SELECT c.*,m.Quizze01,m.Quizze02,m.Quizze03, m.Mid_term , m.final_tyeory ,m.final_practical, m.assesment,m.final_marks FROM student_course c LEFT JOIN student_mark m ON m.student_Reg_no = c.Student_id AND m.course_Course_id = c.Course_id order by c.Student_id";
        ResultSet result = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sqlStudent_marks);
            result = pstmt.executeQuery();

        } catch (SQLException e) {
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    ResultSet getStudentMarksWithALLStudents(String code) {
        String sqlStudent_marks = "SELECT c.*,m.Quizze01,m.Quizze02,m.Quizze03 , m.Mid_term , m.final_tyeory ,m.final_practical, m.assesment,m.final_marks FROM student_course c LEFT JOIN student_mark m ON m.student_Reg_no = c.Student_id AND m.course_Course_id = c.Course_id where c.Course_id = ? order by c.Student_id";
        ResultSet result = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sqlStudent_marks);
            pstmt.setString(1, code);
            result = pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println("");
        }
        return result;
    }

    ResultSet getStudentMarks(int stuID, String code) {
        String sqlStudent_marks = "SELECT * FROM student_mark where student_Reg_no = ? AND course_Course_id = ?";
        ResultSet result = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sqlStudent_marks);
            pstmt.setInt(1, stuID);
            pstmt.setString(2, code);
            result = pstmt.executeQuery();

        } catch (SQLException e) {
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    public boolean insertStudent_mark(String EditedColName, int stuNum, String CId, int EditedColvalue) {
        boolean updated = false;
        String sqlStudent_marks = "insert into student_mark (student_Reg_no,course_Course_id," + EditedColName + ") values(?,?,?);";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sqlStudent_marks);
            pstmt.setInt(1, stuNum);
            pstmt.setString(2, CId);
            pstmt.setInt(3, EditedColvalue);
            int rowsAffected = pstmt.executeUpdate();
            updated = rowsAffected > 0;

        } catch (SQLException e) {
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return updated;

    }

    public boolean updateStudent_mark(Object[] rowData, DefaultTableModel model) {
        boolean updated = false;
        StringBuilder sql = new StringBuilder("UPDATE student_mark SET ");
        System.out.println(rowData.length);
        for (int i = 0; i < rowData.length - 2; i++) {
            //System.out.println("i" + i + " - name" + model.getColumnName(i + 2));
            sql.append(model.getColumnName(i + 2));
            sql.append(" = ?, ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE student_Reg_no = ? AND course_Course_id = ?");
        System.out.println(sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < rowData.length - 2; i++) {
                Object value = rowData[i + 2];
                System.out.println(value);
                if (value == null || "".equals(value.toString())) {
                    System.out.println(model.getColumnName(i + 2)+"object is null");
                    pstmt.setObject(i + 1, null);
                } /*else if(i == rowData.length - 3){
                    System.out.println("This is Grade");
                }*/
                else {
                    System.out.println(model.getColumnName(i + 2)+"object is NOT null");
                    pstmt.setObject(i + 1, value);
                }
            }
            int studentID = Integer.parseInt(rowData[0].toString());
            String courseID = rowData[1].toString();
            pstmt.setInt(rowData.length - 2 + 1, studentID);
            pstmt.setString(rowData.length - 2 + 2, courseID);
            System.out.println(sql);
            int rowsAffected = pstmt.executeUpdate();

            updated = rowsAffected > 0;
        } catch (SQLException ex) {

            String message = "SQL Error: " + ex.getMessage();
            System.out.println(message+"test");
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);

        }
        return updated;
    }

}
