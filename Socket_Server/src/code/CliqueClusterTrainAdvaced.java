package code;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

public class CliqueClusterTrainAdvaced extends TrainSample {
	public static int cluster_id=0;
	Distance m_Distance=new DistanceAbsolute();
	@Override
	int train(DatabaseManager m_DatabaseManager)
	{
		m_DatabaseManager.Delete_ALL_Cluster();
		List<Integer> map_id_list=m_DatabaseManager.get_map_id();
		for(Integer map_id_one:map_id_list)
		{
			cliquecluster one=new cliquecluster();
			one.map_id=map_id_one;
			one.run(m_DatabaseManager);
		}
		return 0;
	}
	class cliquecluster 
	{
		private int map_id=8;
		public int ALL_sample_num=0;
		public int ALL_Dimens_NUM=0;
		void run(DatabaseManager m_DatabaseManager)
		{
			List<String> all_Dimension=All_Dimension(m_DatabaseManager);
			train(all_Dimension, m_DatabaseManager);
		}
		/////������е�ά����Ϣ
	    List<String> All_Dimension(DatabaseManager m_DatabaseManager)
	    {
	    	List<String> all_Dimension=new ArrayList<String>();
	    	
			List<Integer> all_sample_id=m_DatabaseManager.get_wifi_sample_id_in_map(map_id);
			ALL_sample_num=all_sample_id.size();
			System.out.println("�ܲ�������"+map_id+"��"+ALL_sample_num);
			////////////////�󲢼�
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
			ALL_Dimens_NUM=all_Dimension.size();
			System.out.println("ά�ȣ�"+all_Dimension.size());
			FileWriter fw;
			try {
				fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all.txt");
			
			
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
			try {
				fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_xy.txt");
			
			
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
			return all_Dimension;
	    }
	    ////�ؼ�⺯��

	    void train(List<String> all_Dimension,DatabaseManager m_DatabaseManager)
	    {
	    	//m_DatabaseManager.Delete_ALL_Cluster();
	    	//////�����ۼ�     map_DimensionΪÿһά��cell��Ϣ
	    	List<Cluster> m_Cluster_list=new ArrayList<Cluster>();
	    	Map<String,List<Cell>> map_Dimension=new HashMap<String,List<Cell>>();
	    	
	    	for(String one_Dimension:all_Dimension)
	    	{
	    		List<Cell> cell_list=cluster_list_on_Dimension(one_Dimension,m_DatabaseManager);//cluster_list_on_Dimension(one_Dimension,m_DatabaseManager);//
	    		System.out.println(""+one_Dimension);
	    		map_Dimension.put(one_Dimension, cell_list);
	    		for(Cell m_cell:cell_list)//ÿһ������cell����һά�Ļ�����
	    		{
	    			Cluster temp_cluster=new Cluster(cluster_id);//��Ӵغ� ��ʼ��������
	        		cluster_id++;
	        		temp_cluster.m_element.addAll(temp_cluster.addCell(m_cell));
	        		temp_cluster.m_cluster_contain.add(one_Dimension);///��ӵ�ǰά
	        		m_Cluster_list.add(temp_cluster);
	        		
	        		
	    		}
	    	}

	    	////////////////�ݹ�
	    	int i=1;
	    	while(true)
	    	{
	    		boolean flag_stop=true;
	    		System.out.println("ά����"+i);
	    		i++;
	    		List<Cluster> new_cluster=new ArrayList<Cluster>();
	    		for(Cluster temp_cluster:m_Cluster_list)
	        	{
	        		if(temp_cluster.get_check_flag())//���ô�
	        		{
	        			flag_stop=false;
	        			List<String> cluster_contain=temp_cluster.m_cluster_contain;
	        			List<String> cluster_now=new ArrayList<String>();
	        			cluster_now.addAll(all_Dimension);
	        			cluster_now.removeAll(cluster_contain);///ȡ� ���ڿ��õ�ά
	        			
	    				///////////
	    				Cell current_cell=null;
	    				String current_dimension=null;
	    				int current_max=0;
	    				/////����򲹼�
	    				for(String one_Dimension:cluster_now)///ȡ��������   ���Ա�������ȥ���ƶ�����k��ֵ
	        			{
	        				for(Cell one_cell:map_Dimension.get(one_Dimension))//ÿһά��cell
	        				{
	        					List<Integer> m_element=new ArrayList<Integer>();
	        					
	        					//for(Integer temp_copy:temp_cluster.m_element)
	        					//	m_element.add(temp_copy);
	        					m_element.addAll(temp_cluster.m_element);
	            				m_element.retainAll(one_cell.element);
	            				if(m_element.size()>current_max)
	            				{
	            					current_cell=one_cell;
	            					current_dimension=one_Dimension;
	            					current_max=m_element.size();
	            				}
	        				}///for(Cell one_cell:map_Dimension.get(one_Dimension))//ÿһά��cell
	        			}
	    				
	    				if(current_max!=0)
	    				{
	    				////�򲹼����
	        				if(check(temp_cluster,current_cell))
	        				{
	        					
	        					List<Integer> m_temp_list=temp_cluster.addCell(current_cell);
	        					/*if(m_temp_list.size()!=0)
	        					{
	        						Cluster m_retain=new Cluster(cluster_id);
	        						m_retain.m_element.addAll(m_temp_list);
		        					m_retain.set_check_flag(false);
		        					m_retain.m_cluster_contain.addAll(temp_cluster.m_cluster_contain);
		        					new_cluster.add(m_retain);
		        					cluster_id++;
	        					}
	        					*/
	        					temp_cluster.m_cluster_contain.add(current_dimension);///��ӵ�ǰά
	        					//System.out.println(""+temp_cluster.getClusterNo()+"+"+current_dimension);
	        					
	        				}
	        				else
	        				{
	        					temp_cluster.set_check_flag(false);
	        					
		    					System.out.println("ʧ�ܴغ�:	"+temp_cluster.clusterNo);
		    					System.out.println("����:	"+temp_cluster.m_element.size());
		    					for(String ttt:temp_cluster.m_cluster_contain)
		    					{
		    						System.out.println(ttt);
		    					}
		    					System.out.println("//////////////////////////");
	        					//System.out.println("false:"+temp_cluster.getClusterNo()+"+"+current_dimension);
	        				}
	    				}else
	    				{
	    					temp_cluster.set_check_flag(false);
	    					System.out.println("ʧ�ܴغ�:	"+temp_cluster.clusterNo);
	    					System.out.println("����:	"+temp_cluster.m_element.size());
	    					for(String ttt:temp_cluster.m_cluster_contain)
	    					{
	    						System.out.println(ttt);
	    					}
	    					System.out.println("//////////////////////////");
	    				}
	    				
	        		} ///if(!check(temp_cluster))//���ô�
	        		
	        	}///for(Cluster temp_cluster:m_Cluster_list)
	    		if(flag_stop)
	    			break;
	    		//m_Cluster_list.addAll(new_cluster);
	    	}//while(true)
	    	Merger(m_Cluster_list, m_DatabaseManager);
	    	
	    }
	    ///////��ĳһά��cell
	    List<Cell> cluster_list_on_Dimension(String m_one_Dimension,DatabaseManager m_DatabaseManager)
	    {
	    	List<Integer> all_sample_id=m_DatabaseManager.get_wifi_sample_id_in_map(map_id);
			////////////////��m_one_Dimensionά�Ļ����� ���������� �ڴ�ά����ͶӰ
	    	List<Cell> cell_list=new ArrayList<Cell>();
	    	Cell one_cell=new Cell();
	    	System.out.println(m_one_Dimension);
			for(Integer temp:all_sample_id)
			{
				Map<String,Integer> wifi_info =m_DatabaseManager.get_wifi_info(temp);
		      	if(wifi_info.containsKey(m_one_Dimension))
		      	{
		      		one_cell.element.add(temp);
		      		//System.out.println(""+temp);
		      	}
			}
			
			//if(one_cell.getNumberPoints()>=ALL_sample_num*ClusterConfig.densityRatioThreshold)
			//{
				cell_list.add(one_cell);
			//}
			
			return cell_list;
	    }
	    ///////��ĳһά��cell
	    List<Cell> cluster_list_PAM_on_Dimension(String m_one_Dimension,DatabaseManager m_DatabaseManager)
	    {
	    	List<Integer> all_sample_id=m_DatabaseManager.get_wifi_sample_id_in_map(map_id);
			////////////////��m_one_Dimensionά�Ļ����� ���������� �ڴ�ά����ͶӰ
	    	//List<Cell> cell_list=new ArrayList<Cell>();
	    	List<Integer> exist_sample=new ArrayList<Integer>();
	    	//Cell one_cell=new Cell();
	    	//System.out.println(m_one_Dimension);
			for(Integer temp:all_sample_id)
			{
				Map<String,Integer> wifi_info =m_DatabaseManager.get_wifi_info(temp);
		      	if(wifi_info.containsKey(m_one_Dimension))
		      	{
		      		//one_cell.element.add(temp);
		      		exist_sample.add(temp);
		      		//System.out.println(""+temp);
		      	}
		      	wifi_info=null;
			}
			////PAM k=2
			int K=2;//K>=2
			///////////�������ݼ���
			/////////K�����ĵ�h
			List<Integer> K_cluster=new ArrayList<Integer>();
			////////////////////ѡȡk����
			/*int k_count=0;
			for(Integer temp_Integer:exist_sample)
			{
				K_cluster.add(temp_Integer);
				k_count++;
				if(k_count>=K)
					break;
			}*/
			if(K>exist_sample.size())
			{
				for(int j=0;j<exist_sample.size();j++)
				{
					K_cluster.add(exist_sample.get(j));
				}
			}else
			{
				int k_count=exist_sample.size()/K;
				
				for(int i=0;i<K;i++)
				{
					/////select max
					K_cluster.add(exist_sample.get(i*k_count));
					
					
			       	//////////
				}
			}
			/*Integer [] sample_id=new Integer[exist_sample.size()];
			double [] des=new double[exist_sample.size()];
				//////////////�����������ܶ�////////////
				int iii=0;
				for(Integer temp_Integer_from:exist_sample)
				{
					HashMap<String ,Integer> from=m_DatabaseManager.get_wifi_info(temp_Integer_from);
					double distance_sum=0;
					for(Integer temp_Integer_to:exist_sample)
					{
						HashMap<String ,Integer> to=m_DatabaseManager.get_wifi_info(temp_Integer_to);
						double distance_from_to=Math.abs(to.get(m_one_Dimension)-from.get(m_one_Dimension));//    m_Distance.distance(from, to);
						double distance_all=0;
						for(Integer temp_Integer_other:exist_sample)
						{
							HashMap<String ,Integer> other=m_DatabaseManager.get_wifi_info(temp_Integer_other);
							double temp_distacne=Math.abs(to.get(m_one_Dimension)-other.get(m_one_Dimension));//m_Distance.distance(other, to);
							distance_all=distance_all+temp_distacne;
						}
						distance_from_to=distance_from_to/distance_all;
						distance_sum=distance_sum+distance_from_to;
					}
					//density.put(temp_Integer_from, distance_sum);
					des[iii]=distance_sum;
					sample_id[iii]=temp_Integer_from;
					iii++;
				}
				for(int ii=0;ii<exist_sample.size()-1;ii++)
				{
					for(int jj=0;jj<exist_sample.size()-ii-1;jj++)
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
				if(K>exist_sample.size())
				{
					for(int j=0;j<exist_sample.size();j++)
					{
						K_cluster.add(sample_id[j]);
					}
				}else
				{
					int k_count=exist_sample.size()/K;
					double bef_temp=Double.MAX_VALUE;
					for(int i=0;i<K;i++)
					{
						/////select max
						K_cluster.add(sample_id[i*k_count]);
						
						
				       	//////////
					}
				}
				sample_id=null;
				des=null;
				*/
				/////
			//////////n-k�������ĵ�
			List<Integer> N_K_cluster=new ArrayList<Integer>();
			N_K_cluster.clear();
			for(Integer temp_Integer:exist_sample)
			{
				N_K_cluster.add(temp_Integer);
			}
			N_K_cluster.removeAll(K_cluster);
				///////////////////////////����N-k������Ϣ//////////////////
			Map<Integer, PAMcell> N_K_info=new HashMap<Integer,PAMcell>();
			for(Integer temp_N_K_cluster:N_K_cluster)
			{
				HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
				double second=Double.MAX_VALUE;
				int second_id=0;
				double first=Double.MAX_VALUE-1;
				int first_id=0;
				for(Integer temp_k_cluster:K_cluster)
				{
					/////һά����
					HashMap<String ,Integer> K_cluster_one=m_DatabaseManager.get_wifi_info(temp_k_cluster);
					double m_distance_temp=Math.abs(K_cluster_one.get(m_one_Dimension)-N_K_cluster_one.get(m_one_Dimension));
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
				PAMcell cell_temp=new PAMcell();
				cell_temp.belong_to_cluster=first_id;
				cell_temp.near_to_cluster=second_id;
				N_K_info.put(temp_N_K_cluster, cell_temp);
			}
			///////////////////////////
				
			double bef_pay=Double.MAX_VALUE;
			int last_id_K=0;
			int last_id_N_K=0;
			while(true)
			{
				for(Integer temp_k_cluster:K_cluster)//Oi
				{
					HashMap<String ,Integer> K_cluster_one=m_DatabaseManager.get_wifi_info(temp_k_cluster);
					for(Integer temp_N_K_cluster:N_K_cluster)//Oh
					{
							
						double Last_pay=0;
						HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
						for(Integer temp_N_K_1_cluster:N_K_cluster)//Oj
						{
								
							if(temp_N_K_cluster!=temp_N_K_1_cluster)
							{
								HashMap<String ,Integer> N_K_1_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_1_cluster);
								if(N_K_info.get(temp_N_K_1_cluster).belong_to_cluster==temp_k_cluster)//���ڵ�ǰ��
								{
									HashMap<String ,Integer> near_one=m_DatabaseManager.get_wifi_info(N_K_info.get(temp_N_K_1_cluster).near_to_cluster);
										double Ohj=Math.abs(N_K_cluster_one.get(m_one_Dimension)-N_K_1_cluster_one.get(m_one_Dimension));
										double Omj=Math.abs(near_one.get(m_one_Dimension)-N_K_1_cluster_one.get(m_one_Dimension));
										double Oij=Math.abs(K_cluster_one.get(m_one_Dimension)-N_K_1_cluster_one.get(m_one_Dimension));
										if(Ohj>Omj)//����
										{
											Last_pay=Last_pay+Omj-Oij;
										}else//������
										{
											Last_pay=Last_pay+Ohj-Oij;
										}
										
									}else//�����ڵ�ǰ��
									{
										HashMap<String ,Integer> belong_one=m_DatabaseManager.get_wifi_info(N_K_info.get(temp_N_K_1_cluster).belong_to_cluster);
										double Ohj=Math.abs(N_K_cluster_one.get(m_one_Dimension)-N_K_1_cluster_one.get(m_one_Dimension));
										double Omj=Math.abs(belong_one.get(m_one_Dimension)-N_K_1_cluster_one.get(m_one_Dimension));
										if(Ohj>=Omj)//������
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
								HashMap<String ,Integer> temp_k_=m_DatabaseManager.get_wifi_info(temp_k);
								double distance_temp=Math.abs(temp_k_.get(m_one_Dimension)-K_cluster_one.get(m_one_Dimension));
								if(min_diatace>distance_temp)
								{
									min_diatace=distance_temp;
								}
							}
						}
						double distance_temp=Math.abs(N_K_cluster_one.get(m_one_Dimension)-K_cluster_one.get(m_one_Dimension));
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
						}
					}//for(Integer temp_N_K_cluster:N_K_cluster)
				}//for(Integer temp_k_cluster:K_cluster)
				if(bef_pay<0)///�������������
				{
					//System.out.println("bef_pay:"+bef_pay);
					//System.out.println("last_id_K:"+last_id_K+"		Oh:"+last_id_N_K);
					int id_k=K_cluster.indexOf(last_id_K);
					K_cluster.remove(id_k);
					K_cluster.add(last_id_N_K);
					id_k=N_K_cluster.indexOf(last_id_N_K);
					N_K_cluster.remove(id_k);
					N_K_cluster.add(last_id_K);
					///////////////////////////����N-k������Ϣ//////////////////
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
							HashMap<String ,Integer> K_cluster_one=m_DatabaseManager.get_wifi_info(temp_k_cluster);
								
							double m_distance_temp=Math.abs(K_cluster_one.get(m_one_Dimension)-N_K_cluster_one.get(m_one_Dimension));
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
							PAMcell cell_temp=new PAMcell();
							cell_temp.belong_to_cluster=first_id;
							cell_temp.near_to_cluster=second_id;
							N_K_info.put(temp_N_K_cluster, cell_temp);
					}
						///////////////////////////	
				}///if(bef_pay<0)///�������������
				else///if(bef_pay>=0)
				{
					System.out.println("break");
					break;
				}//�˳�while��true��
				bef_pay=Double.MAX_VALUE;
				last_id_K=0;
				last_id_N_K=0;
			}//while(true)
			///////////////////////////////������Ϣ
			Map<Integer,Cell> cluster=new HashMap<Integer,Cell>();
			for(Integer temp_k_cluster:K_cluster)
			{
				Cell one_cell=new Cell();
				cluster.put(temp_k_cluster, one_cell);
			}
			for(Integer temp_k_cluster:K_cluster)
			{
				//List<String> temp= new ArrayList<String>();
				//int size_array=1;
				for(Integer temp_N_K_cluster:N_K_cluster)
				{
					if(N_K_info.get(temp_N_K_cluster).belong_to_cluster==temp_k_cluster)
					{
							//////����cluster����Ԫ��
						
						cluster.get(temp_k_cluster).element.add(temp_N_K_cluster);
					}
				}
				cluster.get(temp_k_cluster).element.add(temp_k_cluster);///��������
				System.out.println(m_one_Dimension+":	"+cluster.get(temp_k_cluster).element.size());
			}
			List<Cell> temp1= new ArrayList<Cell>();
	       	Iterator<Entry<Integer,Cell>> iter1 = cluster.entrySet().iterator(); 
	       	while(iter1.hasNext())
	       	{ 
	       	Map.Entry<Integer,Cell> entry = (Map.Entry<Integer,Cell>)iter1.next();
	       	temp1.add(entry.getValue()); 
	       	}
	       	System.out.println("////////////////////////////");
			return temp1;
	    }
	    boolean equal_cluster(Cluster m_cluster,Cluster m_cluster_1)
	    {
	    	if(m_cluster.m_cluster_contain.size()!= m_cluster_1.m_cluster_contain.size())
	    		return false;
	    	int i=m_cluster.m_cluster_contain.size();
	    	boolean flag=true;
	    	Set<String> map_cluster=new HashSet<String>();
	    	for(String one:m_cluster.m_cluster_contain)
	    	{
	    		map_cluster.add(one);
	    		
	    	}
	    	for(String one:m_cluster_1.m_cluster_contain)
	    	{
	    		if(!map_cluster.contains(one))
	    		{
	    			flag=false;
	    			break;
	    		}
	    		
	    	}
	    	return flag;
	    }
		boolean check(Cluster m_cluster,Cell m_cell)
		{
			double r=0.8;//0.05
			double densityThreshold  ; //�ܶ���ֵ��������ÿ����Ԫ���е����С��Ŀ 
			List<Integer> ele_cell=new ArrayList<Integer>();
			ele_cell.addAll(m_cell.element);
			List<Integer> ele_cluster=new ArrayList<Integer>();
			ele_cluster.addAll(m_cluster.m_element);
			ele_cluster.retainAll(ele_cell);
			densityThreshold=((double)ALL_sample_num)*r;//*Math.pow(0.5, m_cluster.m_cluster_contain.size()+1);//r*((double)ALL_sample_num)/((double)m_cluster.m_cluster_contain.size()+1.0);
			double density=((double)ele_cluster.size());///((double)m_cluster.m_cluster_contain.size()+1.0);
			//densityThreshold = 2.0;
			if(density>densityThreshold)
			{
				return true;
			}else
				return false;
			
			/*if(ele_cluster.size()>densityThreshold
					&&r*((double)ALL_sample_num)/(m_cluster.m_cluster_contain.size()+1)>densityRatioThreshold)
			{
				return true;
			}else
			 return false;*/
			
		}
		///////�ؼ�����
		void Merger(List<Cluster> m_Cluster_list,DatabaseManager m_DatabaseManager)
		{
			System.out.println("�ϲ�");
			Set<Integer> set = new HashSet<Integer>();///������������
			int size_cluster=m_Cluster_list.size();
			for(Cluster temp_cluster:m_Cluster_list)
			{
				if(temp_cluster.flag_scan)
					continue;
				temp_cluster.flag_scan=true;
				if(temp_cluster.m_cluster_contain.size()<=Math.min(ALL_Dimens_NUM*0.1,3))
					continue;
				int merg_cluster=1;
				Set<Integer> set_cluster = new HashSet<Integer>();
				System.out.println("�ϲ���");
				System.out.println(""+temp_cluster.clusterNo+":		"+temp_cluster.m_element.size());
				for(Integer temp:temp_cluster.m_element)
				{
					set_cluster.add(temp); 
				}
				for(Cluster temp_cluster_next:m_Cluster_list)
				{
					
					if(!temp_cluster_next.flag_scan&&////û��ɨ���
							temp_cluster_next!=temp_cluster&&/////���Ǳ���
							equal_cluster(temp_cluster, temp_cluster_next))////ά��ͬ
					{
						
						
						for(Integer temp:temp_cluster_next.m_element)
						{
							set_cluster.add(temp); 
						}
						temp_cluster_next.flag_scan=true;
						merg_cluster++;
						System.out.println(""+temp_cluster_next.clusterNo+":		"+temp_cluster_next.m_element.size());
					}
				}
				System.out.println("/////////");
				Integer [] element_array={};
				Integer [] element_array1=set_cluster.toArray(element_array);//0������
				/////////////
				
				
				set.addAll(set_cluster);
				List<Integer> all_sample_id= new ArrayList<Integer>();
				for(int i_list=0;i_list<element_array1.length;i_list++)
				{
					all_sample_id.add(element_array1[i_list]);
				}
				//////////////
				int K=4;
				System.out.println("K:	"+K);
				if(merg_cluster<=3)
				{
					if(temp_cluster.m_cluster_contain.size()>=3)
					{
						System.out.println("�غţ�"+temp_cluster.getClusterNo()+"		ά��:"+temp_cluster.m_cluster_contain.size()+"	����������:"+temp_cluster.m_element.size());
						//***************************************************************************//
						int cluster_id=m_DatabaseManager.insert_into_cluster_centre(temp_cluster.clusterNo,map_id);///û�����ĵ� �����дغŴ���
						FileWriter fw;
						try {
							fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+cluster_id+".txt");
							
						for(Integer cluster_id_array:all_sample_id/*temp_cluster.m_element*/)///����س�Ա
						{
							
							m_DatabaseManager.insert_into_cluster_array(cluster_id, cluster_id_array);
							ResultSet  rstemp_k_cluster=m_DatabaseManager.get_ResultSet_from_wifi_sample(cluster_id_array);
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
						fw.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				       	////////////
					
						HashMap<String ,Double> wifi_info= new HashMap<String, Double>();
						//��ά�ľ�ֵ
						for(String name:temp_cluster.m_cluster_contain)
						{
							wifi_info.put(name, 0.0);
						}
						for(Integer sample_id:all_sample_id/*temp_cluster.m_element*/)
						{
							HashMap<String ,Integer> wifi_sample_id=m_DatabaseManager.get_wifi_info(sample_id);
							for(String name:temp_cluster.m_cluster_contain)///�������
							{
								wifi_info.put(name, wifi_info.get(name)+wifi_sample_id.get(name));
							}
						}
						for(String name:temp_cluster.m_cluster_contain)///��ƽ��
						{
							wifi_info.put(name, wifi_info.get(name)/all_sample_id/*temp_cluster.m_element*/.size());
						}
						////����wifi����info
						Iterator<Entry<String, Double>> iter_N_K_info = wifi_info.entrySet().iterator(); 
				       	while(iter_N_K_info.hasNext())
				       	{ 
				       		Map.Entry<String, Double> entry = (Map.Entry<String, Double>)iter_N_K_info.next();
				       		/////����centr-wifi����info
				       		double mean_value=entry.getValue();
				       		m_DatabaseManager.insert_into_cluster_centre_wifi_info(cluster_id, (String) entry.getKey(), (int) mean_value, 0, 0, 0, 0);
				       	} 
				       	//***************************************************************************//
				       	
			    		for(String tt:temp_cluster.m_cluster_contain)
			    		{
			    			System.out.println(tt);
			    		}
					}
					
				
				}else
				{
		///////////////
					System.out.println("K-pam");
					System.out.println("�غţ�"+temp_cluster.getClusterNo()+"		ά��:"+temp_cluster.m_cluster_contain.size()+"	����������:"+all_sample_id.size());
					for(String tt:temp_cluster.m_cluster_contain)
		    		{
		    			System.out.println(tt);
		    		}
					Integer [] sample_id=new Integer[all_sample_id.size()];
					double [] des=new double[all_sample_id.size()];
					/////////K�����ĵ�
					List<Integer> K_cluster=new ArrayList<Integer>();
					//////////////�����������ܶ�////////////
					int iii=0;
					for(Integer temp_Integer_from:all_sample_id)
					{
						HashMap<String ,Integer> from=m_DatabaseManager.get_wifi_info(temp_Integer_from);
						double distance_sum=0;
						for(Integer temp_Integer_to:all_sample_id)
						{
							HashMap<String ,Integer> to=m_DatabaseManager.get_wifi_info(temp_Integer_to);
							double distance_from_to=m_Distance.distance(from, to,temp_cluster.m_cluster_contain);
							double distance_all=0;
							for(Integer temp_Integer_other:all_sample_id)
							{
								HashMap<String ,Integer> other=m_DatabaseManager.get_wifi_info(temp_Integer_other);
								double temp_distacne=m_Distance.distance(other, to,temp_cluster.m_cluster_contain);
								distance_all=distance_all+temp_distacne;
							}
							distance_from_to=distance_from_to/distance_all;
							distance_sum=distance_sum+distance_from_to;
						}
						//density.put(temp_Integer_from, distance_sum);
						des[iii]=distance_sum;
						sample_id[iii]=temp_Integer_from;
						iii++;
					}
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
					
					////////////////////ѡȡk����
					/*int k_count=0;
					for(Integer temp_Integer:all_sample_id)
					{
						K_cluster.add(temp_Integer);
						k_count++;
						if(k_count>=K)
							break;
					}*/
					System.out.println("************************");
					System.out.println("��ǰ");
					for(Integer ii:K_cluster)
					{
						System.out.println("id:"+ii);
					}
					System.out.println("************************");
					//////////n-k�������ĵ�
					List<Integer> N_K_cluster=new ArrayList<Integer>();
					N_K_cluster.clear();
					for(Integer temp_Integer:all_sample_id)
					{
						N_K_cluster.add(temp_Integer);
					}
					N_K_cluster.removeAll(K_cluster);
					///////////////////////////����N-k������Ϣ//////////////////
					Map<Integer, cell_switch> N_K_info=new HashMap<Integer,cell_switch>();
					for(Integer temp_N_K_cluster:N_K_cluster)
					{
						
						HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
						double second=Double.MAX_VALUE;
						int second_id=0;
						double first=Double.MAX_VALUE-1;
						int first_id=0;
						for(Integer temp_k_cluster:K_cluster)
						{
							HashMap<String ,Integer> K_cluster_one=m_DatabaseManager.get_wifi_info(temp_k_cluster);
							
							double m_distance_temp=m_Distance.distance(K_cluster_one, N_K_cluster_one,temp_cluster.m_cluster_contain);
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
						cell_switch cell_temp=new cell_switch();
						cell_temp.belong_to_cluster=first_id;
						cell_temp.near_to_cluster=second_id;
						N_K_info.put(temp_N_K_cluster, cell_temp);
					}
					/////////////////////�ִ�
					
					double bef_pay=Double.MAX_VALUE;
					List<switch_ID> map_list=new ArrayList<switch_ID>();
					List<Integer> bef_K_medoin=new ArrayList<Integer>();////��¼��һ�������ͬ����ʱ�����ĵ�
					int bef_payfor=0;////��¼��ͬ�Ĵ���
					List<Integer> id_same_payfor=new ArrayList<Integer>();//��¼��ͬ�Ĵ��۵�
					List<Integer> id_same_payfor_K=new ArrayList<Integer>();//��¼��ͬ�Ĵ��۵��Ӧ�����ĵ�
					int bef_pay_for_exchange=0;////��¼��һ�εĽ�������
					int last_id_K=0;
					int last_id_N_K=0;
					int stor_last_id_K=0;
					int stor_last_id_N_K=0;
					while(true)
					{
						for(Integer temp_k_cluster:K_cluster)//Oi
						{
							HashMap<String ,Integer> K_cluster_one=m_DatabaseManager.get_wifi_info(temp_k_cluster);
							for(Integer temp_N_K_cluster:N_K_cluster)//Oh
							{
								
								double Last_pay=0;
								HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
								for(Integer temp_N_K_1_cluster:N_K_cluster)//Oj
								{
									
									if(temp_N_K_cluster!=temp_N_K_1_cluster)
									{
										HashMap<String ,Integer> N_K_1_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_1_cluster);
										if(N_K_info.get(temp_N_K_1_cluster).belong_to_cluster==temp_k_cluster)//���ڵ�ǰ��
										{
											HashMap<String ,Integer> near_one=m_DatabaseManager.get_wifi_info(N_K_info.get(temp_N_K_1_cluster).near_to_cluster);
											double Ohj=m_Distance.distance(N_K_cluster_one, N_K_1_cluster_one,temp_cluster.m_cluster_contain);
											double Omj=m_Distance.distance(near_one, N_K_1_cluster_one,temp_cluster.m_cluster_contain);
											double Oij=m_Distance.distance(K_cluster_one, N_K_1_cluster_one,temp_cluster.m_cluster_contain);
											if(Ohj>Omj)//����
											{
												Last_pay=Last_pay+Omj-Oij;
											}else//������
											{
												Last_pay=Last_pay+Ohj-Oij;
											}
											
										}else//�����ڵ�ǰ��
										{
											HashMap<String ,Integer> belong_one=m_DatabaseManager.get_wifi_info(N_K_info.get(temp_N_K_1_cluster).belong_to_cluster);
											double Ohj=m_Distance.distance(N_K_cluster_one, N_K_1_cluster_one,temp_cluster.m_cluster_contain);
											double Omj=m_Distance.distance(belong_one, N_K_1_cluster_one,temp_cluster.m_cluster_contain);
											
											if(Ohj>=Omj)//������
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
										HashMap<String ,Integer> temp_k_=m_DatabaseManager.get_wifi_info(temp_k);
										double distance_temp=m_Distance.distance(temp_k_,K_cluster_one,temp_cluster.m_cluster_contain);
										if(min_diatace>distance_temp)
										{
											min_diatace=distance_temp;
										}
									}
								}
								double distance_temp=m_Distance.distance(N_K_cluster_one,K_cluster_one,temp_cluster.m_cluster_contain);
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
									System.out.println("��ͬtemp-bef_pay:"+bef_pay);
								}
							}//for(Integer temp_N_K_cluster:N_K_cluster)
						}//for(Integer temp_k_cluster:K_cluster)
						if(bef_pay<0)///�������������
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
							///////////////////////����滻
							int id_k=K_cluster.indexOf(last_id_K);
							K_cluster.remove(id_k);
							K_cluster.add(last_id_N_K);
							System.out.println("************************");
							System.out.println("��ǰ");
							for(Integer ii:K_cluster)
							{
								System.out.println("id:"+ii);
							}
							System.out.println("************************");
							id_k=N_K_cluster.indexOf(last_id_N_K);
							N_K_cluster.remove(id_k);
							N_K_cluster.add(last_id_K);
							///////////////////////////����N-k������Ϣ//////////////////
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
									HashMap<String ,Integer> K_cluster_one=m_DatabaseManager.get_wifi_info(temp_k_cluster);
									
									double m_distance_temp=m_Distance.distance(K_cluster_one, N_K_cluster_one,temp_cluster.m_cluster_contain);
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
								cell_switch cell_temp=new cell_switch();
								cell_temp.belong_to_cluster=first_id;
								cell_temp.near_to_cluster=second_id;
								N_K_info.put(temp_N_K_cluster, cell_temp);
							}
							///////////////////////////	
						}///if(bef_pay<0)///�������������
						else///if(bef_pay>=0)
						{
							System.out.println("break");
							break;
						}//�˳�while��true��
						bef_pay=Double.MAX_VALUE;
						last_id_K=0;
						last_id_N_K=0;
						map_list.clear();
					}//while(true)
				///////////////////////////////������Ϣ
					for(Integer temp_k_cluster:K_cluster)
					{
						
						int cluster_id=m_DatabaseManager.insert_into_cluster_centre(temp_k_cluster,map_id);
						HashMap<String ,Integer> K_cluster_one=m_DatabaseManager.get_wifi_info(temp_k_cluster);
				    
				       	int size_array=1;
				       	FileWriter fw;
				       	try {
							fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+cluster_id+".txt");
						
						for(Integer temp_N_K_cluster:N_K_cluster)
						{
							if(N_K_info.get(temp_N_K_cluster).belong_to_cluster==temp_k_cluster)
							{
								//////����cluster����Ԫ��
								HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
								m_DatabaseManager.insert_into_cluster_array(cluster_id, temp_N_K_cluster);
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
						m_DatabaseManager.insert_into_cluster_array(cluster_id, temp_k_cluster);///��������
						///////�󽻼���ֵ
						HashMap<String ,Double> wifi_info= new HashMap<String, Double>();
						for(String name:temp_cluster.m_cluster_contain)
						{
							wifi_info.put(name, K_cluster_one.get(name).doubleValue());
						}
						for(Integer temp_N_K_cluster:N_K_cluster)
						{
							if(N_K_info.get(temp_N_K_cluster).belong_to_cluster==temp_k_cluster)
							{
								HashMap<String ,Integer> N_K_cluster_one=m_DatabaseManager.get_wifi_info(temp_N_K_cluster);
								for(String name:temp_cluster.m_cluster_contain)
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
				       		/////����centr-wifi����info
				       		double mean_value=entry.getValue()/size_array;
				       		m_DatabaseManager.insert_into_cluster_centre_wifi_info(cluster_id, (String) entry.getKey(), (int) mean_value, 0, 0, 0, 0);
				       	} 
					}
				
					
					////////////////
				}
				
			}
			
			System.out.println("�����ο���:"+set.size());
			Scanner sc=new Scanner(System.in);
	    	//String in=sc.next();
			
			
		}
		class cell_switch
		{
			int belong_to_cluster;
			
			int near_to_cluster;
			
		}
		class switch_ID
		{
			int K_ID;
			
			int N_K_ID;
			
		}
		class PAMcell
		{
			int belong_to_cluster;
			int near_to_cluster;
			
		}
	    public class Cell {
			List<Integer> element=new ArrayList<Integer>();///���а�����Ԫ��
			
			private int qualified;//�ܶ��Ƿ�ﵽ��ֵ��0,1
			private int checked;//�Ƿ���� ,0,1 
			private int clusterNo;//�������,0��ʾ��ɢ��
			public Cell(){
				qualified = 0;
				checked = 0;
				clusterNo = 0;
			}
			public void setQualified(int i){
				qualified = i;
			}
			public void setChecked(int i){
				checked = i;
			}
			public void setClusterNo(int i){
				clusterNo = i;
			}
			public int getNumberPoints(){
				return element.size();
			}
			public int getChecked(){
				return checked;
			}
			public int getQualified(){
				return qualified;
			}
			public int getClusterNo(){
				return clusterNo;
			}
		}
		public class Cluster {
			public boolean flag_scan=false;
			private int clusterNo ;//c0 means outlier;
			private boolean check_flag=true;//true ����ִ�� false ʧ��ֹͣ
			public List<String> m_cluster_contain=new ArrayList<String>();///�Ѿ��ϲ��Ĵص�ά����
			public List<Integer> m_element=new ArrayList<Integer>();
			public Cluster(int i)
			{
				clusterNo = i;
				
			}
			public void set_check_flag(boolean m_check_flag)
			{
				check_flag=m_check_flag;
			}
			public boolean get_check_flag()
			{
				return check_flag;
			}
			public List<Integer> addCell(Cell m_add_cell)
			{
				List<Integer> cell_element=m_add_cell.element;
				List<Integer> m_retainALL=new ArrayList<Integer>();
				if(m_element.size()==0)
				{
					m_element.addAll(cell_element);
				}
				else
				{
					m_retainALL.addAll(m_element);
					m_element.retainAll(cell_element);
					m_retainALL.removeAll(m_element);
					
				}
				return m_retainALL;
			}
		
			public void setclusterNo(int i){
				clusterNo = i;
			}
		
			public int getClusterNo(){
				return clusterNo;
			}
		}
}
	/////������е�ά����Ϣ
   
}
