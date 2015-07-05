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


public class KNN_based_on_PAM extends Algorithm {
	//////K_PAM里面最好  分类不加距离 分簇求均值 不使用方差
	int temp_map_id=0;
	Distance m_Distance=new DistanceLog();// DistanceAbsolute();
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
		 double step_by=0.75;
         List<Integer> ok_sample_id=new ArrayList<Integer>();
         
         /////寻找匹配的位置点ID
         double MAX_DOuble=Double.MAX_VALUE;
         /*int OK_ID=0;
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
        while(ok_sample_id.size()<1)
         {
        	 ok_sample_id.clear();
        	 for(Integer id:cluster_id_list)
             {
        		
        		 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_cluster_centre_wifi_info(id);
        		 double distance=m_Distance.distance_double(wifi_up, temp_map);
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
	/*@Override
	int train(DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		 m_TrainSample=new PAMClusterTrain();
		 m_TrainSample.train(m_DatabaseManager);
		return 0;
	}*/
	@Override
	void initial_train() {
		// TODO Auto-generated method stub
		 m_TrainSample=new PAMClusterTrain();
	}
	@Override
	List<Integer> find_right_sample_ok_2(List<String> wifi_ssid,
			Map<String, Double> wifi_up, int map_id_1, int map_id_2,
			DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		return null;
	}
}
