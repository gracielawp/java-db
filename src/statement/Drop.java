package statement;

import schema.DBModel;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;
import utils.QueryUtils;

import java.io.File;

class Drop extends Statement
{
   private String result, name;
   private boolean dropDatabase;

   @Override
   public void parse(TokenStream stream)
   {
      Token token = stream.consume(Token.Type.STR);
      if(token.data.equalsIgnoreCase("DATABASE")) {
         dropDatabase = true;
      } else if(token.data.equalsIgnoreCase("TABLE")) {
         dropDatabase = false;
      } else throw new SQLException("ERROR: Invalid DROP query, specify DATABASE or TABLE");

      token = stream.consume(Token.Type.STR);
      name = token.data;
   }

   @Override
   public String getResult()
   {
      return result;
   }

   @Override
   public void execute(DBModel model)
   {
      if(dropDatabase) {
         deleteDatabase(model.getDbPath(name));
         model.setUseDatabase("", false);
      } else {
         QueryUtils.useDatabaseTrue(model.isUseDatabase());
         File file = new File(model.getTablePath(name));
         if(file.exists()) file.delete();
      }
      result = "OK";
   }

   private void deleteDatabase(String path)
   {
      // deletes all files (tables) inside before deleting directory (database)
      File dir = new File(path);
      if(dir.exists()) {
         String[] fileNames = dir.list();
         for(String fName : fileNames) {
            File file = new File(dir.getPath(), fName);
            file.delete();
         }
         dir.delete();
      }
   }

}
