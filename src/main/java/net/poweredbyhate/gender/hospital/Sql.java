package net.poweredbyhate.gender.hospital;

import net.poweredbyhate.gender.GenderPlugin;
import net.poweredbyhate.gender.special.Asylum;
import net.poweredbyhate.gender.special.Gender;
import net.poweredbyhate.gender.utilities.DatabaseManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Sql implements Asylum {

    GenderPlugin plugin;
    DatabaseManager manager;

    public Sql(GenderPlugin plugin, DatabaseManager manager) {
        this.plugin = plugin;
    }

    @Override
    public void loadGenders() {

    }

    @Override
    public String getGender(Player p) {
        return null;
    }

    @Override
    public String getGender(UUID uuid) {
        return null;
    }

    @Override
    public void setGender(UUID uuid, Gender gender) {

    }

    @Override
    public void setGender(UUID uuid, String gender) {

    }

    /*
    @Gender
    id | name | description | pronoun | pack id? | hidden

    @pack
    id | name | author | website | version | enabled

    @users
    id | uuid | gender id/name?
     */
}
