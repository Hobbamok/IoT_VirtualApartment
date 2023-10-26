import Configuration.ConfigManager;
import Configuration.SensorConfig;
import Datasource.Datasource;

import java.util.ArrayList;
import java.util.Date;

public class Controller {
    private Datasource datasource;
    private ConfigManager configManager;
    private ArrayList<SensorConfig> sensors;
    public Controller (){
        //todo init the other stuff
        datasource = new Datasource();
        configManager = new ConfigManager();
    }

    /**
     * Basically the "init" method per scenario, call this first.
     * @param scenarioName
     */
    public void setUpScenario(String scenarioName){
        //todo
    }

    /**
     * Sends a timeSeries after a scenario has been set up
     * @param timeSeriesName
     * @param start
     * @param end
     */
    public void sendTimeSeries(String timeSeriesName, Date start, Date end ){

        //send per sensor
    }

    /**
     * "Does everything method" so the program runs as a script
     */
    public void setupAndSendTimeSeries(){
        setUpScenario("default");//creates the required sensor objects & reads the timeframe etc.
        //todo set up the sensors with data
        sendTimeSeries("default", new Date(Long.MIN_VALUE), new Date(Long.MAX_VALUE));//sends to Connector
    }
}
