package fr.inria.maestro.lga.clustering.analysis.impl;

/*
 * Initial Software by Marina Sokol and Alexey Mishenin,
 * Copyright © Inria (MAESTRO research team), All Rights Reserved, 2009-2014.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

import com.google.common.collect.Sets;
import fr.inria.maestro.lga.clustering.ICluster;
import fr.inria.maestro.lga.clustering.IClustering;
import fr.inria.maestro.lga.clustering.analysis.IClassificationQuantuty;
import fr.inria.maestro.lga.clustering.impl.ClusteringBuilder;
import fr.inria.maestro.lga.utils.collection.CollectionUtils;
import fr.inria.maestro.lga.utils.collection.Pair;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;

/**
 * This class compare two classifications with the same names and number of clusters.
 * If two files contains the same name of the instance in the right cluster
 * they will be counted as right answer.
 *
 * Example:
 * String_from_the_first_classification_file:
 * The_same_name_of_the_instance '\t' property1 '\t' property2
 * String_from_the_second_classification_file:
 * The_same_name_of_the_instance
 * So, the instance The_same_name_of_the_instance will be counted as right true positive answer. 
 */
public class PrecisionQuantity implements IClassificationQuantuty {
    private final ClusteringPreprocessor expertFormingClustering;

    public PrecisionQuantity(final ClusteringPreprocessor expertFormingCluster) {
        this.expertFormingClustering = expertFormingCluster;
    }

    /**
     * Returns the precision of classification.
     * @param clustering - the classification.
     * @return the precision
     */
    @Override
    public double getQuantity(IClustering clustering) {
        return getPrecisionRecall(new ClusteringPreprocessor(clustering)).getFirst();
    }

    /*
      * Calculate precision and recall.
      * Use it in case if you have an expert collection for the whole graph and
      * classification by algorithm for the same graph.
     */

    public Pair<Double, Double> getPrecisionRecall(final ClusteringPreprocessor analysisClustering) {
        final Set<String> expertClusterNames = Sets.newHashSet();
        final Set<String> expertClusterEntries = Sets.newHashSet();

        for (final ICluster cluster : expertFormingClustering.getClustering().getClusters()) {
            expertClusterNames.add(cluster.getName());
            expertClusterEntries.addAll(cluster.getEntriesNames());

            final ICluster judgementCluster = analysisClustering.getClusterByName(cluster.getName());
            if (judgementCluster == null) {
                throw new IllegalArgumentException("can't find analog for cluster: " + cluster.getName());
            }
        }

        int tp = 0;
        int fp = 0;
        int fn = 0;

        for (final ICluster cluster : expertFormingClustering.getClustering().getClusters()) {
            final Set<String> expertDocuments = cluster.getEntriesNames();

            final ICluster analysisCluster = analysisClustering.getClusterByName(cluster.getName());
            if (analysisCluster == null) {
                throw new IllegalArgumentException("can't find analog for cluster: " + cluster.getName());
            }
            final Set<String> analysisDocuments = Sets.intersection(analysisCluster.getEntriesNames(), expertClusterEntries);

            tp += Sets.intersection(analysisDocuments, expertDocuments).size();
            fp += Sets.difference(analysisDocuments, expertDocuments).size();
            fn += Sets.difference(expertDocuments, analysisDocuments).size();
        }
        
        return Pair.create(tp / (1. * (tp + fp)), tp / (1. * (tp + fn)));
    }

   /*
    * Calculates precision and recall.
    * Use it in case if you have an expert collection for the whole graph and
    * classification by algorithm for subgraph or some part of the graph.
    */
    public Pair<Double, Double> getPrecisionRecallWithIntersection(final ClusteringPreprocessor analysisClustering) {
        final Set<String> expertClusterNames = Sets.newHashSet();
        final Set<String> expertClusterEntries = Sets.newHashSet();

        for (final ICluster cluster : expertFormingClustering.getClustering().getClusters()) {
            expertClusterNames.add(cluster.getName());

            for (final String clusterEntryName : cluster.getEntriesNames()) {
                if (!analysisClustering.getClustersByEntryName(clusterEntryName).isEmpty()) {
                    expertClusterEntries.add(clusterEntryName);
                }
            }

            final ICluster judgementCluster = analysisClustering.getClusterByName(cluster.getName());
            if (judgementCluster == null) {
                throw new IllegalArgumentException("can't find analog for cluster: " + cluster.getName());
            }
        }

        int tp = 0;
        int fp = 0;
        int fn = 0;

        for (final ICluster cluster : expertFormingClustering.getClustering().getClusters()) {
            final Set<String> expertDocuments = Sets.intersection(cluster.getEntriesNames(), expertClusterEntries);

            final ICluster analysisCluster =  CollectionUtils.forceNotNull(analysisClustering.getClusterByName(cluster.getName()));
            final Set<String> analysisDocuments = Sets.intersection(analysisCluster.getEntriesNames(), expertClusterEntries);

            tp += Sets.intersection(analysisDocuments, expertDocuments).size();
            fp += Sets.difference(analysisDocuments, expertDocuments).size();
            fn += Sets.difference(expertDocuments, analysisDocuments).size();
        }

        return Pair.create(tp / (1. * (tp + fp)), tp / (1. * (tp + fn)));
    }


    /**
     * Prints confusion matrix between classifications.
     * @param analysis - classification obtained by algorithm
     * @param wr - writer (could be console or file) 
     */
    public void printConfusionMatrix(final ClusteringPreprocessor analysis, final PrintWriter wr) {
        final SortedSet<String> clusterNamesSorted = Sets.newTreeSet();

        int maxClusterNamerLength = 0;
        for (final ICluster cluster : expertFormingClustering.getClustering().getClusters()) {
            final String clusterName = cluster.getName();

            clusterNamesSorted.add(clusterName);

            if (clusterName.length() > maxClusterNamerLength) {
                maxClusterNamerLength = clusterName.length();
            }
        }

        for (final ICluster cluster : analysis.getClustering().getClusters()) {
            final String clusterName = cluster.getName();

            clusterNamesSorted.add(clusterName);

            if (clusterName.length() > maxClusterNamerLength) {
                maxClusterNamerLength = clusterName.length();
            }
        }

        final String clusterNameFormat = "%" + maxClusterNamerLength + "s";
        for (final String clusterName : clusterNamesSorted) {
            wr.println();
            wr.format(Locale.US, clusterNameFormat, clusterName);
            wr.print(" | ");

            final ICluster expertCluster  = expertFormingClustering.getClusterByName(clusterName);

            for (final String analysisClusterName : clusterNamesSorted) {
                final ICluster analysisCluster = analysis.getClusterByName(analysisClusterName);

                if (analysisCluster == null) {
                    throw new IllegalArgumentException("Can't find analog for cluster: " + analysisClusterName);
                }

                if (expertCluster == null) {
                    wr.format(Locale.US, "%7d", 0);
                } else {
                  final int intersection =
                        Sets.intersection(expertCluster.getEntriesNames(),
                                          analysisCluster.getEntriesNames()
                                ).size();
                    wr.print("&");
                    wr.format(Locale.US,  "%7d", intersection);
                }
            }
        }
        wr.print("\n");
        wr.flush();
    }


     /**
      * Creates new classification estimation without seeds.
     * @param analysisClustering -  clustering with seeds
     * @param seedsClustering - seeds nodes
     * @return clustering without seeds nodes
     */

    public static ClusteringPreprocessor getClassificationWithoutSeeds(final ClusteringPreprocessor analysisClustering, final ClusteringPreprocessor seedsClustering) {
        ClusteringBuilder builder = new ClusteringBuilder(analysisClustering.getClustering().getName()+" without seeds");
        for (final ICluster cluster : analysisClustering.getClustering().getClusters()) {
            String curCluster = cluster.getName();
            builder.addCluster(curCluster);
            ICluster clusterSeeds = seedsClustering.getClusterByName(cluster.getName());
            Set<String> entriesSeeds =  clusterSeeds.getEntriesNames();
            for (final String entryName :cluster.getEntriesNames()) {
                 if (!entriesSeeds.contains(entryName)) {
                     builder.addEntry(curCluster, entryName);
                 }
            }
        }
        return new ClusteringPreprocessor(builder.build());
    }

    /**
     * Computes precision and recall without seeds.
     */
    public static Pair<Double, Double> getPrecisionRecallWithoutSeeds(final ClusteringPreprocessor expertCl,final ClusteringPreprocessor analysisCl,
                             final ClusteringPreprocessor seedsCl) {
          ClusteringPreprocessor expertClRight = PrecisionQuantity.getClassificationWithoutSeeds(expertCl, seedsCl);
          ClusteringPreprocessor analysisClRight = PrecisionQuantity.getClassificationWithoutSeeds( analysisCl, seedsCl);

          PrecisionQuantity pq = new PrecisionQuantity(expertClRight);
          return pq.getPrecisionRecall(analysisClRight);
    }

     /**
     * Computes and prints confusion matrix without seeds.
     */
    public static void printStatisticWithoutSeeds(final ClusteringPreprocessor expertCl, final ClusteringPreprocessor analysisCl,
                              final ClusteringPreprocessor seedsCl, final PrintWriter wr) {
         ClusteringPreprocessor expertClRight = PrecisionQuantity.getClassificationWithoutSeeds(expertCl, seedsCl);
         ClusteringPreprocessor analysisClRight = PrecisionQuantity.getClassificationWithoutSeeds( analysisCl, seedsCl);
         PrecisionQuantity pq = new PrecisionQuantity(expertClRight);
         pq.printConfusionMatrix(analysisClRight, wr);
    }

}
