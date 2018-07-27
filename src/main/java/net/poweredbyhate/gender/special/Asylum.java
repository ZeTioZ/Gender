package net.poweredbyhate.gender.special;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface Asylum {

    void loadGenders();

    String getGender(Player p);

    String getGender(UUID uuid);

    void setGender(UUID uuid, Gender gender);

    void setGender(UUID uuid, String gender);

    boolean dbExport(String... args);

    boolean dbImport(String... args);

    void reload();
}
