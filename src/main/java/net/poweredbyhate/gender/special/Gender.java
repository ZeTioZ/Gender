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
    private boolean isPublic = true;
    private String pronoun;

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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getPronoun() {
        return pronoun;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }
}
