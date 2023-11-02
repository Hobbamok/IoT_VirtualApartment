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
     *
     * @param timeSeriesName name of the timeseries, usually sensorName or sensorName_scenarioB, should correspond to a csv file present
     * @param start of time series, inclusive
     * @param end of time series, inclusive
     * @return list of updated sensorConfigs
     */
    public ArrayList<SensorConfig> retrieveTimeSeries(String timeSeriesName, LocalDate start, LocalDate end, ArrayList<SensorConfig> scs){
        Selection dateSelection;

        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath = Paths.get(currentPath.toString(), "data", timeSeriesName);
        Table table = Table.read().csv(filePath +".csv");
        DateColumn dc = table.dateTimeColumn("date_time").date(); // assume that selections will not be time-based
        dateSelection = dc.isOnOrBefore(end).and(dc.isOnOrAfter(start));
        Table timeSeries = table.where(dateSelection); // selecting only relevant rows

        for (SensorConfig sc : scs) {
            HashMap<LocalDateTime, String> tm = new HashMap<>();
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
}
