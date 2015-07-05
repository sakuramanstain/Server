package code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Jama.Matrix;
import JuLei.Euclidean;
import JuLei.KMeans;
import JuLei.InitCentroid;
/**
 * 本算法旨在实现利用LDA方法基于KNN实现定位功能
 * @author wangpeng
 * @version 1.0
 *
 */
public class KNN_ML extends Algorithm {
	

	public static ArrayList<Integer>  location_section_id=new ArrayList<Integer>();
	public static ArrayList<Integer>  Kmeans_section_id=new ArrayList<Integer>();
	public static ArrayList<Integer>  Lda_section_id=new ArrayList<Integer>();
	public static double Result_x,Result_y;//用来存放局部knn算法的上次定位坐标
	public static double Kmeans_Result_x,Kmeans_Result_y;//用来存放局部Kmeans算法的上次定位坐标
	public static double LDA_Result_x,LDA_Result_y;//用来存放局部LDA算法的上次定位坐标
	
	
	Distance m_Distance=new  DistanceEuclidean();//DistanceEuclidean(); DistanceAbsolute

	
	/**************************find_map_id************************************/
	@Override
	int find_map_id(List<String> wifi_ssid, Map<String, Double> wifi_up,
			DatabaseManager m_DatabaseManager) {
		// TODO 寻找距离上传信息最近的坐标点所在的地图为定位地图

		List<Integer> sample_id_list =m_DatabaseManager.get_ALL_ID_wifi_sample_id();
		int sample_id=0;
        double min_distance=100000;
        for(Integer id:sample_id_list){
        	 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
        	 double distance=m_Distance.distance_double(wifi_up,temp_map);
        	 if(distance<min_distance){
        		 sample_id=id;
        		 min_distance=distance;
        	 }
        }
        int map_id=m_DatabaseManager.get_map_ID_from_wifi_sample(sample_id);
		return map_id;
	}
    /*************************find_right_sample_ok**************************************/
	@Override
	List<Integer> find_right_sample_ok(List<String> wifi_ssid,
			Map<String, Double> wifi_up, int map_id,
			DatabaseManager m_DatabaseManager) {
		// TODO 寻找距离最近的K个采样信息
		
		 int K_NN=5;
		 
		 List<Integer> sample_id_list =m_DatabaseManager.get_wifi_sample_id_in_map(map_id);
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
				 double distance=m_Distance.distance_double(wifi_up,temp_map);
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

	@Override
	List<Integer> find_right_sample_ok_2(List<String> wifi_ssid,
			Map<String, Double> wifi_up, int map_id_1, int map_id_2,
			DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		return null;
	}
	
	List<Integer> find_right_sample_ok_3(List<String> wifi_ssid,
			Map<String, Double> wifi_up, List<Integer> sample_id_list,
			DatabaseManager m_DatabaseManager) {

		// TODO 寻找距离最近的K个采样信息
		
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
				 double distance=m_Distance.distance_double(wifi_up,temp_map);
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
	
    /************************find_coordinate*****************************/
	@Override
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
    		//计算k个点中，每个点分别与其他点距离的总和
    		double distance_sum_all=Double.MAX_VALUE;//保存距离之和最小的距离值
    		int id_sum_all=0;//距离值和最小的样本id
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
//      			System.out.println("remove:"+ok_sample_id.get(id)+"    X:  "+ sample_id_xy.get(ok_sample_id.get(id))[0]+"   Y:  "+sample_id_xy.get(ok_sample_id.get(id))[1]);    
//      			ok_sample_id.remove(id);
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
//				  		System.out.println("x:"+temp_x*x_pixl/1000.0+"	y:"+temp_y*p_pixl/1000.0+"	id:"+s+"   distance:  "+distance_map.get(s));
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

	@Override
	void initial_train() {
		// TODO Auto-generated method stub

	}
	public Map<String, Double> section_find_coordinate(List<Integer> ok_sample_id,
			List<String> wifi_ssid, Map<String, Double> wifi_up,
			DatabaseManager m_DatabaseManager) throws IOException {
		// TODO Auto-generated method stub
		
		int temp_map_id=1;

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
//	  			System.out.println("remove:"+ok_sample_id.get(id)+"    X:  "+ sample_id_xy.get(ok_sample_id.get(id))[0]+"   Y:  "+sample_id_xy.get(ok_sample_id.get(id))[1]);    
//	  			ok_sample_id.remove(id);
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
//				  		System.out.println("x:"+temp_x*x_pixl/1000.0+"	y:"+temp_y*p_pixl/1000.0+"	id:"+s+"   distance:  "+distance_map.get(s));
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
	
	
  /*****************LDA_coordinate
 * @param wifi_ssid 
 * @throws IOException ****************************/
	Map<String, Double> LDA_coordinate(List<Integer> ok_sample_id,List<String> wifi_ssid, Map<String, Double> wifi_up,DatabaseManager m_DatabaseManager) throws IOException{
		ArrayList<Integer>  section_id=new ArrayList<Integer>();
		ArrayList<Integer> All_XY=m_DatabaseManager.get_all_xy(1);  //得到所有参考点的坐标
		ArrayList<Integer> x_y=new ArrayList();
		HashMap<Integer,Integer> section_frequency=new HashMap<Integer,Integer>();
		ArrayList<Integer> Section=new ArrayList();
	     /***判断找到参考点所在的区域***/	
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
		for(int i=0;i<x_y.size();i=i+2){
			int sec=m_DatabaseManager.get_sample_section(x_y.get(i),x_y.get(i+1));
			if(section_frequency.containsKey(sec)){
				int value=section_frequency.get(sec)+1;
				section_frequency.put(sec, value);
			}else{
				section_frequency.put(sec, 1);
			}			
		}
		//找出包含两个及以上邻接点的区域id
		Iterator iter=section_frequency.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry= (Entry) iter.next();
			int value=(Integer) entry.getValue();
			if(value>=2){
				Section.add((Integer) entry.getKey());
			}
		}
		/**********找出距离定位点最近的参考点以及它所在的区域**********/
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
		int LocationSection = m_DatabaseManager.get_sample_section(Near_x,
				Near_y);
		location_section_id.add(LocationSection);
		/*****根据上一步所选区域，找出区域中所有参考点坐标*****/
		ArrayList<String> section_xy = new ArrayList<String>();
		ArrayList<String> X_Y = new ArrayList<String>();// 存储所选区域所有参考点坐标
		for (int i = 0; i < Section.size(); i++) {
			section_xy = m_DatabaseManager.get_section_xy(Section.get(i));
			for (int j = 0; j < section_xy.size(); j++) {
				String temp = section_xy.get(j);
				if (!X_Y.contains(temp)) {
					X_Y.add(temp);
				}
			}
		}

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
		
		/*******在所选区域进行section的knn定位**定位结果存储在all_res1文件夹中*****/

		List<Integer> ok_sample_id_3=find_right_sample_ok_3(wifi_ssid,wifi_up, Renerence_sample_id,m_DatabaseManager);
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
		Map<String,Double> coordinate_3=section_find_coordinate(ok_sample_id_3,wifi_ssid,wifi_up,m_DatabaseManager);
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
        	 HashMap<String,Double> result_fianl=new HashMap<String,Double>();
             result_fianl.put("x",Result_x );
             result_fianl.put("y",Result_y );
             return result_fianl;
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
    
        /* *//*******在所选区域section进行K-means定位**定位结果存储在all_res2文件夹中*****//*   
         
         List<Integer> Renerence_sample_id2 =m_DatabaseManager.get_wifi_sample_id_in_map(1);
         
         ArrayList Reference_infor=new ArrayList();        
         ArrayList<ArrayList<Double>> outer=new ArrayList<ArrayList<Double>>();
         ArrayList<String> temp_wifi_ssid=new ArrayList<String>();
         temp_wifi_ssid.addAll(wifi_ssid);
         for(Integer id:Renerence_sample_id){
        	 HashMap<String ,Integer> ReInfo=m_DatabaseManager.get_wifi_info(id);
        	 Reference_infor.add(ReInfo);
         }
         for(int i=0;i<Reference_infor.size();i++){
        	 HashMap<String ,Integer> temp=(HashMap<String, Integer>) Reference_infor.get(i);
        	 Iterator<Entry<String, Integer>> it = temp.entrySet().iterator(); 
        	 List<String> first_temp= new ArrayList<String>();
        	 while(it.hasNext()){ 
        		 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)it.next();
        		 first_temp.add((String) entry.getKey()); 
        	 }
        	 temp_wifi_ssid.retainAll(first_temp);
         }
         for(int i=0;i<Reference_infor.size();i++){
        	 HashMap<String ,Integer> temp=(HashMap<String, Integer>) Reference_infor.get(i);
        	 ArrayList<Double> inner=new ArrayList<Double>();
        	 for(int k=0;k<temp_wifi_ssid.size();k++){
        		 inner.add((double)temp.get(temp_wifi_ssid.get(k)));        		 
        	 }
        	 outer.add(inner);
         }

         ArrayList<Double> wifi_up_temp=new ArrayList<Double>();
         for(int i=0;i<temp_wifi_ssid.size();i++){
        	 wifi_up_temp.add((double)wifi_up.get(temp_wifi_ssid.get(i)));
         }
         outer.add(wifi_up_temp);         
         KMeans k = new KMeans();
         Euclidean d=new Euclidean();
         InitCentroid init = new InitCentroid();
         int maxIteration = 1000;
 	     int numCluster = 10;
 	     ArrayList<ArrayList<ArrayList<Double>>> KMeans=k.kMeans(outer, maxIteration, d, numCluster, init);
 	     boolean Flag=false;
 	     ArrayList<ArrayList<Double>> Cluster=new ArrayList<ArrayList<Double>>();
 	     for(int i=0;i<KMeans.size();i++){
 	    	ArrayList<ArrayList<Double>> out=KMeans.get(i);
 	    	for(int j=0;j<out.size();j++){
 	    		ArrayList<Double> inner=out.get(j);
 	    		if(inner.containsAll(wifi_up_temp)){
 	    			Flag=true;
 	    			break;
 	    		}
 	    	}
 	    	if(Flag){
 	    		Cluster=out;
 	    		break;
 	    	}
 	     }
 	    ArrayList<Integer> ok_sample_id_4=new ArrayList<Integer>();
 	    for(Integer id:Renerence_sample_id){
 	    	HashMap<String ,Integer> ReInfo=m_DatabaseManager.get_wifi_info(id);
 	    	int match=0;
 	    	for(int i=0;i<Cluster.size();i++){
 	    		for(int j=0;j<Cluster.get(i).size();j++){
 	    			if((ReInfo.containsKey(temp_wifi_ssid.get(j)))&&((double)ReInfo.get(temp_wifi_ssid.get(j))==Cluster.get(i).get(j))){
 	    				match++;
 	    			}else{
 	    				break;
 	    			}
 	    		}
 	    		if(match==temp_wifi_ssid.size()){
 	    			ok_sample_id_4.add(id);
 	    			break;
 	    		}
 	    	}
 	    }
		Map<String, Double> coordinate_4 = section_find_coordinate(
				ok_sample_id_4, wifi_ssid, wifi_up, m_DatabaseManager);
		double Kmeans_x = coordinate_4.get("x");
		double Kmeans_y = coordinate_4.get("y");
		double L_x_4 = Kmeans_x * 1568 / 1000;
		double L_y_4 = Kmeans_y * 988 / 1000;
		
		 FileWriter section_fw_temp;        	 
		 section_fw_temp=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_res"+10+".txt",true);
		 section_fw_temp.write("		"+L_x_4+"			"+L_y_4+"		\n");
		 section_fw_temp.flush();
		 section_fw_temp.close();

		
		ArrayList<Integer> x_y_4 = new ArrayList();
		for (int i = 0; i < ok_sample_id_4.size(); i++) {
			ResultSet rs1 = m_DatabaseManager
					.get_ResultSet_from_wifi_sample(ok_sample_id_4.get(i));
			try {
				if (rs1.next()) {
					int id_x_4 = rs1.getInt("x");
					int id_y_4 = rs1.getInt("y");
					x_y_4.add(id_x_4);
					x_y_4.add(id_y_4);
				}
				rs1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		int Near_x_4=0,Near_y_4=0;
		double Dis_4=Double.MAX_VALUE;
		for(int i=0;i<All_XY.size();i=i+2){//x_y_4
			   double x_temp=Math.pow(L_x_4-All_XY.get(i), 2);
			   double y_temp=Math.pow(L_y_4-All_XY.get(i+1), 2);
			   double dis_4=Math.sqrt(x_temp+y_temp);
			   if(dis_4<Dis_4){
				   Dis_4=dis_4;
				   Near_x_4=All_XY.get(i);
				   Near_y_4=All_XY.get(i+1);
			   }		 
		}
		 int LocationSection_4=m_DatabaseManager.get_sample_section(Near_x_4,Near_y_4);
		 Kmeans_section_id.add(LocationSection_4);
		 int Kmeans_count=0;
		 for(int i=0;i<Kmeans_section_id.size();i++){
			 if(LocationSection_4==Kmeans_section_id.get(i)){
				 Kmeans_count++;
			 }
		 }
		 if(((double)Kmeans_count)/Kmeans_section_id.size()>0.3){

			 Kmeans_Result_x=coordinate_4.get("x");
			 Kmeans_Result_y=coordinate_4.get("y");
			 double tt_x_4=Kmeans_Result_x*1568/1000;
			 double tt_y_4=Kmeans_Result_y*988/1000;
			 FileWriter section_fw_4;        	 
			 section_fw_4=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_res"+2+".txt",true);
			 section_fw_4.write("		"+tt_x_4+"			"+tt_y_4+"		\n");
			 section_fw_4.flush();
			 section_fw_4.close();
		 
		 }else{

			 double tt_x_4=Kmeans_Result_x*1568/1000;
			 double tt_y_4=Kmeans_Result_y*988/1000;
			 FileWriter section_fw_4;        	 
			 section_fw_4=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_res"+2+".txt",true);
			 section_fw_4.write("		"+tt_x_4+"			"+tt_y_4+"		\n");
			 section_fw_4.flush();
			 section_fw_4.close();
		 
		 }
	

  	    
  	 *//*************在区分好的section进行LDA变换然后进行定位**********************//*   
		ArrayList<ArrayList<ArrayList<Double>>> lda_Class = new ArrayList<ArrayList<ArrayList<Double>>>();
		lda_Class.addAll(KMeans);
		int size=temp_wifi_ssid.size();
		HashMap<Double, ArrayList<Double>> Aftertrans=new HashMap<Double, ArrayList<Double>>();
		double[][] Sw = SW(lda_Class, temp_wifi_ssid);
		double[][] Sb = SB(lda_Class, temp_wifi_ssid);
		double[][] trans = TRANS(Sb, Sw, temp_wifi_ssid);
		Matrix Trans=new Matrix(trans);
        double[][] lda_wifi_up=new double[1][size];
        for(int i=0;i<size;i++){
        	lda_wifi_up[0][i]=wifi_up_temp.get(i);
        }
        Matrix LDA_wifi_up=new Matrix(lda_wifi_up);
        Matrix Trans_wifi_up=Trans.times(LDA_wifi_up.transpose());
        double[][] trans_wifi_up=Trans_wifi_up.getArray(); //将上传信息进行转换
        for(int i=0;i<lda_Class.size();i++){
        	ArrayList<ArrayList<Double>> EveryClass = lda_Class.get(i);
        	for (int j = 0; j < EveryClass.size(); j++) {
        		ArrayList<Double> Info = EveryClass.get(j);
        		double[][] temp=new double[1][size];
        		 for(int h=0;h<size;h++){
        			 temp[0][h]=Info.get(h);
        		 }
        		 Matrix Temp= new Matrix(temp);
        		 Matrix Trans_Temp=Trans.times(Temp.transpose());
        		 double[][] trans_temp=Trans_Temp.getArray();
        		 double distance=DISTANCE(trans_wifi_up,trans_temp);
        		 Aftertrans.put(distance, Info);
        	}
        }
        ArrayList Aftertrans_key=new ArrayList();
        Iterator key_it=Aftertrans.entrySet().iterator();
        while(key_it.hasNext()){
        	Map.Entry entry = (Map.Entry) key_it.next();
        	double key=(Double) entry.getKey();
        	Aftertrans_key.add(key);
        }
        Collections.sort(Aftertrans_key);

        ArrayList<ArrayList<Double>> LDA=new ArrayList<ArrayList<Double>>();
        LDA.add(Aftertrans.get(Aftertrans_key.get(2)));
        LDA.add(Aftertrans.get(Aftertrans_key.get(3)));
        LDA.add(Aftertrans.get(Aftertrans_key.get(4)));

        
        ArrayList<Integer> ok_sample_id_5=new ArrayList<Integer>();
        for(Integer id:Renerence_sample_id){
 	    	HashMap<String ,Integer> ReInfo=m_DatabaseManager.get_wifi_info(id);
 	    	int match=0;
 	    	for(int i=0;i<LDA.size();i++){
 	    		for(int j=0;j<LDA.get(i).size();j++){
 	    			if((ReInfo.containsKey(temp_wifi_ssid.get(j)))&&((double)ReInfo.get(temp_wifi_ssid.get(j))==LDA.get(i).get(j))){
 	    				match++;
 	    			}else{
 	    				break;
 	    			}
 	    		}
 	    		if(match==temp_wifi_ssid.size()){
 	    			ok_sample_id_5.add(id);
 	    			break;
 	    		}
 	    	}
 	    }
        
		Map<String, Double> coordinate_5 = section_find_coordinate(
				ok_sample_id_5, wifi_ssid, wifi_up, m_DatabaseManager);
		double LDA_x = coordinate_5.get("x");
		double LDA_y = coordinate_5.get("y");
		double L_x_5 = LDA_x * 1568 / 1000;
		double L_y_5 = LDA_y * 988 / 1000;
		ArrayList<Integer> x_y_5=new ArrayList();
		for(int i=0;i<ok_sample_id_5.size();i++){

			ResultSet  rs1=m_DatabaseManager.get_ResultSet_from_wifi_sample(ok_sample_id_5.get(i));
		    try {
				if(rs1.next())
				  {
				    int id_x_5=rs1.getInt("x");
				   	int id_y_5=rs1.getInt("y");
				   	x_y_5.add(id_x_5);
				   	x_y_5.add(id_y_5); 
				  }
				rs1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		 int Near_x_5=0,Near_y_5=0;
		 double Dis_5=Double.MAX_VALUE;
		 for(int i=0;i<All_XY.size();i=i+2){//x_y_5

			   double x_temp=Math.pow(L_x_5-All_XY.get(i), 2);
			   double y_temp=Math.pow(L_y_5-All_XY.get(i+1), 2);
			   double dis_5=Math.sqrt(x_temp+y_temp);
			   if(dis_5<Dis_5){
				   Dis_5=dis_5;
				   Near_x_5=All_XY.get(i);
				   Near_y_5=All_XY.get(i+1);
			   }
		 
		 }
		 int LocationSection_5=m_DatabaseManager.get_sample_section(Near_x_5,Near_y_5);
		 Lda_section_id.add(LocationSection_5);
		 int Lda_count=0;
		 for(int i=0;i<Lda_section_id.size();i++){
			 if(LocationSection_5==Lda_section_id.get(i)){
				 Lda_count++;
			 }
		 }
		 if(((double)Lda_count)/Lda_section_id.size()>0.3){

			 LDA_Result_x=coordinate_5.get("x");
			 LDA_Result_y=coordinate_5.get("y");
			 double tt_x_5=LDA_Result_x*1568/1000;
			 double tt_y_5=LDA_Result_y*988/1000;
			 FileWriter section_fw_5;        	 
			 section_fw_5=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_res"+3+".txt",true);
			 section_fw_5.write("		"+tt_x_5+"			"+tt_y_5+"		\n");
			 section_fw_5.flush();
			 section_fw_5.close();
		 
		 }else{

			 double tt_x_5=LDA_Result_x*1568/1000;
			 double tt_y_5=LDA_Result_y*988/1000;
			 FileWriter section_fw_5;        	 
			 section_fw_5=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_res"+3+".txt",true);
			 section_fw_5.write("		"+tt_x_5+"			"+tt_y_5+"		\n");
			 section_fw_5.flush();
			 section_fw_5.close();
		 
		 }
		 
		 


        
        
        
        
		coordinate.clear();
		coordinate_3.clear();
		coordinate_4.clear();
		coordinate_5.clear();*/
        
		
	}
private double DISTANCE(double[][] trans_wifi_up, double[][] trans_temp) {
	// TODO Auto-generated method stub
	double Distance=0;
	for(int i=0;i<trans_wifi_up.length;i++){
		for(int j=0;j<trans_wifi_up[i].length;j++){
			Distance+=Math.pow((trans_wifi_up[i][j]-trans_temp[i][j]), 2);
		}
	}
	Distance=Math.sqrt(Distance);
	return Distance;
}
private double[][] TRANS(double[][] sb, double[][] sw, ArrayList<String> temp_wifi_ssid) {
	// TODO Auto-generated method stub
	Matrix SW=new Matrix(sw);
	Matrix SB=new Matrix(sb);
	Matrix Sw_Sb=SW.inverse().times(SB);
	double[] SingularValue=Sw_Sb.svd().getSingularValues();
	Matrix trans=Sw_Sb.svd().getU();
	double[][] Trans=trans.getArray();
	int SingularValue_size=0;
	
	double SingularValue_sum=0;
	for(int i=0;i<SingularValue.length;i++){
		SingularValue_sum+=SingularValue[i];
	}
	for(int i=0;i<SingularValue.length;i++){
		SingularValue_size=i;
		double temp=0;
		for(int j=0;j<=i;j++){
			temp+=SingularValue[j];
		}
		if(temp/SingularValue_sum>0.99){
			break;
		}
	}
	
	double[][] ans=new double[SingularValue_size][temp_wifi_ssid.size()];
	for(int i=0;i<ans.length;i++){
		for(int j=0;j<ans[i].length;j++){
			ans[i][j]=Trans[i][j];
		}
	}
	return ans;
	
}
private double[][] SB(ArrayList<ArrayList<ArrayList<Double>>> lda_Class, ArrayList<String> temp_wifi_ssid) {
	// TODO Auto-generated method stub
		int size = temp_wifi_ssid.size();
		int lda_All = 0;
		double[] AllClass_average = new double[size];
		Matrix SB = new Matrix(size, size, 0);
		for (int i = 0; i < lda_Class.size(); i++) {
			ArrayList<ArrayList<Double>> EveryClass = lda_Class.get(i);
			lda_All += EveryClass.size();
			for (int j = 0; j < EveryClass.size(); j++) {
				ArrayList<Double> Info = EveryClass.get(j);
				for (int k = 0; k < Info.size(); k++) {
					AllClass_average[k] += Info.get(k);
				}
			}
		}
		for (int i = 0; i < size; i++) {
			AllClass_average[i] = AllClass_average[i] / lda_All;
		}
		for (int i = 0; i < lda_Class.size(); i++) {
			ArrayList<ArrayList<Double>> EveryClass = lda_Class.get(i);
			int everyclass = EveryClass.size();
			double[] everyClass_average = new double[size];
			for (int j = 0; j < EveryClass.size(); j++) {
				ArrayList<Double> Info = EveryClass.get(j);
				for (int k = 0; k < Info.size(); k++) {
					everyClass_average[k] += Info.get(k);
				}
			}
			for (int j = 0; j < size; j++) {
				everyClass_average[j] = everyClass_average[j] / everyclass;
			}
			double[][] temp = new double[1][size];
			for (int j = 0; j < size; j++) {
				temp[0][j] = everyClass_average[j] - AllClass_average[j];
			}
			Matrix Temp = new Matrix(temp);
			Matrix Temp1 = (Temp.transpose().times(Temp)).times(everyclass);
			SB.plusEquals(Temp1);
		}
		double[][] sb = SB.getArray();
		for (int i = 0; i < sb.length; i++) {
			for (int j = 0; j < sb[0].length; j++) {
				sb[i][j] = sb[i][j] / lda_All;
			}
		}
		return sb;
}
private double[][] SW(ArrayList<ArrayList<ArrayList<Double>>> lda_Class, ArrayList<String> temp_wifi_ssid) {
	// TODO Auto-generated method stub
		int size = temp_wifi_ssid.size();
		Matrix SW = new Matrix(size, size, 0);
		for (int i = 0; i < lda_Class.size(); i++) {
			ArrayList<ArrayList<Double>> EveryClass=lda_Class.get(i);
			double[]  EveryClass_average=new double[size];
			double[][] Difference=new double[1][size];
			Matrix EveryClass_Matrix=new Matrix(size,size,0);
			for (int j = 0; j < EveryClass.size(); j++) {
				ArrayList<Double> Info = EveryClass.get(j);
				for (int k = 0; k < Info.size(); k++) {
					EveryClass_average[k] += Info.get(k);
				}
			}
			for(int h=0;h<size;h++){
				EveryClass_average[h]=EveryClass_average[h]/EveryClass.size();
			}
			for (int j = 0; j < EveryClass.size(); j++) {
				ArrayList<Double> Info = EveryClass.get(j);
				for (int k = 0; k < Info.size(); k++) {
					Difference[0][k] = Info.get(k) - EveryClass_average[k];
				}
				Matrix lda_Temp1 = new Matrix(Difference);
				Matrix lda_Temp2 = lda_Temp1.transpose().times(lda_Temp1);
				EveryClass_Matrix.plusEquals(lda_Temp2);
			}
			EveryClass_Matrix.timesEquals(EveryClass.size());
			SW.plusEquals(EveryClass_Matrix);
		}
		int lda_All = 0;
		for (int i = 0; i < lda_Class.size(); i++) {
			ArrayList<ArrayList<Double>> EveryClass = lda_Class.get(i);
			lda_All += EveryClass.size();
		}
		
		double[][] sw=SW.getArray();
		for(int i=0;i<sw.length;i++){
	    	   for(int j=0;j<sw[0].length;j++){
	    		   sw[i][j]=sw[i][j]/lda_All;
	    	   }
	       }
		return sw;
}

}
