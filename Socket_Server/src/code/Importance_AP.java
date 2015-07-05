package code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Importance_AP {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 ArrayList<String> FileNameList=new ArrayList<String>();
		String txtPath="E:\\samples";
		 File file = new File(txtPath);
		 
	      if(file.isDirectory()){
	           File [] oneArray = file.listFiles();//第一级目录
		for(File one_directory:oneArray)
		{
			
			
			if(one_directory.isDirectory())
			{
				File [] twoArray=one_directory.listFiles();//第二级目录
				for(File two_directory:twoArray)
				{
					if(two_directory.isDirectory()){
						File [] threeArray=two_directory.listFiles();//第三级目录
						for(File temp_file:threeArray){
							if(temp_file.getPath().endsWith(".txt")){
								BufferedReader reader=new BufferedReader(new FileReader(temp_file.getPath()));
								String content="";
								while((content=reader.readLine())!=null){
									String[] con=content.split("\\s+");
									String coordinate=con[0]+con[1]+con[2]+con[3];
									for(int i=4;i<con.length;i=i+2){
										String[] stri=con[i].split(":");
										StringBuffer sb=new StringBuffer();
										for(int j=0;j<stri.length-1;j++){
											sb.append(stri[j]+"_");
										}
										sb.append(stri[stri.length-1]);
										String ap_name=sb.toString();
										   String ap_directory="E:\\apimportance"+File.separator+ap_name;
										   File ap_file=new File(ap_directory);
										   String location_name=ap_directory+File.separator+coordinate+".txt";
										   File location_file=new File(location_name);
										   if(!ap_file.exists()){
											   ap_file.mkdirs();
										   }
										   if(!location_file.exists()){
											   location_file.createNewFile();
										   }
										  FileWriter writer=new FileWriter(location_file.getPath(),true);
										  writer.write(con[i+1]+"  ");
										  writer.close();
									}
								}
								reader.close();
								
								
								
							}
							
						
						}
					}
					/*if(two_directory.getPath().endsWith(".txt"))
					{
						String file_name=two_directory.getName();
						//String [] filename=file_name.split("x|y|e|n|s|w");
						String myname=file_name.substring(0, file_name.length()-5);
						//String name=temp_file.getPath();
						//String Orientation=name.substring(name.length()-5,name.length()-4);	
						//FileNameList.add(temp_file.getPath());
						//System.out.print(myname+"  "+name);
						//break;
						String newname="E:\\apimportance"+File.separator+myname+".txt";
						File myfile=new File(newname);
						if(!myfile.exists()){
							myfile.createNewFile();
							BufferedReader in_txt = new BufferedReader(new FileReader(two_directory.getPath()));
							BufferedWriter out_txt=new BufferedWriter(new FileWriter(myfile));
							String con="";
							while((con=in_txt.readLine())!=null)
							{
								out_txt.write(con);
							}
							out_txt.close();
							in_txt.close();
						}
						
					}*/
				}
			}
		}
		
		
		
	}
		/* HashMap<String, List> map=new HashMap<String, List>();
		 String txtPath="E:\\apimportance";
		 File file = new File(txtPath);
	      if(file.isDirectory()){
	           File [] fileArray = file.listFiles();
		
				for(File temp_file:fileArray)
				{
					if(temp_file.getPath().endsWith(".txt"))
					{
						
							BufferedReader in_txt = new BufferedReader(new FileReader(temp_file.getPath()));
							String con="";
							while((con=in_txt.readLine())!=null)
							{
								String[] tt=con.split("\\s+");
								int t=(tt.length-4)/3;
								for(int i=4;i<tt.length;i=i+3)
								{
									String value_double=tt[i+1];
	            	    	      	double sum=Double.parseDouble(value_double);
	            	    	       String value_delt=tt[i+2];
	            	    	       double sumdelt2=Double.parseDouble(value_delt);
								}
							}
							in_txt.close();
						}
						
					}
				}
			}*/
		}
		
		
	 
	      
	      
	      
   
}
