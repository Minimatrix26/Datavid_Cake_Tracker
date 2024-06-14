package org.example.datavid_cake_tracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.datavid_cake_tracker.Model.Member;
import org.example.datavid_cake_tracker.Repo.Repo;
import org.example.datavid_cake_tracker.Service.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class HelloController {
    @FXML
    private TableView<Member> tableMembers;

    @FXML
    private TableColumn<Member, Integer> colId;

    @FXML
    private TableColumn<Member, String> colFirstName;

    @FXML
    private TableColumn<Member, String> colLastName;

    @FXML
    private TableColumn<Member, String> colBirthDate;

    @FXML
    private TableColumn<Member, String> colCountry;

    @FXML
    private TableColumn<Member, String> colCity;

    @FXML
    private Button btnSort;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnAdd;

    private Repo repo;
    private Service service;

    @FXML
    private void initialize() throws SQLException {
        repo = new Repo();
        service = new Service(repo);
        loadMembers();
    }

    private void loadMembers() throws SQLException {
        ObservableList<Member> members = FXCollections.observableList(service.sortMembersByBirthDate());
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        tableMembers.setItems(members);
    }

    @FXML
    private void handleDelete() throws SQLException {
        Member selectedMember = tableMembers.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            service.deleteMember(selectedMember.getId());
            loadMembers();
        }
    }

    @FXML
    private void handleSort() throws SQLException {
        loadMembers();
    }

    @FXML
    private void handleAddAction(ActionEvent event) throws SQLException  {
        if (event.getSource() == btnAdd) {
            openAddMemberView();
        }
    }

    private void openAddMemberView() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-member.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Add Member");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}