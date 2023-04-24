package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.connectionpool;

import java.util.ResourceBundle;

public class DBResourceBundler {
    private static final DBResourceBundler instance = new DBResourceBundler();

    private static final String DB_PROPERTIES_FILE = "resources/db";
    private static final ResourceBundle bundle = ResourceBundle.getBundle(DB_PROPERTIES_FILE);

    public static DBResourceBundler getInstance() {
        return instance;
    }

    private DBResourceBundler() {
    }

    public String getProperty(String name) {
        return bundle.getString(name);
    }
}
