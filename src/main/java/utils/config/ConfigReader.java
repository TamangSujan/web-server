package utils.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConfigReader {
    private ConfigReader(){}
    public static Map<String, String> getConfigFrom(File file) throws IOException{
        List<String> configLines = getAllConfigLines(file);
        return getConfigMapFromConfigList(configLines);
    }

    private static List<String> getAllConfigLines(File file) throws IOException {
        List<String> lines = new LinkedList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line = "";
            while((line = reader.readLine())!=null){
                if(line.contains("="))
                    lines.add(line);
            }
        }
        return lines;
    }

    private static Map<String, String> getConfigMapFromConfigList(List<String> configLines){
        Map<String, String> configMap = new HashMap<>();
        for(String configLine: configLines){
            addConfigKeyValueInMap(configMap, configLine);
        }
        return configMap;
    }

    private static void addConfigKeyValueInMap(Map<String, String> configMap, String configLine){
        int equalIndex = configLine.indexOf("=");
        configMap.put(
                configLine.substring(0, equalIndex),
                configLine.substring(equalIndex + 1)
                );
    }
}
