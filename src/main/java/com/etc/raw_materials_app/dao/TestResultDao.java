package com.etc.raw_materials_app.dao;

import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.MaterialTest;
import com.etc.raw_materials_app.models.TestResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class TestResultDao {
    public static String lastErrorMessage;

    public  Integer insertTestResult(TestResult testResult) {
        String sql = """
        INSERT INTO material_testing.dbo.test_results
            (material_test_id, test_name_id, sample_id, user_id, requirement, actual, creation_date, test_situation)
        VALUES (?, ?,?, ?, ?, ?, ?, ?);
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, testResult.getMaterialTestId());  // Fix: Use direct getMaterialTestId() instead of getMaterialTest().getMaterialTestId()
            ps.setInt(2, testResult.getTestNameId());
            ps.setInt(3, testResult.getSampleId());
            ps.setInt(4, testResult.getUserId());
            ps.setString(5, testResult.getRequirement());
            ps.setString(6, testResult.getActual());
            ps.setObject(7, testResult.getCreationDate());
            ps.setObject(8, testResult.getTestSituation());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting test result failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Inserting test result failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", TestResultDao.class.getName(), "insertTestResult", e, "sql", sql);
            return null;
        }
    }


    public  ObservableList<TestResult> getAllTestResults() {
        ObservableList<TestResult> list = FXCollections.observableArrayList();
        String sql = """
            SELECT tr.test_result_id, tr.material_test_id, tr.sample_id, tr.test_name_id, tr.user_id,
                   tr.requirement, tr.actual, tr.creation_date, tr.test_situation,
                   tn.test_name, u.full_name,
                   s.supplier_name, m.material_name,m.item_code,
                   mt.po_no, mt.oracle_sample, sa.sample_name
            FROM material_testing.dbo.test_results tr
            LEFT JOIN material_testing.dbo.test_names tn ON tr.test_name_id = tn.test_name_id
            LEFT JOIN material_testing.dbo.users u ON tr.user_id = u.user_id
            LEFT JOIN material_testing.dbo.material_tests mt ON tr.material_test_id = mt.material_test_id
            LEFT JOIN material_testing.dbo.suppliers s ON mt.supplier_id = s.supplier_id
            LEFT JOIN material_testing.dbo.materials m ON mt.material_id = m.material_id
            LEFT JOIN material_testing.dbo.samples sa ON tr.sample_id = sa.sample_id
            ORDER BY tr.sample_id , creation_date ASC
        """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TestResult tr = new TestResult();
                tr.setTestResultId(rs.getInt("test_result_id"));
                tr.setMaterialTestId(rs.getInt("material_test_id"));
                tr.setTestNameId(rs.getInt("test_name_id"));
                tr.setSampleId(rs.getInt("sample_id"));
                tr.setUserId(rs.getInt("user_id"));
                tr.setRequirement(rs.getString("requirement"));
                tr.setActual(rs.getString("actual"));
                Timestamp ts = rs.getTimestamp("creation_date");
                tr.setCreationDate(ts != null ? ts.toLocalDateTime() : null);
                tr.setTestSituation(rs.getObject("test_situation") != null ? rs.getInt("test_situation") : null);
                tr.setTestName(rs.getString("test_name"));
                tr.setUserFullName(rs.getString("full_name"));
                tr.setSampleName(rs.getString("sample_name"));
                MaterialTest mt = new MaterialTest();
                mt.setSupplierName(rs.getString("supplier_name"));
                mt.setMaterialName(rs.getString("material_name"));
                mt.setItemCode(rs.getString("item_code"));
                mt.setPoNo(rs.getString("po_no"));
                mt.setOracleSample(rs.getString("oracle_sample"));


                tr.setMaterialTest(mt);
                list.add(tr);
            }
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", TestResultDao.class.getName(), "getAllTestResults", e, "sql", sql);
        }
        return list;
    }

    public  TestResult getTestResultById(int id) {
        TestResult tr = null;
        String sql = """
            SELECT tr.test_result_id, tr.material_test_id, tr.test_name_id, tr.user_id,
                   tr.requirement, tr.actual, tr.creation_date, tr.test_situation, tr.sample_id
                   tn.test_name, u.full_name,
                   s.supplier_name, m.material_name,m.item_code,
                   mt.po_no, mt.oracle_sample ,sa.sample_name
            FROM material_testing.dbo.test_results tr
            LEFT JOIN material_testing.dbo.test_names tn ON tr.test_name_id = tn.test_name_id
            LEFT JOIN material_testing.dbo.users u ON tr.user_id = u.user_id
            LEFT JOIN material_testing.dbo.material_tests mt ON tr.material_test_id = mt.material_test_id
            LEFT JOIN material_testing.dbo.suppliers s ON mt.supplier_id = s.supplier_id
            LEFT JOIN material_testing.dbo.materials m ON mt.material_id = m.material_id
            LEFT JOIN material_testing.dbo.samples sa ON tr.sample_id = sa.sample_id
            WHERE tr.test_result_id = ?
            ORDER BY tr.sample_id , creation_date ASC
        """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tr = new TestResult();
                    tr.setTestResultId(rs.getInt("test_result_id"));
                    tr.setMaterialTestId(rs.getInt("material_test_id"));
                    tr.setTestNameId(rs.getInt("test_name_id"));
                    tr.setSampleId(rs.getInt("sample_id"));
                    tr.setSampleName(rs.getString("sample_name"));
                    tr.setUserId(rs.getInt("user_id"));
                    tr.setRequirement(rs.getString("requirement"));
                    tr.setActual(rs.getString("actual"));
                    Timestamp ts = rs.getTimestamp("creation_date");
                    tr.setCreationDate(ts != null ? ts.toLocalDateTime() : null);
                    tr.setTestSituation(rs.getObject("test_situation") != null ? rs.getInt("test_situation") : null);
                    tr.setTestName(rs.getString("test_name"));
                    tr.setUserFullName(rs.getString("full_name"));
                    MaterialTest mt = new MaterialTest();
                    mt.setSupplierName(rs.getString("supplier_name"));
                    mt.setMaterialName(rs.getString("material_name"));
                    mt.setItemCode(rs.getString("item_code"));
                    mt.setPoNo(rs.getString("po_no"));
                    mt.setOracleSample(rs.getString("oracle_sample"));
                    tr.setMaterialTest(mt);
                }
            }
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", TestResultDao.class.getName(), "getTestResultById", e, "sql", sql);
        }
        return tr;
    }

    public  boolean updateTestResult(TestResult tr) {
        String sql = """
        UPDATE material_testing.dbo.test_results
        SET material_test_id=?, test_name_id=?, sample_id=?, user_id=?, requirement=?, actual=?, test_situation=?
        WHERE test_result_id=?
    """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tr.getMaterialTestId());  // Fix: Use direct getMaterialTestId()
            ps.setInt(2, tr.getTestNameId());
            ps.setInt(3, tr.getSampleId());
            ps.setInt(4, tr.getUserId());
            ps.setString(5, tr.getRequirement());
            ps.setString(6, tr.getActual());
            ps.setObject(7, tr.getTestSituation());
            ps.setInt(8, tr.getTestResultId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            return false;
        }
    }


    public 
    boolean deleteTestResult(int id) {
        String sql = "DELETE FROM material_testing.dbo.test_results WHERE test_result_id=?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", TestResultDao.class.getName(), "deleteTestResult", e, "sql", sql);
            return false;
        }
    }


}
