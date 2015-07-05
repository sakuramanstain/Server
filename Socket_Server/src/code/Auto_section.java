package code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Auto_section {
	int x_pixl=1568;
	int y_pixl=988;
	Distance m_Distance=new  DistanceEuclidean();
	Algorithm knn_ml=new KNN_ML();
	 Map<String,Double> find_coordinate(List<Integer> ok_sample_id,List<String> wifi_ssid, Map<String, Double> wifi_up,DatabaseManager m_DatabaseManager) throws IOException {
		 //从k个邻接点中找出三个小区，使得小区内的信号强度与在线信号强度最近
		List<Integer> k_ok_sample_id=find_senction(3,ok_sample_id,wifi_ssid,wifi_up,m_DatabaseManager);
		List<Integer> all_id=find_all_id(k_ok_sample_id,m_DatabaseManager);
		List<Integer> final_id=find_senction(4,all_id,wifi_ssid,wifi_up,m_DatabaseManager);
		Map<String,Double> coordinate=knn_ml.find_coordinate(final_id, wifi_ssid, wifi_up, m_DatabaseManager);
		double x=coordinate.get("x");
		double y=coordinate.get("y");
		FileWriter fw;
		fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_res"+46+".txt",true);
		double ttx=x*x_pixl/1000.0;
		double tty=y*y_pixl/1000.0;			
		fw.write("		"+ttx+"			"+tty+"		\n");
		fw.flush();
		fw.close();
		return coordinate;
	}
	 
	 
	 
	 List<Integer> find_senction(int num,List<Integer> ok_sample_id,List<String> wifi_ssid, Map<String, Double> wifi_up,DatabaseManager m_DatabaseManager){
		 int number=num;
		 int len_ok_id=ok_sample_id.size();
		 HashMap<Integer,Double> id_distance=new HashMap<Integer,Double>();
		 for(int i=0;i<len_ok_id;i++){
			 ArrayList<Integer> neighbors=m_DatabaseManager.get_neighbors(ok_sample_id.get(i));
			 int len_nei=neighbors.size();
			 double dis_total=0;
			 for(int j=0;j<len_nei;j++){
				
				 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(neighbors.get(j));
	        	 double distance=m_Distance.distance_double(wifi_up,temp_map);
	        	 dis_total=dis_total+distance;
			 }
			 double dis_ava=dis_total/len_nei;
			 id_distance.put(ok_sample_id.get(i), dis_ava);
		 }
		 
		 Integer [] K_NN_OK=new Integer[number];
    	 Double[] K_NN_value_OK=new Double[number];
    	 for(int j=0;j<number;j++)
    	 {
    		 K_NN_value_OK[j]=Double.MAX_VALUE-number-1+j;
    		 K_NN_OK[j]=-1;
    	 }
		 Iterator<Entry<Integer,Double>> iter=id_distance.entrySet().iterator();
		 while(iter.hasNext()){
			 Entry<Integer, Double> entry=iter.next();
			 int ok_id=entry.getKey();
			 double distance=entry.getValue();
			 if(distance<K_NN_value_OK[number-1])
    		 {
    			 int ii=number-1;
    			 K_NN_OK[number-1]=ok_id;
    			 K_NN_value_OK[number-1]=distance;
    			 while(ii>0&&K_NN_value_OK[ii-1]>K_NN_value_OK[ii])
    			 {
    				 double temp_value=K_NN_value_OK[ii];
    				 K_NN_value_OK[ii]=K_NN_value_OK[ii-1];
    				 K_NN_value_OK[ii-1]=temp_value;
    				 int temp_id=K_NN_OK[ii];
    				 K_NN_OK[ii]=K_NN_OK[ii-1];
    				 K_NN_OK[ii-1]=temp_id;
    				 ii--;
    			 }
    		 }
		 }
		 List<Integer> k_ok_sample_id=new ArrayList<Integer>();
		 
		 for(Integer ok_id:K_NN_OK)
    	 {
					 k_ok_sample_id.add(ok_id);
    	 }
		 
		 
		return k_ok_sample_id;
		 
	 }
	 List<Integer> find_all_id(List<Integer> k_ok_sample_id,DatabaseManager m_DatabaseManager) {
		 ArrayList<Integer> all_id=new ArrayList<Integer>();
		 for(Integer ok_id:k_ok_sample_id)
    	 {
			 
			 ArrayList<Integer> neighbor=m_DatabaseManager.get_neighbors(ok_id);
			 for(Integer ok_nei:neighbor){
				 if(!all_id.contains(ok_nei)){
					 all_id.add(ok_nei);
				 }
			 }
    	 }
		 return all_id;
	 }
	 
}
