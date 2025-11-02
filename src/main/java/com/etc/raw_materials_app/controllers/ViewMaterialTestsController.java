
package com.etc.raw_materials_app.controllers;

import com.etc.raw_materials_app.dao.*;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.*;
import com.etc.raw_materials_app.services.UserService;
import com.etc.raw_materials_app.services.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import org.fxmisc.richtext.InlineCssTextArea;

import static com.etc.raw_materials_app.services.WindowUtils.*;

public class ViewMaterialTestsController implements Initializable {

    @FXML private TableView<MaterialTest> material_tests_table_view;

    @FXML private TableColumn<MaterialTest, String> no_column;
    @FXML private TableColumn<MaterialTest, String> id_column;
    @FXML private TableColumn<MaterialTest, String> section_column;
    @FXML private TableColumn<MaterialTest, String> supplier_column;
    @FXML private TableColumn<MaterialTest, String> supplier_country_column;
    @FXML private TableColumn<MaterialTest, String> material_column;
    @FXML private TableColumn<MaterialTest, String> material_description_column;
    @FXML private TableColumn<MaterialTest, String> po_no_column;
    @FXML private TableColumn<MaterialTest, String> oracle_sample_column;
    @FXML private TableColumn<MaterialTest, String> item_code_column;
    @FXML private TableColumn<MaterialTest, String> total_quantity_column;
    @FXML private TableColumn<MaterialTest, String> accepted_quantity_column;
    @FXML private TableColumn<MaterialTest, String> rejected_quantity_column;
    @FXML private TableColumn<MaterialTest, String> receipt_column;
    @FXML private TableColumn<MaterialTest, String> spqr_column;
    @FXML private TableColumn<MaterialTest, String> notes_column;
    @FXML private TableColumn<MaterialTest, String> comment_column;
    @FXML private TableColumn<MaterialTest, String> files_column;
    @FXML private TableColumn<MaterialTest, String> results_column;
    @FXML private TableColumn<MaterialTest, LocalDateTime> creation_date_column;
    @FXML private TableColumn<MaterialTest, String> edit_column;

    @FXML private ComboBox<Section> section_comb;
    @FXML private ComboBox<Supplier> supplier_comb;
    @FXML private ComboBox<Country> supplier_country_comb;
    @FXML private ComboBox<Material> material_comb;

    @FXML private TextField filter_material_test_id_textF;
    @FXML private DatePicker from_creation_date;
    @FXML private DatePicker to_creation_date;
    @FXML private TextField total_material_tests_count_textF;
    @FXML private TextField fo_material_tests_count_textF;
    @FXML private TextField copper_material_tests_count_textF;
    @FXML private TextField accessories_material_tests_count_textF;
    @FXML private TextField filter_item_code_textF;
    @FXML private TableColumn<MaterialTest, String> supplier_code_column;



    @FXML private Label welcome_lbl;
    @FXML private Label date_lbl;
    @FXML private ImageView logo_image_view;

    private MaterialTest materialTestObj = null;

    private ObservableList<MaterialTest> materialTestsList;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a");
    private static ViewMaterialTestsController instance;

    public static ViewMaterialTestsController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        Platform.runLater(() -> welcome_lbl.requestFocus());

        // Welcome + Date + Logo
        java.util.Date date = new java.util.Date();
        date_lbl.setText(new SimpleDateFormat("dd-MM-yyyy  hh:mm a").format(date));
        welcome_lbl.setText("Welcome : " + UserContext.getCurrentUser().getFullName());
        logo_image_view.setImage(new Image(Objects.requireNonNull(MainController.class.getResourceAsStream("/images/company_logo.png"))));

        // Load ComboBoxes
        initializeComboBoxes();

        // Load Data
        loadData();
        updateMaterialTestCount();

        // Add filtering listeners
        addFilterListeners();

        // Set Editable
        total_material_tests_count_textF.setEditable(false);
        fo_material_tests_count_textF.setEditable(false);
        copper_material_tests_count_textF.setEditable(false);
        accessories_material_tests_count_textF.setEditable(false);

    }

    private void initializeComboBoxes() {
        section_comb.setItems(SectionDao.getAllSections());
        supplier_comb.setItems(SupplierDao.getAllSuppliers());
        supplier_country_comb.setItems(CountryDao.getAllCountries());
        material_comb.setItems(MaterialDao.getAllMaterials());

        supplier_comb.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateSupplierCountries(newVal);
        });
    }

    private void addFilterListeners() {
        filter_material_test_id_textF.textProperty().addListener((obs, oldV, newV) -> filterMaterialTests());
        filter_item_code_textF.textProperty().addListener((obs, oldV, newV) -> filterMaterialTests());
        section_comb.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> filterMaterialTests());
        supplier_comb.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> filterMaterialTests());
        supplier_country_comb.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> filterMaterialTests());
        material_comb.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> filterMaterialTests());
    }

    private void filterMaterialTests() {
        Integer materialTestId = null;

        try {
            if (!filter_material_test_id_textF.getText().trim().isEmpty()) {
                materialTestId = Integer.parseInt(filter_material_test_id_textF.getText().trim());
            }
        } catch (NumberFormatException e) {
            ALERT("Error", "Please enter a valid Material Test ID (numeric value)", ALERT_ERROR);
            filter_material_test_id_textF.clear();
            return;
        }

        String itemCode;
        if (filter_item_code_textF.getText() != null && !filter_item_code_textF.getText().trim().isEmpty()) {
            itemCode = filter_item_code_textF.getText().trim();
        } else {
            itemCode = null;
        }

        Integer sectionId = section_comb.getSelectionModel().getSelectedItem() != null
                ? section_comb.getSelectionModel().getSelectedItem().getSectionId() : null;
        Integer supplierId = supplier_comb.getSelectionModel().getSelectedItem() != null
                ? supplier_comb.getSelectionModel().getSelectedItem().getSupplierId() : null;
        Integer countryId = supplier_country_comb.getSelectionModel().getSelectedItem() != null
                ? supplier_country_comb.getSelectionModel().getSelectedItem().getCountryId() : null;
        Integer materialId = material_comb.getSelectionModel().getSelectedItem() != null
                ? material_comb.getSelectionModel().getSelectedItem().getMaterialId() : null;


        LocalDate fromDate = from_creation_date.getValue();
        LocalDate toDate = to_creation_date.getValue();

        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            LocalDate temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }

        //  final variables for lambda
        final Integer finalTestId = materialTestId;
        final String finalItemCode = itemCode;
        final Integer finalSectionId = sectionId;
        final Integer finalSupplierId = supplierId;
        final Integer finalCountryId = countryId;
        final Integer finalMaterialId = materialId;
        final LocalDate start = fromDate;
        final LocalDate end = toDate;

        List<MaterialTest> allTests = MaterialTestDao.getAllMaterialTest();

        List<MaterialTest> filtered = allTests.stream()
                .filter(t -> finalTestId == null || t.getMaterialTestId() == finalTestId)
                .filter(t -> finalItemCode == null || t.getItemCode().equals(finalItemCode))
                .filter(t -> finalSectionId == null || t.getSectionId() == finalSectionId)
                .filter(t -> finalSupplierId == null || t.getSupplierId() == finalSupplierId)
                .filter(t -> finalCountryId == null || t.getCountryId() == finalCountryId)
                .filter(t -> finalMaterialId == null || t.getMaterialId() == finalMaterialId)
                .filter(t -> {

                    if (start == null && end == null) return true; // if user don't select date range
                    if (t.getCreationDate() == null) return false;

                    LocalDate creationDate = t.getCreationDate().toLocalDate();

                    // لو "من" محدد وتاريخ الإنشاء قبله ⇒ استبعد
                    if (start != null && creationDate.isBefore(start)) return false;

                    // لو "إلى" محدد وتاريخ الإنشاء بعده ⇒ استبعد
                    if (end != null && creationDate.isAfter(end)) return false;

                    return true; //  accepted all other status
                })
                .collect(Collectors.toList());

        materialTestsList.setAll(filtered);
        updateMaterialTestCount();
    }



    private void updateSupplierCountries(Supplier supplier) {
        ObservableList<Country> filteredCountries = FXCollections.observableArrayList();
        if (supplier != null) {
            ObservableList<SupplierCountry> list = SupplierCountryDao.getAllSupplierCountries();
            for (SupplierCountry sc : list) {
                if (sc.getSupplierId() == supplier.getSupplierId()) {
                    Country c = new Country();
                    c.setCountryId(sc.getCountryId());
                    c.setCountryName(sc.getCountryName());
                    filteredCountries.add(c);
                }
            }
        }
        supplier_country_comb.setItems(filteredCountries);
        supplier_country_comb.getSelectionModel().clearSelection();
        if (!filteredCountries.isEmpty()) {
            supplier_country_comb.getSelectionModel().selectFirst();
        }
    }


    void loadData() {
        Platform.runLater(() -> {
            materialTestsList = MaterialTestDao.getAllMaterialTest();
            material_tests_table_view.setItems(materialTestsList);

            // Row numbering
            no_column.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : String.valueOf(getIndex() + 1));
                    setStyle("-fx-alignment:CENTER;-fx-font-weight:bold;");
                }
            });

            id_column.setCellValueFactory(new PropertyValueFactory<>("materialTestId"));
            section_column.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
            supplier_column.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            supplier_code_column.setCellValueFactory(new PropertyValueFactory<>("supplierCode"));
            supplier_country_column.setCellValueFactory(new PropertyValueFactory<>("countryName"));
            material_column.setCellValueFactory(new PropertyValueFactory<>("materialName"));
            material_description_column.setCellValueFactory(new PropertyValueFactory<>("materialDesName"));
            po_no_column.setCellValueFactory(new PropertyValueFactory<>("poNo"));
            oracle_sample_column.setCellValueFactory(new PropertyValueFactory<>("oracleSample"));
            item_code_column.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
            total_quantity_column.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
            accepted_quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantityAccepted"));
            rejected_quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantityRejected"));
            receipt_column.setCellValueFactory(new PropertyValueFactory<>("receipt"));
            spqr_column.setCellValueFactory(new PropertyValueFactory<>("spqr"));
            notes_column.setCellValueFactory(new PropertyValueFactory<>("notes"));
            comment_column.setCellValueFactory(new PropertyValueFactory<>("comment"));

            creation_date_column.setCellValueFactory(cd -> new SimpleObjectProperty<>(cd.getValue().getCreationDate()));
            creation_date_column.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : dateFormatter.format(item));
                }
            });

            // Files column
            files_column.setCellFactory(param -> new TableCell<>() {
                private final FontIcon folderIcon = new FontIcon("fas-folder");
                private final FontIcon plusIcon = new FontIcon("fas-plus");
                private final HBox container = new HBox(folderIcon, plusIcon);

                {
                    folderIcon.setIconSize(20);
                    folderIcon.setFill(Color.web("#ecab29"));
                    plusIcon.setIconSize(10);
                    plusIcon.setFill(Color.web("#3b3b3b"));
                    container.setAlignment(Pos.CENTER);
                    container.setSpacing(3);
                    container.setCursor(Cursor.HAND);

                    container.setOnMouseClicked((MouseEvent event) -> {
                        MaterialTest selected = getTableView().getItems().get(getIndex());
                        if (selected != null) {
                            WindowUtils.OPEN_WINDOW_WITH_CONTROLLER_AND_STAGE_FILE("/screens/AddFile.fxml", controller -> {
                                controller.initData(selected.getMaterialTestId(), selected.getSupplierName(), selected.getMaterialName());
                            });
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : container);
                }
            });

            // Results column
            results_column.setCellFactory(param -> new TableCell<>() {
                private final FontIcon flaskIcon = new FontIcon("fas-flask");
                private final FontIcon plusIcon = new FontIcon("fas-plus");
                private final HBox container = new HBox(flaskIcon, plusIcon);

                {
                    flaskIcon.setIconSize(15);
                    flaskIcon.setFill(Color.web("#2c7be5"));
                    plusIcon.setIconSize(10);
                    plusIcon.setFill(Color.web("#3b3b3b"));
                    container.setAlignment(Pos.CENTER);
                    container.setSpacing(3);
                    container.setCursor(Cursor.HAND);

                    container.setOnMouseClicked((MouseEvent event) -> {
                        MaterialTest selected = getTableView().getItems().get(getIndex());
                        if (selected != null) {
                            WindowUtils.OPEN_WINDOW_WITH_CONTROLLER_AND_STAGE_RESULT("/screens/TestResults.fxml", controller -> {
                                controller.initData(
                                        selected.getMaterialTestId(),
                                        selected.getSupplierName(),
                                        selected.getMaterialName(),
                                        selected.getMaterialDesName(),
                                        selected.getPoNo(),
                                        selected.getOracleSample(),
                                        selected.getItemCode(),
                                        selected.getComment()
                                );
                            });
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : container);
                }
            });

            // Configure edit column with edit and delete icons
            edit_column.setCellFactory(param -> new TableCell<MaterialTest, String>() {
                private final FontIcon editIcon = new FontIcon("fa-pencil-square");
                private final FontIcon deleteIcon = new FontIcon("fas-trash");
                private final HBox manageBtn = new HBox(editIcon, deleteIcon);

                {
                    editIcon.setCursor(Cursor.HAND);
                    editIcon.setIconSize(16);
                    editIcon.setFill(Color.GREEN);
                    deleteIcon.setCursor(Cursor.HAND);
                    deleteIcon.setIconSize(13);
                    deleteIcon.setFill(Color.RED);
                    manageBtn.setSpacing(2);
                    manageBtn.setAlignment(Pos.CENTER);
                    HBox.setMargin(editIcon, new javafx.geometry.Insets(1.7, 5, 1.7, 5));
                    HBox.setMargin(deleteIcon, new javafx.geometry.Insets(1.7, 5, 1.7, 5));

                    Tooltip.install(editIcon, new Tooltip("Update Material Test"));
                    Tooltip.install(deleteIcon, new Tooltip("Delete Material Test"));

                    editIcon.setOnMouseClicked(event -> {
                        MaterialTest mt = getTableView().getItems().get(getIndex());
                        if (mt != null) {
                            materialTestObj = mt;   // ليس له دور هنا لكن ربما ساحتاجه فيما بعد
                            WindowUtils.OPEN_EDIT_MATERIAL_TEST_PAGE(mt.getMaterialTestId(), () -> {
                                materialTestsList.set(getIndex(), MaterialTestDao.getMaterialTestById(mt.getMaterialTestId()));
                                material_tests_table_view.refresh();
                                updateMaterialTestCount();
                            });
                        }
                    });

                    deleteIcon.setOnMouseClicked(event -> {
                        MaterialTest mt = getTableView().getItems().get(getIndex());
                        if (mt != null) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Delete Confirmation");
                            alert.setHeaderText("Are you sure you want to delete this material test?");
                            alert.setContentText("material test: " + mt.getMaterialName() );

                            ButtonType okButton = ButtonType.OK;
                            ButtonType cancelButton = ButtonType.CANCEL;
                            alert.getButtonTypes().setAll(okButton, cancelButton);

                            Button okBtn = (Button) alert.getDialogPane().lookupButton(okButton);
                            Button cancelBtn = (Button) alert.getDialogPane().lookupButton(cancelButton);
                            okBtn.setText("OK");
                            cancelBtn.setText("Cancel");
                            Platform.runLater(cancelBtn::requestFocus);

                            alert.showAndWait().ifPresent(response -> {
                                if (response == okButton) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            boolean deleted = MaterialTestDao.deleteMaterialTest(mt.getMaterialTestId());
                                            if (deleted) {
                                                loadData(); // Refresh table
                                                WindowUtils.ALERT("Success", "Material Test deleted successfully", WindowUtils.ALERT_INFORMATION);
                                            } else {
                                                WindowUtils.ALERT("Error", "Failed to delete material test", WindowUtils.ALERT_ERROR);
                                            }
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", ViewMaterialTestsController.class.getName(), "delete material test", ex);
                                            WindowUtils.ALERT("Error", "Failed to delete material test", WindowUtils.ALERT_ERROR);
                                        }
                                    } else {
                                        WindowUtils.ALERT("Error", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(manageBtn);
                    }
                    setText(null);
                }
            });

            // Apply consistent styling to columns
            String columnStyle = "-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;";
            String columnStyle2 = "-fx-alignment: CENTER; -fx-font-size: 10px; -fx-font-weight: bold;";
            id_column.setStyle(columnStyle);
            section_column.setStyle(columnStyle);
            material_column.setStyle(columnStyle);
            files_column.setStyle(columnStyle);
            creation_date_column.setStyle(columnStyle2);
            oracle_sample_column.setStyle(columnStyle);
            po_no_column.setStyle(columnStyle);
            spqr_column.setStyle(columnStyle);
            accepted_quantity_column.setStyle(columnStyle);
            rejected_quantity_column.setStyle(columnStyle);
            receipt_column.setStyle(columnStyle);
            total_quantity_column.setStyle(columnStyle);
            material_description_column.setStyle(columnStyle);
            comment_column.setStyle(columnStyle);
            item_code_column.setStyle(columnStyle);
            item_code_column.setStyle(columnStyle);
            no_column.setStyle(columnStyle);
            supplier_column.setStyle(columnStyle);
            supplier_code_column.setStyle(columnStyle);
            supplier_country_column.setStyle(columnStyle);
            notes_column.setStyle(columnStyle);

            // Set fixed cell size and row factory
            material_tests_table_view.setFixedCellSize(32);
            material_tests_table_view.setRowFactory(tv -> {
                TableRow<MaterialTest> row = new TableRow<>() {
                    @Override
                    protected void updateItem(MaterialTest mt, boolean empty) {
                        super.updateItem(mt, empty);
                    }
                };

                row.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    if (isSelected && !row.isEmpty()) {
                        row.getStyleClass().add("selected-row");
                    } else {
                        row.getStyleClass().remove("selected-row");
                    }
                });

                return row;
            });
            material_tests_table_view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            material_tests_table_view.refresh();

            // Add double-click listener to show row details
            material_tests_table_view.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && material_tests_table_view.getSelectionModel().getSelectedItem() != null) {
                    MaterialTest selectedRow = material_tests_table_view.getSelectionModel().getSelectedItem();
                    StringBuilder details = new StringBuilder();
                    details.append("Material Test ID: ").append(selectedRow.getMaterialTestId()).append("\n");
                    details.append("Section: ").append(selectedRow.getSectionName() != null ? selectedRow.getSectionName() : "-").append("\n");
                    details.append("Supplier: ").append(selectedRow.getSupplierName() != null ? selectedRow.getSupplierName() : "-").append("\n");
                    details.append("Supplier Code: ").append(selectedRow.getSupplierCode() != null ? selectedRow.getSupplierCode() : "-").append("\n");
                    details.append("Country: ").append(selectedRow.getCountryName() != null ? selectedRow.getCountryName() : "-").append("\n");
                    details.append("Material: ").append(selectedRow.getMaterialName() != null ? selectedRow.getMaterialName() : "-").append("\n");
                    details.append("Material Description: ").append(selectedRow.getMaterialDesName() != null ? selectedRow.getMaterialDesName() : "-").append("\n");
                    details.append("PO No: ").append(selectedRow.getPoNo() != null ? selectedRow.getPoNo() : "-").append("\n");
                    details.append("Receipt: ").append(selectedRow.getReceipt() != null ? selectedRow.getReceipt() : "-").append("\n");
                    details.append("Oracle Sample: ").append(selectedRow.getOracleSample() != null ? selectedRow.getOracleSample() : "-").append("\n");
                    details.append("Item Code: ").append(selectedRow.getItemCode() != null ? selectedRow.getItemCode() : "-").append("\n");
                    details.append("SPQR: ").append(selectedRow.getSpqr() != null ? selectedRow.getSpqr() : "-").append("\n");
                    details.append("Total Quantity: ").append(selectedRow.getTotalQuantity()).append("\n");
                    details.append("Accepted Quantity: ").append(selectedRow.getQuantityAccepted()).append("\n");
                    details.append("Rejected Quantity: ").append(selectedRow.getQuantityRejected()).append("\n");
                    details.append("Notes: ").append(selectedRow.getNotes() != null ? selectedRow.getNotes() : "-").append("\n");
                    details.append("Comment: ").append(selectedRow.getComment() != null ? selectedRow.getComment() : "-").append("\n");
                    details.append("Creation Date: ").append(
                            selectedRow.getCreationDate() != null ? selectedRow.getCreationDate().format(dateFormatter) : "-"
                    ).append("\n");
                    details.append("User: ").append(selectedRow.getUserFullName() != null ? selectedRow.getUserFullName() : "-").append("\n");
                    showInfo(details.toString());

                }
            });

        });
    }

    // Method to display tool details in a styled dialog
    private void showInfo(String message) {
        Platform.runLater(() -> {
            // Create a new information alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Material Test");
            alert.setHeaderText("Details");

            // Initialize a styled text area for displaying details
            InlineCssTextArea richTextArea = new InlineCssTextArea();
            richTextArea.setEditable(false);
            richTextArea.setWrapText(true);

            // Split message into lines and calculate maximum line length
            String[] lines = message.split("\n");
            int maxLineLength = 0;
            for (String line : lines) {
                maxLineLength = Math.max(maxLineLength, line.length());
            }

            // Style each line with appropriate formatting
            for (String line : lines) {
                if (line.contains(":")) {
                    String[] parts = line.split(": ", 2);
                    richTextArea.appendText(parts[0] + ": ");
                    int titleStart = richTextArea.getLength() - (parts[0] + ": ").length();
                    int titleEnd = richTextArea.getLength();
                    richTextArea.setStyle(titleStart, titleEnd, "-fx-font-size:13;-fx-font-weight: bold; -fx-fill: #00008B;");

                    if (parts.length > 1) {
                        richTextArea.appendText(parts[1]);
                        int valueStart = richTextArea.getLength() - parts[1].length();
                        int valueEnd = richTextArea.getLength();
                        richTextArea.setStyle(valueStart, valueEnd, "-fx-font-size:12;-fx-font-weight: bold; -fx-fill: #000000;");
                    }
                } else {
                    richTextArea.appendText(line);
                    int textStart = richTextArea.getLength() - line.length();
                    int textEnd = richTextArea.getLength();
                    richTextArea.setStyle(textStart, textEnd, "-fx-font-size:12;-fx-font-weight: normal; -fx-fill: #000000;");
                }
                richTextArea.appendText("\n\n");
            }

            // Set reduced line height and calculate preferred height
            double lineHeight = 15; // Reduced from 20 to decrease overall height
            int lineCount = lines.length * 2;
            richTextArea.setPrefHeight(Math.min(lineCount * lineHeight + 20, 600));

            // Calculate dynamic width based on longest line
            double charWidth = 7.5;
            double prefWidth = Math.min(maxLineLength * charWidth + 50, 800);
            richTextArea.setPrefWidth(prefWidth);

            // Add text area to a container
            VBox container = new VBox(richTextArea);
            alert.getDialogPane().setContent(container);
            alert.getDialogPane().setPrefWidth(prefWidth + 60);

            // Add Copy All button
            ButtonType copyButton = new ButtonType("Copy All", ButtonBar.ButtonData.LEFT);
            alert.getButtonTypes().add(copyButton);

            // Add Close button (X) to allow closing the dialog
            ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().add(closeButton);

            // Handle button actions
            alert.showAndWait().ifPresent(response -> {
                if (response == copyButton) {
                    // Copy details to clipboard
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    ClipboardContent content = new ClipboardContent();
                    content.putString(message);
                    clipboard.setContent(content);
                }
                // Dialog closes automatically when Close or X is clicked
            });
        });
    }

    @FXML
    void clearSearch(ActionEvent event) {
        clearHelp();
    }

    void clearHelp() {
        filter_material_test_id_textF.clear();
        filter_item_code_textF.clear();
        from_creation_date.setValue(null);
        to_creation_date.setValue(null);
        section_comb.getSelectionModel().clearSelection();
        supplier_comb.getSelectionModel().clearSelection();
        supplier_country_comb.getSelectionModel().clearSelection();
        material_comb.getSelectionModel().clearSelection();
        loadData();
    }

    @FXML
    void searchWithFilter(ActionEvent event) {
        filterMaterialTests();
    }

    @FXML
    void filterMaterialTestId(KeyEvent event) {
        filterMaterialTests();
    }


    @FXML
    void update(ActionEvent event) {
        refreshTable();
    }

    public void refreshTable() {
        Platform.runLater(() -> {
            clearHelp();
            loadData();
            material_tests_table_view.refresh();
            updateMaterialTestCount();
        });
    }

    void updateMaterialTestCount() {
        int total = MaterialTestDao.getMaterialTestCount();
        int fo = MaterialTestDao.getMaterialTestCountBySection(1);
        int cu = MaterialTestDao.getMaterialTestCountBySection(2);
        int acc = MaterialTestDao.getMaterialTestCountBySection(3);
        total_material_tests_count_textF.setText(String.valueOf(total));
        fo_material_tests_count_textF.setText(String.valueOf(fo));
        copper_material_tests_count_textF.setText(String.valueOf(cu));
        accessories_material_tests_count_textF.setText(String.valueOf(acc));
    }

    @FXML
    void openAddMaterialTest(ActionEvent event) {
        OPEN_ADD_MATERIAL_TESTS_PAGE(false, this);
    }
}

