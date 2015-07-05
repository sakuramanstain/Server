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
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
public class PAMClusterTrainNew extends Algorithm {
////////训练加距离维 分组加方差 匹配距离加方差
	int temp_map_id=0;
	class cell
	{
		int belong_to_cluster;
		
		int near_to_cluster;
		
	}
	class switch_ID
	{
		int K_ID;
		
		int N_K_ID;
		
	}
	int K=6;
	
	Distance m_Distance=new DistanceAbsolute ();//DistanceAbsolute();DistanceEuclidean
	@Override
	int train(DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		////////////距离计算方法
		m_DatabaseManager.Delete_ALL_Cluster();
		DistanceSave m_DistanceSave=new DistanceSave();
		
		///////////所有数据集合
		Map<Integer,Double> density=new HashMap<Integer,Double>();
		List<Integer> map_id=m_DatabaseManager.get_map_id();
		
		
			
		for(Integer temp_map_id:map_id)
		{
			System.out.println("map_id:"+temp_map_id);
			List<Integer> all_sample_id= m_DatabaseManager.get_wifi_sample_id_in_map(temp_map_id);
			 //***********************样本点位置**************************
			{
				FileWriter fw;
				try {
					fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_xy"+temp_map_id+".txt");
				
				
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
			/////*******************************************************
			Integer [] sample_id=new Integer[all_sample_id.size()];
			double [] des=new double[all_sample_id.size()];
			/////////K个中心点
			List<Integer> K_cluster=new ArrayList<Integer>();
			//////////////计算各个点的密度////////////
			long a=System.currentTimeMillis();
			
			try {
				File temp_distance=new File("F:"+File.separator+"workspace"+File.separator+"temp_distance"+temp_map_id+".txt");
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
        	
			//**************************
			m_DistanceSave.initial("F:"+File.separator+"workspace"+File.separator+"temp_distance"+temp_map_id+".txt", all_sample_id);
			//////////////////
			int iii=0;
			a=System.currentTimeMillis();
			for(Integer temp_Integer_from:all_sample_id)
			{
				double distance_sum=0;
				for(Integer temp_Integer_to:all_sample_id)
				{
					
					double distance_from_to=m_DistanceSave.get_distance(temp_Integer_from, temp_Integer_to);//m_Distance.distance(from, to);
		            double distance_all=m_DistanceSave.get_distance(temp_Integer_from);
				
					distance_from_to=distance_from_to/distance_all;
					distance_sum=distance_sum+distance_from_to;
				}
				//density.put(temp_Integer_from, distance_sum);
				des[iii]=distance_sum;
				sample_id[iii]=temp_Integer_from;
				//System.out.println(""+temp_Integer_from+":   "+distance_sum);
				iii++;
			}
			System.out.println("执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
        	
			for(int ii=0;ii<all_sample_id.size()-1;ii++)
			{
				for(int jj=0;jj<all_sample_id.size()-ii-1;jj++)
				{
					if(des[jj]>des[jj+1])
					{
						int temp=sample_id[jj];
						sample_id[jj]=sample_id[jj+1];
						sample_id[jj+1]=temp;
						double tempd=des[jj];
						des[jj]=des[jj+1];
						des[jj+1]=tempd;
					}
				}
			}
			if(K>all_sample_id.size())
			{
				for(int j=0;j<all_sample_id.size();j++)
				{
					K_cluster.add(sample_id[j]);
				}
			}else
			{
				int k_count=all_sample_id.size()/K;
				double bef_temp=Double.MAX_VALUE;
				for(int i=0;i<K;i++)
				{
					/////select max
					K_cluster.add(sample_id[i*k_count]);
					
					
			       	//////////
				}
			}
			des=null;
			sample_id=null;
			
			////////////////////选取k个点
			/*
			int k_count=0;
			for(Integer temp_Integer:all_sample_id)
			{
				K_cluster.add(temp_Integer);
				k_count++;
				if(k_count>=K)
					break;
			}*/
			System.out.println("************************");
			System.out.println("当前");
			for(Integer ii:K_cluster)
			{
				System.out.println("id:"+ii);
			}
			System.out.println("************************");
			//////////n-k个非中心点
			List<Integer> N_K_cluster=new ArrayList<Integer>();
			N_K_cluster.clear();
			for(Integer temp_Integer:all_sample_id)
			{
				N_K_cluster.add(temp_Integer);
			}
			N_K_cluster.removeAll(K_cluster);
			///////////////////////////构造N-k个的信息//////////////////
			Map<Integer, cell> N_K_info=new HashMap<Integer,cell>();
			for(Integer temp_N_K_cluster:N_K_cluster)
			{
				
				HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
				double second=Double.MAX_VALUE;
				int second_id=0;
				double first=Double.MAX_VALUE-1;
				int first_id=0;
				
				
				for(Integer temp_k_cluster:K_cluster)
				{
					//HashMap<String ,Integer> K_cluster_one=m_DatabaseManager.get_wifi_info(temp_k_cluster);
					
					double m_distance_temp=m_DistanceSave.get_distance(temp_k_cluster, temp_N_K_cluster);//m_Distance.distance(K_cluster_one, N_K_cluster_one);
					if(m_distance_temp<second)
					{
						second=m_distance_temp;
						second_id=temp_k_cluster;
						if(second<first)
						{
							int temp_media=second_id;
							double temP_value=second;
							second_id=first_id;
							second=first;
							first_id=temp_media;
							first=temP_value;
						}
					}
				}
				cell cell_temp=new cell();
				cell_temp.belong_to_cluster=first_id;
				cell_temp.near_to_cluster=second_id;
				N_K_info.put(temp_N_K_cluster, cell_temp);
			}
			/////////////////////分簇
			
			double bef_pay=Double.MAX_VALUE;
			List<switch_ID> map_list=new ArrayList<switch_ID>();
			List<Integer> bef_K_medoin=new ArrayList<Integer>();////记录上一组存在相同代价时的中心点
			int bef_payfor=0;////记录相同的代价
			List<Integer> id_same_payfor=new ArrayList<Integer>();//记录相同的代价点
			List<Integer> id_same_payfor_K=new ArrayList<Integer>();//记录相同的代价点对应的中心点
			int bef_pay_for_exchange=0;////记录上一次的交换代价
			int last_id_K=0;
			int last_id_N_K=0;
			int stor_last_id_K=0;
			int stor_last_id_N_K=0;
			while(true)
			{
				for(Integer temp_k_cluster:K_cluster)//Oi
				{
					
					//HashMap<String ,Integer> K_cluster_one=m_DatabaseManager.get_wifi_info(temp_k_cluster);
					/////

					/////
					for(Integer temp_N_K_cluster:N_K_cluster)//Oh
					{
						//////
						           
						///////
						double Last_pay=0;
						//HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
						for(Integer temp_N_K_1_cluster:N_K_cluster)//Oj
						{
							
							if(temp_N_K_cluster!=temp_N_K_1_cluster)
							{
								//HashMap<String ,Integer> N_K_1_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_1_cluster);
						          
								///////
								if(N_K_info.get(temp_N_K_1_cluster).belong_to_cluster==temp_k_cluster)//属于当前簇
								{
									//HashMap<String ,Integer> near_one=m_DatabaseManager.get_wifi_info(N_K_info.get(temp_N_K_1_cluster).near_to_cluster);
									double Ohj=m_DistanceSave.get_distance( temp_N_K_1_cluster,temp_N_K_cluster);//m_Distance.distance(N_K_cluster_one, N_K_1_cluster_one);
									double Omj=m_DistanceSave.get_distance( temp_N_K_1_cluster,N_K_info.get(temp_N_K_1_cluster).near_to_cluster);//m_Distance.distance(near_one, N_K_1_cluster_one);
									double Oij=m_DistanceSave.get_distance(temp_N_K_1_cluster,temp_k_cluster);//m_Distance.distance(K_cluster_one, N_K_1_cluster_one);
									////////////
									 
									////////////
									if(Ohj>Omj)//换簇
									{
										Last_pay=Last_pay+Omj-Oij;
									}else//不换簇
									{
										Last_pay=Last_pay+Ohj-Oij;
									}
									
								}else//不属于当前簇
								{
									//HashMap<String ,Integer> belong_one=m_DatabaseManager.get_wifi_info(N_K_info.get(temp_N_K_1_cluster).belong_to_cluster);
									double Ohj=m_DistanceSave.get_distance(temp_N_K_1_cluster, temp_N_K_cluster);//m_Distance.distance(N_K_cluster_one, N_K_1_cluster_one);
									double Omj=m_DistanceSave.get_distance(temp_N_K_1_cluster, N_K_info.get(temp_N_K_1_cluster).belong_to_cluster);//m_Distance.distance(belong_one, N_K_1_cluster_one);
									if(Ohj>=Omj)//不换簇
									{
										Last_pay=Last_pay+0;
									}else
									{
										Last_pay=Last_pay+Ohj-Omj;
									}
								}
							}
							
							
						}//for(Integer temp_N_K_1_cluster:N_K_cluster)
						double min_diatace=Double.MAX_VALUE;
						for(Integer temp_k:K_cluster)
						{
							if(temp_k!=temp_k_cluster)
							{
								
								double distance_temp=m_DistanceSave.get_distance(temp_k_cluster, temp_k);//m_Distance.distance(temp_k_,K_cluster_one);
								if(min_diatace>distance_temp)
								{
									min_diatace=distance_temp;
								}
							}
						}
						double distance_temp=m_DistanceSave.get_distance(temp_k_cluster,temp_N_K_cluster);//m_Distance.distance(N_K_cluster_one,K_cluster_one);
						if(min_diatace>distance_temp)
						{
							min_diatace=distance_temp;
						}
						if(N_K_info.get(temp_N_K_cluster).belong_to_cluster==temp_k_cluster)
							Last_pay=Last_pay+min_diatace-distance_temp;
						
						if(Last_pay<bef_pay)
						{	
							
							bef_pay=Last_pay;
							last_id_K=temp_k_cluster;
							last_id_N_K=temp_N_K_cluster;
							map_list.clear();
							switch_ID t=new switch_ID();
							t.K_ID=temp_k_cluster;
							t.N_K_ID=temp_N_K_cluster;
							map_list.add(t);
							//id_same_payfor.clear();
							//id_same_payfor_K.clear();
							///id_same_payfor.add(last_id_N_K);
							//id_same_payfor_K.add(last_id_K);
							System.out.println("temp-bef_pay:"+bef_pay);
						}else if(Last_pay==bef_pay)
						{
							//id_same_payfor.add(last_id_N_K);
							//id_same_payfor_K.add(last_id_K);
							switch_ID t=new switch_ID();
							t.K_ID=temp_k_cluster;
							t.N_K_ID=temp_N_K_cluster;
							map_list.add(t);
							System.out.println("相同temp-bef_pay:"+bef_pay);
						}
					}//for(Integer temp_N_K_cluster:N_K_cluster)
				}//for(Integer temp_k_cluster:K_cluster)
				if(bef_pay<0&&!(stor_last_id_N_K==last_id_N_K &&stor_last_id_K==last_id_K))///调整后继续迭代
				{

					Random random = new Random();
					int id=Math.abs(random.nextInt())%map_list.size();
					last_id_K=map_list.get(id).K_ID;
					last_id_N_K=map_list.get(id).N_K_ID;
					System.out.println("map_list:"+map_list.size());
					System.out.println("bef_pay:"+bef_pay);
					System.out.println("last_id_K:"+last_id_K+"		Oh:"+last_id_N_K);
					if(stor_last_id_N_K==last_id_N_K &&stor_last_id_K==last_id_K)
						break;
					/////////////////////////
					stor_last_id_N_K=last_id_K;
					stor_last_id_K=last_id_N_K;
					///////////////////////点的替换
					int id_k=K_cluster.indexOf(last_id_K);
					K_cluster.remove(id_k);
					K_cluster.add(last_id_N_K);
					System.out.println("************************");
					System.out.println("当前");
					for(Integer ii:K_cluster)
					{
						System.out.println("id:"+ii);
					}
					System.out.println("************************");
					id_k=N_K_cluster.indexOf(last_id_N_K);
					N_K_cluster.remove(id_k);
					N_K_cluster.add(last_id_K);
					///////////////////////////构造N-k个的信息//////////////////
					N_K_info.clear();
					for(Integer temp_N_K_cluster:N_K_cluster)
					{
						HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
						double second=Double.MAX_VALUE;
						int second_id=0;
						double first=Double.MAX_VALUE-1;
						int first_id=0;
						
						
			            
						
						for(Integer temp_k_cluster:K_cluster)
						{
							//HashMap<String ,Integer> K_cluster_one=m_DatabaseManager.get_wifi_info(temp_k_cluster);
							
							double m_distance_temp=m_DistanceSave.get_distance(temp_N_K_cluster, temp_k_cluster);//m_Distance.distance(K_cluster_one, N_K_cluster_one);
							
						
							
							if(m_distance_temp<second)
							{
								second=m_distance_temp;
								second_id=temp_k_cluster;
								if(second<first)
								{
									int temp_media=second_id;
									double temP_value=second;
									second_id=first_id;
									second=first;
									first_id=temp_media;
									first=temP_value;
								}
									
							}
						}
						cell cell_temp=new cell();
						cell_temp.belong_to_cluster=first_id;
						cell_temp.near_to_cluster=second_id;
						N_K_info.put(temp_N_K_cluster, cell_temp);
					}
					///////////////////////////	
				}///if(bef_pay<0)///调整后继续迭代
				else///if(bef_pay>=0)
				{
					System.out.println("break");
					break;
				}//退出while（true）
				bef_pay=Double.MAX_VALUE;
				last_id_K=0;
				last_id_N_K=0;
				map_list.clear();
			}//while(true)
		///////////////////////////////保存信息
			
			for(Integer temp_k_cluster:K_cluster)
			{
				
				int cluster_id=m_DatabaseManager.insert_into_cluster_centre(temp_k_cluster,temp_map_id);
				HashMap<String ,Integer> K_cluster_one=m_DatabaseManager.get_wifi_info(temp_k_cluster);
		       	List<String> temp= new ArrayList<String>();
		       	Iterator<Entry<String, Integer>> iter = K_cluster_one.entrySet().iterator(); 
		       	while(iter.hasNext())
		       	{ 
		       		Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
		       		temp.add((String) entry.getKey()); 
		       	} 
		       	int size_array=1;
		       	FileWriter fw;
				try {
					fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"map_"+temp_map_id+"_"+temp_k_cluster+".txt");
					for(Integer temp_N_K_cluster:N_K_cluster)
					{
						if(N_K_info.get(temp_N_K_cluster).belong_to_cluster==temp_k_cluster)
						{
							//////插入cluster的组元素
							HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
							m_DatabaseManager.insert_into_cluster_array(cluster_id, temp_N_K_cluster);
							List<String> temp1= new ArrayList<String>();
					       	Iterator<Entry<String, Integer>> iter1 = N_K_cluster_one.entrySet().iterator(); 
					       	while(iter1.hasNext())
					       	{ 
					       	Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter1.next();
					       	temp1.add((String) entry.getKey()); 
					       	}
					       	temp.retainAll(temp1);
					       	size_array++;
					       	////////////////
					       	ResultSet  rstemp_k_cluster=m_DatabaseManager.get_ResultSet_from_wifi_sample(temp_N_K_cluster);
							int x=0;
						   	int y=0;
				            try {
								if(rstemp_k_cluster.next())
								  {
								   	 x=rstemp_k_cluster.getInt("x");
								   	 y=rstemp_k_cluster.getInt("y");
								  }
								rstemp_k_cluster.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				            fw.write("   "+x+"   "+y+"   \n");
					       	////////////
						}
					}
					ResultSet  rstemp_k_cluster=m_DatabaseManager.get_ResultSet_from_wifi_sample(temp_k_cluster);
					int x=0;
				   	int y=0;
		            try {
						if(rstemp_k_cluster.next())
						  {
						   	 x=rstemp_k_cluster.getInt("x");
						   	 y=rstemp_k_cluster.getInt("y");
						  }
						rstemp_k_cluster.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            fw.write("   "+x+"   "+y+"   \n");
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				m_DatabaseManager.insert_into_cluster_array(cluster_id, temp_k_cluster);///加入自身
				///////求交集均值
				HashMap<String ,Double> wifi_info= new HashMap<String, Double>();
				for(String name:temp)
				{
					wifi_info.put(name, K_cluster_one.get(name).doubleValue());
				}
				HashMap<String ,Double> wifi_info_delet= new HashMap<String, Double>();
				for(String name:temp)
				{
					wifi_info_delet.put(name, 0.0);
				}
				for(Integer temp_N_K_cluster:N_K_cluster)
				{
					if(N_K_info.get(temp_N_K_cluster).belong_to_cluster==temp_k_cluster)
					{
						HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
						for(String name:temp)
						{
							double new_value=wifi_info_delet.get(name)+Math.pow(K_cluster_one.get(name).doubleValue()-N_K_cluster_one.get(name),2);
							double new_value1=wifi_info.get(name)+N_K_cluster_one.get(name);
							wifi_info.put(name, new_value1) ;
							wifi_info_delet.put(name, new_value) ;
						}
					}
					
				}
				for(String name:temp)
				{
					double mean_value=wifi_info.get(name)/size_array;
		       		double delt=wifi_info_delet.get(name)/size_array;
		       		if(delt==0)
		       			delt=0.001;
		       		m_DatabaseManager.insert_into_cluster_centre_wifi_info(cluster_id, name, (int) mean_value, delt, 0, 0, 0);
				}
				
			}
		}
		
		
		return 0;
	}
	@Override
	int find_map_id(List<String> wifi_ssid, Map<String, Double> wifi_up,
			DatabaseManager m_DatabaseManager) {
		List<Integer> cluster_id_list =m_DatabaseManager.get_ALL_cluster_ID();
        ///////
       int cluster_id=0;
        double min_distance=Double.MAX_VALUE;
        for(Integer id:cluster_id_list)
        {
         double distance=0;
         HashMap<String ,Integer> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info(id);
         distance=m_Distance.distance_double(wifi_up, temp_map);
         if(distance<min_distance)
         {
        	 cluster_id=id;
        	 min_distance=distance;
         }
         /*List<String> temp= new ArrayList<String>();
       	 Iterator<Entry<String, Integer>> iter = temp_map.entrySet().iterator(); 
       	 while(iter.hasNext()){ 
       	 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
       	 temp.add((String) entry.getKey()); 
       	 } 
     
       	 temp.retainAll(wifi_ssid);
       	 
       	 for(String t:temp)
       	 {
       		 distance=distance+Math.pow(temp_map.get(t)-wifi_up.get(t), 2);
       	 }
       	 if(temp.size()!=0)
       	 {
       		 	distance=Math.sqrt(distance);
        	 	distance=distance/temp.size();
        	 	if(distance<min_distance)
        	 	{
        	 		cluster_id=id;
        	 		min_distance=distance;
        	 	}
        	 	
       	 }
       	 */
        }//for
        int map_id=m_DatabaseManager.get_map_ID_from_cluster_centre(cluster_id);
        temp_map_id=map_id;
		return map_id;
	}
	@Override
	List<Integer> find_right_sample_ok(List<String> wifi_ssid ,Map<String,Double> wifi_up,int map_id,DatabaseManager m_DatabaseManager)
	{
		// TODO Auto-generated method stub
		 List<Integer> cluster_id_list =m_DatabaseManager.get_ALL_cluster_ID(map_id);
		 double step=0;
		 double step_by=0.001;
         List<Integer> ok_sample_id=new ArrayList<Integer>();
         
         /////寻找匹配的位置点ID
       /*  double MAX_DOuble=Double.MAX_VALUE;
         int OK_ID=0;
         for(Integer id:cluster_id_list)
         {
    		
    		 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info(id);
    		 double distance=m_Distance.distance_double(wifi_up, temp_map);
    		 if(distance<MAX_DOuble)
    		 {
    			 MAX_DOuble=distance;
    			 OK_ID=id;
    			 
    		 }
        	 
         }//for
         
         ok_sample_id.add(OK_ID);*/
        while(ok_sample_id.size()<2)
         {
        	 ok_sample_id.clear();
        	 for(Integer id:cluster_id_list)
             {
        		
        		 HashMap<String ,Double[]> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info_delt(id);
        		 List<String> first_temp= new ArrayList<String>();
               	 Iterator<Entry<String, Double>> iter = wifi_up.entrySet().iterator(); 
               	 while(iter.hasNext()){ 
               	  Map.Entry<String, Double> entry = (Map.Entry<String, Double>)iter.next();
               	  first_temp.add((String) entry.getKey()); 
               	 } 
               	 List<String> second_temp= new ArrayList<String>();
               	Iterator<Entry<String,Double[]>> iter2 = temp_map.entrySet().iterator(); 
              	 while(iter2.hasNext()){ 
              	 Map.Entry<String,Double[]> entry = (Map.Entry<String,Double[]>)iter2.next();
              	 second_temp.add((String) entry.getKey()); 
              	 } 
              	 first_temp.retainAll(second_temp);
              	 double distance=0;
              	 if(first_temp.size()>0)
              	 {
              		 for(String temp:first_temp)
            		 {
              			distance+=Math.pow(temp_map.get(temp)[0]-wifi_up.get(temp),2)/temp_map.get(temp)[1];
            		 }
              		distance=distance/first_temp.size();
              		 System.out.println(""+id+":	"+distance);
              	 }else
              		distance=Double.MAX_VALUE;
        		
        		// double distance=m_Distance.distance_double(wifi_up, temp_map);
        		 if(distance<step)
        		 {
        			 ok_sample_id.add(id);
        		 }
            	 
             }//for
        	 System.out.println(":	"+step_by);
        	 step=step+step_by;
        	 if(step>100)
        	 {
        			
        		   System.out.println("step>2048失败 ");
        		   break;
        	 }
        		 
         }//while 
         
         for(Integer temp_cluster_id:ok_sample_id)
         {
        	 System.out.println("簇ID号："+temp_cluster_id);
         }
         step=0;
         List<Integer> ok_sample_id2=new ArrayList<Integer>();///簇中选中的点
         List<Integer> sample_id_list=new ArrayList<Integer>();///簇内的原始点
         for(Integer temp_cluster_id:ok_sample_id)
         {
        	 List<Integer> temp= m_DatabaseManager.get_ALL_cluster_array(temp_cluster_id);
        	 sample_id_list.addAll(temp);
        	 //int current_sample_id=m_DatabaseManager.get_sample_id_from_cluster_id(temp_cluster_id);
        	 //sample_id_list.add(current_sample_id);//已经包含自身
         }
     /////标准的K-NN算法 取最近的K个
        int K_NN=7;
         if(K_NN>=sample_id_list.size())
         {
        	 ok_sample_id2.addAll(sample_id_list);
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
        		 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(sample_id_list.get(i));
        		 double distance=m_Distance.distance_double(wifi_up,temp_map);
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
        		 ok_sample_id2.add(ok_id);
        	 }
         }
         /////寻找匹配的位置点ID
        // 采用迭代的方法 发现某个范围内的所有符合条件的点 限制依次放松
         /*while(ok_sample_id2.size()<1)
         {
        	 ok_sample_id2.clear();
        	 for(Integer id:sample_id_list)
             {
        		
        		 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
        		 double distance=m_Distance.distance_double(wifi_up, temp_map);
        		 //System.out.println(" "+distance);
        		 if(distance<step)
        		 {
        			 ok_sample_id2.add(id);
        		 }
            	
             }//for
        	
        	 step=step+step_by;
        	 if(step>100)
        	 {
        			
        		   System.out.println("step>100失败 ");
        		  break;
        	 }
        		 
         }//while 
        */
         for(Integer id:ok_sample_id2)
         {
        	 System.out.println("sample_id："+id);
         }
         return ok_sample_id2;
	}
	@Override
	Map<String,Double> find_coordinate(List<Integer> ok_sample_id,List<String> wifi_ssid,Map<String,Double> wifi_up,DatabaseManager m_DatabaseManager)
	{
		
		HashMap<Integer ,Double> distance_map = new HashMap<Integer, Double>();
        /////计算比例
		int i=0;
        for(Integer id:ok_sample_id)
        {
        	 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
        	 double distance=m_Distance.distance_double(wifi_up, temp_map);;
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
        	//System.out.println(""+id+"	:	"+distance);
           	//distance=Math.exp(-2*distance);
           	//distance_map.put(id, distance);
        	distance_map.put(id, Math.pow(0.6, i));
           	System.out.println(""+id+"	:	"+distance);
           i++;
       	
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
				  		System.out.println(""+distance_map.get(s));
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
			
			fw.append(""+ttx+"	"+tty+"    \n");
			
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
	@Override
	List<Integer> find_right_sample_ok_2(List<String> wifi_ssid,
			Map<String, Double> wifi_up, int map_id_1, int map_id_2,
			DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
