package Connector;

import Configuration.SensorConfig;

public class Connector {
    public boolean sendPerSensor(SensorConfig sensor){
        //todo connection to MQTT hub

        //read sensor config to find topic

        //send the sensors "formattedDataEntries" to the topic, each entry separately


      return true; //for success
    };
}
