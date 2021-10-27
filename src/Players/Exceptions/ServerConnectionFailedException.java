package Players.Exceptions;

public class ServerConnectionFailedException extends Exception {
    
    public ServerConnectionFailedException(String errorMessage) {
        super(errorMessage);
    }
}
