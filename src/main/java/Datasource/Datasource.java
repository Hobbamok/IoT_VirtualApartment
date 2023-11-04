package Datasource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import Configuration.SensorConfig;
import tech.tablesaw.api.*;
import tech.tablesaw.selection.Selection;

import java.time.LocalDate;

public class Datasource {
    /**
     * @param timeSeriesName name of the timeseries, usually sensorName or sensorName_scenarioB, should correspond to a csv file present
     * @param start          of time series, inclusive
     * @param end            of time series, inclusive
     * @return list of updated sensorConfigs
     */
    public ArrayList<SensorConfig> retrieveTimeSeries(String timeSeriesName, LocalDate start, LocalDate end, ArrayList<SensorConfig> scs) {
        Selection dateSelection;


        Table table = Table.read().csv(getDataDirectoryWithAddition(timeSeriesName + ".csv"));
        DateColumn dc = table.dateTimeColumn("date_time").date(); // assume that selections will not be time-based
        dateSelection = dc.isOnOrBefore(end).and(dc.isOnOrAfter(start));
        Table timeSeries = table.where(dateSelection); // selecting only relevant rows

        for (SensorConfig sc : scs) {
            LinkedHashMap<LocalDateTime, String> tm = new LinkedHashMap<>();
            int sensorId = sc.getId();
            Table sensorData = timeSeries.where(timeSeries.intColumn("sensor_id").isEqualTo(sensorId));
            List<String> st = sensorData.column("msg").asStringColumn().asList();
            List<LocalDateTime> dt = sensorData.dateTimeColumn("date_time").asList();

            for (int j = 0; j < dt.size(); j++) {
                tm.put(dt.get(j), st.get(j));
            }
            sc.setData(tm);
            System.out.println("added data to SensorConfig: " + sc);
        }
        return scs;
    }

    //the setup below allows changing the data directory dynamically.
    Path dataDirectory = Paths.get(System.getProperty("user.dir"), "data");

    public void setDataDirectory(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public String getDataDirectory() {
        return dataDirectory.toString();
    }

    public String getDataDirectoryWithAddition(String addition) {
        return getDataDirectory() +"/" + addition;
    }
}