package net.poweredbyhate.gender;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Lax on 7/20/2017.
 */
public class MentalIllness {

    private GenderPlugin plugin;
    private HashMap<String, Gender> mentalillness;

    public MentalIllness(GenderPlugin plugin) {
        this.plugin = plugin;
        mentalillness = new HashMap<>();
    }

    public void imagine(Gender g) {
        mentalillness.put(g.getName().toLowerCase(), g);
    }

    public Gender getGender(String s) {
        return mentalillness.get(s.toLowerCase());
    }

    public Collection<Gender> getGenders() {
        return mentalillness.values();
    }

    public HashMap getDatabase() {
        return mentalillness;
    }

    private String getMentalIllness(UUID player) {
        String g = getConfig().getString(player.toString());
        if (g == null) {
            return "???";
        }
        return g.replace("_", " ");
    }

    public String getPlayerGender(Player player) {
        return getMentalIllness(player.getUniqueId());
    }

    public String getPlayerGender(OfflinePlayer player) {
        return getMentalIllness(player.getUniqueId());
    }

    @Deprecated
    public String getPlayerGender(String player) {
        return getPlayerGender(Bukkit.getOfflinePlayer(player));
    }

    public void setPlayerGender(Player p, String gender) {
        setConfig(p.getUniqueId(), gender);
    }

    public void setConfig(Object key, String payload) {
        GenderPlugin.instance.getConfig().set(key.toString(), StringUtils.capitalize(payload));
        GenderPlugin.instance.saveConfig();
    }

    public FileConfiguration getConfig() {
        return GenderPlugin.instance.getConfig();
    }

    public void sendNonGenderNeutralMessage(Object obj, String message) {
        if (obj instanceof Player) {
            ((Player) obj).sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }
        if (obj instanceof CommandSender) {
            ((CommandSender) obj).sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}
