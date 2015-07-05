package JuLei;

import java.util.ArrayList;


public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Distance d;
		KMeans k = new KMeans();
		ReadFile rf = new ReadFile();
		InitCentroid init = new InitCentroid();
		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
		
		// import data file
		data = rf.addFile();
	   
	    d = new Euclidean();
	    int maxIteration = 1000;
	    int numCluster = 5;
	    
	    k.kMeans(data, maxIteration, d, numCluster, init);

	}

}
