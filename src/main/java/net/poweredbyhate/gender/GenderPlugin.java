package net.poweredbyhate.gender;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandReplacements;
import com.cloutteam.samjakob.gui.types.PaginatedGUI;
import net.poweredbyhate.gender.commands.CommandGender;
import net.poweredbyhate.gender.hospital.Flat;
import net.poweredbyhate.gender.hospital.Sql;
import net.poweredbyhate.gender.listeners.ChatListener;
import net.poweredbyhate.gender.listeners.PlaceholderListener;
import net.poweredbyhate.gender.listeners.PlayerListener;
import net.poweredbyhate.gender.special.Asylum;
import net.poweredbyhate.gender.utilities.DatabaseManager;
import net.poweredbyhate.gender.utilities.Messenger;
import net.poweredbyhate.gender.utilities.Settings;
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
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

/**
 * Created by Lax on 12/15/2016.
 *
 * Charge for setting gender
 */
public class GenderPlugin extends JavaPlugin {

    public static GenderPlugin instance;
    private MentalIllness mentalIllness;
    private DatabaseManager databaseManager;
    private Metrics metrics;
    private Asylum asylum;

    public void onEnable() {
        saveDefaultConfig();
        saveResources();
        instance = this;
        makeDatabase();
        mentalIllness = new MentalIllness(this, asylum);
        metrics = new Metrics(this);
        PaginatedGUI.prepare(this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderListener(this, "gender").hook();
        }
        registerListeners();
        registerCommand();
        updateCheck();
        asylum.loadGenders();
        loadCustomChart();
        loadMessagesCache();
    }

    public void onDisable() {
        databaseManager.unmake();
    }

    public void updateCheck() {
        try {
            new SpigotUpdater(this, 33217);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerCommand() {
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        CommandReplacements replacements = commandManager.getCommandReplacements();
        String s = Settings.getConfig().getString("baseCommand", "gender");
        getLogger().log(Level.INFO, "[ACF] Commands: " + s);
        replacements.addReplacement("gender", s);
        commandManager.registerCommand(new CommandGender(this));
        commandManager.enableUnstableAPI("help");
    }

    public void makeDatabase() {
        if (Settings.getConfig().getBoolean("database.enabled", false)) {
            try {
                getLogger().log(Level.INFO, "[Backend] SQL");
                String host = Settings.getConfig().getString("database.address");
                String user = Settings.getConfig().getString("database.username");
                String pass = Settings.getConfig().getString("database.password");
                String database = Settings.getConfig().getString("database.database");
                //debug("Database", host, user, pass, database);
                databaseManager = new DatabaseManager(this, host, user, pass, database);
                asylum = new Sql(this);
            } catch (Exception e) {
                getLogger().log(Level.WARNING, "[Backend] File ");
                asylum = new Flat(this);
                e.printStackTrace();
            }
        } else {
            getLogger().log(Level.INFO, "[Backend] File");
            asylum = new Flat(this);
        }
    }

    public void saveResources() {
        saveResource("CustomGenders.yml", false);
        saveResource("messages.yml", false);
        saveResource("settings.yml", false);
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public void loadCustomChart() {
        getLogger().log(Level.INFO,"Loading: Custom Charts");
        metrics.addCustomChart(new Metrics.AdvancedPie("le_popular_genders", () -> {
            Map<String, Integer> genderCount = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
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

    public Asylum getAsylum() {
        return asylum;
    }

    public void loadMessagesCache() {
        File messagesFile = new File(getDataFolder(), "messages.yml");
        FileConfiguration c = new YamlConfiguration();
        try {
            c.load(messagesFile);
            for (String key : c.getKeys(false)) {
                Messenger.messagesCache.put(key, c.getString(key));
            }
            //Load missing messages
            c.load(new BufferedReader(new InputStreamReader(getClassLoader().getResourceAsStream("messages.yml"))));
            for (String key : c.getKeys(false)) {
                if (!Messenger.messagesCache.containsKey(key)) {
                    Messenger.messagesCache.put(key, c.getString(key));
                }
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

    public void debug(Object... o) {
        getLogger().log(Level.INFO, Arrays.toString(o));
    }

}
