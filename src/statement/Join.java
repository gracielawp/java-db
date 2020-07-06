package statement;

import schema.Column;
import schema.DBModel;
import schema.Table;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;
import utils.QueryUtils;
import utils.TableDataLoader;
import utils.TableView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Join extends Statement
{
   private String result;
   private final List<String> tableNames;
   private final List<String> attributeNames;


   Join()
   {
      tableNames = new ArrayList<>();
      attributeNames = new ArrayList<>();
   }

   @Override
   public void parse(TokenStream stream) throws SQLException
   {
      tableNames.add(stream.consume(Token.Type.STR).data);
      stream.consume(Token.Type.STR, "AND");
      tableNames.add(stream.consume(Token.Type.STR).data);
      stream.consume(Token.Type.STR, "ON");
      attributeNames.add(stream.consume(Token.Type.STR).data);
      stream.consume(Token.Type.STR, "AND");
      attributeNames.add(stream.consume(Token.Type.STR).data);
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
      List<Table> tables = loadAllTables(model);
      List<List<Integer>> indexes = findMatchingData(tables);

      TableView view = new TableView(createJoinTable(tables, indexes));
      result = view.printAll();
   }

   // Loads all tables from files
   private List<Table> loadAllTables(DBModel model)
   {
      List<Table> tables = new ArrayList<>();
      TableDataLoader loader = new TableDataLoader();

      for(String name : tableNames) {
         File file = QueryUtils.getTableFile(model.getTablePath(name));
         try {
            tables.add(loader.load(name, file));
         } catch(IOException e) {
            e.printStackTrace();
         }
      }
      return tables;
   }

   // Finds matching data between columns of 2 tables and returns indexes of matching data
   private List<List<Integer>> findMatchingData(List<Table> tables)
   {
      List<List<Integer>> indexes = new ArrayList<>(2);
      for(int i = 0; i < 2; i++) {
         indexes.add(new ArrayList<>());
      }

      List<String> data1 = tables.get(0).getColumn(attributeNames.get(0)).getDataList();
      List<String> data2 = tables.get(1).getColumn(attributeNames.get(1)).getDataList();
      for(int i = 0; i < data1.size(); i++) {
         for(int j = 0; j < data2.size(); j++) {
            if(data2.get(j).equals(data1.get(i))) {
               indexes.get(0).add(i);
               indexes.get(1).add(j);
            }
         }
      }
      return indexes;
   }

   private Table createJoinTable(List<Table> tables, List<List<Integer>> indexes)
   {
      Table join = new Table("join");

      // initialises table with ids first
      join.addColumn("id", new Column("id"));
      for(int i = 0; i < indexes.get(0).size(); i++) {
         join.getColumn("id").addData(Integer.toString(i+1));
      }

      // adds all specified column data to the join table
      for(int i = 0; i < tables.size(); i++) {
         for(String name : tables.get(i).getColumnNames()) {
            // create column names of the format table.colName
            if(!name.equals("id")) {
               String joinName = String.format("%s.%s", tables.get(i).getName(), name);
               Column col = new Column(joinName);
               for(int j : indexes.get(i)) {
                  col.addData(tables.get(i).getColumn(name).getData(j));
               }
               join.addColumn(joinName, col);
            }
         }
      }

      return join;
   }
}
