package Players.Errors;

public class ClientConnectionFailedException extends Exception {
    
    public ClientConnectionFailedException(String errorMessage) {
        super(errorMessage);
    }
}
