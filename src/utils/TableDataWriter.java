package utils;

import schema.Column;
import schema.Table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TableDataWriter
{
   private final Table table;

   public TableDataWriter(Table table)
   {
      this.table = table;
   }

   public void write(File file) throws IOException
   {
      // always overwrites file contents
      CsvWriter csvWriter = new CsvWriter(file, false);
      // fill file with column names first
      List<String> colNames = new ArrayList<>(table.getColumnNames());
      csvWriter.append(colNames);

      // get column data formatted into row to append to file
      List<Column> columns = new ArrayList<>(table.getColumns());
      List<String> row = new ArrayList<>();
      int size = columns.get(0).getSize();
      for(int i = 0; i < size; i++) {
         for(Column col : columns) {
            if(col.getSize() > i) row.add(col.getData(i));
         }
         csvWriter.append(row);
         row.clear(); // prevent memory overhead (chosen over new instance for every row)
      }
      csvWriter.closeStream();
   }
}
