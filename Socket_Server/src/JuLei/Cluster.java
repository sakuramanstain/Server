package JuLei;

import java.util.ArrayList;


public class Cluster {
	// each cluster has cluster Data
		private ArrayList<ArrayList<Double>> clusterData;
		
		Cluster(){
			clusterData = new ArrayList<ArrayList<Double>>();
		}
		
		// get cluster data
		public ArrayList<ArrayList<Double>> getCluster(){
			return this.clusterData;
		}

}
