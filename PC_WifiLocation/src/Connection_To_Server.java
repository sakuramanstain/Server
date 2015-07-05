import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Connection_To_Server {
	public Connection_To_Server() throws IOException{
	    m_Typeconvert type=new m_Typeconvert();     
		ArrayList<String> FileNameList=new ArrayList<String>();
		Map<String,String> Result_in=new HashMap<String,String>();
		String txtpath="C:\\Users\\wangpeng\\Desktop\\主E采样终极版\\dingwei\\x2343y1190cy";	
		File file=new File(txtpath);
		if(file.isDirectory()){
			File[] fileArray=file.listFiles();	
			for(File one_directory:fileArray){
				if(one_directory.isDirectory()){
					String file_name=one_directory.getName();
					String [] filename=file_name.split("x|y|e|n|s|w");			
					System.out.println("文件夹名称:"+file_name);
					File[] m_file=one_directory.listFiles();
					System.out.println("文件个数："+m_file.length);
		      		int Count=m_file.length;
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
							for(int i=4;i<info.length;i=i+2){
								Result_in.put(info[i], info[i+1]);
							}
					//		Connection up_load1=new Connection(Result_in);
				     		Connection up_load2=new Connection(Result_in,Count);
				     		Count--;
							Result_in.clear();
							System.out.println("已成功上传文件："+temp_file.getPath());
						}
					}
				}
			}
		}
	
	}

}
