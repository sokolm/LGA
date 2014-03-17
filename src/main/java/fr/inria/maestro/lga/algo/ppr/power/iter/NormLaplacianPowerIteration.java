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
import it.unimi.dsi.fastutil.doubles.DoubleArrays;

/**
 *  Normalized Laplacian power iteration.
 */
public class NormLaplacianPowerIteration extends AbstractPowerIteration {

    private final double defaultWeight;
    private final boolean isWeightedGraph;

    //stored values for efficiency
    private final double[] storedSumForNodeSqrt;
    private final double[] storedSumForNode;
    private final double[] storedDegreeSqrt;


    public static IPageRankIteration createForWeightedGraph(final IPageRankInputData input, final double defaultWeight) {
        return new NormLaplacianPowerIteration(input,defaultWeight, true);
    }

    public static IPageRankIteration createForUnWeightedGraph(final IPageRankInputData input) {
        return new NormLaplacianPowerIteration(input);
    }

    //for weighted
    private NormLaplacianPowerIteration(final IPageRankInputData input, final double defaultWeight, final boolean isWeighted) {
        super(input, isWeighted);
        this.defaultWeight = defaultWeight;
        this.isWeightedGraph = isWeighted;
        storedSumForNode = new double[numNodes];
        storedSumForNodeSqrt = new double[numNodes];
        storedDegreeSqrt = new double[numNodes];
        for (int i = 0; i < numNodes; i++) {
            storedDegreeSqrt[i]  = Math.sqrt(graph.outdegree(i));
            storedSumForNode[i] = getSumForNode(i);
            storedSumForNodeSqrt[i] = Math.sqrt(storedSumForNode[i]);
        }
    }

    //not weighted
    private NormLaplacianPowerIteration(final IPageRankInputData input) {
        this(input, 0.0, false);
    }

    @Override
    public void step(final double[] newRank, final double[] oldRank) {
        DoubleArrays.fill(newRank, 0.0);

        double accum = 0.0;

        final double oneOverNumNodes = 1.0 / numNodes;

        for (int curNode = 0; curNode < numNodes; curNode++) {
            final double toNodeWeight;
            if (personalVector != null) {
                toNodeWeight = personalVector[curNode];
            } else {
                toNodeWeight = oneOverNumNodes;
            }

            accum += oldRank[curNode] * toNodeWeight / storedSumForNodeSqrt[curNode];
        }

        for (int curNode = 0; curNode < numNodes; curNode++) {
            int outdegree = graph.outdegree(curNode);

            if (outdegree != 0)  {
                final int[] succ = graph.successorArray(curNode);

                if (isWeightedGraph) {
                    final double[] weights = ((IArcWeightedGraph)graph).arcWeights(curNode);

                    for (int j = 0; j < outdegree; j++) {
                        newRank[curNode] +=
                                oldRank[succ[j]] * ((weights[j] + defaultWeight) / storedSumForNodeSqrt[curNode] / storedSumForNodeSqrt[succ[j]]);
                    }
                } else {
                    double tmpNewRankCurNode = 0.0;
                    for (int j = 0; j < outdegree; j++) {
                        tmpNewRankCurNode += oldRank[succ[j]] / storedSumForNodeSqrt[succ[j]];
                    }
                    newRank[curNode] = tmpNewRankCurNode / storedDegreeSqrt[curNode];
                }
            } else {
                newRank[curNode] = accum;
            }
        }

        if (personalVector != null) {
            for (int i = numNodes; i-- != 0;)  {
                newRank[i] = dFactor * newRank[i] + (1 - dFactor) * personalVector[i];
            }
        } else {
            for (int i = numNodes; i-- != 0;) {
                newRank[i] = dFactor * newRank[i] + (1 - dFactor) * oneOverNumNodes;
            }
        }
    }

    private double getSumForNode(final int node) {
        final int outdegree = graph.outdegree(node);
        if (outdegree == 0) {
            return 1.;
        }

        if (!isWeightedGraph) {
            return outdegree;
        } else {
            return rowSums[node] + defaultWeight * outdegree;
        }
    }
}