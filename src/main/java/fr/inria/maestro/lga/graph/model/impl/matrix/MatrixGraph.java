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

import com.google.common.collect.ImmutableMap;
import fr.inria.maestro.lga.graph.model.IGraph;
import fr.inria.maestro.lga.graph.model.impl.AbstractGraph;
import fr.inria.maestro.lga.graph.model.property.PropertyKey;

/**
 * Represents the graph as adjacency list.
 */
public class MatrixGraph extends AbstractGraph implements IGraph {
    private final int[][] matrix;
    private final int numArcs;


    public MatrixGraph(final int[][] matrix) {
        super(ImmutableMap.<PropertyKey<?>, Object>of(UNDERLYING_GRAPH, matrix));
        this.matrix =matrix;

        int numArcs = 0;
        //calculating the total number of arcs
        for (final int[] line : matrix) {
            if (line != null) {
                numArcs += line.length;
            }
        }

        this.numArcs = numArcs;
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
        return matrix[node] != null ? matrix[node] : new int[0];
    }
}
