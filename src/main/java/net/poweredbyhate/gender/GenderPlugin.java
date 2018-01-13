package net.poweredbyhate.gender;

import co.aikar.commands.BukkitCommandManager;
import com.cloutteam.samjakob.gui.types.PaginatedGUI;
import net.poweredbyhate.gender.commands.CommandGender;
import net.poweredbyhate.gender.listeners.ChatListener;
import net.poweredbyhate.gender.listeners.PlayerListener;
import net.poweredbyhate.gender.special.Gender;
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
 *
 * Charge for setting gender
 */
public class GenderPlugin extends JavaPlugin {

    public static GenderPlugin instance;
    private MentalIllness mentalIllness;
    private BukkitCommandManager commandManager;
    private Metrics metrics;

    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        commandManager = new BukkitCommandManager(this);
        mentalIllness = new MentalIllness(this);
        metrics = new Metrics(this);
        //getCommand("gender").setExecutor(new GenderCommand(this));
        commandManager.registerCommand(new CommandGender(this));
        commandManager.enableUnstableAPI("help");
        PaginatedGUI.prepare(this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderListener(this, "gender").hook();
        }
        saveResource("CustomGenders.yml", false);
        registerListeners();
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

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public void loadCustomChart() {
        getLogger().log(Level.INFO,"Loading: Custom Charts");
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
            saveFile("ElementsPack.yml");
            saveFile("HogwartsPack.yml");
        }
        try {
            Files.walk(Paths.get(folder.getAbsolutePath())).filter(file -> file.getFileName().toString().endsWith(".yml")).collect(Collectors.toList()).forEach(this::loadData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        goMental().imagine(new Gender(this, "???", "Null gender"));
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
                Gender g = new Gender(this,m,configuration.getString("genders."+m+".description"));
                g.setFromPack(file.getName().split("\\.")[0]);
                goMental().imagine(g);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void saveFile(String name) {
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
