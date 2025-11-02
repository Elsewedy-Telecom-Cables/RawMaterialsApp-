//package com.etc.raw_materials_app.controllers;
//
//import com.etc.raw_materials_app.dao.FileDao;
//import com.etc.raw_materials_app.dao.MaterialDocumentDao;
//import com.etc.raw_materials_app.models.File;
//import com.etc.raw_materials_app.models.MaterialDocument;
//import com.etc.raw_materials_app.models.MaterialTest;
//import com.etc.raw_materials_app.models.UserContext;
//import com.etc.raw_materials_app.services.UserService;
//import com.etc.raw_materials_app.services.WindowUtils;
//import javafx.application.Platform;
//import javafx.beans.property.SimpleObjectProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.Initializable;
//import javafx.scene.Cursor;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.control.cell.TextFieldTableCell;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.HBox;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import org.kordamp.ikonli.javafx.FontIcon;
//import com.etc.raw_materials_app.logging.Logging;
//
//public class AddFileController implements Initializable {
//
//    @FXML
//    private Button addFile_btn;
//
//    @FXML
//    private FontIcon clear_selected_material_doc_icon;
//
//    @FXML
//    private TableColumn<File,String> comment_column;
//
//    @FXML
//    private TableColumn<File,String> delete_file_column;
//
//    @FXML
//    private TableColumn<File,String> download_file_column;
//
//    @FXML
//    private TableColumn<File, LocalDateTime> file_creation_date_column;
//
//    @FXML
//    private TableColumn<File,String> file_id_column;
//
//    @FXML
//    private ComboBox<MaterialDocument> material_document_comb;
//
//    @FXML
//    private TableColumn<File,String> material_document_name_column;
//
//    @FXML
//    private TableColumn<File,String> material_name_column;
//
//    @FXML
//    private TableColumn<File,String> material_test_id_column;
//
//    @FXML
//    private TableColumn<File,String> open_file_column;
//
//    @FXML
//    private TableColumn<File,String> supplier_name_column;
//
//    @FXML
//    private TableView<File> table_view;
//
//    @FXML
//    private Label title_lbl;
//
//    @FXML
//    private TableColumn<File,String> upload_file_column;
//
//    private ObservableList<File> fileList = FXCollections.observableArrayList();
//    private ObservableList<MaterialDocument> materialDocuments = FXCollections.observableArrayList();
//    private List<Integer> uploadedMaterialDocumentIds = new ArrayList<>();
//    private static final String SERVER_UPLOAD_PATH = "\\\\ETCSVR\\MaterialTestsUpload\\";
//    private static String selectedDownloadPath = null;
//    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a");
//
//    private int materialTestId;
//    private String supplierName;
//    private String materialName;
//
//    private Stage stage;
//
//    public void setStage(Stage stage) {
//        this.stage = stage;
//        //   System.out.println("setStage called with stage: " + (stage != null ? stage.toString() : "null"));
//    }
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Platform.runLater(()->title_lbl.requestFocus());
//        material_document_comb.setItems(MaterialDocumentDao.getAllMaterialDocuments());
//        clear_selected_material_doc_icon.setCursor(javafx.scene.Cursor.HAND);
//        setupTable();
//    }
//
//    public void initData(int materialTestId, String supplierName, String materialName) {
//        this.materialTestId = materialTestId;
//        this.supplierName = supplierName;
//        this.materialName = materialName;
//        fileList.clear();
//        uploadedMaterialDocumentIds.clear();
//        loadData();
//
//    }
//    private void setupTable() {
//
//        file_id_column.setCellValueFactory(new PropertyValueFactory<>("fileId"));
//        material_document_name_column.setCellValueFactory(new PropertyValueFactory<>("materialDocumentName"));
//        material_name_column.setCellValueFactory(new PropertyValueFactory<>("materialName"));
//        supplier_name_column.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
//        material_test_id_column.setCellValueFactory(new PropertyValueFactory<>("materialTestId"));
//        file_creation_date_column.setCellValueFactory(cellData ->
//                new SimpleObjectProperty<>(cellData.getValue().getCreationDate()));
//        file_creation_date_column.setCellFactory(column -> new TableCell<File, LocalDateTime>() {
//            @Override
//            protected void updateItem(LocalDateTime item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(dateFormatter.format(item));
//                }
//            }
//        });
//
//        upload_file_column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;");
//
//
//        comment_column.setCellValueFactory(new PropertyValueFactory<>("comment"));
//        comment_column.setCellFactory(TextFieldTableCell.forTableColumn());
//        comment_column.setOnEditCommit(event -> {
//            File file = event.getRowValue();
//            if (file.getFileId() != 0) {
//                file.setComment(event.getNewValue());
//                FileDao.updateFile(file);
//                table_view.getItems().set(table_view.getItems().indexOf(file), file);
//                table_view.refresh();
//            }
//        });
//        table_view.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2 && table_view.getSelectionModel().getSelectedItem() != null) {
//                File selectedFile = table_view.getSelectionModel().getSelectedItem();
//                if (selectedFile.getFileId() != 0) {
//                    table_view.edit(table_view.getSelectionModel().getSelectedIndex(), comment_column);
//                }
//            }
//        });
//
//        upload_file_column.setCellFactory(col -> new TableCell<>() {
//            private final Button btn = new Button();
//            private final FontIcon uploadIcon = new FontIcon("fas-upload");
//
//            {
//                uploadIcon.setIconSize(14);
//                uploadIcon.setIconColor(javafx.scene.paint.Color.GREEN);
//                btn.setGraphic(uploadIcon);
//                btn.setStyle("-fx-background-color: transparent;");
//                btn.setCursor(Cursor.HAND);
//                btn.setOnAction(event -> uploadFile(getIndex()));
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                setGraphic(empty ? null : btn);
//            }
//        });
//
//        open_file_column.setCellFactory(col -> new TableCell<>() {
//            private final Button btn = new Button();
//            private final FontIcon openIcon = new FontIcon("fas-folder-open");
//
//            {
//                openIcon.setIconSize(15);
//                openIcon.setIconColor(javafx.scene.paint.Color.web("#ecab29"));
//                btn.setStyle("-fx-background-color: transparent;");
//                btn.setCursor(Cursor.HAND);
//                btn.setOnAction(event -> openFile(getTableView().getItems().get(getIndex()).getFilePath()));
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || getTableView().getItems().get(getIndex()).getFilePath() == null) {
//                    setGraphic(null);
//                } else {
//                    String fullFileName = new java.io.File(getTableView().getItems().get(getIndex()).getFilePath()).getName();
//                    String nameWithoutExtension = fullFileName.contains(".") ?
//                            fullFileName.substring(0, fullFileName.lastIndexOf(".")) : fullFileName;
//                    String originalName = nameWithoutExtension.contains("_") ?
//                            nameWithoutExtension.substring(0, nameWithoutExtension.indexOf("_")) : nameWithoutExtension;
//                    String extension = fullFileName.contains(".") ?
//                            fullFileName.substring(fullFileName.lastIndexOf(".")) : "";
//                    String fileName = originalName + extension;
//
//                    btn.setText(fileName);
//                    btn.setGraphic(openIcon);
//                    setGraphic(btn);
//                    Tooltip tooltip = new Tooltip(fileName);
//                    tooltip.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-color: #f4f4f4; -fx-text-fill: #333;");
//                    Tooltip.install(btn, tooltip);
//                }
//            }
//        });
//
//        download_file_column.setCellFactory(col -> new TableCell<>() {
//            private final Button btn = new Button();
//            private final FontIcon downloadIcon = new FontIcon("fas-download");
//
//            {
//                downloadIcon.setIconSize(14);
//                downloadIcon.setIconColor(javafx.scene.paint.Color.web("#1E90FF"));
//                btn.setGraphic(downloadIcon);
//                btn.setStyle("-fx-background-color: transparent;");
//                btn.setCursor(Cursor.HAND);
//                btn.setOnAction(event -> downloadFile(getTableView().getItems().get(getIndex())));
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                setGraphic(empty || getTableView().getItems().get(getIndex()).getFilePath() == null ? null : btn);
//            }
//        });
//
//        delete_file_column.setCellFactory(col -> new TableCell<>() {
//            private final Button btn = new Button();
//            private final FontIcon deleteIcon = new FontIcon("fas-trash");
//
//            {
//                deleteIcon.setIconSize(14);
//                deleteIcon.setIconColor(javafx.scene.paint.Color.RED);
//                btn.setGraphic(deleteIcon);
//                btn.setStyle("-fx-background-color: transparent;");
//                btn.setCursor(Cursor.HAND);
//                btn.setOnAction(event -> deleteFile(getTableView().getItems().get(getIndex())));
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                setGraphic(empty || getTableView().getItems().get(getIndex()).getFileId() == 0 ? null : btn);
//            }
//        });
//
//        String columnStyle1 = "-fx-alignment: CENTER; -fx-font-size: 12px; -fx-font-weight: bold;";
//        String columnStyle2 = "-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;";
//        String columnStyle3 = "-fx-alignment: CENTER; -fx-font-size: 10px; -fx-font-weight: bold;";
//        file_id_column.setStyle(columnStyle1);
//        comment_column.setStyle(columnStyle2);
//        upload_file_column.setStyle(columnStyle1);
//        open_file_column.setStyle(columnStyle3);
//        download_file_column.setStyle(columnStyle1);
//        delete_file_column.setStyle(columnStyle1);
//        table_view.setFixedCellSize(32);
//    }
//
//    @FXML
//    void clear_selected_material_doc(MouseEvent event) {
//         material_document_comb.selectionModelProperty().get().clearSelection();
//    }
//    private void loadData() {
//        fileList.clear();
//        table_view.setItems(fileList);
//        table_view.refresh();
//        for (File file : fileList) {
//            //      System.out.println("File: file_id=" + file.getFileId() + ", trial_purpose=" + file.getTrialPurpose() + ", file_type_id=" + file.getFileTypeId());
//        }
//    }
//
//    @FXML
//    void addFile(ActionEvent event) {
//        if (materialDocuments.isEmpty()) {
//            WindowUtils.ALERT("Error", "No Material Document  available for this material.", WindowUtils.ALERT_ERROR);
//            return;
//        }
//        for (MaterialDocument mt : materialDocuments) {
//            File newFile = new File();
//
//            newFile.setMaterialTest(new MaterialTest().getMaterialTestId(materialTestId)); // AI Check This point
//            newFile.setMaterialDocumentId(material_document_comb.getValue().getMaterialDocumentId());
//            newFile.setUserId(UserContext.getCurrentUser().getUserId());
//            fileList.add(newFile);
//            //     System.out.println("Added file row: trialId=" + trialId + ", trialPurpose=" + trialPurpose + ", fileTypeId=" + ft.getFileTypeId() + ", fileTypeName=" + ft.getFileTypeName());
//        }
//        table_view.setItems(fileList);
//        table_view.refresh();
//    }
//
//    public void uploadFile(int rowIndex) {
//        if (stage == null) {
//            Logging.logException("ERROR", this.getClass().getName(), "uploadFile", new IllegalStateException("Stage is null in uploadFile"));
//            //     System.out.println("Cannot open FileChooser: stage is null");
//            WindowUtils.ALERT("Error", "Cannot open file chooser: Stage is not initialized.", WindowUtils.ALERT_ERROR);
//            return;
//        }
//
//        if (rowIndex < 0 || rowIndex >= fileList.size()) {
//            WindowUtils.ALERT("Error", "Invalid row selected.", WindowUtils.ALERT_ERROR);
//            return;
//        }
//
//        File selectedRow = fileList.get(rowIndex);
//        if (selectedRow.getMaterialDocumentId() == 0) { // Ai Check fo this point please is it correct or no
//            WindowUtils.ALERT("Error", "No Material Document  assigned to this row.", WindowUtils.ALERT_ERROR);
//            return;
//        }
//
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select File to Upload");
//        java.io.File selectedFile = fileChooser.showOpenDialog(stage);
//        if (selectedFile != null) {
//            try {
//                File file = new File();
//
//                file.setMaterialDocumentId(materialTestId);
//                file.setMaterialDocumentId(selectedRow.getMaterialDocumentId());
//                file.setUserId(UserContext.getCurrentUser().getUserId());
//                file.setCreationDate(LocalDateTime.now());
//
//
//
//                String fileName = selectedFile.getName();
//                int userId = UserContext.getCurrentUser().getUserId();
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
//                String timestamp = LocalDateTime.now().format(formatter);
//                String namePart = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
//                String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
//                String uniqueName = namePart + "-" + userId + "_" + timestamp + extension;
//
//                file.setFilePath(uniqueName);
//                uploadedMaterialDocumentIds.add(file.getMaterialDocumentId());
//                file.setComment("");
//
//                java.io.File dest = new java.io.File(SERVER_UPLOAD_PATH + uniqueName);
//                dest.getParentFile().mkdirs();
//                Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//                if (dest.exists()) {
//                    if (FileDao.insertFile(file)) {
//                        File fetchedFile = FileDao.getFileByFilePath(uniqueName);
//                        if (fetchedFile != null && fetchedFile.getFileId() != 0) {
//                            file.setFileId(fetchedFile.getFileId());
//                        }
//                        fileList.set(rowIndex, file); // Update the row with the uploaded file
//                        //    System.out.println("File uploaded, filePath: " + uniqueName);
//                        table_view.refresh();
//                        WindowUtils.ALERT("Success", "File uploaded successfully", WindowUtils.ALERT_INFORMATION);
//                    } else {
//                        //     System.out.println("Failed to insert file into database");
//                        WindowUtils.ALERT("Error", "Failed to insert file into database", WindowUtils.ALERT_ERROR);
//                    }
//                } else {
//                    //    System.out.println("Failed to copy file to server: " + uniqueName);
//                    WindowUtils.ALERT("Error", "Failed to upload file to server", WindowUtils.ALERT_ERROR);
//                }
//            } catch (IOException e) {
//                Logging.logException("ERROR", this.getClass().getName(), "uploadFile", e);
//                //     System.out.println("IOException in uploadFile: " + e.getMessage());
//                WindowUtils.ALERT("Error", "Failed to upload file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
//            }
//        }
//    }
//
//    private void openFile(String fileName) {
//        try {
//            if (fileName != null && !fileName.isEmpty()) {
//                String fullPath = SERVER_UPLOAD_PATH + fileName;
//                java.io.File networkFile = new java.io.File(fullPath);
//                if (networkFile.exists()) {
//                    String tempDir = System.getProperty("java.io.tmpdir");
//                    java.io.File tempFile = new java.io.File(tempDir + fileName);
//                    Files.copy(networkFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                    if (tempFile.exists()) {
//                        java.awt.Desktop.getDesktop().open(tempFile);
//                        tempFile.deleteOnExit();
//                    } else {
//                        WindowUtils.ALERT("Error", "Failed to copy file to: " + tempFile.getAbsolutePath(), WindowUtils.ALERT_ERROR);
//                    }
//                } else {
//                    WindowUtils.ALERT("Error", "File not found at: " + fullPath, WindowUtils.ALERT_ERROR);
//                }
//            } else {
//                WindowUtils.ALERT("Error", "File name is empty", WindowUtils.ALERT_ERROR);
//            }
//        } catch (IOException e) {
//            Logging.logException("ERROR", this.getClass().getName(), "openFile", e);
//            WindowUtils.ALERT("Error", "Failed to open file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
//        }
//    }
//
//    private void downloadFile(File fileRecord) {
//        try {
//            if (fileRecord != null && fileRecord.getFilePath() != null && !fileRecord.getFilePath().isEmpty()) {
//                String fileName = fileRecord.getFilePath();
//                String fullPath = SERVER_UPLOAD_PATH + fileName;
//                java.io.File networkFile = new java.io.File(fullPath);
//                if (networkFile.exists()) {
//                    java.io.File saveFile;
//                    if (selectedDownloadPath == null) {
//                        FileChooser fileChooser = new FileChooser();
//                        fileChooser.setInitialFileName(fileName.replaceFirst("^\\d+_", ""));
//                        fileChooser.setTitle("Select Download Location");
//                        java.io.File selectedDir = fileChooser.showSaveDialog(stage);
//                        if (selectedDir != null) {
//                            selectedDownloadPath = selectedDir.getParent();
//                            saveFile = selectedDir;
//                        } else {
//                            WindowUtils.ALERT("Error", "Download cancelled by user", WindowUtils.ALERT_ERROR);
//                            return;
//                        }
//                    } else {
//                        saveFile = new java.io.File(selectedDownloadPath + java.io.File.separator + fileName.replaceFirst("^\\d+_", ""));
//                    }
//
//                    Files.copy(networkFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                    if (saveFile.exists()) {
//                        WindowUtils.ALERT("Success", "File downloaded successfully to: " + saveFile.getAbsolutePath(), WindowUtils.ALERT_INFORMATION);
//                    } else {
//                        WindowUtils.ALERT("Error", "Failed to download file", WindowUtils.ALERT_ERROR);
//                    }
//                } else {
//                    WindowUtils.ALERT("Error", "Source file doesn't exist at: " + fullPath, WindowUtils.ALERT_ERROR);
//                }
//            } else {
//                WindowUtils.ALERT("Error", "File name is empty", WindowUtils.ALERT_ERROR);
//            }
//        } catch (IOException e) {
//            Logging.logException("ERROR", this.getClass().getName(), "downloadFile", e);
//            WindowUtils.ALERT("Error", "Failed to download file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
//        }
//    }
//
//    private void deleteFile(File file) {
//        if (file.getFileId() != 0) {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Delete Confirmation");
//            alert.setHeaderText("Are you sure you want to delete this file?");
//            String fileName = new java.io.File(file.getFilePath()).getName().replaceFirst("^\\d+_", "");
//            alert.setContentText("File: " + fileName);
//
//            ButtonType okButton = ButtonType.OK;
//            ButtonType cancelButton = ButtonType.CANCEL;
//            alert.getButtonTypes().setAll(okButton, cancelButton);
//
//            Button okBtn = (Button) alert.getDialogPane().lookupButton(okButton);
//            Button cancelBtn = (Button) alert.getDialogPane().lookupButton(cancelButton);
//            okBtn.setText("OK");
//            cancelBtn.setText("Cancel");
//            Platform.runLater(() -> cancelBtn.requestFocus());
//
//            alert.showAndWait().ifPresent(response -> {
//                if (response == okButton) {
//                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
//                        try {
//                            boolean deleted = FileDao.deleteFile(file.getFileId());
//                            if (deleted) {
//                                fileList.remove(file);
//                                // in case user remove any file before save
//                                uploadedMaterialDocumentIds.remove(Integer.valueOf(file.getMaterialDocumentId()));
//                                loadData();
//                                WindowUtils.ALERT("Success", "File deleted successfully", WindowUtils.ALERT_INFORMATION);
//                            } else {
//                                WindowUtils.ALERT("Error", "Failed to delete file", WindowUtils.ALERT_ERROR);
//                            }
//                        } catch (Exception ex) {
//                            Logging.logException("ERROR", this.getClass().getName(), "deleteFile", ex);
//                            WindowUtils.ALERT("Error", "Failed to delete file", WindowUtils.ALERT_ERROR);
//                        }
//                    } else {
//                        WindowUtils.ALERT("Error", "Password not correct", WindowUtils.ALERT_WARNING);
//                    }
//                }
//            });
//        }
//    }
//}


package com.etc.raw_materials_app.controllers;

import com.etc.raw_materials_app.dao.FileDao;
import com.etc.raw_materials_app.dao.MaterialDocumentDao;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.MaterialDocument;
import com.etc.raw_materials_app.models.MaterialTest;
import com.etc.raw_materials_app.models.UserContext;
import com.etc.raw_materials_app.services.UserService;
import com.etc.raw_materials_app.services.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.Desktop;  // For opening files
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.*;

import com.etc.raw_materials_app.models.File;  // Import the model File last to avoid overshadowing

public class AddFileController implements Initializable {

    @FXML
    private Button addFile_btn;

    @FXML
    private FontIcon clear_selected_material_doc_icon;

    @FXML
    private TableColumn<File, String> comment_column;

    @FXML
    private TableColumn<File, String> delete_file_column;

    @FXML
    private TableColumn<File, String> download_file_column;

    @FXML
    private TableColumn<File, LocalDateTime> file_creation_date_column;

    @FXML
    private TableColumn<File, String> file_id_column;

    @FXML
    private ComboBox<MaterialDocument> material_document_comb;

    @FXML
    private TableColumn<File, String> material_document_name_column;

    @FXML
    private TableColumn<File, String> material_name_column;

    @FXML
    private TableColumn<File, String> material_test_id_column;

    @FXML
    private TableColumn<File, String> open_file_column;

    @FXML
    private TableColumn<File, String> supplier_name_column;

    @FXML
    private TableView<File> table_view;

    @FXML
    private Label title_lbl;

    @FXML
    private TableColumn<File, String> upload_file_column;

    private ObservableList<File> fileList = FXCollections.observableArrayList();
    private ObservableList<MaterialDocument> materialDocuments = FXCollections.observableArrayList();
    private List<Integer> uploadedMaterialDocumentIds = new ArrayList<>();
    //private static final String SERVER_UPLOAD_PATH = "\\\\ETCSVR\\MaterialTestsUpload\\";
    private static final String SERVER_UPLOAD_PATH = "G:\\ETC_Projects\\Quality\\Raw_Materials\\MaterialTestsUpload";

    private static String selectedDownloadPath = null;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a");

    private int materialTestId;
    private String supplierName;
    private String materialName;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> title_lbl.requestFocus());
        materialDocuments = MaterialDocumentDao.getAllMaterialDocuments();  // Load all material documents
        material_document_comb.setItems(materialDocuments);
        clear_selected_material_doc_icon.setCursor(Cursor.HAND);
        setupTable();
    }

    public void initData(int materialTestId, String supplierName, String materialName) {
        this.materialTestId = materialTestId;
        this.supplierName = supplierName;
        this.materialName = materialName;
        fileList.clear();
        uploadedMaterialDocumentIds.clear();
        loadData();  // Load existing files for this materialTestId
    }

    private void setupTable() {
        // Set cell value factories
        file_id_column.setCellValueFactory(new PropertyValueFactory<>("fileId"));
        material_document_name_column.setCellValueFactory(new PropertyValueFactory<>("materialDocumentName"));
        material_name_column.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        supplier_name_column.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        // For material_test_id_column, since it's inside MaterialTest, use a Callback
        material_test_id_column.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getMaterialTest().getMaterialTestId())));

        file_creation_date_column.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        file_creation_date_column.setCellFactory(column -> new TableCell<File, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });
        table_view.setEditable(true);
        comment_column.setEditable(true);
        comment_column.setCellValueFactory(new PropertyValueFactory<>("comment"));
        comment_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comment_column.setOnEditCommit(event -> {
            File file = event.getRowValue();
            if (file.getFileId() != 0) {
                file.setComment(event.getNewValue());
                FileDao.updateFile(file);
                table_view.getItems().set(table_view.getItems().indexOf(file), file);
                table_view.refresh();
            }
        });
        table_view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && table_view.getSelectionModel().getSelectedItem() != null) {
                File selectedFile = table_view.getSelectionModel().getSelectedItem();
                if (selectedFile.getFileId() != 0) {
                    table_view.edit(table_view.getSelectionModel().getSelectedIndex(), comment_column);
                }
            }
        });
        // Upload column: Show upload button only for rows without filePath (new rows)
        upload_file_column.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            private final FontIcon uploadIcon = new FontIcon("fas-upload");

            {
                uploadIcon.setIconSize(14);
                uploadIcon.setIconColor(GREEN);
                btn.setGraphic(uploadIcon);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(event -> uploadFile(getIndex()));
            }
// Without Show Upload Icon
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || getTableView().getItems().get(getIndex()).getFilePath() != null) {
//                    setGraphic(null);
//                } else {
//                    setGraphic(btn);
//                }
//            }
//        });
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : btn);
        }
    });

        // Open column: Show open button with file name (without timestamp and userId)
        open_file_column.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            private final FontIcon openIcon = new FontIcon("fas-folder-open");

            {
                openIcon.setIconSize(15);
                openIcon.setIconColor(web("#ecab29"));
                btn.setStyle("-fx-background-color: transparent;");
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(event -> openFile(getTableView().getItems().get(getIndex()).getFilePath()));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()).getFilePath() == null) {
                    setGraphic(null);
                } else {
                    // Use java.io.File for file operations to avoid name conflict
                    String fullFileName = new java.io.File(getTableView().getItems().get(getIndex()).getFilePath()).getName();
                    // Remove userId_timestamp part
                    String nameWithoutExtension = fullFileName.contains(".") ?
                            fullFileName.substring(0, fullFileName.lastIndexOf(".")) : fullFileName;
                    String originalName = nameWithoutExtension.contains("-") ?
                            nameWithoutExtension.substring(0, nameWithoutExtension.indexOf("-")) : nameWithoutExtension;
                    String extension = fullFileName.contains(".") ?
                            fullFileName.substring(fullFileName.lastIndexOf(".")) : "";
                    String fileName = originalName + extension;

                    btn.setText(fileName);
                    btn.setGraphic(openIcon);
                    setGraphic(btn);
                    Tooltip tooltip = new Tooltip(fileName);
                    tooltip.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-color: #f4f4f4; -fx-text-fill: #333;");
                    Tooltip.install(btn, tooltip);
                }
            }
        });

        // Download column: Show only if filePath exists
        download_file_column.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            private final FontIcon downloadIcon = new FontIcon("fas-download");

            {
                downloadIcon.setIconSize(14);
                downloadIcon.setIconColor(web("#1E90FF"));
                btn.setGraphic(downloadIcon);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(event -> downloadFile(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || getTableView().getItems().get(getIndex()).getFilePath() == null ? null : btn);
            }
        });

        // Delete column: Show only for saved files (fileId != 0)
        delete_file_column.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            private final FontIcon deleteIcon = new FontIcon("fas-trash");

            {
                deleteIcon.setIconSize(14);
                deleteIcon.setIconColor(RED);
                btn.setGraphic(deleteIcon);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(event -> deleteFile(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || getTableView().getItems().get(getIndex()).getFileId() == 0 ? null : btn);
            }
        });

        // Styles for columns
        String columnStyle1 = "-fx-alignment: CENTER; -fx-font-size: 12px; -fx-font-weight: bold;";
        String columnStyle2 = "-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;";
        String columnStyle3 = "-fx-alignment: CENTER; -fx-font-size: 10px; -fx-font-weight: bold;";
        file_id_column.setStyle(columnStyle1);
        material_test_id_column.setStyle(columnStyle1);
        supplier_name_column.setStyle(columnStyle1);
        supplier_name_column.setStyle(columnStyle1);
        material_name_column.setStyle(columnStyle2);
        material_document_name_column.setStyle(columnStyle2);
        file_creation_date_column.setStyle(columnStyle3);
        comment_column.setStyle(columnStyle2);
        upload_file_column.setStyle(columnStyle1);
        open_file_column.setStyle(columnStyle3);
        download_file_column.setStyle(columnStyle1);
        delete_file_column.setStyle(columnStyle1);
        table_view.setFixedCellSize(32);
        table_view.setItems(fileList);
    }

    @FXML
    void clear_selected_material_doc(MouseEvent event) {
        material_document_comb.getSelectionModel().clearSelection();
    }

    private void loadData() {
        // Load existing files for the current materialTestId
        fileList.addAll(FileDao.getFilesByMaterialTestId(materialTestId));
        // Set materialName and supplierName for all loaded files (in case not set)
        for (File file : fileList) {
            file.setMaterialName(materialName);
            file.setSupplierName(supplierName);
            uploadedMaterialDocumentIds.add(file.getMaterialDocumentId());
        }
        table_view.refresh();
    }

    @FXML
    void addFile(ActionEvent event) {
        MaterialDocument selectedDoc = material_document_comb.getValue();
        if (selectedDoc == null) {
            WindowUtils.ALERT("Error", "Please select a Material Document.", WindowUtils.ALERT_ERROR);
            return;
        }

        // Add a new row with fileId=0, based on selected MaterialDocument
        File newFile = new File();
        MaterialTest mt = new MaterialTest();  // Assuming MaterialTest has a default constructor
        mt.setMaterialTestId(materialTestId);  // Set the ID
        newFile.setMaterialTest(mt);
        newFile.setMaterialDocumentId(selectedDoc.getMaterialDocumentId());
        newFile.setMaterialDocumentName(selectedDoc.getMaterialDocumentName());  // Assuming this getter exists; if not, adjust to getMaterialDocName()
        newFile.setUserId(UserContext.getCurrentUser().getUserId());
        newFile.setCreationDate(null);  // Will be set on upload
        newFile.setFilePath(null);  // Will be set on upload
        newFile.setComment("");
        newFile.setMaterialName(materialName);  // From previous page
        newFile.setSupplierName(supplierName);  // From previous page
        fileList.add(newFile);
        table_view.refresh();
    }

    public void uploadFile(int rowIndex) {
        if (stage == null) {
            Logging.logException("ERROR", this.getClass().getName(), "uploadFile", new IllegalStateException("Stage is null in uploadFile"));
            WindowUtils.ALERT("Error", "Cannot open file chooser: Stage is not initialized.", WindowUtils.ALERT_ERROR);
            return;
        }

        if (rowIndex < 0 || rowIndex >= fileList.size()) {
            WindowUtils.ALERT("Error", "Invalid row selected.", WindowUtils.ALERT_ERROR);
            return;
        }

        File selectedRow = fileList.get(rowIndex);  // This is model File
        if (selectedRow.getMaterialDocumentId() == 0) {
            WindowUtils.ALERT("Error", "No Material Document assigned to this row.", WindowUtils.ALERT_ERROR);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Upload");
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);  // Use fully qualified java.io.File
        if (selectedFile != null) {
            try {
                // Prepare the model File for insert
                File file = new File();  // Model File
                MaterialTest mt = new MaterialTest();
                mt.setMaterialTestId(materialTestId);
                file.setMaterialTest(mt);
                file.setMaterialDocumentId(selectedRow.getMaterialDocumentId());
                file.setUserId(UserContext.getCurrentUser().getUserId());
                file.setCreationDate(LocalDateTime.now());
                file.setComment(selectedRow.getComment());
                file.setMaterialName(materialName);
                file.setSupplierName(supplierName);
                file.setMaterialDocumentName(selectedRow.getMaterialDocumentName());

                // Generate unique file name
                String fileName = selectedFile.getName();
                int userId = UserContext.getCurrentUser().getUserId();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
                String timestamp = LocalDateTime.now().format(formatter);
                String namePart = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
                String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
                String uniqueName = namePart + "-" + userId + "_" + timestamp + extension;

                file.setFilePath(uniqueName);

                // Copy to server using java.io.File
                java.io.File dest = new java.io.File(SERVER_UPLOAD_PATH + uniqueName);
                dest.getParentFile().mkdirs();
                Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                if (dest.exists()) {
                    if (FileDao.insertFile(file)) {
                        // Fetch the inserted file to get the auto-generated fileId
                        File fetchedFile = FileDao.getFileByFilePath(uniqueName);
                        if (fetchedFile != null && fetchedFile.getFileId() != 0) {
                            file.setFileId(fetchedFile.getFileId());
                            file.setUserFullName(fetchedFile.getUserFullName());  // If needed from DB
                        }
                        // Update the list and add to uploaded IDs
                        uploadedMaterialDocumentIds.add(file.getMaterialDocumentId());
                        fileList.set(rowIndex, file);
                        table_view.refresh();
                        WindowUtils.ALERT("Success", "File uploaded successfully", WindowUtils.ALERT_INFORMATION);
                    } else {
                        WindowUtils.ALERT("Error", "Failed to insert file into database", WindowUtils.ALERT_ERROR);
                    }
                } else {
                    WindowUtils.ALERT("Error", "Failed to upload file to server", WindowUtils.ALERT_ERROR);
                }
            } catch (IOException e) {
                Logging.logException("ERROR", this.getClass().getName(), "uploadFile", e);
                WindowUtils.ALERT("Error", "Failed to upload file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
            }
        }
    }

    private void openFile(String fileName) {
        try {
            if (fileName != null && !fileName.isEmpty()) {
                String fullPath = SERVER_UPLOAD_PATH + fileName;
                java.io.File networkFile = new java.io.File(fullPath);  // Use fully qualified java.io.File
                if (networkFile.exists()) {
                    String tempDir = System.getProperty("java.io.tmpdir");
                    java.io.File tempFile = new java.io.File(tempDir + fileName);
                    Files.copy(networkFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    if (tempFile.exists()) {
                        Desktop.getDesktop().open(tempFile);
                        tempFile.deleteOnExit();
                    } else {
                        WindowUtils.ALERT("Error", "Failed to copy file to: " + tempFile.getAbsolutePath(), WindowUtils.ALERT_ERROR);
                    }
                } else {
                    WindowUtils.ALERT("Error", "File not found at: " + fullPath, WindowUtils.ALERT_ERROR);
                }
            } else {
                WindowUtils.ALERT("Error", "File name is empty", WindowUtils.ALERT_ERROR);
            }
        } catch (IOException e) {
            Logging.logException("ERROR", this.getClass().getName(), "openFile", e);
            WindowUtils.ALERT("Error", "Failed to open file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }

    private void downloadFile(File fileRecord) {  // Model File
        try {
            if (fileRecord != null && fileRecord.getFilePath() != null && !fileRecord.getFilePath().isEmpty()) {
                String fileName = fileRecord.getFilePath();
                String fullPath = SERVER_UPLOAD_PATH + fileName;
                java.io.File networkFile = new java.io.File(fullPath);  // Use fully qualified java.io.File
                if (networkFile.exists()) {
                    java.io.File saveFile;
                    if (selectedDownloadPath == null) {
                        FileChooser fileChooser = new FileChooser();
                        // Remove userId_timestamp from suggested name
                        String cleanName = fileName.replaceAll("-\\d+_\\d{8}-\\d{6}", "");
                        fileChooser.setInitialFileName(cleanName);
                        fileChooser.setTitle("Select Download Location");
                        java.io.File selectedDir = fileChooser.showSaveDialog(stage);  // Use fully qualified java.io.File
                        if (selectedDir != null) {
                            selectedDownloadPath = selectedDir.getParent();
                            saveFile = selectedDir;
                        } else {
                            WindowUtils.ALERT("Error", "Download cancelled by user", WindowUtils.ALERT_ERROR);
                            return;
                        }
                    } else {
                        String cleanName = fileName.replaceAll("-\\d+_\\d{8}-\\d{6}", "");
                        saveFile = new java.io.File(selectedDownloadPath + java.io.File.separator + cleanName);
                    }

                    Files.copy(networkFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    if (saveFile.exists()) {
                        WindowUtils.ALERT("Success", "File downloaded successfully to: " + saveFile.getAbsolutePath(), WindowUtils.ALERT_INFORMATION);
                    } else {
                        WindowUtils.ALERT("Error", "Failed to download file", WindowUtils.ALERT_ERROR);
                    }
                } else {
                    WindowUtils.ALERT("Error", "Source file doesn't exist at: " + fullPath, WindowUtils.ALERT_ERROR);
                }
            } else {
                WindowUtils.ALERT("Error", "File name is empty", WindowUtils.ALERT_ERROR);
            }
        } catch (IOException e) {
            Logging.logException("ERROR", this.getClass().getName(), "downloadFile", e);
            WindowUtils.ALERT("Error", "Failed to download file: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }

    private void deleteFile(File file) {  // Model File
        if (file.getFileId() != 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete this file?");
            // Clean file name for display using java.io.File
            String fileName = new java.io.File(file.getFilePath()).getName().replaceAll("-\\d+_\\d{8}-\\d{6}", "");
            alert.setContentText("File: " + fileName);

            ButtonType okButton = ButtonType.OK;
            ButtonType cancelButton = ButtonType.CANCEL;
            alert.getButtonTypes().setAll(okButton, cancelButton);

            Button okBtn = (Button) alert.getDialogPane().lookupButton(okButton);
            Button cancelBtn = (Button) alert.getDialogPane().lookupButton(cancelButton);
            okBtn.setText("OK");
            cancelBtn.setText("Cancel");
            Platform.runLater(() -> cancelBtn.requestFocus());

            alert.showAndWait().ifPresent(response -> {
                if (response == okButton) {
                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                        try {
                            boolean deleted = FileDao.deleteFile(file.getFileId());
                            if (deleted) {
                                fileList.remove(file);
                                uploadedMaterialDocumentIds.remove(Integer.valueOf(file.getMaterialDocumentId()));
                                table_view.refresh();  // Refresh instead of loadData to avoid reloading all
                                WindowUtils.ALERT("Success", "File deleted successfully", WindowUtils.ALERT_INFORMATION);
                            } else {
                                WindowUtils.ALERT("Error", "Failed to delete file", WindowUtils.ALERT_ERROR);
                            }
                        } catch (Exception ex) {
                            Logging.logException("ERROR", this.getClass().getName(), "deleteFile", ex);
                            WindowUtils.ALERT("Error", "Failed to delete file", WindowUtils.ALERT_ERROR);
                        }
                    } else {
                        WindowUtils.ALERT("Error", "Password not correct", WindowUtils.ALERT_WARNING);
                    }
                }
            });
        }
    }
}