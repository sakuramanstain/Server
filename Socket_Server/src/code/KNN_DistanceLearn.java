package code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class KNN_DistanceLearn extends Algorithm {
	//////K-NN迭代计算 距离最近的若干点 半径逐渐增大
	int temp_map_id=0;
	Distance m_Distance=new DistanceAbsolute();//DistanceEuclidean();
	@Override
	int find_map_id(List<String> wifi_ssid, Map<String, Double> wifi_up,
			DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		List<Integer> cluster_id_list =m_DatabaseManager.get_ALL_cluster_ID();
		int sim=-1;//最大相似度
	    List<Integer> sim_max_cluster_id=new ArrayList<Integer>();//相似度可能会出现相同的多个簇
	    for(Integer id:cluster_id_list)
	    {
	       HashMap<String ,Integer> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info(id);
	       List<String> temp= new ArrayList<String>();
	       Iterator<Entry<String, Integer>> iter = temp_map.entrySet().iterator(); 
	       while(iter.hasNext())
	        { 
	           Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
	           temp.add((String) entry.getKey()); 
	        }
	       temp.retainAll(wifi_ssid);
	       if(temp.size()>sim)///相似度大于原始的数据 原来的情况
	        {
	           		sim_max_cluster_id.clear();
	           		sim_max_cluster_id.add(id);
	           		sim=temp.size();
	        }else if(temp.size()==sim)//相似度相同 将本簇ID保存
	        {
	           		sim_max_cluster_id.add(id);
	           		
	        }else
	        {
	           		
	        }
	    }
	    System.out.println("相似度:"+sim);
	   /* System.out.println("相似cluster_id:");
	    for(Integer out:sim_max_cluster_id)
	    {
	        System.out.println("id:"+out);
	    }*/
	    System.out.println("****************");
	    double min_distance=Double.MAX_VALUE;
		int cluster_id=0;
        for(Integer id:sim_max_cluster_id)
        {
        	HashMap<String ,Double[]> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info_delt(id);
        	HashMap<String ,Integer> temp_map1=m_DatabaseManager.get_cluster_centre_wifi_info(id);
 	       List<String> temp= new ArrayList<String>();
 	       Iterator<Entry<String, Integer>> iter = temp_map1.entrySet().iterator(); 
 	       while(iter.hasNext())
 	        { 
 	           Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
 	           temp.add((String) entry.getKey()); 
 	        }
 	       temp.retainAll(wifi_ssid);
 	       double distance=0;
 	       for(String one:temp)
 	       {
 	    	   distance+=temp_map.get(one)[1]*getdistance(temp_map.get(one)[0],wifi_up.get(one));
 	       }
 	      if(distance<min_distance)
    	   {
 	    	 cluster_id=id;
    	   }
        }//for
        int map_id=m_DatabaseManager.get_map_ID_from_cluster_centre(cluster_id);
        temp_map_id=map_id;
		return map_id;
	}
	
	@Override
	List<Integer> find_right_sample_ok(List<String> wifi_ssid ,Map<String,Double> wifi_up,int map_id,DatabaseManager m_DatabaseManager)
	{
		// TODO Auto-generated method stub
		 List<Integer> sample_id_list =m_DatabaseManager.get_ALL_cluster_ID(map_id);
		 double step=0;
         List<Integer> ok_sample_id=new ArrayList<Integer>();
         int K_NN=4;
         /////标准的K-NN算法 取最近的K个
         if(K_NN>=sample_id_list.size())
         {
        	 ok_sample_id.addAll(sample_id_list);
         }else
         {
        	 Integer [] K_NN_OK=new Integer[K_NN];
        	 Double[] K_NN_value_OK=new Double[K_NN];
        	 for(int j=0;j<K_NN;j++)
        	 {
        		 K_NN_value_OK[j]=Double.MAX_VALUE-K_NN-1+j;
        		 K_NN_OK[j]=-1;
        	 }
        	 for(int i=0;i<sample_id_list.size();i++)
        	 {
        		///////////////////////////////////////////
        		 
        		HashMap<String ,Double[]> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info_delt(sample_id_list.get(i));
             	HashMap<String ,Integer> temp_map1=m_DatabaseManager.get_cluster_centre_wifi_info(sample_id_list.get(i));
      	        List<String> temp= new ArrayList<String>();
      	        Iterator<Entry<String, Integer>> iter = temp_map1.entrySet().iterator(); 
      	        while(iter.hasNext())
      	         { 
      	           Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
      	           temp.add((String) entry.getKey()); 
      	         }
      	        temp.retainAll(wifi_ssid);
      	        double distance=0;
      	        for(String one:temp)
      	        {
      	    	   distance+=temp_map.get(one)[1]*getdistance(temp_map.get(one)[0],wifi_up.get(one));
      	        }
        		 //////////////////////////////////////////////
        		 if(distance<K_NN_value_OK[K_NN-1])
        		 {
        			 int ii=K_NN-1;
        			 K_NN_OK[K_NN-1]=sample_id_list.get(i);
        			 K_NN_value_OK[K_NN-1]=distance;
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
        	 for(Integer ok_id:K_NN_OK)
        	 {
        		 ok_sample_id.add(ok_id);
        	 }
         }
         /////寻找匹配的位置点ID
         //采用迭代的方法 发现某个范围内的所有符合条件的点 限制依次放松
        /* while(ok_sample_id.size()<1)
         {
        	 ok_sample_id.clear();
        	 for(Integer id:sample_id_list)
             {
        		
        		 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
        		 double distance=m_Distance.distance_double(wifi_up,temp_map);
        		 if(distance<step)
        		 {
        			 ok_sample_id.add(id);
        			 System.out.println(""+id);
        		 }
        		
            	 
             }//for
        	 System.out.println("当前阈值："+step);
        	 step=step+0.25;
        	 if(step>100)
        	 {
        			
        		System.out.println("失败 ");
        		break;
        	 }
        		 
         }//while 
         */
         return ok_sample_id;
	}
	@Override
	Map<String,Double> find_coordinate(List<Integer> ok_sample_id,List<String> wifi_ssid,Map<String,Double> wifi_up,DatabaseManager m_DatabaseManager)
	{
		
		HashMap<Integer ,Double> distance_map = new HashMap<Integer, Double>();
        /////计算比例
        for(Integer id:ok_sample_id)
        {
        	int sample_id=m_DatabaseManager.get_sample_id_from_cluster_id(id);
        	HashMap<String ,Double[]> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info_delt(id);
         	HashMap<String ,Integer> temp_map1=m_DatabaseManager.get_cluster_centre_wifi_info(id);
  	        List<String> temp= new ArrayList<String>();
  	        Iterator<Entry<String, Integer>> iter = temp_map1.entrySet().iterator(); 
  	        while(iter.hasNext())
  	         { 
  	           Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
  	           temp.add((String) entry.getKey()); 
  	         }
  	        temp.retainAll(wifi_ssid);
  	        double distance=0;
  	        for(String one:temp)
  	        {
  	    	   distance+=temp_map.get(one)[1]*getdistance(temp_map.get(one)[0],wifi_up.get(one));
  	        }
        	 /* List<String> temp= new ArrayList<String>();
        	 Iterator<Entry<String, Integer>> iter = temp_map.entrySet().iterator(); 
        	 while(iter.hasNext())
        	 { 
        		 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
        		 temp.add((String) entry.getKey()); 
        	 } 
           	 temp.retainAll(wifi_ssid);
           	 double distance=0;
           	 for(String t:temp)
           	 {
           		 distance=distance+Math.pow(temp_map.get(t)-wifi_up.get(t), 2);
           	 }
           	 distance=Math.sqrt(distance);
           	 distance=distance/temp.size();
           	 */
        	 distance=1/distance;//Math.exp(-2*distance);
           	 distance_map.put(sample_id, distance);
           	System.out.println(temp);
           	System.out.println(""+sample_id+" :  "+distance);
       	
        }
        ///加权
        
        Set<Integer> key = distance_map.keySet(); 
        double _x=0;
        double _y=0;
        double weight_all=0;
       	for (java.util.Iterator<Integer> it = key.iterator(); it.hasNext();)
       	{            
       		Integer s = (Integer) it.next(); 
  				
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
				  		System.out.println("x:"+temp_x+"	y:"+temp_y+"	id:"+s);
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
			double x_pixl=1191.0;
			double p_pixl=851.0;
			fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_res"+temp_map_id+".txt",true);
			double ttx=_x*x_pixl/1000.0;
			double tty=_y*p_pixl/1000.0;
			
			fw.append(""+ttx+"			"+tty+"		\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		return xy_map;
	}
	@Override
	int train(DatabaseManager m_DatabaseManager) {
		// 无训练

		// TODO Auto-generated method stub
		////////////距离计算方法
		m_DatabaseManager.Delete_ALL_Cluster();
		
		 
		///////////所有数据集合
		List<Integer> map_id=m_DatabaseManager.get_map_id();
		
		for(Integer temp_map_id:map_id)
		{
			System.out.println("map_id:"+temp_map_id);
			List<Integer> all_sample_id= m_DatabaseManager.get_wifi_sample_id_in_map(temp_map_id);
			////////////////
			{

				FileWriter fw;
				try {
					fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_xy_基准点位置-"+temp_map_id+".txt");
				
				
					for(Integer temp:all_sample_id)
					{
						ResultSet  rs1=m_DatabaseManager.get_ResultSet_from_wifi_sample(temp);
						int temp_x=0;
					   	int temp_y=0;
			            try {
			            	
							if(rs1.next())
							  {
							   	temp_x=rs1.getInt("x");
							   	temp_y=rs1.getInt("y");
							  }
							rs1.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String dd="";
						dd=""+temp_x+"	"+temp_y;
						dd+="\n";
						fw.write(dd);
					}
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			long a=System.currentTimeMillis();
			
			try {
				File temp_distance=new File("F:"+File.separator+"workspace"+File.separator+"点之间的距离"+temp_map_id+".txt");
				if(temp_distance.exists())
				{
					temp_distance.delete();
					temp_distance.createNewFile();
				}else
					temp_distance.createNewFile();
				//fw_temp = new FileWriter (temp_distance,true);
				BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp_distance)));  
				for(Integer temp_Integer_from:all_sample_id)
				{
					HashMap<String ,Integer> from=m_DatabaseManager.get_wifi_info(temp_Integer_from);
					double distance_sum=0;
					String dd="";
					for(Integer temp_Integer_to:all_sample_id)
					{
						HashMap<String ,Integer> to=m_DatabaseManager.get_wifi_info(temp_Integer_to);
						double distance_from_to=m_Distance.distance(from, to);
						dd+=""+distance_from_to+"   ";
					}
					bw.write(dd);
					bw.newLine();
					bw.flush();
				}
				//fw_temp.close();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
			
			
			
			
			
			
			////////////////求交集
			List<String> dimension_list = new ArrayList<String>();   
			{
				Map<String,Integer> wifi_info =m_DatabaseManager.get_wifi_info(all_sample_id.get(0));
		      	Iterator<Entry<String, Integer>> iter = wifi_info.entrySet().iterator(); 
		      	while(iter.hasNext()){ 
		      	  Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
		      	  dimension_list.add((String) entry.getKey()); 
		      	 } 
			}
			for(Integer temp:all_sample_id)
			{
				Map<String,Integer> wifi_info =m_DatabaseManager.get_wifi_info(temp);
				List<String> one_Dimension=new ArrayList<String>();
				Iterator<Entry<String, Integer>> iter = wifi_info.entrySet().iterator(); 
		      	while(iter.hasNext()){ 
		      	  Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
		      	 one_Dimension.add((String) entry.getKey()); 
		      	 }
		      	dimension_list.retainAll(one_Dimension);
			}
			File File_temp=new File("F:" +File.separator+ "workspace" + File.separator+"tempfile");
			if(File_temp.exists())
			{
				File [] m_file=File_temp.listFiles();
				for(File one:m_file)
				{
					one.delete();
				}
			}else
			{
				File_temp.mkdir();
			}
			//////计算距离
			for(Integer from:all_sample_id)
			{
				
				try {
					
					BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("F:" +File.separator+ "workspace" + File.separator+"tempfile"+File.separator+from+"_id_all.txt")))); 
					Map<String,Integer> wifi_info_from =m_DatabaseManager.get_wifi_info(from);
					
					for(Integer to:all_sample_id)
					{
						Map<String,Integer> wifi_info_to =m_DatabaseManager.get_wifi_info(to);
						String dd="";//+to+"  ";
						for(String one:dimension_list)
						{
							double value=getdistance(wifi_info_from.get(one),wifi_info_to.get(one));;//Math.pow(wifi_info_from.get(one)-wifi_info_to.get(one), 2);
							dd+=""+value+"  ";
						} 
						bw.write(dd);
						bw.newLine();
					}
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/////
			double [] P=new double[dimension_list.size()];
			for(double m_i:P)
			{
				m_i=0;
			}
			
			for(Integer from:all_sample_id)
			{
				BufferedReader in_txt;
				try {
					in_txt = new BufferedReader(new FileReader("F:" +File.separator+ "workspace" + File.separator+"tempfile"+File.separator+from+"_id_all.txt"));
					String con="";
					try {
					
						while((con=in_txt.readLine())!=null)
						{
							String [] value=con.split("\\s+");
						
							for(int i=0;i<value.length;i++)
							{
								P[i]+=Double.parseDouble(value[i]);
							}
						
						}
						in_txt.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		
			////////////////////////
			
			double [][] PP=new double[dimension_list.size()][dimension_list.size()];
			for(Integer from:all_sample_id)
			{
				BufferedReader in_txt;
				try {
					in_txt = new BufferedReader(new FileReader("F:" +File.separator+ "workspace" + File.separator+"tempfile"+File.separator+from+"_id_all.txt"));
					String con="";
					try {
						while((con=in_txt.readLine())!=null)
						{
							String [] value=con.split("\\s+");
							for(int i=0;i<value.length;i++)
							{
								double m_i=Double.parseDouble(value[i]);
								int j=0;
								while(j<value.length)
								{
									double m_j=Double.parseDouble(value[j]);
									PP[i][j]+=m_i*m_j;
									
									j++;
								}
							}
							
						}
						in_txt.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			InvMatrix m_inv=new InvMatrix();
			double [][] result=m_inv.Inv(PP, dimension_list.size());
			PP=null;
			double [] result_w=new double[dimension_list.size()];
			for(int i=0;i<dimension_list.size();i++)
			{
				double value=0;
				for(int j=0;j<dimension_list.size();j++)
				{
					value+=result[i][j]*P[j];
				}
				result_w[i]=value;
			}
			System.out.print("加权系数");
			System.out.println(dimension_list);
			for (int i = 0; i < dimension_list.size(); i++) {
				System.out.print(""+result_w[i]+"	");
			}
			//////////////////////////////////////////
			a=System.currentTimeMillis();
			
			try {
				File temp_distance=new File("F:"+File.separator+"workspace"+File.separator+"处理后点之间的距离"+temp_map_id+".txt");
				if(temp_distance.exists())
				{
					temp_distance.delete();
					temp_distance.createNewFile();
				}else
					temp_distance.createNewFile();
				//fw_temp = new FileWriter (temp_distance,true);
				   BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp_distance)));  
				for(Integer temp_Integer_from:all_sample_id)
				{
					HashMap<String ,Integer> from=m_DatabaseManager.get_wifi_info(temp_Integer_from);
					String dd="";
					for(Integer temp_Integer_to:all_sample_id)
					{
						HashMap<String ,Integer> to=m_DatabaseManager.get_wifi_info(temp_Integer_to);
						int i=0;
						double distance_from_to=0;
						for(String one:dimension_list)
						{
							distance_from_to+=result_w[i]*getdistance(from.get(one),to.get(one));//Math.pow(from.get(one)-to.get(one), 2);
							i++;
						}
						dd+=""+distance_from_to+"   ";
					}
					bw.write(dd);
					bw.newLine();
					bw.flush();
				}
				//fw_temp.close();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
			for(Integer temp_Integer_from:all_sample_id)
			{
				int cluster_id=m_DatabaseManager.insert_into_cluster_centre(temp_Integer_from,temp_map_id);
				int i=0;
				HashMap<String ,Integer> one=m_DatabaseManager.get_wifi_info(temp_Integer_from);
				for(String name:dimension_list)
				{
					m_DatabaseManager.insert_into_cluster_centre_wifi_info(cluster_id, name, one.get(name), result_w[i], 0, 0, 0);
		       		i++;
				}
			}
			result_w=null;
		}

		
		return 0;
	}
	double getdistance(double m,double n)
	{
		double value=Math.abs(m-n);
		return value;
	}
	@Override
	void initial_train() {
		// TODO Auto-generated method stub
		
	}

	@Override
	List<Integer> find_right_sample_ok_2(List<String> wifi_ssid,
			Map<String, Double> wifi_up, int map_id_1, int map_id_2,
			DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		return null;
	}
}
