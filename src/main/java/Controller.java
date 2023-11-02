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
    private ArrayList<SensorConfig> sensors = new ArrayList<SensorConfig>();;
    public Controller (){
        //todo init the other stuff
        datasource = new Datasource();
        configManager = new ConfigManager();
        //DONT configure the Connector here since that is established per scenario
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
            System.exit(5);//if we can't connect to the MQTT broker it's not worth continuing
        }

        // retrieve time series data for provided time slice
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String startAsString = df.format(begin);
        String endAsString = df.format(end);
        Map<String, Object> ts = datasource.retrieveTimeSeries("testData", startAsString, endAsString);

    }

    /**
     * Sends a timeSeries after a scenario has been set up
     * @param timeSeriesName
     * @param start
     * @param end
     */
    public void sendTimeSeries(String timeSeriesName, Date start, Date end ){
        //todo get the data from the datasource  or filter it for the start & end time
        var timeseries = datasource.retrieveTimeSeries("testData", "2011-01-01", "2011-12-31");
        //TODO HOW TO MAP THIS TO THE SENSORS!?

        connector.sendForSensors(sensors);
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
