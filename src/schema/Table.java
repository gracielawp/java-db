
package schema;

import java.util.*;

public class Table
{
   private String name;
   private final Map<String, Column> columns;

   public Table(String tableName)
   {
      columns = new LinkedHashMap<>();
      setName(tableName);
   }

   public void addColumn(String colName, Column col)
   {
      columns.put(colName, col);
   }

   public void setName(String tableName)
   {
      this.name = tableName;
   }

   public String getName()
   {
      return name;
   }

   public Collection<Column> getColumns() { return columns.values(); }

   public Collection<String> getColumnNames() { return columns.keySet(); }

   public boolean columnExists(String colName) { return columns.containsKey(colName); }

   public Column getColumn(String colName)
   {
      return columns.get(colName);
   }

   public void removeColumn(String colName)
   {
      columns.remove(colName);
   }

   public int getRowSize()
   {
      return columns.get("id").getSize();
   }

   public void addColumnName(String colName)
   {
      Column col = new Column(colName);
      for(int i = 0; i < getRowSize(); i++) {
         col.addData(""); // inits new column with empty strings
      }
      columns.putIfAbsent(colName, col);
   }

}
