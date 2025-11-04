package com.etc.raw_materials_app.dao;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.Sample;
import com.etc.raw_materials_app.db.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SampleDao {
        public static String lastErrorMessage;
        // Get all samples
        public static ObservableList<Sample> getAllSamples() {
            ObservableList<Sample> list = FXCollections.observableArrayList();
            String query = "SELECT sample_id, sample_name FROM material_testing.dbo.samples ORDER BY sample_id ASC";

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("sample_id");
                    String name = rs.getString("sample_name");
                    list.add(new Sample(id, name));
                }

            } catch (SQLException e) {
                lastErrorMessage = e.getMessage();
                Logging.logExpWithMessage("ERROR", SampleDao.class.getName(), "getAllSamples", e, "sql", query);
            }
            return list;
        }

        // Get a single sample by ID
        public static Sample getSampleById(int sampleId) {
            String query = "SELECT sample_id, sample_name FROM material_testing.dbo.samples WHERE sample_id = ?";
            Sample sample = null;

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, sampleId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("sample_id");
                        String name = rs.getString("sample_name");
                        sample = new Sample(id, name);
                    }
                }

            } catch (SQLException e) {
                lastErrorMessage = e.getMessage();
                Logging.logExpWithMessage("ERROR", SampleDao.class.getName(), "getSampleById", e, "sql", query);
            }
            return sample;
        }

        // Insert a new sample
        public static boolean insertSample(Sample sample) {
            String query = "INSERT INTO material_testing.dbo.samples (sample_name) VALUES (?)";

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, sample.getSampleName());
                return ps.executeUpdate() > 0;


            } catch (SQLException e) {
                lastErrorMessage = e.getMessage();
                Logging.logExpWithMessage("ERROR", SampleDao.class.getName(), "insertSample", e, "sql", query);
            }
          return false;
        }

        // Update an existing sample
        public static boolean updateSample(Sample sample) {
            String query = "UPDATE material_testing.dbo.samples SET sample_name = ? WHERE sample_id = ?";
            boolean success = false;

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, sample.getSampleName());
                ps.setInt(2, sample.getSampleId());

                int affectedRows = ps.executeUpdate();
                success = affectedRows > 0;

            } catch (SQLException e) {
                lastErrorMessage = e.getMessage();
                Logging.logExpWithMessage("ERROR", SampleDao.class.getName(), "updateSample", e, "sql", query);
            }
            return success;
        }

        // Delete a sample
        public static boolean deleteSample(int sampleId) {
            String query = "DELETE FROM material_testing.dbo.samples WHERE sample_id = ?";
            boolean success = false;

            try (Connection con = DbConnect.getConnect();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, sampleId);
                int affectedRows = ps.executeUpdate();
                success = affectedRows > 0;

            } catch (SQLException e) {
                lastErrorMessage = e.getMessage();
                Logging.logExpWithMessage("ERROR", SampleDao.class.getName(), "deleteSample", e, "sql", query);
            }
            return success;
        }
    }


