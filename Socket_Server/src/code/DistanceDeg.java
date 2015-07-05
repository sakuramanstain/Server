package code;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DistanceDeg extends Distance {

	@Override
	double distance(Map<String, Integer> first, Map<String, Integer> second) {
		// TODO Auto-generated method stub
		
        
        ///////
       
       	
       	 List<String> first_temp= new ArrayList<String>();
       	 Iterator<Entry<String, Integer>> iter = first.entrySet().iterator(); 
       	 while(iter.hasNext()){ 
       	  Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
       	  first_temp.add((String) entry.getKey()); 
       	 } 
       	 List<String> second_temp= new ArrayList<String>();
       	 iter = second.entrySet().iterator(); 
      	 while(iter.hasNext()){ 
      	 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
      	 second_temp.add((String) entry.getKey()); 
      	 } 
       	 
      	first_temp.retainAll(second_temp);
       	 ////////////////
       	 if(first_temp.size()>=1)
       	 {
       		 
       		double distance=0;
       		double x1=0;
       		double x2=0;
       		for(String t:first_temp)
       		{
       		  distance=distance+first.get(t)*second.get(t);
       		  x1+=Math.pow(first.get(t),2);
       		  x2+=Math.pow(second.get(t), 2);
       		}
       		double cos=distance/(Math.sqrt(x1)*Math.sqrt(x2))-1;
           	return -cos*1000;
          	 
       		
       	 }
       	 else
       	 {
   ////////////TODO
       		return Double.MAX_VALUE;
           	
       	 }
		
	}

	@Override
	double distance_double(Map<String, Double> first,
			Map<String, Integer> second) {
		// TODO Auto-generated method stub
		
        
        ///////
       
       	
       	 List<String> first_temp= new ArrayList<String>();
       	 Iterator<Entry<String, Double>> iter = first.entrySet().iterator(); 
       	 while(iter.hasNext()){ 
       	  Map.Entry<String, Double> entry = (Map.Entry<String, Double>)iter.next();
       	  first_temp.add((String) entry.getKey()); 
       	 } 
       	 List<String> second_temp= new ArrayList<String>();
       	Iterator<Entry<String, Integer>> iter2 = second.entrySet().iterator(); 
      	 while(iter2.hasNext()){ 
      	 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter2.next();
      	 second_temp.add((String) entry.getKey()); 
      	 } 
       	 
      	first_temp.retainAll(second_temp);
       	 ////////////////
       	 if(first_temp.size()>=1)
       	 {
       		 
       		double distance=0;
        	double x1=0;
       		double x2=0;
       		for(String t:first_temp)
       		{
       		  distance=distance+first.get(t)*second.get(t);
       		  x1+=Math.pow(first.get(t),2);
       		  x2+=Math.pow(second.get(t), 2);
       		}
       		double cos=distance/(Math.sqrt(x1)*Math.sqrt(x2))-1;
           	return -cos*1000;
       		
       	 }
       	 else
       	 {
   ////////////TODO
       		return Double.MAX_VALUE;
           	
       	 }
		
	}

	@Override
	double distance_double(Map<String, Double> first,
			Map<String, Integer> second, List<String> restricted_the_first) {
		// TODO Auto-generated method stub
		
        
        ///////
		List<String> temp_array= new ArrayList<String>();///簇中wifi与采样点的交集
   	 	temp_array.addAll(restricted_the_first);
   	 	
   	 	
       	 List<String> first_temp= new ArrayList<String>();
       	 Iterator<Entry<String, Double>> iter = first.entrySet().iterator(); 
       	 while(iter.hasNext()){ 
       	  Map.Entry<String, Double> entry = (Map.Entry<String, Double>)iter.next();
       	  first_temp.add((String) entry.getKey()); 
       	 } 
       	 temp_array.retainAll(first_temp);
       	/* List<String> second_temp= new ArrayList<String>();
       	Iterator<Entry<String, Integer>> iter2 = second.entrySet().iterator(); 
      	 while(iter.hasNext()){ 
      	 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter2.next();
      	 second_temp.add((String) entry.getKey()); 
      	 } 
       	 
      	first_temp.retainAll(second_temp);*/
       	 ////////////////
       	 if(temp_array.size()>=1)
       	 {
       		 
       		double distance=0;
       		
        	double x1=0;
       		double x2=0;
       		for(String t:temp_array)
       		{
       		  distance=distance+first.get(t)*second.get(t);
       		  x1+=Math.pow(first.get(t),2);
       		  x2+=Math.pow(second.get(t), 2);
       		}
       		double cos=distance/(Math.sqrt(x1)*Math.sqrt(x2))-1;
           	return -cos*1000;
       		
       	 }
       	 else
       	 {
   ////////////TODO
       		return Double.MAX_VALUE;
           	
       	 }
		
	}

	@Override
	double distance(Map<String, Integer> first, Map<String, Integer> second,
			List<String> restricted_the_first) {
		// TODO Auto-generated method stub
		
        
        ///////
		List<String> temp_array= new ArrayList<String>();///簇中wifi与采样点的交集
   	 	temp_array.addAll(restricted_the_first);
   	 	
   	 	
       	 List<String> first_temp= new ArrayList<String>();
       	 Iterator<Entry<String, Integer>> iter = first.entrySet().iterator(); 
       	 while(iter.hasNext()){ 
       	  Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
       	  first_temp.add((String) entry.getKey()); 
       	 } 
       	 temp_array.retainAll(first_temp);
       	/* List<String> second_temp= new ArrayList<String>();
       	Iterator<Entry<String, Integer>> iter2 = second.entrySet().iterator(); 
      	 while(iter.hasNext()){ 
      	 Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter2.next();
      	 second_temp.add((String) entry.getKey()); 
      	 } 
       	 
      	first_temp.retainAll(second_temp);*/
       	 ////////////////
       	 if(temp_array.size()>=1)
       	 {
       		 
       		double distance=0;
       		double x1=0;
       		double x2=0;
       		for(String t:temp_array)
       		{
       		  distance=distance+first.get(t)*second.get(t);
       		  x1+=Math.pow(first.get(t),2);
       		  x2+=Math.pow(second.get(t), 2);
       		}
       		double cos=distance/(Math.sqrt(x1)*Math.sqrt(x2))-1;
           	return -cos*1000;
          	 
       		
       	 }
       	 else
       	 {
   ////////////TODO
       		return Double.MAX_VALUE;
           	
       	 }
		
	}

}
