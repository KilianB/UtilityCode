package com.github.kilianB.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.github.kilianB.ArrayUtil;
import com.github.kilianB.clustering.distance.DistanceFunction;

/**
 * @author Kilian
 *
 */
public class OPTICS implements ClusterAlgorithm{

	//Ordering Points To Identify the Clustering Structure
	
	DistanceFunction dist;
	
	double eps;
	
	public OPTICS() {
		
	}
	
	@Override
	public int[] cluster(double[][] data) {
		
		
		// -2  unprocessed.  -1 noise
		int cluster[] = new int[data.length];
		ArrayUtil.fillArray(cluster,() -> {return -2;});
		
		double[] distance = new double[data.length];


		for(int i = 0; i < data.length; i++) {
			
			//Point already processed
			if(cluster[i] != -2){
				continue;
			}
			
			
			//Get Neightboors
			List<Integer> neighboor = new ArrayList<>();
			
			for(int j = 0; j < data.length; j++) {
				if(i != j) {
					if(dist.distance(data[i],data[j]) <= eps) {
						neighboor.add(j);
					}
				}
			}
			
			//Set Processed
			cluster[i] = -1;
		
			PriorityQueue seed = new PriorityQueue();
			
			
			
		}
		
		
		return null;
	}
	
	
	/*
	 OPTICS(DB, eps, MinPts)
	    for each point p of DB
	       p.reachability-distance = UNDEFINED
	    for each unprocessed point p of DB
	       N = getNeighbors(p, eps)
	       mark p as processed
	       output p to the ordered list
	       Seeds = empty priority queue
	       if (core-distance(p, eps, Minpts) != UNDEFINED)
	          update(N, p, Seeds, eps, Minpts)
	          for each next q in Seeds
	             N' = getNeighbors(q, eps)
	             mark q as processed
	             output q to the ordered list
	             if (core-distance(q, eps, Minpts) != UNDEFINED)
	                update(N', q, Seeds, eps, Minpts)
	*/
}
