package net.poweredbyhate.gender.utilities;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import net.poweredbyhate.gender.GenderPlugin;

public class DatabaseManager {

    private GenderPlugin plugin;
    private String host;
    private String database;
    private String user;
    private String pass;

    public DatabaseManager(GenderPlugin plugin, String host, String user, String pass, String database) {
        this.plugin = plugin;
        this.host = host;
        this.database = database;
        this.user = user;
        this.pass = pass;
        make();
    }

    private void make() {
        Database db = PooledDatabaseOptions
                .builder()
                .options(
                        DatabaseOptions.builder()
                                .mysql(user,pass,database,host)
                                .build()
                ).createHikariDatabase();
        DB.setGlobalDatabase(db);
    }

}