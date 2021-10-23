package Players.Errors;

public class PlayerDisconnectedException extends Exception {
    
    public PlayerDisconnectedException(String errorMessage) {
        super(errorMessage);
    }
}
