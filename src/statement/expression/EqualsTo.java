package statement.expression;

import schema.Table;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;
import statement.value.Value;
import utils.QueryUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;

class EqualsTo implements Expression
{
   private final static String operator = "==";
   private final String attributeName;
   private final Value value;

   EqualsTo(TokenStream stream) throws SQLException
   {
      this.attributeName = stream.consume(Token.Type.STR).data;
      stream.consume(Token.Type.OPERATOR, operator);
      value = new Value(stream);
   }

   /**
    * Evaluate equals to expression.
    * @param table - to get specified column (attributeName)
    * @return all indexes of data that are equal to value in expression
    * @throws SQLException if column not found
    */
   @Override
   public Set<Integer> evaluate(Table table) throws SQLException
   {
      // Checks first that column exist otherwise SQLException thrown
      QueryUtils.colExistTrue(table.columnExists(attributeName), attributeName);
      List<String> dataList = table.getColumn(attributeName).getDataList();
      return IntStream.range(0, dataList.size())
              .filter(i -> value.getValue().equals(dataList.get(i)))
              .boxed()
              .collect(toCollection(LinkedHashSet::new));
   }
}
