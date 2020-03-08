package employeeDatabase.controllers;

import employeeDatabase.helpers.ConnectionManager;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

//For initializing this program we need to implement interface "Initializable"
public class LoginController implements Initializable {

    //Defining all elements from Login.fxml file
    public AnchorPane LoginForm;
    public TextField UsernameField;
    public javafx.scene.control.PasswordField PasswordField;
    public Label LoginLabel;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    //Create connection with database
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = ConnectionManager.getConnection();
    }

    //Defining button "Confirm"
    public void buttonConfirm() throws IOException {
        //If value correct change stage on MainMenu
        if (UsernameField.getText().equals(getDepartment()) && PasswordField.getText().equals(getSurname())) {
            Stage stage = (Stage) LoginForm.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("/employeeDatabase/views/MainMenu.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("MindIT");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();

        } else {
            //Show alert if input incorrect value into username and password fields
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Username or Password!", ButtonType.OK);
            alert.setTitle("Invalid");
            alert.showAndWait();
        }
    }

    //Method for checking whether inserted value about Department is correct
    private String getDepartment() {
        String ID = "";
        try {
            preparedStatement = connection.prepareStatement("SELECT viddil FROM employee.employee WHERE viddil = ?;");
            preparedStatement.setString(1, UsernameField.getText());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ID = resultSet.getString(1);
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ID;
    }

    //Method for checking whether inserted value about Surname and its Department is correct
    private String getSurname() {
        String Surname = "";
        try {
            preparedStatement = connection.prepareStatement("SELECT prizvyshche FROM employee.employee WHERE viddil = ?;");
            preparedStatement.setString(1, UsernameField.getText());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Surname = resultSet.getString(1);
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Surname;
    }
}