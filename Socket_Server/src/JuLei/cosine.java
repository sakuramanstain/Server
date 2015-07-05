package JuLei;

import java.util.ArrayList;


public class cosine extends Distance {
	@Override
	public double getDistance(ArrayList<Double> p1, ArrayList<Double> p2){
		double result;
		int denominator = 0;
		int numeratorP1 = 0, numeratorP2 = 0;
		double modeP1 = 0, modeP2 = 0;
		
		for(int i = 0; i < p1.size(); i++){
			denominator += p1.get(i) * p2.get(i);
			numeratorP1 += Math.pow(p1.get(i), 2);
			numeratorP2 += Math.pow(p2.get(i), 2);
		}
		
		modeP1 = Math.sqrt(numeratorP1);
		modeP2 = Math.sqrt(numeratorP2);
		result = denominator / (modeP1 * modeP2);
		return result;
	}


}
