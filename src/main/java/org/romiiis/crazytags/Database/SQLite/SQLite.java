package org.romiiis.crazytags.Database.SQLite;

import org.romiiis.crazytags.Database.Database;
import org.romiiis.crazytags.CrazyTags;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;


/**
 * SQLite
 * This class is used to manage the SQLite database
 */
public class SQLite extends Database {


    /***
     * Constructor
     * @param instance Instance of the plugin
     */
    public SQLite(CrazyTags instance){
        super(instance);
    }


    /**
     * Get the connection
     * @return Connection
     */
    public Connection getSQLConnection() {

        // Open database
        File dataFolder = new File(plugin.getDataFolder(), "database.db");

        // If the file doesn't exist
        if (!dataFolder.exists()){
            try {
                // Create the file
                dataFolder.createNewFile();

            } catch (IOException e) {
                // Error creating file
                plugin.getLogger().log(Level.SEVERE, "File write error: database.db");
            }
        }


        try {

            // If the connection is not null and is not closed
            if(connection!=null&&!connection.isClosed()){

                return connection;

            }

            // Load the SQLite JBDC library
            Class.forName("org.sqlite.JDBC");

            // Create a new connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);

            return connection;

        }
        // Catch exceptions
        catch (SQLException ex) {
            // Log the exception
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);

        }
        // Catch class not found exception
        catch (ClassNotFoundException ex) {

            // Log the exception
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }


    /**
     * Load and initialize the database
     */
    public void load() {

        // Get the connection
        connection = getSQLConnection();

        try {

            // If the table doesn't exist create it
            Statement s = connection.createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
                    "  `uuid_player` VARCHAR(255) NOT NULL," +
                    "  `id_tag` VARCHAR(255) NOT NULL)");

            s.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }
        initialize();
    }
}
