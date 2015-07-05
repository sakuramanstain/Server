package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Local_location {

	public static void main(String[] args) throws IOException {
		DatabaseManager m_DatabaseManager1=new DatabaseManager();
     	m_DatabaseManager1.Initial();
     	Algorithm m_Algorithm=new KNN();//KNN_ML() KNN() KNN_DistanceLearn  PAMClusterTrainNew KNN_basde_on_Clique() KNN_based_on_PAM() KNN()  k_NN();
     	Section_ap_importance section_ap_impotance=new Section_ap_importance();
     	Auto_section auto_section=new Auto_section();
     	Auto_section2 auto_section2=new Auto_section2();
     	// TODO Auto-generated method stub
		//String txtpath="E:\\主E采样终极版\\主E采样终极版\\dingwei";
		String txtpath="E:\\室内定位2\\定位测试数据";
		File first_file=new File(txtpath);
		File[] first_files=first_file.listFiles();
		for(File second_file:first_files){
		 String name=second_file.getName();
		 String[] names=name.split("x|y|c");
			System.out.println(names[1]+","+names[2]);
			ArrayList<Double> result_list=new ArrayList<Double>();
			 Double ox=Double.valueOf(names[1]);
			 Double oy=Double.valueOf(names[2]);
			 KNN_ML.location_section_id.clear();
			 Section_ap_importance.location_section_id.clear();
		 if(second_file.isDirectory()){
				File[] fileArray=second_file.listFiles();	
				for(File one_directory:fileArray){
					if(one_directory.isDirectory()){
					File[] m_file=one_directory.listFiles();
					for(File temp_file:m_file){
						if(temp_file.getPath().endsWith(".txt")){
							
							Map<String,Double> wifi_up=new HashMap<String,Double>();
							 List<String> wifi_ssid=new ArrayList<String>();
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
								Double values= Double.parseDouble(info[i+1]);
								wifi_ssid.add(info[i]);
								wifi_up.put(info[i], values);
							}
							int map_id=m_Algorithm.find_map_id(wifi_ssid, wifi_up, m_DatabaseManager1);
							 List<Integer> ok_sample_id=m_Algorithm.find_right_sample_ok(wifi_ssid, wifi_up,map_id, m_DatabaseManager1);
							 Map<String,Double> coordinate=m_Algorithm.find_coordinate(ok_sample_id, wifi_ssid, wifi_up, m_DatabaseManager1);
							 //Map<String,Double> coordinate=section_ap_impotance.LDA_coordinate(ok_sample_id,wifi_ssid,wifi_up,m_DatabaseManager1);
							// Map<String,Double> coordinate=((KNN_ML) m_Algorithm).LDA_coordinate(ok_sample_id,wifi_ssid,wifi_up,m_DatabaseManager1);
							//Map<String,Double> coordinate=auto_section.find_coordinate(ok_sample_id, wifi_ssid, wifi_up, m_DatabaseManager1);
							//	Map<String,Double> coordinate=auto_section2.find_coordinate(ok_sample_id, wifi_ssid, wifi_up, m_DatabaseManager1);
								Double x=coordinate.get("x")*4126/1000;
							 Double y=coordinate.get("y")*2610/1000;
							
							 Double error=Math.sqrt(Math.pow(x-ox,2)+Math.pow(y-oy,2));
							 result_list.add(error);
							 wifi_ssid.clear();
	          	             wifi_up.clear();
	          	             
					}
				}
	}

}
	}
		 int number=result_list.size();
		 double sum=0;
		 for(Double result:result_list){
			 sum=sum+result;
		 }
		 double err=sum/(number*100);
		 System.out.println("错误为"+err);
		 result_list.clear();
		 String out_path="F:\\workspace\\output66knn.txt";
		 File out_file=new File(out_path);
		 if(!out_file.exists()){
			 out_file.createNewFile();
		 }
		 FileWriter writer=new FileWriter(out_file, true);
		 writer.write(err+"  ");
		 writer.close();
		}
}
}