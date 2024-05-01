/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package admingui;

/**
 *
 * @author lakin
 */
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;
import java.sql.DriverManager;

public class Admin {
    // Database connection 
    private static final String DB_URL = "jdbc:mysql://localhost:3306/teclms_new";
    private static final String USERNAME = "laki";
    private static final String PASSWORD = "";

    // Method to create a user profile
    public void createProfile(String username, String password, String id, String name, String address, String country, String position, String telephone,  String prof, String dep) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
             int userId = Integer.parseInt(id.substring(2)); // Remove first two characters from username
         String tableName = null;
        if (id.startsWith("TG")) {
            tableName = "student";
        } else if (id.startsWith("TE")) {
            tableName = "technicalofficer";
        } else if (id.startsWith("LE")) {
            tableName = "lecture";
        } 
        
            String sql1;
            String sql2;
            sql1 = "INSERT INTO" +tableName+ "(staff_no,Password, name, address, country, position, telephone, profile, department_name) VALUES ('" +userId+ "', '" +password+ "','" +name+ "','"+address+"','"+position+"','"+telephone+"','"+prof+"','"+dep+"')";
            //sql2 = "INSERT INTO login VALUES ('"+username+"', '"+password+"', '"+position+"')";
            stmt.executeUpdate(sql1);
            //stmt.executeUpdate(sql2);
            System.out.println("User profile created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to maintain a user profile
public void maintainProfile(String username, String password, String id, String name, String address, String country, String position, String telephone, String prof, String dep) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
         Statement stmt = conn.createStatement()) {
        // Update login table
        //String loginUpdateSql = "UPDATE login SET password = '" + password + "' WHERE username = '" + username + "'";
        //int loginRowsAffected = stmt.executeUpdate(loginUpdateSql);
        
        int userId = Integer.parseInt(id.substring(2)); // Remove first two characters from username
        // Determine the table name based on the prefix of the id
        String tableName;
        if (id.startsWith("TG")) {
            tableName = "student";
        } else if (id.startsWith("TE")) {
            tableName = "technicalofficer";
        } else if (id.startsWith("LE")) {
            tableName = "lecture";
        } else {
            System.out.println("Invalid id.");
            return; // Exit method if id is not recognized
        }

        // Construct the SQL update query for the profile table
        String profileUpdateSql = "UPDATE " + tableName + " SET staff_no = '" + userId + "','"+password+"', name = '" + name + "', address = '" + address + "', country = '" + country + "', position = '" + position + "', telephone = '" + telephone + "', prof = '" + prof + "', dep = '" + dep + "' WHERE username = '" + userId + "'";

        // Execute the SQL update query for the profile table
        int profileRowsAffected = stmt.executeUpdate(profileUpdateSql);

        // Check if any rows were affected in both login and profile tables
        if (profileRowsAffected > 0) {
            System.out.println("User profile updated successfully.");
        } else {
            System.out.println("User profile or login information not found.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



// Method to create a course
public void createCourse(String Id, String Name, int nos, String dep, int hours, String imagePath,int credits, int Lecture_Incharge) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
         Statement stmt = conn.createStatement()) {
        // Prepare the SQL statement
        String sql = "INSERT INTO Course (Course_id, Name, Number_Of_Student, Offered_department, Hours, Image,Credits,Lecture_Incharge) " +
                     "VALUES (?, ?, ?, ?, ?, ? ,?,?)";

        // Prepare the SQL statement with parameters
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, Id);
        pstmt.setString(2, Name);
        pstmt.setInt(3, nos);
        pstmt.setString(4, dep);
        pstmt.setInt(5, hours);
        pstmt.setString(6, imagePath); 
        pstmt.setInt(7,credits);
        pstmt.setInt(8,Lecture_Incharge);
// Set the image file path

        // Execute the SQL statement
        pstmt.executeUpdate();
        System.out.println("Course created successfully.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


// Method to maintain a course
public void maintainCourse(String Id, String Name, int nos, String dep, int hours, String image) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
         Statement stmt = conn.createStatement()) {
        // Prepare the SQL statement
        String sql = "UPDATE Course SET Name = ?, Number_Of_Student = ?, Offered_department = ?, Hours = ?, Image = ? WHERE Course_id = ?";

        // Prepare the SQL statement with parameters
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, Name);
        pstmt.setInt(2, nos);
        pstmt.setString(3, dep);
        pstmt.setInt(4, hours);
        pstmt.setString(5, image);
        pstmt.setString(6, Id);

        // Execute the SQL statement
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Course updated successfully.");
        } else {
            System.out.println("Course not found.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


/*// Method to maintain a course
public void maintainCourse(String Id, String Name, int nos, String dep, int hours, Image image) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
        // Prepare the SQL statement
        String sql = "UPDATE Course SET Name = ?, Number_Of_Student = ?, Offered_department = ?, Hours = ?, Image = ? WHERE Course_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, Name);
        pstmt.setInt(2, nos);
        pstmt.setString(3, dep);
        pstmt.setInt(4, hours);

        // Convert the image to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write((BufferedImage) image, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        pstmt.setBinaryStream(5, bais, imageBytes.length);

        pstmt.setString(6, Id);

        // Execute the update statement
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Course updated successfully.");
        } else {
            System.out.println("Course not found.");
        }
    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
}
*/  

 // Method to create a notice
    public void createNotice(String name, String details, String filepath) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            // Get the current date and time
            LocalDateTime currentTime = LocalDateTime.now();
            
            // Convert LocalDateTime to string representation
            String formattedDateTime = currentTime.toString();
            
            String sql = "INSERT INTO Notice (title, details, date_time, documents) " +
                         "VALUES ('" + name + "', '" + details + "', '" + formattedDateTime + "', '" + filepath + "')";
            stmt.executeUpdate(sql);
            System.out.println("Notice created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //maintain notice
    public void maintainNotice(int noticeId, String newName, String newDetails, String newFilepath) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            // Check if the notice exists
            String checkSql = "SELECT * FROM Notice WHERE notice_id = " + noticeId;
            if (stmt.executeQuery(checkSql).next()) {
                // Notice exists, perform update
                String updateSql = "UPDATE Notice SET title = '" + newName + "', details = '" + newDetails + "', " +
                                   "documents = '" + newFilepath + "' WHERE notice_id = " + noticeId;
                int rowsAffected = stmt.executeUpdate(updateSql);
                if (rowsAffected > 0) {
                    System.out.println("Notice updated successfully.");
                } else {
                    System.out.println("Failed to update notice.");
                }
            } else {
                System.out.println("Notice not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //Delete notice method if need 
    
    
    
      // Method to create a timetable
    public void createTimetable(String ID, int semester, int year, String time, String activity, String Day, int batch) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO Timetable (course_id, semester, year, start_time, end_time, day, batch) " +
                         "VALUES ('" + ID + "', " + semester + ", " + year + ", '" + time + "', '" + activity + "', '" + Day + "', " + batch + ")";
            stmt.executeUpdate(sql);
            System.out.println("Timetable created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method to maintain a timetable function
    public void maintainTimetable(String ID, int semester, int year, String time, String activity, String Day, int batch) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            // Check if the timetable entry exists
            String checkSql = "SELECT * FROM Timetable WHERE course_id = '" + ID + "'";
            if (stmt.executeQuery(checkSql).next()) {
                // Timetable entry exists, update the entry
                String updateSql = "UPDATE Timetable SET semester = " + semester + ", year = " + year + 
                                    ", start_time = '" + time + "', end_time = '" + activity + 
                                    "', day = '" + Day + "', batch = " + batch + " WHERE course_id = '" + ID + "'";
                int rowsAffected = stmt.executeUpdate(updateSql);
                if (rowsAffected > 0) {
                    System.out.println("Timetable updated successfully.");
                } else {
                    System.out.println("Failed to update timetable.");
                }
            } else {
                System.out.println("Timetable entry not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method to create a student profile
public void createStudentProfile(String regNo, String password, String address, String name, String email, int level, String departmentName, String profilePicture, String contactNo, int age, String country, int semester) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
         Statement stmt = conn.createStatement()) {
        // Remove the first two characters from the regNo to extract the integer part
        int userId = Integer.parseInt(regNo.substring(2));
        
        // Construct the SQL insert query for the student table
        String sql = "INSERT INTO Student (Reg_no, password, Address, Name, Email, Level, department_name, profile_picture, Contact_No, Age, Country, Semester) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Prepare the SQL statement with placeholders for parameters
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        pstmt.setString(2, password);
        pstmt.setString(3, address);
        pstmt.setString(4, name);
        pstmt.setString(5, email);
        pstmt.setInt(6, level);
        pstmt.setString(7, departmentName);
        pstmt.setString(8, profilePicture);
        pstmt.setString(9, contactNo);
        pstmt.setInt(10, age);
        pstmt.setString(11, country);
        pstmt.setInt(12, semester);
        
        // Execute the SQL insert statement
        pstmt.executeUpdate();
        
        System.out.println("Student profile created successfully.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// Method to maintain a student profile
public void maintainStudentProfile(String regNo, String password, String address, String name, String email, int level, String departmentName, String profilePicture, String contactNo, int age, String country, int semester) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
         Statement stmt = conn.createStatement()) {
        // Remove the first two characters from the regNo to extract the integer part
        int userId = Integer.parseInt(regNo.substring(2));
        
        // Construct the SQL update query for the student table
        String sql = "UPDATE Student SET password = ?, Address = ?, Name = ?, Email = ?, Level = ?, department_name = ?, profile_picture = ?, Contact_No = ?, Age = ?, Country = ?, Semester = ? " +
                     "WHERE Reg_no = ?";

        // Prepare the SQL statement with placeholders for parameters
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, password);
        pstmt.setString(2, address);
        pstmt.setString(3, name);
        pstmt.setString(4, email);
        pstmt.setInt(5, level);
        pstmt.setString(6, departmentName);
        pstmt.setString(7, profilePicture);
        pstmt.setString(8, contactNo);
        pstmt.setInt(9, age);
        pstmt.setString(10, country);
        pstmt.setInt(11, semester);
        pstmt.setInt(12, userId);
        
        // Execute the SQL update statement
        int rowsAffected = pstmt.executeUpdate();
        
        if (rowsAffected > 0) {
            System.out.println("Student profile updated successfully.");
        } else {
            System.out.println("Student profile not found.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public static void main(String[] args) {
        Admin admin = new Admin();

        //Example usage
        //admin.createProfile("john_doe", "Admin", "country", "position", "telephone", "general", "telephone", prof, dep, dep);
        //admin.createCourse("ICT2124", "Database Management System", "ICT", dep, 60, telephone, image);
        //admin.createNotice("Important Announcement", "This is a notice.", "2024-04-18 10:00:00");
        //admin.createTimetable("ICT2124", 1, 2024, "Monday", "09:00", "11:00", 6);
    }
}
