package by.teachmeskills;

import by.teachmeskills.db.client.GroupDBClient;
import by.teachmeskills.db.dto.Group;
import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class GroupsTest {
    private GroupDBClient dbClient;

    @BeforeMethod
    public void setUp() {
        dbClient = new GroupDBClient();
        dbClient.connect();
    }

    @Test
    public void testGroups() {
        String expectedName = new Faker().code().asin();
        String expectedTeacher = new Faker().name().firstName();
        dbClient.createGroup(expectedName, expectedTeacher);
        List<Group> allGroups = dbClient.selectAllGroups();
        log.info(allGroups.toString());
        List<String> names = dbClient.getGroupsNames();
        assertThat(names)
                .as("List of names should contain name of added group - {}", expectedName)
                .contains(expectedName);

        dbClient.deleteGroupByName(expectedName);
        names = dbClient.getGroupsNames();
        assertThat(names)
                .as("Deleted group shouldn't be in the table")
                .doesNotContain(expectedName);

        expectedName = new Faker().code().asin();
        expectedTeacher = new Faker().name().firstName();
        dbClient.createGroup(expectedName, expectedTeacher);
        String newName = new Faker().code().isbnGroup();
        dbClient.updateGroupName(expectedName, newName);
        names = dbClient.getGroupsNames();
        assertThat(names)
                .as("Updated group name should be in the table")
                .contains(newName);
    }

    @AfterMethod
    public void tearDown() {
        dbClient.close();
    }
}
