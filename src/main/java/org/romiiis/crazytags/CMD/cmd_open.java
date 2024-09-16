package org.romiiis.crazytags.CMD;

import org.bukkit.entity.Player;
import org.romiiis.crazytags.Objects.TagInv;

import java.util.ArrayList;
import java.util.List;

public class cmd_open implements SubCommand{
    @Override
    public String getName() {
        return "open";
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage("Opening tag menu");
        // Open tag menu
        TagInv.getInstance().openInventory(player);



    }

    @Override
    public String getHelp() {
        String result = "";
        String help = "Opens a tag menu";

        result += "§6/st " + getName() + "§r: " + help;

        return result;
    }

    @Override
    public List<String> getPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add("open");
        permissions.add("user");
        permissions.add("admin");
        permissions.add("*");
        return permissions;
    }
}
