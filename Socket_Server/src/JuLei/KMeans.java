package JuLei;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class KMeans {
	//============ k-means method =====================
		public ArrayList<ArrayList<ArrayList<Double>>> 
		kMeans(ArrayList<ArrayList<Double>> dataSet, int maxIter, 
			   Distance dist, int k, InitCentroid randCentroid){
			
			ArrayList<ArrayList<ArrayList<Double>>> Ans=new  ArrayList<ArrayList<ArrayList<Double>>>();
			ArrayList<ArrayList<Double>> centroids = randCentroid.randCentroid(dataSet, k);// init centroids for k clusters	
//			ArrayList<ArrayList<Double>> centroids = randCentroid.randCentroid(k, dataSet, dist);// init centroids for k clusters	
			ArrayList<Cluster> clusters = new ArrayList<Cluster>();// put clusters object into clusters
			
			// create k clusters
			int i = 0;
			while(i < k){
				clusters.add(new Cluster());
				i++;
			}
			for(int j = 0; j < maxIter; j++){
//				System.out.println("======================" + (j+1) + "===================\n");
				int minDisIndex = 0; 				 // index of cluster which has farthest distance with point
				double minDis;        		      // min distance	
													 
				for(int l = 0; l < dataSet.size(); l++){
					minDis = dist.getDistance(dataSet.get(l), centroids.get(0));
					for(int m = 0; m < k; m++){
						double distance = dist.getDistance(dataSet.get(l), centroids.get(m));
						if(distance <= minDis){
							minDis = distance;
							minDisIndex = m;
						}
					}
					clusters.get(minDisIndex).getCluster().add(dataSet.get(l)); // add point to cluster
				}
				
				// print all clusters
//				for(int h = 0; h < k; h++){
//					System.out.println("Cluster" + (h+1) + clusters.get(h).getCluster());
//				
//					}								
//				System.out.println("centroids: " + centroids);//print all centroids for all clusters				
				centroids.clear();// clear all centroids
				// update centroids for all clusters
				int cNum = 0;
				while(cNum < k){
					centroids.add(updateCentroid(clusters.get(cNum).getCluster()));
					cNum++;
				}
				// clear all clusters
				if(j<maxIter-1){
					for(int p = 0; p < k; p++){
						clusters.get(p).getCluster().clear();
					}
				}
				if(j==maxIter-1){
				for(int h = 0; h < k; h++){					
					Ans.add(clusters.get(h).getCluster());
					}
				}
			}	
			return Ans;
		}
		
		
		
		
		
		
		//======== update centroid of each cluster dataset ==========
		public static ArrayList<Double> updateCentroid(ArrayList<ArrayList<Double>> clusterData){
			double sum;
			
			DecimalFormat df = new DecimalFormat("0.00");
			df.setRoundingMode(RoundingMode.HALF_UP); 
			
			ArrayList<Double> avgCentroid = new ArrayList<Double>();
			
			if(clusterData.size() > 0){
				for(int i = 0; i < clusterData.get(0).size(); i++){
					sum = 0;
					for(int j = 0; j < clusterData.size(); j++){
						sum += clusterData.get(j).get(i);
					}
					double avg = sum / clusterData.size() ;
					avgCentroid.add(Double.parseDouble(df.format(avg))); // reserve two decimals for centroids(convert needed)
				}
			}
			return avgCentroid;
		}


}
