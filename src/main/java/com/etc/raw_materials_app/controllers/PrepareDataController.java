
package com.etc.raw_materials_app.controllers;
import com.etc.raw_materials_app.dao.SectionDao;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.Section;
import com.etc.raw_materials_app.models.UserContext;
import com.etc.raw_materials_app.services.UserService;
import com.etc.raw_materials_app.services.WindowUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.ResourceBundle;

public class PrepareDataController implements Initializable {
    @FXML private Button add_country_btn;
    @FXML private Button add_supplier_btn;
    @FXML private Button add_supplier_country_btn;
    @FXML private Button add_test_btn;
    @FXML private Button clear_country_btn;
    @FXML private Button clear_supplier_btn;
    @FXML private Button clear_supplier_cou_btn;
    @FXML private Button clear_test_btn;
    @FXML private ComboBox<?> country_comb;
    @FXML private TableColumn<?, ?> country_delete_colm;
    @FXML private TableColumn<?, ?> country_id_colm;
    @FXML private TableColumn<?, ?> country_name_colm;
    @FXML private TextField country_name_textF;
    @FXML private TableView<?> country_table_view;
    @FXML private TextField filter_country_textF;
    @FXML private TextField filter_supplier_cou_textF;
    @FXML private TextField filter_supplier_textF;
    @FXML private TextField filter_test_textF;
    @FXML private ComboBox<?> supplier_comb;
    @FXML private TableColumn<?, ?> supplier_cou_delete_colm;
    @FXML private TableColumn<?, ?> supplier_cou_name_colm;
    @FXML private TableView<?> supplier_country_table_view;
    @FXML private TableColumn<?, ?> supplier_delete_colm;
    @FXML private TableColumn<?, ?> supplier_id_colm;
    @FXML private TableColumn<?, ?> supplier_name_colm;
    @FXML private TableColumn<?, ?> supplier_name_in_sup_coun_colm;
    @FXML private TextField supplier_name_textF;
    @FXML private TableView<?> supplier_table_view;
    @FXML private TableColumn<?, ?> test_delete_colm;
    @FXML private TableColumn<?, ?> test_id_colm;
    @FXML private TableColumn<?, ?> test_name_colm;
    @FXML private TextField test_name_textF;
    @FXML private TableView<?> test_names_table_view;
    @FXML private Button update_country_btn;
    @FXML private TextField update_country_name_textF;
    @FXML private Button update_supplier_btn;
    @FXML private Button update_supplier_cou_btn;
    @FXML private TextField update_supplier_name_textF;
    @FXML private Button update_test_btn;
    @FXML private TextField update_test_name_textF;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void add_country(ActionEvent event) {

    }

    @FXML
    void add_supplier(ActionEvent event) {

    }

    @FXML
    void add_supplier_country(ActionEvent event) {

    }

    @FXML
    void add_test(ActionEvent event) {

    }

    @FXML
    void clear_country(ActionEvent event) {

    }

    @FXML
    void clear_supplier(ActionEvent event) {

    }

    @FXML
    void clear_supplier_country(ActionEvent event) {

    }

    @FXML
    void clear_test(ActionEvent event) {

    }

    @FXML
    void filter_country(KeyEvent event) {

    }

    @FXML
    void filter_test_names(KeyEvent event) {

    }

    @FXML
    void filter_supplier(KeyEvent event) {

    }

    @FXML
    void filter_supplier_country(KeyEvent event) {

    }

    @FXML
    void update_country(ActionEvent event) {

    }

    @FXML
    void update_supplier(ActionEvent event) {

    }

    @FXML
    void update_supplier_country(ActionEvent event) {

    }

    @FXML
    void update_test(ActionEvent event) {

    }

}

