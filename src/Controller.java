import Configuration.ConfigManager;
import Configuration.SensorConfig;
import Connector.Connector;
import Datasource.Datasource;

import java.util.ArrayList;
import java.util.Date;

public class Controller {
    private Datasource datasource;
    private ConfigManager configManager;
    private Connector connector;
    private ArrayList<SensorConfig> sensors = new ArrayList<SensorConfig>();;
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
        //todo move this to an actual config, read from scenario?
        String mqttBrokerURL = "tcp://localhost:1883";//todo get this fromt he actual scenario file
        Date begin = new Date(Long.MIN_VALUE);
        Date end = new Date(Long.MAX_VALUE);
        try{
            connector = new Connector(mqttBrokerURL);
        }catch(Exception e){
            System.out.println("ERROR: Failed to create connector");
            e.printStackTrace();
            System.exit(5);//seriously, if we can't connect to the MQTT broker it's not worth continuing
        }
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
