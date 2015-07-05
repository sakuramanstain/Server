package code;
import java.util.List;
import java.util.Map;


public abstract class Distance {
	abstract double distance(Map<String,Integer> first,Map<String,Integer> second);
	abstract double distance(Map<String, Integer> first,
			Map<String, Integer> second, List<String> restricted_the_first) ;
	abstract double distance_double(Map<String,Double> first,Map<String,Integer> second);
	abstract double distance_double(Map<String,Double> first,Map<String,Integer> second,List<String> restricted_the_first);
}
