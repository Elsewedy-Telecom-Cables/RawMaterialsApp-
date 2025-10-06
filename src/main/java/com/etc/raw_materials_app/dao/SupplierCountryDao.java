
package com.etc.raw_materials_app.dao;

import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.SupplierCountry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SupplierCountryDao {
    public static String lastErrorMessage = null;
  //   Get all supplier countries (with country name via JOIN)
    public static ObservableList<SupplierCountry> getAllSupplierCountries() {
        ObservableList<SupplierCountry> list = FXCollections.observableArrayList();

        String query;
        query = """
            SELECT sc.country_id, c.country_name,
                   sc.supplier_id, s.supplier_name
            FROM material_testing.dbo.supplier_country sc
            LEFT JOIN material_testing.dbo.countries c ON sc.country_id = c.country_id
            LEFT JOIN material_testing.dbo.suppliers s ON sc.supplier_id = s.supplier_id
            ORDER BY s.supplier_id ASC, c.country_name ASC
        """;

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SupplierCountry sc = new SupplierCountry();
                sc.setCountryId(rs.getInt("country_id"));
                sc.setCountryName(rs.getString("country_name"));
                sc.setSupplierId(rs.getInt("supplier_id"));
                sc.setSupplierName(rs.getString("supplier_name"));
                list.add(sc);
            }

        } catch (Exception e) {
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "getAllSupplierCountries", e, "sql", query);
        }

        return list;
    }


    public static boolean insertSupplierCountry(SupplierCountry sc) {
        String query = "INSERT INTO material_testing.dbo.supplier_country (country_id, supplier_id) VALUES (?, ?)";

        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
             ps.setInt(1, sc.getCountryId());
             ps.setInt(2, sc.getSupplierId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", SupplierCountryDao.class.getName(), "insertSupplierCountry", e, "sql", query);
        }
        return false;
    }



    // Get supplier country by ID (includes country name)
    // getSupplierCountriesBySupplierId


    public boolean existsSupplierCountry(int supplierId, int countryId) {
        String query = "SELECT COUNT(*) FROM material_testing.dbo.supplier_country WHERE supplier_id = ? AND country_id = ?";
        try (Connection con = DbConnect.getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, supplierId);
            ps.setInt(2, countryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            Logging.logException("ERROR", getClass().getName(), "existsSupplierCountry", e);
        }
        return false;
    }
}

