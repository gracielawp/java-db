package statement.value;

import sqlparser.Token;
import sqlparser.TokenStream;
import statement.value.Value;

public class NameValuePair
{
   private final String name;
   private final String value;

   public NameValuePair(TokenStream stream)
   {
      name = stream.consume(Token.Type.STR).data;
      stream.consume(Token.Type.OPERATOR, "=");
      value = (new Value(stream)).getValue();
   }

   public String getName()
   {
      return name;
   }

   public String getValue()
   {
      return value;
   }

}
