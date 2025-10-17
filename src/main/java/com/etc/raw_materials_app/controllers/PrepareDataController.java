//
//    package com.etc.raw_materials_app.controllers;
//
//    import com.etc.raw_materials_app.dao.CountryDao;
//    import com.etc.raw_materials_app.dao.TestNameDao;
//    import com.etc.raw_materials_app.dao.SupplierDao;
//    import com.etc.raw_materials_app.logging.Logging;
//    import com.etc.raw_materials_app.models.*;
//    import com.etc.raw_materials_app.services.UserService;
//    import com.etc.raw_materials_app.services.WindowUtils;
//    import javafx.application.Platform;
//    import javafx.collections.ObservableList;
//    import javafx.collections.transformation.FilteredList;
//    import javafx.collections.transformation.SortedList;
//    import javafx.event.ActionEvent;
//    import javafx.fxml.FXML;
//    import javafx.fxml.Initializable;
//    import javafx.scene.Cursor;
//    import javafx.scene.control.*;
//    import javafx.scene.control.cell.PropertyValueFactory;
//    import javafx.scene.input.KeyEvent;
//    import javafx.scene.layout.HBox;
//    import javafx.util.Callback;
//    import org.kordamp.ikonli.javafx.FontIcon;
//
//    import java.net.URL;
//    import java.util.ResourceBundle;
//
//    public class PrepareDataController implements Initializable {
//        @FXML
//        private Button add_country_btn;
//        @FXML
//        private Button add_supplier_btn;
//        @FXML
//        private Button add_supplier_country_btn;
//        @FXML
//        private Button add_test_btn;
//        @FXML
//        private Button clear_country_btn;
//        @FXML
//        private Button clear_supplier_btn;
//        @FXML
//        private Button clear_supplier_cou_btn;
//        @FXML
//        private Button clear_test_btn;
//        @FXML
//        private ComboBox<Country> country_comb;
//        @FXML
//        private TableColumn<Country, String> country_delete_colm;
//        @FXML
//        private TableColumn<Country, String> country_id_colm;
//        @FXML
//        private TableColumn<Country, String> country_name_colm;
//        @FXML
//        private TextField country_name_textF;
//        @FXML
//        private TableView<Country> country_table_view;
//        @FXML
//        private TextField filter_country_textF;
//        @FXML
//        private TextField filter_supplier_cou_textF;
//        @FXML
//        private TextField filter_supplier_textF;
//        @FXML
//        private TextField filter_test_textF;
//        @FXML
//        private ComboBox<Supplier> supplier_comb;
//        @FXML
//        private TableColumn<SupplierCountry, String> supplier_cou_delete_colm;
//        @FXML
//        private TableColumn<SupplierCountry, String> supplier_cou_name_colm;
//        @FXML
//        private TableView<SupplierCountry> supplier_country_table_view;
//        @FXML
//        private TableColumn<Supplier, String> supplier_delete_colm;
//        @FXML
//        private TableColumn<Supplier, String> supplier_id_colm;
//        @FXML private TableColumn<Supplier, String> supplier_code_colm;
//        @FXML private TableColumn<Supplier, String> supplier_name_colm;
//        @FXML private TableColumn<SupplierCountry, String> supplier_name_in_sup_country_colm;
//        @FXML private TextField supplier_name_textF;
//        @FXML private TextField supplier_code_textF;
//        @FXML private TextField update_supplier_code_textF;
//
//        @FXML
//        private TableView<Supplier> supplier_table_view;
//        @FXML
//        private TableColumn<TestName, String> test_delete_colm;
//        @FXML
//        private TableColumn<TestName, String> test_id_colm;
//        @FXML
//        private TableColumn<TestName, String> test_name_colm;
//        @FXML
//        private TextField test_name_textF;
//        @FXML
//        private TableView<TestName> test_names_table_view;
//        @FXML
//        private Button update_country_btn;
//        @FXML
//        private TextField update_country_name_textF;
//        @FXML
//        private Button update_supplier_btn;
//        @FXML
//        private Button update_supplier_cou_btn;
//        @FXML
//        private TextField update_supplier_name_textF;
//        @FXML
//        private Button update_test_btn;
//        @FXML
//        private TextField update_test_name_textF;
//        ObservableList<Supplier> supplierList;
//        ObservableList<Country> countryList;
//        ObservableList<TestName> testNamesList;
//
//
//        @Override
//        public void initialize(URL url, ResourceBundle resourceBundle) {
//            // Set ComboBox
//            supplier_comb.setItems(SupplierDao.getAllSuppliers());
//            country_comb.setItems(CountryDao.getAllCountries());
//
//            // Load Data For All Tables
//            loadSuppliersData();
//            loadCountriesData();
//            loadTestNamesData();
//
//            // Call Tables Listener
//            setupSupplierTableListener();
//            setupTestNamesTableListener();
//            setupCountryTableListener();
//            setupSupplierCountryTableListener();
//            //
//            countryList = CountryDao.getAllCountries();
//            supplierList = SupplierDao.getAllSuppliers();
//            testNamesList = TestNameDao.getAllTestNames();
//
//
//            // Set items to TableViews
//            country_table_view.setItems(countryList);
//            supplier_table_view.setItems(supplierList);
//            test_names_table_view.setItems(testNamesList);
//
//        }
//
//        // Load Suppliers Data
//        private void loadSuppliersData() {
//            supplier_name_colm.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
//            supplier_code_colm.setCellValueFactory(new PropertyValueFactory<>("supplierCode"));
//            supplier_id_colm.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
//            supplier_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
//            supplier_code_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
//            supplier_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
//            Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellFactory = param -> {
//                final TableCell<Supplier, String> cell = new TableCell<Supplier, String>() {
//                    @Override
//                    public void updateItem(String item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                            setText(null);
//                        } else {
//                            final FontIcon deleteIcon = new FontIcon("fas-trash");
//                            deleteIcon.setCursor(Cursor.HAND);
//                            deleteIcon.setIconSize(13);
//                            deleteIcon.setFill(javafx.scene.paint.Color.RED);
//                            Tooltip.install(deleteIcon, new Tooltip("Delete Supplier"));
//
//                            deleteIcon.setOnMouseClicked(event -> {
//                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                alert.setHeaderText("Are you sure you want to delete this supplier?");
//                                alert.setContentText("Delete supplier confirmation");
//                                alert.getButtonTypes().addAll(ButtonType.CANCEL);
//
//                                Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
//                                Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
//                                cancelButton.setText("Cancel");
//                                okButton.setText("OK");
//                                Platform.runLater(cancelButton::requestFocus);
//                                alert.showAndWait().ifPresent(response -> {
//                                    if (response == ButtonType.OK) {
//                                        if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
//                                            try {
//                                                Supplier supplier = supplier_table_view.getSelectionModel().getSelectedItem();
//                                                boolean deleted = SupplierDao.deleteSupplier(supplier.getSupplierId());
//                                                if (deleted) {
//                                                    supplierList = SupplierDao.getAllSuppliers();
//                                                    supplier_table_view.setItems(supplierList);
//                                                    WindowUtils.ALERT("Success", "Supplier deleted successfully", WindowUtils.ALERT_INFORMATION);
//                                                } else {
//                                                    WindowUtils.ALERT("Warning", "Cannot delete supplier. It may be referenced in other tables or an error occurred.", WindowUtils.ALERT_WARNING);
//                                                }
//
//                                            } catch (Exception ex) {
//                                                Logging.logException("ERROR", getClass().getName(), "deleteSupplier", ex);
//                                            }
//
//                                        } else {
//                                            WindowUtils.ALERT("ERR", "Password not correct", WindowUtils.ALERT_WARNING);
//                                        }
//                                    }
//                                });
//                            });
//
//                            HBox manageBtn = new HBox(deleteIcon);
//                            manageBtn.setStyle("-fx-alignment:center");
//                            HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
//                            setGraphic(manageBtn);
//                            setText(null);
//                        }
//                    }
//                };
//                return cell;
//            };
//            supplier_delete_colm.setCellFactory(cellFactory);
//            supplier_table_view.setItems(supplierList);
//        }
//
//        // Setup Supplier Table Listener
//        private void setupSupplierTableListener() {
//            supplier_table_view.setOnMouseClicked(event -> {
//                Supplier selectedSupplier = supplier_table_view.getSelectionModel().getSelectedItem();
//                if (selectedSupplier != null) {
//                    update_supplier_name_textF.setText(selectedSupplier.getSupplierName());
//                    update_supplier_code_textF.setText(selectedSupplier.getSupplierCode());
//                }
//            });
//        }
//
//        // Load Countries Data
//        private void loadCountriesData() {
//            country_name_colm.setCellValueFactory(new PropertyValueFactory<>("countryName"));
//            country_id_colm.setCellValueFactory(new PropertyValueFactory<>("countryId"));
//            country_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
//            country_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
//            Callback<TableColumn<Country, String>, TableCell<Country, String>> cellFactory = param -> {
//                final TableCell<Country, String> cell = new TableCell<Country, String>() {
//                    @Override
//                    public void updateItem(String item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                            setText(null);
//                        } else {
//                            final FontIcon deleteIcon = new FontIcon("fas-trash");
//                            deleteIcon.setCursor(Cursor.HAND);
//                            deleteIcon.setIconSize(13);
//                            deleteIcon.setFill(javafx.scene.paint.Color.RED);
//                            Tooltip.install(deleteIcon, new Tooltip("Delete Country"));
//
//                            deleteIcon.setOnMouseClicked(event -> {
//                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                alert.setHeaderText("Are you sure you want to delete this country?");
//                                alert.setContentText("Delete country confirmation");
//                                alert.getButtonTypes().addAll(ButtonType.CANCEL);
//
//                                Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
//                                Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
//                                cancelButton.setText("Cancel");
//                                okButton.setText("OK");
//                                Platform.runLater(cancelButton::requestFocus);
//                                alert.showAndWait().ifPresent(response -> {
//                                    if (response == ButtonType.OK) {
//                                        if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
//                                            try {
//                                                Country country = country_table_view.getSelectionModel().getSelectedItem();
//                                                CountryDao.deleteCountry(country.getCountryId());
//                                                country_name_textF.clear();
//                                                filter_country_textF.clear();
//                                                update_country_name_textF.clear();
//                                                countryList = CountryDao.getAllCountries();
//                                                country_table_view.setItems(countryList);
//                                                WindowUtils.ALERT("Success", "Country deleted successfully", WindowUtils.ALERT_INFORMATION);
//                                            } catch (Exception ex) {
//                                                Logging.logException("ERROR", getClass().getName(), "deleteCountry", ex);
//                                            }
//                                        } else {
//                                            WindowUtils.ALERT("ERR", "Password not correct", WindowUtils.ALERT_WARNING);
//                                        }
//                                    }
//                                });
//                            });
//
//                            HBox manageBtn = new HBox(deleteIcon);
//                            manageBtn.setStyle("-fx-alignment:center");
//                            HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
//                            setGraphic(manageBtn);
//                            setText(null);
//                        }
//                    }
//                };
//                return cell;
//            };
//            country_delete_colm.setCellFactory(cellFactory);
//            country_table_view.setItems(countryList);
//
//        }
//
//        // Add Country
//        @FXML
//        void add_country(ActionEvent event) {
//            String countryName = country_name_textF.getText().trim();
//            if (countryName.isEmpty()) {
//                WindowUtils.ALERT("ERR", "country_name_empty", WindowUtils.ALERT_ERROR);
//                return;
//            }
//
//            Country country = new Country();
//            country.setCountryName(countryName);
//
//            boolean success = CountryDao.insertCountry(country);
//
//            if (success) {
//                WindowUtils.ALERT("Success", "Country added successfully", WindowUtils.ALERT_INFORMATION);
//                country_name_textF.clear();
//                update_country_name_textF.clear();
//                filter_country_textF.clear();
//                countryList = CountryDao.getAllCountries();
//                country_table_view.setItems(countryList);
//                country_comb.setItems(CountryDao.getAllCountries());
//            } else {
//                String err = CountryDao.lastErrorMessage;
//                if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
//                    WindowUtils.ALERT("Duplicate", "Country name already exists", WindowUtils.ALERT_ERROR);
//                } else {
//                    WindowUtils.ALERT("database_error", "country_add_failed", WindowUtils.ALERT_ERROR);
//                }
//            }
//        }
//
//
//    // Update Country
//    @FXML
//    void update_country(ActionEvent event) {
//        try {
//            Country selectedCountry = country_table_view.getSelectionModel().getSelectedItem();
//            if (selectedCountry == null) {
//                WindowUtils.ALERT("ERR", "No Country selected", WindowUtils.ALERT_ERROR);
//                return;
//            }
//
//            String countryName = update_country_name_textF.getText().trim();
//            if (countryName.isEmpty()) {
//                WindowUtils.ALERT("ERR", "country_name_empty", WindowUtils.ALERT_ERROR);
//                return;
//            }
//
//            selectedCountry.setCountryName(countryName);
//            boolean success = CountryDao.updateCountry(selectedCountry);
//            if (success) {
//                WindowUtils.ALERT("Success", "Country updated successfully", WindowUtils.ALERT_INFORMATION);
//                update_country_name_textF.clear();
//                country_name_textF.clear();
//                filter_country_textF.clear();
//                countryList = CountryDao.getAllCountries();
//                country_table_view.setItems(countryList);
//                country_comb.setItems(CountryDao.getAllCountries());
//            } else {
//                WindowUtils.ALERT("ERR", "country_updated_failed", WindowUtils.ALERT_ERROR);
//            }
//        } catch (Exception ex) {
//            Logging.logException("ERROR", getClass().getName(), "updateCountry", ex);
//        }
//    }
//
//    // Clear Country
//    @FXML
//    void clear_country(ActionEvent event) {
//        filter_country_textF.clear();
//        update_country_name_textF.clear();
//        country_name_textF.clear();
//    }
//
//    // Filter Countries
//    @FXML
//    void filter_country(KeyEvent event) {
//        FilteredList<Country> filteredData = new FilteredList<>(countryList, p -> true);
//        filter_country_textF.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(country -> {
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                String lowerCaseFilter = newValue.toLowerCase();
//                if (country.getCountryName().toLowerCase().contains(lowerCaseFilter)) {
//                    return true;
//                }
//                String id = country.getCountryId() + "";
//                return id.contains(lowerCaseFilter);
//            });
//        });
//        SortedList<Country> sortedData = new SortedList<>(filteredData);
//        sortedData.comparatorProperty().bind(country_table_view.comparatorProperty());
//        country_table_view.setItems(sortedData);
//    }
//
//    // Setup Country Table Listener
//    private void setupCountryTableListener() {
//        country_table_view.setOnMouseClicked(event -> {
//            Country selectedCountry = country_table_view.getSelectionModel().getSelectedItem();
//            if (selectedCountry != null) {
//                update_country_name_textF.setText(selectedCountry.getCountryName());
//            }
//        });
//    }
//
//    // Update Supplier
//    @FXML
//    void update_supplier(ActionEvent event) {
//        try {
//            Supplier selectedSupplier = supplier_table_view.getSelectionModel().getSelectedItem();
//            if (selectedSupplier == null) {
//                WindowUtils.ALERT("ERROR", "No Supplier selected", WindowUtils.ALERT_ERROR);
//                return;
//            }
//
//            String supplierName = update_supplier_name_textF.getText().trim();
//            String supplierCode = update_supplier_code_textF.getText().trim();
//            if (supplierName.isEmpty()) {
//                WindowUtils.ALERT("ERROR", "supplier_name_empty", WindowUtils.ALERT_ERROR);
//                return;
//            }
//
//            selectedSupplier.setSupplierName(supplierName);
//            selectedSupplier.setSupplierCode(supplierCode);
//            boolean success = SupplierDao.updateSupplier(selectedSupplier);
//            if (success) {
//                WindowUtils.ALERT("Success", "Supplier updated successfully", WindowUtils.ALERT_INFORMATION);
//                update_supplier_name_textF.clear();
//                supplier_name_textF.clear();
//                supplier_code_textF.clear();
//                update_supplier_code_textF.clear();
//                filter_supplier_textF.clear();
//                supplierList = SupplierDao.getAllSuppliers();
//                supplier_table_view.setItems(supplierList);
//                supplier_comb.setItems(SupplierDao.getAllSuppliers());
//            } else {
//                WindowUtils.ALERT("ERROR", "supplier_updated_failed", WindowUtils.ALERT_ERROR);
//            }
//        } catch (Exception ex) {
//            Logging.logException("ERROR", getClass().getName(), "updateSupplier", ex);
//        }
//    }
//
//    // Add Supplier
//    @FXML
//    void add_supplier(ActionEvent event) {
//        String supplierName = supplier_name_textF.getText().trim();
//        String supplierCode = supplier_code_textF.getText().trim();
//        if (supplierName.isEmpty()) {
//            WindowUtils.ALERT("ERROR", "supplier_name_empty", WindowUtils.ALERT_ERROR);
//            return;
//        }
//
//        Supplier supplier = new Supplier();
//        supplier.setSupplierName(supplierName);
//        supplier.setSupplierCode(supplierCode);
//
//        boolean success = SupplierDao.insertSupplier(supplier);
//        if (success) {
//            WindowUtils.ALERT("Success", "Supplier added successfully", WindowUtils.ALERT_INFORMATION);
//            supplier_name_textF.clear();
//            update_supplier_name_textF.clear();
//            supplier_code_textF.clear();
//            update_supplier_code_textF.clear();
//            filter_supplier_textF.clear();
//            supplierList = SupplierDao.getAllSuppliers();
//            supplier_table_view.setItems(supplierList);
//            supplier_comb.setItems(SupplierDao.getAllSuppliers());
//        } else {
//            String err = SupplierDao.lastErrorMessage;
//            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
//                WindowUtils.ALERT("Duplicate", "Supplier name already exists", WindowUtils.ALERT_ERROR);
//            } else {
//                WindowUtils.ALERT("database_error", "supplier_add_failed", WindowUtils.ALERT_ERROR);
//            }
//        }
//    }
//
//    // Clear Supplier
//    @FXML
//    void clear_supplier(ActionEvent event) {
//        filter_supplier_textF.clear();
//        update_supplier_name_textF.clear();
//        supplier_name_textF.clear();
//        update_supplier_code_textF.clear();
//        supplier_code_textF.clear();
//    }
//
//    // Clear Supplier Country
//    void helpSupplierCountry() {
//        filter_supplier_cou_textF.clear();
//        // Clear combo box completely
//        supplier_comb.getSelectionModel().clearSelection();
//        supplier_comb.setValue(null);
//        country_comb.getSelectionModel().clearSelection();
//        country_comb.setValue(null);
//    }
//
//    @FXML
//    void clear_supplier_country(ActionEvent event) {
//        helpSupplierCountry();
//    }
//
//    // Filter Suppliers
//    @FXML
//    void filter_supplier(KeyEvent event) {
//        FilteredList<Supplier> filteredData = new FilteredList<>(supplierList, p -> true);
//        filter_supplier_textF.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(supplier -> {
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                String lowerCaseFilter = newValue.toLowerCase();
//                if (supplier.getSupplierName().toLowerCase().contains(lowerCaseFilter)) {
//                    return true;
//                }
//                if (supplier.getSupplierCode().toLowerCase().contains(lowerCaseFilter)) {
//                    return true;
//                }
//                String id = supplier.getSupplierId() + "";
//                return id.contains(lowerCaseFilter);
//            });
//        });
//        SortedList<Supplier> sortedData = new SortedList<>(filteredData);
//        sortedData.comparatorProperty().bind(supplier_table_view.comparatorProperty());
//        supplier_table_view.setItems(sortedData);
//    }
//
//    @FXML
//    void add_supplier_country(ActionEvent event) {
//
//    }
//    // Load Test Names Data
//    private void loadTestNamesData() {
//            test_name_colm.setCellValueFactory(new PropertyValueFactory<>("testName"));
//            test_id_colm.setCellValueFactory(new PropertyValueFactory<>("testNameId"));
//            test_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
//        test_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
//            Callback<TableColumn<TestName, String>, TableCell<TestName, String>> cellFactory =
//                    param -> {
//                final TableCell<TestName, String> cell = new TableCell<TestName, String>() {
//                    @Override
//                    public void updateItem(String item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                            setText(null);
//                        } else {
//                            final FontIcon deleteIcon = new FontIcon("fas-trash");
//                            deleteIcon.setCursor(Cursor.HAND);
//                            deleteIcon.setIconSize(13);
//                            deleteIcon.setFill(javafx.scene.paint.Color.RED);
//                            Tooltip.install(deleteIcon, new Tooltip("Delete Test Name"));
//
//                            deleteIcon.setOnMouseClicked(event -> {
//                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                alert.setHeaderText("Are you sure you want to delete this Test?");
//                                alert.setContentText("Delete Test confirmation");
//                                alert.getButtonTypes().addAll(ButtonType.CANCEL);
//
//                                Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
//                                Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
//                                cancelButton.setText("Cancel");
//                                okButton.setText("OK");
//                                Platform.runLater(cancelButton::requestFocus);
//                                alert.showAndWait().ifPresent(response -> {
//                                    if (response == ButtonType.OK) {
//                                        if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
//                                            try {
//                                                TestName testName = test_names_table_view.getSelectionModel().getSelectedItem();
//                                                TestNameDao.deleteTestName(testName.getTestNameId());
//                                                test_name_textF.clear();
//                                                filter_test_textF.clear();
//                                                update_test_name_textF.clear();
//                                                testNamesList = TestNameDao.getAllTestNames();
//                                                test_names_table_view.setItems(testNamesList);
//                                                WindowUtils.ALERT("Success", "Test deleted successfully", WindowUtils.ALERT_INFORMATION);
//                                            } catch (Exception ex) {
//                                                Logging.logException("ERROR", getClass().getName(), "deleteTest", ex);
//                                            }
//                                        } else {
//                                            WindowUtils.ALERT("ERROR", "Password not correct", WindowUtils.ALERT_WARNING);
//                                        }
//                                    }
//                                });
//                            });
//
//                            HBox manageBtn = new HBox(deleteIcon);
//                            manageBtn.setStyle("-fx-alignment:center");
//                            HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
//                            setGraphic(manageBtn);
//                            setText(null);
//                        }
//                    }
//                };
//                return cell;
//            };
//            test_delete_colm.setCellFactory(cellFactory);
//            test_names_table_view.setItems(testNamesList);
//
//        }
//        // Setup Test Names Table Listener
//        private void setupTestNamesTableListener() {
//            test_names_table_view.setOnMouseClicked(event -> {
//                TestName selectTestName = test_names_table_view.getSelectionModel().getSelectedItem();
//                if (selectTestName != null) {
//                    update_test_name_textF.setText(selectTestName.getTestName());
//                }
//            });
//        }
//
//        @FXML
//        void add_test(ActionEvent event) {
//            String testName = test_name_textF.getText().trim();
//            if (testName.isEmpty()) {
//                WindowUtils.ALERT("ERROR", "test_name_empty", WindowUtils.ALERT_ERROR);
//                return;
//            }
//
//            TestName  tn  = new TestName();
//            tn.setTestName(testName);
//
//            int  success = TestNameDao.insertTestName(tn);
//
//            if (success != 0) {
//                WindowUtils.ALERT("Success", "Test added successfully", WindowUtils.ALERT_INFORMATION);
//                test_name_textF.clear();
//                update_test_name_textF.clear();
//                filter_test_textF.clear();
//                testNamesList = TestNameDao.getAllTestNames();
//                test_names_table_view.setItems(testNamesList);
//            } else {
//                String err = TestNameDao.lastErrorMessage;
//                if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
//                    WindowUtils.ALERT("Duplicate", "Test name already exists", WindowUtils.ALERT_ERROR);
//                } else {
//                    WindowUtils.ALERT("database_error", "test_add_failed", WindowUtils.ALERT_ERROR);
//                }
//            }
//        }
//
//
//        // Update Test Name
//        @FXML
//        void update_test(ActionEvent event) {
//            try {
//                TestName selectTest = test_names_table_view.getSelectionModel().getSelectedItem();
//                if (selectTest == null) {
//                    WindowUtils.ALERT("ERR", "No Test selected", WindowUtils.ALERT_ERROR);
//                    return;
//                }
//
//                String testName = update_test_name_textF.getText().trim();
//                if (testName.isEmpty()) {
//                    WindowUtils.ALERT("ERR", "test_name_empty", WindowUtils.ALERT_ERROR);
//                    return;
//                }
//
//                selectTest.setTestName(testName);
//                boolean success = TestNameDao.updateTestName(selectTest);
//                if (success) {
//                    WindowUtils.ALERT("Success", "Test updated successfully", WindowUtils.ALERT_INFORMATION);
//                    update_test_name_textF.clear();
//                    test_name_textF.clear();
//                    filter_test_textF.clear();
//                    testNamesList = TestNameDao.getAllTestNames();
//                    test_names_table_view.setItems(testNamesList);
//                } else {
//                    WindowUtils.ALERT("ERROR", "test_updated_failed", WindowUtils.ALERT_ERROR);
//                }
//            } catch (Exception ex) {
//                Logging.logException("ERROR", getClass().getName(), "updateTestName", ex);
//            }
//        }
//
//        // Clear Test Name
//        @FXML
//        void clear_test(ActionEvent event) {
//            filter_test_textF.clear();
//            update_test_name_textF.clear();
//            test_name_textF.clear();
//        }
//
//        // Filter Test Names
//        @FXML
//        void filter_test_names(KeyEvent event) {
//            FilteredList<TestName> filteredData = new FilteredList<>(testNamesList, p -> true);
//            filter_test_textF.textProperty().addListener((observable, oldValue, newValue) -> {
//                filteredData.setPredicate(testName -> {
//                    if (newValue == null || newValue.isEmpty()) {
//                        return true;
//                    }
//                    String lowerCaseFilter = newValue.toLowerCase();
//                    if (testName.getTestName().toLowerCase().contains(lowerCaseFilter)) {
//                        return true;
//                    }
//                    String id = testName.getTestNameId() + "";
//                    return id.contains(lowerCaseFilter);
//                });
//            });
//            SortedList<TestName> sortedData = new SortedList<>(filteredData);
//            sortedData.comparatorProperty().bind(test_names_table_view.comparatorProperty());
//            test_names_table_view.setItems(sortedData);
//        }
//
//        // Setup Supplier Country Table Listener
//        private void setupSupplierCountryTableListener() {
//            supplier_country_table_view.setOnMouseClicked(event -> {
//                SupplierCountry selectedSupplierCountry = supplier_country_table_view.getSelectionModel().getSelectedItem();
//                if (selectedSupplierCountry != null) {
//                    supplier_comb.getSelectionModel().select(
//                            supplierList.stream()
//                                    .filter(s -> s.getSupplierId() == selectedSupplierCountry.getSupplierId())
//                                    .findFirst()
//                                    .orElse(null)
//                    );
//                }
//                if (selectedSupplierCountry != null) {
//                    country_comb.getSelectionModel().select(
//                            countryList.stream()
//                                    .filter(s -> s.getCountryId() == selectedSupplierCountry.getCountryId())
//                                    .findFirst()
//                                    .orElse(null)
//                    );
//                }
//            });
//        }
//
//    @FXML
//    void filter_supplier_country(KeyEvent event) {
//
//    }
//
//    @FXML
//    void update_supplier_country(ActionEvent event) {
//
//    }
//
//
//    }
//

package com.etc.raw_materials_app.controllers;

import com.etc.raw_materials_app.dao.CountryDao;
import com.etc.raw_materials_app.dao.SupplierCountryDao;
import com.etc.raw_materials_app.dao.TestNameDao;
import com.etc.raw_materials_app.dao.SupplierDao;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.*;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class PrepareDataController implements Initializable {
    @FXML
    private Button add_country_btn;
    @FXML
    private Button add_supplier_btn;
    @FXML
    private Button add_supplier_country_btn;
    @FXML
    private Button add_test_btn;
    @FXML
    private Button clear_country_btn;
    @FXML
    private Button clear_supplier_btn;
    @FXML
    private Button clear_supplier_cou_btn;
    @FXML
    private Button clear_test_btn;
    @FXML
    private ComboBox<Country> country_comb;
    @FXML
    private TableColumn<Country, String> country_delete_colm;
    @FXML
    private TableColumn<Country, String> country_id_colm;
    @FXML
    private TableColumn<Country, String> country_name_colm;
    @FXML
    private TextField country_name_textF;
    @FXML
    private TableView<Country> country_table_view;
    @FXML
    private TextField filter_country_textF;
    @FXML
    private TextField filter_supplier_cou_textF;
    @FXML
    private TextField filter_supplier_textF;
    @FXML
    private TextField filter_test_textF;
    @FXML
    private ComboBox<Supplier> supplier_comb;
    @FXML
    private TableColumn<SupplierCountry, String> supplier_cou_delete_colm;
    @FXML
    private TableColumn<SupplierCountry, String> supplier_cou_name_colm;
    @FXML
    private TableView<SupplierCountry> supplier_country_table_view;
    @FXML
    private TableColumn<Supplier, String> supplier_delete_colm;
    @FXML
    private TableColumn<Supplier, String> supplier_id_colm;
    @FXML private TableColumn<Supplier, String> supplier_code_colm;
    @FXML private TableColumn<Supplier, String> supplier_name_colm;
    @FXML private TableColumn<SupplierCountry, String> supplier_name_in_sup_country_colm;
    @FXML private TextField supplier_name_textF;
    @FXML private TextField supplier_code_textF;
    @FXML private TextField update_supplier_code_textF;
    @FXML
    private TableView<Supplier> supplier_table_view;
    @FXML
    private TableColumn<TestName, String> test_delete_colm;
    @FXML
    private TableColumn<TestName, String> test_id_colm;
    @FXML
    private TableColumn<TestName, String> test_name_colm;
    @FXML
    private TextField test_name_textF;
    @FXML
    private TableView<TestName> test_names_table_view;
    @FXML
    private Button update_country_btn;
    @FXML
    private TextField update_country_name_textF;
    @FXML
    private Button update_supplier_btn;
    @FXML
    private Button update_supplier_cou_btn;
    @FXML
    private TextField update_supplier_name_textF;
    @FXML
    private Button update_test_btn;
    @FXML
    private TextField update_test_name_textF;
    ObservableList<Supplier> supplierList;
    ObservableList<Country> countryList;
    ObservableList<TestName> testNamesList;
    ObservableList<SupplierCountry> supplierCountryList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set ComboBox
        supplier_comb.setItems(SupplierDao.getAllSuppliers());
        country_comb.setItems(CountryDao.getAllCountries());

        // Load Data For All Tables
        loadSuppliersData();
        loadCountriesData();
        loadTestNamesData();
        loadSupplierCountryData();

        // Call Tables Listener
        setupSupplierTableListener();
        setupTestNamesTableListener();
        setupCountryTableListener();
        setupSupplierCountryTableListener();

        countryList = CountryDao.getAllCountries();
        supplierList = SupplierDao.getAllSuppliers();
        testNamesList = TestNameDao.getAllTestNames();
        supplierCountryList = SupplierCountryDao.getAllSupplierCountries();

        // Set items to TableViews
        country_table_view.setItems(countryList);
        supplier_table_view.setItems(supplierList);
        test_names_table_view.setItems(testNamesList);
        supplier_country_table_view.setItems(supplierCountryList);
    }

    // Load Suppliers Data
    private void loadSuppliersData() {
        supplier_name_colm.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        supplier_code_colm.setCellValueFactory(new PropertyValueFactory<>("supplierCode"));
        supplier_id_colm.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        supplier_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        supplier_code_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        supplier_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellFactory = param -> {
            final TableCell<Supplier, String> cell = new TableCell<Supplier, String>() {
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
                        Tooltip.install(deleteIcon, new Tooltip("Delete Supplier"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this supplier?");
                            alert.setContentText("Delete supplier confirmation");
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
                                            Supplier supplier = supplier_table_view.getSelectionModel().getSelectedItem();
                                            boolean deleted = SupplierDao.deleteSupplier(supplier.getSupplierId());
                                            if (deleted) {
                                                supplierList = SupplierDao.getAllSuppliers();
                                                supplier_table_view.setItems(supplierList);
                                                supplier_comb.setItems(SupplierDao.getAllSuppliers());
                                                WindowUtils.ALERT("Success", "Supplier deleted successfully", WindowUtils.ALERT_INFORMATION);
                                            } else {
                                                WindowUtils.ALERT("Warning", "Cannot delete supplier. It may be referenced in other tables or an error occurred.", WindowUtils.ALERT_WARNING);
                                            }
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteSupplier", ex);
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
        supplier_delete_colm.setCellFactory(cellFactory);
        supplier_table_view.setItems(supplierList);
    }

    // Setup Supplier Table Listener
    private void setupSupplierTableListener() {
        supplier_table_view.setOnMouseClicked(event -> {
            Supplier selectedSupplier = supplier_table_view.getSelectionModel().getSelectedItem();
            if (selectedSupplier != null) {
                update_supplier_name_textF.setText(selectedSupplier.getSupplierName());
                update_supplier_code_textF.setText(selectedSupplier.getSupplierCode());
            }
        });
    }

    // Load Countries Data
    private void loadCountriesData() {
        country_name_colm.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        country_id_colm.setCellValueFactory(new PropertyValueFactory<>("countryId"));
        country_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        country_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Country, String>, TableCell<Country, String>> cellFactory = param -> {
            final TableCell<Country, String> cell = new TableCell<Country, String>() {
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
                        Tooltip.install(deleteIcon, new Tooltip("Delete Country"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this country?");
                            alert.setContentText("Delete country confirmation");
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
                                            Country country = country_table_view.getSelectionModel().getSelectedItem();
                                            CountryDao.deleteCountry(country.getCountryId());
                                            country_name_textF.clear();
                                            filter_country_textF.clear();
                                            update_country_name_textF.clear();
                                            countryList = CountryDao.getAllCountries();
                                            country_table_view.setItems(countryList);
                                            country_comb.setItems(CountryDao.getAllCountries());
                                            WindowUtils.ALERT("Success", "Country deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteCountry", ex);
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
        country_delete_colm.setCellFactory(cellFactory);
        country_table_view.setItems(countryList);
    }

    // Load Supplier Country Data
    private void loadSupplierCountryData() {
        supplier_name_in_sup_country_colm.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        supplier_cou_name_colm.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        supplier_name_in_sup_country_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        supplier_cou_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<SupplierCountry, String>, TableCell<SupplierCountry, String>> cellFactory = param -> {
            final TableCell<SupplierCountry, String> cell = new TableCell<SupplierCountry, String>() {
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
                        Tooltip.install(deleteIcon, new Tooltip("Delete Supplier-Country Mapping"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this supplier-country mapping?");
                            alert.setContentText("Delete supplier-country confirmation");
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
                                            SupplierCountry sc = supplier_country_table_view.getSelectionModel().getSelectedItem();
                                            boolean deleted = SupplierCountryDao.deleteSupplierCountry(sc.getSupplierId(), sc.getCountryId());
                                            if (deleted) {
                                                supplierCountryList = SupplierCountryDao.getAllSupplierCountries();
                                                supplier_country_table_view.setItems(supplierCountryList);
                                                helpSupplierCountry();
                                                WindowUtils.ALERT("Success", "Supplier-Country mapping deleted successfully", WindowUtils.ALERT_INFORMATION);
                                            } else {
                                                WindowUtils.ALERT("Warning", "Cannot delete supplier-country mapping.", WindowUtils.ALERT_WARNING);
                                            }
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteSupplierCountry", ex);
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
        supplier_cou_delete_colm.setCellFactory(cellFactory);
        supplier_country_table_view.setItems(supplierCountryList);
    }

    // Add Country
    @FXML
    void add_country(ActionEvent event) {
        String countryName = country_name_textF.getText().trim();
        if (countryName.isEmpty()) {
            WindowUtils.ALERT("ERROR", "country_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Country country = new Country();
        country.setCountryName(countryName);

        boolean success = CountryDao.insertCountry(country);

        if (success) {
            WindowUtils.ALERT("Success", "Country added successfully", WindowUtils.ALERT_INFORMATION);
            country_name_textF.clear();
            update_country_name_textF.clear();
            filter_country_textF.clear();
            countryList = CountryDao.getAllCountries();
            country_table_view.setItems(countryList);
            country_comb.setItems(CountryDao.getAllCountries());
        } else {
            String err = CountryDao.lastErrorMessage;
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "Country name already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("database_error", "country_add_failed", WindowUtils.ALERT_ERROR);
            }
        }
    }

    // Update Country
    @FXML
    void update_country(ActionEvent event) {
        try {
            Country selectedCountry = country_table_view.getSelectionModel().getSelectedItem();
            if (selectedCountry == null) {
                WindowUtils.ALERT("ERROR", "No Country selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String countryName = update_country_name_textF.getText().trim();
            if (countryName.isEmpty()) {
                WindowUtils.ALERT("ERROR", "country_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedCountry.setCountryName(countryName);
            boolean success = CountryDao.updateCountry(selectedCountry);
            if (success) {
                WindowUtils.ALERT("Success", "Country updated successfully", WindowUtils.ALERT_INFORMATION);
                update_country_name_textF.clear();
                country_name_textF.clear();
                filter_country_textF.clear();
                countryList = CountryDao.getAllCountries();
                country_table_view.setItems(countryList);
                country_comb.setItems(CountryDao.getAllCountries());
                supplierCountryList = SupplierCountryDao.getAllSupplierCountries();
                supplier_country_table_view.setItems(supplierCountryList);
            } else {
                WindowUtils.ALERT("ERROR", "country_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateCountry", ex);
        }
    }

    // Clear Country
    @FXML
    void clear_country(ActionEvent event) {
        filter_country_textF.clear();
        update_country_name_textF.clear();
        country_name_textF.clear();
    }

    // Filter Countries
    @FXML
    void filter_country(KeyEvent event) {
        FilteredList<Country> filteredData = new FilteredList<>(countryList, p -> true);
        filter_country_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(country -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (country.getCountryName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = country.getCountryId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Country> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(country_table_view.comparatorProperty());
        country_table_view.setItems(sortedData);
    }

    // Setup Country Table Listener
    private void setupCountryTableListener() {
        country_table_view.setOnMouseClicked(event -> {
            Country selectedCountry = country_table_view.getSelectionModel().getSelectedItem();
            if (selectedCountry != null) {
                update_country_name_textF.setText(selectedCountry.getCountryName());
            }
        });
    }

    // Update Supplier
    @FXML
    void update_supplier(ActionEvent event) {
        try {
            Supplier selectedSupplier = supplier_table_view.getSelectionModel().getSelectedItem();
            if (selectedSupplier == null) {
                WindowUtils.ALERT("ERROR", "No Supplier selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String supplierName = update_supplier_name_textF.getText().trim();
            String supplierCode = update_supplier_code_textF.getText().trim();
            if (supplierName.isEmpty()) {
                WindowUtils.ALERT("ERROR", "supplier_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedSupplier.setSupplierName(supplierName);
            selectedSupplier.setSupplierCode(supplierCode);
            boolean success = SupplierDao.updateSupplier(selectedSupplier);
            if (success) {
                WindowUtils.ALERT("Success", "Supplier updated successfully", WindowUtils.ALERT_INFORMATION);
                update_supplier_name_textF.clear();
                supplier_name_textF.clear();
                supplier_code_textF.clear();
                update_supplier_code_textF.clear();
                filter_supplier_textF.clear();
                supplierList = SupplierDao.getAllSuppliers();
                supplier_table_view.setItems(supplierList);
                supplier_comb.setItems(SupplierDao.getAllSuppliers());
                supplierCountryList = SupplierCountryDao.getAllSupplierCountries();
                supplier_country_table_view.setItems(supplierCountryList);

            } else {
                WindowUtils.ALERT("ERROR", "supplier_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateSupplier", ex);
        }
    }

    // Add Supplier
    @FXML
    void add_supplier(ActionEvent event) {
        String supplierName = supplier_name_textF.getText().trim();
        String supplierCode = supplier_code_textF.getText().trim();
        if (supplierName.isEmpty()) {
            WindowUtils.ALERT("ERROR", "supplier_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Supplier supplier = new Supplier();
        supplier.setSupplierName(supplierName);
        supplier.setSupplierCode(supplierCode);

        boolean success = SupplierDao.insertSupplier(supplier);
        if (success) {
            WindowUtils.ALERT("Success", "Supplier added successfully", WindowUtils.ALERT_INFORMATION);
            supplier_name_textF.clear();
            update_supplier_name_textF.clear();
            supplier_code_textF.clear();
            update_supplier_code_textF.clear();
            filter_supplier_textF.clear();
            supplierList = SupplierDao.getAllSuppliers();
            supplier_table_view.setItems(supplierList);
            supplier_comb.setItems(SupplierDao.getAllSuppliers());
        } else {
            String err = SupplierDao.lastErrorMessage;
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "Supplier name already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("database_error", "supplier_add_failed", WindowUtils.ALERT_ERROR);
            }
        }
    }

    // Clear Supplier
    @FXML
    void clear_supplier(ActionEvent event) {
        filter_supplier_textF.clear();
        update_supplier_name_textF.clear();
        supplier_name_textF.clear();
        update_supplier_code_textF.clear();
        supplier_code_textF.clear();
    }

    // Clear Supplier Country
    void helpSupplierCountry() {
        filter_supplier_cou_textF.clear();
        supplier_comb.getSelectionModel().clearSelection();
        supplier_comb.setValue(null);
        country_comb.getSelectionModel().clearSelection();
        country_comb.setValue(null);
    }

    @FXML
    void clear_supplier_country(ActionEvent event) {
        helpSupplierCountry();
    }

    // Filter Suppliers
    @FXML
    void filter_supplier(KeyEvent event) {
        FilteredList<Supplier> filteredData = new FilteredList<>(supplierList, p -> true);
        filter_supplier_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplier -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplier.getSupplierName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (supplier.getSupplierCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = supplier.getSupplierId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Supplier> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(supplier_table_view.comparatorProperty());
        supplier_table_view.setItems(sortedData);
    }

    // Add Supplier Country
    @FXML
    void add_supplier_country(ActionEvent event) {
        Supplier selectedSupplier = supplier_comb.getSelectionModel().getSelectedItem();
        Country selectedCountry = country_comb.getSelectionModel().getSelectedItem();

        if (selectedSupplier == null || selectedCountry == null) {
            WindowUtils.ALERT("ERROR", "Please select both a supplier and a country", WindowUtils.ALERT_ERROR);
            return;
        }

        SupplierCountry supplierCountry = new SupplierCountry();
        supplierCountry.setSupplierId(selectedSupplier.getSupplierId());
        supplierCountry.setCountryId(selectedCountry.getCountryId());
        supplierCountry.setSupplierName(selectedSupplier.getSupplierName());
        supplierCountry.setCountryName(selectedCountry.getCountryName());

        boolean success = SupplierCountryDao.insertSupplierCountry(supplierCountry);
        if (success) {
            WindowUtils.ALERT("Success", "Supplier-Country mapping added successfully", WindowUtils.ALERT_INFORMATION);
            helpSupplierCountry();
            supplierCountryList = SupplierCountryDao.getAllSupplierCountries();
            supplier_country_table_view.setItems(supplierCountryList);
        } else {
            String err = SupplierCountryDao.lastErrorMessage;
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "Supplier-Country mapping already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("database_error", "supplier_country_add_failed", WindowUtils.ALERT_ERROR);
            }
        }
    }

    // Filter Supplier Country
    @FXML
    void filter_supplier_country(KeyEvent event) {
        FilteredList<SupplierCountry> filteredData = new FilteredList<>(supplierCountryList, p -> true);
        filter_supplier_cou_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplierCountry -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplierCountry.getSupplierName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (supplierCountry.getCountryName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String supplierId = supplierCountry.getSupplierId() + "";
                String countryId = supplierCountry.getCountryId() + "";
                return supplierId.contains(lowerCaseFilter) || countryId.contains(lowerCaseFilter);
            });
        });
        SortedList<SupplierCountry> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(supplier_country_table_view.comparatorProperty());
        supplier_country_table_view.setItems(sortedData);
    }

    // Update Supplier Country
    @FXML
    void update_supplier_country(ActionEvent event) {
        try {
            SupplierCountry selectedSupplierCountry = supplier_country_table_view.getSelectionModel().getSelectedItem();
            if (selectedSupplierCountry == null) {
                WindowUtils.ALERT("ERROR", "No Supplier-Country mapping selected", WindowUtils.ALERT_ERROR);
                return;
            }

            Supplier selectedSupplier = supplier_comb.getSelectionModel().getSelectedItem();
            Country selectedCountry = country_comb.getSelectionModel().getSelectedItem();

            if (selectedSupplier == null || selectedCountry == null) {
                WindowUtils.ALERT("ERROR", "Please select both a supplier and a country", WindowUtils.ALERT_ERROR);
                return;
            }

            // Delete the old mapping
            boolean deleted = SupplierCountryDao.deleteSupplierCountry(selectedSupplierCountry.getSupplierId(), selectedSupplierCountry.getCountryId());
            if (!deleted) {
                WindowUtils.ALERT("ERROR", "Failed to update supplier-country mapping", WindowUtils.ALERT_ERROR);
                return;
            }

            // Insert the new mapping
            SupplierCountry newSupplierCountry = new SupplierCountry();
            newSupplierCountry.setSupplierId(selectedSupplier.getSupplierId());
            newSupplierCountry.setCountryId(selectedCountry.getCountryId());
            newSupplierCountry.setSupplierName(selectedSupplier.getSupplierName());
            newSupplierCountry.setCountryName(selectedCountry.getCountryName());

            boolean success = SupplierCountryDao.insertSupplierCountry(newSupplierCountry);
            if (success) {
                WindowUtils.ALERT("Success", "Supplier-Country mapping updated successfully", WindowUtils.ALERT_INFORMATION);
                helpSupplierCountry();
                supplierCountryList = SupplierCountryDao.getAllSupplierCountries();
                supplier_country_table_view.setItems(supplierCountryList);
            } else {
                WindowUtils.ALERT("ERROR", "supplier_country_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateSupplierCountry", ex);
        }
    }

    // Load Test Names Data
    private void loadTestNamesData() {
        test_name_colm.setCellValueFactory(new PropertyValueFactory<>("testName"));
        test_id_colm.setCellValueFactory(new PropertyValueFactory<>("testNameId"));
        test_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        test_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<TestName, String>, TableCell<TestName, String>> cellFactory =
                param -> {
                    final TableCell<TestName, String> cell = new TableCell<TestName, String>() {
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
                                Tooltip.install(deleteIcon, new Tooltip("Delete Test Name"));

                                deleteIcon.setOnMouseClicked(event -> {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText("Are you sure you want to delete this Test?");
                                    alert.setContentText("Delete Test confirmation");
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
                                                    TestName testName = test_names_table_view.getSelectionModel().getSelectedItem();
                                                    TestNameDao.deleteTestName(testName.getTestNameId());
                                                    test_name_textF.clear();
                                                    filter_test_textF.clear();
                                                    update_test_name_textF.clear();
                                                    testNamesList = TestNameDao.getAllTestNames();
                                                    test_names_table_view.setItems(testNamesList);
                                                    WindowUtils.ALERT("Success", "Test deleted successfully", WindowUtils.ALERT_INFORMATION);
                                                } catch (Exception ex) {
                                                    Logging.logException("ERROR", getClass().getName(), "deleteTest", ex);
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
        test_delete_colm.setCellFactory(cellFactory);
        test_names_table_view.setItems(testNamesList);
    }

    // Setup Test Names Table Listener
    private void setupTestNamesTableListener() {
        test_names_table_view.setOnMouseClicked(event -> {
            TestName selectTestName = test_names_table_view.getSelectionModel().getSelectedItem();
            if (selectTestName != null) {
                update_test_name_textF.setText(selectTestName.getTestName());
            }
        });
    }

    // Add Test
    @FXML
    void add_test(ActionEvent event) {
        String testName = test_name_textF.getText().trim();
        if (testName.isEmpty()) {
            WindowUtils.ALERT("ERROR", "test_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        TestName tn = new TestName();
        tn.setTestName(testName);

        int success = TestNameDao.insertTestName(tn);

        if (success != 0) {
            WindowUtils.ALERT("Success", "Test added successfully", WindowUtils.ALERT_INFORMATION);
            test_name_textF.clear();
            update_test_name_textF.clear();
            filter_test_textF.clear();
            testNamesList = TestNameDao.getAllTestNames();
            test_names_table_view.setItems(testNamesList);
        } else {
            String err = TestNameDao.lastErrorMessage;
            if (err != null && (err.toLowerCase().contains("duplicate") || err.contains("UNIQUE"))) {
                WindowUtils.ALERT("Duplicate", "Test name already exists", WindowUtils.ALERT_ERROR);
            } else {
                WindowUtils.ALERT("database_error", "test_add_failed", WindowUtils.ALERT_ERROR);
            }
        }
    }

    // Update Test Name
    @FXML
    void update_test(ActionEvent event) {
        try {
            TestName selectTest = test_names_table_view.getSelectionModel().getSelectedItem();
            if (selectTest == null) {
                WindowUtils.ALERT("ERROR", "No Test selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String testName = update_test_name_textF.getText().trim();
            if (testName.isEmpty()) {
                WindowUtils.ALERT("ERROR", "test_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectTest.setTestName(testName);
            boolean success = TestNameDao.updateTestName(selectTest);
            if (success) {
                WindowUtils.ALERT("Success", "Test updated successfully", WindowUtils.ALERT_INFORMATION);
                update_test_name_textF.clear();
                test_name_textF.clear();
                filter_test_textF.clear();
                testNamesList = TestNameDao.getAllTestNames();
                test_names_table_view.setItems(testNamesList);
            } else {
                WindowUtils.ALERT("ERROR", "test_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateTestName", ex);
        }
    }

    // Clear Test Name
    @FXML
    void clear_test(ActionEvent event) {
        filter_test_textF.clear();
        update_test_name_textF.clear();
        test_name_textF.clear();
    }

    // Filter Test Names
    @FXML
    void filter_test_names(KeyEvent event) {
        FilteredList<TestName> filteredData = new FilteredList<>(testNamesList, p -> true);
        filter_test_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(testName -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (testName.getTestName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = testName.getTestNameId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<TestName> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(test_names_table_view.comparatorProperty());
        test_names_table_view.setItems(sortedData);
    }

    // Setup Supplier Country Table Listener
    private void setupSupplierCountryTableListener() {
        supplier_country_table_view.setOnMouseClicked(event -> {
            SupplierCountry selectedSupplierCountry = supplier_country_table_view.getSelectionModel().getSelectedItem();
            if (selectedSupplierCountry != null) {
                supplier_comb.getSelectionModel().select(
                        supplierList.stream()
                                .filter(s -> s.getSupplierId() == selectedSupplierCountry.getSupplierId())
                                .findFirst()
                                .orElse(null)
                );
                country_comb.getSelectionModel().select(
                        countryList.stream()
                                .filter(s -> s.getCountryId() == selectedSupplierCountry.getCountryId())
                                .findFirst()
                                .orElse(null)
                );
            }
        });
    }
}
