package br.com.leandromoreira.chip16.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author leandro-rm
 */
public final class ConfigManager {
    private static final ConfigManager config = new ConfigManager();
    private Properties properties;
    public static ConfigManager getConfig() {
        return config;
    }
    private ConfigManager(){
        refreshProperties();
    }

    public void refreshProperties() throws IllegalStateException {
        File file = new File("config/config.properties");
        properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException ex) {
            throw new IllegalStateException("The config/config.properties raise a io or wasn't found!",ex);
        }
    }

    public final String getTitle() {
        return properties.getProperty("name") + " - " + properties.getProperty("version");
    }

    public String getVMHeader() {
        return "<html><FONT COLOR=RED><U>VM Version:</U></FONT> "+properties.getProperty("vhardware.version")+" <FONT COLOR=RED><U>Spec. cast:</U></FONT> "+properties.getProperty("cast.specs")+"</html>";
    }
}
