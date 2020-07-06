package statement.expression;

import exception.SQLNumberComparisonException;
import exception.SQLNumberConversionException;
import schema.Table;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;
import statement.value.Value;
import utils.QueryUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class SmallerThan implements Expression
{
   private final static String operator = "<";
   private final String attributeName;
   private final Value value;

   SmallerThan(TokenStream stream) throws SQLException
   {
      this.attributeName = stream.consume(Token.Type.STR).data;
      stream.consume(Token.Type.OPERATOR, operator);
      value = new Value(stream);
   }

   /**
    * Evaluate smaller than expression.
    * @param table - to get specified column (attributeName)
    * @return all indexes of data that are < value in expression
    * @throws SQLException if column does not exist, or if comparison was not with numbers
    */
   @Override
   public Set<Integer> evaluate(Table table) throws SQLException
   {
      // Checks first that column exist otherwise SQLException thrown
      QueryUtils.colExistTrue(table.columnExists(attributeName), attributeName);
      List<String> dataList = table.getColumn(attributeName).getDataList();

      Set<Integer> indexes = new LinkedHashSet<>();
      for(int i = 0; i < dataList.size(); i++) {
         try {
            if(value.isFloatLiteral()) {
               if(Float.parseFloat(dataList.get(i)) < value.getFloatVal()) indexes.add(i);
            } else if(value.isIntLiteral()) {
               if(Integer.parseInt(dataList.get(i)) < value.getIntVal()) indexes.add(i);
            } else throw new SQLNumberComparisonException(operator);
         } catch(NumberFormatException e) {
            throw new SQLNumberConversionException();
         }
      }
      return indexes;
   }

}
