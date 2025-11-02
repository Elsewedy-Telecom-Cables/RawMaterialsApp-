package com.etc.raw_materials_app.dao;
import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SupplierDao {
    public static String lastErrorMessage = null;

    // Get all suppliers
    public static ObservableList<Supplier> getAllSuppliers() {
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        String query = "SELECT supplier_id, supplier_name , supplier_code FROM material_testing.dbo.suppliers ORDER BY supplier_id ASC";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Supplier sup = new Supplier() ;
                sup.setSupplierId(rs.getInt("supplier_id"));
                sup.setSupplierName(rs.getString("supplier_name"));
                sup.setSupplierCode(rs.getString("supplier_code"));
                list.add(sup);
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDao.class.getName(), "getAllSuppliers", e, "sql", query);
        }

        return list;
    }

    // Insert supplier
    public static boolean insertSupplier(Supplier supplier) {
        String query = "INSERT INTO material_testing.dbo.suppliers (supplier_name,supplier_code) VALUES (?,?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getSupplierCode());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", SupplierDao.class.getName(), "insertSupplier", e, "sql", query);
        }

        return false;
    }

    // Update supplier
    public static boolean updateSupplier(Supplier supplier) {
        String query = "UPDATE material_testing.dbo.suppliers SET supplier_name = ? , supplier_code = ? WHERE supplier_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getSupplierCode());
            ps.setInt(3, supplier.getSupplierId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDao.class.getName(), "updateSupplier", e, "sql", query);
        }

        return false;
    }

    public static boolean canDeleteSupplier(int supplierId) {
        String[] tablesToCheck = {
                "material_testing.dbo.material_tests",
                "material_testing.dbo.supplier_country"
        };

        try (Connection con = DbConnect.getConnect()) {
            for (String table : tablesToCheck) {
                String query = "SELECT COUNT(*) AS ref_count FROM " + table + " WHERE supplier_id = ?";
                try (PreparedStatement ps = con.prepareStatement(query)) {
                    ps.setInt(1, supplierId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next() && rs.getInt("ref_count") > 0) {
                            return false; // يوجد مرجع في جدول آخر
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDao.class.getName(), "canDeleteSupplier", e, "sql", "multi-table check");
        }

        return true;
    }

    // Delete supplier
    public static boolean deleteSupplier(int supplierId) {
        if (!canDeleteSupplier(supplierId)) {
            System.out.println("Supplier ID " + supplierId + " is referenced in other tables.");
            return false;
        }

        String query = "DELETE FROM material_testing.dbo.suppliers WHERE supplier_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDao.class.getName(), "deleteSupplier", e, "sql", query);
        }
        return false;
    }

    // Get supplier by ID
    public static Supplier getSupplierById(int supplierId) {
        String query = "SELECT supplier_id, supplier_name , supplier_code FROM material_testing.dbo.suppliers WHERE supplier_id = ?";
        Supplier supplier = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                Supplier sup = new Supplier();
                if (rs.next()) {
                    sup.setSupplierId(rs.getInt("supplier_id"));
                    sup.setSupplierName(rs.getString("supplier_name"));
                    sup.setSupplierCode(rs.getString("supplier_code"));
                    supplier = sup;
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDao.class.getName(), "getSupplierById", e, "sql", query);
        }

        return supplier;
    }
}
