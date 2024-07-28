package context;

import utils.config.ConfigReader;
import utils.file.FileHandler;
import utils.folder.FolderHandler;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class AppConfigContext {
    private final Map<String, String> configContext;
    private static AppConfigContext appConfigContext;

    private AppConfigContext() throws IOException {
        File appConfigFile = FileHandler.getFileFrom(FolderHandler.getResourceFolder(), "app.config");
        configContext = ConfigReader.getConfigFrom(appConfigFile);
    }

    public static AppConfigContext context() throws IOException{
        if(appConfigContext == null)
            appConfigContext = new AppConfigContext();
        return appConfigContext;
    }

    public String getValue(String key){
        if(!configContext.containsKey(key))
            throw new ConfigException("No such key: "+key);
        return configContext.get(key);
    }
}
