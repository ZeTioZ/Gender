package net.poweredbyhate.gender;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spigot.SpigotUpdater;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Created by Lax on 12/15/2016.
 */
public class GenderPlugin extends JavaPlugin {

    public static GenderPlugin instance;
    private MentalIllness mentalIllness;
    private Metrics metrics;

    public void onEnable() {
        saveDefaultConfig();
        saveResource("CustomGenders.yml", false);
        instance = this;
        mentalIllness = new MentalIllness(this);
        getCommand("gender").setExecutor(new GenderCommand(this));
        metrics = new Metrics(this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderListener(this, "gender").hook();
        }
        updateCheck();
        loadCustomChart();
        loadFiles();
    }

    public void updateCheck() {
        try {
            new SpigotUpdater(this, 33217);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCustomChart() {
        getLogger().log(Level.INFO,"Loading custom charts");
        metrics.addCustomChart(new Metrics.AdvancedPie("le_popular_genders", () -> {
            Map<String, Integer> genderCount = new HashMap<>();
            for (String s : getConfig().getKeys(false)) {
                String gender = getConfig().getString(s);
                int count = genderCount.getOrDefault(gender, 0);
                genderCount.put(gender, count + 1);
            }
            return genderCount;
        }));
    }

    public MentalIllness goMental() {
        return mentalIllness;
    }

    public void loadFiles() {
        File folder = new File(getDataFolder(), "genderpacks");
        loadData(Paths.get(new File(getDataFolder(), "CustomGenders.yml").getAbsolutePath()), false);
        if (!folder.exists()) {
            folder.mkdir();
            saveFile("CommunityPack.yml");
            saveFile("MasterPack.yml");
            saveFile("BasicPack.yml");
        }
        try {
            Files.walk(Paths.get(folder.getAbsolutePath())).filter(file -> file.getFileName().toString().endsWith(".yml")).collect(Collectors.toList()).forEach(this::loadData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData(Path path) {
        loadData(path, true);
    }

    public void loadData(Path path, boolean isPack) {
        File file = new File(path.toUri());
        getLogger().log(Level.INFO, "Loading: " + file.getName());
        FileConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
            if (configuration.getConfigurationSection("genders") == null) {
                return;
            }
            if (!configuration.getBoolean("pack.enabled") && isPack) {
                return;
            }
            for (String m : configuration.getConfigurationSection("genders").getKeys(false)) {
                goMental().imagine(new Gender(this,m,configuration.getString("genders."+m+".description")));
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveFile(String name) { //should only be called on first startup ¯\_(ツ)_/¯
        File folder = new File(getDataFolder(), "genderpacks");
        File newFile = new File(folder, name);
        FileConfiguration c = new YamlConfiguration();
        try {
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            c.load(new BufferedReader(new InputStreamReader(getClassLoader().getResourceAsStream(name))));
            c.save(newFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

}
