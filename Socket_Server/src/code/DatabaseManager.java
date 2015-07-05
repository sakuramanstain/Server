package code;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * ���ݿ�ṹ 4����
 * ��map ���� ��ͼ��Ϣ ������ͼ��ID�ź����� ID��Ϊ���� map����Դ ������ �ƶ��Ĺ���Ŀ¼�� �ļ�����Ϊ��ͼ����
 * wifi_info �洢�����ÿ��wifi������ǿ�ȣ�wifi���ƺ����Ӧ�Ĳ����ص��sample_id��wifi����info����idΪ���� ��ͬ������Ĳ����ͬwifi���ڲ�ͬ���� 
 * wifi_sample �����������λ����Ϣ ���� ����ڵ�ͼ���Ͻǵ����� ˮƽ����Ϊx ��ֱ����Ϊy ����ʱ��Ӧ�ĵ�ͼ��Ⱥ͸߶� ���е�ͼ��Ӧ��map����id sample����idΪ���� 
 * user_count �����˻���Ϣ 
 * 
 * */
public class DatabaseManager {
	private Connection m_conn=null;
////////**************************���ݿ�����ʼ����ʹ�ø�����������ǰ���ñ�����************************/////////////	
	public void Initial()
	{
		
   	   try{
   		   Class.forName("com.mysql.jdbc.Driver");
   		   String url="jdbc:mysql://localhost:3306/wifi_location";
   		   String user="root";/////�û���
   		   String password="root";////����
   		   m_conn=DriverManager.getConnection(url,user,password);
   		   if(m_conn!=null){
       			   System.out.println("���ݿ����ӳɹ�");
       		   }
       	   }catch(Exception e){
       		   e.printStackTrace();
       		   System.out.println("���ݿ��ʼ��ʧ��:"+e);
       	   }
	}
	public Connection get_Connection()
	{
		return m_conn;
	}
	
////////**************************����У�麯��************************/////////////		
	public int check_user_password(String user_name,String password)
	{
		if(get_Connection()==null)
		{
			System.out.println("���ݿ����Ӳ��ɹ�");
			return 2;
		}
		 Connection conn=get_Connection();
    	 Statement ps;
		try {
			ps = conn.createStatement();
			ResultSet  rs=ps.executeQuery("SELECT * FROM user_count ");
        
	         while(rs.next())
	    	 {
	    		 String t=rs.getString("user");
	    		 String t_pass=rs.getString("password");
	    		if(t.equals(user_name)&&t_pass.equals(password))
	    		{
	    			rs.close();
	    			return 0;
	    		}
	    	 }
	         rs.close();
	         return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return 2;
		}
         
	}//////////////check_user_password
////////**************************�������ݿ�ĵ�ͼ��Ϣ����������ڣ���ֱ�Ӳ������ݿ⣬���򲻲������ݿ⣬ֻ��������Դ************************/////////////		
     public int InsertMaptoDataBase(String map_name)//����map���ݵ����ݿ�
     {
    	if(get_Connection()==null)
 		{
 			System.out.println("���ݿ����Ӳ��ɹ�");
 			return 2;
 		}
    	Connection conn=get_Connection();
    	/////////����Ƿ���ڵ�ͼ
    	try 
	   	{
		    	 Statement  ps_map = conn.createStatement();
		    	 ResultSet  rs_map=ps_map.executeQuery("SELECT * FROM map ");
		    	 while(rs_map.next())
		    	 {
		    		 String t=rs_map.getString("map_name");
		    		if(t.equals(map_name))
		    		{
		    			rs_map.close();
		    			return 1;
		    		}
		    	 }
		    	 rs_map.close();
		  
	   	 }
	   	 catch (SQLException e)
	   	 {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("���ݿ����ʧ��:"+e);
				return 2;
		}
    	////////�����ͼ
  	    String  sqlparse = "insert into map(map_id,map_name) values(?,?)";
  	    PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sqlparse);
	  	    ps.setNull(1, Types.INTEGER);
	        ps.setString(2, map_name);
	        
	        ps.executeUpdate();
	        return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return 2;
		}
    
     }/////Insert_Map
////////**************************��ȡ���ݿ��ж�Ӧmap���Ƶ�ID��************************/////////////		   
     public int get_map_id_in_map(String map_name)//��ȡmap��ID��
     {
    	if(get_Connection()==null)
  		{
  			System.out.println("���ݿ����Ӳ��ɹ�");
  			return -1;
  		}
    	 Connection conn=get_Connection();
    	 int current_map_id=-1;
  	   	 ///////////////////
    	 try 
    	 {
	    	 Statement  ps_map = conn.createStatement();
	    	 ResultSet  rs_map=ps_map.executeQuery("SELECT * FROM map ");
	    	 while(rs_map.next())
	    	 {
	    		 String t=rs_map.getString("map_name");
	    		if(t.equals(map_name))
	    		{
	    			current_map_id=rs_map.getInt(1);
	    			break;
	    		}
	    	 }
	    	 rs_map.close();
	    	 return current_map_id;
    	 }
    	 catch (SQLException e)
    	 {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			System.out.println("���ݿ����ʧ��:"+e);
 			return current_map_id;
 		 }
    	
     }//get_map_id_in_map
     ///////////////////////////////���ϴ�������͵�ͼ��Ϣ������ʱ���ݿ�////////////////////////////////////
     public int insert_into_temp_wifi_sample(int x,int y,int width,int height,int current_map_id,String account)//��������
     {

    	 if(get_Connection()==null)
   		{
   			System.out.println("���ݿ����Ӳ��ɹ�");
   			return -1;
   		}
     	 Connection conn=get_Connection();
    	 String  sqlparse = "insert into temp_wifi_sample(temp_sample_id,x,y,width,height,map_id,account) values(?,?,?,?,?,?,?)";
  		 PreparedStatement ps;
		try {
			 ps = conn.prepareStatement(sqlparse);
	  		 ps.setNull(1, Types.INTEGER);
	         ps.setInt(2,x);
	         ps.setInt(3,y);
	         ps.setInt(4,width);
	         ps.setInt(5,height);
	         ps.setInt(6, current_map_id);
	         ps.setString(7, account);
	         ps.executeUpdate();
	         ResultSet  rs=ps.executeQuery("SELECT max(sample_id) FROM wifi_sample");
	         int current_sample_id=0;
	         if (rs.next()) {
	        	 current_sample_id = rs.getInt(1);
	            } else {
	             // throw an exception from here
	            }
	         rs.close();
	         return current_sample_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
 			System.out.println("���ݿ����ʧ��:"+e);
 			return -1;
		}
      
     }//insert_into_temp_wifi_sample
////////**************************���ϴ�������͵�ͼ��Ϣ�������ݿ�************************/////////////		        
     public int insert_into_wifi_sample(int x,int y,int width,int height,int current_map_id)//��������
     {
    	 if(get_Connection()==null)
   		{
   			System.out.println("���ݿ����Ӳ��ɹ�");
   			return -1;
   		}
     	 Connection conn=get_Connection();
    	 String  sqlparse = "insert into wifi_sample(sample_id,x,y,width,height,map_id) values(?,?,?,?,?,?)";
  		 PreparedStatement ps;
		try {
			 ps = conn.prepareStatement(sqlparse);
	  		 ps.setNull(1, Types.INTEGER);
	         ps.setInt(2,x);
	         ps.setInt(3,y);
	         ps.setInt(4,width);
	         ps.setInt(5,height);
	         ps.setInt(6, current_map_id);
	         ps.executeUpdate();
	         ResultSet  rs=ps.executeQuery("SELECT max(sample_id) FROM wifi_sample");
	         int current_sample_id=0;
	         if (rs.next()) {
	        	 current_sample_id = rs.getInt(1);
	            } else {
	             // throw an exception from here
	            }
	         rs.close();
	         return current_sample_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
 			System.out.println("���ݿ����ʧ��:"+e);
 			return -1;
		}
     }////insert_into_wifi_sample
////////**************************���ϴ���wifi�źź���Ӧ�ĵ�ͼ��Ϣ�������ݿ�************************/////////////     
  public int insert_into_temp_wifi_info(int current_sample_id,String str_wifi_name,double wifi_value)///����wifi_info
  {
 	if(get_Connection()==null)
 	{
 			System.out.println("���ݿ����Ӳ��ɹ�");
 			return 2;
 	}
 	Connection conn=get_Connection();
 	String  sqlparse_wifi = "insert into temp_wifi_info(temp_wifi_info_id,temp_sample_id,ssid,level,delat) values(?,?,?,?,?)";
		 PreparedStatement ps_wifi;
		try {
			 ps_wifi = conn.prepareStatement(sqlparse_wifi);
	 		 ps_wifi.setNull(1, Types.INTEGER);
	 		 ps_wifi.setInt(2,current_sample_id);
	 		 ps_wifi.setString(3,str_wifi_name);
	 		 ps_wifi.setInt(4,(int)wifi_value);
	 		 ps_wifi.setDouble(5, 0);
	 		 ps_wifi.executeUpdate();
	 		 return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return 2;
		}
  }///insert_into_temp_wifi_info
     
////////**************************���ϴ���wifi�źź���Ӧ�ĵ�ͼ��Ϣ�������ݿ�************************/////////////     
     public int insert_into_wifi_info(int current_sample_id,String str_wifi_name,double wifi_value)///����wifi_info
     {
    	if(get_Connection()==null)
    	{
    			System.out.println("���ݿ����Ӳ��ɹ�");
    			return 2;
    	}
    	Connection conn=get_Connection();
    	String  sqlparse_wifi = "insert into wifi_info(wifi_info_id,sample_id,ssid,level,delat) values(?,?,?,?,?)";
 		 PreparedStatement ps_wifi;
		try {
			 ps_wifi = conn.prepareStatement(sqlparse_wifi);
	 		 ps_wifi.setNull(1, Types.INTEGER);
	 		 ps_wifi.setInt(2,current_sample_id);
	 		 ps_wifi.setString(3,str_wifi_name);
	 		 ps_wifi.setInt(4,(int)wifi_value);
	 		 ps_wifi.setDouble(5, 0);
	 		 ps_wifi.executeUpdate();
	 		 return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return 2;
		}
     }///insert_into_wifi_info
////////**************************���ϴ���wifi�źź���Ӧ�ĵ�ͼ��Ϣ�������ݿ�************************/////////////     
  public int insert_into_wifi_info(int current_sample_id,String str_wifi_name,double wifi_value,double delt)///����wifi_info
  {
 	if(get_Connection()==null)
 	{
 			System.out.println("���ݿ����Ӳ��ɹ�");
 			return 2;
 	}
 	Connection conn=get_Connection();
 	String  sqlparse_wifi = "insert into wifi_info(wifi_info_id,sample_id,ssid,level,delat) values(?,?,?,?,?)";
		 PreparedStatement ps_wifi;
		try {
			 ps_wifi = conn.prepareStatement(sqlparse_wifi);
	 		 ps_wifi.setNull(1, Types.INTEGER);
	 		 ps_wifi.setInt(2,current_sample_id);
	 		 ps_wifi.setString(3,str_wifi_name);
	 		 ps_wifi.setInt(4,(int)wifi_value);
	 		 ps_wifi.setDouble(5, delt);
	 		 ps_wifi.executeUpdate();
	 		 return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return 2;
		}
  }///insert_into_wifi_info
////////**************************���ϴ���wifi�źź���Ӧ�Ĵ��������ݴ������ݿ�************************///////////// 
  public int insert_into_wifi_sensor(int current_sample_id,double orientation)
  {
	  if(get_Connection()==null)
	 	{
	 			System.out.println("���ݿ����Ӳ��ɹ�");
	 			return 2;
	 	}
	  Connection conn=get_Connection();
	  String  sqlparse_wifi = "insert into sensor(sensor_id,sensor_orientation,sensor_A,sensor_B,sample_id) values(?,?,?,?,?)";
	  PreparedStatement ps_wifi;
	  try {
			 ps_wifi = conn.prepareStatement(sqlparse_wifi);
	 		 ps_wifi.setNull(1, Types.INTEGER);
	 		 ps_wifi.setDouble(2,orientation);	
	 		 ps_wifi.setDouble(3,0);	
	 		 ps_wifi.setDouble(4,0);	
	 		 ps_wifi.setInt(5, current_sample_id);
	 		 ps_wifi.executeUpdate();
	 		 return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return 2;
		}
  }
////////**************************���ϴ���wifi�źź���Ӧ��λ���������ݴ������ݿ�************************///////////// 
  public int insert_into_sample_section(int current_sample_id,int section)
  {
	  if(get_Connection()==null)
	 	{
	 			System.out.println("���ݿ����Ӳ��ɹ�");
	 			return 2;
	 	}
	  Connection conn=get_Connection();
	  String  sqlparse_wifi = "insert into section_sample(num,sample_id,section_id) values(?,?,?)";
	  PreparedStatement ps_wifi;
	  try {
			 ps_wifi = conn.prepareStatement(sqlparse_wifi);
	 		 ps_wifi.setNull(1, Types.INTEGER);	
	 		 ps_wifi.setInt(2, current_sample_id);
	 		 ps_wifi.setInt(3, section);
	 		 ps_wifi.executeUpdate();
	 		 return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return 2;
		}
  }
////////**************************��ȡ���ݿ������е�wifi�������ID************************/////////////       
     public List<Integer> get_ALL_ID_wifi_sample_id()//��ȡ���в�����ID
     {
    	 List<Integer> sample_id=new ArrayList<Integer>();
    	 if(get_Connection()==null)
     	 {
     			System.out.println("���ݿ����Ӳ��ɹ�");
     			return sample_id;
     	 }
    	 Connection conn=get_Connection();
    	 Statement ps;
		try {
			 ps = conn.createStatement();
	    	 ResultSet  rs=ps.executeQuery("SELECT * FROM wifi_sample");
	    	 while(rs.next())
	    	 {
	    		 int sample_id_temp=rs.getInt(1);
	    		 sample_id.add(sample_id_temp);
	    	 }
	         rs.close();
	         return sample_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return sample_id;
		}
     }/////get_ALL_wifi_sample
////////**************************��ȡ���ݿ���wifi������sample_id��Ӧ������ǿ������Ϣ************************/////////////  
     public HashMap<String ,Integer> get_wifi_info(int sample_id)///"SELECT * FROM wifi_info where sample_id="+id
     {
    	 HashMap<String ,Integer> temp_map = new HashMap<String, Integer>();
    	 if(get_Connection()==null)
     	 {
     			System.out.println("���ݿ����Ӳ��ɹ�");
     			return temp_map;
     	 }
    	 Connection conn=get_Connection();
		try {
			 Statement  ps1 = conn.createStatement();
        	 ResultSet  rs1=ps1.executeQuery("SELECT * FROM wifi_info where sample_id="+sample_id);
        	 while(rs1.next())
        	 {
        		
        		 temp_map.put(rs1.getString("ssid"), rs1.getInt("level"));
        	 }
        	 rs1.close();
        	 return temp_map;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return temp_map;
		}
     }///"SELECT * FROM wifi_info where sample_id="+id
////////**************************��ȡ���ݿ���wifi������sample_id���ڵ�map��ID************************/////////////    
     public int get_map_ID_from_wifi_sample(int sample_id)///SELECT * FROM wifi_sample where sample_id="+map_id_temp
     {
    	 int map_id=-1;
    	 if(get_Connection()==null)
     	 {
     			System.out.println("���ݿ����Ӳ��ɹ�");
     			return map_id;
     	 }
    	 Connection conn=get_Connection();
    	 Statement ps_map;
		try {
			 ps_map = conn.createStatement();
	    	 ResultSet  rs_map=ps_map.executeQuery("SELECT * FROM wifi_sample where sample_id="+sample_id);
	    	 if(rs_map.next())
	    	 {
	    		 map_id=rs_map.getInt("map_id");
	    	 }
	    	 rs_map.close();
	    	 return map_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return map_id;
		}
     }//get_wifi_sample
     public List<Integer> get_map_id()//��ȡ����map id
     {
    	 List<Integer> map_id=new ArrayList<Integer>();
    	 if(get_Connection()==null)
     	 {
     			System.out.println("���ݿ����Ӳ��ɹ�");
     			return map_id;
     	 }
    	 Connection conn=get_Connection();
    	 Statement ps;
   		try {
   			 ps = conn.createStatement();
   	    	 ResultSet  rs=ps.executeQuery("SELECT * FROM map");
   	    	 while(rs.next())
   	    	 {
   	    		 int map_id_temp=rs.getInt(1);
   	    		map_id.add(map_id_temp);
   	    	 }
   	         rs.close();
   	         return map_id;
   		} catch (SQLException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   			System.out.println("���ݿ����ʧ��:"+e);
   			return map_id;
   		}
     }/////get_wifi_sample_id_in_map
////////**************************��ȡ���ݿ���map��ID�ĵ�ͼ����************************/////////////  
     public String get_map_name_from_map(int map_id)///SELECT * FROM map where map_id
     {
    	 String imageName="";
    	 if(get_Connection()==null)
     	 {
     			System.out.println("���ݿ����Ӳ��ɹ�");
     			return imageName;
     	 }
    	 Connection conn=get_Connection();
    	 Statement ps1;
		try {
			 ps1 = conn.createStatement();
	    	 ResultSet  rs1=ps1.executeQuery("SELECT * FROM map where map_id="+map_id);
	    	 
	    	 if(rs1.next())
	    	 {
	    		 imageName=rs1.getString("map_name");
	    	 }
	    	rs1.close();
	    	return imageName;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return imageName;
		}
     }
////////**************************��ȡ���ݿ�������ǿ������wifi������sample_id�ļ���************************/////////////      
     public ResultSet get_ResultSet_from_wifi_sample(int sample_id)
     {
    	 
    	 ResultSet  rs1=null;
    	 if(get_Connection()==null)
     	 {
     			System.out.println("���ݿ����Ӳ��ɹ�");
     			return rs1;
     	 }
    	 Connection conn=get_Connection();
    	 Statement ps1;
		try {
			 ps1 = conn.createStatement();
		
	         rs1=ps1.executeQuery("SELECT * FROM wifi_sample where sample_id="+sample_id);
	         return rs1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return rs1;
		}
     }
////////**************************��ȡ���ݿ���ĳ��map ID�µ�sample_iD************************/////////////       
  public List<Integer> get_wifi_sample_id_in_map(int map_id)//��ȡ���в�����ID
  {
 	 List<Integer> sample_id=new ArrayList<Integer>();
 	 if(get_Connection()==null)
  	 {
  			System.out.println("���ݿ����Ӳ��ɹ�");
  			return sample_id;
  	 }
 	 Connection conn=get_Connection();
 	 Statement ps;
		try {
			 ps = conn.createStatement();
	    	 ResultSet  rs=ps.executeQuery("SELECT * FROM wifi_sample where map_id="+map_id);
	    	 while(rs.next())
	    	 {
	    		 int sample_id_temp=rs.getInt(1);
	    		 sample_id.add(sample_id_temp);
	    	 }
	         rs.close();
	         return sample_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return sample_id;
		}
  }/////get_wifi_sample_id_in_map
        /***************��ȡĳ��section�е�����sample_id*****************/ 
  public List<Integer> get_wifi_sample_id_in_section(int section_id)//��ȡ���в�����ID
  {
 	 List<Integer> sample_id=new ArrayList<Integer>();
 	 if(get_Connection()==null)
  	 {
  			System.out.println("���ݿ����Ӳ��ɹ�");
  			return sample_id;
  	 }
 	 Connection conn=get_Connection();
 	 Statement ps;
		try {
			 ps = conn.createStatement();
	    	 ResultSet  rs=ps.executeQuery("SELECT * FROM section_sample where section_id="+section_id);
	    	 while(rs.next())
	    	 {
	    		 int sample_id_temp=rs.getInt(2);
	    		 sample_id.add(sample_id_temp);
	    	 }
	         rs.close();
	         return sample_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return sample_id;
		}
  }
  /********************************************************/
  /***************��ȡĳ��section�е������ھ�����id*****************/ 
  public List<Integer> get_neighbor_id_in_section(int section_id)//��ȡ���в�����ID
  {
 	 List<Integer> neighbor_id=new ArrayList<Integer>();
 	 if(get_Connection()==null)
  	 {
  			System.out.println("���ݿ����Ӳ��ɹ�");
  			return neighbor_id;
  	 }
 	 Connection conn=get_Connection();
 	 Statement ps;
		try {
			 ps = conn.createStatement();
	    	 ResultSet  rs=ps.executeQuery("SELECT * FROM section_map where section_id="+section_id);
	    	 while(rs.next())
	    	 {
	    		 int neighbor_1=rs.getInt(6);
	    		 int neighbor_2=rs.getInt(7);
	    		 int neighbor_3=rs.getInt(8);
	    		 int neighbor_4=rs.getInt(9);
	    		 int neighbor_5=rs.getInt(10);
	    		 int neighbor_6=rs.getInt(11);
	    		 int neighbor_7=rs.getInt(12);
	    		 int neighbor_8=rs.getInt(13);
	    		 neighbor_id.add(neighbor_1);
	    		 neighbor_id.add(neighbor_2);
	    		 neighbor_id.add(neighbor_3);
	    		 neighbor_id.add(neighbor_4);
	    		 neighbor_id.add(neighbor_5);
	    		 neighbor_id.add(neighbor_6);
	    		 neighbor_id.add(neighbor_7);
	    		 neighbor_id.add(neighbor_8);

	    	 }
	         rs.close();
	         return neighbor_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return neighbor_id;
		}
  }
    /********************************************************/
  //////////ɾ��temp����sample����id�е�account������
  public int Delete_all_from_temp_wifi_sample(String account)
  {
	  int order=1;
	  if(get_Connection()==null)
	  	 {
	  			System.out.println("���ݿ����Ӳ��ɹ�");
	  			return order;
	  	 }
	 	 Connection conn=get_Connection();
	 	 Statement ps;
			try {
				 ps = conn.createStatement();
				 int  rs=ps.executeUpdate("DELECT * FROM temp_wifi_sample where account_id="+account);
		    	 
		         order=0;
		         return order;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("���ݿ����ʧ��:"+e);
				return order;
			}
  }
////////////////////////////////////
////////**************************����cluster��Ϣ************************/////////////     
	public int insert_into_cluster_centre(int current_sample_id,int map_id)///����wifi_info
	{
		if(get_Connection()==null)
		{
				System.out.println("���ݿ����Ӳ��ɹ�");
				return 2;
		}
		Connection conn=get_Connection();
		String  sqlparse_wifi = "insert into cluster_centre(cluster_id,sample_id,para1,para2,para3,para4,para5,para6,para7,para8,para9,para10) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		 PreparedStatement ps_wifi;
		try {
			 ps_wifi = conn.prepareStatement(sqlparse_wifi);
			 ps_wifi.setNull(1, Types.INTEGER);
			 ps_wifi.setInt(2,current_sample_id);
			 ps_wifi.setDouble(3,(double)map_id);
			 ps_wifi.setDouble(4,0);
			 ps_wifi.setDouble(5,0);
			 ps_wifi.setDouble(6,0);
			 ps_wifi.setDouble(7,0);
			 ps_wifi.setDouble(8,0);
			 ps_wifi.setDouble(9,0);
			 ps_wifi.setDouble(10,0);
			 ps_wifi.setDouble(11,0);
			 ps_wifi.setDouble(12,0);
			 ps_wifi.executeUpdate();
			 ResultSet  rs=ps_wifi.executeQuery("SELECT max(cluster_id) FROM cluster_centre");
	         int cluster_id=-1;
	         if (rs.next()) {
	        	 cluster_id = rs.getInt(1);
	            } else {
	             // throw an exception from here
	            }
	         rs.close();
			 return cluster_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return -1;
		}
	}///insert_into_cluster_wifi_info
////////**************************����cluster����Ϣ************************/////////////   
	public int insert_into_cluster_array(int cluster_id,int sample_id)///����wifi_info
	{
		if(get_Connection()==null)
		{
				System.out.println("���ݿ����Ӳ��ɹ�");
				return 2;
		}
		Connection conn=get_Connection();
		String  sqlparse_wifi = "insert into cluster_array(self_id,cluster_id,sample_id) values(?,?,?)";
		 PreparedStatement ps_wifi;
		try {
			 ps_wifi = conn.prepareStatement(sqlparse_wifi);
			 ps_wifi.setNull(1, Types.INTEGER);
			 ps_wifi.setInt(2,cluster_id);
			 ps_wifi.setInt(3,sample_id);
			 ps_wifi.executeUpdate();
			 return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return -1;
		}
	}///insert_into_cluster_array
////////**************************����cluster���ĵ�wifi��Ϣ����Ϣ************************/////////////   
	public int insert_into_cluster_centre_wifi_info(int cluster_id,String name,int level,double para1,double para2,double para3,double para4)///����wifi_info
		{
		if(get_Connection()==null)
		{
			System.out.println("���ݿ����Ӳ��ɹ�");
			return 2;
		}
		Connection conn=get_Connection();
		String  sqlparse_wifi = "insert into cluster_centre_wifi_info(cluster_wifi_info,cluster_id,name,level,para1,para2,para3,para4) values(?,?,?,?,?,?,?,?)";
		PreparedStatement ps_wifi;
		try {
		 ps_wifi = conn.prepareStatement(sqlparse_wifi);
		 ps_wifi.setNull(1, Types.INTEGER);
		 ps_wifi.setInt(2,cluster_id);
		 ps_wifi.setString(3,name);
		 ps_wifi.setInt(4,level);
		 ps_wifi.setDouble(5,para1);
		 ps_wifi.setDouble(6,para2);
		 ps_wifi.setDouble(7,para3);
		 ps_wifi.setDouble(8,para4);
		 ps_wifi.executeUpdate();
		 return 0;
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("���ݿ����ʧ��:"+e);
		return -1;
		}
	}///insert_into_cluster_centre_wifi_info
////////**************************��ȡ���ݿ���cluster_id��wifi��������Ӧ������ǿ������Ϣ************************/////////////  
 public HashMap<String ,Integer> get_cluster_centre_wifi_info(int cluster_id)///"SELECT * FROM wifi_info where sample_id="+id
 {
	 HashMap<String ,Integer> temp_map = new HashMap<String, Integer>();
	 if(get_Connection()==null)
 	 {
 			System.out.println("���ݿ����Ӳ��ɹ�");
 			return temp_map;
 	 }
	 Connection conn=get_Connection();
	try {
		 Statement  ps1 = conn.createStatement();
    	 ResultSet  rs1=ps1.executeQuery("SELECT * FROM cluster_centre_wifi_info where cluster_id="+cluster_id);
    	 while(rs1.next())
    	 {
    		 temp_map.put(rs1.getString("name"), rs1.getInt("level"));
    	 }
    	 rs1.close();
    	 return temp_map;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("���ݿ����ʧ��:"+e);
		return temp_map;
	}
 }///get_cluster_centre_wifi_info
 public HashMap<String ,Double []> get_cluster_centre_wifi_info_delt(int cluster_id)///"SELECT * FROM wifi_info where sample_id="+id
 {
	 HashMap<String ,Double []> temp_map = new HashMap<String, Double[]>();
	 if(get_Connection()==null)
 	 {
 			System.out.println("���ݿ����Ӳ��ɹ�");
 			return temp_map;
 	 }
	 Connection conn=get_Connection();
	try {
		 Statement  ps1 = conn.createStatement();
    	 ResultSet  rs1=ps1.executeQuery("SELECT * FROM cluster_centre_wifi_info where cluster_id="+cluster_id);
    	 while(rs1.next())
    	 {
    		 Double [] value= new Double[2];
    		 value[0]=(double)rs1.getInt("level");
    		 value[1]=rs1.getDouble("para1");
    		 temp_map.put(rs1.getString("name"), value);
    	 }
    	 rs1.close();
    	 return temp_map;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("���ݿ����ʧ��:"+e);
		return temp_map;
	}
 }///get_cluster_centre_wifi_info
 public HashMap<String ,Double []> get_wifi_info_delt(int sample_id)///"SELECT * FROM wifi_info where sample_id="+id
 {
	 HashMap<String ,Double []> temp_map = new HashMap<String, Double[]>();
	 if(get_Connection()==null)
 	 {
 			System.out.println("���ݿ����Ӳ��ɹ�");
 			return temp_map;
 	 }
	 Connection conn=get_Connection();
	try {
		 Statement  ps1 = conn.createStatement();
    	 ResultSet  rs1=ps1.executeQuery("SELECT * FROM wifi_info where sample_id="+sample_id);
    	 while(rs1.next())
    	 {
    		 Double [] value= new Double[2];
    		 value[0]=(double)rs1.getInt("level");
    		 value[1]=rs1.getDouble("delat");
    		 temp_map.put(rs1.getString("ssid"), value);
    	 }
    	 rs1.close();
    	 return temp_map;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("���ݿ����ʧ��:"+e);
		return temp_map;
	}
 }///get_cluster_centre_wifi_info
 ///////////////////////////////////��ȡ����cluster��ID//
    public List<Integer> get_ALL_cluster_ID()//��ȡ���в�����ID
    {
   	 List<Integer> cluster_id_list=new ArrayList<Integer>();
   	 if(get_Connection()==null)
    	 {
    			System.out.println("���ݿ����Ӳ��ɹ�");
    			return cluster_id_list;
    	 }
   	 Connection conn=get_Connection();
   	 Statement ps;
		try {
			 ps = conn.createStatement();
	    	 ResultSet  rs=ps.executeQuery("SELECT * FROM cluster_centre");
	    	 while(rs.next())
	    	 {
	    		 int cluster_id=rs.getInt(1);
	    		 cluster_id_list.add(cluster_id);
	    	 }
	         rs.close();
	         return cluster_id_list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return cluster_id_list;
		}
    }/////get_ALL_wifi_sample
    ///////////////////////////////////��ȡ����cluster��ID//
    public int get_sample_id_from_cluster_id(int cluster_id)//��ȡ���в�����ID
    {
   	 int sample_id=-1;
   	 if(get_Connection()==null)
    	 {
    			System.out.println("���ݿ����Ӳ��ɹ�");
    			return sample_id;
    	 }
   	 Connection conn=get_Connection();
   	 Statement ps;
		try {
			 ps = conn.createStatement();
	    	 ResultSet  rs=ps.executeQuery("SELECT * FROM cluster_centre where cluster_id="+cluster_id);
	    	  if (rs.next()) {
	    		  sample_id = rs.getInt(2);
		            } else {
		             // throw an exception from here
		            }
		     
	         rs.close();
	         return sample_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return sample_id;
		}
    }/////get_ALL_wifi_sample
///////////////////////////////////��ȡĳһ��cluster��ID//
	public List<Integer> get_ALL_cluster_ID(int map_id)//��ȡ���в�����ID
	{
		List<Integer> cluster_id_list=new ArrayList<Integer>();
		if(get_Connection()==null)
		{
			System.out.println("���ݿ����Ӳ��ɹ�");
			return cluster_id_list;
		}
		Connection conn=get_Connection();
		Statement ps;
		try {
			ps = conn.createStatement();
			ResultSet  rs=ps.executeQuery("SELECT * FROM cluster_centre where para1="+map_id);
			while(rs.next())
			{
			int cluster_id=rs.getInt(1);
			cluster_id_list.add(cluster_id);
			}
			rs.close();
			return cluster_id_list;
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return cluster_id_list;
		}
	}/////get_ALL_wifi_sample
////////**************************��ȡ���ݿ���cluster_id��Ӧ��sample_id************************/////////////    
 public int get_map_ID_from_cluster_centre(int cluster_id)///SELECT * FROM wifi_sample where sample_id="+map_id_temp
 {
	 int map_id=-1;
	 if(get_Connection()==null)
 	 {
 			System.out.println("���ݿ����Ӳ��ɹ�");
 			return map_id;
 	 }
	 Connection conn=get_Connection();
	 Statement ps_map;
	try {
		 ps_map = conn.createStatement();
    	 ResultSet  rs_map=ps_map.executeQuery("SELECT * FROM cluster_centre where cluster_id="+cluster_id);
    	 if(rs_map.next())
    	 {
    		 double temp=rs_map.getDouble("para1");
    		 map_id=(int) temp;
    	 }
    	 rs_map.close();
    	 return map_id;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("���ݿ����ʧ��:"+e);
		return map_id;
	}
 }//get_wifi_sample
 ///////��ȡcluster�� array
 public List<Integer> get_ALL_cluster_array(int cluster_id)//��ȡ���в�����ID
 {
	 List<Integer> cluster_id_list=new ArrayList<Integer>();
	 if(get_Connection()==null)
		{
			System.out.println("���ݿ����Ӳ��ɹ�");
			return cluster_id_list;
		}
		Connection conn=get_Connection();
		Statement ps;
		try {
			ps = conn.createStatement();
			ResultSet  rs=ps.executeQuery("SELECT * FROM cluster_array where cluster_id="+cluster_id);
			while(rs.next())
			{
			int sample_id=rs.getInt(3);
			cluster_id_list.add(sample_id);
			}
			rs.close();
			return cluster_id_list;
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return cluster_id_list;
		}
 }
 ////��ȡ���ڳ�Ա�Ĺؼ�ֵ
 public List<Integer> get_ALL_KEY_cluster_array(int cluster_id)//��ȡ���в�����ID
 {
	 List<Integer> cluster_id_list=new ArrayList<Integer>();
	 if(get_Connection()==null)
		{
			System.out.println("���ݿ����Ӳ��ɹ�");
			return cluster_id_list;
		}
		Connection conn=get_Connection();
		Statement ps;
		try {
			ps = conn.createStatement();
			ResultSet  rs=ps.executeQuery("SELECT * FROM cluster_array where cluster_id="+cluster_id);
			while(rs.next())
			{
			int self_id=rs.getInt(1);
			cluster_id_list.add(self_id);
			}
			rs.close();
			return cluster_id_list;
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return cluster_id_list;
		}
 }
 /////��ȡcluster���ڵ�sample_id
 public int get_sample_id_cluster_array(int key)
 {
	 int sample_id=-1;
	 if(get_Connection()==null)
		{
			System.out.println("���ݿ����Ӳ��ɹ�");
			return sample_id;
		}
		Connection conn=get_Connection();
		Statement ps;
		try {
			ps = conn.createStatement();
			ResultSet  rs=ps.executeQuery("SELECT * FROM cluster_array where self_id="+key);
			while(rs.next())
			{
				sample_id=rs.getInt(3);
			
			}
			rs.close();
			return sample_id;
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return sample_id;
		}
 }
 //////////��ȡsampleId��clusterID
 public int get_cluster_ID_from_array(int key)
 {
	 int cluster_id=-1;
	 if(get_Connection()==null)
		{
			System.out.println("���ݿ����Ӳ��ɹ�");
			return cluster_id;
		}
		Connection conn=get_Connection();
		Statement ps;
		try {
			ps = conn.createStatement();
			ResultSet  rs=ps.executeQuery("SELECT * FROM cluster_array where self_id="+key);
			while(rs.next())
			{
			 cluster_id=rs.getInt(2);
			
			}
			rs.close();
			return cluster_id;
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return cluster_id;
		}
 }
	/////////////ɾ������cluster
	public int Delete_ALL_Cluster()
	{
		if(get_Connection()==null)
		{
			System.out.println("���ݿ����Ӳ��ɹ�");
			return 2;
		}
		Connection conn=get_Connection();
	 	 Statement ps;
		try {
			 ps = conn.createStatement();
	    	 int  rs=ps.executeUpdate("DELETE   FROM cluster_centre");
	    	 rs=ps.executeUpdate("DELETE   FROM cluster_array");
	    	 rs=ps.executeUpdate("DELETE   FROM cluster_centre_wifi_info");
		 return 0;
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("���ݿ����ʧ��:"+e);
		return -1;
	}
}///insert_into_cluster_centre_wifi_info
	public int insert_into_wifi_sample_ml(int x, int y, int width, int height,
			int section_id, int map_id) {
		// TODO Auto-generated method stub
		

   	 if(get_Connection()==null)
  		{
  			System.out.println("���ݿ����Ӳ��ɹ�");
  			return -1;
  		}
    	 Connection conn=get_Connection();
   	 String  sqlparse = "insert into ml_wifi_sample(ml_sample_id,x,y,width,height,section_id,ml_map_id) values(?,?,?,?,?,?,?)";
 		 PreparedStatement ps;
		try {
			 ps = conn.prepareStatement(sqlparse);
	  		 ps.setNull(1, Types.INTEGER);
	         ps.setInt(2,x);
	         ps.setInt(3,y);
	         ps.setInt(4,width);
	         ps.setInt(5,height);
	         ps.setInt(6, section_id);
	         ps.setInt(7, map_id);
	         ps.executeUpdate();
	         ResultSet  rs=ps.executeQuery("SELECT max(ml_sample_id) FROM ml_wifi_sample");
	         int current_sample_id=0;
	         if (rs.next()) {
	        	 current_sample_id = rs.getInt(1);
	            } else {
	             // throw an exception from here
	            }
	         rs.close();
	         return current_sample_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return -1;
		}

	}
	public int insert_into_wifi_info_ml(int current_sample_id, String str_wifi_name,
			double wifi_value, int i) {
		// TODO Auto-generated method stub

	 	if(get_Connection()==null)
	 	{
	 			System.out.println("���ݿ����Ӳ��ɹ�");
	 			return 2;
	 	}
	 	Connection conn=get_Connection();
	 	String  sqlparse_wifi = "insert into ml_wifi_info(ml_wifi_info_id,ml_sample_id,ssid,level,delat) values(?,?,?,?,?)";
			 PreparedStatement ps_wifi;
			try {
				 ps_wifi = conn.prepareStatement(sqlparse_wifi);
		 		 ps_wifi.setNull(1, Types.INTEGER);
		 		 ps_wifi.setInt(2,current_sample_id);
		 		 ps_wifi.setString(3,str_wifi_name);
		 		 ps_wifi.setInt(4,(int)wifi_value);
		 		ps_wifi.setInt(5, i);
		 		 ps_wifi.executeUpdate();
		 		 return 0;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("���ݿ����ʧ��:"+e);
				return 2;
			}
	  
		
	}
	public int get_sample_section(Integer x, Integer y) {
		// TODO ͨ������Ѱ��xy���ڵ�����

   	  int section =0;
   	 if(get_Connection()==null)
    	 {
    			System.out.println("���ݿ����Ӳ��ɹ�");
    			return section;
    	 }
   	 Connection conn=get_Connection();
		try {
			 Statement  ps1 = conn.createStatement();
       	 ResultSet  rs1=ps1.executeQuery("SELECT * FROM ml_wifi_sample where x="+x+" and y="+y);
       	 while(rs1.next())
       	 {
       		section=rs1.getInt("section_id");
       	 }
       	 rs1.close();
       	 return section;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ����ʧ��:"+e);
			return section;
		}
    
	}
	public ArrayList<String> get_section_xy(Integer section) {
		// TODO ͨ�������Ѽ����������еĲο���xy
		ArrayList<String> xy=new ArrayList<String>();
	    if(get_Connection()==null)
		 {
		    System.out.println("���ݿ����Ӳ��ɹ�");
		    return xy;
		   }
		Connection conn=get_Connection();
		try {
			 Statement  ps1 = conn.createStatement();
		     ResultSet  rs1=ps1.executeQuery("SELECT * FROM ml_wifi_sample where section_id="+section);
		      while(rs1.next())
		       	 {
		       		int x=rs1.getInt("x");
		       		int y=rs1.getInt("y");
		       		String XY=String.valueOf(x)+" "+String.valueOf(y);
		       		xy.add(XY);
		       	 }
		       	 rs1.close();
		       	 return xy;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("���ݿ����ʧ��:"+e);
					return xy;
				}
	}
	public ArrayList<Integer> get_ref_sample_id(int x, int y) {
		// TODO ͨ�������ҵ��ο����sample_id
		ArrayList<Integer> SampleId=new ArrayList<Integer>();
		if(get_Connection()==null)
		 {
		    System.out.println("���ݿ����Ӳ��ɹ�");
		    return SampleId;
		   }
		Connection conn=get_Connection();
		try {
			 Statement  ps1 = conn.createStatement();
		     ResultSet  rs1=ps1.executeQuery("SELECT * FROM wifi_sample where x="+x+" and y="+y);
		      while(rs1.next())
		       	 {
		       		int id=rs1.getInt("sample_id");	
		       		SampleId.add(id);
		       	 }
		       	 rs1.close();
		       	 return SampleId;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("���ݿ����ʧ��:"+e);
					return SampleId;
		
	}

	}
	public ArrayList<Integer> get_all_xy(int i) {
		// TODO Auto-generated method stub

		// TODO ͨ�������ҵ��ο����sample_id
		ArrayList<Integer> All_xy=new ArrayList<Integer>();
		if(get_Connection()==null)
		 {
		    System.out.println("���ݿ����Ӳ��ɹ�");
		    return All_xy;
		   }
		Connection conn=get_Connection();
		try {
			 Statement  ps1 = conn.createStatement();
		     ResultSet  rs1=ps1.executeQuery("SELECT * FROM wifi_sample where map_id="+i);
		      while(rs1.next())
		       	 {
		       		int x=rs1.getInt("x");	
		       		All_xy.add(x);
		       		int y=rs1.getInt("y");	
		       		All_xy.add(y);
		       	 }
		       	 rs1.close();
		       	 return All_xy;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("���ݿ����ʧ��:"+e);
					return All_xy;
		
	}

	
	}
	
	public int get_nadsfal(int i){
		ArrayList<Integer> neighbors=new ArrayList<Integer>();
		double x_base=0;
		double y_base=0;
		int real_width=4126;
		int real_height=2610;
		int pixl_width=1568;
		int pixl_height=988;
		if(get_Connection()==null)
		 {
		    System.out.println("���ݿ����Ӳ��ɹ�");
		    return 0;
		   }
		Connection conn=get_Connection();
		try {
			 Statement  ps1 = conn.createStatement();
		     ResultSet  rs1=ps1.executeQuery("SELECT * FROM wifi_sample where sample_id="+i);
		      while(rs1.next())
		       	 {
		    	  x_base=rs1.getInt("x")*real_width/pixl_width;	
		       	  y_base=rs1.getInt("y");
		       	neighbors.add((int) x_base);
		       	 }
		       	 rs1.close();
		} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("���ݿ����ʧ��:"+e);
	    }
		return (int) x_base;
	}
	
	public ArrayList<Integer> get_neighbors(int i){
		ArrayList<Integer> neighbors=new ArrayList<Integer>();
		int real_width=4126;
		int real_height=2610;
		int pixl_width=1568;
		int pixl_height=988;
		double x_base=0;
		double y_base=0;
		if(get_Connection()==null)
		 {
		    System.out.println("���ݿ����Ӳ��ɹ�");
		    return neighbors;
		   }
		Connection conn=get_Connection();
		try {
			 Statement  ps1 = conn.createStatement();
		     ResultSet  rs1=ps1.executeQuery("SELECT * FROM wifi_sample where sample_id="+i);
		      while(rs1.next())
		       	 {
		    	  x_base=rs1.getInt("x")*real_width/pixl_width;	
		       	  y_base=rs1.getInt("y")*real_height/pixl_height;	
		       	 }
		       	 rs1.close();
		} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("���ݿ����ʧ��:"+e);
	    }
		
		
		try {
			 Statement  ps2 = conn.createStatement();
		     ResultSet  rs2=ps2.executeQuery("SELECT * FROM wifi_sample");
		      while(rs2.next())
		       	 {
		    	 double x=rs2.getInt("x")*real_width/pixl_width;	
		       	 double y=rs2.getInt("y")*real_height/pixl_height;
		       	 int id=rs2.getInt("sample_id");
		       	 double distance=Math.sqrt(Math.pow(x_base-x, 2)+Math.pow(y_base-y, 2));
		       	 if(distance<305){
		       		neighbors.add(id);
		       	 }
		       	 }
		       	 rs2.close();
		} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("���ݿ����ʧ��:"+e);
	    }

		
		return neighbors;
		
	}
	public int insert_into_ap_impor(String wifi_name,double wifi_value) {
		// TODO Auto-generated method stub

	 	if(get_Connection()==null)
	 	{
	 			System.out.println("���ݿ����Ӳ��ɹ�");
	 			return 2;
	 	}
	 	Connection conn=get_Connection();
	 	String  sqlparse_wifi = "insert into ap_impor(ap_id,ap_name,ap_level) values(?,?,?)";
			 PreparedStatement ps_wifi;
			try {
				 ps_wifi = conn.prepareStatement(sqlparse_wifi);
		 		 ps_wifi.setNull(1, Types.INTEGER);
		 		 ps_wifi.setString(2,wifi_name);
		 		 ps_wifi.setDouble(3,wifi_value);
		 		 ps_wifi.executeUpdate();
		 		 return 0;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("���ݿ����ʧ��:"+e);
				return 2;
			}
	  
		
	}
	  public double get_weight(String wifi_name)
	     {   
		     double weight=0;
	    	 if(get_Connection()==null)
	     	 {
	     			System.out.println("������ݿ����Ӳ��ɹ�");
	     			return weight;
	     	 }
	    	 Connection conn=get_Connection();
			try {
				 Statement  ps1 = conn.createStatement();
	        	 ResultSet  rs1=ps1.executeQuery("SELECT * FROM ap_impor");
	        	 while(rs1.next())
	        	 {   String name=rs1.getString("ap_name");
	        		 if(name.equals(wifi_name)){
	        			 weight=rs1.getDouble("ap_level");
	        			 break;
	        		 }
	        	 }
	        	 rs1.close();
	        	 return weight;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("�������ݿ����ʧ��:"+e);
				return weight;
			}
	     }
	  public HashMap<String ,Integer> get_wifi_info_ap(int sample_id)///"SELECT * FROM wifi_info where sample_id="+id
	     {
	    	 HashMap<String ,Integer> temp_map = new HashMap<String, Integer>();
	    	 if(get_Connection()==null)
	     	 {
	     			System.out.println("���ݿ����Ӳ��ɹ�");
	     			return temp_map;
	     	 }
	    	 Connection conn=get_Connection();
			try {
				 Statement  ps1 = conn.createStatement();
	        	 ResultSet  rs1=ps1.executeQuery("SELECT * FROM wifi_info where sample_id="+sample_id);
	        	 while(rs1.next())
	        	 {
	        		
	        		 temp_map.put(rs1.getString("ssid")+"_"+rs1.getDouble("weight"), rs1.getInt("level"));
	        	 }
	        	 rs1.close();
	        	 return temp_map;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("���ݿ����ʧ��:"+e);
				return temp_map;
			}
	     }

}
