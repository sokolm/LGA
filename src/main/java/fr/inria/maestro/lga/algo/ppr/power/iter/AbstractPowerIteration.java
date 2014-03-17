package fr.inria.maestro.lga.algo.ppr.power.iter;

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

import fr.inria.maestro.lga.algo.ppr.power.IPageRankInputData;
import fr.inria.maestro.lga.algo.ppr.power.IPageRankIteration;
import fr.inria.maestro.lga.graph.model.IArcWeightedGraph;
import fr.inria.maestro.lga.graph.model.IGraph;

abstract class AbstractPowerIteration implements IPageRankIteration {
    //could be null
    protected final double[] personalVector;

    protected final double dFactor;

    protected final IGraph graph;
    protected final boolean isWeightedGraph;
    protected final int numNodes;


    protected final double[] rowSums;

    public AbstractPowerIteration(final IPageRankInputData input) {
        this(input, false);
    }

    public AbstractPowerIteration(final IPageRankInputData input, final boolean isWeightedGraph) {
        this.graph = input.getGraph();
        this.numNodes = graph.numNodes();
        this.dFactor = input.getDampingFactor();
        this.personalVector = input.getPersonalVector();
        this.isWeightedGraph = isWeightedGraph;

         if(isWeightedGraph) {
             if(!(input.getGraph() instanceof IArcWeightedGraph)) {
                throw new IllegalArgumentException("Graph is not weighted!");
             }

             this.rowSums = calculateRowSums((IArcWeightedGraph)input.getGraph());
        }  else {
             this.rowSums = null;
         }
    }

    public static double[] calculateRowSums(final IArcWeightedGraph weightedGraph) {
        final double[] result = new double[weightedGraph.numNodes()];

        for (int curNode = 0; curNode < weightedGraph.numNodes(); curNode++) {
            final int outdegree = weightedGraph.outdegree(curNode);
            final double[] weights = weightedGraph.arcWeights(curNode);

            double sum = 0.;
            for (int sucId = 0; sucId < outdegree; sucId++) {
                sum += weights[sucId];
            }

            result[curNode] = sum;
        }

        return result;
    }
}
