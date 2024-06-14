package org.example.datavid_cake_tracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.datavid_cake_tracker.Exceptions.DuplicateMemberException;
import org.example.datavid_cake_tracker.Exceptions.UnderageMemberException;
import org.example.datavid_cake_tracker.Model.Member;
import org.example.datavid_cake_tracker.Repo.Repo;
import org.example.datavid_cake_tracker.Service.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddMemberController {
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField birthDateField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField cityField;

    private Service service;
    private Repo repo;

    @FXML
    private void initialize() throws SQLException {
        repo = new Repo();
        service = new Service(repo);
    }

    @FXML
    private void handleAddMember() {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            LocalDate birthDate = LocalDate.parse(birthDateField.getText());
            String country = countryField.getText();
            String city = cityField.getText();

            Member newMember = new Member(0, firstName, lastName, birthDate, country, city);
            service.addMember(newMember);

        } catch (DateTimeParseException e) {
            showAlert(e.getMessage(), "Please enter a valid date (yyyy-MM-dd)");
        } catch (SQLException | UnderageMemberException | DuplicateMemberException e) {
            showAlert(String.valueOf(e), e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
