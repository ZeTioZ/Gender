package net.poweredbyhate.gender;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

/**
 * Created by Lax on 12/16/2016.
 */
public class PlaceholderListener extends EZPlaceholderHook {

    private Gender plugin;

    public PlaceholderListener(Gender plugin) {
        super(plugin, "gender");
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        if (player == null) {
            return "";
        }
        if (s.equalsIgnoreCase("gender")) {
            return new Gurlz().getGender(player.getUniqueId());
        }
        return null;
    }
}
