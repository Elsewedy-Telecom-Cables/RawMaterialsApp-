package com.etc.raw_materials_app.dao;

import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.Material;
import com.etc.raw_materials_app.models.MaterialDocument;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialDocumentDao {
   public static String lastErrorMessage = null;
    // INSERT
    public static boolean insertMaterialDocument(MaterialDocument materialDocument) {
        String sql = "INSERT INTO material_testing.dbo.material_documents (material_doc_name, material_id) VALUES (?,?)";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, materialDocument.getMaterialDocumentName());
            if (materialDocument.getMaterial() == null)
                throw new IllegalArgumentException("Material cannot be null");
            ps.setInt(2, materialDocument.getMaterial().getMaterialId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialDocumentDao.class.getName(), "insertMaterialDocument", e, "sql", sql);
            return false;
        }
    }

    // UPDATE
    public static boolean updateMaterialDocument(MaterialDocument materialDocument) {
     String sql = "UPDATE material_testing.dbo.material_documents SET material_doc_name = ?, material_id = ? WHERE material_doc_id = ?";
     try (Connection con = DbConnect.getConnect();
     PreparedStatement ps = con.prepareStatement(sql)){
         ps.setString(1, materialDocument.getMaterialDocumentName());
         if (materialDocument.getMaterial() == null)
             throw new IllegalArgumentException("Material cannot be null");
         ps.setInt(2, materialDocument.getMaterial().getMaterialId());
         ps.setInt(3, materialDocument.getMaterialDocumentId());
         return ps.executeUpdate() > 0;
     } catch (SQLException e) {
         lastErrorMessage = e.getMessage();
         Logging.logExpWithMessage("ERROR", MaterialDocumentDao.class.getName(), "updateMaterialDocument", e, "sql", sql);
         return false;
     }
    }

   // DELETE
    public static boolean deleteMaterialDocument(int materialDocumentId) {
        String sql = "DELETE FROM material_testing.dbo.material_documents WHERE material_doc_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, materialDocumentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialDocumentDao.class.getName(), "deleteMaterialDocument", e, "sql", sql);
            return false;
        }
    }

    // GET ALL MATERIAL DOCUMENTS
    public static ObservableList<MaterialDocument> getAllMaterialDocuments() {
        ObservableList<MaterialDocument> list = FXCollections.observableArrayList();
        String sql = """
                SELECT md.material_doc_id, md.material_doc_name, m.material_id, m.material_name
                FROM material_testing.dbo.material_documents md
                JOIN material_testing.dbo.materials m ON md.material_id = m.material_id
                """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             while(rs.next()){
                 MaterialDocument md = new MaterialDocument();
                 md.setMaterialDocumentId(rs.getInt("material_doc_id"));
                 md.setMaterialDocumentName(rs.getString("material_doc_name"));
                 Material m = new Material();
                 m.setMaterialId(rs.getInt("material_id"));
                 m.setMaterialName(rs.getString("material_name"));
                 md.setMaterial(m);
                 list.add(md);
             }
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", MaterialDocumentDao.class.getName(), "getAllMaterialDocuments", e, "sql", sql);
        }
        return list;
    }

}



