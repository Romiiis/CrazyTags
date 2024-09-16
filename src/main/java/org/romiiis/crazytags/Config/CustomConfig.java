package org.romiiis.crazytags.Config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.romiiis.crazytags.CrazyTags;
import org.romiiis.crazytags.Objects.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomConfig {

    private final static CustomConfig instance = new CustomConfig();

    private File file;
    private File tagsFile;
    private YamlConfiguration config;
    private YamlConfiguration tagsConfig;


    private List<Tag> tagsList;

    private List<String> tagIDs;

    private Map<String, String> database;
    private CustomConfig() {
    }

    public void load(){

        // Create file if it doesn't exist
        file = new File(CrazyTags.getInstance().getDataFolder(), "config.yml");

        if(!file.exists()){
            CrazyTags.getInstance().saveResource("config.yml", false);
        }

        // Load config
        config = YamlConfiguration.loadConfiguration(file);
        config.options().parseComments(true);

        try {
            config.load(file);
            replaceColorCodes(config, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadTagsCnf();
        loadDatabase();

    }


    private void loadTagsCnf(){

        // Create file if it doesn't exist
        tagsFile = new File(CrazyTags.getInstance().getDataFolder(), "tags.yml");

        if(!tagsFile.exists()){
            CrazyTags.getInstance().saveResource("tags.yml", false);
        }

        // Load config
        tagsConfig = YamlConfiguration.loadConfiguration(tagsFile);
        tagsConfig.options().parseComments(true);

        try {
            tagsConfig.load(tagsFile);
            replaceColorCodes(tagsConfig, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadTags();

    }

    public void save(){
        try {
            config.save(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CustomConfig getInstance() {
        return instance;
    }


    public void loadTags() {


        tagsList = new ArrayList<>();
        tagIDs = new ArrayList<>();

        ConfigurationSection tagsSection = tagsConfig.getConfigurationSection("Tags");
        if (tagsSection != null) {
            // Iterate through each tag
            for (String tagId : tagsSection.getKeys(false)) {
                ConfigurationSection tag = tagsSection.getConfigurationSection(tagId);

                // Get values for the tag
                String name = tag.getString("name");
                String description = tag.getString("description");
                String prefix = tag.getString("prefix");
                double price = tag.getDouble("price");
                // You can add more keys as needed

                // Create a Tag object and add it to the list
                Tag newTag = new Tag(tagId, name, description, prefix, price);

                tagsList.add(newTag);
                tagIDs.add(tagId);
            }
        }
    }


    public void loadDatabase() {

        database = new HashMap<>();
        ConfigurationSection databaseSection = config.getConfigurationSection("database");
        if (databaseSection != null) {
            for (String key : databaseSection.getKeys(false)) {
                String value = databaseSection.getString(key);
                if (value != null) {
                    database.put(key, value);
                }
            }
        }
    }


    public List<Tag> getTagsList() {
        return tagsList;
    }

    public List<String> getTagIDs() {
        return tagIDs;
    }

    public Map<String, String> getDatabase() {
        return database;
    }

    private void replaceColorCodes(ConfigurationSection section, String path) {
        for (String key : section.getKeys(false)) {
            String fullPath = path.isEmpty() ? key : path + "." + key;

            if (section.isConfigurationSection(key)) {
                // Recursively replace color codes in sub-sections
                replaceColorCodes(section.getConfigurationSection(key), fullPath);
            } else if (section.isString(key)) {
                // Replace color codes in string values
                String value = section.getString(key);
                if (value != null) {
                    value = ChatColor.translateAlternateColorCodes('&', value);
                    section.set(key, value);
                }
            }
        }
    }





}
