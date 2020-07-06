package sqlparser;

public class Token
{
   public final Type type;
   public final String data;

   public Token(Type type, String data)
   {
      this.type = type;
      this.data = data;
   }

   // Contains regex patterns for each specified token type
   public enum Type
   {
      OPERATOR("(?i)(<=|>=|==|!=|<|>|LIKE|=)"), OPENBRACKET("[(]"), CLOSEBRACKET("[)]"),
      COMMA("[,]"), KEYWORD("(?i)(SELECT|USE|CREATE|DROP|ALTER|INSERT|SELECT|UPDATE|DELETE|JOIN|ADD)"),
      STRLITERAL("(')([a-zA-Z_0-9 ]*)(')"), WHITESPACE("[\n\t\f\r ]+"),
      STR("[a-zA-Z][_a-zA-Z0-9]*"), FLOAT("[+-]?([0-9]*[.])[0-9]+"), INT("[+-]?[0-9]+"),
      SEMICOLON("[;]"), ALL("[*]");

      public final String pattern;

      Type(String pattern) {
         this.pattern = pattern;
      }
   }

   @Override
   public String toString()
   {
      return String.format("(%s %s)", type.name(), data);
   }
}
