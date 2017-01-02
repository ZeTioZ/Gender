package net.poweredbyhate.gender;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Lax on 12/16/2016.
 */
public class Gurlz {

    Gender gender;

    public void Gurlz(Gender gender) {
        this.gender = gender;
    }

    public String getGender(UUID player) {
        String g = gender.getConfig().getString(player.toString());
        if (g == null) {
            return "";
        }
        return g;
    }

    public String getGender(Player player) {
        String g = gender.getConfig().getString(player.getUniqueId().toString());
        if (g == null) {
            return "";
        }
        return g;
    }

    public String getGender(OfflinePlayer player) {
        String g = gender.getConfig().getString(player.getUniqueId().toString());
        if (g == null) {
            return "";
        }
        return g;
    }
}
