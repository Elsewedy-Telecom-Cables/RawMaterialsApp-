package com.etc.raw_materials_app.controllers;

import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.models.UserContext;
import com.etc.raw_materials_app.services.ShiftManager;
import com.etc.raw_materials_app.services.WindowUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.etc.raw_materials_app.services.WindowUtils.*;

public class MainController implements Initializable {

    @FXML
    private Label date_lbl;

    @FXML
    private Label shift_label;

    @FXML
    private Label welcome_lbl;

    @FXML
    private Button users_btn;
    @FXML
    private ImageView logo_ImageView;
    @FXML
    private ImageView icon01_view;
    @FXML
    private ImageView icon02_view;
    @FXML
    private ImageView icon03_view;
    @FXML
    private Button prepare_data_btn;
    @FXML
    private Button addPdsData_btn;
    @FXML
    private Button exportPds_btn;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // Set focus to welcome label
        Platform.runLater(() -> welcome_lbl.requestFocus());


        // Load and set company logo
        Image img = new Image(Objects.requireNonNull(MainController.class.getResourceAsStream("/images/company_logo.png")));
        logo_ImageView.setImage(img);
        Image img2 = new Image(Objects.requireNonNull(MainController.class.getResourceAsStream("/images/icon01.png")));
        icon01_view.setImage(img2);
        Image img3 = new Image(Objects.requireNonNull(MainController.class.getResourceAsStream("/images/icon02.png")));
        icon02_view.setImage(img3);
        Image img4 = new Image(Objects.requireNonNull(MainController.class.getResourceAsStream("/images/icon03.png")));
        icon03_view.setImage(img4);
        // Set welcome message with current user's full name
        String msg = ("Welcome : " + UserContext.getCurrentUser().getFullName());
        welcome_lbl.setText(msg);

        // Set current date and time
        java.util.Date date = new java.util.Date();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy  hh:mm a");
        date_lbl.setText(dateFormat2.format(date) + " ");

        //set Curser
        users_btn.setCursor(Cursor.HAND);
        prepare_data_btn.setCursor(Cursor.HAND);


    }


    @FXML
    void openViewUsers(ActionEvent event) {
        // set Permissions
        try {
            // Super Admin and Department Manager
            int role = UserContext.getCurrentUser().getRole();
            if (role == 2) {
                CLOSE(event);
                OPEN_VIEW_USERS_PAGE();
            } else {
                WindowUtils.ALERT("Warning", "You are not authorized to access this page.", WindowUtils.ALERT_WARNING);
                return;
            }
        }catch (Exception ex){
            Logging.logException("ERROR", this.getClass().getName(), "openPrepareData Permission", ex);
        }

    }

        @FXML
        void openPrepareData(ActionEvent event) {
                    CLOSE(event);
                    OPEN_PREPARE_DATA_PAGE();
        }
    @FXML
    void openAddPdsData(ActionEvent event) {
        CLOSE(event);
        OPEN_ADD_UPDATE_PDS_DATA();
    }
    @FXML
    void openExportPds(ActionEvent event) {
       WindowUtils.ALERT("Info", "Work Still Progress️", ALERT_INFORMATION);
         CLOSE(event);
         OPEN_EXPORT_PDS_DATA();

    }






}
