package by.teachmeskills.db.client;

import by.teachmeskills.db.dto.Group;
import lombok.extern.log4j.Log4j2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class GroupDBClient extends DBClient {
    private static final String GROUPS_TABLE_NAME = "`groups`";
    private static final String INSERT_GROUP = "INSERT INTO " + GROUPS_TABLE_NAME + "(name, teacher) VALUES (?, ?);";

    public List<Group> selectAllGroups() {
        ResultSet groupEntries = selectAllRows(GROUPS_TABLE_NAME);
        List<Group> groups = new ArrayList<>();
        try {
            while (groupEntries.next()) {
                groups.add(new Group(groupEntries.getInt("id"), groupEntries.getString("name"),
                        groupEntries.getString("teacher")));
            }
        } catch (SQLException ex) {
            log.error("Groups were not found because of error: {}", ex.getMessage());
        }
        return groups;
    }

    public Group selectGroupById(String id){
        ResultSet group = executeQuery(String.format("SELECT * FROM %s WHERE id = %s", GROUPS_TABLE_NAME, id));
        List<Group> groups = new ArrayList<>();
        try {
            while (group.next()) {
                groups.add(new Group(group.getInt("id"), group.getString("name"),
                        group.getString("teacher")));
            }
        } catch (SQLException ex) {
            log.error("Groups were not found because of error: {}", ex.getMessage());
        }
        return groups.get(0);
    }

    public List<String> getGroupsNames() {
        ResultSet groupEntries = selectAllRows(GROUPS_TABLE_NAME);
        List<String> groups = new ArrayList<>();

        try {
            while (groupEntries.next()) {
                groups.add(groupEntries.getString("name"));
            }
        } catch (SQLException ex) {
            log.error("Group names were not found because of error: {}", ex.getMessage());
        }
        return groups;
    }

    public void createGroup(String groupName, String teacher) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_GROUP);
            preparedStatement.setString(1, groupName);
            preparedStatement.setString(2, teacher);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Group was not created because of error: {}", ex.getMessage());
        }
    }

    public void deleteGroupByName(String groupName) {
        deleteRows(GROUPS_TABLE_NAME, String.format("name = '%s'", groupName));
    }

    public void updateGroupName(String presentName, String newName) {
        updateRows(GROUPS_TABLE_NAME, String.format("name = '%s'", newName), String.format("name = '%s'", presentName));
    }
}
