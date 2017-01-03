package net.poweredbyhate.gender;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Lax on 12/16/2016.
 */
public class Gurlz {

    public String getGender(UUID player) {
        String g = Gender.instance.getConfig().getString(player.toString());
        if (g == null) {
            return "";
        }
        return g;
    }

    public String getGender(Player player) {
        String g = Gender.instance.getConfig().getString(player.getUniqueId().toString());
        if (g == null) {
            return "";
        }
        return g;
    }

    public String getGender(OfflinePlayer player) {
        String g = Gender.instance.getConfig().getString(player.getUniqueId().toString());
        if (g == null) {
            return "";
        }
        return g;
    }
}
