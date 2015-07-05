package code;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

//import org.ujmp.core.Matrix;
//import org.ujmp.core.doublematrix.*;
public class TCPDesktopServer {
	
	private static TransferServer m_TransferServer;
	 static void Run() {
		 try {
			m_TransferServer=new TransferServer(1818);
			m_TransferServer.service();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("failure");
		}
		
	}
    public static void main(String args[]) throws IOException {
    	
    	
    	
    	DatabaseManager m_DatabaseManager1=new DatabaseManager();
     	m_DatabaseManager1.Initial();
     //	KNN2 dd=new KNN2();
     	//KNN_DistanceLearn3 dd=new KNN_DistanceLearn3();
     	//KNN_DistanceLearn dd=new KNN_DistanceLearn();
     	//KNN_DistanceLearn dd=new KNN_DistanceLearn();
     	//PAMClusterTrain dd=new PAMClusterTrain();
     	//PAMClusterTrainNew dd=new PAMClusterTrainNew();
     	//dd.train(m_DatabaseManager1);
    	//KNN_basde_on_Clique dd=new KNN_basde_on_Clique();
    	//dd.train(m_DatabaseManager1);
     	//CliqueClusterTrainAdvaced dd=new CliqueClusterTrainAdvaced();
    //	dd.train(m_DatabaseManager1);
    	System.out.println("输入命令选择：test1为测试 其他为进入服务器");
    	Scanner sc=new Scanner(System.in);
    	String in=sc.next();
    	//int order=0;
    	
    	
    	if(in.equals("test1"))
    	{
    		Connection  m_conn=null;
    		try{
		   		   Class.forName("com.mysql.jdbc.Driver");
		   		   String url="jdbc:mysql://localhost:3306/wifi_location";
		   		   String user="root";/////用户名
		   		   String password="root";////密码
		   		   m_conn=DriverManager.getConnection(url,user,password);
		   		   if(m_conn!=null){
		       			   System.out.println("数据库连接成功");
		       		   }
    		 }catch(Exception e){
	       		   e.printStackTrace();
	       		   System.out.println("数据库初始化失败:"+e);
	       	   }
    		Statement ps_map1;
    		try 
			{
				ps_map1 = m_conn.createStatement();
		   	    String insert="INSERT INTO map(map_id,map_name)"+"VALUES(1,'zhuE.bmp')";
				ps_map1.executeUpdate(insert);
			    DatabaseManager m_DatabaseManager=new DatabaseManager();
	        	m_DatabaseManager.Initial();
			    List<Integer> cluster_id_list=m_DatabaseManager.get_ALL_cluster_ID();
			    ArrayList<String> FileNameList=new ArrayList<String>();
			    String txtPath="E:\\上传";
				 File file = new File(txtPath);
			      if(file.isDirectory()){
			           File [] fileArray = file.listFiles();

						for(File one_directory:fileArray)
						{
							
							if(one_directory.isDirectory())
							{
								File [] m_file=one_directory.listFiles();
								for(File temp_file:m_file)
								{
									if(temp_file.getPath().endsWith(".txt"))
									{
										FileNameList.add(temp_file.getPath());
									}
								}
							}
						}
			          
			           for(String path:FileNameList)
						{
			        	   System.out.println(path);
			        	   try {
			        		    File resultfile;
								String name=path;
								int name_length=name.length();
								String Orientation=name.substring(name.length()-5,name.length()-4);								
								double orientation=0;
								if(Orientation.equals("n")){
									orientation=0;
								}
								if(Orientation.equals("e")){
									orientation=90;
								}
								if(Orientation.equals("s")){
									orientation=180;
								}
								if(Orientation.equals("w")){
									orientation=270;
								}
								
								//resultfile = new File(name); 
								//PrintWriter fos_result = new PrintWriter(resultfile);
								File txtFile=new File(path);
								BufferedReader in_txt = new BufferedReader(new FileReader(path));
								String con="";																
									int j=0;									
									while((con=in_txt.readLine())!=null)
									{
										/****得到x,y,以及方向****/
										String[] tt=con.split("\\s+");
										
										double x_temp=Double.parseDouble(tt[1]) ;									
										double x_real=4126;//6300//6300//4126//4032
										int x_pixl=1568;//1191//1191//1568//6096
										double y_real=2610;//4500;//5400//2610//3825
										int y_pixl=988;//851//1044;//988//5785
										x_temp=x_temp/x_real*x_pixl;
										int map_id=1;
										int x=(int) x_temp;
										
										double y_temp=Double.parseDouble(tt[3]) ;
										y_temp=y_temp/y_real*y_pixl;
										int y=(int) y_temp;	
																														
										int current_sample_id=m_DatabaseManager.insert_into_wifi_sample(x,y,x_pixl,y_pixl,map_id);
										
										/*****insert_to_sensor******/
		            	            	m_DatabaseManager.insert_into_wifi_sensor(current_sample_id, orientation);
										/*****instert_to_section*****/
		            	    	      	int section=0;
		            	    	      	//此处以后为了应用方便应从数据库中提取数据而不是直接写出坐标
		            	    	      	
		            	    	      	
//		            	    	      	m_DatabaseManager.insert_into_sample_section(current_sample_id,section);
		            	    	      
		            	    	      	System.out.println(""+x+"    "+y);
										int t=(tt.length-4)/3;
										for(int i=4;i<tt.length;i=i+3)
										{
											String value_double=tt[i+1];
			            	    	      	double sum=Double.parseDouble(value_double);
			            	    	       String value_delt=tt[i+2];
			            	    	       double sumdelt2=Double.parseDouble(value_delt);
			            	    	      	/****insert_to_wifi_info****/
			            	    	      	m_DatabaseManager.insert_into_wifi_info(current_sample_id,tt[i],-sum,sumdelt2);//sumdelt2
			            	    	      	
			            	    	      	System.out.println(""+tt[i]+"    "+sum+"   ");  //sumdelt2
										}
										
										j++;
									}
									 System.out.println(""+j);
									 in_txt.close();
									 File FileDelete=new File(path);
						        	 FileDelete.delete();
								} catch (IOException e) {
										// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							
						}    
			      }
				
			} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
			}
    		return;
    	}
    	
    	
    	if(in.equals("ml"))
    	{

    		Connection  m_conn=null;
    		try{
		   		   Class.forName("com.mysql.jdbc.Driver");
		   		   String url="jdbc:mysql://localhost:3306/wifi_location";
		   		   String user="root";/////用户名
		   		   String password="root";////密码
		   		   m_conn=DriverManager.getConnection(url,user,password);
		   		   if(m_conn!=null){
		       			   System.out.println("数据库连接成功");
		       		   }
    		 }catch(Exception e){
	       		   e.printStackTrace();
	       		   System.out.println("数据库初始化失败:"+e);
	       	   }
    		Statement ps_map1;
    		try 
			{
				ps_map1 = m_conn.createStatement();
//		   	    String insert="INSERT INTO map(map_id,map_name)"+"VALUES(1,'zhuE.bmp')";
//				ps_map1.executeUpdate(insert);
			    DatabaseManager m_DatabaseManager=new DatabaseManager();
	        	m_DatabaseManager.Initial();
			    List<Integer> cluster_id_list=m_DatabaseManager.get_ALL_cluster_ID();
			    ArrayList<String> FileNameList=new ArrayList<String>();
			    String txtPath="E:\\室内定位2\\室内定位补实验\\不同区域数\\区域数30\\分区结果";
				 File file = new File(txtPath);
			      if(file.isDirectory()){
			    	  File [] fileArray = file.listFiles();
			    	  for(File file2:fileArray){
			    		  String section=file2.getName();
			    		  int section_id=Integer.valueOf(section);
			    		  File[] txtFile=file2.listFiles();
			    		  for(File txt:txtFile){
			    			  String txtpath=txt.getPath();
			    			  BufferedReader in_txt = new BufferedReader(new FileReader(txtpath));
			    			  String con="";
			    			  int j=0;
			    			  
			    			  String txtName=txt.getName();
			    			  String [] filename=txtName.split("x|y|e|n|s|w|c|y");
			    			  double x_temp=Double.parseDouble(filename[1]) ;									
							  double x_real=4126;//6300//6300//4126//4032
							  int x_pixl=1568;//1191//1191//1568//6096
							  double y_real=2610;//4500;//5400//2610//3825
							  int y_pixl=988;//851//1044;//988//5785
							  x_temp=x_temp/x_real*x_pixl;
							  int map_id=1;
							  int x=(int) x_temp;
								
							  double y_temp=Double.parseDouble(filename[2]) ;
							  y_temp=y_temp/y_real*y_pixl;
							  int y=(int) y_temp;
							  int current_sample_id=m_DatabaseManager.insert_into_wifi_sample_ml(x,y,x_pixl,y_pixl,section_id,map_id);	

			    			  
			 			 /* while((con=in_txt.readLine())!=null){
			    				    String[] tt=con.split("\\s+");							
									double x_temp=Double.parseDouble(tt[1]) ;									
									double x_real=4126;//6300//6300//4126//4032
									int x_pixl=1568;//1191//1191//1568//6096
									double y_real=2610;//4500;//5400//2610//3825
									int y_pixl=988;//851//1044;//988//5785
									x_temp=x_temp/x_real*x_pixl;
									int map_id=1;
									int x=(int) x_temp;
									
									double y_temp=Double.parseDouble(tt[3]) ;
									y_temp=y_temp/y_real*y_pixl;
									int y=(int) y_temp;
									
									int current_sample_id=m_DatabaseManager.insert_into_wifi_sample_ml(x,y,x_pixl,y_pixl,section_id,map_id);									            	    	      		            	    	      	            	    	      
	            	    	      	System.out.println(""+x+"    "+y);
									int t=(tt.length-4)/3;
									for(int i=4;i<tt.length;i=i+2)
									{
										String value_double=tt[i+1];
		            	    	      	double sum=Double.parseDouble(value_double);
		            	    	      
		            	    	      	m_DatabaseManager.insert_into_wifi_info_ml(current_sample_id,tt[i],sum,0);//sumdelt2
		            	    	      	
		            	    	      	System.out.println(""+tt[i]+"    "+sum+"   ");  //sumdelt2
									}
									
									j++;
			    			  }*/
			    			  System.out.println(""+section_id);
								 in_txt.close();
			    		  }
			    	  }
			      }
				
			} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
			}
			
    		return;
    	
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	if(in.equals("test"))
    	{
    		//in=sc.next();
    		//if(in.equals("d"))
    		//{
    		//	order=1;
    		//}else
    		//{
    		//	order=0;
    		//}
    		Connection  m_conn=null;
    		try{
		   		   Class.forName("com.mysql.jdbc.Driver");
		   		   String url="jdbc:mysql://localhost:3306/wifi_location";
		   		   String user="root";/////用户名
		   		   String password="root";////密码
		   		   m_conn=DriverManager.getConnection(url,user,password);
		   		   if(m_conn!=null){
		       			   System.out.println("数据库连接成功");
		       		   }
    		 }catch(Exception e){
	       		   e.printStackTrace();
	       		   System.out.println("数据库初始化失败:"+e);
	       	   }
    		////////////////////////////////////////////////////////////////////
    		
    		
    		
    			Statement ps_map1;
				try 
				{
					ps_map1 = m_conn.createStatement();
						
    			    ResultSet  rs_map0=ps_map1.executeQuery("SELECT * FROM test1 where machine_id=1 and name='6c:e8:73:51:7f:f8'");
    			    
    			    FileWriter fw;
					try {
						fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"swt48.txt");
					
    			    while(rs_map0.next())
	    			{
    			    	 String t=rs_map0.getString("name");
    		    		 int t_pass=rs_map0.getInt("value") ; 
    		    		 fw.write("		"+t_pass);
	    			}
    			    fw.flush();
		        	fw.close();
		        	ResultSet  rs_map1=ps_map1.executeQuery("SELECT * FROM test1 where machine_id=1 and name='DTT'");
    			    
    			    FileWriter fw1=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"DTT.txt");
    			    while(rs_map1.next())
	    			{
    			    	 String t=rs_map1.getString("name");
    		    		 int t_pass=rs_map1.getInt("value") ; 
    		    		 fw1.write("		"+t_pass);
	    			}
    			    fw1.flush();
		        	fw1.close();
		        	rs_map0=ps_map1.executeQuery("SELECT * FROM test1 where machine_id=0 and name='swt48'");
		        	fw=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"swt48_0.txt");
    			    while(rs_map0.next())
	    			{
    			    	 String t=rs_map0.getString("name");
    		    		 int t_pass=rs_map0.getInt("value") ; 
    		    		 fw.write("		"+t_pass);
	    			}
    			    fw.flush();
		        	fw.close();
		        	rs_map1=ps_map1.executeQuery("SELECT * FROM test1 where machine_id=0 and name='DTT'");
    			    
    			    fw1=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"DTT_0.txt");
    			    while(rs_map1.next())
	    			{
    			    	 String t=rs_map1.getString("name");
    		    		 int t_pass=rs_map1.getInt("value") ; 
    		    		 fw1.write("	"+t_pass);
	    			}
    			    fw1.flush();
		        	fw1.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			   
				} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
				}
    		/////////////////////////////////////////////////////////////////////////
    		int machine_id=0;
    		int i=2;
    		 HashMap<Integer ,Double> temp_distance = new HashMap<Integer ,Double>();
    		 HashMap<Integer ,Double> temp_distance1 = new HashMap<Integer ,Double>();
    		while(true)
    		{
    			Statement ps_map;
				try 
				{
					ps_map = m_conn.createStatement();
						
    			    ResultSet  rs_map0=ps_map.executeQuery("SELECT * FROM test1 where machine_id="+machine_id+" and id="+i);
    			    
    			    HashMap<String ,Integer> temp_map0 = new HashMap<String, Integer>();
    				 List<String> temp0= new ArrayList<String>();
    			    HashMap<String ,Integer> temp_map1= new HashMap<String, Integer>();
    				 List<String> temp1= new ArrayList<String>();
    			    while(rs_map0.next())
	    			{
    			    	 String t=rs_map0.getString("name");
    		    		 int t_pass=rs_map0.getInt("value") ; 
    		    		 temp_map0.put(t, t_pass);
    		    		 temp0.add(t);
	    			}
    			    if(temp0.size()==0)
    			    	break;
    			    rs_map0.close();
    			    int machine_id1=machine_id+1;
    			    ResultSet  rs_map1=ps_map.executeQuery("SELECT * FROM test1 where machine_id="+machine_id1+" and id="+i);
     			   
    			    while(rs_map1.next())
	    			{
    			    	 String t=rs_map1.getString("name");
    		    		 int t_pass=rs_map1.getInt("value") ; 
    		    		 temp_map1.put(t, t_pass);
    		    		 temp1.add(t);
	    			}
    			    if(temp0.size()==0)
    			    	break;
    			    rs_map1.close();
    			    temp0.retainAll(temp1);
    			   // if(order==1)
    			    {
    			    	 if(temp0.size()>1)
        		       	 {
        		       		 
        		       		int flag=0;
        		       		String s0=null;
        		       		String s1=null;
        		       		double distance=0;
        		       		for(String t:temp0)
        		       		{
        		       			if(flag==0)
        		       			{
        		       				flag++;
        		       				s0=t;
        		       			}else
        		       			{
        		       				s1=t;
        		       				distance=distance+Math.pow(temp_map0.get(s0)-temp_map1.get(s0)+temp_map0.get(s1)-temp_map1.get(s1), 2);
        		       				s0=s1;
        		       			}
        		       			
        		       		}
        		       		if(temp0.size()!=0)
        		          	 {

        		           	 	distance=distance/(temp0.size()-1);
        		           	 	
        		           	 	
        		          	 }
        		       	   temp_distance.put(i, distance);
        		       	 }
    			    }///oredr==1
    			    //if(order==0)
    			    {
    			    	
    			   	 double distance=0;
    	           	 for(String t:temp0)
    	           	 {
    	           		 distance=distance+Math.pow(temp_map0.get(t)-temp_map1.get(t), 2);
    	           	 }
    	           	 distance=distance/temp0.size();
    	           	 temp_distance1.put(i, distance);
    			    }
    			    i++;
    			   
				} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
				}
				try {
					FileWriter fw=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"test.txt");
					Iterator<Entry<Integer, Double>> iter = temp_distance.entrySet().iterator(); 
		        	 while(iter.hasNext())
		        	 { 
		        		 Map.Entry<Integer, Double> entry = (Map.Entry<Integer, Double>)iter.next();
		        		 fw.write("		"+entry.getValue());
		        	 } 
		        	 fw.flush();
		        	 fw.close();
		        	 FileWriter fw1=new FileWriter("F:" +File.separator+ "workspace" + File.separator+"test1.txt");
						Iterator<Entry<Integer, Double>> iter1 = temp_distance1.entrySet().iterator(); 
			        	 while(iter1.hasNext())
			        	 { 
			        		 Map.Entry<Integer, Double> entry = (Map.Entry<Integer, Double>)iter1.next();
			        		 fw1.write("	"+entry.getValue());
			        	 } 
			        	 fw1.flush();
			        	 fw1.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
    		         
    		}///while(true)
    	}else
    	{
    		try {
				Run();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    	
    
  }
	public TCPDesktopServer() {
		try {
			m_TransferServer=new TransferServer(1818);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}   

