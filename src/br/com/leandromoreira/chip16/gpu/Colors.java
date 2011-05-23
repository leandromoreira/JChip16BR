package br.com.leandromoreira.chip16.gpu;

import java.awt.Color;
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
                Integer index = Integer.valueOf(key.substring(key.length()-1,key.length()), 16);
                colors.put(index, new Color(getR(colorRGB), getG(colorRGB), getB(colorRGB)));
            }
        } catch (Exception e) {
            //todo: offer default colors
        }
    }
    private static int getR(final String rgb) {
        return Integer.valueOf(rgb.substring(rgb.length()-6, rgb.length()-4),16);
    }

    private static int getG(final String rgb) {
        return Integer.valueOf(rgb.substring(rgb.length()-4, rgb.length()-2),16);
    }

    private static int getB(final String rgb) {
        return Integer.valueOf(rgb.substring(rgb.length()-2, rgb.length()),16);
    }
    
    public static Color getColor(final int index) {
        return colors.get(index);
    }
}