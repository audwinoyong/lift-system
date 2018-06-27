package model;

import java.util.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;

/**
 * The building has 3 lifts.
 *
 * The lifts are operated automatically by repeatedly calling the operate()
 * method in a loop in a thread. This loop is controlled by the following
 * fields:
 * - "delay" specifies how many seconds to wait between calling operate()
 * - "time" indicates how many times operate() has been called so far.
 */
public class Building {
    // READ CAREFULLY!
    //
    // You may be wondering how it is possible to test your application before
    // you have implemented the "Call Lift" window. Without the ability to call a
    // lift, your lifts will remain stationary, and you would not be able to test
    // that your application shows the lifts moving and the passengers getting on
    // and off.
    //
    // To solve this problem, you need to leave the following constant set to
    // true.
    //
    // Setting AUTO_CALL to true will cause people to automatically call lifts to
    // go to random destinations. You NEED to leave this set to true until you
    // implement the "Call Lift" window. When you submit your final assignment,
    // if your "Call Lift" window does not work, then you must also leave this
    // set to true so that your peer markers can test your application!
    //
    // When you finally implement the "Call Lift" window, you need to set this to
    // false so that people don't automatically call lifts while the user is
    // trying to call a lift.
    public static final boolean AUTO_CALL = false;

    private int entrance = 2;
    private int bottom = 1;
    private int top = 6;
    private IntegerProperty time = new SimpleIntegerProperty(); // Time starts at zero
    private ObservableList<Lift> lifts = FXCollections.observableArrayList();
    private ObservableList<Person> people = FXCollections.observableArrayList();
    private IntegerProperty delay = new SimpleIntegerProperty(1); // The initial delay between operate() calls is 1 second.
    private OperationThread thread;

    public Building() {
        lifts.add(new Lift(1, 1, 6, entrance));
        lifts.add(new Lift(2, 2, 6, entrance));
        lifts.add(new Lift(3, 2, 5, entrance));
        people.add(new Person(1, "Ryan", entrance));
        people.add(new Person(2, "Georges", entrance));
        people.add(new Person(3, "David", entrance));
        people.add(new Person(4, "Yvonne", entrance));
        people.add(new Person(5, "Divraj", entrance));
    }

    // PROPERTIES

    public final int getBottom() {
        return bottom; 
    }

    public final int getTop() {
        return top;
    }

    public final int getDistance() {
        return top - bottom;
    }

    public final int getDelay() {
        return delay.get();
    }

    public final void setDelay(int delay) {
        this.delay.set(delay);
    }
    
    public IntegerProperty delayProperty() { return delay; }

    public final int getTime() {
        return time.get();
    }
    
    public ReadOnlyIntegerProperty timeProperty() { return time; }

    public final ObservableList<Lift> getLifts() {
        return lifts;
    }
        
    public final ObservableList<Person> getPeople() {
        return people;
    }

    // FUNCTIONS and PROCEDURES

    public synchronized void startup() {
        if (thread == null) {
            thread = new OperationThread();
            thread.start();
        }
    }

    public synchronized void shutdown() {
        if (thread != null) {
            thread.shutdown();
        }
    }
    
    public void call(Person caller, int destination) throws NoSuitableLiftException {
        Lift bestLift = bestLift(caller.getLevel(), destination);
        if (bestLift != null) {
            caller.call(destination);
            bestLift.call(caller);
        }
        else {
            throw new NoSuitableLiftException();
            //System.out.println("No suitable lift found");
        }
    }

    private void operate() {
        if (AUTO_CALL)
            autoCall();
        for (Lift lift : lifts)
            lift.operate();
    }

    private void autoCall() {
        Person caller = people.get((int)(people.size() * Math.random()));
        if (caller.isIdle()) {
            int destination = bottom + (int)(getDistance()*Math.random());
            if (caller.isAt(destination))
                destination++;
            try {
                call(caller, destination);
            }
            catch (Exception e) {
                // Ignore the call
            }
        }
    }

    private Lift bestLift(int start, int destination) {
        int max = 0;
        Lift best = null;
        for (Lift lift : lifts) {
            int suitability = lift.suitability(getDistance(), start, destination);
            if (suitability > max) {
                max = suitability;
                best = lift;
            }
        }
        return best;
    }

    private class OperationThread extends Thread {
        private boolean running = true;

        @Override public void run() {
            while (running) {
                try {
                    Thread.sleep(getDelay() * 1000);
                    Platform.runLater(() -> {
                        operate();
                        time.set(time.get() + 1);
                    });
                }
                catch (InterruptedException e) {
                    // Happens when requested to shut down
                }
            }
        }

        public void shutdown() {
            running = false;
            interrupt();
        }
    }
}
