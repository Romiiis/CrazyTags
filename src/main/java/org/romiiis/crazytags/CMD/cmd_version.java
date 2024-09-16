package org.romiiis.crazytags.CMD;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class cmd_version implements SubCommand {


    @Override
    public String getName() {
        return "version";
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(ChatColor.GRAY + "|--------------------------------|");
        player.sendMessage("SuperTags v1.0.0");
        player.sendMessage(ChatColor.GRAY + "|--------------------------------|");

        //Print all teams for debugging
        player.getScoreboard().getTeams().forEach(team -> player.sendMessage(team.getName()));


    }

    @Override
    public String getHelp() {
        String result = "";
        String help = "Displays the current version of SuperTags";

        result += "§6/st " + getName() + "§r: " + help;

        return result;
    }


    @Override
    public List<String> getPermissions() {
        List<String> perms = new ArrayList<>();
        perms.add("version");
        perms.add("user");
        perms.add("admin");
        perms.add("*");
        return perms;
    }



}
