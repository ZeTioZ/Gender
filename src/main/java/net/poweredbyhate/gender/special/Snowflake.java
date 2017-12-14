package net.poweredbyhate.gender.special;

import net.poweredbyhate.gender.GenderPlugin;
import org.bukkit.entity.Player;

/**
 * Created by Lax on 12/10/2017.
 */
public class Snowflake {

    private GenderPlugin plugin;
    private Player player;
    private Gender gender;

    public Snowflake(GenderPlugin plugin, Player player, Gender gender) {
        this.plugin = plugin;
        this.player = player;
        this.gender = gender;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isSnowflake() {
        return true;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

}
