import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class Connection {
	public Connection(Map<String, String> Result) {
		// TODO Auto-generated constructor stub	
		try {
			m_Typeconvert type=new m_Typeconvert(); 
			Socket socket=new Socket("90.0.0.19", 1818);
			 OutputStream out= new DataOutputStream(socket.getOutputStream());                   
	         InputStream  in = new DataInputStream(socket.getInputStream());  
	         byte[] ack=new byte[1];                                                    //连接服务器        
	         ack[0]=4;
	         out.write(ack[0]);
	         out.flush();  
	         if(in.read()==0){
	        	   String name="zls";
	        	   String password="123";
	        	   byte[] user_name=name.getBytes();
	        	   byte[] user_password=password.getBytes();
	        	   out.write(user_name);
	        	   out.write(user_password);
	        	   out.flush();
	        	   in.read();
	         }else{
	        	   out.write(4);
	        	   out.flush();
	           }
	         byte[] ack1=new byte[1];
	         ack1[0]=3;
	         out.write(ack1[0]);
	         out.flush();
	         if(in.read()==0){
	        	 out.write(Result.size()) ;                                              
	             out.flush();
	             if(in.read()==0){
	            	 if(Result!=null) {
	            		 for(Map.Entry<String, String>entry: Result.entrySet()) {                        		
	            			 double Level= Double.parseDouble(entry.getValue().toString())  ;    
	            			 byte[] ab=type.getBytes(Level);
	            			 out.write(ab);
	            			 in.read();
	            			 byte[] BSSID= entry.getKey().getBytes();                    	             
	            			 out.write(BSSID); 
	            			 in.read();
	            			 out.flush();
	            		 }  
	            		 byte[] a=new byte[1];                                      //获取地图名称
		            	 in.read(a);
		            	 byte[] str_map_name=new byte[1024];
		                 in.read(str_map_name);                	
		                 char[] imageName1=type.getChars(str_map_name);
		                 int i=0;
		                 while(imageName1[i]!=0) i++;                	 
		                 String imageName= new String(imageName1,0,i);
		                 System.out.println("接收图片："+imageName);
		                 out.write(1);  
		                 out.flush();
	            	 }
	             }
	        	 
	         }
	         out.write(5);
	         out.close();          
	         in.close();
	         socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*******************重载********************************/
	public Connection(Map<String, String> Result,int Count){

		// TODO Auto-generated constructor stub	
		try {
			m_Typeconvert type=new m_Typeconvert(); 
			Socket socket=new Socket("90.0.0.19", 1818);
			 OutputStream out= new DataOutputStream(socket.getOutputStream());                   
	         InputStream  in = new DataInputStream(socket.getInputStream());  
	         byte[] ack=new byte[1];                                                    //连接服务器        
	         ack[0]=4;
	         out.write(ack[0]);
	         out.flush();  
	         if(in.read()==0){
	        	   String name="zls";
	        	   String password="123";
	        	   byte[] user_name=name.getBytes();
	        	   byte[] user_password=password.getBytes();
	        	   out.write(user_name);
	        	   out.write(user_password);
	        	   out.flush();
	        	   in.read();
	         }else{
	        	   out.write(4);
	        	   out.flush();
	           }
	         byte[] ack1=new byte[1];
	         ack1[0]=7;
	         out.write(ack1[0]);
	         out.flush();
	         if(in.read()==0){
	        	 out.write(Count);
	        	 out.flush();
	        	 if(in.read()==0){
	        	    out.write(Result.size()) ;                                              
	                out.flush();
	              if(in.read()==0){
	            	 if(Result!=null) {
	            		 for(Map.Entry<String, String>entry: Result.entrySet()) {                        		
	            			 double Level= Double.parseDouble(entry.getValue().toString())  ;    
	            			 byte[] ab=type.getBytes(Level);
	            			 out.write(ab);
	            			 in.read();
	            			 byte[] BSSID= entry.getKey().getBytes();                    	             
	            			 out.write(BSSID); 
	            			 in.read();
	            			 out.flush();
	            		 }  
	            		 byte[] a=new byte[1];                                      //获取地图名称
		            	 in.read(a);
		            	 byte[] str_map_name=new byte[1024];
		                 in.read(str_map_name);                	
		                 char[] imageName1=type.getChars(str_map_name);
		                 int i=0;
		                 while(imageName1[i]!=0) i++;                	 
		                 String imageName= new String(imageName1,0,i);
		                 System.out.println("接收图片："+imageName);
		                 out.write(1);  
		                 out.flush();
	            	 }
	             }
	        	 }
	         }
	         out.write(5);
	         out.close();          
	         in.close();
	         socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

		
	}



