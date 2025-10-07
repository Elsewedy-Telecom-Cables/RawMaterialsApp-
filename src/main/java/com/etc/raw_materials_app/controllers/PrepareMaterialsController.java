package com.etc.raw_materials_app.controllers;

import com.etc.raw_materials_app.models.UserContext;
import javafx.application.Platform;
import javafx.fxml.Initializable;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;


public class PrepareMaterialsController implements Initializable {
    @FXML private Button add_material_btn;
    @FXML private Button add_material_description_btn;
    @FXML private Button add_material_document_btn;
    @FXML private Button clear_material_btn;
    @FXML private Button clear_material_description_btn;
    @FXML private Button clear_material_document_btn;
    @FXML private TextField filter_material_description_textF;
    @FXML private TextField filter_material_document_textF;
    @FXML private TextField filter_material_textF;
    @FXML private ImageView logo_ImageView;
    @FXML private ComboBox<?> material_comb;
    @FXML private ComboBox<?> material_comb_tbl_description;
    @FXML private TableColumn<?, ?> material_delete_colm;
    @FXML private TableColumn<?, ?> material_description_delete_colm;
    @FXML private TableColumn<?, ?> material_description_id_colm;
    @FXML private TableColumn<?, ?> material_description_name_colm;
    @FXML private TextField material_description_name_textF;
    @FXML private TableColumn<?, ?> material_document_delete_colm;
    @FXML private TableColumn<?, ?> material_document_id_colm;
    @FXML private TableColumn<?, ?> material_document_name_colm;
    @FXML private TextField material_document_name_textF;
    @FXML private TableView<?> material_document_table_view;
    @FXML private TableView<?> material_description_table_view;
    @FXML private TableColumn<?, ?> material_id_colm;
    @FXML private TableColumn<?, ?> material_name_colm;
    @FXML private TableColumn<?, ?> material_name_in_description_colm;
    @FXML private TableColumn<?, ?> material_name_in_document_colm;
    @FXML private TextField material_name_textF;
    @FXML private TableView<?> material_table_view;
    @FXML private Button update_material_btn;
    @FXML private Button update_material_description_btn;
    @FXML private TextField update_material_description_name_textF;
    @FXML private Button update_material_document_btn;
    @FXML private TextField update_material_document_name_textF;
    @FXML private TextField update_material_name_textF;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void add_material(ActionEvent event) {

    }

    @FXML
    void add_material_description(ActionEvent event) {

    }

    @FXML
    void add_material_document(ActionEvent event) {

    }

    @FXML
    void clear_material(ActionEvent event) {

    }

    @FXML
    void clear_material_description(ActionEvent event) {

    }

    @FXML
    void clear_material_document(ActionEvent event) {

    }

    @FXML
    void filter_file_type(KeyEvent event) {

    }

    @FXML
    void filter_matrial(KeyEvent event) {

    }

    @FXML
    void update_material(ActionEvent event) {

    }

    @FXML
    void update_material_description(ActionEvent event) {

    }

    @FXML
    void update_material_document(ActionEvent event) {

    }
}
