package statement.expression;

import schema.Table;

import java.util.Set;

public interface Expression
{
   Set<Integer> evaluate(Table table);
}
