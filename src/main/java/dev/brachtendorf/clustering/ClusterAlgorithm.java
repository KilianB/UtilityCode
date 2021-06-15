package dev.brachtendorf.clustering;

/**
 * @author Kilian
 *
 */
public interface ClusterAlgorithm {

	ClusterResult cluster(double[][] data);

}