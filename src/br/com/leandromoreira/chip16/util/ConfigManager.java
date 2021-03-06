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

    private ConfigManager() {
        refreshProperties();
    }

    public void refreshProperties() throws IllegalStateException {
        File file = new File("config/metainfo.properties");
        properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException ex) {
            throw new IllegalStateException("The config/metainfo.properties raise a io or wasn't found!", ex);
        }
    }

    public final String getTitle() {
        return properties.getProperty("name") + " - " + properties.getProperty("version");
    }

    public String getVMHeader() {
        return "<html><FONT COLOR=RED><U>VM name:</U></FONT> " + properties.getProperty("vm.name") + " <FONT COLOR=RED><U>VM version:</U></FONT> " + properties.getProperty("vhardware.version") + " <FONT COLOR=RED><U>Spec. cast:</U></FONT> " + properties.getProperty("cast.specs") + "</html>";
    }

    public String getLAF() {
        File file = new File("config/emulator.properties");
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(file));
        } catch (IOException ex) {
            throw new IllegalStateException("The config/metainfo.properties raise a io or wasn't found!", ex);
        }
        return p.getProperty("laf");
    }

    public int getScreen() {
        File file = new File("config/video.properties");
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(file));
        } catch (IOException ex) {
            throw new IllegalStateException("The config/metainfo.properties raise a io or wasn't found!", ex);
        }
        return Integer.parseInt(p.getProperty("size"));
    }
}
