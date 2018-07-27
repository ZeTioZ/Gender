package net.poweredbyhate.gender.hospital;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import net.poweredbyhate.gender.GenderPlugin;
import net.poweredbyhate.gender.special.Asylum;
import net.poweredbyhate.gender.special.Gender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/*
This class is currently a clusterfk
TODO: Clean up this clusterfk.
 */

public class Sql implements Asylum {

    private GenderPlugin plugin;
    private Flat flat;

    public Sql(GenderPlugin plugin) {
        this.plugin = plugin;
        this.flat = new Flat(plugin);
        firstRun();
    }

    @Override
    public void reload() {

    }

    @Override
    public void loadGenders() {
        Gender nullGender = new Gender("", "Null gender", "");
        nullGender.setPublic(false);
        plugin.goMental().imagine(nullGender);
        try {
            List<DbRow> genders = DB.getResults("SELECT g.name, g.description, g.pronoun, g.public FROM genders g INNER JOIN packs p ON g.pack_id = p.pack_id WHERE p.enabled = 'true';");
            for (DbRow gender : genders) {
                Gender g = new Gender(gender.getString("name"), gender.getString("description"), gender.getString("pronoun"));
                plugin.goMental().imagine(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getGender(Player p) {
        return getGender(p.getUniqueId());
    }

    @Override
    public String getGender(UUID uuid) {
        try {
            DbRow dbRow = DB.getFirstRow("select name from genders where gender_id in (select gender_id from snowflakes where uuid= ?);", uuid.toString());
            return dbRow != null ? dbRow.get("name").toString() : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setGender(UUID uuid, Gender gender) {
        setGender(uuid, gender.getName());
    }

    @Override
    public void setGender(UUID uuid, String gender) {
        try {
            DB.executeUpdate("INSERT INTO snowflakes (uuid, gender_id) VALUES (?, (select gender_id from genders where name=?)) ON DUPLICATE KEY UPDATE gender_id=(select gender_id from genders where name=?)",uuid.toString(), gender, gender);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void firstRun() {
        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS `packs` (`pack_id` int(16) NOT NULL AUTO_INCREMENT PRIMARY KEY, `name` varchar(256) NOT NULL UNIQUE, `author` VARCHAR(256), `website` VARCHAR(256), `version` VARCHAR(16), `enabled` VARCHAR(5) NOT NULL)");

            DB.executeInsert("INSERT IGNORE INTO packs (name, author, website, version, enabled) VALUES (?,?,?,?,?)", "CustomGenders", "null.entity", "https://www.spigotmc.org/resources/33217/", "0.1", "true");

            DB.executeUpdate("CREATE TABLE IF NOT EXISTS `genders` (`gender_id` int(16) NOT NULL AUTO_INCREMENT PRIMARY KEY, `name` varchar(256) NOT NULL UNIQUE, `description` text NOT NULL, `pronoun` VARCHAR(16) DEFAULT '', `public` VARCHAR(5) DEFAULT 'true', `pack_id` int(16), FOREIGN KEY (pack_id) REFERENCES packs(pack_id))");

            DB.executeUpdate("CREATE TABLE IF NOT EXISTS `snowflakes` (`snowflake_id` int(16) NOT NULL AUTO_INCREMENT PRIMARY KEY, `uuid` varchar(36) NOT NULL UNIQUE, `gender_id` int(16), FOREIGN KEY (gender_id) REFERENCES genders(gender_id))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean dbImport(String... importFile) {
        File file = new File(plugin.getDataFolder(),importFile[0]+".yml");
        FileConfiguration conf = new YamlConfiguration();

        try {
            conf.load(file);

            /*
            Import gender packs
             */

            for (String pack : conf.getConfigurationSection("packs").getKeys(false)) {
                DB.executeInsert("INSERT IGNORE INTO packs (name, author, website, version, enabled) VALUES (?,?,?,?,?)", pack, conf.getString("packs."+pack+".author"), conf.getString("packs."+pack+".website"), conf.getString("packs."+pack+".version"), conf.getString("packs."+pack+".enabled"));
            }

            /*
            Import Genders
             */
            for (String name : conf.getConfigurationSection("genders").getKeys(false)) {
                DB.executeInsert("INSERT IGNORE INTO genders (name, description, pack_id, pronoun, public) VALUES (?, ? ,(select pack_id from packs where name=?) , ?, ?)", name, conf.getString("genders."+name+".description"), conf.getString("genders."+name+".pack"), conf.getString("genders."+name+".pronoun", ""), conf.getString("genders."+name+".public", "true"));
            }

            /*
            Import snowflakes
             */

            for (String snowflake : conf.getConfigurationSection("players").getKeys(false)) {
                setGender(UUID.fromString(snowflake), conf.getString("players."+snowflake));
            }

            return true;
        } catch (IOException | InvalidConfigurationException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean importPack(String... importFile) {
        File file = new File(flat.getPackFolder(), importFile[0]+".yml");
        FileConfiguration conf = new YamlConfiguration();
        try {
            conf.load(file);
            String packName = conf.getString("pack.name", "CustomGenders");

            if (!packName.equalsIgnoreCase("CuStOmGeNdErS")) {
                DB.executeInsert("REPLACE INTO packs (name, author, website, version, enabled) VALUES (?,?,?,?,?)", packName, conf.getString("pack.author"), conf.getString("pack.website"), conf.getString("packs.version"), conf.getString("packs.enabled"));
            }

            for (String name : conf.getConfigurationSection("genders").getKeys(false)) {
                DB.executeInsert("INSERT IGNORE INTO genders (name, description, pack_id, pronoun, public) VALUES (?, ? ,(select pack_id from packs where name=?) , ?, ?)", name, conf.getString("genders."+name+".description"), packName, conf.getString("genders."+name+".pronoun", ""), conf.getString("genders."+name+".public", "true"));
            }
            loadGenders();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean dbExport(String... export) {
        return false;
    }

}
