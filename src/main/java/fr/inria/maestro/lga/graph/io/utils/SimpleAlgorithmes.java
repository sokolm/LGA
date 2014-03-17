package fr.inria.maestro.lga.graph.io.utils;

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

import fr.inria.maestro.lga.graph.model.IArcWeightedGraph;
import fr.inria.maestro.lga.graph.model.IGraph;
import fr.inria.maestro.lga.graph.model.impl.matrix.MatrixGraph;
import fr.inria.maestro.lga.graph.model.impl.matrix.MatrixWeightedGraph;

import java.util.*;


/**
 * Provides some simple operation on graphs,
 * like sorted successors of the node's ids in increasing order.
 */
public class SimpleAlgorithmes {

    private SimpleAlgorithmes() {}

    // preprocessing weighted graph to make it working with bvg format
    //Work with: graph, weighted  graph
    public static IGraph sortSuccessors(IGraph graph) {
        // Help Class to be putted into a set
        if (graph instanceof IArcWeightedGraph) {
            Set[] successors = new SortedSet[graph.numNodes()];
            for (int i = 0; i < graph.numNodes(); i++) {
                successors[i] = new TreeSet<SimpleEdge>();
                int[] tempArray = graph.successorArray(i);
                double[] tempWeightArray = ((IArcWeightedGraph) graph).arcWeights(i);
                for (int j = 0; j < graph.outdegree(i); j++) {
                    successors[i].add(new SimpleEdge(tempArray[j], tempWeightArray[j]));
                }
            }

            int[][] resultArray = new int[graph.numNodes()][];
            double[][] resultWeightArray = new double[graph.numNodes()][];
            for (int i = 0; i < graph.numNodes(); i++) {
                resultArray[i] = new int[graph.outdegree(i)];
                resultWeightArray[i] = new double[graph.outdegree(i)];
                Iterator iterator = successors[i].iterator();
                int count = 0;
                while (iterator.hasNext()) {
                    SimpleEdge tempEdge = (SimpleEdge) iterator.next();
                    resultArray[i][count] = tempEdge.nodeId;
                    resultWeightArray[i][count] = tempEdge.edgeWeight;
                    count++;
                }
            }
            return new MatrixWeightedGraph(resultArray, resultWeightArray);
        } else {
            int[][] matrix = new int[graph.numNodes()][];
            for (int i = 0; i < graph.numNodes(); i++) {
                matrix[i] = new int[graph.outdegree(i)];
                for (int j = 0; j < graph.outdegree(i); j++) {
                    matrix[i][j] = graph.successorArray(i)[j];
                }
                Arrays.sort(matrix[i]);
            }
            return new MatrixGraph(matrix);
        }
    }

    private static  class SimpleEdge implements Comparable {
        final int nodeId;
        final double edgeWeight;

        private SimpleEdge(int nodeId, double edgeWeight) {
            this.nodeId = nodeId;
            this.edgeWeight = edgeWeight;
        }

        @Override
        public int compareTo(final Object o) {
            if (o instanceof SimpleEdge) {
                if (((SimpleEdge) o).nodeId < this.nodeId) {
                    return 1;
                } else if (((SimpleEdge) o).nodeId > this.nodeId) {
                    return -1;
                } else return 0;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
