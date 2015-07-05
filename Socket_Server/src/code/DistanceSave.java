package code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

class DistanceSave
{
	String file_name;
	int m_from=-1;
	int m_to=-1;
	String [] value;
	List<Integer> all_sample_id=new ArrayList<Integer>();
	void initial(String name,List<Integer> sample_id)
	{
		all_sample_id.clear();
		all_sample_id.addAll(sample_id);
		file_name=name;
		value=null;
		m_from=-1;
		try {
			File temp_distance=new File(name+".txt");
			if(temp_distance.exists())
			{
				temp_distance.delete();
				temp_distance.createNewFile();
			}else
				temp_distance.createNewFile();
			//fw_temp = new FileWriter (temp_distance,true);
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp_distance)));  
			
				double dis_value=Double.MAX_VALUE;
				BufferedReader in_txt;
				try {
					in_txt = new BufferedReader(new FileReader(file_name));
				
					String con="";
					
					try {
						while((con=in_txt.readLine())!=null)
						{
							String [] value_sum=con.split("\\s+");
							dis_value=0;
							for(String tt:value_sum)
							{
								
								dis_value+=Double.parseDouble(tt);
							}
							bw.write(""+dis_value+"  ");
						
						}
						//con=in_txt.readLine();
						in_txt.close();
						bw.flush();
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
			//fw_temp.close();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	double get_distance(int from)
	{
		double dis_value=Double.MAX_VALUE;
		int index=all_sample_id.indexOf(from);
		
		BufferedReader in_txt;
		try {
			in_txt = new BufferedReader(new FileReader(file_name+".txt"));
		
			String con="";
			int i=0;
			try {
				con=in_txt.readLine();
				//con=in_txt.readLine();
				in_txt.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String [] value_sum=con.split("\\s+");
			
			dis_value=Double.parseDouble(value_sum[index]);
			value_sum=null;
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dis_value;
	}
	double get_distance(int from,int to)
	{
		double dis_value=Double.MAX_VALUE;
			if(m_from==from)
			{
				dis_value=Double.parseDouble(value[all_sample_id.indexOf(to)]);
				
			}else
			{
				int index=all_sample_id.indexOf(from);
				
				BufferedReader in_txt;
				try {
					in_txt = new BufferedReader(new FileReader(file_name));
				
					String con="";
					int i=0;
					try {
						while((con=in_txt.readLine())!=null&&i++<index);
						//con=in_txt.readLine();
						in_txt.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					value=con.split("\\s+");
					m_from=from;
					int indexto=all_sample_id.indexOf(to);
					String ffff=value[indexto];
					dis_value=Double.parseDouble(value[indexto]);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dis_value;
		
	}
	
}