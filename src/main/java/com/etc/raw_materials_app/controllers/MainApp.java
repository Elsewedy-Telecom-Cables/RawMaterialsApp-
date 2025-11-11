package com.etc.raw_materials_app.controllers;
import com.etc.raw_materials_app.db.DbConnect;
import com.etc.raw_materials_app.logging.Logging;
import com.etc.raw_materials_app.services.LoggingSetting;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        try {

            Connection connection = DbConnect.getConnect();
            if (connection == null) {
                System.out.println("Connection failed");
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/screens/Login.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Login Form");
                stage.setScene(scene);
                stage.show();
                stage.setResizable(false);
                LoggingSetting.startJarUpdateWatcher();
                LoggingSetting.initJarWatcher();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logException("ERROR", this.getClass().getName(), "start", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}



