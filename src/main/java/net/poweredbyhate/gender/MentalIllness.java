package net.poweredbyhate.gender;

import net.poweredbyhate.gender.events.GenderChangeEvent;
import net.poweredbyhate.gender.special.Gender;
import net.poweredbyhate.gender.special.Snowflake;
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
    private HashMap<UUID, Snowflake> snowflakes;

    public MentalIllness(GenderPlugin plugin) {
        this.plugin = plugin;
        mentalillness = new HashMap<>();
        snowflakes = new HashMap<>();
    }

    public void registerFlake(Player p) {
        snowflakes.put(p.getUniqueId(), new Snowflake(plugin,p,getPlayerGender(p)));
    }

    public void removeFlake(Player p) {
        snowflakes.remove(p.getUniqueId());
    }

    public Snowflake getSnowflake(Player p) {
        snowflakes.computeIfAbsent(p.getUniqueId(), k -> new Snowflake(plugin, p, getPlayerGender(p)));
        return snowflakes.get(p.getUniqueId());
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
        return g;
    }

    public Gender getPlayerGender(Player player) {
        return getGender(getMentalIllness(player.getUniqueId()));
    }


    public void setPlayerGender(Player p, String gender) {
        GenderChangeEvent genderChangeEvent = new GenderChangeEvent(p, getSnowflake(p).getGender(), getGender(gender));
        Bukkit.getServer().getPluginManager().callEvent(genderChangeEvent);
        getSnowflake(p).setGender(getGender(gender));
        setConfig(p.getUniqueId(), getGender(gender).getName());
    }

    private void setConfig(Object key, String payload) {
        plugin.getConfig().set(key.toString(), StringUtils.capitalize(payload));
        plugin.saveConfig();
    }

    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }

}
