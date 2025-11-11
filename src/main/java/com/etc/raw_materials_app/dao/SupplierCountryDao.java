
package com.etc.raw_materials_app.dao;
import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.SupplierCountry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;

public class SupplierCountryDao {
    public static String lastErrorMessage = null;
    // Get supplier-country by supplier ID and country ID
    public  SupplierCountry getSupplierCountryById(int supplierId, int countryId) {
        String query = "SELECT sc.supplier_id, s.supplier_name, sc.country_id, c.country_name " +
                "FROM material_testing.dbo.supplier_country sc " +
                "JOIN material_testing.dbo.suppliers s ON sc.supplier_id = s.supplier_id " +
                "JOIN material_testing.dbo.countries c ON sc.country_id = c.country_id " +
                "WHERE sc.supplier_id = ? AND sc.country_id = ?";
        SupplierCountry supplierCountry = null;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierId);
            ps.setInt(2, countryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SupplierCountry sc = new SupplierCountry();
                    sc.setSupplierId(rs.getInt("supplier_id"));
                    sc.setSupplierName(rs.getString("supplier_name"));
                    sc.setCountryId(rs.getInt("country_id"));
                    sc.setCountryName(rs.getString("country_name"));
                    supplierCountry = sc;
                }
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "getSupplierCountryById", e, "sql", query);
        }

        return supplierCountry;
    }

    // Get all supplier-country mappings
    public  ObservableList<SupplierCountry> getAllSupplierCountries() {
        ObservableList<SupplierCountry> list = FXCollections.observableArrayList();
        String query = """ 
                SELECT sc.supplier_id, s.supplier_name, sc.country_id, c.country_name
                FROM material_testing.dbo.supplier_country sc
                JOIN material_testing.dbo.suppliers s ON sc.supplier_id = s.supplier_id
                JOIN material_testing.dbo.countries c ON sc.country_id = c.country_id
                ORDER BY s.supplier_name ASC, c.country_name ASC
                """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SupplierCountry sc = new SupplierCountry();
                sc.setSupplierId(rs.getInt("supplier_id"));
                sc.setSupplierName(rs.getString("supplier_name"));
                sc.setCountryId(rs.getInt("country_id"));
                sc.setCountryName(rs.getString("country_name"));
                list.add(sc);
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "getAllSupplierCountries", e, "sql", query);
        }

        return list;
    }

    // Insert supplier-country mapping
    public  boolean insertSupplierCountry(SupplierCountry supplierCountry) {
        String query = "INSERT INTO material_testing.dbo.supplier_country (supplier_id, country_id) VALUES (?,?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierCountry.getSupplierId());
            ps.setInt(2, supplierCountry.getCountryId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "insertSupplierCountry", e, "sql", query);
        }

        return false;
    }

    // Delete supplier-country mapping
    public  boolean deleteSupplierCountry(int supplierId, int countryId) {
        String query = "DELETE FROM material_testing.dbo.supplier_country WHERE supplier_id = ? AND country_id = ?";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierId);
            ps.setInt(2, countryId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "deleteSupplierCountry", e, "sql", query);
        }

        return false;
    }


}

