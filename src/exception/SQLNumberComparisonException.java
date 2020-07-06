package exception;

public class SQLNumberComparisonException extends SQLException
{
    public SQLNumberComparisonException(String operator)
    {
        super("ERROR: " + operator + " only accepts numerical comparisons");
    }
}
