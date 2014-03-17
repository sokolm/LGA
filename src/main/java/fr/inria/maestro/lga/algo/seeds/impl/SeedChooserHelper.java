package fr.inria.maestro.lga.algo.seeds.impl;

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

import fr.inria.maestro.lga.clustering.ICluster;
import fr.inria.maestro.lga.clustering.IClustering;
import fr.inria.maestro.lga.clustering.analysis.impl.ClusteringPreprocessor;
import fr.inria.maestro.lga.clustering.impl.ClusteringBuilder;
import fr.inria.maestro.lga.graph.model.INodeNamer;

import java.util.*;

/**
 * Helps choose seeds by different params.
 */
public class SeedChooserHelper {
    public static final int MODE_ID = 0;
    public static final int MODE_NAME = 1;


    private final ICoreFinder finder;
    private final int K;
    private final IClustering expertEstim;
    private final ClusteringBuilder builderSeeds;
    private final INodeNamer namer;
    private final int mode;
    //save how many element in each class already marked as seeds
    private CounterMap<String> map = new CounterMap<String>();
    private boolean inverseOrder = false;

    /**
     *  Creates seeds chooser.
     * @param K - the number of seeds for each class
     * @param expertEstim - the expert estimation (with names)
     * @param namer - the namer which is transform from id to name of the class
     * @param mode - the mode in which the seeds will be obtaines (could be node ids, could be node names)
     * @param finder - finder which giver the sorted cores according to centrality measure in decreasing order
     */
    public SeedChooserHelper(int K, IClustering expertEstim, INodeNamer namer, int mode, ICoreFinder finder) {
        this.finder = finder;
        this.K = K;
        this.expertEstim = expertEstim;
        this.builderSeeds = new ClusteringBuilder("Seeds for:"+expertEstim.getName());
        this.namer = namer;
        this.mode = mode;
        Collection<ICluster> clusters = expertEstim.getClusters();
        for (ICluster cluster : clusters) {
            String clusterName = cluster.getName();
             builderSeeds.addCluster(clusterName);
        }
    }

    /**
     *  Creates seeds chooser.
     * @param K - the number of seeds for each class
     * @param expertEstim - the expert estimation (with names)
     * @param namer - the namer which is transform from id to name of the class
     * @param mode - the mode in which the seeds will be obtaines (could be node ids, could be node names)
     * @param finder - finder which giver the sorted cores according to centrality measure in decreasing order
     * @param inverseOrder - the oredr of the seeds.
     */
    public SeedChooserHelper(int K, IClustering expertEstim, INodeNamer namer, int mode, ICoreFinder finder, boolean inverseOrder) {
        this (K, expertEstim, namer, mode, finder);
        this.inverseOrder = inverseOrder;
    }

    /**
     * Finds and selects seeds. Returns the seeds in clustering format.
     * @return the seeds in clustering format.
     */
    public IClustering getSeeds() {
        List<MeasuredNode> cores = finder.findCores();
        if (inverseOrder) {
           Collections.reverse(cores);
        }
        ClusteringPreprocessor preprocessorExpert = new ClusteringPreprocessor(expertEstim);
        for (MeasuredNode core : cores) {
            int nodeId = core.getId();
            String nodeName = namer.getNodeName(nodeId);
            Collection<ICluster> clusters = preprocessorExpert.getClustersByEntryName(nodeName);
            //check if we have this node in our expert estimation, otherwise skip it.
            if (clusters.size() > 0) {
                ICluster cluster = (ICluster)clusters.toArray()[0];
                String clusterName = cluster.getName();
                int counter = map.getCounter(clusterName);
                if (counter < K) {
                    map.addCounter(clusterName);

                    if (mode == MODE_ID) {
                        builderSeeds.addEntry(clusterName, ""+nodeId);

                    } else {
                        builderSeeds.addEntry(clusterName, nodeName);
                    }
                }
            }
        }
        return builderSeeds.build();
    }

    //Helper class. Computes how ofter the element was put in map.
    private static class CounterMap<K> {
        private final  Map<K, Integer> counterMap = new HashMap<K, Integer>();

        /**
         * Increase the number of the same element, if it is new add it
         * to the collection with counter equals to 1.
         * @param name - the identifier of the element
         */
        private void addCounter(K name) {
            Integer count = counterMap.get(name);
            if (count == null) {
                count = 1;
            } else {
                count++;
            }
            this.counterMap.put(name, count);
        }

        /**
         * Returns the number of times the key was added to the collection.
         * @param key - the key
         * @return the number of times the key waw added
         */
        private int getCounter(K key) {
            Integer value = this.counterMap.get(key);
            return (value == null)? 0 : value;
        }
    }
}
