package lms_lecture;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.Time;
import lms_lecture.DB_Connection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DUVINDU
 */
public class TimetableEntry {

    DB_Connection connection = new DB_Connection();
    Connection conn = connection.getConnection();
    private String courseCourseId;
    private String semester;
    private int year;
    private Time time;
    private String activity;
    private String day;
    private int batch;

    public TimetableEntry() {
    }

    public TimetableEntry(String courseCourseId, String semester, int year, Time time, String activity, String day, int batch) {
        this.courseCourseId = courseCourseId;
        this.semester = semester;
        this.year = year;
        this.time = time;
        this.activity = activity;
        this.day = day;
        this.batch = batch;
    }

    // Getters and setters
    public String getCourseCourseId() {
        return courseCourseId;
    }

    public void setCourseCourseId(String courseCourseId) {
        this.courseCourseId = courseCourseId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public ResultSet getAllsubjects() {
        String sql = "SELECT * FROM timetable ";
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
}
