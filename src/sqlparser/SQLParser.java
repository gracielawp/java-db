package sqlparser;

import exception.SQLException;
import statement.Query;
import statement.Statement;

import java.lang.reflect.InvocationTargetException;

public class SQLParser
{
   private final Lexer lexer;

   public SQLParser()
   {
      lexer = new Lexer();
   }

   public Statement parse(String input) throws SQLException, InvocationTargetException, NoSuchMethodException,
           InstantiationException, IllegalAccessException
   {
      TokenStream stream = lexer.lex(input);

      // Checks first token is a SQL query keyword, if not SQLException is thrown from stream
      Token token = stream.consume(Token.Type.KEYWORD);
      Statement statement = Query.createQuery(token.data);
      statement.parse(stream);

      // After statement successfully parsed, check that only semicolon left at the end
      if(stream.consumeIf(Token.Type.SEMICOLON) == null) {
         if(stream.getCurrentToken() == null) throw new SQLException("ERROR: Missing semicolon at the end");
         else throw new SQLException("ERROR: Expected semicolon " + stream.getCurrentToken());
      }
      if(!stream.isEnd()) throw new SQLException("ERROR: Unexpected token " + stream.getCurrentToken().data);

      return statement;
   }

}
