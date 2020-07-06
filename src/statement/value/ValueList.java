package statement.value;

import exception.SQLException;
import sqlparser.Token;
import sqlparser.TokenStream;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ValueList
{
    private final List<Value> values;
    private final LinkedList<String> strValues;

    public ValueList(TokenStream stream) throws SQLException
    {
        int i = 0;
        values = new ArrayList<>();
        strValues = new LinkedList<>();

        // gets first value
        values.add(new Value(stream));
        strValues.add(values.get(i).getValue());

        // keeps adding values if next token after value is comma
        while(stream.consumeIf(Token.Type.COMMA) != null) {
            values.add(new Value(stream));
            i++;
            strValues.add(values.get(i).getValue());
        }
    }

    public List<Value> getValues()
    {
        return values;
    }

    public LinkedList<String> getStrValues()
    {
        return strValues;
    }

}
