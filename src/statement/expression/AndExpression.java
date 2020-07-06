package statement.expression;

import schema.Table;
import sqlparser.TokenStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class AndExpression implements Expression
{

   private final List<Expression> children = new ArrayList<>();

   AndExpression(TokenStream stream)
   {
      do {
         // checks for parentheses expression
         children.add(new SubExpression(stream));
      } while(stream.consumeIf("AND") != null);
   }

   @Override
   public Set<Integer> evaluate(Table table)
   {
      // get the first expression then evaluate others
      Set<Integer> indexes = children.get(0).evaluate(table);
      for(int i = 1; i < children.size(); i++) {
         indexes.retainAll(children.get(i).evaluate(table));
      }
      return indexes;
   }

}
