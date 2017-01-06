package net.poweredbyhate.gender;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Lax on 12/16/2016.
 */
public class MentalIllness {

    private String getMentalIllness(UUID player) {
        String g = getConfig().getString(player.toString());
        if (g == null) {
            return "???";
        }
        return g.replace("_", " ");
    }

    public String getGender(Player player) {
        return getMentalIllness(player.getUniqueId());
    }

    public String getGender(OfflinePlayer player) {
        return getMentalIllness(player.getUniqueId());
    }

    @Deprecated
    public String getGender(String player) {
        return getGender(Bukkit.getOfflinePlayer(player));
    }

    public String getGenderInfo(String gender) {
        if (!Gender.instance.genderList.containsKey(gender.toLowerCase())) {
            return "Gender does not exist";
        }

        return getDatabase().get(gender).toString();
    }

    public int getDatabaseSize() {
        return Gender.instance.genderList.size();
    }

    public HashMap getDatabase() {
        return Gender.instance.genderList;
    }

    public void setGender(Player p, String gender) {
        setConfig(p.getUniqueId(), gender);
    }

    public void setConfig(Object key, String payload) {
        Gender.instance.getConfig().set(key.toString(), StringUtils.capitalize(payload));
        Gender.instance.saveConfig();
    }

    public FileConfiguration getConfig() {
        return Gender.instance.getConfig();
    }

    public void sendGenderNeutralMessage(Object obj, String message) {
        if (obj instanceof Player) {
            ((Player) obj).sendMessage(ChatColor.RESET + message);
            return;
        }
        if (obj instanceof CommandSender) {
            ((CommandSender) obj).sendMessage(ChatColor.RESET + message);
        }
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
