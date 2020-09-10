package net.poweredbyhate.gender.listeners;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.poweredbyhate.gender.GenderPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Lax on 12/16/2016.
 */
public class PlaceholderListener extends PlaceholderExpansion {

    private GenderPlugin plugin;
    private String identifier = "gender";

    public PlaceholderListener(GenderPlugin plugin) {
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

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull String getAuthor() {
        return "LaxWasHere";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
