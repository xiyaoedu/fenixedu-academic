package net.sourceforge.fenixedu.renderers.utils;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

public class RenderMode {
    private static Map<String, RenderMode> modes = new Hashtable<String, RenderMode>();

    static {        
        addMode("output");
        addMode("input");
    }
    
    /**
     * @throws NullPointerException if name is null
     */
    public static RenderMode getMode(String name) {
        return RenderMode.modes.get(name.toLowerCase());
    }
    
    public static void addMode(String name) {
        RenderMode.modes.put(name, new RenderMode());
    }
    
    public static Collection<RenderMode> getAllModes() {
        return RenderMode.modes.values();
    }
}
