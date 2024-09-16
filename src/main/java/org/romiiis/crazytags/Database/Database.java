package org.romiiis.crazytags.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.romiiis.crazytags.Config.CustomConfig;
import org.romiiis.crazytags.Database.SQLite.Error;
import org.romiiis.crazytags.Database.SQLite.Errors;
import org.romiiis.crazytags.CrazyTags;


public abstract class Database {

    /** Instance of the plugin */
    protected CrazyTags plugin;

    /** Connection */
    protected Connection connection;

    /** Table */
    protected String table = "supertags";


    /***
     * Constructor
     * @param instance Instance of the plugin
     */
    public Database(CrazyTags instance){
        plugin = instance;
    }


    /**
     * Get the connection
     * @return Connection
     */
    public abstract Connection getSQLConnection();


    /**
     * Load the database
     */
    public abstract void load();


    /**
     * Initialize the database
     */
    public void initialize(){

        // Get the connection
        connection = getSQLConnection();

        System.out.println(connection);

        try{
            // Try to select all from the table
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // Close the connection
            close(ps,rs);

            // Remove records from table if tag doesn't exist anymore
            initTags();

        } catch (SQLException ex) {

            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
        }
    }


    /**
     * Initialize the tags
     * Remove records from table if tag doesn't exist anymore
     *
     */
    private void initTags(){

        // Initialize the connection
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {

            // Remove records from table if tag doesn't exist anymore
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table);
            rs = ps.executeQuery();
            while (rs.next()) {
                String tagId = rs.getString("id_tag");
                if(!CustomConfig.getInstance().getTagIDs().contains(tagId)){
                    ps = conn.prepareStatement("DELETE FROM " + table + " WHERE id_tag = ?");
                    ps.setString(1, tagId);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(ps,rs);

        }


    }







    /**-----------------------------------------------------**/
    /**--------------------SQL QUERIES-----------------------**/
    /**-----------------------------------------------------**/


    /**
     * When a player buys a tag it is added to the database
     * @param player Player
     * @param tagId Tag ID
     */
    public void buyTag(Player player, String tagId){

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("INSERT INTO " + table + " (uuid_player,id_tag) VALUES(?,?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, tagId);
            ps.executeUpdate();
            return;

        } catch (SQLException ex) {

            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);

        } finally {

            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;

    }


    /**
     * Get the tags bought by a player
     * @param player Player
     * @return List of tags
     */
    public List<String> getBoughtTags(Player player) {

        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<String> tags = new ArrayList<>();

        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE uuid_player = ?");
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();

            while (rs.next()) {

                tags.add(rs.getString("id_tag"));

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tags;
    }


        /**
     * Close the connection
     * @param ps prepared statement
     * @param rs result set
     */
    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }
}