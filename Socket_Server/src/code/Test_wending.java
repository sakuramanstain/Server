package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Test_wending {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		m_Typeconvert type=new m_Typeconvert(); 
		Typeconvert m_Type=new Typeconvert();
		String txtpath="E:\\主E采样终极版\\主E采样终极版\\dingwei\\x198y1315cy";
		 File file=new File(txtpath);
		 if(file.isDirectory()){
				File[] fileArray=file.listFiles();	
				for(File one_directory:fileArray){
					File[] m_file=one_directory.listFiles();
					for(File temp_file:m_file){
						if(temp_file.getPath().endsWith(".txt")){
							Map<String,Double> first=new HashMap<String,Double>();
							BufferedReader in_txt = new BufferedReader(new FileReader(temp_file.getPath()));
							String con="";
							String tt="";
							String aa=" ";					
							while((con=in_txt.readLine())!=null)
							{
								tt+=con;
							}
							String [] info=tt.split("\\s+");
							for(int i=0;i<info.length;i=i+2){
								Double values= Double.parseDouble(info[i+1]);
								 byte[] ab=type.getBytes(values);
								 double wifi_value=m_Type.byteToDouble(ab);
								first.put(info[i], wifi_value);
							}
							DatabaseManager m_DatabaseManager1=new DatabaseManager();
					     	m_DatabaseManager1.Initial();
							 HashMap<String ,Integer> second=m_DatabaseManager1.get_wifi_info(58);
							 List<String> first_temp= new ArrayList<String>();
					       	 Iterator<Entry<String, Double>> iter = first.entrySet().iterator(); 
					       	 while(iter.hasNext()){ 
					       	  Map.Entry<String, Double> entry = (Map.Entry<String, Double>)iter.next();
					       	  first_temp.add((String) entry.getKey()); 
					       	 } 
					       	 List<String> second_temp= new ArrayList<String>();
					       	Iterator<Entry<String, Integer>> iter2 = second.entrySet().iterator(); 
					      	 while(iter2.hasNext()){ 
					      	 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter2.next();
					      	 second_temp.add((String) entry.getKey()); 
					      	 } 
					       	 
					      	first_temp.retainAll(second_temp);
					       	 ////////////////
					       	 if(first_temp.size()>=1)
					       	 {
					       		 
					       		double distance=0;
					       		for(String t:first_temp)
					       		{
					       			//Double weight=mdb.get_weight(t);
					       		 // distance=distance+(1/weight)*Math.abs(first.get(t)-second.get(t));
					       		 distance=distance+Math.abs(first.get(t)-second.get(t));
					       		}
					           	 
					        	distance=distance/(first_temp.size());
					           System.out.println(distance);
					          	 
					       		
					       	 }
					       	 else
					       	 {
					   ////////////TODO
					       		
					       		System.out.println( Double.MAX_VALUE);
					       	 }
							
							
					}
				}
	}

}
}
}