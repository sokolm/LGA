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

import com.google.common.base.Preconditions;
import fr.inria.maestro.lga.clustering.ICluster;
import fr.inria.maestro.lga.clustering.IClustering;
import fr.inria.maestro.lga.clustering.analysis.IClassificationQuantuty;
import fr.inria.maestro.lga.graph.model.IGraph;
import fr.inria.maestro.lga.graph.model.INodeNamer;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Computes modularity for a graph.
 */
public class Modularity implements IClassificationQuantuty {
    private final IGraph graph;
    private final INodeNamer namer;

    /**
     * Creates modularity object.
     * @param graph - the graph
     * @param namer - the namer for this graph corresponding to clustering.
     */
    public Modularity(IGraph graph, INodeNamer namer) {
        this.graph = graph;
        this.namer = namer;
    }

    @Override
    public double getQuantity(IClustering clustering) {
        final long numArcs = graph.numArcs();

        double result = 0;

        for (final ICluster cl : clustering.getClusters()) {
            final IntSet clusterNodes = new IntOpenHashSet(cl.getEntries().size());
            for (final String name : cl.getEntriesNames()) {
                final int nodeId = namer.getNodeId(name);

                Preconditions.checkState(nodeId >= 0);

                clusterNodes.add(nodeId);
            }

            int nodesInside = 0;
            int outDegree = 0;
            for (final int fstNode : clusterNodes) {

                outDegree += graph.outdegree(fstNode);

                final int[] succ = graph.successorArray(fstNode);
                final int succLength = graph.outdegree(fstNode);

                for (int i = 0; i < succLength; i++) {
                    if (clusterNodes.contains(succ[i])) {
                        nodesInside++;
                    }
                }
            }

            final double tmp = (1. * outDegree) / numArcs;
            result += (1. * nodesInside) / numArcs - tmp * tmp;
        }

        return result;
    }
}
