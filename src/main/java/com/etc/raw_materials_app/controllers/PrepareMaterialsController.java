
package com.etc.raw_materials_app.controllers;

import com.etc.raw_materials_app.dao.MaterialDao;
import com.etc.raw_materials_app.dao.MaterialDescriptionDao;
import com.etc.raw_materials_app.dao.MaterialDocumentDao;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.Material;
import com.etc.raw_materials_app.models.MaterialDescription;
import com.etc.raw_materials_app.models.MaterialDocument;
import com.etc.raw_materials_app.models.UserContext;
import com.etc.raw_materials_app.services.UserService;
import com.etc.raw_materials_app.services.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

public class PrepareMaterialsController implements Initializable {

    @FXML private TextField filter_material_description_textF;
    @FXML private TextField filter_material_document_textF;
    @FXML private TextField filter_material_textF;
    @FXML private TextField update_material_name_textF;
    @FXML private ImageView logo_ImageView;
    @FXML private ComboBox<Material> material_comb_des;
    @FXML private TableColumn<Material,String> material_delete_colm;
    @FXML private TableColumn<MaterialDescription,String> material_description_delete_colm;
    @FXML private TableColumn<MaterialDescription,String> material_description_id_colm;
    @FXML private TableColumn<MaterialDescription,String> material_description_name_colm;
    @FXML private TextField material_description_name_textF;
    @FXML private TableColumn<MaterialDocument,String> material_document_delete_colm;
    @FXML private TableColumn<MaterialDocument,String> material_document_id_colm;
    @FXML private TableColumn<MaterialDocument,String> material_document_name_colm;
    @FXML private TextField material_document_name_textF;
    @FXML private TableView<MaterialDocument> material_document_table_view;
    @FXML private TableView<MaterialDescription> material_description_table_view;
    @FXML private TableColumn<Material,String> material_id_colm;
    @FXML private TableColumn<Material,String> material_name_colm;
    @FXML private TableColumn<MaterialDescription,String> material_name_in_description_tbl;
    @FXML private TextField material_name_textF;
    @FXML private TableView<Material> material_table_view;
    @FXML private TextField update_material_description_name_textF;
    @FXML private TextField update_material_document_name_textF;
    @FXML private TextField item_code_textF ;
    @FXML private TextField update_item_code_textF ;
    @FXML private TableColumn<Material,String> item_code_colm;



    ObservableList<Material> materialList;
    ObservableList<MaterialDescription> materialDescriptionList;
    ObservableList<MaterialDocument> materialDocumentList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMaterialsData();
        loadMaterialDescriptionData();
        loadMaterialDocumentData();

        // Initialize ObservableLists
        materialList = MaterialDao.getAllMaterials();
        materialDescriptionList = MaterialDescriptionDao.getAllMaterialDescriptions();
        materialDocumentList = MaterialDocumentDao.getAllMaterialDocuments();

        // Set items to TableViews
        material_table_view.setItems(materialList);
        material_description_table_view.setItems(materialDescriptionList);
        material_document_table_view.setItems(materialDocumentList);

        // Call Tables Listener
        setupMaterialTableListener();
        setupMaterialDescriptionTableListener();
        setupMaterialDocumentTableListener();

        // Set ComboBox items
        material_comb_des.setItems(MaterialDao.getAllMaterials());

    }

    // Load materials Data
    private void loadMaterialsData() {
        material_name_colm.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        item_code_colm.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        material_id_colm.setCellValueFactory(new PropertyValueFactory<>("materialId"));
        material_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        item_code_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        material_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Material, String>, TableCell<Material, String>> cellFactory = param -> {
            final TableCell<Material, String> cell = new TableCell<Material, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete material"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this material?");
                            alert.setContentText("Delete material confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(cancelButton::requestFocus);
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            Material material = material_table_view.getSelectionModel().getSelectedItem();
                                            MaterialDao.deleteMaterial(material.getMaterialId());
                                            materialList = MaterialDao.getAllMaterials();
                                            material_table_view.setItems(materialList);
                                            WindowUtils.ALERT("Success", "material deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "delete material", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERROR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        material_delete_colm.setCellFactory(cellFactory);
        material_table_view.setItems(materialList);
    }

    // Load materials Description Data
    private void loadMaterialDescriptionData() {
        material_description_name_colm.setCellValueFactory(new PropertyValueFactory<>("materialDescriptionName"));
        material_description_id_colm.setCellValueFactory(new PropertyValueFactory<>("materialDescriptionId"));
        material_name_in_description_tbl.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMaterial().getMaterialName())
        );
        String style = "-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;";
        material_description_name_colm.setStyle(style);
        material_description_id_colm.setStyle(style);
        material_name_in_description_tbl.setStyle(style);
        Callback<TableColumn<MaterialDescription, String>, TableCell<MaterialDescription, String>> cellFactory = param -> {
            final TableCell<MaterialDescription, String> cell = new TableCell<MaterialDescription, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete Material Description"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this Material Description?");
                            alert.setContentText("Delete Material Description confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(cancelButton::requestFocus);
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            MaterialDescription materialDescription = material_description_table_view.getSelectionModel().getSelectedItem();
                                            MaterialDescriptionDao.deleteMaterialDescription(materialDescription.getMaterialDescriptionId());
                                            materialDescriptionList = MaterialDescriptionDao.getAllMaterialDescriptions();
                                            material_description_table_view.setItems(materialDescriptionList);
                                            WindowUtils.ALERT("Success", "Material Description deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "delete Material Description", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERROR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        material_description_delete_colm.setCellFactory(cellFactory);
        material_description_table_view.setItems(materialDescriptionList);
    }

    // Load material Document Data
    private void loadMaterialDocumentData() {
        material_document_name_colm.setCellValueFactory(new PropertyValueFactory<>("materialDocumentName"));
        material_document_id_colm.setCellValueFactory(new PropertyValueFactory<>("materialDocumentId"));

        String style = "-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;";
        material_document_name_colm.setStyle(style);
        material_document_id_colm.setStyle(style);
        Callback<TableColumn<MaterialDocument, String>, TableCell<MaterialDocument, String>> cellFactory = param -> {
            final TableCell<MaterialDocument, String> cell = new TableCell<MaterialDocument, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete Material Document"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this Material Document?");
                            alert.setContentText("Delete Material Document confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(cancelButton::requestFocus);
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            MaterialDocument materialDocument = material_document_table_view.getSelectionModel().getSelectedItem();
                                            MaterialDocumentDao.deleteMaterialDocument(materialDocument.getMaterialDocumentId());
                                            materialDocumentList = MaterialDocumentDao.getAllMaterialDocuments();
                                            material_document_table_view.setItems(materialDocumentList);
                                            WindowUtils.ALERT("Success", "Material Document deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "delete Material Document", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERROR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        material_document_delete_colm.setCellFactory(cellFactory);
        material_document_table_view.setItems(materialDocumentList);
    }

    // Filter materials
    @FXML
    void filter_material(KeyEvent event) {
        FilteredList<Material> filteredData = new FilteredList<>(materialList, p -> true);
        filter_material_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(material -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (material.getMaterialName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (material.getItemCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = material.getMaterialId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Material> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(material_table_view.comparatorProperty());
        material_table_view.setItems(sortedData);
    }

    // Filter material descriptions
    @FXML
    void filter_material_description(KeyEvent event) {
        FilteredList<MaterialDescription> filteredData = new FilteredList<>(materialDescriptionList, p -> true);
        filter_material_description_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(materialDescription -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (materialDescription.getMaterialDescriptionName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (materialDescription.getMaterial().getMaterialName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = materialDescription.getMaterialDescriptionId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<MaterialDescription> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(material_description_table_view.comparatorProperty());
        material_description_table_view.setItems(sortedData);
    }

    // Filter material documents
    @FXML
    void filter_material_document(KeyEvent event) {
        FilteredList<MaterialDocument> filteredData = new FilteredList<>(materialDocumentList, p -> true);
        filter_material_document_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(materialDocument -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (materialDocument.getMaterialDocumentName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                String id = materialDocument.getMaterialDocumentId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<MaterialDocument> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(material_document_table_view.comparatorProperty());
        material_document_table_view.setItems(sortedData);
    }

    // Add material
    @FXML
    void add_material(ActionEvent event) {
        String materialName = material_name_textF.getText().trim();
        if (materialName.isEmpty()) {
            WindowUtils.ALERT("ERROR", "material_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }
        String itemCode = item_code_textF.getText().trim();
        if (itemCode.isEmpty()) {
            WindowUtils.ALERT("ERROR", "item_code_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Material material = new Material();
        material.setMaterialName(materialName);
        material.setItemCode(itemCode);

        boolean success = MaterialDao.insertMaterial(material);

        if (success) {
            WindowUtils.ALERT("Success", "material added successfully", WindowUtils.ALERT_INFORMATION);
            material_name_textF.clear();
            item_code_textF.clear();
            update_material_name_textF.clear();
            update_item_code_textF.clear();
            filter_material_textF.clear();
            materialList = MaterialDao.getAllMaterials();
            material_table_view.setItems(materialList);
            material_comb_des.setItems(MaterialDao.getAllMaterials());
        } else {
            String err = MaterialDao.lastErrorMessage;
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "material name already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("database_error", "material_name_add_failed", WindowUtils.ALERT_ERROR);
            }
        }
    }

    // Add material description
    @FXML
    void add_material_description(ActionEvent event) {
        String materialDescriptionName = material_description_name_textF.getText().trim();
        if (materialDescriptionName.isEmpty()) {
            WindowUtils.ALERT("ERROR", "material_description_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }
        Material selectedMaterial = material_comb_des.getSelectionModel().getSelectedItem();
        if (selectedMaterial == null) {
            WindowUtils.ALERT("ERROR", "Material is Empty", WindowUtils.ALERT_ERROR);
            return;
        }
        MaterialDescription materialDescription = new MaterialDescription();
        materialDescription.setMaterialDescriptionName(materialDescriptionName);
        materialDescription.setMaterial(selectedMaterial);
        boolean success = MaterialDescriptionDao.insertMaterialDescription(materialDescription);
        if (success) {
            WindowUtils.ALERT("Success", "material description added successfully", WindowUtils.ALERT_INFORMATION);
            material_description_name_textF.clear();
            update_material_description_name_textF.clear();
            filter_material_description_textF.clear();
            material_comb_des.getSelectionModel().clearSelection();
            materialDescriptionList = MaterialDescriptionDao.getAllMaterialDescriptions();
            material_description_table_view.setItems(materialDescriptionList);
        } else {
            String err = MaterialDescriptionDao.lastErrorMessage;
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "material description name already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("database_error", "add_material_description_failed", WindowUtils.ALERT_ERROR);
            }
        }
    }

    // Add material document
    @FXML
    void add_material_document(ActionEvent event) {
        String materialDocumentName = material_document_name_textF.getText().trim();
        if (materialDocumentName.isEmpty()) {
            WindowUtils.ALERT("ERROR", "material_document_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        MaterialDocument materialDocument = new MaterialDocument();
        materialDocument.setMaterialDocumentName(materialDocumentName);
        boolean success = MaterialDocumentDao.insertMaterialDocument(materialDocument);
        if (success) {
            WindowUtils.ALERT("Success", "material document added successfully", WindowUtils.ALERT_INFORMATION);
            material_document_name_textF.clear();
            update_material_document_name_textF.clear();
            filter_material_document_textF.clear();
            materialDocumentList = MaterialDocumentDao.getAllMaterialDocuments();
            material_document_table_view.setItems(materialDocumentList);
        } else {
            String err = MaterialDocumentDao.lastErrorMessage;
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "material document name already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("database_error", "add_material_document_failed", WindowUtils.ALERT_ERROR);
            }
        }
    }

    // Clear material
    @FXML
    void clear_material(ActionEvent event) {
        filter_material_textF.clear();
        update_material_name_textF.clear();
        item_code_textF.clear();
        update_item_code_textF.clear();
        material_name_textF.clear();
    }

    // Clear material description
    @FXML
    void clear_material_description(ActionEvent event) {
        filter_material_description_textF.clear();
        update_material_description_name_textF.clear();
        material_description_name_textF.clear();
        material_description_table_view.getSelectionModel().clearSelection();
        material_comb_des.getSelectionModel().clearSelection();
        material_comb_des.setValue(null);
    }

    // Clear material document
    @FXML
    void clear_material_document(ActionEvent event) {
        filter_material_document_textF.clear();
        update_material_document_name_textF.clear();
        material_document_name_textF.clear();
        material_document_table_view.getSelectionModel().clearSelection();
    }

    // Update material
    @FXML
    void update_material(ActionEvent event) {
        try {
            Material selectedMaterial = material_table_view.getSelectionModel().getSelectedItem();
            if (selectedMaterial == null) {
                WindowUtils.ALERT("ERROR", "No material selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String materialName = update_material_name_textF.getText().trim();
            if (materialName.isEmpty()) {
                WindowUtils.ALERT("ERROR", "material_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }
            String itemCode = update_item_code_textF.getText().trim();
            if (itemCode.isEmpty()) {
                WindowUtils.ALERT("ERROR", "item_code_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedMaterial.setMaterialName(materialName);
            selectedMaterial.setItemCode(itemCode);
            boolean success = MaterialDao.updateMaterial(selectedMaterial);
            if (success) {
                WindowUtils.ALERT("Success", "material updated successfully", WindowUtils.ALERT_INFORMATION);
                update_material_name_textF.clear();
                update_item_code_textF.clear();
                material_name_textF.clear();
                item_code_textF.clear();
                filter_material_textF.clear();
                materialList = MaterialDao.getAllMaterials();
                material_table_view.setItems(materialList);
                material_comb_des.setItems(MaterialDao.getAllMaterials());
            } else {
                String err = MaterialDao.lastErrorMessage;
                if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                    WindowUtils.ALERT("Duplicate", "material name already exists", WindowUtils.ALERT_ERROR);
                } else {
                    WindowUtils.ALERT("ERROR", "material_updated_failed", WindowUtils.ALERT_ERROR);
                }
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "update material", ex);
        }
    }

    // Update material description
    @FXML
    void update_material_description(ActionEvent event) {
        try {
            MaterialDescription selectedMaterialDescription = material_description_table_view.getSelectionModel().getSelectedItem();
            if (selectedMaterialDescription == null) {
                WindowUtils.ALERT("ERROR", "No material description selected", WindowUtils.ALERT_ERROR);
                return;
            }
            String materialDescriptionName = update_material_description_name_textF.getText().trim();
            if (materialDescriptionName.isEmpty()) {
                WindowUtils.ALERT("ERROR", "material_description_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            Material selectedMaterial = material_comb_des.getSelectionModel().getSelectedItem();
            if (selectedMaterial == null) {
                WindowUtils.ALERT("ERROR", "Material is Empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedMaterialDescription.setMaterialDescriptionName(materialDescriptionName);
            selectedMaterialDescription.setMaterial(selectedMaterial);
            boolean success = MaterialDescriptionDao.updateMaterialDescription(selectedMaterialDescription);
            if (success) {
                WindowUtils.ALERT("Success", "material description updated successfully", WindowUtils.ALERT_INFORMATION);
                update_material_description_name_textF.clear();
                material_description_name_textF.clear();
                filter_material_description_textF.clear();
                material_comb_des.getSelectionModel().clearSelection();
                material_comb_des.setValue(null);
                materialDescriptionList = MaterialDescriptionDao.getAllMaterialDescriptions();
                material_description_table_view.setItems(materialDescriptionList);
            } else {
                String err = MaterialDescriptionDao.lastErrorMessage;
                if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                    WindowUtils.ALERT("Duplicate", "material description name already exists", WindowUtils.ALERT_ERROR);
                } else {
                    WindowUtils.ALERT("ERROR", "material_description_updated_failed", WindowUtils.ALERT_ERROR);
                }
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "update material description", ex);
        }
    }

    // Update material document
    @FXML
    void update_material_document(ActionEvent event) {
        try {
            MaterialDocument selectedMaterialDocument = material_document_table_view.getSelectionModel().getSelectedItem();
            if (selectedMaterialDocument == null) {
                WindowUtils.ALERT("ERROR", "No material document selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String materialDocumentName = update_material_document_name_textF.getText().trim();
            if (materialDocumentName.isEmpty()) {
                WindowUtils.ALERT("ERROR", "material_document_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }


            selectedMaterialDocument.setMaterialDocumentName(materialDocumentName);

            boolean success = MaterialDocumentDao.updateMaterialDocument(selectedMaterialDocument);
            if (success) {
                WindowUtils.ALERT("Success", "material document updated successfully", WindowUtils.ALERT_INFORMATION);
                update_material_document_name_textF.clear();
                material_document_name_textF.clear();
                filter_material_document_textF.clear();
                materialDocumentList = MaterialDocumentDao.getAllMaterialDocuments();
                material_document_table_view.setItems(materialDocumentList);
            } else {
                String err = MaterialDocumentDao.lastErrorMessage;
                    WindowUtils.ALERT("ERROR", "material_document_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "update material document", ex);
        }
    }

    // Setup material Table Listener
    private void setupMaterialTableListener() {
        material_table_view.setOnMouseClicked(event -> {
            Material selectedMaterial = material_table_view.getSelectionModel().getSelectedItem();
            if (selectedMaterial != null) {
                update_material_name_textF.setText(selectedMaterial.getMaterialName());
                update_item_code_textF.setText(selectedMaterial.getItemCode());
            }
        });
    }

    // Setup material Description Table Listener
    private void setupMaterialDescriptionTableListener() {
        material_description_table_view.setOnMouseClicked(event -> {
            MaterialDescription selectedMaterialDescription = material_description_table_view.getSelectionModel().getSelectedItem();
            if (selectedMaterialDescription != null) {
                update_material_description_name_textF.setText(selectedMaterialDescription.getMaterialDescriptionName());
                material_comb_des.getSelectionModel().select(selectedMaterialDescription.getMaterial());
            }
        });
    }

    // Setup material Document Table Listener
    private void setupMaterialDocumentTableListener() {
        material_document_table_view.setOnMouseClicked(event -> {
            MaterialDocument selectedMaterialDocument = material_document_table_view.getSelectionModel().getSelectedItem();
            if (selectedMaterialDocument != null) {
                update_material_document_name_textF.setText(selectedMaterialDocument.getMaterialDocumentName());
            }
        });
    }


}
