package Datasource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FilesManager {
    //the setup below allows changing the data directory dynamically.
    private static Path dataDirectory = Paths.get(System.getProperty("user.dir"), "data");

    public static void setDataDirectory(Path dataDirectory) {
        FilesManager.dataDirectory = dataDirectory;
    }

    public static String getDataDirectory() {
        return dataDirectory.toString();
    }

    public  static String getDataDirectoryWithAddition(String addition) {
        return getDataDirectory() +"/" + addition;
    }

    public static ArrayList<String> getAllFilesWithEnding(String ending, boolean isRecursive){
        ArrayList<String> scenarios = new ArrayList<>();
        File folder = new File(getDataDirectory());
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if(file.getName().endsWith("." +ending)){
                    scenarios.add(file.getPath());
                }else{
                    if(isRecursive && file.isDirectory()){
                        scenarios.addAll(getAllFilesWithEnding(ending, true));
                    }
                }
            }
        }
        return scenarios;
    }
}
