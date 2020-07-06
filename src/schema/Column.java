package schema;

import java.util.*;

public class Column
{
   private final String name;
   private final List<String> dataList;
   private int maxCharLength;

   public Column(String name)
   {
      this.name = name;
      dataList = new ArrayList<>();
      maxCharLength = name.length();
   }

   public void addData(String data)
   {
      if(data.length() > maxCharLength) maxCharLength = data.length();
      dataList.add(data);
   }

   public String getName()
   {
      return name;
   }

   public boolean isEmpty()
   {
      return dataList.isEmpty();
   }

   public int getMaxCharLength()
   {
      return maxCharLength;
   }

   public String getData(int index)
   {
      return dataList.get(index);
   }

   public void removeData(String data)
   {
      dataList.remove(data);
   }

   public void removeData(int index)
   {
      dataList.remove(index);
   }

   public void setData(int index, String data)
   {
      while(index >= getSize()) {
         dataList.add("");
      }
      dataList.set(index, data);
   }

   public List<String> getDataList()
   {
      return dataList;
   }

   public String getLastData()
   {
      return dataList.get(dataList.size()-1);
   }

   public int getSize()
   {
      return dataList.size();
   }

}
