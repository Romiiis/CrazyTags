package org.romiiis.crazytags.Database.MySQL;

import org.romiiis.crazytags.Config.CustomConfig;
import org.romiiis.crazytags.Database.Database;
import org.romiiis.crazytags.CrazyTags;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class MySQL extends Database {

    private Connection connection;

    /***
     * Constructor
     * @param instance Instance of the plugin
     */
    public MySQL(CrazyTags instance) {
        super(instance);
    }

    @Override
    public Connection getSQLConnection() {

        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            connection = DriverManager.getConnection("jdbc:mysql://" + CustomConfig.getInstance().getDatabase().get("host") + ":" + CustomConfig.getInstance().getDatabase().get("port") + "/" + CustomConfig.getInstance().getDatabase().get("database"), CustomConfig.getInstance().getDatabase().get("username"), CustomConfig.getInstance().getDatabase().get("password"));
            return connection;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void load() {

        // Load database

        Map<String,String> database = CustomConfig.getInstance().getDatabase();

        try {

            connection = DriverManager.getConnection("jdbc:mysql://" + database.get("host") + ":" + database.get("port") + "/" + database.get("database"), database.get("username"), database.get("password"));

            // If the table doesn't exist create it
            Statement s = connection.createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
                    "  `uuid_player` VARCHAR(255) NOT NULL," +
                    "  `id_tag` VARCHAR(255) NOT NULL)");

            s.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not connect to MySQL database");

        }

        initialize();

    }
}
