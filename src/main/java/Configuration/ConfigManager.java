package Configuration;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

/**
 * was called SensorManager in the Phase 2 doc
 * is basically a SensorConfigFactory
 */
public class ConfigManager {
    public SensorConfig getHardcodedSensorConfig(String type, int id){
        //todo fully flesh out this method, read the scenario file, etc
        SensorConfig sensorConfig = new SensorConfig();
        sensorConfig.setMqttTopic("testing_virtual_apartment/temperature");
        sensorConfig.formatter = new Formatter(".*");
        sensorConfig.id = id;
        sensorConfig.type = type;
        return sensorConfig;
    }

    /**
     * generates a sensorConfig from a string as found in the scenario.property files
     * @param sensorInfo string info for a sensor, containing the type and id like this: "type:temperature;id:1"
     * @return
     */
    public SensorConfig getSensorConfigFromString(String sensorInfo, String dataPath) throws IOException {
        String type = sensorInfo.split("&")[0];
        int id = Integer.parseInt(sensorInfo.split("&")[1]);
        String mqttTopic = (sensorInfo.split("&").length > 2) ? sensorInfo.split("&")[2] : "";
        SensorConfig sensorConfig = readSensorConfigFile(type, dataPath);
        sensorConfig.setId(id);
        if(mqttTopic.equals("default") || mqttTopic.equals("")){ //if no topic is provided, use the default topic
            String defaultTopic = sensorConfig.getMqttTopic();
            sensorConfig.setMqttTopic(defaultTopic + "/" + id);
        }else{
            sensorConfig.setMqttTopic(mqttTopic); //use the topic provided in the scenario config
        }
        return sensorConfig;
    }

    private SensorConfig readSensorConfigFile(String type, String dataPath) throws IOException {
        Properties properties = new Properties();
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(dataPath + "/" + type + ".sensor"));
        properties.load(stream);
        stream.close();
        SensorConfig sensor = new SensorConfig();
        sensor.type = type;
        sensor.setMqttTopic(properties.getProperty("default_mqtt_topic"));
        String charset = properties.getProperty("formatting_charset");
        if(Boolean.getBoolean(properties.getProperty("no_formatting"))){
            sensor.formatter = new Formatter(".*", charset);
        }
        else{
            sensor.formatter = new Formatter(properties.getProperty("formatting_regex"),charset );
        }

        
        
        return sensor;
    }

    /**
     * generates a scenarioConfig from file
     * @param dataPath path to the Data folder
     * @param scenarioName name of the scenario
     * @return the read scenarioConfig
     * @throws IOException
     */
    public ScenarioConfig scenarioConfigFromFile(String dataPath, String scenarioName) throws IOException {
        Properties properties = new Properties();
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(dataPath + "/" + scenarioName + ".scenario"));
        properties.load(stream);
        stream.close();
        ScenarioConfig scenario = new ScenarioConfig();
        scenario.mqttBrokerURL = properties.getProperty("mqttBrokerURL");
        scenario.timeSeriesName = properties.getProperty("timeSeriesName");
        scenario.start = LocalDate.parse(properties.getProperty("start_time"));
        scenario.end = LocalDate.parse(properties.getProperty("end_time"));
        scenario.sensorConfigs = new ArrayList<>();

        String[] sensors = properties.getProperty("sensors").split(";");
        for (String sensorInfo : sensors) {
            scenario.sensorConfigs.add(getSensorConfigFromString(sensorInfo, dataPath));
        }

        System.out.println("Loaded scenario \"+ " + scenarioName + "\" from file. Description: " + properties.getProperty("description"));

        return scenario;
    }

}
