package Connector;

import Configuration.SensorConfig;
import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;

public class Connector {
    String publisherId = "virtual_apartment";//UUID.randomUUID().toString();
    IMqttClient publisher;

    public Connector(String brokerURL) throws MqttException {
        publisher = new MqttClient(brokerURL,publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        publisher.connect(options);
    }
    public boolean sendPerSensor(SensorConfig sensor){
        //todo connection to MQTT hub

        //read sensor config to find topic

        //send the sensors "formattedDataEntries" to the topic, each entry separately
        boolean didAnythingFail = false;


        return !didAnythingFail; //so true is returned if everything went well
    };

    private boolean sendSingleMessage(String mqttTopic, byte[] payload){
        MqttMessage msg = new MqttMessage(payload);
        msg.setQos(2);// means "exactly once", maybe put 0 for at most once (fire&forget), or 1 at least once (if DT can handle duplicates)
        msg.setRetained(true);
        try {
            publisher.publish(mqttTopic,msg);
        } catch (MqttException e) {
            System.out.println("ERROR: Failed to send message to MQTT broker, topic: " + mqttTopic + " payload: " + payload.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
