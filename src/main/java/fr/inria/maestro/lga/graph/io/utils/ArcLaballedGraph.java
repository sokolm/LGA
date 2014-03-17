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

import it.unimi.dsi.webgraph.labelling.ArcLabelledImmutableGraph;
import it.unimi.dsi.webgraph.labelling.ArcLabelledNodeIterator.LabelledArcIterator;
import it.unimi.dsi.webgraph.labelling.Label;

/**
 * This class create a weighted graph.
 */
public class ArcLaballedGraph extends ArcLabelledImmutableGraph {
    private final int[][] matrix;
    private final Label[][] labels;
    private final int numArcs;

    /**
     * @param matrix - matrix[i] - contain sucsessors for node i.
     * @param labels - labels[i] - contans weights of succ for node i;
     */
    public ArcLaballedGraph(final int[][] matrix, final Label[][] labels) {
        this.matrix = matrix;
        this.labels = labels;

        int numArcs = 0;
        for (final int[] row : matrix) {
            numArcs += row.length;
        }

        this.numArcs = numArcs;
    }

    // Convenient constructor
    public ArcLaballedGraph(final int[][] matrix, final double[][] weights) {
        this.matrix = matrix;
        Label[][] labels = new Label[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            labels[i] = new Label[matrix[i].length];
            for (int j = 0; j < labels[i].length; j++) {
                labels[i][j] = WebGraphLabelsFactory.getLabel(weights[i][j]);
            }
        }
        this.labels = labels;

        int numArcs = 0;
        for (final int[] row : matrix) {
            numArcs += row.length;
        }

        this.numArcs = numArcs;
    }


    // Convenient constructor
       public ArcLaballedGraph(final int[][] matrix, final int[][] weights) {
           this.matrix = matrix;
           Label[][] labels = new Label[matrix.length][];
           for (int i = 0; i < matrix.length; i++) {
               labels[i] = new Label[matrix[i].length];
               for (int j = 0; j < labels[i].length; j++) {
                   labels[i][j] = WebGraphLabelsFactory.getLabel(weights[i][j]);
               }
           }
           this.labels = labels;

           int numArcs = 0;
           for (final int[] row : matrix) {
               numArcs += row.length;
           }

           this.numArcs = numArcs;
       }



    /**
     * Create a full-connected graph with weights
     */
    public ArcLaballedGraph(double[][] corrMatrix) {
        int numNodes = corrMatrix.length;
        matrix = new int[numNodes][];
        labels = new Label[numNodes][];
        for (int i = 0; i < numNodes; i++) {
            matrix[i] = new int[numNodes];
            labels[i] = new Label[numNodes];

            for (int k = 0; k < numNodes; k++) {
                matrix[i][k] = k;
                labels[i][k] = WebGraphLabelsFactory.getLabel(corrMatrix[i][k]);
            }
        }
        int numArcs = 0;
        for (final int[] row : matrix) {
            numArcs += row.length;
        }

        this.numArcs = numArcs;
    }

    @Override
    public ArcLabelledImmutableGraph copy() {
        return this;
    }

    @Override
    public LabelledArcIterator successors(final int node) {
        return new LabelledArcIterator() {
            int i = -1;
            final int[] successors = matrix[node];
            final int outdegree = outdegree(node);

            public Label label() {
                return labels[node][i];
            }

            public int nextInt() {
                return i < outdegree - 1 ? successors[++i] : -1;
            }

            public int skip(final int n) {
                final int incr = Math.min(n, outdegree - i - 1);
                i += incr;
                return incr;
            }
        };
    }

    // It would be good to make here returning array of needed outdegree
    @Override
    public int[] successorArray(final int node) {
        return matrix[node];
    }

    @Override
    public Label prototype() {
        return WebGraphLabelsFactory.getLabel(0);
    }

    @Override
    public int numNodes() {
        return matrix.length;
    }

    @Override
    public boolean randomAccess() {
        return true;
    }

    @Override
    public int outdegree(final int i) {
        return matrix[i].length;
    }

    @Override
    public long numArcs() {
        return numArcs;
    }
}
