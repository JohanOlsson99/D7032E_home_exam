package Players.Errors;

public class ServerConnectionFailedException extends Exception {
    
    public ServerConnectionFailedException(String errorMessage) {
        super(errorMessage);
    }
}
