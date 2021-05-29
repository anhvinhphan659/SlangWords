import java.util.*;

public class DataHandle 
{
    private Map<String,List<String>> data;

    public void setData(Map<String,List<String>>o_data)
    {
        data=o_data;
    }

    public Map<String,List<String>> getData()
    {
        return data;
    }
}
