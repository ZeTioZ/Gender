package net.poweredbyhate.gender;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Lax on 12/15/2016.
 */
public class Gender extends JavaPlugin {

    public HashMap genderList = new HashMap<String, String>();
    public static Gender instance;
    public MentalIllness mentalIllness = new MentalIllness();

    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        populateList();
        getCommand("gender").setExecutor(new GenderCommand());
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderListener(this, "gender").hook();
        }
    }

    public FileConfiguration getGenderConfig() {
        try {
            FileConfiguration genderConfig = new YamlConfiguration();
            genderConfig.load(new BufferedReader(new InputStreamReader(getClassLoader().getResourceAsStream("genders.yml"))));
            return genderConfig;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FileConfiguration getCommunityGenders() {
        try {
            FileConfiguration communityGenders = new YamlConfiguration();
            communityGenders.load(new BufferedReader(new InputStreamReader(getClassLoader().getResourceAsStream("cummunityGenders.yml"))));
            return communityGenders;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void populateList() {
        for (String m : getGenderConfig().getKeys(false)) {
            genderList.put(m.toLowerCase(), getGenderConfig().getString(m+".description"));
        }
        for (String i : getCommunityGenders().getKeys(false)) {
            genderList.put(i.toLowerCase(), getCommunityGenders().getString(i+".description"));
        }
    }

}
