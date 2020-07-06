package statement;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Query
{
   private static final HashMap<String, Class<?>> queries = new HashMap<>();
   static {
      queries.put("USE", Use.class);
      queries.put("CREATE", Create.class);
      queries.put("DROP", Drop.class);
      queries.put("ALTER", Alter.class);
      queries.put("INSERT", Insert.class);
      queries.put("SELECT", Select.class);
      queries.put("UPDATE", Update.class);
      queries.put("DELETE", Delete.class);
      queries.put("JOIN", Join.class);
   }

   public static Statement createQuery(String keyword) throws NoSuchMethodException, InstantiationException,
           IllegalAccessException, InvocationTargetException
   {
      // always return new instance of specified class
      if(queries.containsKey(keyword.toUpperCase())) {
         return (Statement) queries.get(keyword.toUpperCase()).getDeclaredConstructor().newInstance();
      } else {
         throw new UnsupportedOperationException("Query not supported yet");
      }
   }
}
