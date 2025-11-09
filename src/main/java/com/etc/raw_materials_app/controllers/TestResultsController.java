
package com.etc.raw_materials_app.controllers;

import com.etc.raw_materials_app.dao.MaterialTestDao;
import com.etc.raw_materials_app.dao.SampleDao;
import com.etc.raw_materials_app.dao.TestNameDao;
import com.etc.raw_materials_app.dao.TestResultDao;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.*;
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
import org.apache.poi.ss.usermodel.Cell;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.CheckComboBox;

public class TestResultsController implements Initializable {

    @FXML private ComboBox<Sample> sample_comb;
    @FXML private TableColumn<TestResult, String> actual_column;
    @FXML private TableColumn<TestResult, String> sample_no_column;
    @FXML private Button addTest_btn;
    @FXML private FontIcon clear_selected_results_icon;
    @FXML private TableColumn<TestResult, LocalDateTime> creation_date_column;
    @FXML private TableColumn<TestResult, String> delete_column;
    @FXML private TableColumn<TestResult, String> item_code_column;
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
    @FXML private ImageView excel_image_view;
    @FXML private Button   export_excel_btn ;


    private int materialTestId;
    private String supplierName;
    private String materialName;
    private LocalDateTime materialTestcreationDate;
    private String poNo;
    private String oracleSample;
    private String itemCode;
    private String comment ;
    private Stage stage;


    private final ObservableList<TestResult> testResultsList = FXCollections.observableArrayList();
    private final List<String> selectedTestNames = new ArrayList<>();
    private final List<Integer> selectedTestNameIds = new ArrayList<>();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a");
    private CheckComboBox<TestName> checkComboBox;
    
    TestResultDao testResultDao = new TestResultDao();
    SampleDao sampleDao = new SampleDao();
    TestNameDao testNameDao = new TestNameDao();

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image img2 = new Image(Objects.requireNonNull(TestResultsController.class.getResourceAsStream("/images/excel.png")));
        excel_image_view.setImage(img2);

        // Set cursor for clear icon
        clear_selected_results_icon.setCursor(Cursor.HAND);

        sample_comb.setItems(sampleDao.getAllSamples());

        // Load all tests
        ObservableList<TestName> allTestNames = testNameDao.getAllTestNames();

        // Create ControlsFX CheckComboBox
        checkComboBox = new CheckComboBox<>(allTestNames);
        checkComboBox.setPrefWidth(350); //
        checkComboBox.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

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

        checkComboBox.getCheckModel().clearChecks();
        selectedTestNames.clear();
        selectedTestNameIds.clear();
        updateSelectedLabel();

       // Place CheckComboBox in your layout
        select_tests_btn.setOnAction(e -> {
            Stage popup = new Stage();
            VBox vbox = new VBox(10, checkComboBox);
            vbox.setPadding(new javafx.geometry.Insets(10));
            javafx.scene.Scene scene = new javafx.scene.Scene(vbox);

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

        // set permissions
        int role = UserContext.getCurrentUser().getRole();
        if (role == 3 || role == 4) {
            addTest_btn.setVisible(true);
            delete_column.setVisible(true);
            select_tests_btn.setVisible(true);
        } else {
            addTest_btn.setVisible(false);
            delete_column.setVisible(false);
            select_tests_btn.setVisible(false);
        }


    }


    public void initData(int materialTestId, String supplierName, String materialName,LocalDateTime materialTestcreationDate,
                         String poNo, String oracleSample, String itemCode ,String comment) {
        this.materialTestId = materialTestId;
        this.supplierName = supplierName;
        this.materialName = materialName;
        this.materialTestcreationDate = materialTestcreationDate ;
        this.poNo = poNo;
        this.oracleSample = oracleSample;
        this.itemCode = itemCode;
        this.comment = comment;

        loadTestResults();
    }

    private void loadTestResults() {
        testResultsList.clear();
        ObservableList<TestResult> allResults = testResultDao.getAllTestResults();
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
        Sample sample  = sample_comb.getValue();
        if (sample == null) {
            WindowUtils.ALERT("Warning", "Please select a sample first.", WindowUtils.ALERT_WARNING);
            return;
        }
        if (selectedTestNameIds.isEmpty()) {
            WindowUtils.ALERT("Warning", "Please select at least one test from the list.", WindowUtils.ALERT_WARNING);
            return;
        }

        for (int i = 0; i < selectedTestNameIds.size(); i++) {
            TestResult newResult = new TestResult();
            newResult.setTestResultId(0); // Temp ID
            newResult.setMaterialTestId(materialTestId);
            newResult.setSampleId(sample.getSampleId());
            newResult.setSampleName(sample.getSampleName());
            newResult.setTestNameId(selectedTestNameIds.get(i));
            newResult.setTestName(selectedTestNames.get(i));
            newResult.setUserId(UserContext.getCurrentUser().getUserId());
            newResult.setUserFullName(UserContext.getCurrentUser().getFullName());
            newResult.setCreationDate(LocalDateTime.now());

            MaterialTest mt = new MaterialTest();
            mt.setMaterialTestId(materialTestId); // Fix: Set ID in MaterialTest
            mt.setSupplierName(supplierName);
            mt.setMaterialName(materialName);
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
        checkComboBox.getCheckModel().clearChecks();
        selectedTestNames.clear();
        selectedTestNameIds.clear();
        updateSelectedLabel();
        if (sample_comb != null) sample_comb.getSelectionModel().clearSelection();
    }

    private void setupTableColumns() {
        // Fixed columns
        material_test_id_column.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getMaterialTestId())));
        supplier_name_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaterialTest().getSupplierName()));
        material_name_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaterialTest().getMaterialName()));
        sample_no_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSampleName()));

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
                                    boolean deleted = testResultDao.deleteTestResult(tr.getTestResultId());
                                    if (deleted) {
                                        getTableView().getItems().remove(tr);
                                        WindowUtils.ALERT("Success", "Test result deleted successfully.", WindowUtils.ALERT_INFORMATION);
                                    } else {
                                        WindowUtils.ALERT("Error", "Failed to delete: " + testResultDao.lastErrorMessage, WindowUtils.ALERT_ERROR);
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
        List.of(material_test_id_column,sample_no_column, supplier_name_column, material_name_column,
                po_no_column, oracle_sample_column, item_code_column, test_id_column, test_name_column,
                requirement_column ,actual_column).forEach(c -> c.setStyle(style));
        List.of(creation_date_column, test_situation_column, delete_column, user_full_name_column )
                .forEach(c -> c.setStyle(style2));
    }

    private void saveOrUpdateTestResult(TestResult tr) {
        tr.setUserId(UserContext.getCurrentUser().getUserId());
        tr.setCreationDate(LocalDateTime.now());

        if (tr.getTestResultId() == 0) {
            Integer newId = testResultDao.insertTestResult(tr);
            if (newId != null) {
                tr.setTestResultId(newId);
                int index = testResultsList.indexOf(tr);
                testResultsList.set(index, tr);
                table_view.refresh();
            } else {
                WindowUtils.ALERT("Error", "Failed to save: " + testResultDao.lastErrorMessage, WindowUtils.ALERT_ERROR);
            }
        } else {
            if (!testResultDao.updateTestResult(tr)) {
                WindowUtils.ALERT("Error", "Failed to update: " + testResultDao.lastErrorMessage, WindowUtils.ALERT_ERROR);
            }
        }
    }

    @FXML
    void exportToExcel(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.setInitialFileName("Material_Testing_Report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".xlsx");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        java.io.File file = fileChooser.showSaveDialog(table_view.getScene().getWindow());
        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Material Testing Report");

                // === STYLES ===
                CellStyle centerStyle = workbook.createCellStyle();
                centerStyle.setAlignment(HorizontalAlignment.CENTER);
                centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                centerStyle.setWrapText(true);
                Font boldFont = workbook.createFont();
                boldFont.setBold(true);
                centerStyle.setFont(boldFont);
                // 2
                CellStyle deptNameStyle = workbook.createCellStyle();
                deptNameStyle.setAlignment(HorizontalAlignment.CENTER);
                deptNameStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                deptNameStyle.setWrapText(true);
                boldFont = workbook.createFont();
                boldFont.setBold(true);
                boldFont.setFontHeightInPoints((short) 15);
                deptNameStyle.setFont(boldFont);

                CellStyle leftStyle = workbook.createCellStyle();
                leftStyle.cloneStyleFrom(centerStyle);
                leftStyle.setAlignment(HorizontalAlignment.LEFT);

                CellStyle titleStyle = workbook.createCellStyle();
                Font titleFont = workbook.createFont();
                titleFont.setFontName("Calibri");
                titleFont.setFontHeightInPoints((short) 18);
                titleFont.setBold(true);
                titleFont.setColor(IndexedColors.BLACK.getIndex());
                titleFont.setUnderline(Font.U_SINGLE);
                titleStyle.setFont(titleFont);
                titleStyle.setAlignment(HorizontalAlignment.CENTER);
                titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);


                CellStyle infoStyle = workbook.createCellStyle();
                infoStyle.cloneStyleFrom(leftStyle);
                infoStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(141, 180, 226), null));
                infoStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                infoStyle.setBorderTop(BorderStyle.THIN);
                infoStyle.setBorderBottom(BorderStyle.THIN);
                infoStyle.setBorderLeft(BorderStyle.THIN);
                infoStyle.setBorderRight(BorderStyle.THIN);

                CellStyle tableHeaderStyle = workbook.createCellStyle();
                tableHeaderStyle.cloneStyleFrom(centerStyle);
                tableHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                tableHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                tableHeaderStyle.setBorderTop(BorderStyle.THIN);
                tableHeaderStyle.setBorderBottom(BorderStyle.THIN);
                tableHeaderStyle.setBorderLeft(BorderStyle.THIN);
                tableHeaderStyle.setBorderRight(BorderStyle.THIN);

                CellStyle borderedCenter = workbook.createCellStyle();
                borderedCenter.cloneStyleFrom(centerStyle);
                borderedCenter.setBorderTop(BorderStyle.THIN);
                borderedCenter.setBorderBottom(BorderStyle.THIN);
                borderedCenter.setBorderLeft(BorderStyle.THIN);
                borderedCenter.setBorderRight(BorderStyle.THIN);

                CellStyle passStyle = workbook.createCellStyle();
                passStyle.cloneStyleFrom(borderedCenter);
                passStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                passStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                CellStyle failStyle = workbook.createCellStyle();
                failStyle.cloneStyleFrom(borderedCenter);
                failStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                // === LOGO ===
                Row logoRow = sheet.createRow(0);
                logoRow.setHeight((short) (60 * 20));
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
                InputStream logoStream = TestResultsController.class.getResourceAsStream("/images/logo_excel.png");
                if (logoStream != null) {
                    byte[] bytes = logoStream.readAllBytes();
                    int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                    XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                    XSSFClientAnchor anchor = new XSSFClientAnchor(100 * 1000, 100 * 1000, -100 * 1000, -100 * 1000, (short) 0, 0, (short) 2, 1);
                    drawing.createPicture(anchor, pictureIdx);
                    logoStream.close();
                }

                // === DEPARTMENT ===
                Row deptRow = sheet.createRow(1);
                deptRow.setHeight((short) (30 * 20));
                Cell deptCell = deptRow.createCell(0);
                deptCell.setCellValue("Quality Control Department");
                deptCell.setCellStyle(deptNameStyle);
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 1));

                // === TITLE ===
                Row titleRow = sheet.createRow(3);
                titleRow.setHeight((short) (titleRow.getHeight() * 2));
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("Material Testing Report");
                titleCell.setCellStyle(titleStyle);
                sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 6));

                // === INFO TABLE ===
                String[] infoLabels = {
                        "Issue date:",
                        "Receiving Notice Number (P.O. No.):",
                        "Inspection Notice Number (Oracle sample N.O):",
                        "Supplier Name:"
                };

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String[] infoValues = {
                        dateFormat.format(Date.from(materialTestcreationDate.atZone(ZoneId.systemDefault()).toInstant())),
                        poNo != null ? poNo : "",
                        oracleSample != null ? oracleSample : "",
                        supplierName != null ? supplierName : ""
                };

                int infoStartRow = 4;
                for (int i = 0; i < infoLabels.length; i++) {
                    Row row = sheet.createRow(infoStartRow + i);
                    row.setHeight((short) (25 * 20));
                    Cell labelCell = row.createCell(0);
                    labelCell.setCellValue(infoLabels[i]);
                    labelCell.setCellStyle(infoStyle);
                    Cell dummyCell = row.createCell(1);
                    dummyCell.setCellStyle(infoStyle);
                    sheet.addMergedRegion(new CellRangeAddress(infoStartRow + i, infoStartRow + i, 0, 1));
                    Cell valueCell = row.createCell(2);
                    valueCell.setCellValue(infoValues[i]);
                    valueCell.setCellStyle(infoStyle);
                }

                // === MAIN TABLE HEADER ===
                int headerRowNum = infoStartRow + infoLabels.length + 1;
                Row headerRow = sheet.createRow(headerRowNum);
                headerRow.setHeight((short) (headerRow.getHeight() * 2));

                String[] headers = {
                        "انواع الاختبارات",
                        "Material Description",
                        "Material Code",
                        "Sample no.",
                        "Requirement",
                        "Actual",
                        "Result (Pass/Fail)"
                };

                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(tableHeaderStyle);
                }

                // === MAIN TABLE DATA + MERGE LOGIC ===
                int rowNum = headerRowNum + 1;
                List<TestResult> results = new ArrayList<>(table_view.getItems());

                // ترتيب حسب Sample No. لضمان التجميع الصحيح
                results.sort(Comparator.comparing(
                        r -> r.getSampleName() != null ? r.getSampleName() : "",
                        Comparator.nullsLast(String::compareTo)
                ));

                int materialDescCol = 1;
                int materialCodeCol = 2;
                int sampleCol = 3;

                String currentSample = null;
                int sampleStartRow = -1;

                // لتتبع بداية كل مجموعة Material (نفس القيم دائمًا)
                int groupStartRow = rowNum;

                for (int i = 0; i < results.size(); i++) {
                    TestResult result = results.get(i);
                    String sampleName = result.getSampleName() != null ? result.getSampleName() : "";

                    Row row = sheet.createRow(rowNum++);

                    // 1. انواع الاختبارات
                    Cell testNameCell = row.createCell(0);
                    testNameCell.setCellValue(result.getTestName());
                    testNameCell.setCellStyle(borderedCenter);

                    // 2. Material Description (سيُدمج لاحقًا)
                    Cell materialCell = row.createCell(materialDescCol);
                    materialCell.setCellValue(materialName); // أو materialDescription
                    materialCell.setCellStyle(borderedCenter);

                    // 3. Material Code (سيُدمج لاحقًا)
                    Cell codeCell = row.createCell(materialCodeCol);
                    codeCell.setCellValue(itemCode);
                    codeCell.setCellStyle(borderedCenter);

                    // 4. Sample No.
                    Cell sampleCell = row.createCell(sampleCol);
                    sampleCell.setCellValue(sampleName);
                    sampleCell.setCellStyle(borderedCenter);

                    // 5. Requirement
                    Cell reqCell = row.createCell(4);
                    reqCell.setCellValue(result.getRequirement() != null ? result.getRequirement() : "");
                    reqCell.setCellStyle(borderedCenter);

                    // 6. Actual
                    Cell actualCell = row.createCell(5);
                    actualCell.setCellValue(result.getActual() != null ? result.getActual() : "");
                    actualCell.setCellStyle(borderedCenter);

                    // 7. Result
                    Cell resultCell = row.createCell(6);
                    String resultText = "";
                    CellStyle resultStyle = borderedCenter;
                    if (result.getTestSituation() != null) {
                        resultText = result.getTestSituation() == 1 ? "Pass" : "Fail";
                        resultStyle = result.getTestSituation() == 1 ? passStyle : failStyle;
                    }
                    resultCell.setCellValue(resultText);
                    resultCell.setCellStyle(resultStyle);

                    // ===  Sample No. ===
                    if (!sampleName.equals(currentSample)) {
                        // دمج المجموعة السابقة
                        if (currentSample != null && sampleStartRow != -1) {
                            if (rowNum - 2 >= sampleStartRow) {
                                sheet.addMergedRegion(new CellRangeAddress(sampleStartRow, rowNum - 2, sampleCol, sampleCol));
                            }
                        }
                        currentSample = sampleName;
                        sampleStartRow = rowNum - 1;
                    }
                }

                // Merge Sample No.
                if (currentSample != null && sampleStartRow != -1 && rowNum - 1 > sampleStartRow) {
                    sheet.addMergedRegion(new CellRangeAddress(sampleStartRow, rowNum - 1, sampleCol, sampleCol));
                }

                // === دمج Material Description + Material Code (لكل مجموعة Sample) ===
                // نفترض أن كل Sample لها نفس Material Description و Code
                int descStart = groupStartRow;
                int descEnd = rowNum - 1;
                if (descEnd > descStart) {
                    sheet.addMergedRegion(new CellRangeAddress(descStart, descEnd, materialDescCol, materialDescCol));
                    sheet.addMergedRegion(new CellRangeAddress(descStart, descEnd, materialCodeCol, materialCodeCol));
                }

                // === COMMENTS ===
                Row commentsRow = sheet.createRow(rowNum++);
                commentsRow.setHeight((short) (40 * 20));
                Cell commentsCell = commentsRow.createCell(0);
                commentsCell.setCellValue("Comments: " + (comment != null ? comment : ""));
                commentsCell.setCellStyle(leftStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 6));

                // === EMPTY ROWS ===
                sheet.createRow(rowNum++);
                sheet.createRow(rowNum++);

                // === FOOTER (Manual) ===
                Row footerRow = sheet.createRow(rowNum++);
                Cell testedCell = footerRow.createCell(0);
                testedCell.setCellValue("Tested by: m.abdelmgged");
                testedCell.setCellStyle(leftStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 2));

                Cell revisedCell = footerRow.createCell(4);
                revisedCell.setCellValue("Revised by:");
                revisedCell.setCellStyle(leftStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 4, 6));

                // === PAGE FOOTER (Print Footer) - حجم 18 ===
                HeaderFooter footer = sheet.getFooter();
                footer.setLeft("&\"Calibri\"&16&B&IQC-FR-20");
                footer.setCenter("&\"Calibri\"&16&B&IRev.(0)");
                footer.setRight("&\"Calibri\"&16&B&IIssue date: 01/01/2024");

                // === AUTO-SIZE COLUMNS ===
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                    int width = sheet.getColumnWidth(i) / 256;
                    if (width < 12) sheet.setColumnWidth(i, 12 * 256);
                    if (width > 50) sheet.setColumnWidth(i, 40 * 256);
                }

                // === WRITE FILE ===
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                    WindowUtils.ALERT("Success", "Excel file exported successfully to " + file.getAbsolutePath(), WindowUtils.ALERT_INFORMATION);
                }

            } catch (IOException e) {
                Logging.logException("ERROR", TestResultsController.class.getName(), "exportToExcel", e);
                WindowUtils.ALERT("Error", "Failed to export Excel file", WindowUtils.ALERT_ERROR);
            }
        }
    }


}