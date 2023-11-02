package Datasource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import tech.tablesaw.api.*;
import tech.tablesaw.selection.Selection;

import java.time.LocalDate;

public class Datasource {
    /**
     *
     * @param timeSeriesName name of the timeseries, usually sensorName or sensorName_scenarioB, should correspond to a csv file present
     * @param start of time series, inclusive
     * @param end of time series, inclusive
     * @return
     */
    public Map<String, Object> retrieveTimeSeries(String timeSeriesName, String start, String end){
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        Selection dateSelection;

        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath = Paths.get(currentPath.toString(), "data", timeSeriesName);
        Table table = Table.read().csv(filePath +".csv");
        DateColumn dc = table.dateColumn("date");
        dateSelection = dc.isOnOrBefore(startDate).and(dc.isOnOrAfter(endDate));
        //Table timeSeries = table.where(dateSelection);

        Set sensors = table.stringColumn("sensor_name").asSet();
        Iterator<String> sensorsIterator = sensors.iterator();
        Map<String, Object> timeSeriesPerSensor = new HashMap<String, Object>();

        while(sensorsIterator.hasNext()) {
            String sensor = sensorsIterator.next();
            Table sensorNames = table.where(table.stringColumn("sensor_name").isEqualTo(sensor));

            HashMap<List<LocalDate>, List<String>> tm = new HashMap<>();
            StringColumn st = sensorNames.column("msg").asStringColumn();
            DateColumn dt = sensorNames.dateColumn("date"); // TODO: combine date column with time column for datetime
            tm.put(dt.asList(), st.asList()); // TODO: these should not be added as lists, but instead each date value unpacked and mapped to its corresponding sensor value
                                                // then TreeMap can be used instead of HashMap (more efficient)
            timeSeriesPerSensor.put(sensor, tm); // Add the time series data for the current sensor
        }
        System.out.println(timeSeriesPerSensor);
        return timeSeriesPerSensor;
    }
}
