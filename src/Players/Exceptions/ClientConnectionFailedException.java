package Players.Exceptions;

public class ClientConnectionFailedException extends Exception {
    
    public ClientConnectionFailedException(String errorMessage) {
        super(errorMessage);
    }
}
