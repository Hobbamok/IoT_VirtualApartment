package Configuration;

/**
 * was called SensorManager in the Phase 2 doc
 * is basically a SensorConfigFactory
 */
public class ConfigManager {
    public SensorConfig getSensorConfig(String type, int id){
        //todo fully flesh out this method, read the scenario file, etc
        SensorConfig sensorConfig = new SensorConfig();
        sensorConfig.setMqttTopic("testing_virtual_apartment/temperature");
        sensorConfig.formatter = new Formatter();
        sensorConfig.id = id;
        sensorConfig.type = type;
        return sensorConfig;
    }

    private String readSensorConfigFile(String type){
        return "";//TODO flesh out this method
    }
}
