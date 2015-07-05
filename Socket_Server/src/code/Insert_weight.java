package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class Insert_weight {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
      DatabaseManager dbm=new DatabaseManager();
      Connection m_conn=null;
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
      Statement  ps_map = m_conn.createStatement();
 	  ResultSet  rs_map=ps_map.executeQuery("SELECT * FROM wifi_info ");
 	 while(rs_map.next())
 	 {
 		 String t=rs_map.getString("ssid");
 		 int number=rs_map.getInt("wifi_info_id");
 		 Statement  ps1 = m_conn.createStatement();
    	 ResultSet  rs1=ps1.executeQuery("SELECT * FROM ap_impor");
    	 double weigh=0;
    	 while(rs1.next())
    	 {   String name=rs1.getString("ap_name");
    		 if(name.equals(t)){
    			 weigh=rs1.getDouble("ap_level");
    			 break;
    		 }
    	 }
    	 rs1.close();
    	 PreparedStatement ps = m_conn.prepareStatement("update wifi_info set weight=? where wifi_info_id=?");
 		 ps.setDouble(1, weigh);
 		 ps.setInt(2, number);
 		 ps.executeUpdate();
 	 }
     rs_map.close();	    
      }
      
      
      
	}


