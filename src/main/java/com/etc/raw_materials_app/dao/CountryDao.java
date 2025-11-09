package com.etc.raw_materials_app.dao;

import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDao {
    public static String lastErrorMessage = null;
    // INSERT
    public  boolean insertCountry(Country country) {
        String sql = "INSERT INTO material_testing.dbo.countries (country_name) VALUES (?)";
        try (Connection conn = DbConnect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, country.getCountryName());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            Logging.logExpWithMessage("ERROR", CountryDao.class.getName(), "insertCountry", e, "sql", sql);
            return false;
        }
    }

    // UPDATE
    public  boolean updateCountry(Country country) {
        String sql = "UPDATE material_testing.dbo.countries SET country_name = ? WHERE country_id = ?";
        try (Connection conn = DbConnect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, country.getCountryName());
            stmt.setInt(2, country.getCountryId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", CountryDao.class.getName(), "updateCountry", e, "sql", sql);

            return false;
        }
    }

    // DELETE
    public  boolean deleteCountry(int countryId) {
        String sql = "DELETE FROM material_testing.dbo.countries WHERE country_id = ?";
        try (Connection conn = DbConnect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, countryId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", CountryDao.class.getName(), "deleteCountry", e, "sql", sql);
            return false;
        }
    }

    // GET ALL
    public  ObservableList<Country> getAllCountries() {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        String sql = "SELECT country_id, country_name FROM material_testing.dbo.countries ORDER BY country_name";
        try (Connection conn = DbConnect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Country country = new Country();
                country.setCountryId(rs.getInt("country_id"));
                country.setCountryName(rs.getString("country_name"));
                countries.add(country);
            }

        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", CountryDao.class.getName(), "getAllCountries", e, "sql", sql);

        }
        return countries;
    }

    // GET BY ID
    public  Country getCountryById(int countryId) {
        String sql = "SELECT country_id, country_name FROM material_testing.dbo.countries WHERE country_id = ?";
        try (Connection conn = DbConnect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, countryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Country(
                            rs.getInt("country_id"),
                            rs.getString("country_name")
                    );
                }
            }

        } catch (SQLException e) {
            Logging.logExpWithMessage("ERROR", CountryDao.class.getName(), "getCountryById", e, "sql", sql);

        }
        return null;
    }
}

