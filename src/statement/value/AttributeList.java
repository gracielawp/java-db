package statement.value;

import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;

import java.util.LinkedList;

public class AttributeList
{
    private static final Token.Type attributeType = Token.Type.STR;
    private final LinkedList<String> attributes;

    public AttributeList(TokenStream stream) throws SQLException
    {
        attributes = new LinkedList<>();
        // adds first attribute and checks subsequent attributes preceded by comma
        attributes.add(stream.consume(attributeType).data);
        while(stream.consumeIf(Token.Type.COMMA) != null) {
            attributes.add(stream.consume(attributeType).data);
        }
    }

    public LinkedList<String> getAttributes()
    {
        return attributes;
    }

}
