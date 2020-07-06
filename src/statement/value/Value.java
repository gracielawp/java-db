package statement.value;

import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;

public class Value
{
   private final String value;
   private float floatVal;
   private int intVal;
   private boolean intLiteral = false;
   private boolean floatLiteral = false;

   public Value(TokenStream stream) throws SQLException
   {
      Token token = stream.consumeIf(Token.Type.STR);
      if(token != null) {
         if(!token.data.equalsIgnoreCase("true") &&
                 !token.data.equalsIgnoreCase("false")) {
            throw new SQLException("ERROR: " + token.data + " must be enclosed in ''");
         }
         value = token.data;
      } else if((token = stream.consumeIf(Token.Type.STRLITERAL)) != null) {
         value = token.data.substring(1, token.data.length()-1);
      } else if((token = stream.consumeIf(Token.Type.INT)) != null) {
         value = token.data;
         intVal = Integer.parseInt(value);
         intLiteral = true;
      } else if((token = stream.consumeIf(Token.Type.FLOAT)) != null) {
         value = token.data;
         floatVal = Float.parseFloat(value);
         floatLiteral = true;
      } else throw new SQLException("ERROR: Unexpected token");
   }

   public int getIntVal()
   {
      return intVal;
   }

   public float getFloatVal()
   {
      return floatVal;
   }

   public String getValue()
   {
      return value;
   }

   public boolean isFloatLiteral()
   {
      return floatLiteral;
   }

   public boolean isIntLiteral()
   {
      return intLiteral;
   }
}