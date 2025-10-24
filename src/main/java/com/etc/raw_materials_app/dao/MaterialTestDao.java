package com.etc.raw_materials_app.dao;

import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.MaterialTest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class MaterialTestDao {
    private static String lastErrorMessage;

    Connection con = null ;

    private static String toNullable(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }


    public static boolean insertMaterialTest(MaterialTest materialTest) {
        String sql = """
                INSERT INTO material_testing.dbo.material_test
                	( section_id, supplier_id, country_id,
                	 material_id, material_des_id, user_id, po_no, receipt,
                	  total_quantity, quantity_accepted, quantity_rejected, creation_date
                , oracle_sample, item_code, spqr, notes, comment)
                 VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );
                """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, materialTest.getSectionId());
            ps.setInt(2, materialTest.getSupplierId());
            ps.setInt(3, materialTest.getCountryId());
            ps.setInt(4, materialTest.getMaterialId());
            ps.setInt(5, materialTest.getMaterialDescId());
            ps.setInt(6, materialTest.getUserId());
            ps.setString(7,  toNullable(materialTest.getPoNo()));
            ps.setString(8,  toNullable(materialTest.getReceipt()));
            ps.setString(9,  toNullable(materialTest.getTotalQuantity()));
            ps.setString(10, toNullable(materialTest.getQuantityAccepted()));
            ps.setString(11, toNullable(materialTest.getQuantityRejected()));
            ps.setObject(12, materialTest.getCreationDate());
            ps.setString(13, toNullable(materialTest.getOracleSample()));
            ps.setString(14, toNullable(materialTest.getItemCode()));
            ps.setString(15, toNullable(materialTest.getSpqr()));
            ps.setString(16, toNullable(materialTest.getNotes()));
            ps.setString(17, toNullable(materialTest.getComment()));
            return ps.executeUpdate() > 0;

        } catch (SQLException e){
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialTestDao.class.getName(), "insertMaterialTest", e, "sql", sql);
            return false;
        }
    }

    public static boolean updateMaterialTest(MaterialTest materialTest){
        String sql = """
            UPDATE material_testing.dbo.material_test
            SET section_id = ?,
                supplier_id = ?,country_id = ?,material_id = ?,
                material_des_id = ?,user_id = ?,po_no = ?,
                receipt = ?,total_quantity = ?,quantity_accepted = ?,
                quantity_rejected = ?,oracle_sample = ?,
                item_code = ?,spqr = ?,notes = ?,comment = ?
            WHERE material_test_id = ?
            """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, materialTest.getSectionId());
            ps.setInt(2, materialTest.getSupplierId());
            ps.setInt(3, materialTest.getCountryId());
            ps.setInt(4, materialTest.getMaterialId());
            ps.setInt(5, materialTest.getMaterialDescId());
            ps.setInt(6, materialTest.getUserId());
            ps.setString(7,  toNullable(materialTest.getPoNo()));
            ps.setString(8,  toNullable(materialTest.getReceipt()));
            ps.setString(9,  toNullable(materialTest.getTotalQuantity()));
            ps.setString(10, toNullable(materialTest.getQuantityAccepted()));
            ps.setString(11, toNullable(materialTest.getQuantityRejected()));
            ps.setString(12, toNullable(materialTest.getOracleSample()));
            ps.setString(13, toNullable(materialTest.getItemCode()));
            ps.setString(14, toNullable(materialTest.getSpqr()));
            ps.setString(15, toNullable(materialTest.getNotes()));
            ps.setString(16, toNullable(materialTest.getComment()));
            ps.setInt(17, materialTest.getMaterialTestId());
            return ps.executeUpdate() > 0;

        }catch (SQLException e){
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialTestDao.class.getName(), "updateMaterialTest", e, "sql", sql);
            return false;
        }
    }

    public static boolean deleteMaterialTest(int materialTestId){
        String sql = "DELETE FROM material_testing.dbo.material_test WHERE material_test_id = ?";
        try(Connection con = DbConnect.getConnect();
        PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, materialTestId);
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialTestDao.class.getName(), "deleteMaterialTest", e, "sql", sql);
            return false;
        }
    }

    public static MaterialTest getMaterialTestById(int materialTestId) {
       // String sql = "SELECT * FROM material_testing.dbo.material_test WHERE material_test_id = ?";
        String sql = """
        SELECT
            mt.*,
            se.section_name, su.supplier_name, su.supplier_code,
            co.country_name, ma.material_name, mad.material_des_name,
            us.full_name
        FROM material_testing.dbo.material_test mt
        LEFT JOIN material_testing.dbo.sections se ON mt.section_id = se.section_id
        LEFT JOIN material_testing.dbo.suppliers su ON mt.supplier_id = su.supplier_id
        LEFT JOIN material_testing.dbo.countries co ON mt.country_id = co.country_id
        LEFT JOIN material_testing.dbo.materials ma ON mt.material_id = ma.material_id
        LEFT JOIN material_testing.dbo.material_descriptions mad
            ON mt.material_des_id = mad.material_des_id
        LEFT JOIN material_testing.dbo.users us ON mt.user_id = us.user_id
        WHERE mt.material_test_id = ?
        ORDER BY mt.creation_date DESC
        """;
        MaterialTest mt = null;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, materialTestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mt = new MaterialTest();
                    mt.setMaterialTestId(rs.getInt("material_test_id"));
                    mt.setSectionId(rs.getInt("section_id"));
                    mt.setSupplierId(rs.getInt("supplier_id"));
                    mt.setCountryId(rs.getInt("country_id"));
                    mt.setMaterialId(rs.getInt("material_id"));
                    mt.setMaterialDescId(rs.getInt("material_des_id"));
                    mt.setUserId(rs.getInt("user_id"));
                    mt.setPoNo(rs.getString("po_no"));
                    mt.setReceipt(rs.getString("receipt"));
                    mt.setTotalQuantity(rs.getString("total_quantity"));
                    mt.setQuantityAccepted(rs.getString("quantity_accepted"));
                    mt.setQuantityRejected(rs.getString("quantity_rejected"));
                    mt.setCreationDate(rs.getTimestamp("creation_date") != null
                            ? rs.getTimestamp("creation_date").toLocalDateTime() : null);
                    mt.setOracleSample(rs.getString("oracle_sample"));
                    mt.setItemCode(rs.getString("item_code"));
                    mt.setSpqr(rs.getString("spqr"));
                    mt.setNotes(rs.getString("notes"));
                    mt.setComment(rs.getString("comment"));

                    mt.setSectionName(rs.getString("section_name"));
                    mt.setSupplierName(rs.getString("supplier_name"));
                    mt.setSupplierCode(rs.getString("supplier_code"));
                    mt.setCountryName(rs.getString("country_name"));
                    mt.setMaterialName(rs.getString("material_name"));
                    mt.setMaterialDesName(rs.getString("material_des_name"));
                    mt.setUserFullName(rs.getString("full_name"));
               }
            }

        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialTestDao.class.getName(), "getMaterialTestById", e, "sql", sql);
            return null;
        }
        return mt;
    }

    public static ObservableList<MaterialTest> getAllMaterialTest() {
        ObservableList<MaterialTest> list = FXCollections.observableArrayList();
        String sql = """
        SELECT
            mt.*,
            se.section_name, su.supplier_name, su.supplier_code,
            co.country_name, ma.material_name, mad.material_des_name,
            us.full_name
        FROM material_testing.dbo.material_test mt
        LEFT JOIN material_testing.dbo.sections se ON mt.section_id = se.section_id
        LEFT JOIN material_testing.dbo.suppliers su ON mt.supplier_id = su.supplier_id
        LEFT JOIN material_testing.dbo.countries co ON mt.country_id = co.country_id
        LEFT JOIN material_testing.dbo.materials ma ON mt.material_id = ma.material_id
        LEFT JOIN material_testing.dbo.material_descriptions mad
            ON mt.material_des_id = mad.material_des_id
        LEFT JOIN material_testing.dbo.users us ON mt.user_id = us.user_id
        ORDER BY mt.creation_date DESC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MaterialTest mt = new MaterialTest();
                mt.setMaterialTestId(rs.getInt("material_test_id"));
                mt.setSectionId(rs.getInt("section_id"));
                mt.setSupplierId(rs.getInt("supplier_id"));
                mt.setCountryId(rs.getInt("country_id"));
                mt.setMaterialId(rs.getInt("material_id"));
                mt.setMaterialDescId(rs.getInt("material_des_id"));
                mt.setUserId(rs.getInt("user_id"));
                mt.setPoNo(rs.getString("po_no"));
                mt.setReceipt(rs.getString("receipt"));
                mt.setTotalQuantity(rs.getString("total_quantity"));
                mt.setQuantityAccepted(rs.getString("quantity_accepted"));
                mt.setQuantityRejected(rs.getString("quantity_rejected"));
                mt.setCreationDate(rs.getTimestamp("creation_date") != null
                        ? rs.getTimestamp("creation_date").toLocalDateTime() : null);
                mt.setOracleSample(rs.getString("oracle_sample"));
                mt.setItemCode(rs.getString("item_code"));
                mt.setSpqr(rs.getString("spqr"));
                mt.setNotes(rs.getString("notes"));
                mt.setComment(rs.getString("comment"));

                mt.setSectionName(rs.getString("section_name"));
                mt.setSupplierName(rs.getString("supplier_name"));
                mt.setSupplierCode(rs.getString("supplier_code"));
                mt.setCountryName(rs.getString("country_name"));
                mt.setMaterialName(rs.getString("material_name"));
                mt.setMaterialDesName(rs.getString("material_des_name"));
                mt.setUserFullName(rs.getString("full_name"));
                list.add(mt);
            }
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialTestDao.class.getName(),
                    "getAllMaterialTest", e, "sql", sql);
        }
        return list;
    }

    // Get the total number of MaterialTestCount
    public static int getMaterialTestCount() {
        String query = "SELECT COUNT(*) AS material_test_count FROM material_testing.dbo.material_test";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("material_test_count");
            }
        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialTestDao.class.getName(), "getMaterialTestCount", e, "sql", query);
        }
        return 0;
    }

    // Get the number of MaterialTestCount by section ID
    public static int getMaterialTestCountBySection(Integer sectionId) {
        String query = "SELECT COUNT(*) AS material_test_count FROM material_testing.dbo.material_test WHERE section_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setObject(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("material_test_count");
                }
            }
        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialTestDao.class.getName(), "getTrialsCountBySection", e, "sql", query);
        }
        return 0;
    }


}
