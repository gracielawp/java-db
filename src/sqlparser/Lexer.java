package sqlparser;

import exception.SQLException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Lexer
{
   private final ArrayList<Token> tokens;
   private final Pattern tokPatterns;

   public Lexer()
   {
      tokens = new ArrayList<>();
      tokPatterns = compileTokenPatterns();
   }

   private Pattern compileTokenPatterns()
   {
      StringBuilder tokenPatternsBuffer = new StringBuilder();
      for(Token.Type tokType : Token.Type.values()) {
         tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokType.name(), tokType.pattern));
      }
      return Pattern.compile(tokenPatternsBuffer.substring(1));
   }

   public TokenStream lex(String input) throws SQLException
   {
      tokens.clear();
      Matcher matcher = tokPatterns.matcher(input);
      int offset = 0;
      Token.Type[] types = Token.Type.values();
      while(offset != input.length()) {
         if(!matcher.find() || matcher.start() != offset) {
            throw new SQLException("ERROR: Unexpected token at " + offset);
         }
         for(Token.Type type : types) {
            if (matcher.group(type.name()) != null) {
               if (type != Token.Type.WHITESPACE) {
                  tokens.add(new Token(type, matcher.group(type.name())));
               }
               break; // exits loop once found matching token to go to next token
            }
         }
         offset = matcher.end(); // goes to next token
      }
      return new TokenStream(tokens);
   }

}
