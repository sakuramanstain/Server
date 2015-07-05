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


public class DataDeal {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String name_path="C:\\Users\\zls\\Desktop\\448";
		File file =new File(name_path);  
		//如果文件夹不存在则创建  
		if(file .exists()  && file .isDirectory())    
		{     
			if(file.listFiles().length > 0)
			{
				File [] file_list=file.listFiles();
				for(File one_directory:file_list)
				{
					
					if(one_directory.isDirectory())
					{
						String path=one_directory.getName();
						String [] value=path.split("x|y");
						int x=Integer.parseInt(value[1]);
						int y=Integer.parseInt(value[2]);
						File [] m_file=one_directory.listFiles();
						List<String> BSSID_List=new ArrayList<String>();
						{
							BufferedReader in_txt = new BufferedReader(new FileReader(m_file[0].getPath()));
							String con="";
							String tt="";
							while((con=in_txt.readLine())!=null)
							{
								tt+=con;
							}
							String [] BSSID=tt.split("\\s+");
							for(int i=4;i<BSSID.length;i=i+2)
							{
								BSSID_List.add(BSSID[i]);
							}
							in_txt.close();
						}
						for(File temp_file:m_file)
						{
							BufferedReader in_txt = new BufferedReader(new FileReader(temp_file.getPath()));
							String con="";
							String tt="";
							while((con=in_txt.readLine())!=null)
							{
								tt+=con;
							}
							String [] BSSID=tt.split("\\s+");
							List<String> BSSID_List_temp=new ArrayList<String>();
							for(int i=4;i<BSSID.length;i=i+2)
							{
								BSSID_List_temp.add(BSSID[i]);
							}
							in_txt.close();
							BSSID_List.retainAll(BSSID_List_temp);
						}
						Map<String,Double> mapwifi=new HashMap<String,Double>();
						Map<String,Double> mapwifidelt=new HashMap<String,Double>();
						for(String bssid:BSSID_List)
						{
							mapwifi.put(bssid, 0.0);
							mapwifidelt.put(bssid, 0.0);
						}
						for(File temp_file:m_file)
						{
							BufferedReader in_txt = new BufferedReader(new FileReader(temp_file.getPath()));
							String con="";
							String tt="";
							while((con=in_txt.readLine())!=null)
							{
								tt+=con;
							}
							String [] BSSID=tt.split("\\s+");
		
							for(int i=4;i<BSSID.length;i=i+2)
							{
								if(mapwifi.containsKey(BSSID[i]))
								{
									mapwifi.put(BSSID[i], mapwifi.get(BSSID[i])+Math.log10(-Double.parseDouble(BSSID[i+1])));
									mapwifidelt.put(BSSID[i], mapwifidelt.get(BSSID[i])+Math.pow(Math.log10(-Double.parseDouble(BSSID[i+1])),2));
								}
							}
							in_txt.close();
							
						}
						String tt="x   "+x+"    y     "+y+"    ";
				       	 Iterator<Entry<String, Double>> iter = mapwifi.entrySet().iterator(); 
				       	 while(iter.hasNext()){ 
				       	  Map.Entry<String, Double> entry = (Map.Entry<String, Double>)iter.next();
				       	  double v=-Math.pow(10, entry.getValue()/m_file.length);
				       	  int vv=(int) v;
				       	  double delt=(mapwifidelt.get(entry.getKey())-m_file.length*Math.pow(entry.getValue()/m_file.length, 2))/(m_file.length-1);
				       	  tt+="  "+entry.getKey()+"   "+vv+"   "+delt+"    ";
				       	  System.out.println("  "+entry.getKey()+"   "+v+"   "+entry.getValue()/m_file.length+"    ");
				       	 } 
				       	File fff=new File(name_path+"-");
				       	fff.mkdir();
				       	File ff=new File(name_path+"-\\"+path);
				       	ff.mkdir();
						FileWriter fw = new FileWriter(name_path+"-\\"+path+"\\"+path+".txt");		
						fw.append(tt);
						fw.close();
						System.out.println(path);
					}
				}
			}
		} 
	}

}
