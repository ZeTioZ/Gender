package net.poweredbyhate.gender;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

/**
 * Created by Lax on 12/16/2016.
 */
public class PlaceholderListener extends EZPlaceholderHook {

    private Gender plugin;

    public PlaceholderListener(Gender plugin, String placeholderName) {
        super(plugin, placeholderName);
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {

        if (s.equalsIgnoreCase("gender")) {
            return plugin.mentalIllness.getGender(player);
        }

        return "UNKNOWN";
    }
}
