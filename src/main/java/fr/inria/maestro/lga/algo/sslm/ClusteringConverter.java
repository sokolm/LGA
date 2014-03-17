package fr.inria.maestro.lga.algo.sslm;

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

import com.google.common.collect.ImmutableSet;
import fr.inria.maestro.lga.algo.ppr.pvector.IPersonalVector;
import fr.inria.maestro.lga.algo.ppr.pvector.PersonalVector;
import fr.inria.maestro.lga.clustering.ICluster;
import fr.inria.maestro.lga.clustering.IClustering;
import fr.inria.maestro.lga.clustering.impl.ClusteringBuilder;
import fr.inria.maestro.lga.graph.model.INodeNamer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Converts {@link fr.inria.maestro.lga.clustering.IClustering IClustering} structure to
 * some others.
 */
public class ClusteringConverter {
    /**
     * @param seedsNames the seeds as names mapped to each class
     * @param namer  the namer of the graph
     * @param numNodes the number of nodes in the graph
     * @return the {@link fr.inria.maestro.lga.algo.sslm.ISeedsInput} object
     */
    public static  ISeedsInput  clustering2SeedsInput(IClustering seedsNames, INodeNamer namer, int numNodes) {
        List<IPersonalVector> pVs = new LinkedList<IPersonalVector>();
        List<ICluster> clusters = seedsNames.getClusters();
        for (int i = 0; i < clusters.size(); i++) {
            List<Integer> clusterNodes = new LinkedList<Integer>();
            ICluster cluster = clusters.get(i);
            ImmutableSet<String> namesSet = cluster.getEntriesNames();
            for (String name: namesSet) {
                int nodeId = namer.getNodeId(name);
                clusterNodes.add(nodeId);
            }
            IPersonalVector pv = new PersonalVector(clusterNodes, numNodes);
            pVs.add(pv);
        }
        return new SeedsInput(pVs);
    }

    /**
     *
     * @param seedsIds  the seeds as Ids mapped to each class
     * @param numNodes the number of nodes in the graph
     * @return the {@link fr.inria.maestro.lga.algo.sslm.ISeedsInput} object
     */
    public static ISeedsInput clustering2SeedsInput(IClustering seedsIds, int numNodes) {
        List<IPersonalVector> pVs = new LinkedList<IPersonalVector>();
        List<ICluster> clusters = seedsIds.getClusters();
        for (int i = 0; i < clusters.size(); i++) {
            List<Integer> clusterNodes = new LinkedList<Integer>();
            ICluster cluster = clusters.get(i);
            ImmutableSet<String> namesSet = cluster.getEntriesNames();
            for (String name: namesSet) {
                int nodeId = Integer.parseInt(name);
                clusterNodes.add(nodeId);
            }
            IPersonalVector pv = new PersonalVector(clusterNodes, numNodes);
            pVs.add(pv);
        }
        return new SeedsInput(pVs);
    }

    /**
     * Converts for array to {@link fr.inria.maestro.lga.clustering.IClustering
     * IClustering} object
     * @param nodeId2Class array contains values as class id and index as node id
     * @param namer the namer of the graph
     * @param seeds  the seeds of graph, contains name of classes
     * @return the given classification as {@link fr.inria.maestro.lga.clustering.IClustering
     * IClustering} object
     */
    public static IClustering array2Custering(int[] nodeId2Class, INodeNamer namer, IClustering seeds) {
        ClusteringBuilder cb = new ClusteringBuilder("Classification "+new Date().toString());
        List<ICluster> clusters = seeds.getClusters();
        String[] clusterNames = new String[clusters.size()];
        for(int i = 0; i < seeds.getClusters().size(); i++) {
             ICluster cluster = clusters.get(i);
             clusterNames[i] = cluster.getName();
             cb.addCluster(clusterNames[i]);
        }
        int unclassifiedNum = 0;
        for (int iNode = 0; iNode < nodeId2Class.length; iNode++) {
            int classId = nodeId2Class[iNode];
            if (classId < 0) {
                unclassifiedNum++;
               //unclassified item
            } else {
                cb.addEntry(clusterNames[classId], namer.getNodeName(iNode));
            }
        }
        return cb.build();
    }

}
