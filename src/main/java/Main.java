import Datasource.Datasource;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        Datasource ds = new Datasource();

        //controller.init();
        controller.setupAndSendTimeSeries();

    }
}