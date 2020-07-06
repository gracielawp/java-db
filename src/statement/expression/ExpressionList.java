package statement.expression;

import schema.Table;
import sqlparser.TokenStream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpressionList implements Expression
{
   private final List<Expression> expressions = new ArrayList<>();

   // recursive parsing of expression with OR expression checked last
   public ExpressionList(TokenStream stream)
   {
      do {
         // AND expression checked first (usually has higher order of importance than OR)
         expressions.add(new AndExpression(stream));
      } while (stream.consumeIf("OR") != null);
   }

   @Override
   public Set<Integer> evaluate(Table table)
   {
      // evaluates all expressions inside list and adds all results together (like OR operator)
      Set<Integer> indexes = new HashSet<>();
      for(Expression expr : expressions) {
         indexes.addAll(expr.evaluate(table));
      }
      return indexes;
   }
}
