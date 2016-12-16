package net.poweredbyhate.gender;

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
}
