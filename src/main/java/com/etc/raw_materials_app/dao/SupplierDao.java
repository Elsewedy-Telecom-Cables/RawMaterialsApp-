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
        String query = "SELECT supplier_id, supplier_name FROM dbtrials.dbo.suppliers ORDER BY supplier_id ASC";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Supplier(rs.getInt("supplier_id"), rs.getString("supplier_name")));
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDao.class.getName(), "getAllSuppliers", e, "sql", query);
        }

        return list;
    }

    // Insert supplier
    public static boolean insertSupplier(Supplier supplier) {
        String query = "INSERT INTO dbtrials.dbo.suppliers (supplier_name) VALUES (?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, supplier.getSupplierName());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", SupplierDao.class.getName(), "insertSupplier", e, "sql", query);
        }

        return false;
    }

    // Update supplier
    public static boolean updateSupplier(Supplier supplier) {
        String query = "UPDATE dbtrials.dbo.suppliers SET supplier_name = ? WHERE supplier_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, supplier.getSupplierName());
            ps.setInt(2, supplier.getSupplierId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDao.class.getName(), "updateSupplier", e, "sql", query);
        }

        return false;
    }

    // Check if supplier can be safely deleted
    public static boolean canDeleteSupplier(int supplierId) {
        String query = "SELECT COUNT(*) AS ref_count FROM dbtrials.dbo.trials WHERE supplier_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ref_count") == 0;
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDao.class.getName(), "canDeleteSupplier", e, "sql", query);
        }

        return false;
    }

    // Delete supplier
    public static boolean deleteSupplier(int supplierId) {
        if (!canDeleteSupplier(supplierId)) {
            System.out.println("Supplier ID " + supplierId + " is referenced in other tables.");
            return false;
        }

        String query = "DELETE FROM dbtrials.dbo.suppliers WHERE supplier_id = ?";

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
        String query = "SELECT supplier_id, supplier_name FROM dbtrials.dbo.suppliers WHERE supplier_id = ?";
        Supplier supplier = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    supplier = new Supplier(rs.getInt("supplier_id"), rs.getString("supplier_name"));
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierDao.class.getName(), "getSupplierById", e, "sql", query);
        }

        return supplier;
    }
}
