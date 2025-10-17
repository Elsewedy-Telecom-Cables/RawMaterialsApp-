package com.etc.raw_materials_app.dao;

import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.Material;
import com.etc.raw_materials_app.models.MaterialDescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class MaterialDescriptionDao {
    public static String lastErrorMessage = null;

    // Insert
    public static boolean insertMaterialDescription(MaterialDescription materialDescription) {
        String sql = """
                INSERT INTO material_testing.dbo.material_descriptions (material_des_name, material_id) VALUES (?,?)
                """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, materialDescription.getMaterialDescriptionName());
            ps.setInt(2, materialDescription.getMaterial().getMaterialId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialDescriptionDao.class.getName(), "insertMaterialDescription", e, "sql", sql);
            return false;
        }
    }

    // Insert To Learning only
    public static int insertMaterialDescription2(MaterialDescription materialDescription) {
        String sql = """
                INSERT INTO material_testing.dbo.material_descriptions (material_des_name, material_id) VALUES (?,?)
                """;
        int generatedId = 0;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, materialDescription.getMaterialDescriptionName());
            ps.setInt(2, materialDescription.getMaterial().getMaterialId());
            ps.executeUpdate();
            int affectedRows = ps.executeUpdate();   // Store affected rows that already inserted into DB usually = 1
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);   // mean return first colum from result set
                    }
                }
            }
            return generatedId;
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialDescriptionDao.class.getName(), "insertMaterialDescription", e, "sql", sql);
            generatedId = 0;
        }
        return generatedId;
    }

    // Update
    public static boolean updateMaterialDescription(MaterialDescription materialDescription) {
        String sql = """
                UPDATE material_testing.dbo.material_descriptions SET material_des_name = ? , material_id = ? WHERE material_des_id = ?
                """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql);) {
             ps.setString(1,materialDescription.getMaterialDescriptionName());
             ps.setInt(2,materialDescription.getMaterial().getMaterialId());
             ps.setInt(3,materialDescription.getMaterialDescriptionId());
             return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialDescriptionDao.class.getName(), "updateMaterialDescription", e, "sql", sql);
            return false;
        }

    }

    // Delete
    public static boolean deleteMaterialDescription(int materialDesId) {
        String sql = "DELETE FROM material_testing.dbo.material_descriptions WHERE material_des_id = ?";
        try(Connection con = DbConnect.getConnect();
        PreparedStatement ps = con.prepareStatement(sql);){
            ps.setInt(1,materialDesId);
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialDescriptionDao.class.getName(), "deleteMaterialDescription", e, "sql", sql);
            return false;
        }
    }

    // Get All Material Descriptions
    public static ObservableList<MaterialDescription> getAllMaterialDescriptions(){
        ObservableList<MaterialDescription> list = FXCollections.observableArrayList();
        String sql = """
                SELECT md.material_des_id, md.material_des_name,m.material_id, m.material_name FROM material_testing.dbo.material_descriptions md
                JOIN material_testing.dbo.materials m ON md.material_id = m.material_id
                """;
        try(Connection con = DbConnect.getConnect();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){
         while(rs.next()){
             MaterialDescription md = new MaterialDescription();
             md.setMaterialDescriptionId(rs.getInt("material_des_id"));
             md.setMaterialDescriptionName(rs.getString("material_des_name"));
             Material m = new Material();
             m.setMaterialId(rs.getInt("material_id"));
             m.setMaterialName(rs.getString("material_name"));
             md.setMaterial(m);
             list.add(md);
         }

    }catch(SQLException e){
        lastErrorMessage = e.getMessage();
        Logging.logExpWithMessage("ERROR", MaterialDescriptionDao.class.getName(), "getAllMaterialDescriptions", e, "sql", sql);
        }
        return list;
    }


}
