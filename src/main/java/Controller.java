import Configuration.ConfigManager;
import Configuration.ScenarioConfig;
import Configuration.SensorConfig;
import Connector.Connector;
import Datasource.Datasource;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
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
        ScenarioConfig scenarioConfig;
        try{
            scenarioConfig = configManager.scenarioConfigFromFile(datasource.getDataDirectory(), scenarioName);
            connector = new Connector(scenarioConfig.mqttBrokerURL);

        }catch (IOException e){
            System.out.println("ERROR: Failed to read scenario config file, no scenario has been set up.");
            e.printStackTrace();
            //TODO handle the failure to read the scenario config file
            return;
        } catch (MqttException e) {
            System.out.println("ERROR: Failed to create connector for the scenario, no scenario has been set up.");
            e.printStackTrace();
            //TODO handle the failure to read the scenario config file
            return;
        }
        sensorConfigs = scenarioConfig.sensorConfigs;
        return;
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
        System.out.println(end.toString());
        System.out.println(start.toString());

        String erndStzring = end.toString();
        var end2 = LocalDate.parse("2013-12-31");
        sendTimeSeries("default", start, end);//sends to Connector
    }


}
