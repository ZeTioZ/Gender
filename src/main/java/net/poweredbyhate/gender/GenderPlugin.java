package net.poweredbyhate.gender;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;

/**
 * Created by Lax on 12/15/2016.
 */
public class GenderPlugin extends JavaPlugin {

    public static GenderPlugin instance;
    private MentalIllness mentalIllness;
    private Metrics metrics;

    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        mentalIllness = new MentalIllness(this);
        getCommand("gender").setExecutor(new GenderCommand(this));
        populateList();
        metrics = new Metrics(this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderListener(this, "gender").hook();
        }
        loadCustomChart();
    }

    public void loadCustomChart() {
        getLogger().log(Level.INFO,"Loading custom charts");
        metrics.addCustomChart(new Metrics.AdvancedPie("le_popular_genders", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> genderCount = new HashMap<>();
                for (String s : getConfig().getKeys(false)) {
                    String gender = getConfig().getString(s);
                    int count = genderCount.containsKey(gender) ? genderCount.get(gender) : 0;
                    genderCount.put(gender, count + 1);
                }
                return genderCount;
            }
        }));
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
