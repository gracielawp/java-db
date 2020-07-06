package statement.expression;

import schema.Table;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;
import utils.QueryUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;

class Like implements Expression
{
   private final static String operator = "LIKE";
   private final String attributeName;
   private final String value;

   Like(TokenStream stream) throws SQLException
   {
      this.attributeName = stream.consume(Token.Type.STR).data;
      stream.consume(Token.Type.OPERATOR, operator);
      Token token = stream.consume(Token.Type.STRLITERAL);
      value = token.data.substring(1, token.data.length()-1);
   }

   /**
    * Evaluate LIKE expression.
    * @param table - to get specified column (attributeName)
    * @return all indexes of data which has a substring of the value in expression
    * @throws SQLException if column does not exist
    */
   @Override
   public Set<Integer> evaluate(Table table) throws SQLException
   {
      // Checks first that column exist otherwise SQLException thrown
      QueryUtils.colExistTrue(table.columnExists(attributeName), attributeName);
      List<String> dataList = table.getColumn(attributeName).getDataList();
      return IntStream.range(0, dataList.size())
              .filter(i -> dataList.get(i).contains(value))
              .boxed()
              .collect(toCollection(LinkedHashSet::new));
   }
}
