import java.util.*;
import java.io.*;

public class FileHandle 
{
    //get data and process data before go to main
    //return array string processed
    public ArrayList<String> getData(String path) throws IOException
    {
        FileReader fr = new FileReader(path);
		BufferedReader br=new BufferedReader(fr);
        
        //array return
        ArrayList<String> r_arrlist=new ArrayList<String>();

        String temp=new String();
        //read data from file
        while (true)
		{
		    temp=br.readLine();
			if (temp==null )
				break;
            //chech if "`" in the string
            //if not add to current index of array
            int pos=temp.indexOf('`');
            if(pos>=0&&pos<temp.length())
            {
                r_arrlist.add(temp);
            }
            else
            {
                String current=r_arrlist.get(r_arrlist.size()-1);
                current+="| "+temp;
                r_arrlist.set(r_arrlist.size()-1, current);
            }
		}
        //closing file
        br.close();
        fr.close();

        //return array
        return r_arrlist;

    }



    public int writeData(String path,SlangData r_dh)throws IOException
    {
        FileWriter fw= new FileWriter(path);
		BufferedWriter bw=new BufferedWriter(fw);

        Map<String,ArrayList<String>> data=r_dh.getData();

        for(Map.Entry<String,ArrayList<String>> m:data.entrySet())
        {
            String define=(String)m.getKey();
            ArrayList<String> means=(ArrayList<String>)m.getValue();
            String mean="";
            //get mean string
            for(int i=0;i<means.size()-1;i++)
                mean+=means.get(i)+"| ";
            mean+=means.get(means.size()-1);

            String w_data=define+"`"+mean+"\n";

            bw.write(w_data);
        }

        bw.close();
        fw.close();
        return 1;
    }
}
