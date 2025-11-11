package com.etc.raw_materials_app.dao;
import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.Section;
import com.etc.raw_materials_app.models.TestName;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class TestNameDao {
    public static String lastErrorMessage = null;
    // Get all TestNames
    public  ObservableList<TestName> getAllTestNames() {
        ObservableList<TestName> list = FXCollections.observableArrayList();
        String query = """
        SELECT test_name_id, test_name
        FROM material_testing.dbo.test_names
        ORDER BY test_name asc
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TestName tn = new TestName();
                tn.setTestNameId(rs.getInt("test_name_id"));
                tn.setTestName(rs.getString("test_name"));
                list.add(tn);

            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TestNameDao.class.getName(), "getAllTestNames", e, "sql", query);
        }
        return list;
    }

    // Get a  Test Name by ID
    public  Section getTestNameById(int testId) {
        String query = "SELECT test_name_id, test_name FROM material_testing.dbo.test_names WHERE test_name_id = ?";
        Section section = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, testId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                  section.setSectionId(rs.getInt("test_name_id"));
                  section.setSectionName(rs.getString("test_name"));
                  return section;
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TestNameDao.class.getName(), "getTestNameById", e, "sql", query);
        }
        return section;
    }

    // Insert a new Test Name
    public  int insertTestName(TestName testName ) {
        String query = "INSERT INTO material_testing.dbo.test_names (test_name) VALUES (?)";
        int generatedId = 0 ;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, testName.getTestName());
            int affectedRows = ps.executeUpdate();   // Store affected rows that already inserted into DB usually = 1

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);   // mean return first colum from result set
                    }
                }
            }

        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", TestNameDao.class.getName(), "insertTestName", e, "sql", query);
            generatedId = 0;
        }
        return generatedId;
    }


    // Update an existing Test Name
    public  boolean updateTestName(TestName testName) {
        String query = "UPDATE material_testing.dbo.test_names SET test_name = ? WHERE test_name_id = ?";
        boolean success = false;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, testName.getTestName());
            ps.setInt(2, testName.getTestNameId());
            int affectedRows = ps.executeUpdate();
            success = affectedRows > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TestNameDao.class.getName(), "updateTestName", e, "sql", query);
        }
        return success;
    }

    // Delete a Test Name
    public  boolean deleteTestName(int testId) {
        String query = "DELETE FROM material_testing.dbo.test_names WHERE test_name_id = ?";
        boolean success = false;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, testId);
            int affectedRows = ps.executeUpdate();
            success = affectedRows > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", TestNameDao.class.getName(), "deleteTestName", e, "sql", query);
        }
        return success;
    }
}
