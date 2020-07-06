package utils;

import schema.Column;
import schema.Table;
import exception.SQLException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TableView
{
   final Table table;
   private final int tableSize;
   private final StringBuilder builder;
   private final Collection<Column> columns;

   public TableView(Table table)
   {
      this.table = table;
      columns = table.getColumns();
      tableSize = table.getColumn("id").getSize();
      builder = new StringBuilder();
   }

   public String printAll()
   {
      appendColNames();
      for(int i = 0; i < tableSize; i++) {
         appendRowData(i);
      }
      return builder.toString();
   }

   public String printAll(Set<Integer> indexes)
   {
      appendColNames();
      for(int i : indexes) {
         appendRowData(i);
      }
      return builder.toString();
   }

   public String printAttributes(List<String> attributes) throws SQLException
   {
      appendColNames(attributes);
      for(int i = 0; i < tableSize; i++) {
         appendRowData(attributes, i);
      }
      return builder.toString();
   }

   public String printAttributes(List<String> attributes, Set<Integer> indexes) throws SQLException
   {
      appendColNames(attributes);
      for(int i : indexes) {
         appendRowData(attributes, i);
      }
      return builder.toString();
   }

   private void addString(int padLength, String data)
   {
      builder.append(String.format("%-" + padLength + "s", data));
      builder.append("\t");
   }

   private void appendColNames()
   {
      for(Column col : columns) {
         addString(col.getMaxCharLength(), col.getName());
      }
      builder.append("\n");
   }

   private void appendColNames(List<String> names)
   {
      for(String name : names) {
         if(!table.columnExists(name)) {
            throw new SQLException("ERROR: Attribute does not exist.");
         }
         addString(table.getColumn(name).getMaxCharLength(), name);
      }
      builder.append("\n");
   }

   private void appendRowData(List<String> attributes, int i)
   {
      for(String attribute : attributes) {
         Column col = table.getColumn(attribute);
         if(!col.isEmpty()) {
            builder.append(String.format("%-" + col.getMaxCharLength() + "s", col.getData(i)));
            builder.append("\t");
         }
      }
      builder.append("\n");
   }

   private void appendRowData(int i)
   {
      for(Column col : columns) {
         if(!col.isEmpty()) {
            addString(col.getMaxCharLength(), col.getData(i));
         }
      }
      builder.append("\n");
   }

}
