package sqlparser;

import exception.SQLException;

import java.util.List;

public class TokenStream
{
   final List<Token> tokens;
   int offset = 0;

   public TokenStream(List<Token> tokens) {
      this.tokens = tokens;
   }

   /**
    * Looks ahead of current token and matches token type.
    * @param offset - integer specifying how far to look ahead
    * @param type - token type to match with
    * @return token if successful, otherwise null
    */
   public Token peek(int offset, Token.Type type)
   {
      // Checks if out-of-bounds
      if(this.offset + offset < tokens.size()) {
         if(tokens.get(this.offset + offset).type == type) {
            return tokens.get(this.offset + offset);
         }
      }
      return null;
   }

   /**
    * Consumes current token in stream.
    * @param type - token type
    * @throws SQLException - if token does not match type or data
    */
   public Token consume(Token.Type type) throws SQLException
   {
      if(isEnd()) throw new SQLException("ERROR: Invalid query expected " + type);
      Token token = tokens.get(offset++);
      if(token.type != type) {
         throw new SQLException("ERROR: Unexpected token [" + token.data + "] (" + "expected " + type + ")");
      }
      return token;
   }

   /**
    * Consumes current token in stream.
    * @param type - token type
    * @param data - string
    * @throws SQLException - if token does not match type or data
    */
   public void consume(Token.Type type, String data) throws SQLException
   {
      Token token = consume(type);
      if(!token.data.equalsIgnoreCase(data)) {
         throw new SQLException("ERROR: Expected " + data + " " + token);
      }
   }


   /**
    * Consumes current token in stream if matches data
    * @param data - string
    * @return token if successful, otherwise null
    */
   public Token consumeIf(String data)
   {
      if(isEnd()) return null;
      Token token = tokens.get(offset);
      if(token.data.equalsIgnoreCase(data)) {
         offset++;
         return token;
      }
      return null;
   }

   /**
    * Consumes current token in stream if matches type
    * @param type - token type
    * @return token if successful, otherwise null
    */
   public Token consumeIf(Token.Type type)
   {
      if(isEnd()) return null;
      Token token = tokens.get(offset);
      if(token.type == type) {
         offset++;
         return token;
      }
      return null;
   }

   public boolean isEnd()
   {
      return tokens.size() == offset;
   }

   public Token getCurrentToken()
   {
      if(isEnd()) return null;
      return tokens.get(offset);
   }

   // For debugging
   @Override
   public String toString() {
      return tokens.toString();
   }

}
