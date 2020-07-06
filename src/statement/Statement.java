package statement;

import schema.DBModel;
import exception.SQLException;
import sqlparser.TokenStream;

abstract public class Statement
{
   public abstract void parse(TokenStream stream) throws SQLException;
   public abstract void execute(DBModel model) throws SQLException;
   public abstract String getResult();
}
