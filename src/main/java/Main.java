import Controller.Controller;
import Interface.CommandLineInterface;
import Interface.UserInterface;

public class Main {
    public static void main(String[] args) {
        UserInterface userInterface = new CommandLineInterface();
        Controller controller = new Controller(userInterface);
        userInterface.init(controller);

        //this starts a scripted default scenario run
        controller.setupAndSendTimeSeries();

    }
}