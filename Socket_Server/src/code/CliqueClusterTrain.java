package code;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
///һ��ID�ɴ����ڶ����
public class CliqueClusterTrain extends TrainSample {
	public static int cluster_id=0;
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
	    	ALL_Dimens_NUM=all_Dimension.size();
			List<Integer> all_sample_id=m_DatabaseManager.get_wifi_sample_id_in_map(map_id);
			ALL_sample_num=all_sample_id.size();
			System.out.println("�ܲ���������"+ALL_sample_num);
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
			String[] all_wifi_infotemp1=set.toArray(all_wifi_infotemp);//0������
			all_Dimension.clear();
			
			for(int i=0;i<all_wifi_infotemp1.length;i++)
			{
				all_Dimension.add(all_wifi_infotemp1[i]);
				//System.out.println(all_wifi_infotemp1[i]);
			}
			System.out.println("ά�ȣ�"+all_Dimension.size());
			return all_Dimension;
	    }
	    ////�ؼ�⺯��

	    void train(List<String> all_Dimension,DatabaseManager m_DatabaseManager)
	    {
	    	m_DatabaseManager.Delete_ALL_Cluster();
	    	//////�����ۼ�     map_DimensionΪÿһά��cell��Ϣ
	    	List<Cluster> m_Cluster_list=new ArrayList<Cluster>();
	    	Map<String,List<Cell>> map_Dimension=new HashMap<String,List<Cell>>();
	    	
	    	for(String one_Dimension:all_Dimension)
	    	{
	    		List<Cell> cell_list=cluster_list_PAM_on_Dimension(one_Dimension,m_DatabaseManager);//cluster_list_on_Dimension(one_Dimension,m_DatabaseManager);//
	    		map_Dimension.put(one_Dimension, cell_list);
	    		for(Cell m_cell:cell_list)//ÿһ������cell����һά�Ļ�����
	    		{
	    			Cluster temp_cluster=new Cluster(cluster_id);//��Ӵغ� ��ʼ��������
	        		cluster_id++;
	        		temp_cluster.addCell(m_cell);
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
	        					temp_cluster.addCell(current_cell);
	        					temp_cluster.m_cluster_contain.add(current_dimension);///��ӵ�ǰά
	        					//System.out.println(""+temp_cluster.getClusterNo()+"+"+current_dimension);
	        				}
	        				else
	        				{
	        					temp_cluster.set_check_flag(false);
	        					//System.out.println("false:"+temp_cluster.getClusterNo()+"+"+current_dimension);
	        				}
	    				}else
	    				{
	    					temp_cluster.set_check_flag(false);
	    				}
	    				
	        		} ///if(!check(temp_cluster))//���ô�
	        		
	        	}///for(Cluster temp_cluster:m_Cluster_list)
	    		if(flag_stop)
	    			break;
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
	    	//System.out.println(m_one_Dimension);
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
							System.out.println("bef_pay:"+bef_pay);
						}
					}//for(Integer temp_N_K_cluster:N_K_cluster)
				}//for(Integer temp_k_cluster:K_cluster)
				if(bef_pay<0)///�������������
				{
					System.out.println("bef_pay:"+bef_pay);
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
				System.out.println(m_one_Dimension+":"+cluster.get(temp_k_cluster).element.size());
			}
			List<Cell> temp1= new ArrayList<Cell>();
	       	Iterator<Entry<Integer,Cell>> iter1 = cluster.entrySet().iterator(); 
	       	while(iter1.hasNext())
	       	{ 
	       	Map.Entry<Integer,Cell> entry = (Map.Entry<Integer,Cell>)iter1.next();
	       	temp1.add(entry.getValue()); 
	       	}
	       	System.out.println(m_one_Dimension+":"+temp1.size());
			return temp1;
	    }
		boolean check(Cluster m_cluster,Cell m_cell)
		{
			double r=0.05;//0.05
			double densityThreshold  ; //�ܶ���ֵ��������ÿ����Ԫ���е����С��Ŀ 
			List<Integer> ele_cell=new ArrayList<Integer>();
			ele_cell.addAll(m_cell.element);
			List<Integer> ele_cluster=new ArrayList<Integer>();
			ele_cluster.addAll(m_cluster.m_element);
			ele_cluster.retainAll(ele_cell);
			densityThreshold=r*((double)ALL_sample_num)/((double)m_cluster.m_cluster_contain.size()+1.0);
			double density=((double)ele_cluster.size())/((double)m_cluster.m_cluster_contain.size()+1.0);
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
			Set<Integer> set = new HashSet<Integer>();
			for(Cluster temp_cluster:m_Cluster_list)
			{
				if(temp_cluster.m_cluster_contain.size()>=3)
				{
					System.out.println("�غţ�"+temp_cluster.getClusterNo()+"		ά��:"+temp_cluster.m_cluster_contain.size()+"	����������:"+temp_cluster.m_element.size());
					//***************************************************************************//
					int cluster_id=m_DatabaseManager.insert_into_cluster_centre(temp_cluster.clusterNo,map_id);///û�����ĵ� �����дغŴ���
					for(Integer cluster_id_array:temp_cluster.m_element)///����س�Ա
					{
						m_DatabaseManager.insert_into_cluster_array(cluster_id, cluster_id_array);
					}
					
					HashMap<String ,Double> wifi_info= new HashMap<String, Double>();
					//��ά�ľ�ֵ
					for(String name:temp_cluster.m_cluster_contain)
					{
						wifi_info.put(name, 0.0);
					}
					for(Integer sample_id:temp_cluster.m_element)
					{
						HashMap<String ,Integer> wifi_sample_id=m_DatabaseManager.get_wifi_info(sample_id);
						for(String name:temp_cluster.m_cluster_contain)///�������
						{
							wifi_info.put(name, wifi_info.get(name)+wifi_sample_id.get(name));
						}
					}
					for(String name:temp_cluster.m_cluster_contain)///��ƽ��
					{
						wifi_info.put(name, wifi_info.get(name)/temp_cluster.m_element.size());
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
			       	
					for(Integer temp:temp_cluster.m_element)
					{
						 set.add(temp); 
					}
		    		for(String tt:temp_cluster.m_cluster_contain)
		    		{
		    			System.out.println(tt);
		    		}
				}
				
			}
			System.out.println("�����ο���:"+set.size());
			
			
			
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
			private int clusterNo ;//c0 means outlier;
			private boolean check_flag=true;//true ����ִ�� false ʧ��ֹͣ
			List<String> m_cluster_contain=new ArrayList<String>();///�Ѿ��ϲ��Ĵص�ά����
			List<Integer> m_element=new ArrayList<Integer>();
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
			public void addCell(Cell m_add_cell)
			{
				List<Integer> cell_element=m_add_cell.element;
				if(m_element.size()==0)
				{
					m_element.addAll(cell_element);
				}
				else
					m_element.retainAll(cell_element);
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
