//package com.etc.raw_materials_app.dao;
//
//import com.etc.raw_materials_app.db.DbConnect;
//import com.etc.raw_materials_app.logging.Logging;
//import com.etc.raw_materials_app.models.File;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class FileDao {
//    public static String lastErrorMessage = null;
//
//    public static boolean insertFile(File file) {
//        String sql = """
//                INSERT INTO material_testing.dbo.files
//                    ( file_id, material_test_id, material_doc_id, user_id, creation_date, file_path, comment)
//                VALUES ( ?, ?, ?, ?, ?, ?, ? );
//                """;
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, file.getFileId());
//            ps.setInt(2, file.getMaterialTest().getMaterialTestId());
//            ps.setInt(3, file.getMaterialDocumentId());
//            ps.setInt(4, file.getUserId());
//            ps.setObject(5, file.getCreationDate());
//            ps.setString(6, file.getFilePath());
//            ps.setString(7, file.getComment());
//            return ps.executeUpdate() > 0;
//
//        } catch (SQLException e) {
//            lastErrorMessage = e.getMessage();
//            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "insertFile", e, "sql", sql);
//            return false;
//        }
//    }
//
//    public static boolean updateFile(File file) {
//        String sql = """
//                        UPDATE material_testing.dbo.files SET
//                            material_test_id = ?, material_doc_id = ?, user_id = ?, file_path = ?, comment = ?
//                        WHERE file_id = ?
//                """;
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, file.getMaterialTest().getMaterialTestId());
//            ps.setInt(2, file.getMaterialDocumentId());
//            ps.setInt(3, file.getUserId());
//            ps.setString(4, file.getFilePath());
//            ps.setString(5, file.getComment());
//            ps.setInt(6, file.getFileId());
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            lastErrorMessage = e.getMessage();
//            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "updateFile", e, "sql", sql);
//            return false;
//        }
//
//    }
//
//    public static boolean deleteFile(int fileId) {
//        String sql = "DELETE FROM material_testing.dbo.files WHERE file_id = ?";
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, fileId);
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            lastErrorMessage = e.getMessage();
//            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "deleteFile", e, "sql", sql);
//            return false;
//        }
//    }
//
//    public static ObservableList<File> getAllFiles() {
//        ObservableList<File> list = FXCollections.observableArrayList();
//        String sql = """
//                      SELECT f.* ,
//                                md.material_doc_name
//                                us.full_name ,
//                                m.material_name,
//                                su.supplier_name
//                                FROM material_testing.dbo.files f
//                                LEFT JOIN material_testing.dbo.material_documents md
//                                ON f.material_doc_id = md.material_doc_id
//                                LEFT JOIN material_testing.dbo.users us
//                                ON f.user_id = us.user_id
//                                LEFT JOIN material_testing.dbo.material_tests mt
//                                ON f.material_test_id = mt.material_test_id
//                                LEFT JOIN material_testing.dbo.materials m
//                                ON mt.material_id = m.material_id
//                                LEFT JOIN material_testing.dbo.suppliers su
//                                ON mt.supplier_id = su.supplier_id
//                                ORDER BY creation_date DESC
//                """;
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()){
//             while(rs.next()){
//                 File file = new File();
//                 file.setFileId(rs.getInt("file_id"));
//                 file.setMaterialDocumentName(rs.getString("material_doc_name"));
//                 file.setUserFullName(rs.getString("full_name"));
//                 file.setCreationDate(rs.getTimestamp("creation_date") != null
//                         ? rs.getTimestamp("creation_date").toLocalDateTime() : null);
//                 file.setFilePath(rs.getString("file_path"));
//                 file.setComment(rs.getString("comment"));
//                 file.setMaterialTest(new MaterialTestDao().getMaterialTestById(rs.getInt("material_test_id")));
//                 file.setMaterialName(rs.getString("material_name"));
//                 file.setSupplierName(rs.getString("supplier_name"));
//                 list.add(file);
//             }
//        } catch (
//                SQLException e) {
//            lastErrorMessage = e.getMessage();
//            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getAllFiles", e, "sql", sql);
//        }
//        return list;
//    }
//
//    public static File getFileById(int fileId) {
//        File file = null;
//        String sql = """
//                      SELECT f.* ,
//                                md.material_doc_name
//                                us.full_name ,
//                                m.material_name,
//                                su.supplier_name
//                                FROM material_testing.dbo.files f
//                                LEFT JOIN material_testing.dbo.material_documents md
//                                ON f.material_doc_id = md.material_doc_id
//        """;
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, fileId);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    file = new File();
//                    file.setFileId(rs.getInt("file_id"));
//                    file.setMaterialDocumentName(rs.getString("material_doc_name"));
//                    file.setUserFullName(rs.getString("full_name"));
//                    file.setCreationDate(rs.getTimestamp("creation_date") != null
//                            ? rs.getTimestamp("creation_date").toLocalDateTime() : null);
//                    file.setFilePath(rs.getString("file_path"));
//                    file.setComment(rs.getString("comment"));
//                    file.setMaterialTest(new MaterialTestDao().getMaterialTestById(rs.getInt("material_test_id")));
//                    file.setMaterialName(rs.getString("material_name"));
//                    file.setSupplierName(rs.getString("supplier_name"));
//                }
//            }
//             } catch (SQLException e) {
//            lastErrorMessage = e.getMessage();
//            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getFileById", e, "sql", sql);
//        }
//        return file;
//    }
//
//    public static File getFileByFilePath(String filePath) {
//        File file = null;
//        String sql = """
//                      SELECT f.* ,
//                                md.material_doc_name
//                                us.full_name ,
//                                m.material_name,
//                                su.supplier_name
//                                FROM material_testing.dbo.files f
//                                LEFT JOIN material_testing.dbo.material_documents md
//                                ON f.material_doc_id = md.material_doc_id
//        """;
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setString(1, filePath);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    file = new File();
//                    file.setFileId(rs.getInt("file_id"));
//                    file.setMaterialDocumentName(rs.getString("material_doc_name"));
//                    file.setUserFullName(rs.getString("full_name"));
//                    file.setCreationDate(rs.getTimestamp("creation_date") != null
//                            ? rs.getTimestamp("creation_date").toLocalDateTime() : null);
//                    file.setFilePath(rs.getString("file_path"));
//                    file.setComment(rs.getString("comment"));
//                    file.setMaterialTest(new MaterialTestDao().getMaterialTestById(rs.getInt("material_test_id")));
//                    file.setMaterialName(rs.getString("material_name"));
//                    file.setSupplierName(rs.getString("supplier_name"));
//                }
//            }
//
//        } catch (SQLException e) {
//            lastErrorMessage = e.getMessage();
//            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getFileByFilePath", e, "sql", sql);
//        }
//        return file;
//    }
//
//    public static File getFilesByMaterialTestId(int materialTestId) {
//        File file = null;
//        String sql = """
//                      SELECT f.* ,
//                                md.material_doc_name
//                                us.full_name ,
//                                m.material_name,
//                                su.supplier_name
//                                FROM material_testing.dbo.files f
//                                LEFT JOIN material_testing.dbo.material_documents md
//                                ON f.material_doc_id = md.material_doc_id
//        """;
//        try (Connection con = DbConnect.getConnect();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, materialTestId);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    file = new File();
//                    file.setFileId(rs.getInt("file_id"));
//                    file.setMaterialDocumentName(rs.getString("material_doc_name"));
//                    file.setUserFullName(rs.getString("full_name"));
//                    file.setCreationDate(rs.getTimestamp("creation_date") != null
//                            ? rs.getTimestamp("creation_date").toLocalDateTime() : null);
//                    file.setFilePath(rs.getString("file_path"));
//                    file.setComment(rs.getString("comment"));
//                    file.setMaterialTest(new MaterialTestDao().getMaterialTestById(rs.getInt("material_test_id")));
//                    file.setMaterialName(rs.getString("material_name"));
//                    file.setSupplierName(rs.getString("supplier_name"));
//                }
//            }
//
//        } catch (SQLException e) {
//            lastErrorMessage = e.getMessage();
//            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getFilesByMaterialId", e, "sql", sql);
//        }
//        return file;
//    }
//}

package com.etc.raw_materials_app.dao;

import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.File;
import com.etc.raw_materials_app.models.MaterialTest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileDao {
    public static String lastErrorMessage = null;

    public  boolean insertFile(File file) {
        String sql = """
                INSERT INTO material_testing.dbo.files
                    (material_test_id, material_doc_id, user_id, creation_date, file_path, comment)
                VALUES (?, ?, ?, ?, ?, ?);
                """;  // Removed file_id from INSERT as it's IDENTITY
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, file.getMaterialTest().getMaterialTestId());
            ps.setInt(2, file.getMaterialDocumentId());
            ps.setInt(3, file.getUserId());
            ps.setObject(4, file.getCreationDate());
            ps.setString(5, file.getFilePath());
            ps.setString(6, file.getComment());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "insertFile", e, "sql", sql);
            return false;
        }
    }

    public  boolean updateFile(File file) {
        String sql = """
                        UPDATE material_testing.dbo.files SET
                            material_test_id = ?, material_doc_id = ?, user_id = ?, file_path = ?, comment = ?
                        WHERE file_id = ?
                """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, file.getMaterialTest().getMaterialTestId());
            ps.setInt(2, file.getMaterialDocumentId());
            ps.setInt(3, file.getUserId());
            ps.setString(4, file.getFilePath());
            ps.setString(5, file.getComment());
            ps.setInt(6, file.getFileId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "updateFile", e, "sql", sql);
            return false;
        }

    }

    public  boolean deleteFile(int fileId) {
        String sql = "DELETE FROM material_testing.dbo.files WHERE file_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, fileId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "deleteFile", e, "sql", sql);
            return false;
        }
    }

    public  ObservableList<File> getAllFiles() {
        ObservableList<File> list = FXCollections.observableArrayList();
        String sql = """
                      SELECT f.* ,
                             md.material_doc_name,
                             us.full_name ,
                             m.material_name,
                             su.supplier_name
                             FROM material_testing.dbo.files f
                             LEFT JOIN material_testing.dbo.material_documents md
                             ON f.material_doc_id = md.material_doc_id
                             LEFT JOIN material_testing.dbo.users us
                             ON f.user_id = us.user_id
                             LEFT JOIN material_testing.dbo.material_tests mt
                             ON f.material_test_id = mt.material_test_id
                             LEFT JOIN material_testing.dbo.materials m
                             ON mt.material_id = m.material_id
                             LEFT JOIN material_testing.dbo.suppliers su
                             ON mt.supplier_id = su.supplier_id
                             ORDER BY creation_date DESC
                """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                File file = new File();
                file.setFileId(rs.getInt("file_id"));
                file.setMaterialDocumentId(rs.getInt("material_doc_id"));  // Added for completeness
                file.setUserId(rs.getInt("user_id"));  // Added
                file.setCreationDate(rs.getTimestamp("creation_date") != null
                        ? rs.getTimestamp("creation_date").toLocalDateTime() : null);
                file.setFilePath(rs.getString("file_path"));
                file.setComment(rs.getString("comment"));
                file.setMaterialDocumentName(rs.getString("material_doc_name"));
                file.setUserFullName(rs.getString("full_name"));
                file.setMaterialName(rs.getString("material_name"));
                file.setSupplierName(rs.getString("supplier_name"));
                // Fetch MaterialTest if needed
                MaterialTest mt = new MaterialTestDao().getMaterialTestById(rs.getInt("material_test_id"));
                file.setMaterialTest(mt);
                list.add(file);
            }
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getAllFiles", e, "sql", sql);
        }
        return list;
    }

    public  File getFileById(int fileId) {
        File file = null;
        String sql = """
                      SELECT f.* ,
                             md.material_doc_name,
                             us.full_name ,
                             m.material_name,
                             su.supplier_name
                             FROM material_testing.dbo.files f
                             LEFT JOIN material_testing.dbo.material_documents md
                             ON f.material_doc_id = md.material_doc_id
                             LEFT JOIN material_testing.dbo.users us
                             ON f.user_id = us.user_id
                             LEFT JOIN material_testing.dbo.material_tests mt
                             ON f.material_test_id = mt.material_test_id
                             LEFT JOIN material_testing.dbo.materials m
                             ON mt.material_id = m.material_id
                             LEFT JOIN material_testing.dbo.suppliers su
                             ON mt.supplier_id = su.supplier_id
                             WHERE f.file_id = ?
                """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, fileId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    file = new File();
                    file.setFileId(rs.getInt("file_id"));
                    file.setMaterialDocumentId(rs.getInt("material_doc_id"));
                    file.setUserId(rs.getInt("user_id"));
                    file.setCreationDate(rs.getTimestamp("creation_date") != null
                            ? rs.getTimestamp("creation_date").toLocalDateTime() : null);
                    file.setFilePath(rs.getString("file_path"));
                    file.setComment(rs.getString("comment"));
                    file.setMaterialDocumentName(rs.getString("material_doc_name"));
                    file.setUserFullName(rs.getString("full_name"));
                    file.setMaterialName(rs.getString("material_name"));
                    file.setSupplierName(rs.getString("supplier_name"));
                    MaterialTest mt = new MaterialTestDao().getMaterialTestById(rs.getInt("material_test_id"));
                    file.setMaterialTest(mt);
                }
            }
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getFileById", e, "sql", sql);
        }
        return file;
    }

    public  File getFileByFilePath(String filePath) {
        File file = null;
        String sql = """
                      SELECT f.* ,
                             md.material_doc_name,
                             us.full_name ,
                             m.material_name,
                             su.supplier_name
                             FROM material_testing.dbo.files f
                             LEFT JOIN material_testing.dbo.material_documents md
                             ON f.material_doc_id = md.material_doc_id
                             LEFT JOIN material_testing.dbo.users us
                             ON f.user_id = us.user_id
                             LEFT JOIN material_testing.dbo.material_tests mt
                             ON f.material_test_id = mt.material_test_id
                             LEFT JOIN material_testing.dbo.materials m
                             ON mt.material_id = m.material_id
                             LEFT JOIN material_testing.dbo.suppliers su
                             ON mt.supplier_id = su.supplier_id
                             WHERE f.file_path = ?
                """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, filePath);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    file = new File();
                    file.setFileId(rs.getInt("file_id"));
                    file.setMaterialDocumentId(rs.getInt("material_doc_id"));
                    file.setUserId(rs.getInt("user_id"));
                    file.setCreationDate(rs.getTimestamp("creation_date") != null
                            ? rs.getTimestamp("creation_date").toLocalDateTime() : null);
                    file.setFilePath(rs.getString("file_path"));
                    file.setComment(rs.getString("comment"));
                    file.setMaterialDocumentName(rs.getString("material_doc_name"));
                    file.setUserFullName(rs.getString("full_name"));
                    file.setMaterialName(rs.getString("material_name"));
                    file.setSupplierName(rs.getString("supplier_name"));
                    MaterialTest mt = new MaterialTestDao().getMaterialTestById(rs.getInt("material_test_id"));
                    file.setMaterialTest(mt);
                }
            }
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getFileByFilePath", e, "sql", sql);
        }
        return file;
    }

    // Changed to return ObservableList<File> instead of single File, as one MaterialTest can have multiple files
    public  ObservableList<File> getFilesByMaterialTestId(int materialTestId) {
        ObservableList<File> list = FXCollections.observableArrayList();
        String sql = """
                      SELECT f.* ,
                             md.material_doc_name,
                             us.full_name ,
                             m.material_name,
                             su.supplier_name
                             FROM material_testing.dbo.files f
                             LEFT JOIN material_testing.dbo.material_documents md
                             ON f.material_doc_id = md.material_doc_id
                             LEFT JOIN material_testing.dbo.users us
                             ON f.user_id = us.user_id
                             LEFT JOIN material_testing.dbo.material_tests mt
                             ON f.material_test_id = mt.material_test_id
                             LEFT JOIN material_testing.dbo.materials m
                             ON mt.material_id = m.material_id
                             LEFT JOIN material_testing.dbo.suppliers su
                             ON mt.supplier_id = su.supplier_id
                             WHERE f.material_test_id = ?
                             ORDER BY f.creation_date DESC
                """;
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, materialTestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    File file = new File();
                    file.setFileId(rs.getInt("file_id"));
                    file.setMaterialDocumentId(rs.getInt("material_doc_id"));
                    file.setUserId(rs.getInt("user_id"));
                    file.setCreationDate(rs.getTimestamp("creation_date") != null
                            ? rs.getTimestamp("creation_date").toLocalDateTime() : null);
                    file.setFilePath(rs.getString("file_path"));
                    file.setComment(rs.getString("comment"));
                    file.setMaterialDocumentName(rs.getString("material_doc_name"));
                    file.setUserFullName(rs.getString("full_name"));
                    file.setMaterialName(rs.getString("material_name"));
                    file.setSupplierName(rs.getString("supplier_name"));
                    MaterialTest mt = new MaterialTestDao().getMaterialTestById(rs.getInt("material_test_id"));
                    file.setMaterialTest(mt);
                    list.add(file);
                }
            }
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", FileDao.class.getName(), "getFilesByMaterialTestId", e, "sql", sql);
        }
        return list;
    }
}
