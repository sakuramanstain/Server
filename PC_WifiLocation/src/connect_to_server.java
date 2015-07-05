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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class connect_to_server {
  public connect_to_server(){
	try{
		m_Typeconvert type=new m_Typeconvert(); 
		Socket socket=new Socket("127.0.0.1", 1818);
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
         ack1[0]=7;
         out.write(ack1[0]);
         out.flush();
         
         byte[] inread2=new byte[1];
         in.read(inread2);
         
      if(inread2[0]==0){
        	     
         ArrayList<String> FileNameList=new ArrayList<String>();
 		 Map<String,String> Result_in=new HashMap<String,String>();
 		 ArrayList<String> Result_list=new ArrayList<String>();
 		//	String txtpath="C:\\Users\\wangpeng\\Desktop\\主EPCA\\主E定位测试\\x198y1315cy";
 			 //String txtpath="C:\\Users\\wangpeng\\Desktop\\主EPCA\\主E定位测试转换\\x378y1255cy";
 			 String txtpath="E:\\主E采样终极版\\主E采样终极版\\dingwei\\x198y1315cy";	
		 File file=new File(txtpath);
		 if(file.isDirectory()){
				File[] fileArray=file.listFiles();	
				for(File one_directory:fileArray){
					
						String file_name=one_directory.getName();
						String [] filename=file_name.split("x|y|e|n|s|w");
						System.out.println("文件夹名称:"+file_name);
					    File[] m_file=one_directory.listFiles();
						System.out.println("文件个数："+m_file.length);
						int count=m_file.length;
			//			   out.write(count);
			//		       out.flush(); 	
						
						String count2=String.valueOf(count);
						byte[] count_to=count2.getBytes();
						out.write(count_to);
				    	
				    	
				    	
				    	
			         				        				         
				       if(in.read()==0){
						 for(File temp_file:m_file){
						
							if(temp_file.getPath().endsWith(".txt")){
								BufferedReader in_txt = new BufferedReader(new FileReader(temp_file.getPath()));
								String con="";
								String tt="";
								String aa=" ";					
								while((con=in_txt.readLine())!=null)
								{
									tt+=con;
								}
							//			String [] info=tt.split("\\s+");
									String [] info=tt.split("\\s+");
								for(int i=0;i<info.length;i=i+2){
									Result_in.put(info[i], info[i+1]);
								}
								 out.write(Result_in.size()) ;                                              
					             out.flush();
					     		if(in.read()==0){
					     			if(Result_in!=null) {
					            		 for(Map.Entry<String, String>entry: Result_in.entrySet()) {                        		
					            			 double Level= Double.parseDouble(entry.getValue().toString())  ;    
					            			 byte[] ab=type.getBytes(Level);
					            			 out.write(ab);	
					            			 out.flush();
					                        
					            			 in.read();
					            			 byte[] BSSID= entry.getKey().getBytes();                    	             
					            			 out.write(BSSID);	
					            			 out.flush();
					            	    	 in.read();
					            			 
					            		 }  					            		
						                 
					            	 }
					     		}
								Result_in.clear();
								
							
								/*		
									for(int i=0;i<info.length;i=i+2){
										Result_list.add(info[i]);
										Result_list.add(info[i+1]);
									}
									 out.write(Result_list.size()/2) ;                                              
						             out.flush();
						             if(in.read()==0){
						            	 if(Result_list!=null) { 
						            		 for(int i=0;i<Result_list.size();i=i+2){
						            			 double Level=Double.valueOf(Result_list.get(i+1));
						            			 byte[] ab=type.getBytes(Level);
						            			 out.write(ab);	
						            			 in.read();
						            			 char[] bssid=Result_list.get(i).toCharArray();
						            			 byte[] BSSID= type.getBytes(bssid);
						            	//		 String bssid=Result_list.get(i);
						            	//		 byte[] BSSID=bssid.getBytes(Charset.forName("utf-8"));
						            			 out.write(BSSID);	
						            			 out.flush();
						            	    	 in.read();
						            		 }
						            	 }
						             }
										*/
								System.out.println("已成功上传文件："+temp_file.getPath());
							}
							in.read();
						}
					
				}
				}
				
		 }
		}
		 
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
