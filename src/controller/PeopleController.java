package controller;

import au.edu.uts.ap.javafx.*;
import javafx.event.*;
import model.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;

/**
 * The controller for people.fxml.
 */
public class PeopleController extends Controller<Building> {
    @FXML private TableView<Person> peopleTv;
    @FXML private TableColumn<Person, String> idClm;
    @FXML private TableColumn<Person, String> nameClm;
    @FXML private TableColumn<Person, String> levelClm;
    @FXML private TableColumn<Person, String> destinationClm;
    @FXML private TableColumn<Person, String> aboardClm;
  
    public final Building getBuilding() { return model; }
    
    @FXML private void initialize() {       
        idClm.setCellValueFactory(cellData -> cellData.getValue().idProperty().asString());
        nameClm.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        levelClm.setCellValueFactory(cellData -> cellData.getValue().levelProperty().asString("Level %d"));
        destinationClm.setCellValueFactory(cellData -> cellData.getValue().destinationProperty().asString("Level %d"));
        aboardClm.setCellValueFactory(cellData -> cellData.getValue().aboardStringProperty());
    }
    
    @FXML private void handleClose() {
        stage.close();
    }

}