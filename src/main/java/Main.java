import Controller.Controller;
import Datasource.FilesManager;
import Interface.CommandLineInterface;
import Interface.UserInterface;

public class Main {
    public static void main(String[] args) {
        var scenario = FilesManager.getAllFilesWithEnding("scenario", true);
        System.out.println(scenario);

        UserInterface userInterface = new CommandLineInterface();
        Controller controller = new Controller(userInterface);
        userInterface.init(controller);

        //this starts a scripted default scenario run
        controller.setupAndSendTimeSeries();

    }
}