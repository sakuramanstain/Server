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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class KNN2 extends Algorithm {
	//////K-NN迭代计算 距离最近的若干点 半径逐渐增大
	int temp_map_id=0;
	List<String> getwifi(Map<String, Double> first,
			Map<String, Integer> second)
	{
		List<String> first_temp= new ArrayList<String>();
      	 Iterator<Entry<String, Double>> iter = first.entrySet().iterator(); 
      	 while(iter.hasNext()){ 
      	  Map.Entry<String, Double> entry = (Map.Entry<String, Double>)iter.next();
      	  first_temp.add((String) entry.getKey()); 
      	 } 
      	 List<String> second_temp= new ArrayList<String>();
      	Iterator<Entry<String, Integer>> iter2 = second.entrySet().iterator(); 
     	 while(iter2.hasNext()){ 
     	 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter2.next();
     	 second_temp.add((String) entry.getKey()); 
     	 } 
      	 
     	first_temp.retainAll(second_temp);
     	return first_temp;
	}
	List<String> getwifiInteger(Map<String, Integer> first,
			Map<String, Integer> second)
	{
		List<String> first_temp= new ArrayList<String>();
      	 Iterator<Entry<String, Integer>> iter = first.entrySet().iterator(); 
      	 while(iter.hasNext()){ 
      	  Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
      	  first_temp.add((String) entry.getKey()); 
      	 } 
      	 List<String> second_temp= new ArrayList<String>();
      	Iterator<Entry<String, Integer>> iter2 = second.entrySet().iterator(); 
     	 while(iter2.hasNext()){ 
     	 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter2.next();
     	 second_temp.add((String) entry.getKey()); 
     	 } 
      	 
     	first_temp.retainAll(second_temp);
     	return first_temp;
	}

	Distance m_Distance=new  DistanceAbsolute();//DistanceEuclidean(); DistanceAbsolute
	@Override
	int find_map_id(List<String> wifi_ssid, Map<String, Double> wifi_up,
			DatabaseManager m_DatabaseManager) {
		// TODO Auto-generated method stub
		List<Integer> sample_id_list =m_DatabaseManager.get_ALL_ID_wifi_sample_id();
        
        ///////
        int sample_id=0;
        double min_distance=-1;
        for(Integer id:sample_id_list)
        {
       	
       	HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
       	List<String> wifi_info=getwifi(wifi_up,temp_map);
       	HashMap<String ,Double []>  temp_map2=m_DatabaseManager.get_wifi_info_delt(id);
       	double distance=1;
       	for(String one:wifi_info)
       	{
       		distance*=Math.exp(-Math.pow(Math.log10(-wifi_up.get(one))-Math.log10(-temp_map2.get(one)[0]), 2)/(2*temp_map2.get(one)[1]))/Math.sqrt(temp_map2.get(one)[1]);
       	}
       	distance=Math.pow(distance, 1.0/wifi_info.size());
       	if(distance>min_distance)
		 	{
		 		sample_id=id;
		 		min_distance=distance;
		 	}
        }//for
       
        int map_id=m_DatabaseManager.get_map_ID_from_wifi_sample(sample_id);
        
        
        temp_map_id=map_id;
		return map_id;
	}
	
	@Override
	List<Integer> find_right_sample_ok(List<String> wifi_ssid ,Map<String,Double> wifi_up,int map_id,DatabaseManager m_DatabaseManager)
	{
		// TODO Auto-generated method stub
		 List<Integer> sample_id_list =m_DatabaseManager.get_wifi_sample_id_in_map(map_id);
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
        		 K_NN_value_OK[j]=(double) (0-j);
        		 K_NN_OK[j]=-1;
        	 }
        	 for(int i=0;i<sample_id_list.size();i++)
        	 {
        		 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(sample_id_list.get(i));
        		 List<String> wifi_info=getwifi(wifi_up,temp_map);
        	       	HashMap<String ,Double []>  temp_map2=m_DatabaseManager.get_wifi_info_delt(sample_id_list.get(i));
        	    	double distance=1;
        	       	for(String one:wifi_info)
        	       	{
        	       		distance*=Math.exp(-Math.pow(Math.log10(-wifi_up.get(one))-Math.log10(-temp_map2.get(one)[0]), 2)/(2*temp_map2.get(one)[1]))/Math.sqrt(temp_map2.get(one)[1]);
        	       	}
        	       	distance=Math.pow(distance, 1.0/wifi_info.size());
        		 if(distance>K_NN_value_OK[K_NN-1])
        		 {
        			 System.out.println(" "+ sample_id_list.get(i));
        			 System.out.println(" "+ wifi_info.size());
        			 int ii=K_NN-1;
        			 K_NN_OK[K_NN-1]=sample_id_list.get(i);
        			 K_NN_value_OK[K_NN-1]=distance;
        			 
        			 while(ii>0&&K_NN_value_OK[ii-1]<K_NN_value_OK[ii])
        			 {
        				 double temp_value=K_NN_value_OK[ii];
        				 K_NN_value_OK[ii]=K_NN_value_OK[ii-1];
        				 K_NN_value_OK[ii-1]=temp_value;
        				 int temp_id=K_NN_OK[ii];
        				 K_NN_OK[ii]=K_NN_OK[ii-1];
        				 K_NN_OK[ii-1]=temp_id;
        				 ii--;
        			 }
        			 for(int j=0;j<K_NN;j++)
        			 {
        				 System.out.print(""+K_NN_OK[j]);
        				 //System.out.print("    ");
        			 }
        			 System.out.println();
        			 for(int j=0;j<K_NN;j++)
        			 {
        				 System.out.print(""+K_NN_value_OK[j]);
        				 System.out.print("   ");
        			 }
        			 System.out.println();
        		 }
        	 }
        	 for(Integer ok_id:K_NN_OK)
        	 {
        		 ok_sample_id.add(ok_id);
        	 }
         }
         
         /////寻找匹配的位置点ID
         //采用迭代的方法 发现某个范围内的所有符合条件的点 限制依次放松
         /*while(ok_sample_id.size()<1)
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
        	 HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(id);
            	List<String> wifi_info=getwifi(wifi_up,temp_map);
            	HashMap<String ,Double []>  temp_map2=m_DatabaseManager.get_wifi_info_delt(id);
            	double distance=1;
               	for(String one:wifi_info)
               	{
               		distance*=Math.exp(-Math.pow(Math.log10(-wifi_up.get(one))-Math.log10(-temp_map2.get(one)[0]), 2)/(2*temp_map2.get(one)[1]))/Math.sqrt(temp_map2.get(one)[1]);
               	}
               	distance=Math.pow(distance, 1.0/wifi_info.size());
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
        	 //System.out.println("  "+ distance);
        	 //distance=1/distance;//Math.exp(-2*distance);
           	 distance_map.put(id, distance);
           	// System.out.println("  "+ distance);
       	
        }
        ///加权
        System.out.println(ok_sample_id);
        Set<Integer> key = distance_map.keySet(); 
        double _x=0;
        double _y=0;
        double weight_all=0;
        
        
        double x_pixl=1568.0;
		double p_pixl=988.0;
		
        while(true)
        {
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
    					xy[1]=temp_y*1000.0*1.0/temp_width;
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
      		 boolean flag=false;
      		 if(P>Math.sqrt(delat)){
      			
      			System.out.println("remove:"+ok_sample_id.get(id)+"    X:  "+ sample_id_xy.get(ok_sample_id.get(id))[0]+"   Y:  "+sample_id_xy.get(ok_sample_id.get(id))[1]);
     
      			ok_sample_id.remove(id);
      		 }else{
    	    	flag=true;
      		 }
      		/*	for (java.util.Iterator<Integer> it = key.iterator(); it.hasNext();)
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
    				  		System.out.println("x:"+temp_x*x_pixl/1000.0+"	y:"+temp_y*p_pixl/1000.0+"	id:"+s+"   distance:  "+distance_map.get(s));
    				  }
    				rs1.close();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                   
      				
      		}
          	*/
          	if(flag)
          		break;
        }
        System.out.println(ok_sample_id);
        for(int i=0;i<ok_sample_id.size();i++)
        {
        	int s =ok_sample_id.get(i); 
				
  			ResultSet  rs1=m_DatabaseManager.get_ResultSet_from_wifi_sample(s);
            try {
				if(rs1.next())
				  {
				   	int temp_x=rs1.getInt("x");
				   	int temp_y=rs1.getInt("y");
				   	int temp_height=rs1.getInt("height");
				   	int temp_width=rs1.getInt("width");
				  		_x=_x+temp_x*1000.0*1.0/temp_width*distance_map.get(s);
				  		_y=_y+temp_y*1000.0*1.0/temp_height*distance_map.get(s);
				  		weight_all=weight_all+distance_map.get(s);
				  		System.out.println("x:"+temp_x*x_pixl/temp_width+"	y:"+temp_y*p_pixl/temp_height+"	id:"+s+"   distance:  "+distance_map.get(s));
				  }
				rs1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        /*	for (java.util.Iterator<Integer> it = key.iterator(); it.hasNext();)
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
				  		System.out.println("x:"+temp_x*x_pixl/1000.0+"	y:"+temp_y*p_pixl/1000.0+"	id:"+s+"   distance:  "+distance_map.get(s));
				  }
				rs1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
               
  				
  		}
      	*/
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
			
			fw.append("		"+ttx+"			"+tty+"		\n");
			fw.flush();
			fw.close();
			System.out.println("x:"+ttx+"	y:"+tty);
			
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
			try {
				File temp_distance=new File("F:"+File.separator+"workspace"+File.separator+"temp_distance"+temp_map_id+".txt");
				if(temp_distance.exists())
				{
					temp_distance.delete();
					
						temp_distance.createNewFile();
					
				}else
					temp_distance.createNewFile();
				BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp_distance)));  
				System.out.println("map_id:"+temp_map_id);
				List<Integer> all_sample_id= m_DatabaseManager.get_wifi_sample_id_in_map(temp_map_id);
				////////////////
				for(Integer temp:all_sample_id)
				{
					String dd="";
					HashMap<String ,Integer> temp_map=m_DatabaseManager.get_wifi_info(temp);
					for(Integer temp1:all_sample_id)
					{
							
						HashMap<String ,Integer> temp_map1=m_DatabaseManager.get_wifi_info(temp1);
					    List<String> wifi_info=getwifiInteger(temp_map,temp_map1);
					    HashMap<String ,Double []>  temp_map2=m_DatabaseManager.get_wifi_info_delt(temp1);
					    double distance=1;
					    for(String one:wifi_info)
					    {
					       	distance*=Math.exp(-Math.pow(Math.log10(-temp_map.get(one))-Math.log10(-temp_map2.get(one)[0]), 2)/(2*temp_map2.get(one)[1]))/Math.sqrt(temp_map2.get(one)[1]);
					    }
					    distance=Math.pow(distance, 1.0/wifi_info.size());
					    dd+=""+distance+"   ";
				}
					bw.write(dd);
					bw.newLine();
					bw.flush();
				}
			    bw.close();
////////////////////////////////////////////
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
						
						BufferedWriter bw_from=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("F:" +File.separator+ "workspace" + File.separator+"tempfile"+File.separator+from+"_id_all.txt")))); 
						Map<String,Integer> wifi_info_from =m_DatabaseManager.get_wifi_info(from);
						
						for(Integer to:all_sample_id)
						{
							HashMap<String ,Double []>  temp_map2=m_DatabaseManager.get_wifi_info_delt(to);
							String dd="";//+to+"  ";
							double delt=1;
							for(String one:dimension_list)
							{
								double value=-Math.pow(Math.log10(-wifi_info_from.get(one))-Math.log10(-temp_map2.get(one)[0]), 2)/(2*temp_map2.get(one)[1]);
								delt*=1/Math.sqrt(temp_map2.get(one)[1]);
								dd+=""+value+"  ";
							} 
							dd+=""+delt;
							bw_from.write(dd);
							bw_from.newLine();
						}
						bw_from.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				double [] P_bij=new double[dimension_list.size()];
				
				int b_i=0;
				for(Integer from:all_sample_id)
				{
					BufferedReader in_txt;
					try {
						in_txt = new BufferedReader(new FileReader("F:" +File.separator+ "workspace" + File.separator+"tempfile"+File.separator+from+"_id_all.txt"));
						String con="";
						try {
							int b_j=0;
							while((con=in_txt.readLine())!=null)
							{
								String [] value=con.split("\\s+");
								double bij=-10;//Double.parseDouble(value[value.length-1]);
								//if(b_i!=b_j)
								//	bij*=1e-7;
								//bij=Math.log(bij);
								for(int i=0;i<value.length-1;i++)
								{
									P_bij[i]+=bij*Double.parseDouble(value[i]);
								}
								b_j++;
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
					b_i++;
				}
				///////////////
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
								for(int i=0;i<value.length-1;i++)
								{
									double m_i=Double.parseDouble(value[i]);
									int j=0;
									while(j<value.length-1)
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
						value+=result[i][j]*P_bij[j];
					}
					result_w[i]=value;
				}
				System.out.print("加权系数");
				System.out.println(dimension_list);
				for (int i = 0; i < dimension_list.size(); i++) {
					System.out.print(""+result_w[i]+"	");
				}
				//////////////////////////////////////////
				
				
				try {
					File temp_distance1=new File("F:"+File.separator+"workspace"+File.separator+"处理后点之间的距离"+temp_map_id+".txt");
					if(temp_distance.exists())
					{
						temp_distance1.delete();
						temp_distance1.createNewFile();
					}else
						temp_distance1.createNewFile();
					//fw_temp = new FileWriter (temp_distance,true);
					   BufferedWriter bw1=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp_distance1)));  
					for(Integer temp_Integer_from:all_sample_id)
					{
						HashMap<String ,Integer> from=m_DatabaseManager.get_wifi_info(temp_Integer_from);
						String dd="";
						for(Integer temp_Integer_to:all_sample_id)
						{
							//HashMap<String ,Integer> to=m_DatabaseManager.get_wifi_info(temp_Integer_to);
							int i=0;
							HashMap<String ,Double []>  to=m_DatabaseManager.get_wifi_info_delt(temp_Integer_to);
							double distance=1;
							for(String one:dimension_list)
							{
							    distance*=Math.exp(-/*result_w[i]*/Math.pow(Math.log10(-from.get(one))-Math.log10(-to.get(one)[0]), 2)/(2*to.get(one)[1]))/Math.sqrt(to.get(one)[1]);
							    i++;
							}
							distance=Math.pow(distance, 1.0/dimension_list.size());
							
							dd+=""+distance+"   ";
						}
						bw1.write(dd);
						bw1.newLine();
						bw1.flush();
					}
					//fw_temp.close();
					bw1.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		return 0;
	}
	double getdistance(double m,double n)
	{
		double value=Math.pow(m-n, 2);
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
