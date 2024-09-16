package org.romiiis.crazytags.CMD;

import org.bukkit.entity.Player;

import java.util.List;

public interface SubCommand {

    String getName();

    void execute(Player player, String[] args);

    String getHelp();


    List<String> getPermissions();


}
