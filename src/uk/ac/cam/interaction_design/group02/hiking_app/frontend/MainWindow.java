package uk.ac.cam.interaction_design.group02.hiking_app.frontend;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

import uk.ac.cam.interaction_design.group02.hiking_app.backend.*;

public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    private BorderPane mainContainer;

    @FXML
    private Button mapButton;

    @FXML
    private Button addButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button settingsButton;

    private MapControl mapControl;
    private HomeControl homeControl;
    private SettingsControl settingsControl;

    public MainWindow() throws IOException {
        homeControl = new HomeControl();
        settingsControl = new SettingsControl(getHostServices());
        mapControl = new MapControl();
    }

    /* TODO: Use set property instead */
    private void markSet(Button b) {
        b.setStyle("-fx-background-color: #246249;" +
                "-fx-text-fill: #78a895");
    }

    private void markUnset(Button b) {
        b.setStyle("-fx-background-color: #286d51;" +
                "-fx-text-fill: #c5d9d1");
    }

    private void markAllUnset() {
        markUnset(settingsButton);
        markUnset(addButton);
        markUnset(mapButton);
        markUnset(homeButton);
    }

    @FXML
    private void handleMapButtonAction(ActionEvent e) {
        mapControl.refresh();
        mainContainer.setCenter(mapControl);
        markAllUnset();
        markSet(mapButton);
        markSet(addButton);
    }

    @FXML
    private void handleHomeButtonAction(ActionEvent e) {
        focusHomeButton();
    }

    private void focusHomeButton() {
        homeControl.refresh();
        mainContainer.setCenter(homeControl);
        markAllUnset();
        markSet(homeButton);
    }

    @FXML
    private void handleSettingsButtonAction(ActionEvent e) {
        mainContainer.setCenter(settingsControl);
        markAllUnset();
        markSet(settingsButton);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));

        Scene scene = new Scene(root, 300, 500);

        primaryStage.setTitle("Hiking Weather Application");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(200);

        //Make sure the settings get dumped to disk when the application is closed
        primaryStage.setOnCloseRequest((event) -> {
            try {
                AppSettings.getInstance().saveToDisk();
            } catch (IOException exception) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Settings not saved!");
                alert.setContentText("Settings file failed to save.");

                alert.showAndWait();
            }
        });

        primaryStage.show();
    }

    /**
     * Method run on FXML load to initialise (ie. default to home page)
     */
    @FXML
    public void initialize() {
        focusHomeButton();
    }
}
