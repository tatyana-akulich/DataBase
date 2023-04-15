package by.teachmeskills;

import by.teachmeskills.db.client.GroupDBClient;
import by.teachmeskills.db.client.StudentDBClient;
import by.teachmeskills.db.dto.Student;
import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class StudentsTest {
    private StudentDBClient dbClient;
    private GroupDBClient groupDBClient;

    @BeforeMethod
    public void setUp() {
        dbClient = new StudentDBClient();
        dbClient.connect();
        groupDBClient = new GroupDBClient();
        groupDBClient.connect();
    }

    @Test
    public void testStudents() {
        String expStudentName = new Faker().name().name();
        dbClient.createStudent(expStudentName, 3);
        List<Student> students = dbClient.selectAllStudents();
        log.info(students.toString());
        List<String> names = dbClient.getStudentsNames();
        assertThat(names)
                .as("List of students names should contain name of added student - {}", expStudentName)
                .contains(expStudentName);

        dbClient.deleteStudentByName(expStudentName);
        names = dbClient.getStudentsNames();
        assertThat(names)
                .as("Deleted student shouldn't be in the table")
                .doesNotContain(expStudentName);

        expStudentName = new Faker().name().name();
        dbClient.createStudent(expStudentName, 3);
        dbClient.deleteStudentByGroup(3);
        names = dbClient.getStudentsNames();
        assertThat(names)
                .as("Deleted student shouldn't be in the table")
                .doesNotContain(expStudentName);

        dbClient.deleteStudentByID(8);
        names = dbClient.getStudentsNames();
        assertThat(names)
                .as("Deleted student shouldn't be in the table")
                .doesNotContain(expStudentName);

        expStudentName = new Faker().name().name();

        dbClient.createStudent(expStudentName, 3);
        String newName = new Faker().name().name();
        dbClient.updateStudentName(expStudentName, newName);
        names = dbClient.getStudentsNames();
        assertThat(names)
                .as("Updated student name should be in the table")
                .contains(newName);

        String actGroupName = dbClient.selectStudentGroupByName(newName);
        String expectedGroupName = groupDBClient.selectGroupById("3").getName();
        assertThat(actGroupName)
                .as("Group names should be equal")
                .isEqualTo(expectedGroupName);
    }

    @AfterMethod
    public void tearDown() {
        dbClient.close();
    }
}
