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
 * The general power iteration on sigma.
 * If sigma equals to 0 - pagerank iteration,
 * 0.5 - normalized laplacian iteration,
 * 1 - standard laplacian iteration.
 */
public class GeneralPowerIteration extends AbstractPowerIteration {
    private final double defaultWeight;
    //stored values for efficiency
    private final double[] storedSumForNode;
    private final double[] storedSumForNodeSigma;
    private final double[] storedSumForNodeOneMinusSigma;
    private final double[] storedDegreeSigma;

    public static IPageRankIteration createForWeightedGraph(final double sigma, final IPageRankInputData input, final double defaultWeight) {
        return new GeneralPowerIteration(sigma, input,defaultWeight, true);
    }

    public static IPageRankIteration createForUnWeightedGraph(final double sigma, final IPageRankInputData input) {
        return new GeneralPowerIteration(sigma, input);
    }

    private GeneralPowerIteration(final double sigma, final IPageRankInputData input, final double defaultWeight, final boolean isWeighted) {
        super(input, isWeighted);
        //this.sigma = sigma;
        this.defaultWeight = defaultWeight;
        //stored values to reduce computation time
        storedSumForNode = new double[numNodes];
        storedSumForNodeSigma = new double[numNodes];
        storedSumForNodeOneMinusSigma = new double[numNodes];
        storedDegreeSigma = new double[numNodes];
        for (int i = 0; i < numNodes; i++) {
            storedDegreeSigma[i]  = Math.pow(graph.outdegree(i), sigma);
            storedSumForNode[i] = getSumForNode(i);
            storedSumForNodeOneMinusSigma[i] = Math.pow(storedSumForNode[i], 1. - sigma);
            storedSumForNodeSigma[i] = Math.pow(storedSumForNode[i], sigma);
        }
    }

    private GeneralPowerIteration(final double sigma, final IPageRankInputData input) {
        this(sigma, input, 0, false);
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

            accum += oldRank[curNode] * toNodeWeight / storedSumForNodeSigma[curNode];
        }

        for (int curNode = 0; curNode < numNodes; curNode++) {
            int outdegree = graph.outdegree(curNode);

            if (outdegree != 0)  {
                final int[] succ = graph.successorArray(curNode);

                if (isWeightedGraph) {
                    final double[] weights = ((IArcWeightedGraph)graph).arcWeights(curNode);

                    for (int j = 0; j < outdegree; j++) {
                        newRank[curNode] +=
                                oldRank[succ[j]] * ((weights[j] + defaultWeight) / storedSumForNodeSigma[curNode] / storedSumForNodeOneMinusSigma[succ[j]]);
                    }
                } else {
                    double tmpNewRank = 0.0;
                    for (int j = 0; j < outdegree; j++) {
                        tmpNewRank += oldRank[succ[j]] / storedSumForNodeOneMinusSigma[succ[j]];
                    }
                    newRank[curNode] = tmpNewRank / storedDegreeSigma[curNode];
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
