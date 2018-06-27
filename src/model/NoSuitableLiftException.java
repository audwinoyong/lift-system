package model;

public class NoSuitableLiftException extends Exception {
    public NoSuitableLiftException() {
        super("No suitable lift found");
    }
}