package eu.nerdz.api;

public class LoginException extends Exception {
    private String defaultMsg = "(LoginException): Wrong combination of username and password during the login session.\n";

    public LoginException(){
        super();
    }

    public LoginException(String errMsg){
        super(errMsg);
        this.defaultMsg = errMsg;
    }

    public String getMessage(){
        return defaultMsg;
    }
}
