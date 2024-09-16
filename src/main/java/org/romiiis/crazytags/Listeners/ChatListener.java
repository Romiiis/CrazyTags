package org.romiiis.crazytags.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;


/**
 * ChatListener
 * This class is used to listen for chat events and modify the chat format to include the player's prefix
 *
 */
public class ChatListener implements org.bukkit.event.Listener {


    /**
     * Method called when a player sends a message
     * @param event Event
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        // Get the player from the event
        Player player = event.getPlayer();

        // Get the scoreboard
        Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();

        // Get the team of the player
        Team team = scoreboard.getTeam(player.getName());

        // Check if the player has a team (assuming you have already set the prefix)
        if (team != null) {

            // Get the team prefix
            String prefix = team.getPrefix();

            // Modify the chat format to include the prefix
            String format = event.getFormat();

            event.setFormat(ChatColor.translateAlternateColorCodes('&', prefix + format));

        }
    }
}
