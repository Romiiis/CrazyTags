package org.romiiis.crazytags.CMD;

import org.bukkit.entity.Player;
import org.romiiis.crazytags.Objects.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class that represents subcomand clean
 * It will clean current equipped tag
 */
public class cmd_clean implements SubCommand{



    @Override
    public String getName() {
        return "clean";
    }

    @Override
    public void execute(Player player, String[] args) {
        Tag.clear(player);
    }

    @Override
    public String getHelp() {
        String result = "";
        String help = "Cleans all tags from the player";

        result += "§6/st " + getName() + "§r: " + help;

        return result;
    }

    @Override
    public List<String> getPermissions() {

        List<String> perms = new ArrayList<>();
        perms.add("clean");
        perms.add("user");
        perms.add("admin");
        perms.add("*");

        return perms;
    }
}
