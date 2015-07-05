import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.html.HTMLDocument.Iterator;


public class Deal {
	public static void main(String args[]) throws IOException{		
	ArrayList<String> FileNameList=new ArrayList<String>();
	Map<String,String> Level=new HashMap<String,String>();
	String txtpath="C:\\Users\\wangpeng\\Desktop\\室内定位\\王鹏\\车库\\定位测试点";	
	File file=new File(txtpath);
	 String cc="";
	if(file.isDirectory()){		
		File[] fileArray=file.listFiles();		
		for(File one_directory:fileArray){			
			if(one_directory.isDirectory()){	
				String file_name=one_directory.getName();
				String [] filename=file_name.split("x|y|e|n|s|w|c|y");
				
				int x=Integer.parseInt(filename[1]);
				int y=Integer.parseInt(filename[2]);
			   String value="x   "+x+"    y     "+y+"    ";
			   
				System.out.println(file_name);
				File[] m_file=one_directory.listFiles();				
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
						String [] info=tt.split("\\s+");					
						for(int i=4;i<info.length;i=i+2)						
						{
							int j=i;
							if(Level.containsKey(info[i])){
							    String level_temp=Level.get(info[i])+aa;
								Level.put(info[i], level_temp+info[j+1]);
							}else{
								Level.put(info[i], info[j+1]);
							}					
						}
						in_txt.close();
						
					}
					
				}
		/* 数据处理  */		
				java.util.Iterator<Entry<String, String>> iter = Level.entrySet().iterator(); 
				while(iter.hasNext()){
					Map.Entry<String,String> entry = (Map.Entry<String, String>)iter.next();
					String [] SSID=entry.getValue().split("\\s+");
					List list=new ArrayList();
					List list_delt=new ArrayList();
					
					for(int i=0;i<SSID.length;i++){											
			
				  if(i==0){
							 double a=Math.abs(Double.valueOf(SSID[i]));
							 list.add(a);
						 }else{
							 double sum=0;
							 double sum_2=0;
							 double delt=0;
							 for(int j=0;j<=i;j++){	
								double b=Math.abs(Double.valueOf((SSID[j])));
								 sum+=Math.log(b);
								 sum_2+=Math.pow(Math.log(b), 2);
							 }
							 double  average=(double)sum/(i+1);
							 delt=sum_2/(i+1)-Math.pow(average, 2);
							 list.add(Math.exp(average));
							 list_delt.add(delt);
							 }
						 }
					double sum1=0;
					double average1=0;
					for(int k=0;k<list.size();k++){
					     String temp= list.get(k).toString();
					     sum1+= Double.valueOf(temp);	
					     System.out.println(list.get(k));
							 }
			        average1=sum1/list.size();
			       double delt=0;
			        for(int k=0;k<list.size();k++){
			        	String temp= list.get(k).toString();
			        	delt+=(Double.valueOf(temp)-average1)*(Double.valueOf(temp)-average1);
			        }
			        delt=delt/list.size();
			       
			      Level.put(entry.getKey(), String.valueOf(average1)+" "+delt);
			       
					list.clear();
            		System.out.println(entry.getKey()+":"+entry.getValue());
				    value+="  "+entry.getKey()+"   "+entry.getValue();	
            		
			}												
				FileWriter fw = new FileWriter(one_directory.getPath()+".txt");	
				fw.append(value);
				fw.close();
				Level.clear();
			}
		}
			
	}

	}
	}


