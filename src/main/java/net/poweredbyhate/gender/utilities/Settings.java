package net.poweredbyhate.gender.utilities;

import net.poweredbyhate.gender.GenderPlugin;
import net.poweredbyhate.gender.special.Gender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Settings {

    public static FileConfiguration getConfig() {
        File settingsFile = new File(GenderPlugin.instance.getDataFolder(), "settings.yml");
        FileConfiguration c = new YamlConfiguration();
        try {
            c.load(settingsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return c;
    }
}
