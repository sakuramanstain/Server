package code;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HandleDeal implements Runnable {
    private Socket socket;
    private  int  current_map_id;
    
    public HandleDeal(Socket socket){
        this.socket = socket;
    }
    public void run() {
    	
    	System.out.println("new thread");
        System.out.println("New connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
        try
        {
        	
        	int Threshold=-500;
        	DatabaseManager m_DatabaseManager=new DatabaseManager();
        	m_DatabaseManager.Initial();
        	Algorithm m_Algorithm=new KNN_ML();//KNN_ML() KNN() KNN_DistanceLearn  PAMClusterTrainNew KNN_basde_on_Clique() KNN_based_on_PAM() KNN()  k_NN();
        	Section_ap_importance section_ap_impotance=new Section_ap_importance();
        	Auto_section auto_section=new Auto_section();
        	Typeconvert m_Typeconvert=new Typeconvert();////��������ת��
        	PrintWriter out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));   
            DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));    
            out.flush();
            long time_sum=0;
            while(true)
            {
                //////////////////////////
                //////////////////////////
                byte[] order=new byte[1];
                 inputStream.read(order);
                 char ack=0;
            	 out.write(ack);
            	 out.flush();
            	 if(order[0]==4)///��½f
            	 {
            		 System.out.println("�û���½");
                	 byte[] str_user_name=new byte[1024];
                	 inputStream.read(str_user_name);
                	 
                	 char[] str_user_name1=m_Typeconvert.getChars(str_user_name);
                	 int i=0;
                	 while(str_user_name1[i]!=0) i++;
                	 String user_name= new String(str_user_name1,0,i);
                	 
               
                	 out.write(ack);
                	 out.flush();
                	 
                	 byte[] str_user_password=new byte[1024];
                	 inputStream.read(str_user_password);
                	 char[] str_user_password1=m_Typeconvert.getChars(str_user_password);
                	  i=0;
                	 while(str_user_password1[i]!=0) i++;
                	 String user_password= new String(str_user_password1,0,i);
                	 
                	 int flag=m_DatabaseManager.check_user_password(user_name, user_password);
                	 switch(flag)
                	 {
                		 case 0:
                			 ack=0;
                         	 out.write(ack);
                         	 out.flush();  
                             System.out.println(user_name+":"+socket.getInetAddress() + ":" + socket.getPort()+"��½�ɹ�");
                			 break;
                		 case 1:
                			 ack=1;
                          	 out.write(ack);
                          	 out.flush();  
                           	 out.close();   
                             socket.close();
                             System.out.println("��½ʧ��");
                             return;
                			
                		 default:
                			 ack=2;
                          	 out.write(ack);
                          	 out.flush();  
                           	 out.close();   
                             socket.close();
                			 System.out.println("���ݿ����Ӳ��ɹ�");	
                			 return;
                	 }
                	
            	 }
            	 if(order[0]==5)///�˳�
            	 {
            		 System.out.println(socket.getInetAddress() + ":" + socket.getPort()+"�˳�");
            		 
            		 out.close();   
            		 outputStream.close();
                     socket.close();
                     return;
            	 }
            	
            	 if(order[0]==6)///ѵ��
            	 {
            		 System.out.println("ѵ����");
            		 m_Algorithm.train(m_DatabaseManager);
            		 System.out.println("ѵ������");
            		 ack=0;
                	 out.write(ack);
                	 out.flush();
            		
                     
                    
            	 }
            	 
                 if(order[0]==0)//�ϴ�ͼƬ
                 {
                	 /////////////////////////////////
                	 ///////����map����
                	 System.out.println("����map");
                	 byte[] str_map_name=new byte[1024];
                	 inputStream.read(str_map_name);
                	
                	 char[] imageName1=m_Typeconvert.getChars(str_map_name);
                	 int i=0;
                	 while(imageName1[i]!=0) i++;
                	 ack=0;
                	 out.write(ack);
                	 out.flush();
                	 String imageName= new String(imageName1,0,i);
                	 
                	 ///////����map����
                	 byte[] buffer = new byte[2*1024];   
                     int len = -1;   
                     FileOutputStream fos = null;   
                     File file = null;   
                     file = new File(TransferServer.PATH + imageName);   
                     if(!file.exists()) {   
                     file.createNewFile();   
                    } else if(file.isFile()) {   
                    file.delete();     
                    file.createNewFile();   
                    }   
                     len = -1;   
                     fos = new FileOutputStream(file);   
                     byte[] str_map_filesize=new byte[8];
                	 inputStream.read(str_map_filesize);
                	 long file_size=m_Typeconvert.getLong(str_map_filesize);
                	 
                	 
                	 
                	 long total=0;
                     while(true) {   
                     len = inputStream.read(buffer) ;
                     fos.write(buffer, 0, len);   
                     total=total+len;
                     if(total>=file_size)
                    	 break;
                     }   
                    
                     fos.flush();   
                     fos.close();  
                    // out.close(); 
                     out.flush();
                     
                    // socket.close();
                  System.out.println("�ļ�: �������!");
                    ///////////////////////////////////////
                  int flag=m_DatabaseManager.InsertMaptoDataBase(imageName);
                  switch(flag)
                      {
                      case 0:
                    	  System.out.println("�ɹ�����map");
                    	  break;
                      case 1:
                    	  System.out.println("��ͼ����");
                    	  break;
                      default:
                    	  System.out.println("���ݿ����Ӳ��ɹ�");	  
                      }
               
                 }
                ///////////////
                 if(order[0]==1)//�ϴ�һ���������wifi����
                 {
                	 ///////����map����
                	 System.out.println("����һ���������wifi����");
                	 byte[] str_map_name=new byte[1024];
                	 inputStream.read(str_map_name);
                	 char[] imageName1=m_Typeconvert.getChars(str_map_name);
                	
                	 int i=0;
                	 while(imageName1[i]!=0) i++;
                	 ack=0;
                	 out.write(ack);
                	 out.flush();
                	 String imageName= new String(imageName1,0,i);
                	 System.out.println(imageName);
                	 ///////����map���
                	 byte[] str_map_width=new byte[4];
                	 inputStream.read(str_map_width);
                	 int width=m_Typeconvert.byteToInt(str_map_width);
                	 ack=0;
                	 out.write(ack);
                	 out.flush();
                	 System.out.println("width:"+width);
                	 ///////����map�߶�
                	 byte[] str_map_height=new byte[4];
                	 inputStream.read(str_map_height);
                	 int height=m_Typeconvert.byteToInt(str_map_height);
                	 ack=0;
                	 out.write(ack);
                	 out.flush();	 
                	 System.out.println("height:"+height);
                	 ///////����x����
                	 
                	 byte[] x_byte=new byte[4];
                	 inputStream.read(x_byte);
                	 int x=m_Typeconvert.byteToInt(x_byte);
                	 ack=0;
                	 out.write(ack);
                	 out.flush();
                	 System.out.println("����x����:"+x);
                	 
                	 ///////����y����
                	 byte[] y_byte=new byte[4];
                	 inputStream.read(y_byte);
                	 int y=m_Typeconvert.byteToInt(y_byte);
                	 System.out.println("����y����:"+y);
                	 ack=0;
                	 out.write(ack);
                	 out.flush(); 
                	 //////����map ID
                	 current_map_id=m_DatabaseManager.get_map_id_in_map(imageName);
                	 if(current_map_id==-1)
                	 {
                		
                		 ack=1;
                    	 out.write(ack);
                    	 out.flush();
                    	 System.out.println("ack:"+1);
                	 }else
                	 {
                		 int current_sample_id=m_DatabaseManager.insert_into_wifi_sample(x,y,width,height,current_map_id);
                     	
                    	 if(current_sample_id==-1)
                    	 {
                    		 
                    		 ack=1;
                        	 out.write(ack);
                        	 out.flush();
                    	 }else
                    	 {
                    		 ack=0;
                        	 out.write(ack);
                        	 out.flush();
                        	 System.out.println("���ݿ�������-�ɹ�����wifi_sample");
                        	 ////////����wifi���ݱ�����
                        	 byte[] wifi_size=new byte[1];
                        	 inputStream.read(wifi_size);
                        	 ack=0;
                        	 out.write(ack);
                        	 out.flush(); 
                        	 
                        	 /////////����wifi�б�
                        	 //int count_num=0;
                        	 System.out.println("��ʼ����wifi_info");
                        	 while(wifi_size[0]>0)
                        	 {
                        		 ////////////����wifi����ǿ��
                        		 byte[] wifi_byte=new byte[8];
                            	 inputStream.read(wifi_byte);
                            	 double wifi_value=m_Typeconvert.byteToDouble(wifi_byte);
                            	 ack=0;
                            	 out.write(ack);
                            	 out.flush(); 
                            	 ///////////����wifi������
                            	 byte[] str_wifi_name2=new byte[1024];
                            	 inputStream.read(str_wifi_name2);
                            	 char[] str_wifi_name1=m_Typeconvert.getChars(str_wifi_name2);
                            	 int j=0;
                            	 while(str_wifi_name1[j]!=0) j++;
                            	
                            	 String str_wifi_name= new String(str_wifi_name1,0,j);
                            	 int flag=0;
                        		 if(wifi_value>Threshold)
                        		 {
                        			 flag=m_DatabaseManager.insert_into_wifi_info(current_sample_id,str_wifi_name,wifi_value);
                        			 System.out.print(str_wifi_name+"��������");
                        			 
                        		 }
                        		 if(flag!=0)
                    			 {
                    				 //TODO
                    			 }
                        		 //count_num++;
                        		 ack=0;
                            	 out.write(ack);
                            	 out.flush();
                            	 
                        		 wifi_size[0]--;
                          	 ///////////////////
                        	 }////wifi_size[0]>0
                    		 
                    	 }///current_sample_id
                        
                	 }///current_map_id!=-1
                	
                	
                	 System.out.println();
                     System.out.println("WIFI�б�������-����һ���������wifi����");
                 }
                 
                 if(order[0]==2)///���ص�ͼ
                 {
                ////*****************************����wifi����*******************************************///	
                	 System.out.println("��λ");
                	 List<String> wifi_ssid=new ArrayList<String>();
                	 byte[] wifi_size=new byte[1];
                	 inputStream.read(wifi_size);
                	 ack=0;
                	 out.write(ack);
                	 out.flush(); 
                	 //////////wifi�б� 
                	 Map<String,Double> wifi_up=new HashMap<String,Double>();
                 	 while(wifi_size[0]>0)
                	 {
                 		 ////////////����wifi����ǿ��
                		 byte[] wifi_byte=new byte[8];
                    	 inputStream.read(wifi_byte);
                    	 double wifi_value=m_Typeconvert.byteToDouble(wifi_byte);
                    	 ack=0;
                    	 out.write(ack);
                    	 out.flush(); 
                    	 ///////////����wifi������
                    	 byte[] str_wifi_name2=new byte[1024];
                    	 inputStream.read(str_wifi_name2);
                    	 char[] str_wifi_name1=m_Typeconvert.getChars(str_wifi_name2);
                    	 int j=0;
                    	 while(str_wifi_name1[j]!=0) j++;
                    	
                    	 String str_wifi_name= new String(str_wifi_name1,0,j);
                  
                		 if(wifi_value>Threshold)
                		 {
                			 wifi_up.put(str_wifi_name, wifi_value);
                			 wifi_ssid.add(str_wifi_name);
                		 }
                		 ack=0;
                    	 out.write(ack);
                    	 out.flush();
                		 wifi_size[0]--;
                  	 
                	 }
                 	System.out.println("wifi����ǿ��");
		         
                 	 String map_name=null;
                 	long a=System.currentTimeMillis();
                ////*****************************Ѱ�ҵ�ͼ*******************************************///
                 	int sample_id=m_Algorithm.find_map_id(wifi_ssid, wifi_up, m_DatabaseManager);
                 	
                 	
                ////**********************���ص�ͼ**********************************************/// 	
                     String imageName=null;
                     int map_id=m_DatabaseManager.get_map_ID_from_wifi_sample(sample_id);
                     if(map_id==-1)
                     {
                    	 //TODO
                    	 System.out.println("-1");
                     }
                     imageName=m_DatabaseManager. get_map_name_from_map(map_id);
                	 System.out.println("ִ�к�ʱ : "+(System.currentTimeMillis()-a)/1000f+" �� ");
                	 if( imageName=="")
                	 {
                		 System.out.println("ʧ�� ");
                		 ack=1;//��������ź�
    	               	 out.write(ack);
    	               	 out.flush();
    	               	
                	 }else
                	 {
                		 ack=0;//��������ź�
    	               	 out.write(ack);
    	               	 out.flush();
    	               	 ////////////////�����ļ�����
                    	 map_name=imageName;
    	               	 byte[] map_name_byte=map_name.getBytes();
    	               	outputStream.write(map_name_byte);
    	               	outputStream.flush();
    	               	//////// 
        	               byte[] ack_map=new byte[1];
        	               inputStream.read(ack_map);
        	               if(ack_map[0]==1)/////�������� ����
        	               {
        	            	 
        	                   System.out.println("��������Map");
        	               }
        	               if(ack_map[0]==0)////��Ҫ����
        	               {
        	            	   File file = null;   
        	            	   long fileLength = 0;     
        	                   file = new File(TransferServer.PATH+map_name);    
        	            	   FileInputStream fins=null;
        	            	   fins=new FileInputStream(file);
        	            	   byte[] buffer = new byte[2*1024];   
        	            	   fileLength=file.length();
        	            	   /////////////////////�����ļ�����
        	            	   byte[] filelength_byte=new byte[8];
        	            	   m_Typeconvert.long2Byte(filelength_byte,fileLength);
        	            	   outputStream.write(filelength_byte);
        		               outputStream.flush();
        	            	   /////�����ļ�
        	            	   int len = -1;   
        	            	   while((len = fins.read(buffer) )> 0) {  
        	            		   outputStream.write(buffer, 0, len);
        	            		   outputStream.flush();
        	                       } 
        	            	   fins.close();
        	                   System.out.println("�ɹ�����Map");
        	               }
                	 }///imageName!=""
                 	 
		        
                 }///���ص�ͼ
                 
                 if(order[0]==3)///��λ
                 {
                ////*****************************����wifi����*******************************************///	
                	 long a=System.currentTimeMillis();
                	 ////////wifi ���ݱ������
                	 System.out.println("��λ");
                	 byte[] wifi_size=new byte[1];
                	 List<String> wifi_ssid=new ArrayList<String>();
                	 inputStream.read(wifi_size);
                	 ack=0;
                	 out.write(ack);
                	 out.flush(); 
                	//////////wifi�б� 
                	 Map<String,Double> wifi_up=new HashMap<String,Double>();
                 	 while(wifi_size[0]>0)
                	 {
                 		 ////////////����wifi����ǿ��
                		 byte[] wifi_byte=new byte[8];
                    	 inputStream.read(wifi_byte);
                    	 double wifi_value=m_Typeconvert.byteToDouble(wifi_byte);
                    	 ack=0;
                    	 out.write(ack);
                    	 out.flush(); 
                    	 ///////////����wifi������
                    	 byte[] str_wifi_name2=new byte[1024];
                    	 inputStream.read(str_wifi_name2);
                    	 char[] str_wifi_name1=m_Typeconvert.getChars(str_wifi_name2);
                    	 int j=0;
                    	 while(str_wifi_name1[j]!=0) j++;
                    	
                    	 String str_wifi_name= new String(str_wifi_name1,0,j);
                  
                		 if(wifi_value>Threshold)
                		 {
                			 wifi_up.put(str_wifi_name, wifi_value);
                			 wifi_ssid.add(str_wifi_name);
                			 System.out.println(str_wifi_name+" ��"+wifi_value);
                          	
                		 }
                		 ack=0;
                    	 out.write(ack);
                    	 out.flush();
                		 wifi_size[0]--;
                  	 ///////////////////
                	 }
		        
                 	 String map_name=null;
                 	 long aa=System.currentTimeMillis();
                ////*****************************Ѱ�ҵ�ͼ*******************************************///
                 	 int map_id=m_Algorithm.find_map_id(wifi_ssid, wifi_up, m_DatabaseManager);
                 	 
                 	 
                 	 
               ////**********************���ص�ͼ**********************************************///
                     String imageName=null;
                    
                     if(map_id==-1)
                     {
                    	 //TODO
                     }
                     imageName=m_DatabaseManager.get_map_name_from_map(map_id);
                   
                	 System.out.println("ִ�к�ʱ : "+(System.currentTimeMillis()-aa)/1000f+" �� ");
                 	 
                	 if( imageName=="")
                	 {
                		 System.out.println("ʧ�� ");
                		 ack=1;//��������ź�
    	               	 out.write(ack);
    	               	 out.flush();
    	               	
                	 }else
                	 {
                		 ack=0;//��������ź�
    	               	 out.write(ack);
    	               	 out.flush();
    	               	 ////////////////�����ļ�����
                    	 map_name=imageName;
    	               	 byte[] map_name_byte=map_name.getBytes();
    	               	 outputStream.write(map_name_byte);
    	              	 outputStream.flush();
    	               	 //////// 
    	              	 System.out.println("�ļ�����:"+imageName);
    	                 byte[] ack_map=new byte[1];
    	                 
    	                 inputStream.read(ack_map);
    	                 System.out.println(""+ack_map[0]);
    	                 if(ack_map[0]==1)/////�������� ����
    	                  {
    	            	   
    	                    System.out.println("��������Map");
    	                  }
    	                 if(ack_map[0]==0)////��Ҫ����
    	                  {
        	            	   File file = null;   
        	            	   long fileLength = 0;     
        	                   file = new File(TransferServer.PATH+map_name);    
        	            	   FileInputStream fins=null;
        	            	   fins=new FileInputStream(file);
        	            	   byte[] buffer = new byte[2*1024];   
        	            	   fileLength=file.length();
        	            	   /////////////////////�����ļ�����
        	            	   byte[] filelength_byte=new byte[8];
        	            	   m_Typeconvert.long2Byte(filelength_byte,fileLength);
        	            	   outputStream.write(filelength_byte);
        		               outputStream.flush();
        	            	   /////�����ļ�
        	            	   int len = -1;   
        	            	   long total=0;
        	            	   while(true) {   
		                             len = fins.read(buffer) ;
		                             outputStream.write(buffer, 0, len);   
		                             outputStream.flush();
		                             total=total+len;
		                             if(total>=fileLength)
		                            	 break;
		                             } 
        	            	   fins.close();
        	            	 
        	            	   
        	            	  
        	                   System.out.println("�ɹ�����Map");
    	                  }
    	         
    	           	      a=System.currentTimeMillis();
    	           	      
    	           	      
                   	
    	           	////**********************Ѱ�Һ��ʵĵ�**********************************************///
    	           	      List<Integer> ok_sample_id=m_Algorithm.find_right_sample_ok(wifi_ssid, wifi_up,map_id, m_DatabaseManager);
    	           	 
    	           	      
    	           	      
              
    	           	
    	           	      ///λ�õ�ľ���
        	           	  if(ok_sample_id.size()<1)
	        	           	{
	        	           	   ack=1;//����ʧ������ź�
	    	               	   out.write(ack);
	    	               	   out.flush();
	    	               	   System.out.println("ִ�к�ʱ : "+(System.currentTimeMillis()-aa)/1000f+" �� ");
	        	           	}else
	        	           	{
	        	           		ack=0;//��������ź�
	                          	out.write(ack);
	                          	out.flush();
	                 ////**********************���������Ȩ**********************************************///      	
	                          	Map<String,Double> coordinate=m_Algorithm.find_coordinate(ok_sample_id,wifi_ssid,wifi_up,m_DatabaseManager);
	                          	
	                          	
	                          	
	               	   			  //////////////////////����x����
	                          	   double _x=coordinate.get("x");
	            	               int x=(int)_x;
	            	               
	            	              
	            	               byte [] x_byte=m_Typeconvert.intToBytes(x);
	            	               outputStream.write(x_byte);
	                    		   outputStream.flush();
	            	               //////////////////////����y����
	                    		   double _y=coordinate.get("y");
	                    		   int y=(int)_y;
	                    		  
	            	              
	            	               byte [] y_byte=m_Typeconvert.intToBytes(y);
	            	               outputStream.write(y_byte);
	                    		   outputStream.flush();
	            	               //////////////////////����width
	            	               
	                    		   int width=1000;
	            	               
	            	               
	            	               byte [] width_byte=m_Typeconvert.intToBytes(width);
	            	               outputStream.write(width_byte);
	                    		   outputStream.flush();
	            	               /////////////////////����height
	                    		   int height=1000;
	            	               
	            	               
	            	               byte [] height_byte=m_Typeconvert.intToBytes(height);
	            	               outputStream.write(height_byte);
	                    		   outputStream.flush();
	                    		   System.out.println("x:"+_x+"	y:"+_y+" width:"+width+" height:"+height);
	                    		   time_sum+=(System.currentTimeMillis()-a)/1000f;
	                    		   System.out.println("time_sum:	"+time_sum);
	        	           	}	 
                	 }/////imageName!=""
                 	 
		         
               }///��λif
          /*********************�������ݶ�λ****************************************/
               if(order[0]==7)
               {
                     int MAP=0;
                   ////*****************************����wifi����*******************************************///	
                   	
                   	 ////////wifi ���ݱ������
                   	 System.out.println("�������ݶ�λ");                   	
                   	 ack=0;
                   	 out.write(ack);
                   	 out.flush();                          
                	   	List<Double> list_x=new ArrayList<Double>();
                		List<Double> list_y=new ArrayList<Double>(); 
                		Map<Byte,List> map_wifi_ssid=new HashMap<Byte,List>();
                		Map<Byte,Map> map_wifi_up=new HashMap<Byte,Map>();
                	 //   byte[] count =new byte[10];
                	  //  inputStream.read(count); 
                	    
                	   byte[] count=new byte[1024];
                   	   inputStream.read(count);
                   	   char[] count2=m_Typeconvert.getChars(count);
                   	   int k=0;
                   	   while(count2[k]!=0) k++;
                   	   String count_num= new String(count2,0,k);  
                	    
                	    int NUM=Integer.parseInt(count_num);
                	    
                	    out.write(ack);
                	    out.flush();
                    	while(NUM>0){       //ѭ�����ܶ������ݽ��ж�λ������λ����洢��һ��list���б�����һ������                 		
                    		byte[] wifi_size=new byte[1];
                       	 List<String> wifi_ssid=new ArrayList<String>();
                       	 inputStream.read(wifi_size);
                       	 ack=0;
                       	 out.write(ack);
                       	 out.flush(); 
                       	//////////wifi�б� 
                       	 Map<String,Double> wifi_up=new HashMap<String,Double>();
                        	 while(wifi_size[0]>0)
                       	 {
                        		 ////////////����wifi����ǿ��
                       		 byte[] wifi_byte=new byte[8];
                           	 inputStream.read(wifi_byte);
                           	 double wifi_value=m_Typeconvert.byteToDouble(wifi_byte);
                           	 ack=0;
                           	 out.write(ack);
                           	 out.flush(); 
                           	 ///////////����wifi������
                           	 byte[] str_wifi_name2=new byte[1024];
                           	 inputStream.read(str_wifi_name2);
                           	 char[] str_wifi_name1=m_Typeconvert.getChars(str_wifi_name2);
                          	 int j=0;
                         	 while(str_wifi_name1[j]!=0) j++;
                           	
                           	  String str_wifi_name= new String(str_wifi_name1,0,j);
                         
                         //  	 String str_wifi_name= new String(str_wifi_name2,"utf-8");
                       		 if(wifi_value>Threshold)
                       		 {
                       			 wifi_up.put(str_wifi_name, wifi_value);
                       			 wifi_ssid.add(str_wifi_name);
                       			 System.out.println(str_wifi_name+" ��"+wifi_value);

                       		 }
                       		 ack=0;
                           	 out.write(ack);
                           	 out.flush();
                       		 wifi_size[0]--;
                         	 ///////////////////
                       		 
                       	 }
                 ////*****************************Ѱ�ҵ�ͼ*******************************************///
                     	 int map_id=m_Algorithm.find_map_id(wifi_ssid, wifi_up, m_DatabaseManager);
                          MAP=map_id;
              
                ////**********************Ѱ�Һ��ʵĵ�**********************************************///
       	           	      List<Integer> ok_sample_id=m_Algorithm.find_right_sample_ok(wifi_ssid, wifi_up,map_id, m_DatabaseManager);
       	      ////**********************���������Ȩ**********************************************///      	
 	                     Map<String,Double> coordinate=m_Algorithm.find_coordinate(ok_sample_id,wifi_ssid,wifi_up,m_DatabaseManager);
 	                     double x=coordinate.get("x");
          	             double y=coordinate.get("y");
          	             list_x.add(x);
          	             list_y.add(y);
          	             System.out.print("x������"+x+"y������"+y);
          	             List<String> wifi_ssid_to_map=new ArrayList<String>();
          	             Map<String,Double> wifi_up_to_map=new HashMap<String,Double>();
          	             wifi_ssid_to_map.addAll(wifi_ssid);
          	             wifi_up_to_map.putAll(wifi_up);
          	             map_wifi_ssid.put(count[0], wifi_ssid_to_map);
          	             map_wifi_up.put(count[0], wifi_up_to_map);
          	             
          	             wifi_ssid.clear();
          	             wifi_up.clear();
          	             count[0]--;
          	             NUM--;
          	             
          	             ack=0;
                     	 out.write(ack);
                     	 out.flush();
                    	 }	
                  ////*****************************���ص�ͼ*******************************************/// 
  /* ����ʱ ע�͵�       
               
                   String imageName=null;
                      String map_name=null;
                      if(MAP==-1){
                     	 //TODO
                      }
                   imageName=m_DatabaseManager.get_map_name_from_map(MAP);
                   if(imageName==""){
                 	  System.out.println("��ͼ����ʧ��");
                 	  ack=1;
                 	  out.write(ack);
                 	  out.flush();
                   }else{
                 	 ack=5;//��������ź�
  	               	 out.write(ack);
  	               	 out.flush();       
  	               	 ////////////////�����ļ�����
  	                 map_name=imageName;
  	               	 byte[] map_name_byte=map_name.getBytes();
  	               	 outputStream.write(map_name_byte);
  	              	 outputStream.flush();
  	               	 //////// 
  	              	 System.out.println("��ͼ����:"+imageName);
  	                 byte[] ack_map=new byte[1];
  	                 inputStream.read(ack_map);
  	                 System.out.println(""+ack_map[0]);
  	                 if(ack_map[0]==1){
  	                	 System.out.println("�������ص�ͼ");
  	                 }
  	                 if(ack_map[0]==0){
  	             	   File file = null;   
 	            	   long fileLength = 0;     
 	                   file = new File(TransferServer.PATH+map_name);    
 	            	   FileInputStream fins=null;
 	            	   fins=new FileInputStream(file);
 	            	   byte[] buffer = new byte[2*1024];   
 	            	   fileLength=file.length();
 	            	   /////////////////////�����ļ�����
 	            	   byte[] filelength_byte=new byte[8];
 	            	   m_Typeconvert.long2Byte(filelength_byte,fileLength);
 	            	   outputStream.write(filelength_byte);
 		               outputStream.flush();
 	            	   /////�����ļ�
 	            	   int len = -1;   
 	            	   long total=0;
 	            	   while(true) {   
                              len = fins.read(buffer) ;
                              outputStream.write(buffer, 0, len);   	                            
                              total=total+len;
                              if(total>=fileLength)
                             	 break;
                              } 
 	            	   outputStream.flush();
 	            	   fins.close();    	            	    	            	     	            	  
 	                   System.out.println("�ɹ�����Map"); 
  	                 }
                   }  
          */       	 /*****�ڴ˶Եõ��Ķ���������е�ͼ�ϴ���Ȼ�󴫸��ͻ���******/
            /*        List<Double> list_XY=(new  Combination()).comb(list_x,list_y, MAP,map_wifi_ssid,map_wifi_up);
                     double  x_1=list_XY.get(0);
                     double  y_1=list_XY.get(1);
                     
                     
                  
                     int x=(int)x_1;
                     byte [] x_byte=m_Typeconvert.intToBytes(x);
  	                outputStream.write(x_byte);
          		    outputStream.flush();
          		   
          		    
          		
          		    int y=(int)y_1;
          		    byte [] y_byte=m_Typeconvert.intToBytes(y);
 	                outputStream.write(y_byte);
         		    outputStream.flush();
          		    
         		    //////////////////////����width
  	               
          		   int width=1000;
  	               
  	               
  	               byte [] width_byte=m_Typeconvert.intToBytes(width);
  	               outputStream.write(width_byte);
          		   outputStream.flush();
  	               /////////////////////����height
          		   int height=1000;
  	               
  	               
  	               byte [] height_byte=m_Typeconvert.intToBytes(height);
  	               outputStream.write(height_byte);
          		   outputStream.flush();
          		   System.out.println("x:"+x_1+"	y:"+y_1+" width:"+width+" height:"+height);
                
            */    	 
                
         		                	
                   	 }   
             
              
              if(order[0]==8){

                	System.out.println("���ض������ݶ�λ");    
                	int MAP=0;
                    ack=0;
                    out.write(ack);
                    out.flush(); 
                    List<Double> list_x=new ArrayList<Double>();
               		List<Double> list_y=new ArrayList<Double>(); 
               		Map<Byte,List> map_wifi_ssid=new HashMap<Byte,List>();
               		Map<Byte,Map> map_wifi_up=new HashMap<Byte,Map>();
               		   
               		String txtpath="E:\\���ڶ�λ2\\��λ��������\\x2823y1750cy";
               		File file=new File(txtpath);
               	   if(file.isDirectory()){
               		File[] fileArray=file.listFiles();
               		for(File one_directory:fileArray){
    					String file_name=one_directory.getName();
    					String [] filename=file_name.split("x|y|e|n|s|w");
    					System.out.println("�ļ�������:"+file_name);
    				    File[] m_file=one_directory.listFiles();
    					System.out.println("�ļ�������"+m_file.length);
    					int count=m_file.length;
    					for(File temp_file:m_file){
    						if(temp_file.getPath().endsWith(".txt")){
    							 List<String> wifi_ssid=new ArrayList<String>();
    							 Map<String,Double> wifi_up=new HashMap<String,Double>();
    							BufferedReader in_txt = new BufferedReader(new FileReader(temp_file.getPath()));
    							String con="";
    							String tt="";
    							String aa=" ";					
    							while((con=in_txt.readLine())!=null)
    							{
    								tt+=con;
    							}
    							String [] info=tt.split("\\s+");
    							for(int i=0;i<info.length;i=i+2){
    								wifi_ssid.add(info[i]);
    								wifi_up.put(info[i], Double.valueOf(info[i+1]));
//    								System.out.println(info[i]+"---->"+info[i+1]);
    							}
    							System.out.println("�ѳɹ��ϴ��ļ���"+temp_file.getPath());
    							 ////*****************************Ѱ�ҵ�ͼ*******************************************///
    	                     	 int map_id=m_Algorithm.find_map_id(wifi_ssid, wifi_up, m_DatabaseManager);
    	                          MAP=map_id;
    	            
    	                       
    	                ////**********************Ѱ�Һ��ʵĵ�**********************************************///
    	       	           	      List<Integer> ok_sample_id=m_Algorithm.find_right_sample_ok(wifi_ssid, wifi_up,map_id, m_DatabaseManager);
    	       	      ////**********************���������Ȩ**********************************************///      	
    	 	                     //Map<String,Double> coordinate=m_Algorithm.find_coordinate(ok_sample_id,wifi_ssid,wifi_up,m_DatabaseManager);
    	 	                     /************LDA*******************/
    	 	                    Map<String,Double> lda=((KNN_ML) m_Algorithm).LDA_coordinate(ok_sample_id,wifi_ssid,wifi_up,m_DatabaseManager);
    	       	           //	Map<String,Double> lda=section_ap_impotance.LDA_coordinate(ok_sample_id, wifi_ssid, wifi_up, m_DatabaseManager);
    	       	           	Map<String,Double> auto_sec=auto_section.find_coordinate(ok_sample_id, wifi_ssid, wifi_up, m_DatabaseManager);
    	 	                     /*********************************/
    	 	                    /* double x=coordinate.get("x");
    	          	             double y=coordinate.get("y");
    	          	             list_x.add(x);
    	          	             list_y.add(y);*/
    	          	             List<String> wifi_ssid_to_map=new ArrayList<String>();
    	          	             Map<String,Double> wifi_up_to_map=new HashMap<String,Double>();
    	          	             wifi_ssid_to_map.addAll(wifi_ssid);
    	          	             wifi_up_to_map.putAll(wifi_up);
    	          	//             map_wifi_ssid.put(count[0], wifi_ssid_to_map);
    	          	//             map_wifi_up.put(count[0], wifi_up_to_map);
    	          	             wifi_ssid.clear();
    	          	             wifi_up.clear();
//    	          	             count[0]--;
    	          	             	          	             	          	           
    						}
    					}
               		}
               	   }
               	   System.out.println("��ζ�λ������������");
                   }
                
            }
             //////////////////////
            
        }catch(Exception e) {  
        	
        }finally {

        	  
            


        }   
      }//run

  }
