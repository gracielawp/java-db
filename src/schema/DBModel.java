package schema;

import java.io.File;

public class DBModel
{
   private final static String SQL_PATH = "sql" + File.separator;
   private Table table;
   private boolean useDatabase;
   String dbName;

   public DBModel()
   {
      useDatabase = false;
   }

   public String getTablePath(String tableName)
   {
      return SQL_PATH + dbName + File.separator + tableName + ".csv";
   }

   public String getDbPath(String dbName) { return SQL_PATH + dbName; }

   public boolean isUseDatabase()
   {
      return useDatabase;
   }

   public void setUseDatabase(String dbName, boolean use)
   {
      this.dbName = dbName;
      useDatabase = use;
   }

   public String getDbName()
   {
      return dbName;
   }

   public void setTable(Table table)
   {
      this.table = table;
   }

   public Table getTable()
   {
      return table;
   }
}
