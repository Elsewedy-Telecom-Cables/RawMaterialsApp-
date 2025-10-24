package com.etc.raw_materials_app.controllers;

import com.etc.raw_materials_app.dao.MaterialDocumentDao;
import com.etc.raw_materials_app.models.File;
import com.etc.raw_materials_app.models.MaterialDocument;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.kordamp.ikonli.javafx.FontIcon;

public class AddFileController implements Initializable {

    @FXML
    private Button addFile_btn;

    @FXML
    private FontIcon clear_selected_material_doc_icon;

    @FXML
    private TableColumn<File,String> comment_column;

    @FXML
    private TableColumn<File,String> delete_file_column;

    @FXML
    private TableColumn<File,String> download_file_column;

    @FXML
    private TableColumn<File, LocalDateTime> file_creation_date_column;

    @FXML
    private TableColumn<File,String> file_id_column;

    @FXML
    private ComboBox<MaterialDocument> material_document_comb;

    @FXML
    private TableColumn<File,String> material_document_name_column;

    @FXML
    private TableColumn<File,String> material_name_column;

    @FXML
    private TableColumn<File,String> material_test_id_column;

    @FXML
    private TableColumn<File,String> open_file_column;

    @FXML
    private TableColumn<File,String> supplier_name_column;

    @FXML
    private TableView<File> table_view;

    @FXML
    private Label title_lbl;

    @FXML
    private TableColumn<File,String> upload_file_column;




    private int materialTestId;
    private String supplierName;
    private String materialName;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        //   System.out.println("setStage called with stage: " + (stage != null ? stage.toString() : "null"));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(()->title_lbl.requestFocus());
        material_document_comb.setItems(MaterialDocumentDao.getAllMaterialDocuments());
        clear_selected_material_doc_icon.setCursor(javafx.scene.Cursor.HAND);
    }

    public void initData(int materialTestId, String supplierName, String materialName) {

    }
    @FXML
    void addFile(ActionEvent event) {

    }

    @FXML
    void clear_selected_material_doc(MouseEvent event) {
         material_document_comb.selectionModelProperty().get().clearSelection();
    }
}
