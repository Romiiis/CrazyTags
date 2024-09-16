package org.romiiis.crazytags.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.romiiis.crazytags.Config.CustomConfig;
import org.romiiis.crazytags.CrazyTags;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the inventories
 *
 * @author Romiiis
 * @version 1.0
 */
public class TagInv implements Listener {


    /**
     * Singleton instance
     */
    private static final TagInv instance = new TagInv();


    /**
     * Inventories
     */
    private List<Inventory> inventories;

    /**
     * Current page of player
     */
    private int currentPage = 0;

    /**
     * Slots
     */
    private final int nextButtonSlot = 53;
    private final int backButtonSlot = 45;


    /**
     * Private constructor
     */
    private TagInv() { }


    /**
     * Get singleton instance
     */
    public static TagInv getInstance() {
        return instance;
    }


    /**
     * This method is called when player executes /st open
     *
     * @param player Player
     */
    public void openInventory(Player player) {

        // Initialize inventories
        inventories = new ArrayList<>();

        String equippedTag = Tag.getEquippedTag(player);
        initInventories(equippedTag, player);

        // Open inventory
        player.openInventory(inventories.get(0));


        // Play sound to player (successfull hit)
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);


    }


    /**
     * This method initializes inventories
     *
     * @param equippedTag Equipped tag
     * @param player      Player
     */
    private void initInventories(String equippedTag, Player player) {

        // Items per page
        int itemsPerPage = 28;

        // Calculate pages count
        int pagesCount = (int) Math.ceil(CustomConfig.getInstance().getTagsList().size() / (double) itemsPerPage);

        // Set current page to 0
        currentPage = 0;

        // Create inventories
        for (int i = 0; i < pagesCount; i++) {

            // Add inventory to list
            inventories.add(Bukkit.createInventory(null, 54, "§6§lSuperTags §r§7(" + (i + 1) + "/" + pagesCount + ")"));

            // Fillers
            fillInv(inventories.get(i), player);

            // Get bought tags
            List<String> boughtTags = CrazyTags.getInstance().getDatabase().getBoughtTags(player);

            // Fill page with tags
            for (int j = 0; j < itemsPerPage; j++) {

                // Calculate index
                int index = i * itemsPerPage + j;

                // Check if index is out of bounds
                if (index >= CustomConfig.getInstance().getTagsList().size()) {
                    break;
                }

                // Check permissions
                if (!player.hasPermission("supertags.tag." + CustomConfig.getInstance().getTagsList().get(index).getId())) {

                    // Check if player has bought the tag
                    if (!boughtTags.contains(CustomConfig.getInstance().getTagsList().get(index).getId())) {
                        continue;
                    }

                }
                // Set item
                inventories.get(i).addItem(CustomConfig.getInstance().getTagsList().get(index).getItem(boughtTags.contains(CustomConfig.getInstance().getTagsList().get(index).getId())));

            }

            // Add navigation buttons
            addNavigationButtons(i, pagesCount, equippedTag);

        }

    }


    /**
     * This method fills inventory with fillers and money paper
     *
     * @param inv Inventory
     */
    private void fillInv(Inventory inv, Player player) {

        // Green and pink glass panes
        ItemStack green = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = green.getItemMeta();
        fillerMeta.setDisplayName("§r");
        green.setItemMeta(fillerMeta);

        ItemStack pink = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
        ItemMeta pinkMeta = pink.getItemMeta();
        pinkMeta.setDisplayName("§r");
        pink.setItemMeta(pinkMeta);


        // Fill inventory
        for (int i = 0; i < 4; i++) {

            if (i % 2 == 1) {
                inv.setItem(i, pink);
            } else {
                inv.setItem(i, green);
            }
        }

        // Paper with money
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();

        // Get player money
        double money = CrazyTags.getInstance().getEconomy().getBalance(player);

        // Format money
        DecimalFormat df = new DecimalFormat("#.#");
        String formatted = df.format(money);

        // Set display name
        paperMeta.setDisplayName("§4Money: §c" + formatted + "$");
        paper.setItemMeta(paperMeta);

        // Set item
        inv.setItem(4, paper);


        for (int i = 5; i < 9; i++) {

            if (i % 2 == 1) {
                inv.setItem(i, pink);
            } else {
                inv.setItem(i, green);
            }
        }

        for (int i = 9; i < 45; i += 9) {

            if (i % 2 == 1) {
                inv.setItem(i, pink);
                inv.setItem(i + 8, pink);
            } else {
                inv.setItem(i, green);
                inv.setItem(i + 8, green);

            }
        }

        for (int i = inv.getSize() - 9; i < inv.getSize(); i++) {

            if (i % 2 == 1) {
                inv.setItem(i, pink);
            } else {
                inv.setItem(i, green);
            }
        }

    }


    /**
     * This method adds navigation buttons to inventory
     *
     * @param i          Index of inventory
     * @param pagesCount Pages count
     */
    private void addNavigationButtons(int i, int pagesCount, String equippedTag) {

        // Create items
        ItemStack next = new ItemStack(Material.ARROW);
        ItemStack back = new ItemStack(Material.ARROW);
        ItemStack nothing = new ItemStack(Material.BARRIER);
        ItemStack clean = new ItemStack(Material.TNT);

        // Create item meta
        ItemMeta nextMeta = next.getItemMeta();
        ItemMeta backMeta = back.getItemMeta();
        ItemMeta nothingMeta = nothing.getItemMeta();
        ItemMeta cleanMeta = clean.getItemMeta();

        // Set display name
        nextMeta.setDisplayName("§aNext page");
        backMeta.setDisplayName("§aPrevious page");
        nothingMeta.setDisplayName("§cX");
        cleanMeta.setDisplayName("§cClean equipped tag");

        // Set lore
        List<String> lore = new ArrayList<>();
        if (equippedTag == null) {

            lore.add("§6You have equipped a tag : §cNone");

        } else {

            lore.add("§6You have equipped a tag : " + equippedTag);

        }


        lore.add("§7Click to clean your equipped tag");
        cleanMeta.setLore(lore);


        // Set item meta
        next.setItemMeta(nextMeta);
        back.setItemMeta(backMeta);
        nothing.setItemMeta(nothingMeta);
        clean.setItemMeta(cleanMeta);

        // Set items
        inventories.get(i).setItem(49, clean);

        // Check if there is only one page
        if (i == 0 && pagesCount == 1) {
            inventories.get(i).setItem(backButtonSlot, nothing);
            inventories.get(i).setItem(nextButtonSlot, nothing);


        }
        // Check if there is only one page and it is the first one
        else if (i == 0) {
            inventories.get(i).setItem(backButtonSlot, nothing);
            inventories.get(i).setItem(nextButtonSlot, next);
        }
        // Check if it is the last one
        else if (i == pagesCount - 1) {
            inventories.get(i).setItem(backButtonSlot, back);
            inventories.get(i).setItem(nextButtonSlot, nothing);
        }
        // Check if its middle page
        else {
            inventories.get(i).setItem(backButtonSlot, back);
            inventories.get(i).setItem(nextButtonSlot, next);
        }
    }


    /**
     * This method is called when player clicks in inventory
     *
     * @param event InventoryClickEvent
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        // Check if the clicked inventory is the one you are interested in
        if (event.getInventory().equals(inventories.get(currentPage))) {

            try {

                // Get clicked item
                ItemStack clickedItem = event.getCurrentItem();

                // Check if clicked item is arrow button (back)
                if (clickedItem != null && clickedItem.getType().equals(Material.ARROW) && event.getSlot() == backButtonSlot) {

                    currentPage--;
                    event.getWhoClicked().openInventory(inventories.get(currentPage));
                    event.setCancelled(true);
                    return;
                }

                // Check if clicked item is arrow button (next)
                if (clickedItem != null && clickedItem.getType().equals(Material.ARROW) && event.getSlot() == nextButtonSlot) {

                    currentPage++;
                    event.getWhoClicked().openInventory(inventories.get(currentPage));
                    event.setCancelled(true);
                    return;
                }

                // Check if clicked item is clean button
                if (clickedItem != null && clickedItem.getType().equals(Material.TNT) && event.getSlot() == 49) {

                    Tag.clear((Player) event.getWhoClicked());
                    event.getWhoClicked().closeInventory();
                    event.setCancelled(true);
                    return;
                }


                // Get tags list
                List<Tag> tags = CustomConfig.getInstance().getTagsList();

                // Iterate through tags
                for (Tag tag : tags) {

                    // Check if clicked item is tag and if it is bought
                    if (clickedItem != null && clickedItem.equals(tag.getItem(true))) {

                        // Equip tag
                        tag.click((Player) event.getWhoClicked());

                        // Reopen inventory
                        openInventory((Player) event.getWhoClicked());

                        break;
                    }

                    // Check if clicked item is tag and if it is not bought
                    if (clickedItem != null && clickedItem.equals(tag.getItem(false))) {

                        // Buy tag
                        tag.buy((Player) event.getWhoClicked());

                        // reopen inventory
                        openInventory((Player) event.getWhoClicked());
                        break;
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
            }


            // Cancel the event
            event.setCancelled(true);
        }


    }


}
