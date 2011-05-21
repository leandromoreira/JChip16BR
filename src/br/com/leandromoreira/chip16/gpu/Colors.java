package br.com.leandromoreira.chip16.gpu;

import java.io.FileReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author leandro-rm
 */
public final class Colors {

    private static final Map<Integer, Color> colors = new HashMap<Integer, Color>();

    static {
        final Properties palette = new Properties();
        try {
            palette.load(new FileReader("config/palette.properties"));
            final Enumeration e = palette.keys();
            while (e.hasMoreElements()) {
                final String key = e.nextElement().toString();
                final String colorRGB = palette.getProperty(key).split(" ")[0];
                final String colorDescription = palette.getProperty(key).split(" ")[1];
                Integer index = Integer.valueOf(key.substring(key.length()-1,key.length()), 16);
                colors.put(index, new Color(colorDescription, colorRGB,index));
            }
        } catch (Exception e) {
            //todo: offer default colors
        }
    }

    public static Color getColor(final int index) {
        return colors.get(index);
    }
}