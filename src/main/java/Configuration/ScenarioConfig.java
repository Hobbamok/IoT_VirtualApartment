package Configuration;

import Configuration.SensorConfig;

import java.time.LocalDate;
import java.util.ArrayList;

public class ScenarioConfig{
    public String mqttBrokerURL;
    public String timeSeriesName;
    public LocalDate start;
    public LocalDate end;
    public ArrayList<SensorConfig> sensorConfigs;


}