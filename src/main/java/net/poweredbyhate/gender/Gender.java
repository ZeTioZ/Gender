package net.poweredbyhate.gender;

/**
 * Created by Lax on 7/19/2017.
 */

public class Gender {

    private GenderPlugin plugin;
    private String name;
    private String description;
    private Boolean mentalillness = true;

    public Gender(GenderPlugin plugin, String name, String description) {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDiscription() {
        return this.description;
    }

    public Boolean isMentalIllness() {
        return this.mentalillness;
    }

    public void setMentalIllness(Boolean b) {
        this.mentalillness = b;
    }
}
