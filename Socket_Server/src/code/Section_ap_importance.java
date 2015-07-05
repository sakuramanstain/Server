package code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/*���������֪�����ǣ���KNN�ҳ���5���ڽӵ㣬�洢��ok_sample_id�б��У�
 * Ŀ�꣺�����ҳ����������⼸�����е��ڽӵ�ĸ������ڵ��ڶ����ٰ���ЩС���ϲ��ɴ������ڴ����м���ap����Ҫ�̶ȣ���ֵ�洢�����ݿ���
 * �ڴ�����ͨ��knn�ҳ�3���ڽӵ㣬���о���ļ�����Ҫ�õ�ap����Ҫ��
  */
public class Section_ap_importance {

	
	//Algorithm knn_ml=new KNN_ML();
	Distance m_Distance=new  DistanceEuclidean();
	public static ArrayList<Integer>  location_section_id=new ArrayList<Integer>();
	public static double Result_x,Result_y;
	Map<String, Double> LDA_coordinate(List<Integer> ok_sample_id,List<String> wifi_ssid, Map<String, Double> wifi_up,DatabaseManager m_DatabaseManager) throws IOException{
	ArrayList<Integer>  section_id=new ArrayList<Integer>();
	ArrayList<Integer> All_XY=m_DatabaseManager.get_all_xy(1);  //�õ����вο��������
	ArrayList<Integer> x_y=new ArrayList();
	HashMap<Integer,Integer> section_frequency=new HashMap<Integer,Integer>();
	ArrayList<Integer> Section=new ArrayList();
     /***�ж��ҵ��ο������ڵ�����***/	
	//�����ڽӵ��ҵ���Ӧ�����꣬�洢��x_y�б���
	for(int i=0;i<ok_sample_id.size();i++)
	{
		ResultSet  rs1=m_DatabaseManager.get_ResultSet_from_wifi_sample(ok_sample_id.get(i));
        try {
			if(rs1.next())
			  {
			    int id_x=rs1.getInt("x");
			   	int id_y=rs1.getInt("y");
			   	x_y.add(id_x);
			   	x_y.add(id_y); 
			  }
			rs1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//��x_y�б��е������ҵ����������ڵ�����id���������������������ڽӵ�ĸ������洢��section_frequency��HashMap<Integer,Integer>����
	for(int i=0;i<x_y.size();i=i+2){
		int sec=m_DatabaseManager.get_sample_section(x_y.get(i),x_y.get(i+1));
		if(section_frequency.containsKey(sec)){
			int value=section_frequency.get(sec)+1;
			section_frequency.put(sec, value);
		}else{
			section_frequency.put(sec, 1);
		}			
	}
	//�ҳ����������������ڽӵ������id���洢��Section��ArrayList����
	Iterator iter=section_frequency.entrySet().iterator();
	while(iter.hasNext()){
		Map.Entry entry= (Entry) iter.next();
		int value=(Integer) entry.getValue();
		if(value>=2){
			Section.add((Integer) entry.getKey());
		}
	}
	/**********�ҳ����붨λ������Ĳο����Լ������ڵ�����**********/
	//�ɶ�λ���ҵ�����Ĳο���
	Map<String, Double> coordinate = find_coordinate(ok_sample_id,
			wifi_ssid, wifi_up, m_DatabaseManager);
	double L_x = coordinate.get("x") * 1568 / 1000;
	double L_y = coordinate.get("y") * 988 / 1000;
	int Near_x = 0, Near_y = 0;
	
	double Dis = Double.MAX_VALUE;
	for (int i = 0; i < All_XY.size(); i = i + 2) {
		double x_temp = Math.pow(L_x - All_XY.get(i), 2);
		double y_temp = Math.pow(L_y - All_XY.get(i + 1), 2);
		double dis = Math.sqrt(x_temp + y_temp);
		if (dis < Dis) {
			Dis = dis;
			Near_x = All_XY.get(i);
			Near_y = All_XY.get(i + 1);
		}
	}
	//�ɲο����ҵ���Ӧ��С��id
	int LocationSection = m_DatabaseManager.get_sample_section(Near_x,
			Near_y);
	location_section_id.add(LocationSection);
	/*****������һ����ѡ�����ҳ����������вο�������*****/
	//�ҵ����������е�����꣬�洢��X_Y��ArrayList<String>����
	ArrayList<String> section_xy = new ArrayList<String>();
	ArrayList<String> X_Y = new ArrayList<String>();// �洢��ѡ�������вο�������
	for (int i = 0; i < Section.size(); i++) {
		section_xy = m_DatabaseManager.get_section_xy(Section.get(i));
		for (int j = 0; j < section_xy.size(); j++) {
			String temp = section_xy.get(j);
			if (!X_Y.contains(temp)) {
				X_Y.add(temp);
			}
		}
	}
   //�������ҵ��ο���id���洢��Renerence_sample_id��ArrayList<Integer>��
	ArrayList<Integer> Renerence_sample_id = new ArrayList<Integer>();//ArrayList<Integer> Renerence_sample_id2 = new ArrayList<Integer>();
	ArrayList<Integer> sample_id = new ArrayList<Integer>();
	for (int i = 0; i < X_Y.size(); i++) {
		String[] ref = X_Y.get(i).split("\\s+");
		int x = Integer.valueOf(ref[0]);
		int y = Integer.valueOf(ref[1]);
		sample_id = m_DatabaseManager.get_ref_sample_id(x, y);
		for (int j = 0; j < sample_id.size(); j++) {
			Renerence_sample_id.add(sample_id.get(j));//Renerence_sample_id2.add(sample_id.get(j));
		}
	}
	//�ɲο���id�ҵ�ap��ssid��rssֵ,���洢��ssid_val(HashMap<String,String>)
	HashMap<String,String> ssid_val=new HashMap<String,String>();
	for(int id:Renerence_sample_id){
		HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
		Iterator<Entry<String,Integer>> iterator=temp_map.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, Integer> entry=iterator.next();
			String key=entry.getKey();
			int value=entry.getValue();
			if(ssid_val.containsKey(key)){
			     String old_value=ssid_val.get(key);
			     String new_value=old_value+" "+value;
			     ssid_val.put(key, new_value);
			}else{
				ssid_val.put(key, String.valueOf(value));
			}
		}
	}
	//��rssֵ������Ҫ�ԣ���������洢��ssid_importance(HashMap<String,Double>)
	Iterator<Entry<String,String>> iterator2=ssid_val.entrySet().iterator();
	HashMap<String,Double> ssid_importance=new HashMap<String,Double>();
	double sum_impor=0;//�洢��Ҫֵ���ܺ�
	while(iterator2.hasNext()){
		Map.Entry<String, String> entry=iterator2.next();
		String key=entry.getKey();
		String value=entry.getValue();
		String[] values=value.split("\\s+");
		int len=values.length;
		double sum=0;
		double sigma=0;
		for(String strength:values){
			sum=sum+Integer.valueOf(strength);
		}
		double ava=sum/len; 
		for(String strength:values){
			sigma=sigma+Math.pow(ava-Integer.valueOf(strength), 2);
		}
		sigma=sigma/len;
		ssid_importance.put(key, sigma);
		sum_impor=sum_impor+sigma;
	}
	//�ҳ������Ҫ�Ե��Ǹ�ֵ
	Iterator<Entry<String,Double>> iterator3=ssid_importance.entrySet().iterator();
	double maximum=0;
	while(iterator3.hasNext()){
		Map.Entry<String, Double> entry=iterator3.next();
		double val=entry.getValue();
		double norm_val=-(1-val/sum_impor)*Math.log(1-val/sum_impor);
		if(norm_val>maximum){
			maximum=norm_val;
		}
	}
	//������Ҫ�Լ���Ȩֵ
	HashMap<String,Double> ssid_weight=new HashMap<String,Double>();
	Iterator<Entry<String,Double>> iterator4=ssid_importance.entrySet().iterator();
	while(iterator4.hasNext()){
		Map.Entry<String, Double> entry=iterator4.next();
		double val=entry.getValue();
		String name=entry.getKey();
		double norm_val=-(1-val/sum_impor)*Math.log(1-val/sum_impor);
		double weight=1+norm_val/maximum;
		ssid_weight.put(name, weight);
	}
	//
	List<Integer> ok_sample_id_3=find_right_sample_ok_3(wifi_ssid,wifi_up, Renerence_sample_id,m_DatabaseManager, ssid_weight);
	ArrayList<Integer> x_y_3=new ArrayList();
	for(int i=0;i<ok_sample_id_3.size();i++)
	{
		ResultSet  rs1=m_DatabaseManager.get_ResultSet_from_wifi_sample(ok_sample_id_3.get(i));
        try {
			if(rs1.next())
			  {
			    int id_x_3=rs1.getInt("x");
			   	int id_y_3=rs1.getInt("y");
			   	x_y_3.add(id_x_3);
			   	x_y_3.add(id_y_3); 
			  }
			rs1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	Map<String,Double> coordinate_3=section_find_coordinate(ok_sample_id_3,wifi_ssid,wifi_up,m_DatabaseManager,ssid_weight);
	 double L_x_3=coordinate_3.get("x")*1568/1000;
     double L_y_3=coordinate_3.get("y")*988/1000;
     

     
     
     int Near_x_3=0,Near_y_3=0;
     double Dis_3=Double.MAX_VALUE;
     for(int i=0;i<All_XY.size();i=i+2){//x_y_3
  	   double x_temp=Math.pow(L_x_3-All_XY.get(i), 2);
  	   double y_temp=Math.pow(L_y_3-All_XY.get(i+1), 2);
  	   double dis_3=Math.sqrt(x_temp+y_temp);
  	   if(dis_3<Dis_3){
  		   Dis_3=dis_3;
  		   Near_x_3=All_XY.get(i);
  		   Near_y_3=All_XY.get(i+1);
  	   }
     }
     int LocationSection_3=m_DatabaseManager.get_sample_section(Near_x_3,Near_y_3);
     location_section_id.add(LocationSection_3);
     int count=0;
     for(int i=0;i<location_section_id.size();i++){
    	 if(LocationSection_3==location_section_id.get(i)){
    		count++; 
    	 }
     }

     if(((double)count)/location_section_id.size()>0.3){
    	 Result_x=coordinate_3.get("x");
    	 Result_y=coordinate_3.get("y");
    	 double tt_x_3=Result_x*1568/1000;
    	 double tt_y_3=Result_y*988/1000;
    	 FileWriter section_fw_3;        	 
    	 section_fw_3=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_res"+1+".txt",true);
    	 section_fw_3.write("		"+tt_x_3+"			"+tt_y_3+"		\n");
    	 section_fw_3.flush();
    	 section_fw_3.close();
    	 return coordinate_3;
     }else{
    	 double tt_x_3=Result_x*1568/1000;
    	 double tt_y_3=Result_y*988/1000;
    	 FileWriter section_fw_3;        	 
    	 section_fw_3=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_res"+1+".txt",true);
    	 section_fw_3.write("		"+tt_x_3+"			"+tt_y_3+"		\n");
    	 section_fw_3.flush();
    	 section_fw_3.close();
    	 HashMap<String,Double> result_fianl=new HashMap<String,Double>();
         result_fianl.put("x",Result_x );
         result_fianl.put("y",Result_y );
        return result_fianl;
     }
	//�˴���Ҫ�޸�
	
}
List<Integer> find_right_sample_ok_3(List<String> wifi_ssid,
			Map<String, Double> wifi_up, List<Integer> sample_id_list,
			DatabaseManager m_DatabaseManager,Map<String,Double> ssid_weight) {

		// TODO Ѱ�Ҿ��������K��������Ϣ
		
		 int K_NN=3;
		 
		 double step=0;
		 List<Integer> ok_sample_id=new ArrayList<Integer>();
		 if(K_NN>=sample_id_list.size()){
			 ok_sample_id.addAll(sample_id_list);
		 }else{
			 Integer [] K_NN_OK=new Integer[K_NN];
			 Double[] K_NN_value_OK=new Double[K_NN];
			 for(int j=0;j<K_NN;j++){
				 K_NN_value_OK[j]=Double.MAX_VALUE-K_NN-1+j;
        		 K_NN_OK[j]=-1;
			 }
			 for(int i=0;i<sample_id_list.size();i++){
				 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(sample_id_list.get(i));
				 double distance=distance_double(wifi_up,temp_map,ssid_weight);
				 if(distance<K_NN_value_OK[K_NN-1]){
					 int ii=K_NN-1;
        			 K_NN_OK[K_NN-1]=sample_id_list.get(i);
        			 K_NN_value_OK[K_NN-1]=distance;
        			 while(ii>0&&K_NN_value_OK[ii-1]>K_NN_value_OK[ii]){
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
			 for(Integer ok_id:K_NN_OK){
				 ok_sample_id.add(ok_id);
			 }
		 }
		
		return ok_sample_id;
	}
double distance_double(Map<String, Double> first,
		Map<String, Integer> second,Map<String,Double> ssid_weight) {
	// TODO Auto-generated method stub
	
    
    ///////
   
   	
   	 List<String> first_temp= new ArrayList<String>();
   	 Iterator<Entry<String, Double>> iter = first.entrySet().iterator(); 
   	 while(iter.hasNext()){ 
   	  Map.Entry<String, Double> entry = (Map.Entry<String, Double>)iter.next();
   	  first_temp.add((String) entry.getKey()); 
   	 } 
	List<String> second_temp = new ArrayList<String>();
	Iterator<Entry<String, Integer>> iter2 = second.entrySet().iterator();
	while (iter2.hasNext()) {
		Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iter2
				.next();
		second_temp.add((String) entry.getKey());
	}
   	 
  	first_temp.retainAll(second_temp);
   	 ////////////////
   	 if(first_temp.size()>=1)
   	 {
   		 
   		double distance=0;
   		for(String t:first_temp)
   		{
   		  double weight=ssid_weight.get(t);
   		  distance=distance+(1/weight)*Math.pow(first.get(t)-second.get(t),2);
   		}
   		distance=Math.sqrt(distance);
       	distance=distance/(first_temp.size());
       	return distance;
      	 
   		
   	 }
   	 else
   	 {
////////////TODO
   		return Double.MAX_VALUE;
       	
   	 }
	
}
public Map<String, Double> section_find_coordinate(List<Integer> ok_sample_id,
		List<String> wifi_ssid, Map<String, Double> wifi_up,
		DatabaseManager m_DatabaseManager,Map<String,Double> ssid_weight) throws IOException {
	// TODO Auto-generated method stub
	
	int temp_map_id=1;

	HashMap<Integer ,Double> distance_map = new HashMap<Integer, Double>();
	for(Integer id:ok_sample_id){
		HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
		double distance=distance_double(wifi_up,temp_map,ssid_weight);

   	    distance=1/distance;//Math.exp(-2*distance);
      	distance_map.put(id, distance);

	}
	
	Set<Integer> key = distance_map.keySet(); 
    double _x=0;
    double _y=0;
    double weight_all=0;
    
    double x_pixl=1568;
	double p_pixl=988;
	
	while(true){
    	Map<Integer,Double[]> sample_id_xy=new HashMap<Integer,Double[]>();
		for(int i=0;i<ok_sample_id.size();i++)
    	{
    		ResultSet  rs1=m_DatabaseManager.get_ResultSet_from_wifi_sample(ok_sample_id.get(i));
            try {
				if(rs1.next())
				  {
				   	int temp_x=rs1.getInt("x");
				   	int temp_y=rs1.getInt("y");
				   	int temp_height=rs1.getInt("height");
				   	int temp_width=rs1.getInt("width");
				   	Double [] xy=new Double[2];
				   	xy[0]=temp_x*1000.0*1.0/temp_width;
					xy[1]=temp_y*1000.0*1.0/temp_height;
					sample_id_xy.put(ok_sample_id.get(i),xy);
				  }
				rs1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		double distance_sum_all=Double.MAX_VALUE;
		int id_sum_all=0;
		for(int i=0;i<ok_sample_id.size();i++)
		{
			double temp=0;
			double from_x=sample_id_xy.get(ok_sample_id.get(i))[0];
			double from_y=sample_id_xy.get(ok_sample_id.get(i))[1];
			for(int j=0;j<ok_sample_id.size();j++)
			{
				double x_temp=Math.pow(from_x-sample_id_xy.get(ok_sample_id.get(j))[0],2);
			   	double y_temp=Math.pow(from_y-sample_id_xy.get(ok_sample_id.get(j))[1],2);
			   	double distance2=Math.sqrt(x_temp+y_temp);
				temp+=distance2;
			}
			if(temp<distance_sum_all)
			{
				id_sum_all=i;
				distance_sum_all=temp;
			}
		}
		double sum_distance=0;
  		double sum_distance2=0;
  		List<Double> distance=new ArrayList<Double>();
		for(int i=0;i<ok_sample_id.size();i++)
		{
			double temp=0;
			double from_x=sample_id_xy.get(ok_sample_id.get(i))[0];
			double from_y=sample_id_xy.get(ok_sample_id.get(i))[1];
			double x_temp=Math.pow(from_x-sample_id_xy.get(ok_sample_id.get(id_sum_all))[0],2);
			double y_temp=Math.pow(from_y-sample_id_xy.get(ok_sample_id.get(id_sum_all))[1],2);
			double distance2=Math.sqrt(x_temp+y_temp);
			sum_distance+=distance2;
		   	sum_distance2+=distance2*distance2;
		   	distance.add(distance2);
		}	
		 double average=sum_distance/(ok_sample_id.size());
  		 double delat=sum_distance2/ok_sample_id.size()-average*average;
  		 double Max=-1;
  		 int id=0;
  		 for(int i=0;i<ok_sample_id.size();i++)
  		 {
  			 if(distance.get(i)>Max)
  			 {
  				Max=distance.get(i);
  				id=i;
  			 }
  		 }
  		 double ap1=Math.abs(Max);
  		 double P=Math.abs(ap1-average);
  		 boolean flag=true;//false
  		 if(P>Math.sqrt(delat)){      			
//  			System.out.println("remove:"+ok_sample_id.get(id)+"    X:  "+ sample_id_xy.get(ok_sample_id.get(id))[0]+"   Y:  "+sample_id_xy.get(ok_sample_id.get(id))[1]);    
//  			ok_sample_id.remove(id);
  		 }else{
	    	flag=true;
  		 }
      	if(flag)
      		break;        
	 }
	for (int i=0;i<ok_sample_id.size();i++){            
   		int s = ok_sample_id.get(i);   				
			ResultSet  rs1=m_DatabaseManager.get_ResultSet_from_wifi_sample(s);
        try {
			if(rs1.next())
			  {
			   	int temp_x=rs1.getInt("x");
			   	int temp_y=rs1.getInt("y");
			   	int temp_height=rs1.getInt("height");
			   	int temp_width=rs1.getInt("width");
			  		_x=_x+temp_x*1000*1/temp_width*distance_map.get(s);
			  		_y=_y+temp_y*1000*1/temp_height*distance_map.get(s);
			  		weight_all=weight_all+distance_map.get(s);
//			  		System.out.println("x:"+temp_x*x_pixl/1000.0+"	y:"+temp_y*p_pixl/1000.0+"	id:"+s+"   distance:  "+distance_map.get(s));
			  }
			rs1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                 			 		
	}
	_x=_x/weight_all;
		_y=_y/weight_all;
		HashMap<String ,Double> xy_map = new HashMap<String, Double>();
		xy_map.put("x", _x);
		xy_map.put("y", _y);
		FileWriter fw;
         return xy_map;

}
Map<String, Double> find_coordinate(List<Integer> ok_sample_id,
		List<String> wifi_ssid, Map<String, Double> wifi_up,
		DatabaseManager m_DatabaseManager) {
	// TODO Auto-generated method stub
	
	int temp_map_id=0;
	HashMap<Integer ,Double> distance_map = new HashMap<Integer, Double>();
	for(Integer id:ok_sample_id){
		HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
		double distance=m_Distance.distance_double(wifi_up,temp_map);			
   	    distance=1/distance;//Math.exp(-2*distance);
      	distance_map.put(id, distance);
	}
	
	Set<Integer> key = distance_map.keySet(); 
    double _x=0;
    double _y=0;
    double weight_all=0;
    
    double x_pixl=1568;
	double p_pixl=988;
	
	while(true){
    	Map<Integer,Double[]> sample_id_xy=new HashMap<Integer,Double[]>();
		for(int i=0;i<ok_sample_id.size();i++)
    	{
    		ResultSet  rs1=m_DatabaseManager.get_ResultSet_from_wifi_sample(ok_sample_id.get(i));
            try {
				if(rs1.next())
				  {
				   	int temp_x=rs1.getInt("x");
				   	int temp_y=rs1.getInt("y");
				   	int temp_height=rs1.getInt("height");
				   	int temp_width=rs1.getInt("width");
				   	Double [] xy=new Double[2];
				   	xy[0]=temp_x*1000.0*1.0/temp_width;
					xy[1]=temp_y*1000.0*1.0/temp_height;
					sample_id_xy.put(ok_sample_id.get(i),xy);
				  }
				rs1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		//����k�����У�ÿ����ֱ��������������ܺ�
		double distance_sum_all=Double.MAX_VALUE;//�������֮����С�ľ���ֵ
		int id_sum_all=0;//����ֵ����С������id
		for(int i=0;i<ok_sample_id.size();i++)
		{
			double temp=0;
			double from_x=sample_id_xy.get(ok_sample_id.get(i))[0];
			double from_y=sample_id_xy.get(ok_sample_id.get(i))[1];
			for(int j=0;j<ok_sample_id.size();j++)
			{
				double x_temp=Math.pow(from_x-sample_id_xy.get(ok_sample_id.get(j))[0],2);
			   	double y_temp=Math.pow(from_y-sample_id_xy.get(ok_sample_id.get(j))[1],2);
			   	double distance2=Math.sqrt(x_temp+y_temp);
				temp+=distance2;
			}
			if(temp<distance_sum_all)
			{
				id_sum_all=i;
				distance_sum_all=temp;
			}
		}
		//
		double sum_distance=0;
  		double sum_distance2=0;
  		List<Double> distance=new ArrayList<Double>(); 
		for(int i=0;i<ok_sample_id.size();i++)
		{
			double temp=0;
			double from_x=sample_id_xy.get(ok_sample_id.get(i))[0];
			double from_y=sample_id_xy.get(ok_sample_id.get(i))[1];
			double x_temp=Math.pow(from_x-sample_id_xy.get(ok_sample_id.get(id_sum_all))[0],2);
			double y_temp=Math.pow(from_y-sample_id_xy.get(ok_sample_id.get(id_sum_all))[1],2);
			double distance2=Math.sqrt(x_temp+y_temp);
			sum_distance+=distance2;
		   	sum_distance2+=distance2*distance2;
		   	distance.add(distance2);
		}	
		 double average=sum_distance/(ok_sample_id.size());
  		 double delat=sum_distance2/ok_sample_id.size()-average*average;
  		 double Max=-1;
  		 int id=0;
  		 for(int i=0;i<ok_sample_id.size();i++)
  		 {
  			 if(distance.get(i)>Max)
  			 {
  				Max=distance.get(i);
  				id=i;
  			 }
  		 }
  		 double ap1=Math.abs(Max);
  		 double P=Math.abs(ap1-average);
  		 boolean flag=true;//false
  		 if(P>Math.sqrt(delat)){      			
//  			System.out.println("remove:"+ok_sample_id.get(id)+"    X:  "+ sample_id_xy.get(ok_sample_id.get(id))[0]+"   Y:  "+sample_id_xy.get(ok_sample_id.get(id))[1]);    
//  			ok_sample_id.remove(id);
  		 }else{
	    	flag=true;
  		 }
      	if(flag)
      		break;        
	 }
	for (int i=0;i<ok_sample_id.size();i++){            
   		int s = ok_sample_id.get(i);   				
			ResultSet  rs1=m_DatabaseManager.get_ResultSet_from_wifi_sample(s);
        try {
			if(rs1.next())
			  {
			   	int temp_x=rs1.getInt("x");
			   	int temp_y=rs1.getInt("y");
			   	int temp_height=rs1.getInt("height");
			   	int temp_width=rs1.getInt("width");
			  		_x=_x+temp_x*1000*1/temp_width*distance_map.get(s);
			  		_y=_y+temp_y*1000*1/temp_height*distance_map.get(s);
			  		weight_all=weight_all+distance_map.get(s);
//			  		System.out.println("x:"+temp_x*x_pixl/1000.0+"	y:"+temp_y*p_pixl/1000.0+"	id:"+s+"   distance:  "+distance_map.get(s));
			  }
			rs1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                 			 		
	}
	_x=_x/weight_all;
		_y=_y/weight_all;
		HashMap<String ,Double> xy_map = new HashMap<String, Double>();
		xy_map.put("x", _x);
		xy_map.put("y", _y);
		FileWriter fw;
try {			
		fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_res"+temp_map_id+".txt",true);
		double ttx=_x*x_pixl/1000.0;
		double tty=_y*p_pixl/1000.0;			
		fw.write("		"+ttx+"			"+tty+"		\n");
		fw.flush();
		fw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return xy_map;
}
}