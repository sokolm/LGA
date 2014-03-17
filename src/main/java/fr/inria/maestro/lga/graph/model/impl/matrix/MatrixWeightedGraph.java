package fr.inria.maestro.lga.graph.model.impl.matrix;

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
import fr.inria.maestro.lga.graph.model.impl.AbstractGraph;

/**
 * Represents the graph as adjacency list.
 * The neighbours are sorted in increasing order.
 * The weight of edges is stored according to neighbour's index.
 */
public class MatrixWeightedGraph extends AbstractGraph implements IArcWeightedGraph {
    private final int[][] matrix;       //store nodes
    private final double[][] weights;   //store weights
    private final int numArcs;

    /**
     * @param matrix  - [i] - index of the nodes [j] only neighbours
     * @param weights [i] - index of the nodes [j] only neighbours
     */
    public MatrixWeightedGraph(final int[][] matrix, double[][] weights) {
        this.matrix = matrix;
        this.weights = weights;
        int numArcs = 0;
        for (final int[] line : matrix) {   //get the int array of each matrix[iteration number]
            if (line != null) {
                numArcs += line.length;     //counting total number of arcs of the matrix
            }
        }
        this.numArcs = numArcs;             //assigning the total number of arcs
    }

    @Override
    public long numArcs() {
        return numArcs;
    }

    @Override
    public int numNodes() {
        return matrix.length;
    }

    @Override
    public int outdegree(final int node) {
        if (matrix[node] == null) {
            return 0;
        }
        return matrix[node].length;
    }

    @Override
    public int[] successorArray(final int node) {
        return matrix[node] != null ? matrix[node] : new int[0];    //if the given node is null send empty array, otherwise send the array at matrix[node]
    }

    @Override
    public double[] arcWeights(final int node) {                    //returns the weights array at weights[node]
        return weights[node];
    }
}
