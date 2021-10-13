package Boards.Errors;

public class PlaceAlreadyTaken extends Exception {
    public PlaceAlreadyTaken(String errorMessage) {
        super(errorMessage);
    }
}
