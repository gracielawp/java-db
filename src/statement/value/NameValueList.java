package statement.value;

import sqlparser.Token;
import sqlparser.TokenStream;

import java.util.ArrayList;
import java.util.List;

public class NameValueList
{
    private final List<NameValuePair> nvpList;

    public NameValueList(TokenStream stream)
    {
        // Adds first one and adds more if comma found
        nvpList = new ArrayList<>();
        nvpList.add(new NameValuePair(stream));
        while(stream.consumeIf(Token.Type.COMMA) != null) {
            nvpList.add(new NameValuePair(stream));
        }
    }

    public List<NameValuePair> getNvpList()
    {
        return nvpList;
    }

}
