package model;

import javafx.beans.binding.*;
import javafx.beans.property.*;

/**
 * A person boards and alights lifts.
 */
public class Person {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty level = new SimpleIntegerProperty();
    private IntegerProperty destination = new SimpleIntegerProperty();
    private BooleanProperty aboard = new SimpleBooleanProperty();
    private StringProperty aboardString = new SimpleStringProperty();
    
    public Person(int id, String name, int level) {
        this.id.set(id);
        this.name.set(name);
        this.level.set(level);
        this.destination.set(level);
        //Bindings property
        aboardString.bind(Bindings.createStringBinding(
                () -> {
                    return (aboard.get()) ? "Yes" : "No";
                }, aboardProperty()
        ));
    }

    // PROPERTIES

    public final int getId() {
        return id.get();
    }
    
    public ReadOnlyIntegerProperty idProperty() { return id; }

    public final String getName() {
        return name.get();
    }
    
    public ReadOnlyStringProperty nameProperty() { return name; }

    public final int getLevel() {
        return level.get();
    }
    
    public ReadOnlyIntegerProperty levelProperty() { return level; }
    
    public final int getDestination() {
        return destination.get();
    }

    public ReadOnlyIntegerProperty destinationProperty() { return destination; }
    
    public final boolean isAboard() {
        return aboard.get();
    }
    
    public ReadOnlyBooleanProperty aboardProperty() { return aboard; }
    
    public final String getAboardStringProperty() {
        return aboardString.get();
    }
    
    public ReadOnlyStringProperty aboardStringProperty() { return aboardString; }
    
    // FUNCTIONS and PROCEDURES

    public void call(int destination) {
        this.destination.set(destination);
    }

    public void move(int direction) {
        level.set(level.get() + direction);
    }

    public boolean hasId(int id) {
        return this.id.get() == id;
    }

    public boolean canBoard(int liftLevel, int liftDirection) {
        return isAt(liftLevel) && isHeadingIn(liftDirection);
    }

    public boolean isAt(int level) {
        return this.level.get() == level;
    }

    public boolean isHeadingIn(int direction) {
        return direction == direction();
    }

    public int direction() {
        return Direction.fromTo(level.get(), destination.get());
    }

    public boolean isIdle() {
        return !aboard.get() && level.get() == destination.get();
    }

    public boolean isWaiting() {
        return !aboard.get() && level.get() != destination.get();
    }

    public boolean hasArrived() {
        return level.get() == destination.get();
    }

    /**
     * Determine the direction that this person wants the lift to go in.
     */
    public int liftDirection(int liftLevel) {
        return Direction.fromTo(liftLevel, level.get() == liftLevel ? destination.get() : level.get());
    }

    public void board() {
        aboard.set(true);
//        setAboard(true);
    }

    public void alight() {
        aboard.set(false);
//        setAboard(false);
    }

    @Override
    public String toString() {
        return name.get();
    }
}
