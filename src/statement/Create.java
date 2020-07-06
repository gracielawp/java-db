package statement;

import schema.DBModel;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;
import statement.value.AttributeList;
import utils.CsvWriter;
import utils.QueryUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

class Create extends Statement
{
   private String result, name;
   private boolean createDatabase, addAttributes;
   private AttributeList attrList;

   Create()
   {
      createDatabase = false;
      addAttributes = false;
   }

   @Override
   public String getResult()
   {
      return result;
   }

   @Override
   public void parse(TokenStream stream)
   {
      Token token = stream.consume(Token.Type.STR);
      if(token.data.equalsIgnoreCase("DATABASE")) {
         createDatabase = true;
         name = stream.consume(Token.Type.STR).data;
      } else if(token.data.equalsIgnoreCase("TABLE")) {
         name = stream.consume(Token.Type.STR).data;
         // checks if attributeList is defined - must have ( ) matching brackets
         if(stream.consumeIf(Token.Type.OPENBRACKET) != null) {
            addAttributes = true;
            attrList = new AttributeList(stream);
            stream.consume(Token.Type.CLOSEBRACKET);
         }
      } else throw new SQLException("ERROR: Invalid CREATE query, specify DATABASE or TABLE");
   }

   @Override
   public void execute(DBModel model)
   {
      if(createDatabase) {
         File dbFolder = new File(model.getDbPath(name));
         if(!dbFolder.mkdirs()) throw new SQLException("ERROR: Database already exists");
      } else {
         QueryUtils.useDatabaseTrue(model.isUseDatabase());
         createTable(model.getTablePath(name));
      }

      result = "OK";
   }

   private void createTable(String path)
   {
      File file = new File(path);
      // Tries to create new file and adds attribute columns if specified
      try {
         if(!file.createNewFile()) throw new SQLException("ERROR: Table already exists");
         // each new table needs an id
         if(addAttributes) {
            CsvWriter csvWriter = new CsvWriter(file, false);
            LinkedList<String> attributeList = attrList.getAttributes();
            attributeList.addFirst("id");
            csvWriter.append(attributeList);
            csvWriter.closeStream();
         }
      } catch(IOException e) {
         e.printStackTrace();
      }
   }

}
