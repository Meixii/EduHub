/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author lugtu
 */
public class SignInWindowController implements Initializable {

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    @FXML
    private TextField si_password;
    private double x = 0;
    private double y = 0;
    @FXML
    private ImageView bgGradient;
    @FXML
    private Pane adminLogin;
    @FXML
    private TextField si_userID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Create a PathTransition for the ImageView along the circular path
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2.5), bgGradient);
        scaleTransition.setByX(0.2); // Scale factor in the x-direction
        scaleTransition.setByY(0.2); // Scale factor in the y-direction
        scaleTransition.setAutoReverse(true); // Auto reverse the animation

        // Set the cycle count to indefinite for the continuous animation
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);

        // Play the animation
        scaleTransition.play();

    }

    @FXML
    private void closeButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout");
        alert.setContentText("Do you want to save before exiting?");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("You successfully logged out");
            stage.close();
        }
    }

    @FXML
    private void minimizeButton(ActionEvent event) {
        Stage stage = (Stage) adminLogin.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void getBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/signInWindow.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            ((Node) (event.getSource())).getScene().getWindow().hide();
            stage.setWidth(785);
            stage.setHeight(514);

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double centerX = screenBounds.getMinX() + screenBounds.getWidth() / 2.0;
            double centerY = screenBounds.getMinY() + screenBounds.getHeight() / 2.0;
            stage.setX(centerX - 392.5);
            stage.setY(centerY - 257);

            Scene scene = new Scene(root, 785, 514);

            stage.setScene(scene);
            stage.show();

            root.setOnMousePressed((mouseEvent) -> {
                x = mouseEvent.getSceneX();
                y = mouseEvent.getSceneY();
            });

            root.setOnMouseDragged((mouseEvent) -> {
                stage.setX(mouseEvent.getScreenX() - x);
                stage.setY(mouseEvent.getScreenY() - y);

                stage.setOpacity(.8);
            });

            root.setOnMouseReleased((mouseEvent) -> {
                stage.setOpacity(1);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void signInButton(ActionEvent event) {
        String sql = "SELECT * FROM account_student WHERE StudentID = ? and Password = ?";

        connect = database.getConnection();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, si_userID.getText());
            prepare.setString(2, si_password.getText());
            result = prepare.executeQuery();

            Alert alert;

            if (si_userID.getText().isEmpty() || si_password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            } else {
                if (result.next()) {

                    int roleID = result.getInt("RoleID");

                    if (roleID == 3) { // Student
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Login as Student!");
                        alert.showAndWait();

                        String surname = result.getString("Surname");
                        int studentID = result.getInt("studentID");
                        System.out.println("Retrieved studentID: " + studentID);

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/userDashboard.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Parent root = loader.load();

                        // Get the controller of the loaded FXML file
                        UserDashboardController userDashboardController = loader.getController();

                        // Set the username using the method in UserDashboardController
                        userDashboardController.setUsername(surname);

                        // Set the studentID using the method in UserDashboardController
                        userDashboardController.setStudentID(studentID);

                        ((Node) (event.getSource())).getScene().getWindow().hide();
                        stage.setWidth(1332);
                        stage.setHeight(835);

                        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                        double centerX = screenBounds.getMinX() + screenBounds.getWidth() / 2.0;
                        double centerY = screenBounds.getMinY() + screenBounds.getHeight() / 2.0;
                        stage.setX(centerX - 666);
                        stage.setY(centerY - 417.5);

                        Scene scene = new Scene(root, 1332, 835);

                        stage.setScene(scene);
                        stage.show();

                        root.setOnMousePressed((mouseEvent) -> {
                            x = mouseEvent.getSceneX();
                            y = mouseEvent.getSceneY();
                        });

                        root.setOnMouseDragged((mouseEvent) -> {
                            stage.setX(mouseEvent.getScreenX() - x);
                            stage.setY(mouseEvent.getScreenY() - y);

                            stage.setOpacity(.8);
                        });

                        root.setOnMouseReleased((mouseEvent) -> {
                            stage.setOpacity(1);
                        });

                    } else if (roleID == 2) { // Officer
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Login as Officer!");
                        alert.showAndWait();

                        Parent root = FXMLLoader.load(getClass().getResource("/view/officerDashboard.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        ((Node) (event.getSource())).getScene().getWindow().hide();
                        stage.setWidth(1332);
                        stage.setHeight(835);

                        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                        double centerX = screenBounds.getMinX() + screenBounds.getWidth() / 2.0;
                        double centerY = screenBounds.getMinY() + screenBounds.getHeight() / 2.0;
                        stage.setX(centerX - 666);
                        stage.setY(centerY - 417.5);

                        Scene scene = new Scene(root, 1332, 835);

                        stage.setScene(scene);
                        stage.show();

                        root.setOnMousePressed((mouseEvent) -> {
                            x = mouseEvent.getSceneX();
                            y = mouseEvent.getSceneY();
                        });

                        root.setOnMouseDragged((mouseEvent) -> {
                            stage.setX(mouseEvent.getScreenX() - x);
                            stage.setY(mouseEvent.getScreenY() - y);

                            stage.setOpacity(.8);
                        });

                        root.setOnMouseReleased((mouseEvent) -> {
                            stage.setOpacity(1);
                        });

                    } else if (roleID == 1) { // Admin
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Login as Admin!");
                        alert.showAndWait();

                        Parent root = FXMLLoader.load(getClass().getResource("/view/adminDashboard.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        ((Node) (event.getSource())).getScene().getWindow().hide();
                        stage.setWidth(1332);
                        stage.setHeight(835);

                        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                        double centerX = screenBounds.getMinX() + screenBounds.getWidth() / 2.0;
                        double centerY = screenBounds.getMinY() + screenBounds.getHeight() / 2.0;
                        stage.setX(centerX - 666);
                        stage.setY(centerY - 417.5);

                        Scene scene = new Scene(root, 1332, 835);

                        stage.setScene(scene);
                        stage.show();

                        root.setOnMousePressed((mouseEvent) -> {
                            x = mouseEvent.getSceneX();
                            y = mouseEvent.getSceneY();
                        });

                        root.setOnMouseDragged((mouseEvent) -> {
                            stage.setX(mouseEvent.getScreenX() - x);
                            stage.setY(mouseEvent.getScreenY() - y);

                            stage.setOpacity(.8);
                        });

                        root.setOnMouseReleased((mouseEvent) -> {
                            stage.setOpacity(1);
                        });

                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Wrong Username/Password");
                        alert.showAndWait();
                    }

                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username/Password");
                    alert.showAndWait();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
