package code;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class DistanceAbsolute extends Distance {

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
       		for(String t:first_temp)
       		{
       		  distance=distance+Math.abs(first.get(t)-second.get(t));
       		}
           	 distance=distance/(first_temp.size());
           	return distance;
          	 
       		
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
       
		/*DatabaseManager mdb=new DatabaseManager();
  		 mdb.Initial();
  		 */
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
       		for(String t:first_temp)
       		{
       			//Double weight=mdb.get_weight(t);
       		 // distance=distance+(1/weight)*Math.abs(first.get(t)-second.get(t));
       		 distance=distance+Math.abs(first.get(t)-second.get(t));
       		}
           	 //
       		distance=distance*Math.pow(second_temp.size(), 3)/Math.pow(first_temp.size(), 4);
        	//distance=distance/(first_temp.size());
           	return distance;
          	 
       		
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
		List<String> temp_array= new ArrayList<String>();///����wifi�������Ľ���
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
       		for(String t:temp_array)
       		{
       		  distance=distance+Math.abs(first.get(t)-second.get(t));
       		}
       		
           	distance=distance/(temp_array.size());
           	return distance;
          	 
       		
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
		

		// TODO Auto-generated method stub
		
        
        ///////
		List<String> temp_array= new ArrayList<String>();///����wifi�������Ľ���
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
       		for(String t:temp_array)
       		{
       		  distance=distance+Math.abs(first.get(t)-second.get(t));
       		}
       		
           	distance=distance/(temp_array.size());
           	return distance;
          	 
       		
       	 }
       	 else
       	 {
   ////////////TODO
       		return Double.MAX_VALUE;
           	
       	 }
		
	
	}
	/*double distance_ap_double(Map<String, Double> first,
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
       		DatabaseManager mdb=new DatabaseManager(); 
       		double distance=0;
       		for(String t:first_temp)
       		{ Double weight=mdb.get_weight(t);
       		  distance=distance+(1/weight)*Math.abs(first.get(t)-second.get(t));
       		}
           	 
        	distance=distance/(first_temp.size());
           	return distance;
          	 
       		
       	 }
       	 else
       	 {
   ////////////TODO
       		return Double.MAX_VALUE;
           	
       	 }
		
	}*/

}