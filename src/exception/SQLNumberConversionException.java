package exception;

public class SQLNumberConversionException extends SQLException
{
    public SQLNumberConversionException()
    {
        super("ERROR: Attribute cannot be converted to number");
    }
}
