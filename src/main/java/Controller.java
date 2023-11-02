import Configuration.ConfigManager;
import Configuration.SensorConfig;
import Connector.Connector;
import Datasource.Datasource;
import tech.tablesaw.api.Table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class Controller {
    private Datasource datasource;
    private ConfigManager configManager;
    private Connector connector;
    private ArrayList<SensorConfig> sensorConfigs = new ArrayList<>();
    public Controller (){
        //todo init the other stuff
        datasource = new Datasource();
        configManager = new ConfigManager();
        //DON'T configure the Connector here since that is established per scenario
    }

    /**
     * Basically the "init" method per scenario, call this first.
     * @param scenarioName
     */
    public void setUpScenario(String scenarioName){
        //todo move this to an actual config, read from scenario?
        String mqttBrokerURL = "tcp://localhost:1883";//todo get this fromt he actual scenario file
        try{
            connector = new Connector(mqttBrokerURL);
        }catch(Exception e){
            System.out.println("ERROR: Failed to create connector");
            e.printStackTrace();
           // System.exit(5);//if we can't connect to the MQTT broker it's not worth continuing
        }

        // TODO: adapt this once method in ConfigManager is fleshed out fully
        sensorConfigs.add(configManager.getSensorConfig("type", 0));

    }

    /**
     * Sends a timeSeries after a scenario has been set up
     * @param timeSeriesName
     * @param start
     * @param end
     */
    public void sendTimeSeries(String timeSeriesName, LocalDate start, LocalDate end){
        sensorConfigs = datasource.retrieveTimeSeries(timeSeriesName, start, end, sensorConfigs);
        //todo REFORMAT HERE!
        connector.sendForSensors(sensorConfigs);
    }

    /**
     * "Does everything method" so the program runs as a script
     */
    public void setupAndSendTimeSeries(){
        setUpScenario("default");//creates the required sensor objects & reads the timeframe etc.
        //todo set up the sensors with data, read required start and end dates from scenario config
        // I think it might be easier to pass date range as string and have the datasource parse that string internally
        LocalDate start = LocalDate.of(2011,1,1);
        LocalDate end = LocalDate.of(2013,12,31);
        sendTimeSeries("default", start, end);//sends to Connector
    }
}
