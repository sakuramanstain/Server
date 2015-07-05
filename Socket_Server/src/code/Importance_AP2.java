package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Importance_AP2 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
     String parent_path="E:\\apimportance";
     File parent_file=new File(parent_path);
     File[] ap_files=parent_file.listFiles();
     int number_ap=ap_files.length;
     ArrayList<HashMap<String,Double>> list=new ArrayList<HashMap<String,Double>>();
     HashMap<String,Double> hashmap=new HashMap<String,Double>();
     for(File ap_file:ap_files){
    	 String file_name=ap_file.getName();
    	 String[] stri=file_name.split("_");
 		StringBuffer sb=new StringBuffer();
 		for(int i=0;i<stri.length-1;i++){
 			sb.append(stri[i]+":");
 		}
 		sb.append(stri[stri.length-1]);
 		String ap_name=sb.toString();
 		File[] posi_files=ap_file.listFiles();
 		int num_coor=posi_files.length; 
 		HashMap<Integer,Double[]> map=new HashMap<Integer,Double[]>();
 		int index=1;
 		for(File posi_file:posi_files){
 			String content="";
 			String con="";
 			BufferedReader reader=new BufferedReader(new FileReader(posi_file.getPath()));
 			while((con=reader.readLine())!=null){
 				content=content+con;
 				
 			}
 			String[] con_array=content.split("\\s+");
 			int total=con_array.length;
 			double sum=0;
 			for(int j=0;j<total;j++){
 				sum=sum+Double.parseDouble(con_array[j]);
 			}
 			double ava=sum/total;
 			Double[] value=new Double[2];
 			value[0]=(double) total;
 			value[1]=ava;
 			map.put(index, value);
 			index++;
 		}
 		double summary=0;
 		double total_num=0;
 		Iterator<Map.Entry<Integer, Double[]>> it=map.entrySet().iterator();
 		while(it.hasNext()){
 			Map.Entry<Integer, Double[]> entry=it.next();
 			Double[] val=entry.getValue();
 			total_num=total_num+val[0];
 			summary=summary+val[0]*val[1];
 		}
 		double global_ava=summary/total_num;
 		double significance=0;
 		
 		Iterator<Map.Entry<Integer, Double[]>> iter=map.entrySet().iterator();
 		while(iter.hasNext()){
 			Map.Entry<Integer, Double[]> entry=iter.next();
 			Double[] val=entry.getValue();
 			significance=significance+(val[1]-global_ava)*(val[1]-global_ava)/num_coor;
 		}
 		hashmap.put(ap_name, significance);
     }  
        int compare_num=0;
        double sum_impor=0;
        Iterator<Map.Entry<String, Double>> iterator=hashmap.entrySet().iterator();
		while(iterator.hasNext()){
			compare_num++;
			Map.Entry<String, Double> entry=iterator.next();
			double val=entry.getValue();
			sum_impor=sum_impor+val;
		}
		if(number_ap==compare_num){
			System.out.println("true");
			double maximum=0;
			Iterator<Map.Entry<String, Double>> iterator2=hashmap.entrySet().iterator();
			while(iterator2.hasNext()){
				Map.Entry<String, Double> entry=iterator2.next();
				double val=entry.getValue();
				//String name=entry.getKey();
				double norm_val=-(1-val/sum_impor)*Math.log(1-val/sum_impor);
				if(norm_val>maximum){
					maximum=norm_val;
				}
			}
			DatabaseManager m_DatabaseManager=new DatabaseManager();
			m_DatabaseManager.Initial();
			Iterator<Map.Entry<String, Double>> iterator3=hashmap.entrySet().iterator();
			while(iterator3.hasNext()){
				Map.Entry<String, Double> entry=iterator3.next();
				double val=entry.getValue();
				String name=entry.getKey();
				double norm_val=-(1-val/sum_impor)*Math.log(1-val/sum_impor);
				double weight=1+norm_val/maximum;
				m_DatabaseManager.insert_into_ap_impor(name, weight);
			}
			
			
		}
     
     
	}

}
