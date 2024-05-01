/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms_lecture;

/**
 *
 * @author DUVINDU
 */
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Attendance {

    DB_Connection connection = new DB_Connection();
    Connection conn = connection.getConnection();
    private int studentId;
    private String courseCode;
    private String date;
    private int hours;
    private int lid;
    private String status;

    public Attendance() {
    }

    public Attendance(int studentId, String courseCode, String date, int hours, int lid, String status) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.date = date;
        this.hours = hours;
        this.lid = lid;
        this.status = status;
    }

    // Getters and setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResultSet getAllAttendances() {
        String sql = "SELECT * FROM attendance ";
        ResultSet result = null;
        try {
            Statement stmt = conn.createStatement();
            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    public ResultSet getAttendances() {
        String sql = "SELECT * FROM attendance ";
        ResultSet result = null;
        try {
            Statement stmt = conn.createStatement();
            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    ResultSet searchStudentByID(String str) {

        String sql = "SELECT * FROM attendance WHERE student_id LIKE ? ";
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

    public int getpresentAttendanceCount(int studentId, String courseCode) {
        int attendanceCount = 0;
        String sql = "SELECT COUNT(*) AS attendance_count FROM attendance WHERE student_id = ? AND course_code = ? AND status = 'present'";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setString(2, courseCode);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                attendanceCount = rs.getInt("attendance_count");
            }

        } catch (SQLException ex) {
            String message = "SQL Error: " + ex.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        return attendanceCount;
    }

    public int getALLAttendanceCount(int studentId, String courseCode) {
        int attendanceCount = 0;
        String sql = "SELECT COUNT(*) AS attendance_count FROM attendance WHERE student_id = ? AND course_code = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setString(2, courseCode);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                attendanceCount = rs.getInt("attendance_count");
            }

        } catch (SQLException ex) {
            String message = "SQL Error: " + ex.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        return attendanceCount;
    }

}
