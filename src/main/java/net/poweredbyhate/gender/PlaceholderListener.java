package net.poweredbyhate.gender;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

/**
 * Created by Lax on 12/16/2016.
 */
public class PlaceholderListener extends EZPlaceholderHook {

    private GenderPlugin plugin;

    public PlaceholderListener(GenderPlugin plugin, String placeholderName) {
        super(plugin, placeholderName);
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {

        if (s.equalsIgnoreCase("gender")) {
            return plugin.goMental().getSnowflake(player).getGender().getName();
        }

        return "UNKNOWN";
    }
}
