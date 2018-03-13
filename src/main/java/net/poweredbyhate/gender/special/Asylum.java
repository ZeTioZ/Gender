package net.poweredbyhate.gender.special;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface Asylum {

    public void loadGenders();

    public String getGender(Player p);

    public String getGender(UUID uuid);

    public void setGender(UUID uuid, Gender gender);

    public void setGender(UUID uuid, String gender);
}
