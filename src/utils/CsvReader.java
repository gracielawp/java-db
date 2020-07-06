package utils;

import java.io.*;

public class CsvReader
{
   private final BufferedReader csvReader;

   public CsvReader(File csvFile) throws FileNotFoundException
   {
      csvReader = new BufferedReader(new FileReader(csvFile));
   }

   public String readRow() throws IOException
   {
      return csvReader.readLine();
   }

   public void close() throws IOException
   {
      csvReader.close();
   }
}
