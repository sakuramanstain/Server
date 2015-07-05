package code;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.text.html.HTMLDocument.Iterator;

public class Combination {
	   List<Double> comb(List<Double> list_x,List<Double> list_y,int map_id,Map<Byte,List> wifi_ssid,Map<Byte,Map> wifi_up){
		/**************实现多个定位信息在划分好的地图上进行剔除与组合*******************/
		int  Num=list_x.size();
		double X=0,Y=0;
		DatabaseManager m_DatabaseManager=new DatabaseManager();
		m_DatabaseManager.Initial();
		Algorithm m_Algorithm= new KNN_in_Section();
		String imageName=null;
		imageName=m_DatabaseManager. get_map_name_from_map(map_id);
		File file_image=new File( "F:" +File.separator+ "workspace" + File.separator+imageName);
		BufferedImage bi=null;
		try {
			bi=ImageIO.read(file_image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		int width=bi.getWidth();    //地图像素
		int height=bi.getHeight();
		System.out.println(imageName+"的像素为"+width+" "+height);
		/*******将图分区间,目前实验分为13个******/

		int section1 = 0,section2 = 0,section3 = 0,section4 = 0,section5 = 0,section6 = 0,section7 = 0,section8 = 0,section9 = 0,section10 = 0,section11 = 0,section12 = 0,section13 = 0;
		List<Double>  List_S1=new ArrayList();  //生成7个链表 存储不同section中的坐标，后期需要修改，动态生成
		List<Double>  List_S2=new ArrayList();
		List<Double>  List_S3=new ArrayList();
		List<Double>  List_S4=new ArrayList();
		List<Double>  List_S5=new ArrayList();
		List<Double>  List_S6=new ArrayList();
		List<Double>  List_S7=new ArrayList();
		List<Double>  List_S8=new ArrayList();
		List<Double>  List_S9=new ArrayList();
		List<Double>  List_S10=new ArrayList();
		List<Double>  List_S11=new ArrayList();
		List<Double>  List_S12=new ArrayList();
		List<Double>  List_S13=new ArrayList();
		List<Double>  List_ALL=new ArrayList();
		List<Integer> List_section=new ArrayList();
		for(int j=0;j<Num;j++){
			double x=(list_x.get(j)/1000)*1568.0;
			double y=(list_y.get(j)/1000)*988.0;			
			if((464<=x&&x<=586)&&(0<=y&&y<=359)){
	      		section1++;
	      		List_S1.add(x);
	      		List_S1.add(y);
	      	}
	      	if((981<=x&&x<=1103)&&(0<=y&&y<=359)){
	      		section2++;
	      		List_S2.add(x);
	      		List_S2.add(y);
	      	}
	      	if((0<=x&&x<=356)&&(436<=y&&y<=554)){
	      		section3++;
	      		List_S3.add(x);
	      		List_S3.add(y);
	      	}
	      	if((1211<=x&&x<=1568)&&(436<=y&&y<=554)){
	      		section4++;
	      		List_S4.add(x);
	      		List_S4.add(y);
	      	}
	      	if((356<=x&&x<=586)&&(359<=y&&y<=753)){
	      		section5++;
	      		List_S5.add(x);
	      		List_S5.add(y);
	      	}
	      	if((981<=x&&x<=1211)&&(359<=y&&y<=743)){
	      		section6++;
	      		List_S6.add(x);
	      		List_S6.add(y);
	      	}
	      	if((78<=x&&x<=356)&&(685<=y&&y<=743)){
	      		section7++;
	      		List_S7.add(x);
	      		List_S7.add(y);
	      	}
	      	if((1211<=x&&x<=1489)&&(685<=y&&y<=743)){
	      		section8++;
	      		List_S8.add(x);
	      		List_S8.add(y);
	      	}
	      	if((464<=x&&x<=586)&&(753<=y&&y<=988)){
	      		section9++;
	      		List_S9.add(x);
	      		List_S9.add(y);
	      	}
	      	if((981<=x&&x<=1103)&&(753<=y&&y<=988)){
	      		section10++;
	      		List_S10.add(x);
	      		List_S10.add(y);
	      	}
	      	if((586<=x&&x<=981)&&(0<=y&&y<=177)){
	      		section11++;
	      		List_S11.add(x);
	      		List_S11.add(y);
	      	}
	      	if((586<=x&&x<=981)&&(813<=y&&y<=988)){
	      		section12++;
	      		List_S12.add(x);
	      		List_S12.add(y);
	      	}
	      	if((552<=x&&x<=981)&&(177<=y&&y<=813)){
	      		section13++;
	      		List_S13.add(x);
	      		List_S13.add(y);
	      	}
		
	}	
	
		System.out.println("共有："+Num+"定位测试，"+"Section1:"+section1+"  Section2:"+section2+"  Section3:"+section3+"  Section4:"+section4+"  Section5:"+section5+"  Section6:"+section6+"  Section7:"+section7+"  Section8:"+section8+"  Section9:"+section9+"  Section10:"+section10+"  Section11:"+section11+"  Section12:"+section12+"  Section13:"+section13);	
		List<Double>  List_S1_info=new ArrayList();//用来存放每个区域的密度最大点和平均位置点
		List<Double>  List_S2_info=new ArrayList();
		List<Double>  List_S3_info=new ArrayList();
		List<Double>  List_S4_info=new ArrayList();
		List<Double>  List_S5_info=new ArrayList();
		List<Double>  List_S6_info=new ArrayList();
		List<Double>  List_S7_info=new ArrayList();
		List<Double>  List_S8_info=new ArrayList();
		List<Double>  List_S9_info=new ArrayList();
		List<Double>  List_S10_info=new ArrayList();
		List<Double>  List_S11_info=new ArrayList();
		List<Double>  List_S12_info=new ArrayList();
		List<Double>  List_S13_info=new ArrayList();
		Map<Integer,List> section_map_info=new HashMap<Integer,List>();
		if(List_S1.size()/(Num*2.0)<0.2){
			List_S1.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num1=List_S1.size()/2;
			double array_x[]=new double[num1];
			double array_y[]=new double[num1];
			for(int i=0,j=0;j<List_S1.size();i++,j=j+2){	
				array_x[i]=List_S1.get(j);
				array_y[i]=List_S1.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num1;k1++){
				double sum1=0;
				for(int k2=0;k2<num1;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num1;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num1;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
	        double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S1_info.add(array_x[num_min]);
	        List_S1_info.add(array_y[num_min]);
			System.out.println("Section1密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S1.size();j=j+2){
				sumx+=List_S1.get(j);
				sumy+=List_S1.get(j+1);
			}
			average_x=sumx/num1;
			average_y=sumy/num1;
			List_S1_info.add(average_x);
			List_S1_info.add(average_y);
			System.out.println("Section1平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(1, List_S1_info);
		}
		if(List_S2.size()/(Num*2.0)<0.2){
			List_S2.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num2=List_S2.size()/2;
			double array_x[]=new double[num2];
			double array_y[]=new double[num2];
			for(int i=0,j=0;j<List_S2.size();i++,j=j+2){	
				array_x[i]=List_S2.get(j);
				array_y[i]=List_S2.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num2;k1++){
				double sum1=0;
				for(int k2=0;k2<num2;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num2;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num2;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
			double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S2_info.add(array_x[num_min]);
	        List_S2_info.add(array_y[num_min]);
			System.out.println("Section2密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;	
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S2.size();j=j+2){
				sumx+=List_S2.get(j);
				sumy+=List_S2.get(j+1);
			}
			average_x=(sumx/num2);
			average_y=(sumy/num2);
			List_S2_info.add(average_x);
			List_S2_info.add(average_y);
			System.out.println("Section2平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(2, List_S2_info);
		}
		if(List_S3.size()/(Num*2.0)<0.2){
			List_S3.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num3=List_S3.size()/2;
			double array_x[]=new double[num3];
			double array_y[]=new double[num3];
			for(int i=0,j=0;j<List_S3.size();i++,j=j+2){	
				array_x[i]=List_S3.get(j);
				array_y[i]=List_S3.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num3;k1++){
				double sum1=0;
				for(int k2=0;k2<num3;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num3;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num3;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
			double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S3_info.add(array_x[num_min]);
	        List_S3_info.add(array_y[num_min]);
			System.out.println("Section3密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S3.size();j=j+2){
				sumx+=List_S3.get(j);
				sumy+=List_S3.get(j+1);
			}
			average_x=(sumx/num3);
			average_y=(sumy/num3);
			List_S3_info.add(average_x);
			List_S3_info.add(average_y);
			System.out.println("Section3平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(3, List_S3_info);
		}
		if(List_S4.size()/(Num*2.0)<0.2){
			List_S4.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num4=List_S4.size()/2;
			double array_x[]=new double[num4];
			double array_y[]=new double[num4];
			for(int i=0,j=0;j<List_S4.size();i++,j=j+2){	
				array_x[i]=List_S4.get(j);
				array_y[i]=List_S4.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num4;k1++){
				double sum1=0;
				for(int k2=0;k2<num4;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num4;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num4;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
			double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S4_info.add(array_x[num_min]);
	        List_S4_info.add(array_y[num_min]);
			System.out.println("Section4密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S4.size();j=j+2){
				sumx+=List_S4.get(j);
				sumy+=List_S4.get(j+1);
			}
			average_x=(sumx/num4);
			average_y=(sumy/num4);
			List_S4_info.add(average_x);
			List_S4_info.add(average_y);
			System.out.println("Section4平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(4, List_S4_info);	
		}
		if(List_S5.size()/(Num*2.0)<0.2){
			List_S5.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num5=List_S5.size()/2;
			double array_x[]=new double[num5];
			double array_y[]=new double[num5];
			for(int j=0,i=0;j<List_S5.size();i++,j=j+2){	
				array_x[i]=List_S5.get(j);
				array_y[i]=List_S5.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num5;k1++){
				double sum1=0;
				for(int k2=0;k2<num5;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num5;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num5;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
			double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S5_info.add(array_x[num_min]);
	        List_S5_info.add(array_y[num_min]);
			System.out.println("Section5密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S5.size();j=j+2){
				sumx+=List_S5.get(j);
				sumy+=List_S5.get(j+1);
			}
			average_x=(sumx/num5);
			average_y=(sumy/num5);
			List_S5_info.add(average_x);
			List_S5_info.add(average_y);
			System.out.println("Section5平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(5, List_S5_info);
		}
		if(List_S6.size()/(Num*2.0)<0.2){
			List_S6.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num6=List_S6.size()/2;
			double array_x[]=new double[num6];
			double array_y[]=new double[num6];
			for(int i=0,j=0;j<List_S6.size();i++,j=j+2){	
				array_x[i]=List_S6.get(j);
				array_y[i]=List_S6.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num6;k1++){
				double sum1=0;
				for(int k2=0;k2<num6;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num6;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num6;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
			double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S6_info.add(array_x[num_min]);
	        List_S6_info.add(array_y[num_min]);
			System.out.println("Section6密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;	
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S6.size();j=j+2){
				sumx+=List_S6.get(j);
				sumy+=List_S6.get(j+1);
			}
			average_x=(sumx/num6);
			average_y=(sumy/num6);
			List_S6_info.add(average_x);
			List_S6_info.add(average_y);
			System.out.println("Section6平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(6, List_S6_info);
		}
		if(List_S7.size()/(Num*2.0)<0.2){
			List_S7.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num7=List_S7.size()/2;
			double array_x[]=new double[num7];
			double array_y[]=new double[num7];
			for(int i=0,j=0;j<List_S7.size();i++,j=j+2){	
				array_x[i]=List_S7.get(j);
				array_y[i]=List_S7.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num7;k1++){
				double sum1=0;
				for(int k2=0;k2<num7;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num7;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num7;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
			double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S7_info.add(array_x[num_min]);
	        List_S7_info.add(array_y[num_min]);	        
			System.out.println("Section7密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;	
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S7.size();j=j+2){
				sumx+=List_S7.get(j);
				sumy+=List_S7.get(j+1);
			}
			average_x=(sumx/num7);
			average_y=(sumy/num7);
			List_S7_info.add(average_x);
			List_S7_info.add(average_y);
			System.out.println("Section7平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(7, List_S7_info);
		}
		if(List_S8.size()/(Num*2.0)<0.2){
			List_S8.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num8=List_S8.size()/2;
			double array_x[]=new double[num8];
			double array_y[]=new double[num8];
			for(int i=0,j=0;j<List_S8.size();i++,j=j+2){	
				array_x[i]=List_S8.get(j);
				array_y[i]=List_S8.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num8;k1++){
				double sum1=0;
				for(int k2=0;k2<num8;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num8;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num8;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
	        double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S8_info.add(array_x[num_min]);
	        List_S8_info.add(array_y[num_min]);
			System.out.println("Section1密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S8.size();j=j+2){
				sumx+=List_S8.get(j);
				sumy+=List_S8.get(j+1);
			}
			average_x=(sumx/num8);
			average_y=(sumy/num8);
			List_S8_info.add(average_x);
			List_S8_info.add(average_y);
			System.out.println("Section8平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(8, List_S8_info);
		}
		if(List_S9.size()/(Num*2.0)<0.2){
			List_S9.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num9=List_S9.size()/2;
			double array_x[]=new double[num9];
			double array_y[]=new double[num9];
			for(int i=0,j=0;j<List_S9.size();i++,j=j+2){	
				array_x[i]=List_S9.get(j);
				array_y[i]=List_S9.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num9;k1++){
				double sum1=0;
				for(int k2=0;k2<num9;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num9;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num9;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
	        double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S9_info.add(array_x[num_min]);
	        List_S9_info.add(array_y[num_min]);
			System.out.println("Section9密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S9.size();j=j+2){
				sumx+=List_S9.get(j);
				sumy+=List_S9.get(j+1);
			}
			average_x=(sumx/num9);
			average_y=(sumy/num9);
			List_S9_info.add(average_x);
			List_S9_info.add(average_y);
			System.out.println("Section9平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(9, List_S9_info);
		}
		if(List_S10.size()/(Num*2.0)<0.2){
			List_S10.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num10=List_S10.size()/2;
			double array_x[]=new double[num10];
			double array_y[]=new double[num10];
			for(int i=0,j=0;j<List_S10.size();i++,j=j+2){	
				array_x[i]=List_S10.get(j);
				array_y[i]=List_S10.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num10;k1++){
				double sum1=0;
				for(int k2=0;k2<num10;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num10;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num10;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
	        double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S10_info.add(array_x[num_min]);
	        List_S10_info.add(array_y[num_min]);
			System.out.println("Section10密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S10.size();j=j+2){
				sumx+=List_S10.get(j);
				sumy+=List_S10.get(j+1);
			}
			average_x=(sumx/num10);
			average_y=(sumy/num10);
			List_S10_info.add(average_x);
			List_S10_info.add(average_y);
			System.out.println("Section10平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(10, List_S10_info);
		}
		if(List_S11.size()/(Num*2.0)<0.2){
			List_S11.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num11=List_S11.size()/2;
			double array_x[]=new double[num11];
			double array_y[]=new double[num11];
			for(int i=0,j=0;j<List_S11.size();i++,j=j+2){	
				array_x[i]=List_S11.get(j);
				array_y[i]=List_S11.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num11;k1++){
				double sum1=0;
				for(int k2=0;k2<num11;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num11;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num11;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
	        double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S11_info.add(array_x[num_min]);
	        List_S11_info.add(array_y[num_min]);
			System.out.println("Section11密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S11.size();j=j+2){
				sumx+=List_S11.get(j);
				sumy+=List_S11.get(j+1);
			}
			average_x=(sumx/num11);
			average_y=(sumy/num11);
			List_S11_info.add(average_x);
			List_S11_info.add(average_y);
			System.out.println("Section11平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(11, List_S11_info);
		}
		if(List_S12.size()/(Num*2.0)<0.2){
			List_S12.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num12=List_S12.size()/2;
			double array_x[]=new double[num12];
			double array_y[]=new double[num12];
			for(int i=0,j=0;j<List_S12.size();i++,j=j+2){	
				array_x[i]=List_S12.get(j);
				array_y[i]=List_S12.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num12;k1++){
				double sum1=0;
				for(int k2=0;k2<num12;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num12;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num12;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
	        double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S12_info.add(array_x[num_min]);
	        List_S12_info.add(array_y[num_min]);
			System.out.println("Section12密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S12.size();j=j+2){
				sumx+=List_S12.get(j);
				sumy+=List_S12.get(j+1);
			}
			average_x=(sumx/num12);
			average_y=(sumy/num12);
			List_S12_info.add(average_x);
			List_S12_info.add(average_y);
			System.out.println("Section12平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(12, List_S12_info);
		}
		if(List_S13.size()/(Num*2.0)<0.2){
			List_S13.clear();
		}else{
			/*********每个区域内的密度最大点************/
			int num13=List_S13.size()/2;
			double array_x[]=new double[num13];
			double array_y[]=new double[num13];
			for(int i=0,j=0;j<List_S13.size();i++,j=j+2){	
				array_x[i]=List_S13.get(j);
				array_y[i]=List_S13.get(j+1);
			}
			List<Double>  list_cluster=new ArrayList<Double>();	
			for(int k1=0;k1<num13;k1++){
				double sum1=0;
				for(int k2=0;k2<num13;k2++){				
					double d=Math.sqrt((Math.pow(array_x[k1]-array_x[k2], 2)+Math.pow(array_y[k1]-array_y[k2], 2)));
					double sum2=0;
					for(int k3=0;k3<num13;k3++){
						sum2+=Math.sqrt((Math.pow(array_x[k2]-array_x[k3], 2)+Math.pow(array_y[k2]-array_y[k3], 2)));
					}
					sum1+=d/sum2;				
				}
				list_cluster.add(sum1);
			}
			double min=Collections.min(list_cluster);
			int num_min=0;
			for(int m=0;m<num13;m++){
				if(min==list_cluster.get(m)){
					num_min=m;
				}
			}
	        double aaa=array_x[num_min];
	        double bbb=array_y[num_min];
	        List_S13_info.add(array_x[num_min]);
	        List_S13_info.add(array_y[num_min]);
			System.out.println("Section13密度最大点："+aaa/1568*4126+"   "+bbb/988*2610);
			list_cluster.clear();
			array_x=null;
			array_y=null;
			/******每个区域的加权平均距离********/
			double sumx = 0,sumy = 0,average_x,average_y;
			for(int j=0;j<List_S13.size();j=j+2){
				sumx+=List_S13.get(j);
				sumy+=List_S13.get(j+1);
			}
			average_x=(sumx/num13);
			average_y=(sumy/num13);
			List_S13_info.add(average_x);
			List_S13_info.add(average_y);
			System.out.println("Section13平均定位点："+average_x/1568*4126+"   "+average_y/988*2610);
			section_map_info.put(13, List_S13_info);
		}
		
		/*******得到定位非零个数的区域section_id并且将他们按照从大到小的顺序放进list中*******/
		Map<Integer,Integer> section_map=new HashMap<Integer,Integer>();
		section_map.put(1, List_S1.size());
		section_map.put(2, List_S2.size());
		section_map.put(3, List_S3.size());
		section_map.put(4, List_S4.size());
		section_map.put(5, List_S5.size());
		section_map.put(6, List_S6.size());
		section_map.put(7, List_S7.size());
		section_map.put(8, List_S8.size());
		section_map.put(9, List_S9.size());
		section_map.put(10, List_S10.size());
		section_map.put(11, List_S11.size());
		section_map.put(12, List_S12.size());
		section_map.put(13, List_S13.size());
		List<Map.Entry<Integer, Integer>> s_list=new ArrayList<Map.Entry<Integer,Integer>>(section_map.entrySet());
		Collections.sort(s_list,new Comparator<Map.Entry<Integer,Integer>>() {
	         
	            public int compare(Entry<Integer, Integer> o1,
	                    Entry<Integer, Integer> o2) {
	                return o2.getValue().compareTo(o1.getValue());
	            }				
	        });
		 for(Map.Entry<Integer,Integer> mapping:s_list){ 
             System.out.println(mapping.getKey()+":"+mapping.getValue()); 
             if(mapping.getValue()!=0){
            	 List_section.add(mapping.getKey());
            	 List_section.add(mapping.getValue());  //List_section中放入需要操作的区域名称以及所对应的定位个数
             }           
        } 
	    /**********通过上面得到中心区域的section_id然后得到对应section的neighbour_id和sample_id以及对应的定位坐标***********/
		 int section_num=List_section.size()/2;
		 int center_id=List_section.get(0);
		 int secodn_id=0;
		 double threshold=0;
		 if(section_num>=2){
			 secodn_id=List_section.get(2);
			 threshold=(List_section.get(3)*1.0)/(List_section.get(1)*1.0);
		 }
		 List<Double>  center_info=new ArrayList();  //用来存放center区域重新knn的定位点
		 List<Integer>  center_neighbor=new ArrayList();  //用来存放center与他的邻居区域id
		 List<Double>  average_neighbor=new ArrayList();  //用来存放所有区域的平均位置
		 List<Double>  density_neighbor=new ArrayList();  //用来存放所有区域的密度最大点
		 center_neighbor=m_DatabaseManager.get_neighbor_id_in_section(center_id);
		 center_neighbor.add(center_id);
		 java.util.Iterator<Entry<Integer, List>> it=section_map_info.entrySet().iterator();
		 while(it.hasNext()){     //将与center不相邻的区域删除并且分别将平均位置和密度最大位置存储起来
			 Map.Entry entry=it.next();
			 if(center_neighbor.contains(entry.getKey())){
				 List coordinate=new ArrayList();
				 coordinate=(List) entry.getValue();
				 average_neighbor.add((Double) coordinate.get(0));
				 average_neighbor.add((Double) coordinate.get(1));
				 density_neighbor.add((Double) coordinate.get(2));
				 density_neighbor.add((Double) coordinate.get(3));
			 }else{
				 it.remove();
			 }
		 }
		 if(threshold<0.7){
		    java.util.Iterator<Byte> iter_ssid=wifi_ssid.keySet().iterator();
		    java.util.Iterator<Byte> iter_up= wifi_up.keySet().iterator();
		    while(iter_ssid.hasNext()&&iter_up.hasNext()){
			    Byte count1=iter_ssid.next();
			    Byte count2=iter_up.next();
			    List<Integer> ok_sample_id=m_Algorithm.find_right_sample_ok(wifi_ssid.get(count1), wifi_up.get(count2),center_id, m_DatabaseManager);
	    	    Map<String,Double> coordinate=m_Algorithm.find_coordinate(ok_sample_id,wifi_ssid.get(count1),wifi_up.get(count2),m_DatabaseManager); 
	    	    double change_x=coordinate.get("x")*1568/1000.0;
	    	    double change_y=coordinate.get("y")*988/1000.0;
	    	    center_info.add(change_x);
	    	    center_info.add(change_y);
		 }		
		   double X_sum=0,Y_sum=0;
		   for(int i=0;i<average_neighbor.size();i=i+2){
			   X_sum+=average_neighbor.get(i);
			   Y_sum+=average_neighbor.get(i+1);			   
		   }
		   for(int i=0;i<density_neighbor.size();i=i+2){
			   X_sum+=density_neighbor.get(i);
			   Y_sum+=density_neighbor.get(i+1);			   
		   }
		   for(int i=0;i<center_info.size();i=i+2){
			   X_sum+=center_info.get(i);
			   Y_sum+=center_info.get(i+1);			   
		   }
		   int NUM=(average_neighbor.size()+density_neighbor.size()+center_info.size())/2;
		 
		   X=X_sum/NUM;
		  
		   Y=Y_sum/NUM;
				   
		 }else{
			    java.util.Iterator<Byte> iter_ssid=wifi_ssid.keySet().iterator();
			    java.util.Iterator<Byte> iter_up= wifi_up.keySet().iterator();
			    while(iter_ssid.hasNext()&&iter_up.hasNext()){
				    Byte count1=iter_ssid.next();
				    Byte count2=iter_up.next();
				    List<Integer> ok_sample_id=m_Algorithm.find_right_sample_ok_2(wifi_ssid.get(count1), wifi_up.get(count2),center_id,secodn_id, m_DatabaseManager);
		    	    Map<String,Double> coordinate=m_Algorithm.find_coordinate(ok_sample_id,wifi_ssid.get(count1),wifi_up.get(count2),m_DatabaseManager); 
		    	    double change_x=coordinate.get("x")*1568/1000.0;
		    	    double change_y=coordinate.get("y")*988/1000.0;
		    	    center_info.add(change_x);
		    	    center_info.add(change_y);
			 }		
			   double X_sum=0,Y_sum=0;
			   for(int i=0;i<average_neighbor.size();i=i+2){
				   X_sum+=average_neighbor.get(i);
				   Y_sum+=average_neighbor.get(i+1);			   
			   }
			   for(int i=0;i<density_neighbor.size();i=i+2){
				   X_sum+=density_neighbor.get(i);
				   Y_sum+=density_neighbor.get(i+1);			   
			   }
			   for(int i=0;i<center_info.size();i=i+2){
				   X_sum+=center_info.get(i);
				   Y_sum+=center_info.get(i+1);			   
			   }
			   int NUM=(average_neighbor.size()+density_neighbor.size()+center_info.size())/2;
			 
			   X=X_sum/NUM;
			  
			   Y=Y_sum/NUM;
		 }
		/**************************************************/
		System.out.println("最后定为点为X："+X+"     "+Y);
		FileWriter fw;
		try {
			
	
			fw = new FileWriter("F:" +File.separator+ "workspace" + File.separator+"location.txt",true);						
			fw.append("		"+X+"			"+Y+"		\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/******************************************************/
		double xx=X*1000/1568.0;
		double yy=Y*1000/988.0;
		List<Double> List_XY=new ArrayList();
		List_XY.add(xx);
		List_XY.add(yy);
		return List_XY;
		
	}

}
