/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms_lecture;

import com.mysql.cj.protocol.Resultset;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.*;
import java.awt.*;
import admingui.LOGIN_Frame;
/**
 *
 * @author DUVINDU
 */
public class Lecturer_UI extends javax.swing.JFrame {

    public static int Lecturer_ID;

    Lecture newLecture = null;
    public int ispicaddedtoprfile = 0;
    public String ImgURL = "";
    Map<String, Object> previousCellValues = new HashMap<>();
    DefaultListModel<String> modelForListofContent = new DefaultListModel<>();
    DefaultListModel<String> modelForListofMetrial = new DefaultListModel<>();

    public String Selected_CourseCode, User_ID /*Selected_week*/;
    DefaultTableModel studentList = new DefaultTableModel();
    DefaultTableModel marksList = new DefaultTableModel();
    DefaultTableModel noticeList = new DefaultTableModel();

    DB_Connection connection = new DB_Connection();
    public java.sql.Connection conn = connection.getConnection();

    void setLayoutAndImg(JPanel jPanel) {
        Icon image = new ImageIcon(ImgURL);

        OverJLabel overJLabel = new OverJLabel(0, 0.5, 200, 200, image);
        jPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        overJLabel.setPreferredSize(new Dimension(200, 200));

        File file = new File(ImgURL);
        if (file.exists()) {
            if (ispicaddedtoprfile < 3) {
                jPanel.add(overJLabel);
                jPanel.revalidate();
                ispicaddedtoprfile = ispicaddedtoprfile + 1;
            }

        } else {

            System.out.println("Image file not found!");
        }

    }

    void refreshImages() {
        jPanel4.removeAll();
        //jPanel15.removeAll();
        jPanel16.removeAll();
        ispicaddedtoprfile = 0;

        //reload Image for jpanel14
        setLayoutAndImg(jPanel4);
        //reload Image for jpanel15
        //setLayoutAndImg(jPanel15);
        //reload Image for jpanel14
        setLayoutAndImg(jPanel16);

    }

    public void updateMarkeTable() {
        Student student = new Student();
        int rowCount = marksList.getRowCount();
        int colCount = marksList.getColumnCount();
        //System.out.println(colCount);
        boolean flag = false;
        String key;
        if (jComboBox3.getSelectedItem() == "ALL") {
            JOptionPane.showMessageDialog(null, "You Can't Enter Marks For All Courses Select a Course First", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            for (int row = 0; row < rowCount; row++) {

                for (int col = 0; col < colCount; col++) {
                    key = row + "-" + col;
                    Object curruntCellValue = marksList.getValueAt(row, col);
                    Object PreviousCellValue = previousCellValues.get(key);
                    if (curruntCellValue == null) {
                        curruntCellValue = -1;
                    }
                    //System.out.println(key);
                    //System.out.println(PreviousCellValue + " , " + curruntCellValue);
                    if (!curruntCellValue.equals(PreviousCellValue)) {
                        //System.out.println(PreviousCellValue + " , " + curruntCellValue+"row , col "+row+"-"+col);
                        int studentID = Integer.parseInt(marksList.getValueAt(row, 0).toString());
                        String courseID = marksList.getValueAt(row, 1).toString();
                        //System.out.println(courseID);
                        courseID = courseID.replace("'", "");

                        //System.out.println("studentID"+studentID+"courseID"+courseID);
                        String columnName = marksList.getColumnName(col);
                        ResultSet resultSet = student.getStudentMarks(studentID, courseID);
                        //System.out.println(courseID);
                        try {
                            if (!resultSet.next()) {
                                flag = student.insertStudent_mark(columnName, studentID, courseID, Integer.parseInt(curruntCellValue.toString()));
                            } else {
                                Object[] rowData = new Object[colCount - 1];
                                for (int i = 0; i < colCount - 1; i++) {
                                    System.out.println("marksList.getValueAt(row, i) - " + marksList.getValueAt(row, i));
                                    rowData[i] = marksList.getValueAt(row, i);
                                    //System.out.println("here");
                                }
                                /*System.out.println("************************------------");
                                for (Object rows : rowData) {
                                    System.out.println(rows);
                                }
                                System.out.println("*********************------------");*/

                                flag = student.updateStudent_mark(rowData, marksList);
                                break;
                            }
                        } catch (SQLException ex) {
                            String message = "SQL Error: " + ex.getMessage();
                            System.out.println(message + "test2");
                            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }
            }
            if (flag) {
                JOptionPane.showMessageDialog(null, "updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            jTable2.setModel(marksList);

        }
    }

    public void setPanel(String cardName) {
        CardLayout CL = (CardLayout) jPanel2.getLayout();
        CL.show(jPanel2, cardName);
    }

    //UPDATE THE dynamic Values in Course detils
    public void updateCourseDetails(String code, String name) {

        jLabel7.setText(code + "-" + name);
        Selected_CourseCode = code;
    }

    public void removeAllListElement() {

        modelForListofContent.removeAllElements();
    }

    public void updateListofContent(String week_name) {
        //jList4.setVisibleRowCount(-1);
        jList4.revalidate();
        jList4.repaint();
        modelForListofContent.addElement(week_name);
        jList4.setModel(modelForListofContent);
        jList4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList4.setSelectionBackground(Color.BLUE);
        //Selected_week = week_name;
        //jFileChooser1.setVisible(false);
        //System.out.println(week_name);

    }

    private ResultSet getCourseMetrial(String selectedCourseCode, String selectedWeek) throws SQLException {
        String SQLgetPathofmat = "SELECT * FROM course_metrial WHERE course_Course_id=? AND week=?";
        PreparedStatement pstmt = conn.prepareStatement(SQLgetPathofmat);
        pstmt.setString(1, selectedCourseCode);
        pstmt.setString(2, selectedWeek);
        return pstmt.executeQuery();
    }

    private void populateStudentList() {
        Student student = new Student();
        ResultSet resultSetALL = student.getAllstudent();
        ResultSet resultSetICT = student.getICTstudent();
        ResultSet resultSetET = student.getETstudent();
        ResultSet resultSetBST = student.getBSTstudent();
        ResultSet resultSet = null;
        studentList.setColumnCount(0);
        studentList.setRowCount(0);
        //model.addElement("Get ALL Student");
        //model.addElement("Get ET Department Student");
        //model.addElement("Get ICT Department Student");
        //model.addElement("Get BST Department Student");
        if (jComboBox2.getSelectedItem() == "Get ALL Student") {
            resultSet = resultSetALL;
        } else if (jComboBox2.getSelectedItem() == "Get ET Department Student") {
            resultSet = resultSetET;
        } else if (jComboBox2.getSelectedItem() == "Get ICT Department Student") {
            resultSet = resultSetICT;
        } else if (jComboBox2.getSelectedItem() == "Get BST Department Student") {
            resultSet = resultSetBST;
        } else {
            resultSet = resultSetALL;
        }
        int count = 0;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount() - 1;
            String[] THs = new String[columnCount];
            int columnIndex = 0;
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                if (!"profile_picture".equals(columnName)) {
                    THs[columnIndex++] = columnName;
                }
            }
            studentList.setColumnIdentifiers(THs);
            while (resultSet.next()) {
                int Reg_no = resultSet.getInt("Reg_no");
                String Address = resultSet.getString("Address");
                String Name = resultSet.getString("Name");
                String Email = resultSet.getString("Email");
                int Level = resultSet.getInt("Level");
                float Gpa = resultSet.getFloat("Gpa");
                Time Last_login = resultSet.getTime("Last_login");
                String department_name = resultSet.getString("department_name");
                //String profile_picture = resultSet.getString("profile_picture");
                int Contact_No = resultSet.getInt("Contact_No");
                int Age = resultSet.getInt("Age");
                String Country = resultSet.getString("Country");
                int Semester = resultSet.getInt("Semester");
                studentList.addRow(new Object[]{Reg_no, Address, Name, Email, Level, Gpa, Last_login, department_name, Contact_No, Age, Country, Semester});
            }
        } catch (SQLException ex) {
            //Logger.getLogger(Lecturer_UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populateStudentList(ResultSet resultSet) {
        Student student = new Student();
        studentList.setColumnCount(0);
        studentList.setRowCount(0);
        //model.addElement("Get ALL Student");
        //model.addElement("Get ET Department Student");
        //model.addElement("Get ICT Department Student");
        //model.addElement("Get BST Department Student");

        int count = 0;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount() - 1;
            String[] THs = new String[columnCount];
            int columnIndex = 0;
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                if (!"profile_picture".equals(columnName)) {
                    THs[columnIndex++] = columnName;
                }
            }
            studentList.setColumnIdentifiers(THs);
            while (resultSet.next()) {
                int Reg_no = resultSet.getInt("Reg_no");
                String Address = resultSet.getString("Address");
                String Name = resultSet.getString("Name");
                String Email = resultSet.getString("Email");
                int Level = resultSet.getInt("Level");
                float Gpa = resultSet.getFloat("Gpa");
                Time Last_login = resultSet.getTime("Last_login");
                String department_name = resultSet.getString("department_name");
                //String profile_picture = resultSet.getString("profile_picture");
                int Contact_No = resultSet.getInt("Contact_No");
                int Age = resultSet.getInt("Age");
                String Country = resultSet.getString("Country");
                int Semester = resultSet.getInt("Semester");
                studentList.addRow(new Object[]{Reg_no, Address, Name, Email, Level, Gpa, Last_login, department_name, Contact_No, Age, Country, Semester});
            }
        } catch (SQLException ex) {
            //Logger.getLogger(Lecturer_UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populateMarksList() {
        Student student = new Student();
        String input = (String) jComboBox3.getSelectedItem();
        String code = null;
        ResultSet resultSet = null;
        if ("ALL".equals(input) || input == null) {
            resultSet = student.getStudentMarks();
        } else {
            String[] parts = input.split(" - ");
            code = parts[0];
            resultSet = student.getStudentMarksWithALLStudents(code);
        }
        //ResultSet resultSet = student.getLecturerCourseStudents(code);

        int count = 0;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            String[] THs = new String[resultSetMetaData.getColumnCount() + 1];// table heders array 
            //System.out.println("1234 - "+resultSetMetaData1.getColumnCount());
            for (int i = 1; i <= (resultSetMetaData.getColumnCount()); i++) {
                THs[i - 1] = resultSetMetaData.getColumnName(i);

                //System.out.println(i);
            }
            THs[resultSetMetaData.getColumnCount()] = "Grade";

            marksList.setColumnIdentifiers(THs);
            while (resultSet.next()) {
                //System.out.println(resultSet.getInt("Student_id") + resultSet.getString("Course_id"));
                int Student_id = resultSet.getInt("Student_id");
                String Course_id = resultSet.getString("Course_id");
                Integer Mid_term = resultSet.getObject("Mid_term") != null ? resultSet.getInt("Mid_term") : null;
                Integer final_marks = resultSet.getObject("final_marks") != null ? resultSet.getInt("final_marks") : null;
                Integer final_tyeory = resultSet.getObject("final_tyeory") != null ? resultSet.getInt("final_tyeory") : null;
                Integer final_practical = resultSet.getObject("final_practical") != null ? resultSet.getInt("final_practical") : null;
                Integer assesment = resultSet.getObject("assesment") != null ? resultSet.getInt("assesment") : null;
                Integer Quizze01 = resultSet.getObject("Quizze01") != null ? resultSet.getInt("Quizze01") : null;
                Integer Quizze02 = resultSet.getObject("Quizze02") != null ? resultSet.getInt("Quizze02") : null;
                Integer Quizze03 = resultSet.getObject("Quizze03") != null ? resultSet.getInt("Quizze03") : null;
                char Grade;
                if (final_marks != null) {
                    if (final_marks >= 90) {
                        Grade = 'A';
                    } else if (final_marks >= 80) {
                        Grade = 'B';
                    } else if (final_marks >= 70) {
                        Grade = 'C';
                    } else if (final_marks >= 60) {
                        Grade = 'D';
                    } else if (final_marks >= 50) {
                        Grade = 'E';
                    } else {
                        Grade = 'F';
                    }
                } else {
                    Grade = 'N';
                }

                marksList.addRow(new Object[]{Student_id, Course_id, Quizze01, Quizze02, Quizze03, Mid_term, final_tyeory, final_practical, assesment, final_marks, Grade});
            }
        } catch (SQLException ex) {
            //Logger.getLogger(Lecturer_UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populateNoticeList() {

        notice Announcements = new notice();

        ResultSet resultSet = null;

        resultSet = Announcements.getAllnotice();

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            String[] THs = new String[]{"title", "details", "date_time"};
            noticeList.setColumnIdentifiers(THs);
            while (resultSet.next()) {
                //System.out.println(resultSet.getInt("Student_id") + resultSet.getString("Course_id"));
                String title = resultSet.getString("title");
                String details = resultSet.getString("details");
                String date_time = resultSet.getString("date_time");

                noticeList.addRow(new Object[]{title, details, date_time});
            }
        } catch (SQLException ex) {
            //Logger.getLogger(Lecturer_UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populateMarksComboBox() {
        try {
            DefaultComboBoxModel<String> modelForMarksComboBox = (DefaultComboBoxModel<String>) jComboBox3.getModel();
            modelForMarksComboBox.removeAllElements();
            CourseList cl = new CourseList();
            ResultSet courses = cl.Courses_Lecture_Incharge(String.valueOf(Lecturer_ID));
            while (courses.next()) {
                String courseId = courses.getString("Course_id");
                String courseName = courses.getString("Name");
                //System.out.println(courseId + " - " + courseName);
                modelForMarksComboBox.addElement(courseId + " - " + courseName);
            }
            modelForMarksComboBox.addElement("ALL");
            jComboBox3.setSelectedItem("ALL");
            jComboBox3.setModel(modelForMarksComboBox);
        } catch (SQLException ex) {
            String message = "SQL Error: " + ex.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setLectureDetilstoLC(int Lecturer_ID) {
        Lecture lecture = new Lecture();
        System.out.println(Lecturer_ID);
        newLecture = lecture.getLectureData(Lecturer_ID);
    }

    public void updateLectureDetils() {
        Lecture lecture = new Lecture();
        lecture.updateLectureData(newLecture);
    }

    public void setLecName() {
        setLectureDetilstoLC(Lecturer_ID);
        jLabel3.setText(newLecture.getPosition() + " . " + newLecture.getName());
        jLabel5.setText(newLecture.getPosition() + " . " + newLecture.getName());
        Font boldLargeFont = new Font("Arial", Font.BOLD, 18);
        jLabel3.setFont(boldLargeFont);
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);

        jLabel5.setFont(boldLargeFont);
        jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void setLectureDetilst() {
        int staffNo = newLecture.getStaffNo();
        String address = newLecture.getAddress();
        String country = newLecture.getCountry();
        String position = newLecture.getPosition();
        int telephone = newLecture.getTelephone();
        String departmentName = newLecture.getDepartmentName();
        ImgURL = newLecture.getProfile();
        jLabel9.setText(" staffNo ");
        jLabel10.setText(" country ");
        jLabel11.setText(" address ");
        jLabel12.setText(" telephone ");
        jLabel13.setText(" position ");
        jLabel14.setText(" departmentName ");
        jTextField7.setText(String.valueOf(staffNo));
        jTextField2.setText(address);
        jTextField3.setText(country);
        jTextField4.setText(position);
        jTextField5.setText(String.valueOf(telephone));
        jTextField6.setText(departmentName);
        //country
    }

    public void populateTimeTable() {
        TimetableEntry timetableEntry = new TimetableEntry();
        DefaultTableModel model = (DefaultTableModel) TimeTable.getModel();
        model.setRowCount(0);
        model.setColumnCount(0);
        try {
            ResultSet resultSet = timetableEntry.getAllsubjects();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                String columnName = metaData.getColumnName(columnIndex);
                model.addColumn(columnName);
            }
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void populateAttendenceList() {
        Attendance attendance = new Attendance();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        model.setColumnCount(0);
        try {
            ResultSet resultSet = attendance.getAllAttendances();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                String columnName = metaData.getColumnName(columnIndex);
                model.addColumn(columnName);
            }
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void populateAttendenceList(ResultSet result) {
        Attendance attendance = new Attendance();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        model.setColumnCount(0);
        try {
            ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                String columnName = metaData.getColumnName(columnIndex);
                model.addColumn(columnName);
            }
            while (result.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = result.getObject(i + 1);
                }
                model.addRow(rowData);
            }
            jTable1.setModel(model);
        } catch (SQLException e) {
            String message = "SQL Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public Lecturer_UI(int lecturerID) {
        this.Lecturer_ID = lecturerID; 
        setLectureDetilstoLC(Lecturer_ID);
        System.out.println("setLectureDetilstoLC  "+Lecturer_ID);

    }

    public Lecturer_UI() {

        initComponents();
        //rendaring todays courses

        setLecName();
        CourseList CL = new CourseList();
        jPanel22.removeAll();
        CL.LoadSortedList(CL.Courses_SQL_TodaysCourse, Lecturer_ID, "Course_id", jPanel22, this);
        jPanel22.revalidate();
        jPanel22.repaint();
        // add course list to panel17
        jPanel17.setLayout(new FlowLayout());
        CourseList courseList = new CourseList(jPanel17, this);

        // pass the  course id that selected from course list
        // add profile pic at start
        setLectureDetilst();
        setLayoutAndImg(jPanel16);
        setLayoutAndImg(jPanel4);

        //add home card
        setPanel("card2");

        // course 
        //Action listener for JList4
        jList4.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedValue = jList4.getSelectedValue();
                jList3.setModel(modelForListofMetrial);
                modelForListofMetrial.removeAllElements();
                String SQLUpdateQuary = "UPDATE course_metrial SET File_url = ? WHERE idCourse_metrial = ? ";
                if (selectedValue != null) {
                    try {
                        ResultSet resultofcourse_metrialLink = getCourseMetrial(Selected_CourseCode, selectedValue);
                        //resultofcourse_metrialLink.next();
                        if (!resultofcourse_metrialLink.next()) {
                            JOptionPane.showMessageDialog(null, "No data found", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            //System.out.println("1" + resultofcourse_metrialLink.getString("File_url"));
                            String folderPath = resultofcourse_metrialLink.getString("File_url");

                            //System.out.println(folderPath);
                            File NewFile = null;
                            if (folderPath == null || "".equals(folderPath)) {
                                folderPath = "D:\\TECLMS\\Course metrial\\" + Selected_CourseCode + "\\" + resultofcourse_metrialLink.getString("WEEK");
                                NewFile = new File(folderPath);
                                System.out.println("filepath " + folderPath);

                                if (!NewFile.exists()) {
                                    NewFile.mkdirs();
                                    System.out.println("Directory created.");
                                    // INSERT THE URL
                                }
                                PreparedStatement pstmt = conn.prepareStatement(SQLUpdateQuary);
                                pstmt.setString(1, folderPath);
                                pstmt.setInt(2, resultofcourse_metrialLink.getInt("idCourse_metrial"));
                                int reult = pstmt.executeUpdate();
                                if (reult > 0) {
                                    System.out.println("URL updated");
                                }

                            } else {
                                NewFile = new File(folderPath);
                                if (!NewFile.exists()) {

                                    NewFile = new File(folderPath);
                                    NewFile.mkdirs();
                                    //System.out.println("Directory created.");
                                    // INSERT THE URL

                                } else {
                                    System.out.println("File_url not null AND File exist.");
                                }
                            }
                            File selectedFolder = new File(folderPath);

                            if (selectedFolder.exists() && selectedFolder.isDirectory()) {
                                File[] files = selectedFolder.listFiles();
                                for (File x : files) {
                                    modelForListofMetrial.addElement(x.getName());
                                }
                            } else {
                                selectedFolder.mkdirs();
                            }
                        }

                    } catch (SQLException ex) {
                        String message = "SQL Error: " + ex.getMessage();
                        System.out.println(message);
                        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    //System.out.println("Selected Item: " + selectedValue);
                }//To change body of generated methods, choose Tools | Templates.x
            }
        });
        jList3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedValue = jList3.getSelectedValue();
                    try {
                        ResultSet resultofcourse_metrialLink = getCourseMetrial(Selected_CourseCode, jList4.getSelectedValue());
                        if (!resultofcourse_metrialLink.next()) {
                            JOptionPane.showMessageDialog(null, "No data found", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (selectedValue != null) {
                                File fileToOpen = new File(resultofcourse_metrialLink.getString("File_url"), selectedValue);
                                try {
                                    Desktop.getDesktop().open(fileToOpen);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }

                    } catch (SQLException ex) {
                        String message = "SQL Error: " + ex.getMessage();
                        System.out.println(message);
                        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    //To change body of generated methods, choose Tools | Templates.
                }
            }
        });

        //student 
        populateStudentList();
        jTable4.removeAll();
        JTableHeader header = jTable4.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                comp.setBackground(new Color(150, 150, 150));
                return comp;
            }
        });

        jTable4.setModel(studentList);
        jTable4.setEnabled(true);
        jTable4.setDefaultEditor(Object.class, null);

        //Marks
        //TimeTable
        populateTimeTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        Main = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TimeTable = new javax.swing.JTable();
        jPanel23 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        Course = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jButton20 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        Stu_managment = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jButton21 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        Announcements = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel24 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        Profile = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        Course_details = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList<>();
        jButton16 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        Stu_marks = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton22 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        Student_attendence = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton25 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(200, 538));

        jButton7.setText("Main");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton8.setText("Course");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton9.setText("Student Managment");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Profile");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton11.setText("Announcements");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton12.setText("LogOut");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("TechLms");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jButton7)
                .addGap(37, 37, 37)
                .addComponent(jButton8)
                .addGap(36, 36, 36)
                .addComponent(jButton9)
                .addGap(36, 36, 36)
                .addComponent(jButton11)
                .addGap(35, 35, 35)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton12)
                .addGap(64, 64, 64))
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.LINE_START);

        jPanel2.setLayout(new java.awt.CardLayout());

        jLabel3.setText("Name");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("TODAYS WORK");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 757, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 253, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        TimeTable.setBackground(new java.awt.Color(204, 204, 204));
        TimeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(TimeTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 170, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 162, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGap(0, 62, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout MainLayout = new javax.swing.GroupLayout(Main);
        Main.setLayout(MainLayout);
        MainLayout.setHorizontalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        MainLayout.setVerticalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.add(Main, "card2");

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 102, 102));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("COURSES");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        );

        jPanel9.setPreferredSize(new java.awt.Dimension(100, 40));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SORT BY Course_Name", "SORT BY Number_Of_Student", "SORT BY Offered_department", "SORT BY Number_Of_Hours", "SORT Course List--Defult"}));
        jComboBox1.setSelectedItem("SORT Course List--Defult");
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("ALL");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed1(evt);
            }
        });

        jButton2.setText("Coordinator Courses");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed1(evt);
            }
        });

        jButton3.setText("BST");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("ET");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed1(evt);
            }
        });

        jPanel17.setPreferredSize(new java.awt.Dimension(600, 400));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 972, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 689, Short.MAX_VALUE)
        );

        jButton20.setText("ICT");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("SORT COURSES");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(100, 100, 100)
                        .addComponent(jButton2)
                        .addGap(100, 100, 100)
                        .addComponent(jButton4)
                        .addGap(100, 100, 100)
                        .addComponent(jButton20)
                        .addGap(100, 100, 100)
                        .addComponent(jButton3))
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 689, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CourseLayout = new javax.swing.GroupLayout(Course);
        Course.setLayout(CourseLayout);
        CourseLayout.setHorizontalGroup(
            CourseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CourseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CourseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 1167, Short.MAX_VALUE)
                    .addGroup(CourseLayout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        CourseLayout.setVerticalGroup(
            CourseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CourseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE))
        );

        jPanel2.add(Course, "card3");

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 153, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("STUDENT MANAGMENT");
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel4.setText("Sort");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        DefaultComboBoxModel model = (DefaultComboBoxModel)jComboBox2.getModel();
        model.removeAllElements();
        model.addElement("Get ALL Student");
        model.addElement("Get ET Department Student");
        model.addElement("Get ICT Department Student");
        model.addElement("Get BST Department Student");
        jComboBox2.setSelectedItem("Get ALL Student");

        jTable4.setBackground(new java.awt.Color(255, 255, 204));
        jTable4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTable4.setForeground(new java.awt.Color(0, 0, 0));
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable4.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable4.getTableHeader().setBackground(new Color(150, 150, 150));
        jTable4.getTableHeader().setForeground(Color.WHITE);
        jTable4.setShowGrid(true);
        jTable4.setGridColor(Color.BLACK);
        DefaultTableModel jTable4model = (DefaultTableModel)jTable4.getModel();
        jScrollPane6.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jButton21.setText("Students Marks");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton23.setText("Students Attendence");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton23, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jButton21)
                .addGap(80, 80, 80)
                .addComponent(jButton23)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel15.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("SEARCH STUDEN BY NAME");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(124, 124, 124)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(187, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Stu_managmentLayout = new javax.swing.GroupLayout(Stu_managment);
        Stu_managment.setLayout(Stu_managmentLayout);
        Stu_managmentLayout.setHorizontalGroup(
            Stu_managmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Stu_managmentLayout.createSequentialGroup()
                .addGroup(Stu_managmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Stu_managmentLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(Stu_managmentLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Stu_managmentLayout.setVerticalGroup(
            Stu_managmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Stu_managmentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        jPanel2.add(Stu_managment, "card4");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable3.setRowHeight(50);
        jTable3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTable3MouseMoved(evt);
            }
        });
        JTableHeader header = jTable3.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 25));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                c.setBackground(Color.black);
                c.setForeground(Color.WHITE);
                c.setFont(c.getFont().deriveFont(Font.BOLD));

                return c;
            }
        });
        jTable3.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (row % 2 == 0) {
                    c.setBackground(new Color(64,64,64));
                } else {
                    c.setBackground(new Color(44, 45, 45));
                }

                switch (column) {
                    case 0:
                    c.setForeground(Color.BLUE);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    break;
                    case 1:
                    c.setForeground(Color.RED);
                    break;
                    case 2:
                    c.setForeground(Color.GREEN);
                    break;
                }

                return c;
            }

        });
        jScrollPane7.setViewportView(jTable3);

        jLabel19.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 102, 102));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("ANNOUNCEMENTS!");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1053, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AnnouncementsLayout = new javax.swing.GroupLayout(Announcements);
        Announcements.setLayout(AnnouncementsLayout);
        AnnouncementsLayout.setHorizontalGroup(
            AnnouncementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AnnouncementsLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        AnnouncementsLayout.setVerticalGroup(
            AnnouncementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AnnouncementsLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );

        jPanel2.add(Announcements, "card5");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setText("NAME");

        jPanel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 269, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 203, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel9.setText("jLabel9");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel10.setText("jLabel9");

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel11.setText("jLabel9");

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel12.setText("jLabel9");

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel13.setText("jLabel9");

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel14.setText("jLabel9");

        jTextField2.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        jTextField2.setText("jTextField1");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        jTextField3.setText("jTextField1");

        jTextField4.setEditable(false);
        jTextField4.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        jTextField4.setText("jTextField1");

        jTextField5.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        jTextField5.setText("jTextField1");

        jTextField6.setEditable(false);
        jTextField6.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        jTextField6.setText("jTextField1");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField7.setEditable(false);
        jTextField7.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        jTextField7.setText("jTextField7");

        jButton5.setText("UPDATE");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed1(evt);
            }
        });

        jButton6.setText("Back");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed1(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 1014, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                            .addGap(130, 130, 130)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(150, 150, 150)
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(150, 150, 150)
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                                        .addComponent(jTextField7)))))
                        .addGroup(jPanel14Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 1014, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout ProfileLayout = new javax.swing.GroupLayout(Profile);
        Profile.setLayout(ProfileLayout);
        ProfileLayout.setHorizontalGroup(
            ProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProfileLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(343, 343, 343))
            .addGroup(ProfileLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ProfileLayout.setVerticalGroup(
            ProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProfileLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(Profile, "card6");

        jLabel7.setForeground(new java.awt.Color(255, 0, 255));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );

        Font labelFont = jLabel7.getFont();
        jLabel7.setFont(new Font(labelFont.getName(), Font.BOLD, 24));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jLabel8.setText("CONTENTS");

        jButton13.setText("ADD CONTENT");
        jButton13.setPreferredSize(new java.awt.Dimension(118, 40));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jList4.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {  };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList4);

        jButton16.setText("Delete");
        jButton16.setPreferredSize(new java.awt.Dimension(118, 40));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton18.setText("Back");
        jButton18.setPreferredSize(new java.awt.Dimension(118, 40));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        labelFont = jLabel8.getFont();
        jLabel8.setFont(new Font(labelFont.getName(), Font.BOLD, 14));

        jList3.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList3);

        jButton14.setText("ADD");
        jButton14.setPreferredSize(new java.awt.Dimension(125, 40));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText("Rename");
        jButton15.setPreferredSize(new java.awt.Dimension(125, 40));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton17.setText("Delete");
        jButton17.setPreferredSize(new java.awt.Dimension(125, 40));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(244, 244, 244)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 264, Short.MAX_VALUE)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout Course_detailsLayout = new javax.swing.GroupLayout(Course_details);
        Course_details.setLayout(Course_detailsLayout);
        Course_detailsLayout.setHorizontalGroup(
            Course_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Course_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Course_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Course_detailsLayout.createSequentialGroup()
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 198, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Course_detailsLayout.setVerticalGroup(
            Course_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Course_detailsLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(Course_details, "card7");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(jTable2);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));
        jComboBox3.setSelectedItem("ALL");
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jButton22.setText("Update Markes");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton19.setText("Back");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton24.setText("Genarate Final Marks");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Stu_marksLayout = new javax.swing.GroupLayout(Stu_marks);
        Stu_marks.setLayout(Stu_marksLayout);
        Stu_marksLayout.setHorizontalGroup(
            Stu_marksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Stu_marksLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addGroup(Stu_marksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Stu_marksLayout.createSequentialGroup()
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(151, 151, 151)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(288, Short.MAX_VALUE))
        );
        Stu_marksLayout.setVerticalGroup(
            Stu_marksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Stu_marksLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Stu_marksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton22)
                    .addComponent(jButton19)
                    .addComponent(jButton24))
                .addContainerGap(412, Short.MAX_VALUE))
        );

        jPanel2.add(Stu_marks, "card8");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1135, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1123, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel25Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jButton25.setText("Back");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton25)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField8KeyReleased(evt);
            }
        });

        jLabel16.setText("SEARCH BY STUDENT ID");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 951, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(309, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Student_attendenceLayout = new javax.swing.GroupLayout(Student_attendence);
        Student_attendence.setLayout(Student_attendenceLayout);
        Student_attendenceLayout.setHorizontalGroup(
            Student_attendenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Student_attendenceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        Student_attendenceLayout.setVerticalGroup(
            Student_attendenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Student_attendenceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(Student_attendence, "card9");

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed1
        // TODO add your handling code here:
        CourseList CL = new CourseList();
        int SelectedIndex = jComboBox1.getSelectedIndex();
        //System.out.println(SelectedIndex);
        String listOfSort[] = new String[]{"Name", "Number_Of_Student", "Offered_department", "Hours", "Course_id"};
        System.out.println(listOfSort[SelectedIndex]);
        CL.LoadSortedList(CL.Courses_SQL_Lecture_Incharge, "1", listOfSort[SelectedIndex], jPanel17, this);
        jPanel17.revalidate();
        jPanel17.repaint();
    }//GEN-LAST:event_jButton2ActionPerformed1

    private void jButton4ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed1
        // TODO add your handling code here:
        CourseList CL = new CourseList();
        int SelectedIndex = jComboBox1.getSelectedIndex();
        //System.out.println(SelectedIndex);
        String listOfSort[] = new String[]{"Name", "Number_Of_Student", "Offered_department", "Hours", "Course_id"};
        System.out.println(listOfSort[SelectedIndex]);
        CL.LoadSortedList(CL.Courses_SQL_Offered_department, "ET", listOfSort[SelectedIndex], jPanel17, this);
        jPanel17.revalidate();
        jPanel17.repaint();
    }//GEN-LAST:event_jButton4ActionPerformed1

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:\
        CourseList CL = new CourseList();
        String sort = (String) jComboBox1.getSelectedItem();
        int SelectedIndex = jComboBox1.getSelectedIndex();
        //System.out.println(SelectedIndex);
        String listOfSort[] = new String[]{"Name", "Number_Of_Student", "Offered_department", "Hours", "Course_id"};
        //CL.

        //System.out.println(listOfSort[SelectedIndex]);
        CL.LoadSortedList(CL.Courses_SQL_Order, "", listOfSort[SelectedIndex], jPanel17, this);
        jPanel17.revalidate();
        jPanel17.repaint();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed1
        // TODO add your handling code here:
        CourseList CL = new CourseList();
        CL.LoadSortedList(CL.Courses_SQL_Order, "", "Course_id", jPanel17, this);
        jPanel17.revalidate();
        jPanel17.repaint();
        //System.out.println("1234");
    }//GEN-LAST:event_jButton1ActionPerformed1

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        JPanel jPanelFromforMetrial = new JPanel(new GridLayout(2, 2));
        TextField TFdescribition = new TextField();
        TextField TFweek = new TextField();
        String defaultText = "WEEK ";
        TFweek.setText(defaultText);
        JLabel LableFroDeescribtion = new JLabel("Enter Describition");
        JLabel LableFroWeek = new JLabel("Enter Week Name");
        jPanelFromforMetrial.add(LableFroDeescribtion);
        jPanelFromforMetrial.add(TFdescribition);
        jPanelFromforMetrial.add(LableFroWeek);
        jPanelFromforMetrial.add(TFweek);

        //textField.setEditable(false);
        int option = JOptionPane.showConfirmDialog(null, jPanelFromforMetrial, "Content name ", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            //ResultSet resultofcourse_metrialLink = getCourseMetrial(Selected_CourseCode, jList4.getSelectedValue());
            String DescribitionValue = TFdescribition.getText();
            String WeekValue = TFweek.getText();
            String SQLInsertMatirialURL = "INSERT INTO course_metrial (`Describition`, `File_url`, `course_Course_id`, `week`) VALUES (?, ?, ?, ?)";
            String course_metrial = "SELECT * FROM course_metrial WHERE course_Course_id = ?";
            System.out.println(Selected_CourseCode + DescribitionValue + WeekValue);
            String FileURL = "D:\\TECLMS\\Course metrial\\" + Selected_CourseCode + "\\" + WeekValue;
            File NewFile = new File(FileURL);
            try {
                if (!NewFile.exists()) {
                    NewFile.mkdirs();
                    System.out.println("Directory created.");
                } else {
                    System.out.println("Directory ERROR.");
                }

                PreparedStatement pstmt = conn.prepareStatement(SQLInsertMatirialURL);
                pstmt.setString(1, DescribitionValue);
                pstmt.setString(2, FileURL);
                pstmt.setString(3, Selected_CourseCode);
                pstmt.setString(4, WeekValue);
                pstmt.executeUpdate();

                PreparedStatement pstmt1 = conn.prepareStatement(course_metrial);
                pstmt1.setString(1, Selected_CourseCode);
                modelForListofContent.removeAllElements();
                ResultSet resultofcourse_metrial = pstmt1.executeQuery();
                while (resultofcourse_metrial.next()) {
                    modelForListofContent.addElement(resultofcourse_metrial.getString("week"));
                    System.out.println(resultofcourse_metrial.getString("week"));

                }
                jList4.setModel(modelForListofContent);

            } catch (SQLException ex) {

            }

        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        String selectedValue = jList3.getSelectedValue();

        try {
            ResultSet resultofcourse_metrialLink = getCourseMetrial(Selected_CourseCode, jList4.getSelectedValue());

            if (!resultofcourse_metrialLink.next()) {
                JOptionPane.showMessageDialog(null, "Select a Week", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File selectedFile = new File(resultofcourse_metrialLink.getString("File_url"));
            JFileChooser fileChooserForCourse = new JFileChooser(selectedFile.getParentFile()); // Set the directory of the file chooser to the parent directory of the selected file

            int returnValue = fileChooserForCourse.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File chosenFile = fileChooserForCourse.getSelectedFile();
                /*fileChooserForCourse.setFileFilter(new FileFilter() {
                    String[] allowedExtensions = {"pdf", "docx", "txt","doc" ,"pptx"};
                    @Override
                    public boolean accept(File f) {
                        if(f.isDirectory()){
                            return true;
                        } 
                        String fileName = f.getName().toLowerCase();
                        for(String x: allowedExtensions){
                            if (fileName.endsWith("."+x)) {
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                });*/
                Path sourcePath = chosenFile.toPath();
                Path destinationDir = selectedFile.toPath();

                System.out.println("Source: " + sourcePath + " Destination: " + destinationDir);

                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to add: " + chosenFile.getName(), "Alert", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                    try {
                        Path destinationPath = destinationDir.resolve(chosenFile.getName());
                        Files.copy(sourcePath, destinationPath);

                        modelForListofMetrial.removeAllElements();
                        File selectedFolder = new File(resultofcourse_metrialLink.getString("File_url"));

                        if (selectedFolder.exists() && selectedFolder.isDirectory()) {
                            File[] files = selectedFolder.listFiles();
                            //  modelForListofMetrial.removeAllElements();
                            for (File x : files) {
                                //System.out.println(x.getName());
                                modelForListofMetrial.addElement(x.getName());
                            }
                        } else {
                            selectedFolder.mkdirs();
                        }
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error copying file: " + e, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            String message = "SQL Error: " + ex.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        String selectedValue = jList3.getSelectedValue();
        try {
            ResultSet resultofcourse_metrialLink = getCourseMetrial(Selected_CourseCode, jList4.getSelectedValue());
            if (!resultofcourse_metrialLink.next()) {
                JOptionPane.showMessageDialog(null, "No data found", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (selectedValue != null) {
                    File fileToDelete = new File(resultofcourse_metrialLink.getString("File_url"), selectedValue);
                    if (JOptionPane.showConfirmDialog(null, "Are you suer you want to Delete: " + selectedValue, "Alert", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                        if (fileToDelete.delete()) {
                            JOptionPane.showConfirmDialog(null, selectedValue + " is successfully Deleted", "Comfrom", JOptionPane.PLAIN_MESSAGE);
                            modelForListofMetrial.removeAllElements();
                            File selectedFolder = new File(resultofcourse_metrialLink.getString("File_url"));
                            //FilenameFilter filter = null;
                            //jFileChooser1.setCurrentDirectory(selectedFile);

                            if (selectedFolder.exists() && selectedFolder.isDirectory()) {
                                File[] files = selectedFolder.listFiles();
                                //  modelForListofMetrial.removeAllElements();
                                for (File x : files) {
                                    //System.out.println(x.getName());
                                    modelForListofMetrial.addElement(x.getName());
                                }
                            } else {
                                selectedFolder.mkdirs();
                            }
                        } else {
                            JOptionPane.showConfirmDialog(null, " Failed to delete the " + selectedValue + ".", "Comfrom", JOptionPane.OK_OPTION);
                        }
                    }

                } else {
                    JOptionPane.showConfirmDialog(null, " First Select a File", "Alert", JOptionPane.PLAIN_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            String message = "SQL Error: " + ex.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        String selectedValue = jList3.getSelectedValue();
        try {
            ResultSet resultofcourse_metrialLink = getCourseMetrial(Selected_CourseCode, jList4.getSelectedValue());
            if (!resultofcourse_metrialLink.next()) {
                JOptionPane.showMessageDialog(null, "No data found", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (selectedValue != null) {
                    File file_ToRename = new File(resultofcourse_metrialLink.getString("File_url"), selectedValue);
                    String newFileName = JOptionPane.showInputDialog(null, "Please enter the new file name:", selectedValue);
                    File file_RenameTo = new File(file_ToRename.getParent(), newFileName);

                    if (file_ToRename.renameTo(file_RenameTo)) {
                        //JOptionPane.showConfirmDialog(null, selectedValue + " is successfully Deleted", "Comfrom", JOptionPane.PLAIN_MESSAGE);
                        modelForListofMetrial.removeAllElements();
                        File selectedFolder = new File(resultofcourse_metrialLink.getString("File_url"));
                        //FilenameFilter filter = null;
                        //jFileChooser1.setCurrentDirectory(selectedFile);

                        if (selectedFolder.exists() && selectedFolder.isDirectory()) {
                            File[] files = selectedFolder.listFiles();
                            //  modelForListofMetrial.removeAllElements();
                            for (File x : files) {
                                //System.out.println(x.getName());
                                modelForListofMetrial.addElement(x.getName());
                            }
                        } else {
                            selectedFolder.mkdirs();
                        }

                    }

                } else {
                    JOptionPane.showConfirmDialog(null, " First Select a File", "Alert", JOptionPane.PLAIN_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            String message = "SQL Error: " + ex.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private static void deleteFiles(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFiles(file);
                } else {
                    file.delete();
                }
            }
        }
    }
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        String selectedValue = jList4.getSelectedValue();
        try {
            ResultSet resultofcourse_metrialLink = getCourseMetrial(Selected_CourseCode, jList4.getSelectedValue());
            if (!resultofcourse_metrialLink.next()) {
                JOptionPane.showMessageDialog(null, "No data found", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (selectedValue != null) {
                    File fileToDelete = new File(resultofcourse_metrialLink.getString("File_url"));
                    if (JOptionPane.showConfirmDialog(null, "Are you suer you want to Delete: " + selectedValue, "Alert", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                        if (fileToDelete.exists()) {
                            deleteFiles(fileToDelete);
                            if (fileToDelete.delete()) {
                                System.out.println("insde delete");
                                String SQLstr = "DELETE FROM course_metrial WHERE idCourse_metrial=? ";
                                PreparedStatement pstmt = conn.prepareStatement(SQLstr);
                                pstmt.setString(1, resultofcourse_metrialLink.getString("idCourse_metrial"));
                                pstmt.executeUpdate();

                                JOptionPane.showConfirmDialog(null, selectedValue + " is successfully Deleted", "Comfrom", JOptionPane.PLAIN_MESSAGE);
                                modelForListofMetrial.removeAllElements();
                                File selectedFolder = new File(resultofcourse_metrialLink.getString("File_url"));
                                //FilenameFilter filter = null;
                                //jFileChooser1.setCurrentDirectory(selectedFile);

                                File[] files = selectedFolder.getParentFile().listFiles();
                                //  modelForListofMetrial.removeAllElements();
                                modelForListofContent.removeAllElements();
                                for (File x : files) {
                                    //System.out.println(x.getName());
                                    modelForListofContent.addElement(x.getName());
                                }

                            } else {
                                JOptionPane.showConfirmDialog(null, " Failed to delete the " + selectedValue + ".", "Comfrom", JOptionPane.OK_OPTION);
                            }
                        } else {

                            JOptionPane.showConfirmDialog(null, selectedValue + "dose not exist.", "Comfrom", JOptionPane.OK_OPTION);
                        }
                    }

                } else {
                    JOptionPane.showConfirmDialog(null, " First Select a File", "Alert", JOptionPane.PLAIN_MESSAGE);
                }
            }
            jList4.setModel(modelForListofContent);

        } catch (SQLException ex) {
            String message = "SQL Error: " + ex.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        setPanel("card3");
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        CourseList CL = new CourseList();
        int SelectedIndex = jComboBox1.getSelectedIndex();
        //System.out.println(SelectedIndex);
        String listOfSort[] = new String[]{"Name", "Number_Of_Student", "Offered_department", "Hours", "Course_id"};
        System.out.println(listOfSort[SelectedIndex]);
        CL.LoadSortedList(CL.Courses_SQL_Offered_department, "BST", listOfSort[SelectedIndex], jPanel17, this);
        jPanel17.revalidate();
        jPanel17.repaint();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        CourseList CL = new CourseList();
        int SelectedIndex = jComboBox1.getSelectedIndex();
        //System.out.println(SelectedIndex);
        String listOfSort[] = new String[]{"Name", "Number_Of_Student", "Offered_department", "Hours", "Course_id"};
        System.out.println(listOfSort[SelectedIndex]);
        CL.LoadSortedList(CL.Courses_SQL_Offered_department, "ICT", listOfSort[SelectedIndex], jPanel17, this);
        jPanel17.revalidate();
        jPanel17.repaint();
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        Lecturer_ID = 0;
        LOGIN_Frame gIN_Frame = new LOGIN_Frame();
        gIN_Frame.RunLogIn();
        dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        noticeList = (DefaultTableModel) jTable3.getModel();
        noticeList.setRowCount(0);
        populateNoticeList();
        jTable3.setModel(noticeList);
        jTable2.setEnabled(false);
        jTable3.revalidate();

        setPanel("card5");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        setLectureDetilst();
        System.out.println(ImgURL);
        setPanel("card6");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        setPanel("card4");

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        setPanel("card3");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        setPanel("card2");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        setPanel("card8");
        populateMarksComboBox();
        jComboBox3.repaint();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
        jTable2.removeAll();
        marksList.setRowCount(0);
        populateMarksList();
        jTable2.setModel(marksList);
        jTable2.setEnabled(true);

        // Set default editor to null
        //jTable2.setDefaultEditor(Object.class, null);
        // Make the first two columns non-editable
        TableColumnModel columnModel = jTable2.getColumnModel();
        System.out.println(columnModel.getColumnCount());
        for (int i = 0; i < columnModel.getColumnCount(); i++) {

            if (i < 2 || i == columnModel.getColumnCount() - 1) {
                TableColumn column = columnModel.getColumn(i);
                column.setCellEditor(new DefaultCellEditor(new JTextField()) {
                    @Override
                    public boolean isCellEditable(java.util.EventObject e) {
                        return false;
                    }
                });
            }
        }

        jTable2.revalidate();
        jTable2.repaint();

        //get Data from Table IF NOT ALL 
        previousCellValues.clear();
        if (jComboBox3.getSelectedItem() != "ALL") {
            int rowCount = marksList.getRowCount();
            int colCount = marksList.getColumnCount();
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < colCount; col++) {
                    Object cellValue = marksList.getValueAt(row, col);
                    previousCellValues.put(row + "-" + col, cellValue);
                }
            }
        }
        //print it 
        /*
        System.out.println(jComboBox3.getSelectedItem());

        if (jComboBox3.getSelectedItem() != "ALL") {
            System.out.println("---------------------------------------------");
            int rowCount = marksList.getRowCount();
            int colCount = marksList.getColumnCount();
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < colCount; col++) {
                    String key = row + "-" + col;
                    Object cellValue = previousCellValues.get(key);

                    System.out.println("Row - Coloum  " + key + ", Previous Value: " + cellValue);

                }
            }
        }*/

        //System.out.println("action pofomed jcombo 3");
    }//GEN-LAST:event_jComboBox3ActionPerformed


    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
        updateMarkeTable();

    }//GEN-LAST:event_jButton22ActionPerformed

    private void jTable3MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseMoved
        // TODO add your handling code here:
        //System.out.println("Mouse moved");
        Point point = evt.getPoint();
        int row = jTable3.rowAtPoint(point);
        int column = jTable3.columnAtPoint(point);

        // Check if the mouse is over a cell in the "details" column
        if (row >= 0 && column == 1) {
            Object value = jTable3.getValueAt(row, column);
            if (value != null) {
                String details = value.toString();
                // Set tooltip text to the full details
                jTable3.setToolTipText(details);
            } else {
                jTable3.setToolTipText(null);
            }
        } else {
            jTable3.setToolTipText(null);
        }
    }//GEN-LAST:event_jTable3MouseMoved

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton5ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed1
        // TODO add your handling code here:

        newLecture.setStaffNo(Integer.parseInt(jTextField7.getText().trim()));
        newLecture.setAddress(jTextField2.getText());
        newLecture.setCountry(jTextField3.getText());
        newLecture.setPosition(jTextField4.getText());
        String TP = jTextField5.getText().trim();
        TP = TP.replace(" ", "");
        newLecture.setTelephone(Integer.parseInt(TP));
        newLecture.setDepartmentName(jTextField6.getText());

        newLecture.display();
        updateLectureDetils();
        setLectureDetilstoLC(Lecturer_ID);
        setLectureDetilst();
        //country
    }//GEN-LAST:event_jButton5ActionPerformed1

    private void jPanel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel16MouseClicked
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("D:\\TECLMS"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            ImgURL = fileChooser.getSelectedFile().getAbsolutePath();

            // Refresh the images
            refreshImages();

        }
        newLecture.setProfile(ImgURL);

    }//GEN-LAST:event_jPanel16MouseClicked

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        jTable4.removeAll();
        populateStudentList();
        jTable4.setModel(studentList);
        jTable4.setEnabled(true);
        jTable4.setDefaultEditor(Object.class, null);
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        if (Character.isDefined(evt.getKeyChar())) {
            String text = jTextField1.getText().trim();
            //System.out.println("Text changed: " + text);
            Student student = new Student();
            if (!text.isEmpty()) {
                jComboBox2.setSelectedItem("Get ALL Student");
                jTable4.removeAll();
                populateStudentList(student.searchStudentByName(text));

            } else {
                jComboBox2.setSelectedItem("Get ALL Student");
                jTable4.removeAll();
                populateStudentList();

            }
            jTable4.setModel(studentList);
            jTable4.setEnabled(true);
            jTable4.setDefaultEditor(Object.class, null);

        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton6ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed1
        // TODO add your handling code here:
        setPanel("card2");
    }//GEN-LAST:event_jButton6ActionPerformed1

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        setPanel("card4");
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
        setPanel("card9");
        populateAttendenceList();
        jTable4.setEnabled(true);
        jTable4.setDefaultEditor(Object.class, null);

    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        Student student = new Student();
        Marks marks = new Marks();
        int rowCount = marksList.getRowCount();
        int colCount = marksList.getColumnCount();
        String[] options = {"Method 1", "Method 2", "Method 3", "Method 4"};
        JLabel label = new JLabel("Choose a method to calculate the final marks:");
        JComboBox<String> comboBox = new JComboBox<>(options);
        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(comboBox);
        System.out.println(colCount);

        String key;
        if (jComboBox3.getSelectedItem() == "ALL") {
            JOptionPane.showMessageDialog(null, "You Can't Enter Marks For All Courses Select a Course First", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int JOptionPaneresult = JOptionPane.showConfirmDialog(null, panel, "Choose a Method", JOptionPane.OK_CANCEL_OPTION);
            if (JOptionPaneresult == JOptionPane.OK_OPTION) {
                for (int row = 0; row < rowCount; row++) {
                    System.out.println(row);
                    int studentID = Integer.parseInt(marksList.getValueAt(row, 0).toString());
                    String courseID = marksList.getValueAt(row, 1).toString();
                    courseID = courseID.replace("'", "");
                    System.out.println("studentID" + studentID + "courseID" + courseID);

                    Double result;
                    String selectedMethod = (String) comboBox.getSelectedItem();

                    double quiz1 = Double.parseDouble(marksList.getValueAt(row, 2).toString());
                    double quiz2 = Double.parseDouble(marksList.getValueAt(row, 3).toString());
                    double quiz3 = Double.parseDouble(marksList.getValueAt(row, 4).toString());
                    if (Double.isNaN(quiz1) || Double.isNaN(quiz2) || Double.isNaN(quiz3)) {

                        result = null;
                    } else {
                        double maxQ1 = Math.max(quiz1, Math.max(quiz2, quiz3));
                        //double minQ1 = Math.min(quiz1, Math.min(quiz2, quiz3));
                        double maxQ2 = Math.max(Math.min(quiz1, quiz2), Math.min(Math.max(quiz1, quiz2), quiz3));
                        double quizzesMarks = maxQ1 + maxQ2;
                        double midTermMarks = Double.parseDouble(marksList.getValueAt(row, 5).toString());
                        double finalTheoryMarks = Double.parseDouble(marksList.getValueAt(row, 6).toString());
                        double finalPracticalMarks = Double.parseDouble(marksList.getValueAt(row, 7).toString());
                        double assessmentsMarks = Double.parseDouble(marksList.getValueAt(row, 8).toString());
                        switch (selectedMethod) {
                            case "Method 1":
                                if (Double.isNaN(quizzesMarks) || Double.isNaN(midTermMarks) || Double.isNaN(finalTheoryMarks) || Double.isNaN(finalPracticalMarks)) {

                                    result = null;
                                } else {
                                    result = marks.calculateFinalMarksMethod0(quizzesMarks, midTermMarks, finalTheoryMarks, finalPracticalMarks);
                                }

                                marksList.setValueAt(result, row, 9);
                                break;
                            case "Method 2":
                                if (Double.isNaN(quizzesMarks) || Double.isNaN(midTermMarks) || Double.isNaN(finalTheoryMarks) || Double.isNaN(assessmentsMarks)) {

                                    result = null;
                                } else {
                                    result = marks.calculateFinalMarksMethod1(quizzesMarks, assessmentsMarks, midTermMarks, finalTheoryMarks);
                                }

                                marksList.setValueAt(result, row, 9);
                                break;
                            case "Method 3":
                                if (Double.isNaN(quizzesMarks) || Double.isNaN(assessmentsMarks) || Double.isNaN(finalTheoryMarks) || Double.isNaN(finalPracticalMarks)) {

                                    result = null;
                                } else {
                                    result = marks.calculateFinalMarksMethod2(quizzesMarks, assessmentsMarks, finalTheoryMarks, finalPracticalMarks);
                                }

                                marksList.setValueAt(result, row, 9);
                                break;
                            case "Method 4":
                                if (Double.isNaN(quizzesMarks) || Double.isNaN(assessmentsMarks) || Double.isNaN(finalTheoryMarks) || Double.isNaN(finalPracticalMarks)) {

                                    result = null;
                                } else {
                                    result = marks.calculateFinalMarksMethod2(quizzesMarks, assessmentsMarks, finalTheoryMarks, finalPracticalMarks);
                                }
                                marksList.setValueAt(result, row, 9);
                                break;
                            default:
                                if (Double.isNaN(quizzesMarks) || Double.isNaN(assessmentsMarks) || Double.isNaN(finalTheoryMarks) || Double.isNaN(finalPracticalMarks)) {

                                    result = null;
                                } else {
                                    result = marks.calculateFinalMarksMethod2(quizzesMarks, assessmentsMarks, finalTheoryMarks, finalPracticalMarks);
                                }
                                marksList.setValueAt(result, row, 9);
                                break;
                        }
                    }

                }
            }
            jTable2.setModel(marksList);
            updateMarkeTable();
            marksList.setRowCount(0);
            populateMarksList();

        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyReleased
        // TODO add your handling code here:

        if (Character.isDefined(evt.getKeyChar())) {

            String text = jTextField8.getText();
            System.out.println("Text changed: " + text);
            Attendance attendance = new Attendance();
            if (!text.isEmpty()) {
                System.out.println("in the event");

                jTable1.removeAll();
                populateAttendenceList(attendance.searchStudentByID(text));

            } else {

                jTable1.removeAll();
                populateAttendenceList();

            }
            //jTable1.setModel();
            jTable1.setEnabled(true);
            jTable1.setDefaultEditor(Object.class, null);

        }
    }//GEN-LAST:event_jTextField8KeyReleased

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        setPanel("card4");
    }//GEN-LAST:event_jButton25ActionPerformed
    public static void LecturerRun() {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        //Profile Photo
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Lecturer_UI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lecturer_UI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lecturer_UI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lecturer_UI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("in lec run");
                Lecturer_UI lecturer = new Lecturer_UI();

                lecturer.setVisible(true);
            }
        });
    }
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        Student student = new Student();
        Attendance attendance = new Attendance();
        System.out.println(jTable1.getValueAt(jTable1.getSelectedRow(), 0));
        Dialog dialog;
        JProgressBar progressBar;
        JLabel stuNum;
        JLabel stuName = new JLabel();
        JLabel attendenceCount;
        JLabel course;
        JPanel textPanel;
        JPanel progressBarPanel;
        dialog = new JDialog(this, "Attendance Percentage", true);
        dialog.setLayout(null);
        progressBar = new JProgressBar(0, 100);
        stuNum = new JLabel("Student Number : " + String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
        course = new JLabel("Cousrse : " + (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1));
        String courseId = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
        int stuID = (int) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
        int allAtt = attendance.getALLAttendanceCount(stuID, courseId);

        int PresentAtt = attendance.getpresentAttendanceCount(stuID, courseId);
        attendenceCount = new JLabel("Attendance : " + PresentAtt + " Out of " + allAtt);
        double attendancePercentage = PresentAtt / allAtt * 100.00;

        try {
            ResultSet rs = student.getstudentById((int) jTable1.getValueAt(jTable1.getSelectedRow(), 0));

            if (rs != null) {
                if (rs.next()) {
                    stuName.setText("Student NAME : " + rs.getString("Name"));
                } else {
                    stuName.setText("Student NAME : NOT FOUND");
                }
            } else {
                stuName.setText("Student NAME : NOT FOUND");

            }
            System.out.println("nam:" + stuName.getText());
        } catch (SQLException ex) {
            String message = "SQL Error: " + ex.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        progressBar.setStringPainted(true);
        progressBar.setValue((int) attendancePercentage);

        textPanel = new JPanel();
        textPanel.setLayout(null);
        progressBarPanel = new JPanel();
        progressBarPanel.setLayout(null);

        stuNum.setBounds(20, 20, 300, 30);
        stuName.setBounds(20, 40, 300, 30);
        course.setBounds(20, 60, 300, 30);
        attendenceCount.setBounds(20, 80, 300, 30);
        progressBar.setBounds(20, 10, 400, 30);

        textPanel.add(attendenceCount);
        textPanel.add(stuNum);
        textPanel.add(stuName);
        textPanel.add(course);
        progressBarPanel.add(progressBar);

        textPanel.setBounds(0, 0, 450, 180);
        progressBarPanel.setBounds(0, 170, 450, 50);

        dialog.add(textPanel);
        dialog.add(progressBarPanel);

        dialog.setSize(450, 270);
        dialog.setLocationRelativeTo(this);

        dialog.setVisible(true);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    /**
     * @param args the command line arguments
     */



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Announcements;
    private javax.swing.JPanel Course;
    private javax.swing.JPanel Course_details;
    private javax.swing.JPanel Main;
    private javax.swing.JPanel Profile;
    private javax.swing.JPanel Stu_managment;
    private javax.swing.JPanel Stu_marks;
    private javax.swing.JPanel Student_attendence;
    private javax.swing.JTable TimeTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList3;
    private javax.swing.JList<String> jList4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables

}
