package JuLei;

import java.util.ArrayList;
import java.util.Random;


public class InitCentroid {
	public ArrayList<ArrayList<Double>> randCentroid(ArrayList<ArrayList<Double>> dataSet, int k){
		ArrayList<ArrayList<Double>> centroids = new ArrayList<ArrayList<Double>>();	
		ArrayList<Integer> centroidsIndex = new ArrayList<Integer>();					
		boolean isCentroid;		     
		Random rand = new Random(); 
		int index = rand.nextInt(dataSet.size());
		centroidsIndex.add(index);
		centroids.add(dataSet.get(index));
		
		while(centroidsIndex.size() < k){
			isCentroid = false;
			index = rand.nextInt(dataSet.size() - 1);
			
			// if this point has been selected, mark this point as a centroid
			for(int i = 0; i < centroidsIndex.size(); i++){
				if(index == centroidsIndex.get(i))	isCentroid = true;
			}
			
			if(isCentroid == false){
				centroidsIndex.add(index);
				centroids.add(dataSet.get(index));
			}
		}
		return centroids;
	}
	// ================= Farthest-first centroids ========================
	public ArrayList<ArrayList<Double>> randCentroid(int k, ArrayList<ArrayList<Double>> dataSet, Distance dist){
		ArrayList<ArrayList<Double>> centroids = new ArrayList<ArrayList<Double>>();
		Random rand = new Random();
		int index = rand.nextInt(dataSet.size());
		centroids.add(dataSet.get(index));
		for(int i = 0; i < k-1; i++){
			double max = 0;
			for(int j = 0; j < dataSet.size(); j++){
				double distance = 0;
				if(j != index){
					for(int m = 0; m < centroids.size(); m++){
						distance += dist.getDistance(centroids.get(m), dataSet.get(j));
					}
				}
				
				if(distance >= max){
					max = distance;
					index = j;
				}	
			}
			centroids.add(dataSet.get(index));
		}
		return centroids;
	}


}
