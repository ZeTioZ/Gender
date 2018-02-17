package net.poweredbyhate.gender.listeners;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import net.poweredbyhate.gender.GenderPlugin;
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
            return plugin.goMental().getSnowflake(player).getGender().getName().replace("_", " ");
        }
        if (s.equalsIgnoreCase("pronoun")) {
            return plugin.goMental().getSnowflake(player).getGender().getPronoun();
        }

        return "UNKNOWN";
    }
}
