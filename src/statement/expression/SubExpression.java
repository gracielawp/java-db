package statement.expression;

import schema.Table;
import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;

import java.util.Set;

class SubExpression implements Expression
{
   private final Expression child;

   SubExpression(TokenStream stream)
   {
      // if parentheses, create new expression list to check for AND or OR operators
      if(stream.consumeIf(Token.Type.OPENBRACKET) != null) {
         child = new ExpressionList(stream);
         stream.consume(Token.Type.CLOSEBRACKET);
      } else {
         // if not, must be a relational expression
         child = createRelationalExpr(stream);
      }
   }

   private static Expression createRelationalExpr(TokenStream stream)
   {
      Token token = stream.peek(1, Token.Type.OPERATOR);
      if(token == null) throw new SQLException("ERROR: Expected OPERATOR");
      switch(token.data.toUpperCase()) {
         case "==":
            return new EqualsTo(stream);
         case "!=":
            return new NotEqualsTo(stream);
         case ">":
            return new GreaterThan(stream);
         case ">=":
            return new GreaterThanEq(stream);
         case "<":
            return new SmallerThan(stream);
         case "<=":
            return new SmallerThanEq(stream);
         case "LIKE":
            return new Like(stream);
         default:
            // thrown when OPERATOR defined in TokenTypes but not implemented yet
            throw new UnsupportedOperationException("Operator expression not supported yet.");
      }
   }

   @Override
   public Set<Integer> evaluate(Table table)
   {
      return child.evaluate(table);
   }
}
