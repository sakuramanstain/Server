package JuLei;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class ReadFile {
	// switch string to double
		public static ArrayList<Double> toDouble(String line){
			String[] str = line.split(",");
			ArrayList<Double> doubleArray = new ArrayList<Double>();
			for(int i = 0; i < str.length; i++){
				doubleArray.add(Double.valueOf(str[i]));	
			}
			return doubleArray;
		}
		
		// import data file
		public ArrayList<ArrayList<Double>> addFile() throws Exception{
			ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
			FileReader fr = new FileReader("C:\\Users\\wangpeng\\Desktop\\data1.txt");
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null){
				data.add(toDouble(line));
			}
			br.close();
			return data;
		}	

}
