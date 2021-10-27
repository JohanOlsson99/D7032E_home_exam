package Players.Exceptions;

public class PlayerDisconnectedException extends Exception {
    
    public PlayerDisconnectedException(String errorMessage) {
        super(errorMessage);
    }
}
