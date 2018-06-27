package levelview;

import java.io.IOException;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class LevelView extends HBox {
    @FXML private Text bottomTxt;
    @FXML private Text topTxt;
    @FXML private Text levelTxt;
    
    public LevelView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("level_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public String getTop() {
        return topProperty().get();
    }
    
    public void setTop(String value) {
        topProperty().set(value);
    }
    
    public StringProperty topProperty() {
        return topTxt.textProperty();                
    }
    
    public String getBottom() {
        return bottomProperty().get();
    }
    
    public void setBottom(String value) {
        bottomProperty().set(value);
    }
    
    public StringProperty bottomProperty() {
        return bottomTxt.textProperty();                
    }
    
    public String getLevel() {
        return levelProperty().get();
    }
    
    public void setLevel(String value) {
        levelProperty().set(value);
    }
    
    public StringProperty levelProperty() {
        return levelTxt.textProperty();                
    }
    
}
