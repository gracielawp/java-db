package statement;

import schema.DBModel;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;
import statement.expression.Expression;
import statement.expression.ExpressionList;
import statement.value.NameValueList;
import statement.value.NameValuePair;
import utils.QueryUtils;
import utils.TableDataLoader;
import utils.TableDataWriter;

import java.io.File;
import java.io.IOException;
import java.util.Set;

class Update extends Statement
{
   private String result, tableName;
   private NameValueList nvpList;
   private Expression exprList;

   @Override
   public void parse(TokenStream stream) throws SQLException
   {
      Token token = stream.consume(Token.Type.STR);
      tableName = token.data;
      stream.consume(Token.Type.STR, "SET");
      nvpList = new NameValueList(stream);
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
         // sets value in specified attribute from conditions
         setNameValue(model);
         // overwrites file with new modified table
         TableDataWriter writer = new TableDataWriter(model.getTable());
         writer.write(file);
      } catch(IOException e) {
         e.printStackTrace();
      }

      result = "OK";
   }

   private void setNameValue(DBModel model)
   {
      Set<Integer> indexes = exprList.evaluate(model.getTable());
      for(NameValuePair nvp : nvpList.getNvpList()) {
         if(!model.getTable().columnExists(nvp.getName())) {
            throw new SQLException("ERROR: Attribute does not exist");
         }
         // for each data index in specified column (attributeName), set to new value
         for(int i : indexes) {
            model.getTable().getColumn(nvp.getName()).setData(i, nvp.getValue());
         }
      }
   }

}
