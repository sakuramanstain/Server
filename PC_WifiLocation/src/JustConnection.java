import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class JustConnection {

	public JustConnection() {
		// TODO Auto-generated constructor stub
		/**
		 * 本方法仅仅用于与服务器连接，不进行任何数据传送
		 */

		try{
			m_Typeconvert type=new m_Typeconvert(); 
			Socket socket=new Socket("127.0.0.1", 1818);//115.154.68.237
			 OutputStream out= new DataOutputStream(socket.getOutputStream());                   
	         InputStream  in = new DataInputStream(socket.getInputStream());  
	         byte[] ack=new byte[1];                                                    //连接服务器        
	         ack[0]=4;
	         out.write(ack[0]);
	         out.flush();  
	         
	         byte[] inread=new byte[1];
	         in.read(inread);
	         
	  //    if(in.read()==0){
	         if(inread[0]==0){
	        	   String name="zls";
	        	   String password="123";
	        	   byte[] user_name=name.getBytes();
	        	   byte[] user_password=password.getBytes();
	        	   out.write(user_name);
	        	   
	        	   in.read();
	        	   
	        	   out.write(user_password);
	        	   out.flush();
	        	   in.read();
	        }else{
	        	   out.write(4);
	        	   out.flush();
	           }
	         byte[] ack1=new byte[1];
	         ack1[0]=8;
	         out.write(ack1[0]);
	         out.flush();
	         
	         byte[] inread2=new byte[1];
	         in.read(inread2);
 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
