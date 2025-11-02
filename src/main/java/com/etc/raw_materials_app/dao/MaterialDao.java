package com.etc.raw_materials_app.dao;

import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.Material;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialDao {
    public static String lastErrorMessage = null;
    // Get all Materials
    public static ObservableList<Material> getAllMaterials() {
        ObservableList<Material> list = FXCollections.observableArrayList();
        String query = "SELECT Material_id, Material_name, item_code FROM material_testing.dbo.Materials ORDER BY Material_id ASC";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Material(
                        rs.getInt("Material_id"),
                        rs.getString("Material_name"),
                        rs.getString("item_code")
                ));
            }

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialDao.class.getName(), "getAllMaterials", e, "sql", query);
        }

        return list;
    }

    // Insert
    public static boolean insertMaterial(Material m) {
        String query = "INSERT INTO material_testing.dbo.Materials (Material_name,item_code) VALUES (?,?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, m.getMaterialName());
            ps.setString(2, m.getItemCode());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialDao.class.getName(), "insertMaterial", e, "sql", query);
        }

        return false;
    }

    // Update
    public static boolean updateMaterial(Material m) {
        String query = "UPDATE material_testing.dbo.Materials SET Material_name = ?, item_code = ? WHERE Material_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, m.getMaterialName());
            ps.setString(2, m.getItemCode());
            ps.setInt(3, m.getMaterialId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialDao.class.getName(), "updateMaterial", e, "sql", query);
        }

        return false;
    }

    // Delete
    public static boolean deleteMaterial(int materialId) {
        String query = "DELETE FROM material_testing.dbo.Materials WHERE Material_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, materialId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", MaterialDao.class.getName(), "deleteMaterial", e, "sql", query);
        }

        return false;
    }

    // Get by ID
    public static Material getMaterialById(int id) {
        String query = "SELECT Material_id, Material_name, item_code FROM material_testing.dbo.Materials WHERE Material_id = ?";
        Material m = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    m = new Material(rs.getInt("Material_id"),
                            rs.getString("Material_name"),
                            rs.getString("item_code"));
                }
            }

        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", MaterialDao.class.getName(), "getMaterialById", e, "sql", query);
        }

        return m;
    }
}
