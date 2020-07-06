package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter
{
   private final FileWriter csvWriter;

   public CsvWriter(File csvFile, boolean append) throws IOException
   {
      csvWriter = new FileWriter(csvFile, append);
   }

   public void append(List<String> rowData) throws IOException
   {
      csvWriter.append(String.join(",", rowData));
      csvWriter.append("\n");
   }

   public void closeStream() throws IOException
   {
      csvWriter.flush();
      csvWriter.close();
   }
}
