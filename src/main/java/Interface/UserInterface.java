package Interface;
import Controller.Controller;


public interface UserInterface {
    void init(Controller controller);
    void log(String message);
    void logError(String message, Exception e);
    void logError(String message);
    void logWarning(String message);
    void logWarning(String message, Exception e);
}
