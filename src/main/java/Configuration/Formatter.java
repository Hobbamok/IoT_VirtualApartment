package Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Formatter {
    public ArrayList<byte[]> formatData(LinkedHashMap<LocalDateTime, String> data){
        var list = new ArrayList<byte[]>();
        for (Object key : data.keySet()) {
            list.add(formatDataEntry(key, data.get(key)));
        }
        //todo actual reformatting
        return list;
    }

    private byte[] formatDataEntry(Object key, String value){
        //todo actual reformatting
        return  value.getBytes();
    }
}
