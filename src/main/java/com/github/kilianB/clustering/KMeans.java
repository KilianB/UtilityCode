package com.github.kilianB.clustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.DoubleSummaryStatistics;
import java.util.Random;

import com.github.kilianB.ArrayUtil;
import com.github.kilianB.clustering.distance.DistanceFunction;
import com.github.kilianB.clustering.distance.EuclideanDistance;
import com.github.kilianB.pcg.fast.PcgRSFast;

/**
 * @author Kilian
 *
 */
public class KMeans implements ClusterAlgorithm {

	/**
	 * The number of cluster the data will be partitioned into
	 */
	protected int k;

	/**
	 * Function to calculate distance between individual data points
	 */
	protected DistanceFunction distanceFunction;

	/**
	 * Create a KMeans clusterer with k clusters and EuclideanDistance.
	 * 
	 * @param clusters the number of cluster to partition the data into
	 */
	public KMeans(int clusters) {
		this(clusters, new EuclideanDistance());
	}

	/**
	 * Create a KMeans clusterer
	 * 
	 * @param clusters         the number of cluster to partition the data into
	 * @param distanceFunction the distanceFunction used to compute the distance
	 *                         between data points
	 */
	public KMeans(int clusters, DistanceFunction distanceFunction) {
		this.k = clusters;
		this.distanceFunction = distanceFunction;
	}

	@Override
	public ClusterResult cluster(double[][] data) {

		int[] cluster = new int[data.length];

		// If only one cluster is available return an array indicating all data
		// belonging to this one cluster
		if (k == 1) {
			ArrayUtil.fillArray(cluster, () -> {
				return 0;
			});
			return new ClusterResult(cluster, data);
		} else if (k >= data.length) {
			throw new IllegalArgumentException("Can't compute more clusters than datapoints are present");
		}
		// How many dimension does each datapoint have?
		int dataDimension = data[0].length;

		// 0 = choose random start clusters
		DoubleSummaryStatistics[][] clusterMeans = computeStartingClusters(data, k, dataDimension);

		for (int clusterIndex = 0; clusterIndex < k; clusterIndex++) {
			double[] means = new double[dataDimension];
			for (int i = 0; i < dataDimension; i++) {
				means[i] = clusterMeans[clusterIndex][i].getAverage();
			}
		}
		// Iteratively improve clusters
		computeKMeans(clusterMeans, data, cluster, dataDimension);

		return new ClusterResult(cluster, data);
	}

	protected DoubleSummaryStatistics[][] computeStartingClusters(double[][] data, int k, int dataDimension) {

		// Fast high quality rng
		Random rng = new PcgRSFast();

		double[][] range = new double[data.length][2];
		DoubleSummaryStatistics[][] clusterMeans = new DoubleSummaryStatistics[k][dataDimension];

		for (double[] arr : range) {
			arr[0] = Double.MAX_VALUE;
			arr[1] = -Double.MAX_VALUE;
		}

		ArrayUtil.fillArrayMulti(clusterMeans, () -> {
			return new DoubleSummaryStatistics();
		});

		// 0.1 find a minimum and maximum of each variable domain
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < dataDimension; j++) {
				double value = data[i][j];
				// Minimum value
				if (value < range[i][0]) {
					range[i][0] = value;
				}
				// Maximum value
				if (value > range[i][1]) {
					range[i][1] = value;
				}
			}
		}

		// We don't choose a random location we choose a random point. To get a location
		// we need min and max by itterating over the entire set.
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < dataDimension; j++) {
				// compute a random cluster point within the min and max of this variable
				clusterMeans[i][j].accept((rng.nextDouble() * (range[j][1] - range[j][0])) + range[j][0]);
			}
		}

		return clusterMeans;
	}

	protected void computeKMeans(DoubleSummaryStatistics[][] clusterMeans, double[][] data, int[] cluster,
			int dataDimension) {
		boolean dirty = false;
		do {
			dirty = false;
			for (int dataIndex = 0; dataIndex < data.length; dataIndex++) {

				double minDistance = Double.MAX_VALUE;
				int bestCluster = -1;

				for (int clusterIndex = 0; clusterIndex < k; clusterIndex++) {

					double distToCluster = distanceFunction.distance(clusterMeans[clusterIndex], data[dataIndex]);
					if (distToCluster < minDistance) {
						bestCluster = clusterIndex;
						minDistance = distToCluster;
					}
				}

				if (cluster[dataIndex] != bestCluster) {
					cluster[dataIndex] = bestCluster;
					dirty = true;
				}
			}

			if (dirty) {
				// recompute cluster means

				// Reset
				ArrayUtil.fillArrayMulti(clusterMeans, () -> {
					return new DoubleSummaryStatistics();
				});
				for (int dataIndex = 0; dataIndex < data.length; dataIndex++) {
					double dat[] = data[dataIndex];
					DoubleSummaryStatistics[] clusterTemp = clusterMeans[cluster[dataIndex]];
					for (int i = 0; i < dataDimension; i++) {
						clusterTemp[i].accept(dat[i]);
					}
				}
			}
		} while (dirty);
	}

	public static void main(String[] args) {

//		ClusterAlgorithm kmeans = new KMeans(2);
//
//		double[][] data = { { 1d, 2d, 3d, 4d }, { 1d, 6d, 8d, 8d }, { 1d, 2d, 3d, 3d }, { 2d, 4d, 5d, 5d },
//				{ 4d, 7d, 8d, 7d }, { 7d, 6d, 8d, 9d }, { 4d, 4d, 3d, 3d }, { 2d, 2d, 5d, 5d }, { 7d, 5d, 5d, 5d },
//				{ 5d, 6d, 8d, 9d } };
//
//		kmeans.cluster(data);
//		kmeans.cluster(data);
//		kmeans.cluster(data);
//		kmeans.cluster(data);
//		kmeans.cluster(data);
//		kmeans.cluster(data);
//		kmeans.cluster(data);
//		kmeans.cluster(data).printInformation();;

		File f = new File("s1.txt");
		try {

			double[][] data = new double[5000][2];
			BufferedReader br = new BufferedReader(new FileReader(f));

			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				String[] raw = line.split("\t");
				data[i][0] = Integer.parseInt(raw[1]);
				data[i][1] = Integer.parseInt(raw[2]);
				i++;
			}

			ClusterAlgorithm cluster = new KMeans(15);

			double smallestError = Double.MAX_VALUE;
			ClusterResult worst = null;
			ClusterResult best = null;
			double highestError = -Double.MAX_VALUE;

			long start = System.currentTimeMillis();
			
			for (i = 0; i < 10; i++) {
				ClusterResult cResult = cluster.cluster(data);
				System.out.println("Error: " + cResult.getSumSquaredError());
				if (cResult.getSumSquaredError() < smallestError) {
					smallestError = cResult.getSumSquaredError();
					best = cResult;
				}
				if (cResult.getSumSquaredError() > highestError) {
					highestError = cResult.getSumSquaredError();
					worst = cResult;
				}
			}

			System.out.println(System.currentTimeMillis() - start);
			
			System.out.println("\nBest: " + smallestError);
			System.out.println("Worst: " + highestError + "\n");

			smallestError = Double.MAX_VALUE;
			highestError = -Double.MAX_VALUE;

			best.toImage(new File("KMeansBest.png"));
			worst.toImage(new File("KMeansWorst.png"));

			cluster = new KMeansPlusPlus(15);

			start = System.currentTimeMillis();

			for (i = 0; i < 10; i++) {
				ClusterResult cResult = cluster.cluster(data);
				System.out.println("Error: " + cResult.getSumSquaredError());
				if (cResult.getSumSquaredError() < smallestError) {
					smallestError = cResult.getSumSquaredError();
					best = cResult;
				}
				if (cResult.getSumSquaredError() > highestError) {
					highestError = cResult.getSumSquaredError();
					worst = cResult;
				}
			}
			
			System.out.println(System.currentTimeMillis() - start);
			

			System.out.println("\nBest: " + smallestError);
			System.out.println("Worst: " + highestError + "\n");

			best.toImage(new File("KMeans++Best.png"));
			worst.toImage(new File("KMeans++Worst.png"));

			cluster = new DBScan(50, 50000);

			smallestError = Double.MAX_VALUE;
			highestError = -Double.MAX_VALUE;

			ClusterResult cResult = cluster.cluster(data);

			System.out.println("DBScan: " + cResult.getSumSquaredError());
			cResult.toImage(new File("DBScan.png"));

			cResult.printInformation();
//			
			cResult = new CURE(new KMeans(15), 5, 0.4d).cluster(data);
			System.out.println("CURE KMeans: " + cResult.getSumSquaredError());
			cResult.toImage(new File("CureKMeans.png"));

			cResult = new CURE(new DBScan(50, 5000), 5, 0.4d).cluster(data);
			System.out.println("CURE DBScan: " + cResult.getSumSquaredError());
			cResult.toImage(new File("CureDBSCan.png"));
//			
//			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
