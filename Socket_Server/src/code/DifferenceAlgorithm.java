package code;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
 * 不同设备采集到的信号强度不同 采用差分的方法消除终端的天线增益 
 * 
 * */
import java.util.Map.Entry;

public class DifferenceAlgorithm extends Algorithm {

	@Override
	int find_map_id(List<String> wifi_ssid, Map<String, Double> wifi_up,
			DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		List<Integer> sample_id_list =m_DatabaseManager.get_ALL_ID_wifi_sample_id();
        
        ///////
        int sample_id=0;
        double min_distance=10000000;
        for(Integer id:sample_id_list)
        {
       	 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
       	 List<String> temp= new ArrayList<String>();
       	 Iterator<Entry<String, Integer>> iter = temp_map.entrySet().iterator(); 
       	 while(iter.hasNext()){ 
       	 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
       	 temp.add((String) entry.getKey()); 
       	 } 
       	 temp.retainAll(wifi_ssid);
       	 ////////////////
       	 if(temp.size()>1)
       	 {
       		 
       		int flag=0;
       		String s0=null;
       		String s1=null;
       		double distance=0;
       		for(String t:temp)
       		{
       			if(flag==0)
       			{
       				flag++;
       				s0=t;
       			}else
       			{
       				s1=t;
       				distance=distance+Math.pow(temp_map.get(s0)-wifi_up.get(s0)+temp_map.get(s1)-wifi_up.get(s1), 2);
       				s0=s1;
       			}
       			
       		}
       		if(temp.size()!=0)
          	 {

           	 	distance=distance/(temp.size()-1);
           	 	if(distance<min_distance)
           	 	{
           	 		sample_id=id;
           	 		min_distance=distance;
           	 	}
           	 	
          	 }
       		
       	 }
       	 else
       	 {
   ////////////TODO
           	 
           	
       	 }
       	 
        }//for
       
        int map_id=m_DatabaseManager.get_map_ID_from_wifi_sample(sample_id);
		return map_id;
	}

	@Override
	List<Integer> find_right_sample_ok(List<String> wifi_ssid,
			Map<String, Double> wifi_up, int map_id,
			DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		
		 List<Integer> sample_id_list =m_DatabaseManager.get_wifi_sample_id_in_map(map_id);
		 int step=2;
         List<Integer> ok_sample_id=new ArrayList<Integer>();
         /////寻找匹配的位置点ID
         while(ok_sample_id.size()<1)
         {
        	 ok_sample_id.clear();
        	 for(Integer id:sample_id_list)
             {
        		
        		 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
            	 List<String> temp= new ArrayList<String>();
            	 Iterator<Entry<String, Integer>> iter = temp_map.entrySet().iterator(); 
            	 while(iter.hasNext()){ 
            	 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
            	 temp.add((String) entry.getKey()); 
            	 } 
            	 temp.retainAll(wifi_ssid);
            	 /////
            	 if(temp.size()>1)
               	 {
               		 
               		int flag=0;
               		String s0=null;
               		String s1=null;
               		double distance=0;
               		for(String t:temp)
               		{
               			if(flag==0)
               			{
               				flag++;
               				s0=t;
               			}else
               			{
               				s1=t;
               				distance=distance+Math.pow(temp_map.get(s0)-wifi_up.get(s0)+temp_map.get(s1)-wifi_up.get(s1), 2);
               				s0=s1;
               			}
               			
               		}
               		if(temp.size()!=0)
               		{

                	 	distance=distance/(temp.size()-1);
                	 	if(distance<step)
                	 		ok_sample_id.add(id);
               		}
               		
               	 }
               	 else
               	 {
           ////////////TODO
                   	 
                   	
               	 }
            	 
             }//for
        	 System.out.println("当前阈值："+step);
        	 step=step+2;
        	 if(step>2048)
        	 {
        		   System.out.println("失败 ");
        	 }
        		 
         }//while 
         return ok_sample_id;
	}
	@Override
	Map<String,Double> find_coordinate(List<Integer> ok_sample_id,List<String> wifi_ssid,Map<String,Double> wifi_up,DatabaseManager m_DatabaseManager)
	{
		
		HashMap<Integer ,Double> distance_map = new HashMap<Integer, Double>();
        /////计算比例
		
        for(Integer id:ok_sample_id)
        {
        	
        	
        	 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
        	 List<String> temp= new ArrayList<String>();
        	 Iterator<Entry<String, Integer>> iter = temp_map.entrySet().iterator(); 
        	 while(iter.hasNext())
        	 { 
        		 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
        		 temp.add((String) entry.getKey()); 
        	 } 
           	 temp.retainAll(wifi_ssid);
           	 
           	 if(temp.size()>1)
           	 {
           		 
           		int flag=0;
           		String s0=null;
           		String s1=null;
           		double distance=0;
           		for(String t:temp)
           		{
           			if(flag==0)
           			{
           				flag++;
           				s0=t;
           			}else
           			{
           				s1=t;
           				distance=distance+Math.pow(temp_map.get(s0)-wifi_up.get(s0)+temp_map.get(s1)-wifi_up.get(s1), 2);
           				s0=s1;
           			}
           			
           		}
           		if(temp.size()!=0)
              	 {

               	 	distance=distance/temp.size();
               	    distance_map.put(id, distance);
               	 	
              	 }
           		
           	 }//////temp.size()>1
           	 else
           	 {
       ////////////TODO
               	 
               	
           	 }
           
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
				  		_x=_x+temp_x*1000*1/temp_width*1/distance_map.get(s);
				  		_y=_y+temp_y*1000*1/temp_height*1/distance_map.get(s);
				  		weight_all=weight_all+1/distance_map.get(s);
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
  		return xy_map;
	}

	@Override
	int train(DatabaseManager m_DatabaseManager) {
		// 无训练
		return 0;
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
