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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class Lecture {

    DB_Connection connection = new DB_Connection();
    Connection conn = connection.getConnection();
    private int staffNo;
    private String name;
    private String address;
    private String country;
    private String position;
    private int telephone;
    private String profile;
    private String departmentName;

    public Lecture() {
    }

    public Lecture(int staffNo, String name, String address, String country, String position, int telephone, String profile, String departmentName) {
        this.staffNo = staffNo;
        this.name = name;
        this.address = address;
        this.country = country;
        this.position = position;
        this.telephone = telephone;
        this.profile = profile;
        this.departmentName = departmentName;
    }

    // getters and setters
    public int getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(int staffNo) {
        this.staffNo = staffNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public ResultSet getAllLecturers() {
        String sql = "SELECT * FROM lecture";
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

    public Lecture getLectureData(int staffNo) {
        Lecture lecture = null;
        String query = "SELECT * FROM lecture WHERE staff_no = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, staffNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("Name");
                    String address = rs.getString("address");
                    String country = rs.getString("country");
                    String position = rs.getString("position");
                    int telephone = rs.getInt("telephone");
                    String profile = rs.getString("profile");
                    String departmentName = rs.getString("department_name");

                    lecture = new Lecture(staffNo, name, address, country, position, telephone, profile, departmentName);
                }
            }
        } catch (SQLException e) {
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return lecture;
    }

    public boolean updateLectureData(Lecture lecture) {
        String query = "UPDATE lecture SET Name = ?, address = ?, country = ?, position = ?, telephone = ?, profile = ?, department_name = ? WHERE staff_no = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, lecture.getName());
            pstmt.setString(2, lecture.getAddress());
            pstmt.setString(3, lecture.getCountry());
            pstmt.setString(4, lecture.getPosition());
            pstmt.setInt(5, lecture.getTelephone());
            pstmt.setString(6, lecture.getProfile());
            pstmt.setString(7, lecture.getDepartmentName());
            pstmt.setInt(8, lecture.getStaffNo());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void display() {
        System.out.println("Staff Number: " + staffNo);
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Country: " + country);
        System.out.println("Position: " + position);
        System.out.println("Telephone: " + telephone);
        System.out.println("Profile: " + profile);
        System.out.println("Department Name: " + departmentName);
    }

}
