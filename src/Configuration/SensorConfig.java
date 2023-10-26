package Configuration;

import java.util.ArrayList;

public class SensorConfig {
    int id;
    String type;
    Formatter formatter;
    //todo Marina: add data here
    public ArrayList<String> getFormattedData() {
        return formattedDataEntries;
    }

    private ArrayList<String> formattedDataEntries;
}
