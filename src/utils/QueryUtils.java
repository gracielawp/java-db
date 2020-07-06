package utils;

import exception.SQLException;

import java.io.File;

// used in statement.execute()
public class QueryUtils
{
    // only executes query if database has been selected (USE database)
    public static void useDatabaseTrue(boolean useDatabase)
    {
        if(!useDatabase) {
            throw new SQLException("ERROR: Database not selected");
        }
    }

    public static File getTableFile(String path)
    {
        File file = new File(path);
        if(!file.exists()) throw new SQLException("ERROR: Table does not exist");
        return file;
    }

    public static void colExistTrue(boolean colExists, String name)
    {
        if(!colExists) {
            throw new SQLException("ERROR: Table " + name + " does not exist.");
        }
    }
}
