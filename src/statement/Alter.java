package statement;

import schema.DBModel;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;
import utils.QueryUtils;
import utils.TableDataLoader;
import utils.TableDataWriter;

import java.io.File;
import java.io.IOException;

class Alter extends Statement
{
   private String result, tableName, attribute;
   private boolean add;

   @Override
   public void parse(TokenStream stream) throws SQLException
   {
      stream.consume(Token.Type.STR, "TABLE");
      tableName = stream.consume(Token.Type.STR).data;

      // DROP is recognised as a keyword token, so ADD was also added into the keyword token to simplify implementation
      Token token = stream.consume(Token.Type.KEYWORD);
      if(token.data.equalsIgnoreCase("ADD")) {
         add = true;
      } else if(token.data.equalsIgnoreCase("DROP")) {
         add = false;
      } else throw new SQLException("ERROR: Expected ADD/DROP");
      attribute = stream.consume(Token.Type.STR).data;
   }

   @Override
   public String getResult()
   {
      return result;
   }

   @Override
   public void execute(DBModel model) throws SQLException
   {
      QueryUtils.useDatabaseTrue(model.isUseDatabase());
      File file = QueryUtils.getTableFile(model.getTablePath(tableName));
      try {
         TableDataLoader loader = new TableDataLoader(tableName);
         model.setTable(loader.load(file));
         if(add) {
            model.getTable().addColumnName(attribute);
         } else model.getTable().removeColumn(attribute);

         // After altering table, overwrite file with new table
         TableDataWriter writer = new TableDataWriter(model.getTable());
         writer.write(file);
      } catch(IOException e) {
         e.printStackTrace();
      }
      result = "OK";
   }
}
