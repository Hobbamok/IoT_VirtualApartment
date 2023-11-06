package Interface;

import Controller.Controller;

public class CommandLineInterface implements UserInterface{
    Controller controller;

    @Override
    public void init(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void log(String message) {

    }

    @Override
    public void logError(String message, Exception e) {

    }

    @Override
    public void logError(String message) {

    }

    @Override
    public void logWarning(String message) {

    }

    @Override
    public void logWarning(String message, Exception e) {

    }
}
