

package com.etc.raw_materials_app.controllers;

import com.etc.raw_materials_app.dao.MaterialTestDao;
import com.etc.raw_materials_app.dao.TestNameDao;
import com.etc.raw_materials_app.dao.TestResultDao;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.MaterialTest;
import com.etc.raw_materials_app.models.TestName;
import com.etc.raw_materials_app.models.TestResult;
import com.etc.raw_materials_app.models.UserContext;
import com.etc.raw_materials_app.services.UserService;
import com.etc.raw_materials_app.services.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.css.converter.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.CheckComboBox;

public class TestResultsController implements Initializable {
    private int materialTestId;
    private String supplierName;
    private String materialName;
    private String materialDesName;
    private String poNo;
    private String oracleSample;
    private String itemCode;
    private String comment ;
    private Stage stage;

    private final ObservableList<TestResult> testResultsList = FXCollections.observableArrayList();
    private final List<String> selectedTestNames = new ArrayList<>();
    private final List<Integer> selectedTestNameIds = new ArrayList<>();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a");
    private FilteredList<TestName> filteredTestNames;

    @FXML private TableColumn<TestResult, String> actual_column;
    @FXML private Button addTest_btn;
    @FXML private FontIcon clear_selected_results_icon;
    @FXML private TableColumn<TestResult, LocalDateTime> creation_date_column;
    @FXML private TableColumn<TestResult, String> delete_column;
    @FXML private TableColumn<TestResult, String> item_code_column;
    @FXML private TableColumn<TestResult, String> material_document_name_column;
    @FXML private TableColumn<TestResult, String> material_name_column;
    @FXML private TableColumn<TestResult, String> material_test_id_column;
    @FXML private TableColumn<TestResult, String> oracle_sample_column;
    @FXML private TableColumn<TestResult, String> po_no_column;
    @FXML private TableColumn<TestResult, String> requirement_column;
    @FXML private TableColumn<TestResult, String> supplier_name_column;
    @FXML private TableView<TestResult> table_view;
    @FXML private TableColumn<TestResult, String> test_id_column;
    @FXML private TableColumn<TestResult, String> test_name_column;
    @FXML private ComboBox<TestName> test_names_comb;
    @FXML private TableColumn<TestResult, Integer> test_situation_column;
    @FXML private Label selected_test_results_lbl;
    @FXML Button select_tests_btn ;
    @FXML private TableColumn<TestResult, String> user_full_name_column;




    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set cursor for clear icon
        clear_selected_results_icon.setCursor(Cursor.HAND);



        // Load all tests
        ObservableList<TestName> allTestNames = TestNameDao.getAllTestNames();

// Create ControlsFX CheckComboBox
        CheckComboBox<TestName> checkComboBox = new CheckComboBox<>(allTestNames);
        checkComboBox.setPrefWidth(350); // زيادة عرض الصندوق
        checkComboBox.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;"); // تكبير النص

// Listener for selection changes
        checkComboBox.getCheckModel().getCheckedItems().addListener((javafx.collections.ListChangeListener<TestName>) c -> {
            selectedTestNames.clear();
            selectedTestNameIds.clear();

            for (TestName t : checkComboBox.getCheckModel().getCheckedItems()) {
                selectedTestNameIds.add(t.getTestNameId());
                selectedTestNames.add(t.getTestName());
            }
            updateSelectedLabel();
        });

// Place CheckComboBox in your layout
        select_tests_btn.setOnAction(e -> {
            Stage popup = new Stage();
            VBox vbox = new VBox(10, checkComboBox);
            vbox.setPadding(new javafx.geometry.Insets(10));
            javafx.scene.Scene scene = new javafx.scene.Scene(vbox);

            // Optional: تكبير النافذة إذا كانت القائمة طويلة
          //  checkComboBox.setPrefHeight(Math.min(allTestNames.size() * 30, 300));

            popup.setScene(scene);
            popup.setTitle("Select Tests");
            popup.initOwner(select_tests_btn.getScene().getWindow());
            popup.setAlwaysOnTop(true);
            popup.show();
        });




        // Setup table
        table_view.setEditable(true);
        setupTableColumns();
        addTest_btn.setCursor(Cursor.HAND);
    }

    public void initData(int materialTestId, String supplierName, String materialName,
                         String materialDesName, String poNo, String oracleSample, String itemCode ,String comment) {
        this.materialTestId = materialTestId;
        this.supplierName = supplierName;
        this.materialName = materialName;
        this.materialDesName = materialDesName;
        this.poNo = poNo;
        this.oracleSample = oracleSample;
        this.itemCode = itemCode;
        this.comment = comment;

        loadTestResults();
    }

    private void loadTestResults() {
        testResultsList.clear();
        ObservableList<TestResult> allResults = TestResultDao.getAllTestResults();
        for (TestResult tr : allResults) {
            if (tr.getMaterialTestId() == materialTestId) {
                testResultsList.add(tr);
            }
        }
        table_view.setItems(testResultsList);
        table_view.refresh();
    }

    private void updateSelectedLabel() {
        selected_test_results_lbl.setText(String.join(" , ", selectedTestNames));
    }

    @FXML
    void addRow(ActionEvent event) {
        if (selectedTestNameIds.isEmpty()) {
            WindowUtils.ALERT("Warning", "Please select at least one test from the list.", WindowUtils.ALERT_WARNING);
            return;
        }

        for (int i = 0; i < selectedTestNameIds.size(); i++) {
            TestResult newResult = new TestResult();
            newResult.setTestResultId(0); // Temp ID
            newResult.setMaterialTestId(materialTestId);
            newResult.setTestNameId(selectedTestNameIds.get(i));
            newResult.setTestName(selectedTestNames.get(i));
            newResult.setUserId(UserContext.getCurrentUser().getUserId());
            newResult.setUserFullName(UserContext.getCurrentUser().getFullName());
            newResult.setCreationDate(LocalDateTime.now());

            MaterialTest mt = new MaterialTest();
            mt.setMaterialTestId(materialTestId); // Fix: Set ID in MaterialTest
            mt.setSupplierName(supplierName);
            mt.setMaterialName(materialName);
            mt.setMaterialDesName(materialDesName);
            mt.setPoNo(poNo);
            mt.setOracleSample(oracleSample);
            mt.setItemCode(itemCode);
            newResult.setMaterialTest(mt);

            testResultsList.add(newResult);
        }

        table_view.refresh();

        // Reset only
        selectedTestNames.clear();
        selectedTestNameIds.clear();
        updateSelectedLabel();
      //  test_names_comb.getSelectionModel().clearSelection();

    }

    @FXML
    void clear_selected_result(MouseEvent event) {
        selectedTestNames.clear();
        selectedTestNameIds.clear();
        updateSelectedLabel();
        //test_names_comb.getSelectionModel().clearSelection();
    }

    private void setupTableColumns() {
        // Fixed columns
        material_test_id_column.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getMaterialTestId())));
        supplier_name_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaterialTest().getSupplierName()));
        material_name_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaterialTest().getMaterialName()));
        material_document_name_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaterialTest().getMaterialDesName()));
        po_no_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaterialTest().getPoNo()));
        oracle_sample_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaterialTest().getOracleSample()));
        item_code_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaterialTest().getItemCode()));
        test_id_column.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getTestResultId())));
        test_name_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTestName()));
        user_full_name_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUserFullName()));
        // Creation Date with time
        creation_date_column.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getCreationDate()));
        creation_date_column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : dateTimeFormatter.format(item));
            }
        });

        // Requirement - Editable
        requirement_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRequirement()));
        requirement_column.setCellFactory(TextFieldTableCell.forTableColumn());
        requirement_column.setOnEditCommit(event -> {
            TestResult tr = event.getRowValue();
            tr.setRequirement(event.getNewValue());
            saveOrUpdateTestResult(tr);
        });

        // Actual - Editable
        actual_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getActual()));
        actual_column.setCellFactory(TextFieldTableCell.forTableColumn());
        actual_column.setOnEditCommit(event -> {
            TestResult tr = event.getRowValue();
            tr.setActual(event.getNewValue());
            saveOrUpdateTestResult(tr);
        });

        // Test Situation: Pass/Fail with CSS classes
        test_situation_column.setCellValueFactory(cell -> {
            Integer situation = cell.getValue().getTestSituation();
            return new SimpleIntegerProperty(situation != null ? situation : 0).asObject();
        });
        test_situation_column.getStyleClass().add("test-situation-cell");
        test_situation_column.setCellFactory(col -> new TableCell<>() {
            private final CheckBox pass = new CheckBox("Pass");
            private final CheckBox fail = new CheckBox("Fail");
            private final HBox hBox = new HBox(22, pass, fail);

            {
                pass.getStyleClass().add("pass-checkbox");
                fail.getStyleClass().add("fail-checkbox");
              //  pass.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                pass.setStyle("-fx-text-fill: green; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 3px 6px;");
                fail.setStyle("-fx-text-fill: red; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 3px 6px;");

             //   fail.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

                hBox.setAlignment(Pos.CENTER);
                hBox.setStyle("-fx-alignment: CENTER; -fx-padding: 4; -fx-spacing: 12;");


                hBox.setStyle("-fx-alignment: CENTER; -fx-padding: 1; -fx-spacing: 3;");
                pass.setOnAction(e -> handleTestSituation(getIndex(), pass.isSelected() ? 1 : null));
                fail.setOnAction(e -> handleTestSituation(getIndex(), fail.isSelected() ? 2 : null));
            }

            private void handleTestSituation(int index, Integer situation) {
                TestResult tr = getTableView().getItems().get(index);
                tr.setTestSituation(situation);
                saveOrUpdateTestResult(tr);
                updateCheckBoxes(tr.getTestSituation());
                getTableView().refresh();
            }

            private void updateCheckBoxes(Integer situation) {
                pass.setSelected(situation != null && situation == 1);
                fail.setSelected(situation != null && situation == 2);
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()).getTestResultId() == 0) {
                    setGraphic(null);
                } else {
                    TestResult tr = getTableView().getItems().get(getIndex());
                    updateCheckBoxes(tr.getTestSituation());
                    setGraphic(hBox);
                }
            }
        });

       // Delete Button
        delete_column.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            private final FontIcon icon = new FontIcon("fas-trash");

            {
                icon.setIconSize(12);
                icon.setIconColor(Color.RED);
                btn.setGraphic(icon);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setCursor(Cursor.HAND);

                btn.setOnAction(e -> {
                    TestResult tr = getTableView().getItems().get(getIndex());
                    if (tr == null || tr.getTestResultId() == 0) return;

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Confirmation");
                    alert.setHeaderText("Are you sure you want to delete this test result?");
                    alert.setContentText("Test name: " + tr.getTestName());

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
                                    boolean deleted = TestResultDao.deleteTestResult(tr.getTestResultId());
                                    if (deleted) {
                                        getTableView().getItems().remove(tr);
                                        WindowUtils.ALERT("Success", "Test result deleted successfully.", WindowUtils.ALERT_INFORMATION);
                                    } else {
                                        WindowUtils.ALERT("Error", "Failed to delete: " + TestResultDao.lastErrorMessage, WindowUtils.ALERT_ERROR);
                                    }
                                } catch (Exception ex) {
                                    Logging.logException("ERROR", getClass().getName(), "delete test result", ex);
                                    WindowUtils.ALERT("Error", "Failed to delete test result.", WindowUtils.ALERT_ERROR);
                                }
                            } else {
                                WindowUtils.ALERT("Error", "Password not correct.", WindowUtils.ALERT_WARNING);
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    TestResult tr = getTableView().getItems().get(getIndex());
                    setGraphic(tr != null && tr.getTestResultId() != 0 ? btn : null);
                }
            }
        });


        // Double-click to edit requirement/actual
        table_view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TablePosition<TestResult, String> pos = table_view.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                TableColumn<TestResult, String> col = pos.getTableColumn();
                if (col == requirement_column || col == actual_column) {
                    table_view.edit(row, col);
                }
            }
        });

        // Styling
        String style = "-fx-alignment: CENTER; -fx-font-size: 12px; -fx-font-weight: bold;";
        String style2 = "-fx-alignment: CENTER; -fx-font-size: 11px; -fx-font-weight: bold;";
        table_view.setFixedCellSize(32);
        List.of(material_test_id_column, supplier_name_column, material_name_column, material_document_name_column,
                po_no_column, oracle_sample_column, item_code_column, test_id_column, test_name_column,
                requirement_column ,actual_column).forEach(c -> c.setStyle(style));
        List.of(creation_date_column, test_situation_column, delete_column, user_full_name_column )
                .forEach(c -> c.setStyle(style2));
    }

    private void saveOrUpdateTestResult(TestResult tr) {
        tr.setUserId(UserContext.getCurrentUser().getUserId());
        tr.setCreationDate(LocalDateTime.now());

        if (tr.getTestResultId() == 0) {
            Integer newId = TestResultDao.insertTestResult(tr);
            if (newId != null) {
                tr.setTestResultId(newId);
                int index = testResultsList.indexOf(tr);
                testResultsList.set(index, tr);
                table_view.refresh();
            } else {
                WindowUtils.ALERT("Error", "Failed to save: " + TestResultDao.lastErrorMessage, WindowUtils.ALERT_ERROR);
            }
        } else {
            if (!TestResultDao.updateTestResult(tr)) {
                WindowUtils.ALERT("Error", "Failed to update: " + TestResultDao.lastErrorMessage, WindowUtils.ALERT_ERROR);
            }
        }
    }

}