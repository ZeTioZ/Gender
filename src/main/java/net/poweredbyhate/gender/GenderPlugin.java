package net.poweredbyhate.gender;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Lax on 12/15/2016.
 */
public class GenderPlugin extends JavaPlugin {

    public static GenderPlugin instance;
    private MentalIllness mentalIllness;

    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        mentalIllness = new MentalIllness(this);
        getCommand("gender").setExecutor(new GenderCommand(this));
        populateList();
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderListener(this, "gender").hook();
        }
    }

    public MentalIllness goMental() {
        return mentalIllness;
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
            goMental().imagine(new Gender(this,m,getGenderConfig().getString(m+".description")));
        }
        for (String i : getCommunityGenders().getKeys(false)) {
            goMental().imagine(new Gender(this,i, getCommunityGenders().getString(i+".description")));
        }
    }

}
