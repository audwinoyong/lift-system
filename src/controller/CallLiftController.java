package controller;

import au.edu.uts.ap.javafx.*;
import java.lang.*;
import javafx.event.*;
import model.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
 * The controller for call_lift.fxml.
 */
public class CallLiftController extends Controller<Building> {
    @FXML private ComboBox peopleCmb;
    @FXML private TextField destinationTf;
    @FXML private Text errorTxt;
    
    public final Building getBuilding() { return model; }
    
    private final int getDestination() { return Integer.parseInt(destinationTf.getText()); }
    private final void setDestination(int value) { destinationTf.setText(""+value); }
    
    @FXML private void initialize() {        
    }
    
    private Person getSelectedPerson() {
        return (Person) peopleCmb.getSelectionModel().getSelectedItem();
    }
    
    @FXML private void handleCancel() {
        stage.close();
    }
    
    @FXML private void handleCall(ActionEvent event) throws Exception {
        try {
            Person person = getSelectedPerson();
            if (person == null) {
                throw new NullPointerException();
            }
            int destination = getDestination();
            getBuilding().call(person, destination);
            stage.close();
        }
        catch (NullPointerException e) {
            errorTxt.setText("You must select a caller");
        }
        catch (NumberFormatException e) {
            errorTxt.setText("Destination must be an integer");
        }
        catch (NoSuitableLiftException e) {
            errorTxt.setText("No suitable lift found");
        }
    }
}