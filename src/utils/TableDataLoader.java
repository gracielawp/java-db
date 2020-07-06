package utils;

import schema.Column;
import schema.Table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TableDataLoader
{
   private Table table;

   public TableDataLoader() {}

   public TableDataLoader(String name)
   {
      table = new Table(name);
   }

   public Table load(String name, File file) throws IOException
   {
      table = new Table(name);
      return load(file);
   }

   public Table load(File file) throws IOException
   {
      CsvReader csvReader = new CsvReader(file);
      String row;
      if((row = csvReader.readRow()) == null) return table;

      String[] colNames = row.split(",");

      // initialise column list with column objects
      List<Column> columns = new ArrayList<>();
      for (String colName : colNames) {
         columns.add(new Column(colName));
      }

      // loads row data from csv file into each column
      while((row = csvReader.readRow()) != null) {
         String[] rowData = row.split(",");
         for(int i = 0; i < rowData.length; i++) {
            columns.get(i).addData(rowData[i]);
         }
      }

      // add columns with data to table in order
      for(int i = 0; i < colNames.length; i++) {
         table.addColumn(colNames[i], columns.get(i));
      }
      csvReader.close();

      return table;
   }

}
