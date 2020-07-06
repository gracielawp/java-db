package statement;

import schema.Column;
import schema.DBModel;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;
import statement.expression.Expression;
import statement.expression.ExpressionList;
import utils.QueryUtils;
import utils.TableDataLoader;
import utils.TableDataWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Delete extends Statement
{
   private String result, tableName;
   private Expression exprList;

   @Override
   public void parse(TokenStream stream) throws SQLException
   {
      stream.consume(Token.Type.STR, "FROM");
      Token token = stream.consume(Token.Type.STR);
      tableName = token.data;
      stream.consume(Token.Type.STR, "WHERE");
      exprList = new ExpressionList(stream);
   }

   @Override
   public String getResult()
   {
      return result;
   }

   @Override
   public void execute(DBModel model)
   {
      QueryUtils.useDatabaseTrue(model.isUseDatabase());
      File file = QueryUtils.getTableFile(model.getTablePath(tableName));

      try {
         TableDataLoader loader = new TableDataLoader(tableName);
         model.setTable(loader.load(file));
         // evaluate conditions to get indexes to delete data of
         List<Integer> indexes = new ArrayList<>(exprList.evaluate(model.getTable()));

         // removes the largest index first to prevent indexOutOfBoundsException
         for(int i = indexes.size()-1; i >= 0; i--) {
            for(Column col : model.getTable().getColumns()) {
               col.removeData(indexes.get(i));
            }
         }
         // writes entire table to file (overwrites old file)
         TableDataWriter writer = new TableDataWriter(model.getTable());
         writer.write(file);
      } catch (IOException e) {
         e.printStackTrace();
      }

      result = "OK";
   }
}
