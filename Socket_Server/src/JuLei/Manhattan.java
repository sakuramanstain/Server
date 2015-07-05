package JuLei;

import java.util.ArrayList;


public class Manhattan extends Distance {
	@Override
	public double getDistance(ArrayList<Double> p1, ArrayList<Double> p2){
		double sum = 0;
		int i = 0;
		
		while(i < p1.size()){
			double tmp = Math.abs(p1.get(i) - p2.get(i));
			sum += tmp;
			i++;
		}
		return sum;
	}


}
