package code;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Random;

import code.PAMClusterTrainNew.cell;
import code.PAMClusterTrainNew.switch_ID;


public class PAMClusterTrain extends TrainSample {
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
	Distance m_Distance=new DistanceLog();//DistanceAbsolute();
	@Override
	int train(DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		////////////距离计算方法
		m_DatabaseManager.Delete_ALL_Cluster();
		
		 
		///////////所有数据集合
		Map<Integer,Double> density=new HashMap<Integer,Double>();
		List<Integer> map_id=m_DatabaseManager.get_map_id();
		for(Integer temp_map_id:map_id)
		{
			System.out.println("map_id:"+temp_map_id);
			List<Integer> all_sample_id= m_DatabaseManager.get_wifi_sample_id_in_map(temp_map_id);
			///////
			{
				List<String> all_Dimension=new ArrayList<String>();
		    	
				int ALL_sample_num=all_sample_id.size();
				System.out.println("总采样点数"+map_id+"："+ALL_sample_num);
				////////////////求并集
				Set<String> set = new HashSet<String>();   
				for(Integer temp:all_sample_id)
				{
					Map<String,Integer> wifi_info =m_DatabaseManager.get_wifi_info(temp);
			      	Iterator<Entry<String, Integer>> iter = wifi_info.entrySet().iterator(); 
			      	while(iter.hasNext()){ 
			      	  Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
			      	  set.add((String) entry.getKey()); 
			      	 } 
				}
				String[] all_wifi_infotemp= {};
				String[] all_wifi_infotemp1=set.toArray(all_wifi_infotemp);
				all_Dimension.clear();
				
				for(int i=0;i<all_wifi_infotemp1.length;i++)
				{
					all_Dimension.add(all_wifi_infotemp1[i]);
					//System.out.println(all_wifi_infotemp1[i]);
				}
				int ALL_Dimens_NUM=all_Dimension.size();
				System.out.println("维度："+all_Dimension.size());
				FileWriter fw;
				try {
					fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all-"+temp_map_id+".txt");
				
				
					for(Integer temp:all_sample_id)
					{
						Map<String,Integer> wifi_info =m_DatabaseManager.get_wifi_info(temp);
						String dd="";
						for(String one:all_Dimension)
						{
							if(wifi_info.containsKey(one))
							{
								dd+="	"+(-wifi_info.get(one))+"  ";
							}else
							{
								dd+="0	";
							}
						} 
						dd+="\n";
						fw.write(dd);
					}
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
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
			
			Integer [] sample_id=new Integer[all_sample_id.size()];
			double [] des=new double[all_sample_id.size()];
			/////////K个中心点
			List<Integer> K_cluster=new ArrayList<Integer>();
			//////////////计算各个点的密度////////////
			
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
			DistanceSave m_DistanceSave=new DistanceSave();
			m_DistanceSave.initial("F:"+File.separator+"workspace"+File.separator+"temp_distance"+temp_map_id+".txt", all_sample_id);
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
			/*int k_count=0;
			double bef_temp=Double.MAX_VALUE;
			for(k_count=0;k_count<K;k_count++)
			{
				/////select max
				double temp_temp=0;
				int id=-1;
				Iterator<Entry<Integer,Double>> iter = density.entrySet().iterator(); 
		       	while(iter.hasNext()){ 
		       	  Map.Entry<Integer,Double> entry = (Map.Entry<Integer,Double>)iter.next();
		       	  if(temp_temp<entry.getValue()&&entry.getValue()<bef_temp)
		       	  {
		       		  id=entry.getKey();
		       		  temp_temp=entry.getValue();
		       	  }
		       	 } 
		       	bef_temp=temp_temp;
		       	if(id!=-1)
		       	{
		       		K_cluster.add(id);
		       	}else
		       	{
		       		break;
		       	}
		       	//////////
			}
			density.clear();
			*/
			
			////////////////////选取k个点
			/*int k_count=0;
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
				for(Integer temp_N_K_cluster:N_K_cluster)
				{
					if(N_K_info.get(temp_N_K_cluster).belong_to_cluster==temp_k_cluster)
					{
						HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
						for(String name:temp)
						{
							double new_value=wifi_info.get(name)+N_K_cluster_one.get(name);
							wifi_info.put(name, new_value) ;
						}
					}
					
				}
				Iterator<Entry<String, Double>> iter_N_K_info = wifi_info.entrySet().iterator(); 
		       	while(iter_N_K_info.hasNext())
		       	{ 
		       		Map.Entry<String, Double> entry = (Map.Entry<String, Double>)iter_N_K_info.next();
		       		/////插入centr-wifi——info
		       		double mean_value=entry.getValue()/size_array;
		       		m_DatabaseManager.insert_into_cluster_centre_wifi_info(cluster_id, (String) entry.getKey(), (int) mean_value, 0, 0, 0, 0);
		       	} 
			}
		}
		
		
		return 0;
	}
	
}
