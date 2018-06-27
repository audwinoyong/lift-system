package controller;

import au.edu.uts.ap.javafx.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.event.*;
import model.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
 * The controller for lift.fxml.
 */
public class LiftController extends Controller<Lift> {
    public final Lift getLift() { return model; }

    @FXML private void initialize() {
    }
    
    @FXML private void handleClose() {
        stage.close();
    }
}
