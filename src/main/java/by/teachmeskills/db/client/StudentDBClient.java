package by.teachmeskills.db.client;

import by.teachmeskills.db.dto.Student;
import lombok.extern.log4j.Log4j2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class StudentDBClient extends DBClient {

    private static final String STUDENTS_TABLE_NAME = "`students`";
    private static final String SELECT_STUDENTS_BY_NAME = "SELECT `groups`.`%s` FROM `students` JOIN  `groups`" +
            "ON `students`.`group_id`= `groups`.`id` WHERE `students`.`name`='%s';";
    private static final String INSERT_STUDENT = "INSERT INTO " + STUDENTS_TABLE_NAME + "(name, group_id) VALUES (?, ?);";

    public List<Student> selectAllStudents() {
        ResultSet studentEntries = selectAllRows(STUDENTS_TABLE_NAME);
        List<Student> students = new ArrayList<>();
        try {
            while (studentEntries.next()) {
                students.add(new Student(studentEntries.getInt("id"), studentEntries.getString("name"),
                        studentEntries.getInt("group_id")));
            }
        } catch (SQLException ex) {
            log.error("Students were not found because of error: {}", ex.getMessage());
        }
        return students;
    }

    public List<String> getStudentsNames() {
        ResultSet studentEntries = selectAllRows(STUDENTS_TABLE_NAME);
        List<String> students = new ArrayList<>();

        try {
            while (studentEntries.next()) {
                students.add(studentEntries.getString("name"));
            }
        } catch (SQLException ex) {
            log.error("Students were not found because of error: {}", ex.getMessage());
        }
        return students;
    }

    public String selectStudentGroupByName(String studentName) {
        ResultSet studentEntries = executeQuery(String.format(SELECT_STUDENTS_BY_NAME, "name", studentName));
        try {
            studentEntries.next();
            return studentEntries.getString("name");
        } catch (SQLException ex) {
            log.error("Student with name {} was not found because of error: {}", studentName, ex.getMessage());
        }
        return "";
    }

    public void createStudent(String studentName, int groupId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STUDENT);
            preparedStatement.setString(1, studentName);
            preparedStatement.setInt(2, groupId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Student was not created because of error: {}", ex.getMessage());
        }
    }

    public void deleteStudentByName(String studentName) {
        deleteRows(STUDENTS_TABLE_NAME, String.format("name = '%s'", studentName));
    }

    public void deleteStudentByID(int id) {
        deleteRows(STUDENTS_TABLE_NAME, String.format("id = '%s'", id));
    }

    public void deleteStudentByGroup(int group_id) {
        deleteRows(STUDENTS_TABLE_NAME, String.format("group_id= '%s'", group_id));
    }

    public void updateStudentName(String presentName, String newName) {
        updateRows(STUDENTS_TABLE_NAME, String.format("name = '%s'", newName), String.format("name = '%s'", presentName));
    }
}

