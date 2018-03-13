package net.poweredbyhate.gender.utilities;

import net.poweredbyhate.gender.GenderPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//https://git.io/vxfX9
public class DatabaseManager {

    private List<Connection> connections = new ArrayList<Connection>();
    private GenderPlugin plugin;
    private String database;
    private String user;
    private String pass;

    public DatabaseManager(GenderPlugin plugin, String database, String user, String pass) {
        this.plugin = plugin;
        this.database = database;
        this.user = user;
        this.pass = pass;
    }

    public synchronized Connection getConnection() {
        for (int i = 0; i < connections.size(); i++) {
            Connection c = connections.get(i);
            try {
                if (c.isValid(2) && !c.isClosed()) {
                    return c;
                } else {
                    connections.remove(c);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                connections.remove(c);
            }
        }

        Connection c = runConnection();
        if (c != null) connections.add(c);
        return c;
    }

    private Connection runConnection() {
        try {
            return DriverManager.getConnection(database, user, pass);
        } catch (SQLException ex) {
            plugin.getLogger().severe("Cannot into SQL " + ex);
            return null;
        }
    }
}