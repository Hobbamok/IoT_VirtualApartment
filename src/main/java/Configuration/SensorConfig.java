package Configuration;

import java.util.ArrayList;

public class SensorConfig {
    int id;
    String type;
    Formatter formatter;

    public String getMqttTopic() {
        return mqttTopic;
    }

    public void setMqttTopic(String mqttTopic) {
        this.mqttTopic = mqttTopic;
    }

    String mqttTopic;
    //todo Marina: add data here
    public ArrayList<byte[]> getFormattedData() {
        return formattedDataEntries;
    }

    private ArrayList<byte[]> formattedDataEntries;
}
