package org.romiiis.crazytags.Objects;


import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.romiiis.crazytags.CrazyTags;

import java.util.ArrayList;
import java.util.List;

public class Tag {

    private final String id;
    private final String Name;

    private final String description;
    private final String prefix;

    private double price;

    private ItemStack item;

    private ItemStack lockedItem;




    public Tag(String id, String Name, String description, String prefix, double price) {
        this.id = id;
        this.Name = Name;
        this.description = description;
        this.prefix = prefix;
        this.price = price;

        createItemStack();
        createLockedItemStack();

    }

    public static String getEquippedTag(Player player) {

            Scoreboard scoreboard = player.getScoreboard();

            // Check if player has a team
            if (scoreboard.getTeam(player.getName()) == null) {
                return null;
            }

            // Get the team prefix
            String prefix = scoreboard.getTeam(player.getName()).getPrefix();

            return prefix;


    }


    private void createItemStack(){

        item = new ItemStack(Material.NAME_TAG);

        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(Name);

        List<String> lore = new ArrayList<>();
        lore.add(description);
        lore.add(prefix);
        lore.add("");
        lore.add("Click to equip this tag");
        meta.setLore(lore);

        item.setItemMeta(meta);

    }

    private void createLockedItemStack(){

        lockedItem = new ItemStack(Material.LIGHT_GRAY_DYE);

        ItemMeta meta = lockedItem.getItemMeta();

        assert meta != null;
        meta.setDisplayName(Name);

        List<String> lore = new ArrayList<>();
        lore.add(description);
        lore.add(prefix);
        lore.add("");
        lore.add(ChatColor.GRAY + "Price: " + ChatColor.GREEN + price + "$");
        meta.setLore(lore);

        lockedItem.setItemMeta(meta);

    }


    public void click(Player player){

        // Get if essentials is installed

        player.sendMessage("You have equipped the tag " + prefix);
        player.playSound(player.getLocation(), "entity.player.levelup", 1, 1);


            Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
            Team t = sb.getTeam(player.getName());
            if (t == null) {
                t = sb.registerNewTeam(player.getName());
            }

            t.setPrefix(prefix + " ");

            if(!t.hasEntry(player.getName())) {
                t.addEntry(player.getName());
            }



    }


    public static void clear(Player player){

        // Remove player from all teams
        Scoreboard scoreboard = player.getScoreboard();

        // Check if player has a team
        if (scoreboard.getTeam(player.getName()) == null) {
            player.sendMessage(ChatColor.RED + "You don't have equipped tags");
            return;
        }

        // Remove scoreboard team with player's name
        scoreboard.getTeam(player.getName()).unregister();

        // Write to player
        player.sendMessage(ChatColor.GREEN + "Your tags have been cleaned");

        // Play sound
        player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
    }

    /** GETTERS **/

    public String getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrefix() {
        return prefix;
    }

    public ItemStack getItem(boolean bought) {

        if (bought) {
            return item;
        } else {
            return lockedItem;
        }

    }





    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", description='" + description + '\'' +
                ", prefix='" + prefix + '\'' +
                '}';
    }


    public void buy(Player whoClicked) {

            // Get if essentials is installed

            Economy economy = CrazyTags.getInstance().getEconomy();

            EconomyResponse r = economy.withdrawPlayer(whoClicked, price);
            if(!r.transactionSuccess()) {
                whoClicked.sendMessage(ChatColor.RED + "You don't have enough money to buy this tag");
                return;
            }

            CrazyTags.getInstance().getDatabase().buyTag(whoClicked, id);

            whoClicked.sendMessage(ChatColor.GREEN + "You have bought the tag " + prefix);
            whoClicked.playSound(whoClicked.getLocation(), "entity.player.levelup", 1, 1);


    }
}
