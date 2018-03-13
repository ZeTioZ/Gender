package net.poweredbyhate.gender;

import net.poweredbyhate.gender.events.GenderChangeEvent;
import net.poweredbyhate.gender.special.Asylum;
import net.poweredbyhate.gender.special.Gender;
import net.poweredbyhate.gender.special.Snowflake;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Lax on 7/20/2017.
 */
public class MentalIllness {

    private GenderPlugin plugin;
    private Asylum asylum;
    private HashMap<String, Gender> mentalillness;
    private HashMap<UUID, Snowflake> snowflakes;

    public MentalIllness(GenderPlugin plugin, Asylum asylum) {
        this.plugin = plugin;
        this.asylum = asylum;
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
        String g = asylum.getGender(player);
        if (g == null) {
            return "";
        }
        return g;
    }

    public Gender getPlayerGender(Player player) {
        Gender g = getGender(getMentalIllness(player.getUniqueId()));
        if (g == null) {
            return getGender("");
        }
        return g;
    }


    public void setPlayerGender(Player p, String gender) {
        GenderChangeEvent genderChangeEvent = new GenderChangeEvent(p, getSnowflake(p).getGender(), getGender(gender));
        asylum.setGender(p.getUniqueId(), getGender(gender));
        getSnowflake(p).setGender(getGender(gender));
        Bukkit.getServer().getPluginManager().callEvent(genderChangeEvent);
    }

}
