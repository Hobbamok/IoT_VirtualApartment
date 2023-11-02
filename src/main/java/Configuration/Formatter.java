package Configuration;

import java.lang.reflect.Array;
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
        return  concatWithCopy2(key.toString().getBytes(),value.getBytes());//todo check if the toString delivers the right info
    }

    static <T> T concatWithCopy2(T array1, T array2) {
        if (!array1.getClass().isArray() || !array2.getClass().isArray()) {
            throw new IllegalArgumentException("Only arrays are accepted.");
        }

        Class<?> compType1 = array1.getClass().getComponentType();
        Class<?> compType2 = array2.getClass().getComponentType();

        if (!compType1.equals(compType2)) {
            throw new IllegalArgumentException("Two arrays have different types.");
        }

        int len1 = Array.getLength(array1);
        int len2 = Array.getLength(array2);

        @SuppressWarnings("unchecked")
        //the cast is safe due to the previous checks
        T result = (T) Array.newInstance(compType1, len1 + len2);

        System.arraycopy(array1, 0, result, 0, len1);
        System.arraycopy(array2, 0, result, len1, len2);

        return result;
    }
}
