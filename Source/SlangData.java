import java.sql.Array;
import java.util.*;

public class SlangData
{
    private Map<String, ArrayList<String>> data;

    public SlangData()
    {
        data=new HashMap<>();
    }

    //handle data and set data from arraly list string 
    public void setData(ArrayList<String> arr_data)
    {
        if(arr_data==null)
            return;

        for(String cur:arr_data)
        {
            String[] word=cur.split("`");
            if(word==null)
                continue;
            String define=word[0];
            String[] meaning=word[1].split("\\| ");
            ArrayList<String> means=new ArrayList<String>(Arrays.asList(meaning));
            data.put(define,means);            
        }       
    }


    public Map<String,ArrayList<String>> getData()
    {
        return data;
    }

    //add new slang word to list
    //mode:
    //'w': overwrite old data
    //'a': add new meaning if define is existed!
    public void addNewSlang(String define,String meaning,char mode)
    {
        ArrayList<String> means=new ArrayList<String>();
        String[] mean_arr=meaning.split(",");
        ArrayList<String> new_mean=new ArrayList<String>(Arrays.asList(mean_arr));
        means.addAll(new_mean);
        if(data.containsKey(define)==false)
        {
            data.put(define,means);
        }
        else
        {
            if (mode=='w')
            {
                means.add(meaning);
                data.replace(define,means);
            }
            else
            {
                means=data.get(define);
                means.add(meaning);
                data.replace(define,means);
            }
            
        }

    }

    public ArrayList<String> getKeyList()
    {
        
        Set<String> dat=data.keySet();
        ArrayList<String> ret=new ArrayList<String>(dat);
        Collections.sort(ret);
        return ret;
    }

    public ArrayList<String> getMeanList()
    {
        ArrayList<String>ret=new ArrayList<>();
        for (ArrayList<String> mean:data.values())
        {
            String temp;
            temp=mean.toString();
            ret.add(temp);
        }
        return ret;
    }



    public void deleteSlang(String define)
    {
        if(data.containsKey(define)==true)
        {
            data.remove(define);
        }
    }

    public void editSlang(String define,ArrayList<String>means)
    {
        data.replace(define,means);
    }

    public Boolean isExisted(String key)
    {
        return data.containsKey(key);
    }
    
}
