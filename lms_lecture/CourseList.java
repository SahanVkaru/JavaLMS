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
import com.sun.jdi.connect.spi.Connection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.CodeSigner;
import java.security.PublicKey;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.Border;

public class CourseList {

    private DefaultListModel<Course> listModel;
    private JList<Course> courseList;
    private CourseClickListener courseClickListener;
    private Lecturer_UI L_UI;
    DB_Connection dB_Connection = new DB_Connection();
    java.sql.Connection conn = dB_Connection.getConnection();
    String sort = "";
    public String course_metrial = "SELECT * FROM course_metrial WHERE course_Course_id = ?";
    public String Courses_SQL_Offered_department = "SELECT * FROM course WHERE Offered_department = ? order by " + sort;
    public String Courses_SQL_Lecture_Incharge = "SELECT * FROM course WHERE Lecture_Incharge = ? order by " + sort;
    public String Courses_SQL_TodaysCourse = "SELECT c.* FROM course c JOIN (SELECT DISTINCT course_Course_id FROM timetable WHERE DAY = ?) AS t ON c.Course_id = t.course_Course_id WHERE c.Lecture_Incharge = ? order by " + sort;
    public String Courses_SQL_Order = "SELECT * FROM course ";

    public ResultSet Courses_Lecture_Incharge(String var) {
        String Courses_SQL = "SELECT * FROM course WHERE Lecture_Incharge = ?";
        PreparedStatement pstmt;
        ResultSet resultofCourses_SQL = null;
        try {
            pstmt = conn.prepareStatement(Courses_SQL);
            pstmt.setInt(1, Integer.parseInt(var));
            resultofCourses_SQL = pstmt.executeQuery();

        } catch (SQLException ex) {
            String message = "SQL Error: " + ex.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return resultofCourses_SQL;

    }

    public void LoadSortedList(String Courses_SQL, int var, String SortVar, JPanel jPanel, Lecturer_UI L_UI) {
        listModel = new DefaultListModel<>();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE");
        String dayOfWeek = currentDate.format(formatter);
        if ("Saturday".equals(dayOfWeek) || "Sunday".equals(dayOfWeek)) {
            dayOfWeek = "Monday";
        }

        PreparedStatement pstmt;
        ResultSet resultofCourses_SQL;

        try {
            sort = SortVar;
            Courses_SQL += sort;
            System.out.println(Courses_SQL);
            pstmt = conn.prepareStatement(Courses_SQL);

            pstmt.setString(1, dayOfWeek);
            pstmt.setInt(2, var);
            resultofCourses_SQL = pstmt.executeQuery();

            listModel.removeAllElements();
            jPanel.removeAll();
            System.out.println(resultofCourses_SQL);
            while (resultofCourses_SQL.next()) {
                Course course = new Course(resultofCourses_SQL.getString("courses_background"), resultofCourses_SQL.getString("Name"), resultofCourses_SQL.getString("Course_id"));
                listModel.addElement(course);
                //System.out.println("resultofCourses_SQL.getString(\"Name\")"+resultofCourses_SQL.getString("Name")+"courses_background "+resultofCourses_SQL.getString("courses_background"));
            }

            jPanel.removeAll();

            courseList = new JList<>(listModel);
            courseList.setCellRenderer(new CourseCellRenderer());
            courseList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            courseList.setVisibleRowCount(-1);
            courseList.setFixedCellHeight(260);

            // Attach mouse listener to the new courseList
            courseClickListener = new CourseClickListener();
            courseList.addMouseListener(courseClickListener);
            courseClickListener.setCourseSelectionListener((selectedCode, selectedName) -> {
                L_UI.setPanel("card7");
                L_UI.updateCourseDetails(selectedCode, selectedName);
                try {
                    PreparedStatement pstmtMetrial = conn.prepareStatement(course_metrial);
                    pstmtMetrial.setString(1, selectedCode);
                    ResultSet resultofcourse_metrial = pstmtMetrial.executeQuery();
                    L_UI.removeAllListElement();
                    while (resultofcourse_metrial.next()) {
                        Course_Metrial cm = new Course_Metrial(resultofcourse_metrial.getString("File_url"), resultofcourse_metrial.getString("Describition"), resultofcourse_metrial.getString("idCourse_metrial"), resultofcourse_metrial.getString("week"));
                        L_UI.updateListofContent(resultofcourse_metrial.getString("week"));
                    }
                } catch (SQLException ex) {
                    String message = "SQL Error: " + ex.getMessage();
                    System.out.println(message);

                    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                }

            });

            JScrollPane scrollPane = new JScrollPane(courseList);
            scrollPane.setPreferredSize(new Dimension(800, 300));
            scrollPane.setBackground(Color.LIGHT_GRAY);

            jPanel.setLayout(new BorderLayout());
            jPanel.add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException ex) {
            // Exception handling
        }
    }

    public void LoadSortedList(String Courses_SQL, String var, String SortVar, JPanel jPanel, Lecturer_UI L_UI) {

        listModel = new DefaultListModel<>();

        //Data From DB
        PreparedStatement pstmt;
        ResultSet resultofCourses_SQL;
        try {
            sort = SortVar;

            if (Courses_SQL.equals(Courses_SQL_Lecture_Incharge)) {
                Courses_SQL = Courses_SQL + sort;
                pstmt = conn.prepareStatement(Courses_SQL);
                pstmt.setInt(1, Integer.parseInt(var));
            } else if (Courses_SQL.equals(Courses_SQL_Order)) {
                //"123" + Courses_SQL_Order);
                sort = SortVar;
                Courses_SQL = Courses_SQL + " ORDER BY " + sort;
                //"1234" + Courses_SQL);
                pstmt = conn.prepareStatement(Courses_SQL);
            } else {
                Courses_SQL = Courses_SQL + sort;
                pstmt = conn.prepareStatement(Courses_SQL);
                pstmt.setString(1, var);
            }

            //System.out.println("Executing SQL Query: " + Courses_SQL);
            resultofCourses_SQL = pstmt.executeQuery();
            //System.out.println("Query executed successfully");
            //System.out.println("------------------------------------------------------");
            listModel.removeAllElements();
            jPanel.removeAll();

            while (resultofCourses_SQL.next()) {
                Course course = new Course(resultofCourses_SQL.getString("courses_background"), resultofCourses_SQL.getString("Name"), resultofCourses_SQL.getString("Course_id"));
                //System.out.println(resultofCourses_SQL.getString("Name"));
                listModel.addElement(course);

            }
            //System.out.println("------------------------------------------------------");
            //stmt.close();
            courseList = new JList<>(listModel);
            courseList.setCellRenderer(new CourseCellRenderer());
            courseList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            courseList.setVisibleRowCount(-1);
            courseList.setFixedCellHeight(260);

            // Attach mouse listener to the new courseList
            courseClickListener = new CourseClickListener();
            courseList.addMouseListener(courseClickListener);
            courseClickListener.setCourseSelectionListener((selectedCode, selectedName) -> {
                L_UI.setPanel("card7");
                L_UI.updateCourseDetails(selectedCode, selectedName);

                try {
                    PreparedStatement pstmtMetrial = conn.prepareStatement(course_metrial);
                    pstmtMetrial.setString(1, selectedCode);
                    ResultSet resultofcourse_metrial = pstmtMetrial.executeQuery();
                    L_UI.removeAllListElement();
                    while (resultofcourse_metrial.next()) {
                        Course_Metrial cm = new Course_Metrial(resultofcourse_metrial.getString("File_url"), resultofcourse_metrial.getString("Describition"), resultofcourse_metrial.getString("idCourse_metrial"), resultofcourse_metrial.getString("week"));
                        L_UI.updateListofContent(resultofcourse_metrial.getString("week"));
                    }
                } catch (SQLException ex) {
                    String message = "SQL Error: " + ex.getMessage();
                    System.out.println(message);

                    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            JScrollPane scrollPane = new JScrollPane(courseList);
            scrollPane.setPreferredSize(new Dimension(800, 600));
            scrollPane.setBackground(Color.LIGHT_GRAY);

            jPanel.setLayout(new BorderLayout());
            jPanel.add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException ex) {
            String message = "SQL Error: " + ex.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public CourseList() {

    }

    public CourseList(JPanel jPanel, Lecturer_UI L_UI) {

        this.L_UI = L_UI;
        LoadSortedList(Courses_SQL_Order, "", "Course_id", jPanel, L_UI);

    }

    public CourseClickListener getCourseClickListener() {
        return courseClickListener;
    }

    private class Course {

        private String imagePath;
        private String name;
        private String Code;

        public Course(String imagePath, String name, String Code) {
            this.imagePath = imagePath;
            this.name = name;
            this.Code = Code;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return Code;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    private class Course_Metrial {

        private String imagePath;
        private String name;
        private String Code;
        private String Week;

        public Course_Metrial(String FilePath, String Describition, String Code, String Week) {
            this.imagePath = FilePath;
            this.name = Describition;
            this.Code = Code;
            this.Week = Week;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return Code;
        }

        public String getWeek() {
            return Week;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    private class CourseCellRenderer extends JPanel implements ListCellRenderer<Course> {

        private JLabel nameLabel = new JLabel();
        private JLabel imageLabel = new JLabel();

        public CourseCellRenderer() {
            setLayout(new BorderLayout());
            add(nameLabel, BorderLayout.SOUTH);
            add(imageLabel, BorderLayout.CENTER);
            //setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            setPreferredSize(new Dimension(220, 240));
            //courseList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            Border innerPadding = BorderFactory.createEmptyBorder(10, 10, 0, 10);

            Border outerBorder = BorderFactory.createLineBorder(Color.GRAY, 2);

            Border outerPadding = BorderFactory.createEmptyBorder(10, 10, 10, 10);

            setBorder(BorderFactory.createCompoundBorder(
                    outerPadding,
                    BorderFactory.createCompoundBorder(
                            outerBorder,
                            innerPadding)));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Course> list, Course value, int index, boolean isSelected, boolean cellHasFocus) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            ImageIcon icon = new ImageIcon(value.getImagePath());
            Image image = icon.getImage();
            Image ScaledImage = image.getScaledInstance(160, 160, Image.SCALE_SMOOTH);
            icon = new ImageIcon(ScaledImage);
            imageLabel.setIcon(icon);

            imageLabel.setPreferredSize(new Dimension(160, 160));
            nameLabel.setPreferredSize(new Dimension(160, 40));
            nameLabel.setText(value.getName());

            nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
            imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
            courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            courseList.setSelectionBackground(Color.WHITE);
            courseList.setSelectionForeground(Color.WHITE);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setOpaque(true);

            return this;
        }

    }

    private class CourseClickListener extends MouseAdapter {

        public String Selected_Code = "";
        private CourseSelectionListener listener;

        @Override
        public void mouseClicked(MouseEvent e) {

            Course selectedCourse = courseList.getSelectedValue();
            //JOptionPane.showMessageDialog(null, "You clicked: " + selectedCourse.getName());
            //String selectedCode = selectedCourse.getCode();
            if (listener != null && selectedCourse != null) {
                listener.onCourseSelected(selectedCourse.getCode(), selectedCourse.getName());
            }
            try {
                Selected_Code = selectedCourse.getCode();
            } catch (NullPointerException ex) {
                String message = "There is no Course To Select , The list is :" + ex.getMessage();
                System.out.println(message);
                JOptionPane.showMessageDialog(null, message, "NullPointerException", JOptionPane.WARNING_MESSAGE);
            }

            //L_UI.setPanel("card7");
            //jLabel7.setText("12345");
        }

        public void setCourseSelectionListener(CourseSelectionListener listener) {
            this.listener = listener;
        }

    }

    public interface CourseSelectionListener {

        void onCourseSelected(String selectedCode, String selectedname);
    }

}
