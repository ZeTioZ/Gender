package net.poweredbyhate.gender.special;

import net.poweredbyhate.gender.GenderPlugin;

/**
 * Created by Lax on 7/19/2017.
 */

public class Gender {

    private GenderPlugin plugin;
    private String name;
    private String description;
    private Boolean mentalillness = true;
    private String fromPack;

    public Gender(GenderPlugin plugin, String name, String description) {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean isMentalIllness() {
        return this.mentalillness;
    }

    public String getFromPack() {
        return this.fromPack;
    }

    public void setMentalIllness(Boolean b) {
        this.mentalillness = b;
    }

    public void setFromPack(String pack) {
        this.fromPack = pack;
    }
}
