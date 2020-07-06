package statement;

import schema.DBModel;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;

import java.io.File;

class Use extends Statement
{
   private String result, dbName;

   @Override
   public String getResult()
   {
      return result;
   }

   @Override
   public void parse(TokenStream stream) throws SQLException
   {
      Token token = stream.consume(Token.Type.STR);
      dbName = token.data;
   }

   @Override
   public void execute(DBModel model) throws SQLException
   {
      File dbFolder = new File(model.getDbPath(dbName));
      if(!dbFolder.exists()) throw new SQLException("ERROR: Database does not exist");
      model.setUseDatabase(dbName, true);
      result = "OK";
   }

}
