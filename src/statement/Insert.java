package statement;

import schema.DBModel;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;
import statement.value.ValueList;
import utils.CsvWriter;
import utils.QueryUtils;
import utils.TableDataLoader;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

class Insert extends Statement
{
   private String result;
   private String tableName;
   private LinkedList<String> values;

   Insert()
   {
      values = new LinkedList<>();
   }

   @Override
   public void parse(TokenStream stream)
   {
      stream.consume(Token.Type.STR, "INTO");
      Token token = stream.consume(Token.Type.STR);
      tableName = token.data;
      stream.consume(Token.Type.STR, "VALUES");
      stream.consume(Token.Type.OPENBRACKET);
      values = (new ValueList(stream)).getStrValues();
      stream.consume(Token.Type.CLOSEBRACKET);
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
         // -1 to exclude autogenerated id column
         if(model.getTable().getColumns().size()-1 != values.size()) {
            throw new SQLException("ERROR: Number of VALUES do not match Table fields");
         }
         addIdToValues(model);
         CsvWriter csvWriter = new CsvWriter(file, true); // don't overwrite file
         csvWriter.append(values);
         csvWriter.closeStream();
      } catch (IOException e) {
         e.printStackTrace();
      }
      result = "OK";
   }

   private void addIdToValues(DBModel model)
   {
      // automatically insert the id number into the table incremented from the last row id
      String id;
      if(model.getTable().getColumn("id").getSize() == 0) {
         id = "1";
      } else {
         int lastId = Integer.parseInt(model.getTable().getColumn("id").getLastData());
         id = Integer.toString(lastId + 1);
      }
      values.addFirst(id); // adds the id to the beginning of the row
   }

}