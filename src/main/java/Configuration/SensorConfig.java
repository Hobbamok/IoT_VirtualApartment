package Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

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

    public HashMap<LocalDateTime, String> getData() {
        return data;
    }

    public void setData(LinkedHashMap<LocalDateTime, String> data) {
        this.data = data;
        formattedDataEntries = formatter.formatData(data);
    }

    LinkedHashMap<LocalDateTime, String> data;
    public ArrayList<byte[]> getFormattedData() {
        return formattedDataEntries;
    }

    private ArrayList<byte[]> formattedDataEntries;

    @Override
    public String toString() {
        return "SensorConfig{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", data=" + data +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void formatData(){
        formattedDataEntries = formatter.formatData(data);
    }
}
