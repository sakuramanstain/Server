import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Chose_aps_we_need {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String url="E:\\室内定位2\\新数据\\概率法\\locating";//原始采样数据
		String destination="E:\\室内定位2\\新数据\\概率法\\locating_delt";//输出根目录
		File root=new File(url);
		File[] first_directory=root.listFiles();//根目录下的一级目录
		for (File first_element : first_directory) {
			File[] second_directory=first_element.listFiles();//根目录下的二级目录
			String first_name=first_element.getName();
			for (File second_element : second_directory) {
				String second_name=second_element.getName();
				BufferedReader reader=new BufferedReader(new FileReader(second_element));
				String content="";
				while((content=reader.readLine())!=null){
					
					String aim_path=destination+File.separator+first_name;
					File aim_file=new File(aim_path);
					if(!aim_file.exists()){
						aim_file.mkdirs();
					}
					String final_path=destination+File.separator+first_name+File.separator+second_name;
					File final_file=new File(final_path);
					if(!final_file.exists()){
						final_file.createNewFile();
					}
					String[] cons=content.split("\\s+");
					FileWriter writer=new FileWriter(final_file, true);
					writer.write(cons[0]+"\t");
					writer.write(cons[1]+"\t");
					writer.write(cons[2]+"\t");
					writer.write(cons[3]+"\t");
					for (int i=4;i<cons.length;i++) {
						//BufferedWriter writer=new BufferedWriter(new FileWriter(aim_file));
						String con_temp=cons[i];
						String p="tp\\d";//模式为tp1、2、3、4、5、6;
						Pattern pattern=Pattern.compile(p);
						Matcher m=pattern.matcher(con_temp);
						if(m.find()){
							if(con_temp.equals(m.group())){   //匹配到的子字符串与父字符串完全一致
								writer.write(cons[i-1]+"\t");
								writer.write(cons[i]+"\t");
								writer.write(cons[i+1]+"\t");
							}
							
							}
						
					}
					writer.close();
				}
				reader.close();
				//System.out.println(destination+File.separator+first_name+File.separator+second_name);
				//break;
			}
			//break;
		}
		//String s="tp5";
		
		
		
	}

}
