package net.poweredbyhate.gender.hospital;

import net.poweredbyhate.gender.GenderPlugin;
import net.poweredbyhate.gender.special.Asylum;
import net.poweredbyhate.gender.special.Gender;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class File implements Asylum {

    GenderPlugin plugin;

    public File(GenderPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadGenders() {
        java.io.File folder = new java.io.File(plugin.getDataFolder(), "genderpacks");
        loadData(Paths.get(new java.io.File(plugin.getDataFolder(), "CustomGenders.yml").getAbsolutePath()), false);
        if (!folder.exists()) {
            folder.mkdir();
            plugin.saveFile("CommunityPack.yml");
            plugin.saveFile("MasterPack.yml");
            plugin.saveFile("BasicPack.yml");
            plugin.saveFile("ElementsPack.yml");
            plugin.saveFile("HogwartsPack.yml");
        }
        try {
            Files.walk(Paths.get(folder.getAbsolutePath())).filter(file -> file.getFileName().toString().endsWith(".yml")).collect(Collectors.toList()).forEach(this::loadData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gender gender = new Gender("", "Null gender", "");
        gender.setPublic(false);
        plugin.goMental().imagine(gender);
    }

    @Override
    public String getGender(Player p) {
        return getConfig().getString(p.getUniqueId().toString());
    }

    @Override
    public String getGender(UUID p) {
        return getConfig().getString(p.toString());
    }

    @Override
    public void setGender(UUID p, Gender gender) {
        setConfig(p, gender.getName());
    }

    @Override
    public void setGender(UUID p, String gender) {
        setConfig(p, gender);
    }

    public void loadData(Path path) {
        loadData(path, true);
    }

    public void loadData(Path path, boolean isPack) {
        java.io.File file = new java.io.File(path.toUri());
        plugin.getLogger().log(Level.INFO, "Loading: " + file.getName());
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
                Gender g = new Gender(m,configuration.getString("genders."+m+".description", m), configuration.getString("genders."+m+".pronoun", ""));
                g.setFromPack(file.getName().split("\\.")[0]);
                g.setPublic(configuration.getBoolean("genders."+m+".public", true));
                plugin.goMental().imagine(g);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void setConfig(Object key, String payload) {
        plugin.getConfig().set(key.toString(), StringUtils.capitalize(payload));
        plugin.saveConfig();
    }

    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
}
