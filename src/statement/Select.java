package statement;

import schema.DBModel;
import sqlparser.Token;
import sqlparser.TokenStream;
import statement.expression.ExpressionList;
import statement.value.AttributeList;
import utils.QueryUtils;
import utils.TableDataLoader;
import utils.TableView;

import java.io.File;
import java.io.IOException;

class Select extends Statement
{
   private String result, tableName;
   private boolean all, condition;
   private ExpressionList exprList;
   private AttributeList attrList;

   Select()
   {
      all = false;
      condition = false;
   }

   @Override
   public void parse(TokenStream stream)
   {
      if(stream.consumeIf(Token.Type.ALL) != null) all = true;
      else attrList = new AttributeList(stream);

      stream.consume(Token.Type.STR, "FROM");
      tableName = stream.consume(Token.Type.STR).data;

      Token token = stream.consumeIf(Token.Type.STR);
      if(token != null) {
         if(token.data.equalsIgnoreCase("WHERE")) {
            condition = true;
            this.exprList = new ExpressionList(stream);
         }
      }
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
         TableView view = new TableView(model.getTable());
         if(all && !condition) {
            result = view.printAll();
         } else if(all) {
            // prints all with specified conditions
            result = view.printAll(exprList.evaluate(model.getTable()));
         } else if(!condition) {
            // prints specified attributes without conditions
            result = view.printAttributes(attrList.getAttributes());
         } else {
            // prints specified attributes with specified conditions
            result = view.printAttributes(attrList.getAttributes(), exprList.evaluate(model.getTable()));
         }
      } catch(IOException e) {
         e.printStackTrace();
      }
   }

}
