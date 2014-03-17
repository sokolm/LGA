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
 * Standard Laplacian power iteration.
 */
public class LaplacianPowerIteration extends AbstractPowerIteration {

    private final double defaultWeight;

    public static IPageRankIteration createForWeightedGraph(final IPageRankInputData input, final double defaultWeight) {
        return new LaplacianPowerIteration(input, defaultWeight, true);
    }

    public static IPageRankIteration createForUnWeightedGraph(final IPageRankInputData input) {
        return new LaplacianPowerIteration(input);
    }

    private LaplacianPowerIteration(final IPageRankInputData input, final double defaultWeight, final boolean isWeighted) {
        super(input, isWeighted);
        this.defaultWeight = defaultWeight;
    }

    private LaplacianPowerIteration(final IPageRankInputData input) {
        this(input, 0.0, false);
    }

    @Override
    public void step(final double[] newRank, final double[] oldRank) {
        DoubleArrays.fill(newRank, 0.0);

        double accum = 0.0;

        final double oneOverNumNodes = 1.0 / numNodes;

        for (int curNode = 0; curNode < numNodes; curNode++) {
            if (personalVector != null) {
                accum += oldRank[curNode] * personalVector[curNode];
            } else {
                accum += oldRank[curNode] * oneOverNumNodes;
            }
        }

        for (int curNode = 0; curNode < numNodes; curNode++) {
            int outdegree = graph.outdegree(curNode);

            if (outdegree != 0)  {
                final int[] succ = graph.successorArray(curNode);

                if (isWeightedGraph) {
                    final double[] weights = ((IArcWeightedGraph)graph).arcWeights(curNode);

                    for (int j = 0; j < outdegree; j++) {
                        newRank[curNode] += oldRank[succ[j]] * ((weights[j] + defaultWeight) / (rowSums[curNode] + defaultWeight * outdegree));
                    }
                } else {
                    for (int j = 0; j < outdegree; j++) {
                        newRank[curNode] += oldRank[succ[j]] / outdegree;
                    }
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
}

