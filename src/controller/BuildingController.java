package controller;

import au.edu.uts.ap.javafx.*;
import javafx.event.*;
import model.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * The controller for building.fxml.
 */
public class BuildingController extends Controller<Building> {
    /* CORE FUNCTIONALITY */
    //@FXML private ListView<Lift> liftsLv;
    
    /* ADVANCED FUNCTIONALITY */
    @FXML private TableView<Lift> liftsTv;
    @FXML private TableColumn<Lift, String> liftClm;
    @FXML private TableColumn<Lift, String> levelClm;
    @FXML private TableColumn<Lift, String> directionClm;
    @FXML private TableColumn<Lift, String> passengersClm;
    @FXML private TableColumn<Lift, String> waitingClm;
    
    @FXML private Button viewLiftBtn;
    @FXML private Slider delaySl;
    
    public final Building getBuilding() { return model; }

    @FXML private void initialize() {
        // Start up the building. Don't forget to also shutdown the building
        // when the user clicks the "Exit" button.
        getBuilding().startup();
        
        /* CORE FUNCTIONALITY */
        //liftsLv.getSelectionModel().selectedItemProperty().addListener(
        //        (o, oldSubject, newSubject) -> viewLiftBtn.setDisable(newSubject == null)
        //        );
        
        /* ADVANCED FUNCTIONALITY */
        liftsTv.getSelectionModel().selectedItemProperty().addListener(
                (o, oldSubject, newSubject) -> viewLiftBtn.setDisable(newSubject == null)
                );
        
        liftClm.setCellValueFactory(cellData -> cellData.getValue().numberProperty().asString("Lift %d"));
        levelClm.setCellValueFactory(cellData -> cellData.getValue().levelLayoutProperty());
        directionClm.setCellValueFactory(cellData -> cellData.getValue().directionStringProperty());
        passengersClm.setCellValueFactory(cellData -> cellData.getValue().passengersSizeProperty().asString());
        waitingClm.setCellValueFactory(cellData -> cellData.getValue().queueSizeProperty().asString());
        
        levelClm.setCellFactory(column -> {
            return new TableCell<Lift, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    }
                    else {
                        setText(item);
                    }
                }
            };
        });
        
        delaySl.valueProperty().bindBidirectional(getBuilding().delayProperty());
        
        /* Slider alternative solution - using Change Listeners */
        //delaySl.valueProperty().addListener(
        //        (o, oldValue, newValue) -> getBuilding().setDelay(getDelay())
        //        );
    }

    /**
     * This accessor method returns the selected number on the delay slider.
     *
     * @return the the selected delay
     */
    private int getDelay() {
        return (int)delaySl.getValue();
    }
    
    private Lift getSelectedLift() {
        /* CORE FUNCTIONALITY */
        //return liftsLv.getSelectionModel().getSelectedItem();
        
        /* ADVANCED FUNCTIONALITY */
        return liftsTv.getSelectionModel().getSelectedItem();
    }
    
    //3rd button to work
    @FXML private void handleCallLift(ActionEvent event) throws Exception {
        ViewLoader.showStage(getBuilding(), "/view/call_lift.fxml", "Call lift", new Stage());
    }
    
    //2nd button to work
    @FXML private void handleViewLift(ActionEvent event) throws Exception {
        Lift lift = getSelectedLift();
        if (lift != null) {
            ViewLoader.showStage(lift, "/view/lift.fxml", "Lift", new Stage());
        }
    }    
    
    //last button to work
    @FXML private void handleViewPeople(ActionEvent event) throws Exception {
        ViewLoader.showStage(getBuilding(), "/view/people.fxml", "People", new Stage());
    }
    
    //1st button to work
    @FXML private void handleExit() {
        getBuilding().shutdown();
        stage.close();
    }
}
