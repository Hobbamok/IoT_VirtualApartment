import Datasource.Datasource;

public class Main {
    public static void main(String[] args) {
        //Controller controller = new Controller();
        Datasource ds = new Datasource();

        ds.retrieveTimeSeries("testData", "2011-01-01", "2011-12-31");
        //controller.init();
        //controller.setupAndSendTimeSeries();

    }
}