package net.poweredbyhate.gender.hospital;

import net.poweredbyhate.gender.GenderPlugin;
import net.poweredbyhate.gender.special.Asylum;
import net.poweredbyhate.gender.special.Gender;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Flat implements Asylum {

    GenderPlugin plugin;

    public Flat(GenderPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadGenders() {
        Gender gender = new Gender("", "Null gender", "");
        gender.setPublic(false);
        plugin.goMental().imagine(gender);
        File folder = new File(plugin.getDataFolder(), "genderpacks");
        loadData(Paths.get(new File(plugin.getDataFolder(), "CustomGenders.yml").getAbsolutePath()), false);
        if (!folder.exists()) {
            folder.mkdir();
            plugin.saveFile("CommunityPack.yml");
            plugin.saveFile("MasterPack.yml");
            plugin.saveFile("BasicPack.yml");
            plugin.saveFile("ElementsPack.yml");
            plugin.saveFile("HogwartsPack.yml");
        }
        try {
            Files.walk(Paths.get(folder.getAbsolutePath())).filter(file -> file.getFileName().toString().endsWith(".yml")).collect(Collectors.toList()).forEach(this::loadData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getGender(Player p) {
        return getConfig().getString(p.getUniqueId().toString());
    }

    @Override
    public String getGender(UUID p) {
        return getConfig().getString(p.toString());
    }

    @Override
    public void setGender(UUID p, Gender gender) {
        setConfig(p, gender.getName());
    }

    @Override
    public void setGender(UUID p, String gender) {
        setConfig(p, gender);
    }

    public void loadData(Path path) {
        loadData(path, true);
    }

    public void loadData(Path path, boolean isPack) {
        File file = new File(path.toUri());
        plugin.getLogger().log(Level.INFO, "Loading: " + file.getName());
        FileConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
            if (configuration.getConfigurationSection("genders") == null) {
                return;
            }
            if (!configuration.getBoolean("pack.enabled") && isPack) {
                return;
            }
            for (String m : configuration.getConfigurationSection("genders").getKeys(false)) {
                Gender g = new Gender(m,configuration.getString("genders."+m+".description", m), configuration.getString("genders."+m+".pronoun", ""));
                g.setFromPack(file.getName().split("\\.")[0]);
                g.setPublic(configuration.getBoolean("genders."+m+".public", true));
                plugin.goMental().imagine(g);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getPack(String pack) {
        File file = new File(getPackFolder(), pack+".yml");
        FileConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
            if (configuration.getConfigurationSection("genders") == null) {
                return null;
            }
            if (!configuration.getBoolean("pack.enabled")) {
                return null;
            }
            return configuration;
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setConfig(Object key, String payload) {
        plugin.getConfig().set(key.toString(), StringUtils.capitalize(payload));
        plugin.saveConfig();
    }

    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public File getPackFolder() {
        return new File(plugin.getDataFolder(), "genderpacks");
    }

    public boolean dbImport(String... importFile) {
        return false;
    }

    public boolean dbExport(String... exportFile) {
        File lefile = new File(plugin.getDataFolder(), exportFile[0]+".yml");
        try {
            if (!lefile.exists()) {
                lefile.createNewFile();
            }
            for (Path path : Files.walk(Paths.get(getPackFolder().getAbsolutePath())).filter(file -> file.getFileName().toString().endsWith(".yml")).collect(Collectors.toList())) {
                export(path, lefile);
            }
            FileConfiguration eFile = new YamlConfiguration();
            eFile.load(lefile);
            for (String flake : plugin.getConfig().getKeys(false)) {
                eFile.set("players."+flake, plugin.getConfig().getString(flake));
            }
            eFile.save(lefile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void export(Path path, File file) {
        FileConfiguration importFile = new YamlConfiguration();
        FileConfiguration exportFile = new YamlConfiguration();
        try {
            importFile.load(path.toFile());
            exportFile.load(file);
            String packName = importFile.getString("pack.name");
            exportFile.set("packs."+packName+".author", importFile.getString("pack.author"));
            exportFile.set("packs."+packName+".website", importFile.getString("pack.website"));
            exportFile.set("packs."+packName+".version", importFile.getString("pack.version"));
            exportFile.set("packs."+packName+".enabled", importFile.getBoolean("pack.enabled"));

            for (String gender : importFile.getConfigurationSection("genders").getKeys(false)) {
                exportFile.set("genders."+gender+".pack", packName);
                exportFile.set("genders."+gender+".description", importFile.getString("genders."+gender+".description"));
                exportFile.set("genders."+gender+".pronoun", importFile.getString("genders."+gender+".pronoun"));
            }
            exportFile.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
