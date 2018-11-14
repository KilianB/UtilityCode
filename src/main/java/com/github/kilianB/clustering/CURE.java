package com.github.kilianB.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;

import com.github.kilianB.ArrayUtil;
import com.github.kilianB.Require;
import com.github.kilianB.clustering.distance.DistanceFunction;
import com.github.kilianB.clustering.distance.EuclideanDistance;
import com.github.kilianB.pcg.fast.PcgRSFast;

/**
 * Clustering Using REpresentatives
 * 
 * @author Kilian
 */
public class CURE implements ClusterAlgorithm {

	ClusterAlgorithm initialClusterSupplier;

	DistanceFunction d = new EuclideanDistance();

	int k;

	double alpha;

	// https://www.youtube.com/watch?v=JrOJspZ1CUw

	// TODO optimization kd-trees
	// TODO Optimization random sampling

	public CURE(ClusterAlgorithm initialClusterSupplier, int k, double alpha) {
		k = Require.positiveValue(k, "k has to be positive");
		this.alpha = (double) Require.inRange(alpha, 0, 1, "alpha has to be between 0 and 1");
		this.initialClusterSupplier = initialClusterSupplier;
	}

	@Override
	public ClusterResult cluster(double[][] data) {
		Random rng = new PcgRSFast();

		// Pick k points as disperese as possible
		ClusterResult cRes = initialClusterSupplier.cluster(data);

		List<List<double[]>> syntheticPoints = new ArrayList<>();

		//TODO draw selected points. and modified points
		BufferedImage bi = new BufferedImage();
		
		for (int i = 0; i < cRes.numberOfClusters; i++) {

			// Pick random point of the cluster
			List<double[]> pointsInCluster = cRes.getCluster(i);

			List<double[]> synPoints = new ArrayList<double[]>();

			// Add the first point randomly

			double[] firstP = pointsInCluster.get(rng.nextInt(pointsInCluster.size()));
			double copy[] = new double[firstP.length];
			for (int m = 0; m < firstP.length; m++) {
				copy[m] = firstP[m];
			}
			synPoints.add(copy);

			// find k-1 more points which cover the greatest possible distance

			for (int j = 1; j < k; j++) {
				double maxDistance = -Double.MAX_VALUE;
				double[] newP = null;
				for (double[] p : pointsInCluster) {
					double dist = 0;

					for (double[] sPoints : synPoints) {
						dist += d.distance(sPoints, p);
					}
					if (dist > maxDistance) {
						maxDistance = dist;

						// Deep clone
						newP = new double[p.length];
						for (int m = 0; m < p.length; m++) {
							newP[m] = p[m];
						}
					}
				}
				synPoints.add(newP);
			}

			DoubleSummaryStatistics[] centeroid = cRes.getStats(i);

			// Move each point alpha fraction to the centeroid
			for (double[] synPoint : synPoints) {
				for (int j = 0; j < synPoint.length; j++) {
					synPoint[j] += (centeroid[j].getAverage() - synPoint[j]) * alpha;
				}
			}
			syntheticPoints.add(synPoints);
		}

		// Step 2
		int[] cluster = new int[data.length];

		for (int i = 0; i < data.length; i++) {
			double[] point = data[i];
			// Find the closest of the synthetic points and assign it to this cluster.
			int bestCluster = -1;
			double minDistance = Double.MAX_VALUE;

			for (int clusterIndex = 0; clusterIndex < cRes.numberOfClusters; clusterIndex++) {

				List<double[]> synPointsInCluster = syntheticPoints.get(clusterIndex);

				for (double[] p : synPointsInCluster) {
					double dist = d.distance(p, point);
					if (dist < minDistance) {
						minDistance = dist;
						bestCluster = clusterIndex;
					}
				}

			}
			cluster[i] = bestCluster;
		}
		return new ClusterResult(cluster, data);
	}

}
