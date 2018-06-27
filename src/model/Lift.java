package model;

import java.util.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.*;

/**
 * A Lift carries people to their destinations.
 */
public class Lift {
    private IntegerProperty number = new SimpleIntegerProperty();
    private int bottom;
    private int top;
    private IntegerProperty level = new SimpleIntegerProperty();
    private IntegerProperty direction = new SimpleIntegerProperty();
    private StringProperty directionString = new SimpleStringProperty();
    private ObservableList<Person> passengers = FXCollections.observableArrayList();
    private ObservableList<Person> queue = FXCollections.observableArrayList();    
    
    private IntegerProperty passengersSize = new SimpleIntegerProperty();
    private IntegerProperty queueSize = new SimpleIntegerProperty();
    private StringProperty levelLayout = new SimpleStringProperty();
    
    public Lift(int number, int bottom, int top, int level) {
        this.number.set(number);
        this.bottom = bottom;
        this.top = top;
        this.level.set(level);
        this.direction.set(Direction.STATIONARY);
        //Bindings properties
        directionString.bind(Bindings.createStringBinding(
                () -> {
                    if (direction.get() == Direction.UP)
                        return "UP";
                    else if (direction.get() == Direction.DOWN)
                        return "DOWN"; 
                    else if (direction.get() == Direction.STATIONARY)
                        return "--";
                    return "";
                } , directionProperty()
        ));
        
        passengersSize.bind(Bindings.createIntegerBinding(
                () -> { return passengers.size(); }, getPassengers()
        ));
        
        queueSize.bind(Bindings.createIntegerBinding(
                () -> { return queue.size(); }, getQueue()
        ));
        
        levelLayout.bind(Bindings.createStringBinding(
                () -> {
                    String s = "";
                    //insert N leading spaces
                    for (int i = 0; i < getBottom(); i++) {
                        s += " ";
                    }
                    //bottom floor bar
                    s += "|";
                    //shows current level
                    for (int i = getBottom(); i <= getTop(); i++) {
                        s += (i == getLevel()) ? getLevel() : " ";
                    }
                    //top floor bar
                    s += "|";
                    return s;
                } , levelProperty()
        ));
        
    }

    // PROPERTIES

    public final int getNumber() {
        return number.get();
    }
    
    public ReadOnlyIntegerProperty numberProperty() { return number; }
    
    public final int getBottom() {
        return bottom;
    }

    public final int getTop() {
        return top;
    }

    public final int getLevel() {
        return level.get();
    }
    
    public ReadOnlyIntegerProperty levelProperty() { return level; }
    
    public final String getLevelLayout() {
        return levelLayout.get();
    }
    
    public ReadOnlyStringProperty levelLayoutProperty() { return levelLayout; }
    
    public final int getDirection() {
        return direction.get();
    }
    
    public ReadOnlyIntegerProperty directionProperty() { return direction; }
    
    public final String getDirectionString() {
        return directionString.get();
    }
        
    public ReadOnlyStringProperty directionStringProperty() { return directionString; }
    
    public final ObservableList<Person> getPassengers() {
        return passengers;
    }
    
    public final int getPassengersSize() {
        return passengersSize.get();
    }
    
    public ReadOnlyIntegerProperty passengersSizeProperty() { return passengersSize; }
    
    public final ObservableList<Person> getQueue() {
        return queue;
    }
    
    public final int getQueueSize() {
        return queueSize.get();
    }
    
    public ReadOnlyIntegerProperty queueSizeProperty() { return queueSize; }

    // FUNCTIONS and PROCEDURES

    public void call(Person person) {
        queue.add(person);
    }

    private void board(Person person) {
        queue.remove(person);
        passengers.add(person);
        person.board();
    }

    private void alight(Person person) {
        passengers.remove(person);
        person.alight();
    }

    /**
     * This procedure carries out the operation of a lift for one step of time.
     * It is intended to be called repeatedly.
     */
    public void operate() {
        // This is slightly different from Assignment 1
        Person nextPerson = nextPerson();
        if (direction.get() == Direction.STATIONARY) {
            if (nextPerson != null)
                direction.set(nextPerson.liftDirection(getLevel()));
//                setDirection(nextPerson.liftDirection(getLevel()));
        }
        if (alight() || board()) {
            if (passengers.isEmpty())
                direction.set(Direction.STATIONARY);
//                setDirection(Direction.STATIONARY);
        }
        else {
            move();
            if (isAtBoundary() || passengers.isEmpty() && anyoneWaitingHere())
                direction.set(Direction.STATIONARY);
//                setDirection(Direction.STATIONARY);
        }
    }

    private boolean anyoneWaitingHere() {
        if (queue.isEmpty())
            return false;
        Person person = queue.get(0);
        return person.isAt(level.get());
    }

    private void move() {
        level.set(level.get() + direction.get());
        for (Person person : passengers)
            person.move(direction.get());
    }

    /**
     * Determine the next person to service.
     */
    private Person nextPerson() {
        // Take the next passenger if there is one
        if (passengers.size() > 0)
            return passengers.get(0);
        // Otherwise go to pick up the next waiting if there is one
        else if (queue.size() > 0)
            return queue.get(0);
        // Otherwise there is no next person
        else
            return null;
    }

    private boolean board() {
        int count = 0;
        for (Person person : new LinkedList<Person>(queue))
            if (person.canBoard(getLevel(), direction.get())) {
                board(person);
                count++;
            }
        return count > 0;
    }

    private boolean alight() {
        int count = 0;
        for (Person person : new ArrayList<Person>(passengers))
            if (person.hasArrived()) {
                alight(person);
                count++;
            }
        return count > 0;
    }

    private boolean isAtBoundary() {
        return getLevel() == bottom || getLevel() == top;
    }

    private int distanceTo(int target) {
        return Math.abs(target - getLevel());
    }

    public int suitability(int distance, int start, int destination) {
        if (!canTake(start, destination))
            return 0;
        else if (direction.get() * Direction.fromTo(getLevel(), start) < 0)
            return 1;
        else if (direction.get() == -Direction.fromTo(start, destination))
            return distance + 1 - distanceTo(start);
        else
            return distance + 2 - distanceTo(start);
    }

    private boolean canTake(int start, int destination) {
        return canReach(start) && canReach(destination);
    }

    private boolean canReach(int level) {
        return level >= bottom && level <= top;
    }
    
    @Override
    public String toString() {
        return "Lift " + number.get();
    }
}
