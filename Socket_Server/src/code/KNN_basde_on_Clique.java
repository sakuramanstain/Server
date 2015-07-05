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
import java.util.Set;
import java.util.Map.Entry;


public class KNN_basde_on_Clique extends Algorithm {
	Distance m_Distance=new DistanceEuclidean();
	@Override
	int find_map_id(List<String> wifi_ssid, Map<String, Double> wifi_up,
			DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		System.out.println("**********************************************");
  		System.out.println("*********************start*********************");
  		System.out.println("**********************************************");
		List<Integer> cluster_id_list =m_DatabaseManager.get_ALL_cluster_ID();
        ///////�����ƶ�����һ���
        int sim=-1;//������ƶ�
        List<Integer> sim_max_cluster_id=new ArrayList<Integer>();//���ƶȿ��ܻ������ͬ�Ķ����
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
           	if(temp.size()>sim)///���ƶȴ���ԭʼ������ ԭ�������
           	{
           		sim_max_cluster_id.clear();
           		sim_max_cluster_id.add(id);
           		sim=temp.size();
           	}else if(temp.size()==sim)//���ƶ���ͬ ������ID����
           	{
           		sim_max_cluster_id.add(id);
           		
           	}else
           	{
           		
           	}
        }
        System.out.println("���ƶ�:"+sim);
        System.out.println("����cluster_id:");
        for(Integer out:sim_max_cluster_id)
        {
        	System.out.println("id:"+out);
        }
        System.out.println("****************");
        ///////////
        int map_id=find_map_MAX_SIM(wifi_ssid,wifi_up,sim_max_cluster_id,m_DatabaseManager);
       
		return map_id;
	}
	/////����1 ��ѯÿ������Ե�map_id �ҵ�����Ƶ����ߵ� һ��
	int find_map_MAX_SIM(List<String> wifi_ssid, Map<String, Double> wifi_up,
			List<Integer> sim_max_cluster_id,DatabaseManager m_DatabaseManager)
	{
		Map<Integer,Integer> map_id_fre=new HashMap<Integer,Integer>();////map_ID���ֵ�Ƶ�� 
        for(Integer id:sim_max_cluster_id)
        {
        	int temp_map_id=m_DatabaseManager.get_map_ID_from_cluster_centre(id);
        	if(map_id_fre.containsKey(temp_map_id))///�Ѿ�����map����id ���1
        	{
        		map_id_fre.put(temp_map_id, map_id_fre.get(temp_map_id)+1);
        	}else//�����µĴ���Ϊ1
        	{
        		map_id_fre.put(temp_map_id, 1);
        	}
        }//for
      	Iterator<Entry<Integer, Integer>> iter = map_id_fre.entrySet().iterator();
      	int fre=0;
      	List<Integer> max_num_map_id=new ArrayList<Integer>();//�Ƿ����ֶ��������ͬ��map_id �����Ƿ��С
      	while(iter.hasNext())
      	{ 
      		 Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>)iter.next();
      		 if(entry.getValue()>fre)
      		 {
      			fre=entry.getValue();
      			max_num_map_id.clear();
      			max_num_map_id.add(entry.getKey());
      		 }else if(entry.getValue()==fre)
      		 {
      			max_num_map_id.add(entry.getKey());
      		 }else
      		 {
      			 
      		 }
      	}
      	int map_id=0;
      	if(max_num_map_id.size()>1)///��ֹһ��map����id ���� ���뷽�� ����2
      	{
      		map_id=find_map_MIN_Distance(wifi_ssid, wifi_up,
      				sim_max_cluster_id,m_DatabaseManager);
      	}else if(max_num_map_id.size()==1)
      	{
      		map_id=max_num_map_id.get(0);
      	}else
      	{
      		map_id=-1;
      	}
		return map_id;
	}
	/////����2��ѯÿ�����������ľ��� �ҳ������һ��
	int find_map_MIN_Distance(List<String> wifi_ssid, Map<String, Double> wifi_up,
			List<Integer> sim_max_cluster_id,DatabaseManager m_DatabaseManager)
	{
		double min_distance=Double.MAX_VALUE;
		int cluster_id=0;
        for(Integer id:sim_max_cluster_id)
        {
        	HashMap<String ,Integer> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info(id);
        	double distance=m_Distance.distance_double(wifi_up, temp_map);
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
        	double distance=0;
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
        	 	
        	}*/
        }//for
        int map_id=m_DatabaseManager.get_map_ID_from_cluster_centre(cluster_id);
		return map_id;
	}
	@Override
	List<Integer> find_right_sample_ok(List<String> wifi_ssid,
			Map<String, Double> wifi_up, int map_id,
			DatabaseManager m_DatabaseManager) {
		List<Integer> cluster_id_list =m_DatabaseManager.get_ALL_cluster_ID(map_id);
		int cluster_id_max_match=0;
        ///////�����ƶ�����һ���
		System.out.println("map_id:"+map_id);
		System.out.println("find_right_sample_ok");
		System.out.println("�����ͺ�wifi����");
   		for(String dd:wifi_ssid)
   		{
   			System.out.println(dd+":"+wifi_up.get(dd));
   		}
        int sim=-1;//������ƶ�
        List<Integer> sim_max_cluster_id=new ArrayList<Integer>();//���ƶȿ��ܻ������ͬ�Ķ����
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
           	if(temp.size()>sim)///���ƶȴ���ԭʼ������ ԭ�������
           	{
           		sim_max_cluster_id.clear();
           		sim_max_cluster_id.add(id);
           		sim=temp.size();
           		
           	}else if(temp.size()==sim)//���ƶ���ͬ ������ID����
           	{
           		sim_max_cluster_id.add(id);
           	
           		
           	}else
           	{
           		
           	}
        }
        System.out.println("���ƶ�:"+sim);
        /*for(Integer temp_cluster_id:sim_max_cluster_id)
        {
        	System.out.println("cluster_id:"+temp_cluster_id);
        }*/
        System.out.println("");
        ///Ѱ�������һ��
       /*if(sim_max_cluster_id.size()>1)
        {

    		double min_distance=Double.MAX_VALUE;
    		int cluster_id=0;
            for(Integer id:sim_max_cluster_id)
            {
            	HashMap<String ,Integer> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info(id);
            	double distance=m_Distance.distance_double(wifi_up, temp_map);
            	System.out.println("cluster_id:"+id+"	distance:"+distance);
            	if(distance<min_distance)
        	 	{
        	 		cluster_id=id;
        	 		min_distance=distance;
        	 	}
          
            }//for
            cluster_id_max_match=cluster_id;    	
        }else
        {
        	cluster_id_max_match=sim_max_cluster_id.get(0);
        }
       */
        System.out.println("����ϴ�ID:"+cluster_id_max_match);
        
        ////��ʾ
        {
        	HashMap<String ,Integer> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info(cluster_id_max_match/*temp_cluster_id*/);
	    	//////��ǰ��ά��Ϣ
	       	 Iterator<Entry<String, Integer>> iter = temp_map.entrySet().iterator(); 
	       	 while(iter.hasNext())
	       	 { 
	       		 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
	       		System.out.println("wifi����:"+(String) entry.getKey());
	       	 }
        }
        ///////////
        System.out.println("����Ѱ�Һ��ʵ�:	"+sim_max_cluster_id.size());
        List<Integer> ok_sample_id2=new ArrayList<Integer>();///����ѡ�еĵ� Ϊcluster_array ��ֵ��Ϣ
        List<Integer> sample_id_list=new ArrayList<Integer>();///���ڵ�
        List<Integer> ok_return=new ArrayList<Integer>();///���ص�
        Set<Integer> set = new HashSet<Integer>();   ///������ļ��� ���ظ�
        for(Integer temp_cluster_id:sim_max_cluster_id)
        {
        	
        	double step=0;
            double step_by=0.25;//0.75
        	sample_id_list.clear();///��ʼ�µ�һ�ִ���Ѱ��
       		List<Integer> temp= m_DatabaseManager.get_ALL_KEY_cluster_array(/*cluster_id_max_match*/temp_cluster_id);///��ֵ
       		sample_id_list.addAll(temp);
       		
	       	HashMap<String ,Integer> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info(/*cluster_id_max_match*/temp_cluster_id);
	    	//////��ǰ��ά��Ϣ
	       	List<String> temp_cluster_id_wifi= new ArrayList<String>();///����ά����Ϣ
	       	 Iterator<Entry<String, Integer>> iter = temp_map.entrySet().iterator(); 
	       	 while(iter.hasNext())
	       	 { 
	       		 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
	       		temp_cluster_id_wifi.add((String) entry.getKey()); 
	       	 }
	     /////��׼��K-NN�㷨 ȡ�����K��
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
	        		int sample_id=m_DatabaseManager.get_sample_id_cluster_array(sample_id_list.get(i));
		        	HashMap<String ,Integer> temp_map_array=m_DatabaseManager.get_wifi_info(sample_id);
		        		
	        		 double distance=m_Distance.distance_double(wifi_up, temp_map_array,temp_cluster_id_wifi);
	             	
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
	       
	       	 ///////��ǰ��Ѱ��
	       	 // ���õ����ķ��� ����ĳ����Χ�ڵ����з��������ĵ� �������η���
	        /* while(ok_sample_id2.size()<1)
	         {
	        	 ok_sample_id2.clear();
	        	 for(Integer id:sample_id_list)
	             {
	        		int sample_id=m_DatabaseManager.get_sample_id_cluster_array(id);
	        		HashMap<String ,Integer> temp_map_array=m_DatabaseManager.get_wifi_info(sample_id);
	        		double distance=m_Distance.distance_double(wifi_up, temp_map_array,temp_cluster_id_wifi);
             	 	if(distance<step)
             	 	{
             	 		
             	 		ok_sample_id2.add(id);
             	 		
             	 	}
	            	 
	             }//for
	        	 step=step+step_by;
	        	 if(step>30)
	        	 {
	        			
	        		 System.out.println("step>30 ");
	        		  break;
	        	 }	 
	         }//while ok_sample_id2.size()<1
	         */
	         ///// һ��ID�ɴ����ڶ����
////////////////�󲢼�
	        //System.out.println("cluster_id:"+temp_cluster_id);
			for(Integer temp_add:ok_sample_id2)
			{
				 set.add(temp_add); 
			}
			ok_sample_id2.clear();
			step=0;
        }/////////for(Integer temp_cluster_id:sim_max_cluster_id)
        Integer[] all_sample_id_array= {};
		Integer[] all_sample_id_array1=set.toArray(all_sample_id_array);
		for(int i=0;i<all_sample_id_array1.length;i++)
		{
			ok_return.add(all_sample_id_array1[i]);
			//System.out.println(all_wifi_infotemp1[i]);
		}
        for(Integer kk:ok_return)
        {
        	System.out.println("sample_id:"+kk);
        	
        }
        System.out.println("sample_id����:"+ok_return.size());
        return ok_return;

	}

	@Override
	Map<String, Double> find_coordinate(List<Integer> ok_sample_id,
			List<String> wifi_ssid, Map<String, Double> wifi_up,
			DatabaseManager m_DatabaseManager) {
		
		HashMap<Integer ,Double> distance_map = new HashMap<Integer, Double>();
        /////�������
        for(Integer id:ok_sample_id)///id Ϊcluster_array�ļ�ֵ
        {
        	 int sample_id=m_DatabaseManager.get_sample_id_cluster_array(id);
        	
        	 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(sample_id);///�������wifi������Ϣ
        	 int belong_to_cluster=m_DatabaseManager.get_cluster_ID_from_array(id);
        	 HashMap<String ,Integer> cluster_wifi=m_DatabaseManager.get_cluster_centre_wifi_info(belong_to_cluster);
        	 List<String> cluster_wifi_list= new ArrayList<String>();///�ص�ά��Ϣ
           	 Iterator<Entry<String, Integer>> iter = cluster_wifi.entrySet().iterator(); 
           	 while(iter.hasNext())
           	 { 
           		Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
           		cluster_wifi_list.add((String) entry.getKey()); 
           	 }
           	 double distance=m_Distance.distance_double(wifi_up, temp_map,cluster_wifi_list);
        	 /*HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(sample_id);///�������wifi������Ϣ
        	 int belong_to_cluster=m_DatabaseManager.get_cluster_ID_from_array(id);
        	 HashMap<String ,Integer> cluster_wifi=m_DatabaseManager.get_cluster_centre_wifi_info(belong_to_cluster);
        	 List<String> cluster_wifi_list= new ArrayList<String>();///�ص�ά��Ϣ
           	 Iterator<Entry<String, Integer>> iter = cluster_wifi.entrySet().iterator(); 
           	 while(iter.hasNext())
           	 { 
           		Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
           		cluster_wifi_list.add((String) entry.getKey()); 
           	 }
           	 cluster_wifi_list.retainAll(wifi_ssid);
           	 double distance=0;
           	 for(String t:cluster_wifi_list)
           	 {
           		 distance=distance+Math.pow(temp_map.get(t)-wifi_up.get(t), 2);
           	 }
           	
           	 distance=Math.sqrt(distance);
           	 
         	//System.out.println("dis:"+distance);
           	 distance=distance/cluster_wifi_list.size();/////����Ϊ0
           	 */
         	
           	distance=Math.exp(-3*distance);
           	//if(distance<1.0E-3)
           	//	distance=0;
           	System.out.println("dis:"+distance);
           	//System.out.println("dis:"+distance);
           	 distance_map.put(id, distance);
        }
        ////��һ��
     
        ///��Ȩ
        
        Set<Integer> key = distance_map.keySet(); 
        double _x=0;
        double _y=0;
        double weight_all=0;
       	for (java.util.Iterator<Integer> it = key.iterator(); it.hasNext();)
       	{            
       		Integer s = (Integer) it.next(); 
       		int sample_id=m_DatabaseManager.get_sample_id_cluster_array(s);	
  			ResultSet  rs1=m_DatabaseManager.get_ResultSet_from_wifi_sample(sample_id);
            try {
				if(rs1.next())
				  {
				   	int temp_x=rs1.getInt("x");
				   	int temp_y=rs1.getInt("y");
				   	int temp_height=rs1.getInt("height");
				   	int temp_width=rs1.getInt("width");
				  		_x=_x+temp_x*1000*1/temp_width*distance_map.get(s);
				  		_y=_y+temp_y*1000*1/temp_height*distance_map.get(s);
				  		double test_dis=distance_map.get(s);
				  		//System.out.println("dis:"+test_dis);
				  		weight_all=weight_all+test_dis;
				  		System.out.println("x:"+temp_x+"	y:"+temp_y+"	id:"+s+"	scale:"+test_dis);
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
  		System.out.println("**********************************************");
  		System.out.println("**********************end*********************");
  		System.out.println("**********************************************");
  		FileWriter fw;
		try {
			fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"all_res.txt",true);
			double ttx=_x*1191.0/1000.0;
			double tty=_y*851.0/1000.0;
			
			fw.append("		"+ttx+"		"+tty+"		\n");
			
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		return xy_map;
	}

	/*@Override
	int train(DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		m_TrainSample=new CliqueClusterTrain();
		m_TrainSample.train(m_DatabaseManager);
		return 0;
	}*/
	@Override
	void initial_train() {
		// TODO Auto-generated method stub
		m_TrainSample=new CliqueClusterTrain();
	}
	@Override
	List<Integer> find_right_sample_ok_2(List<String> wifi_ssid,
			Map<String, Double> wifi_up, int map_id_1, int map_id_2,
			DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		return null;
	}

}
