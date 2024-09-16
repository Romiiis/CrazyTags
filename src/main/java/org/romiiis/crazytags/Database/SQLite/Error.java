package org.romiiis.crazytags.Database.SQLite;

import org.romiiis.crazytags.CrazyTags;

import java.util.logging.Level;

public class Error {
    public static void execute(CrazyTags plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(CrazyTags plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
