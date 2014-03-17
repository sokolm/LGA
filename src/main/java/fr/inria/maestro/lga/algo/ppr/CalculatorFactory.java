package fr.inria.maestro.lga.algo.ppr;

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

import fr.inria.maestro.lga.algo.ppr.montecarlo.MontecarloPRCalculator;
import fr.inria.maestro.lga.algo.ppr.params.CalculatorInputData;
import fr.inria.maestro.lga.algo.ppr.params.CalculatorInputDataSigma;
import fr.inria.maestro.lga.algo.ppr.power.IPageRankInputData;
import fr.inria.maestro.lga.algo.ppr.power.IPageRankIteration;
import fr.inria.maestro.lga.algo.ppr.power.IStoppingCriterion;
import fr.inria.maestro.lga.algo.ppr.power.IStoppingCriterion.DeltaStoppingCriterion;
import fr.inria.maestro.lga.algo.ppr.power.IStoppingCriterion.IterationNumberStoppingCriterion;
import fr.inria.maestro.lga.algo.ppr.power.IStoppingCriterion.OrStoppingCriterion;
import fr.inria.maestro.lga.algo.ppr.power.PowerMethodCalculator;
import fr.inria.maestro.lga.algo.ppr.power.iter.GeneralPowerIteration;
import fr.inria.maestro.lga.algo.ppr.power.iter.LaplacianPowerIteration;
import fr.inria.maestro.lga.algo.ppr.power.iter.NormLaplacianPowerIteration;
import fr.inria.maestro.lga.algo.ppr.power.iter.PageRankPowerIteration;
import fr.inria.maestro.lga.graph.model.IArcWeightedGraph;
import fr.inria.maestro.lga.graph.model.IGraph;

/**
 * Creates an appropriate semi-supervised
 * calculator based on input data.
 */
public class CalculatorFactory {

    /**
     * @param graph - the graph
     * @param inputData - contains setups to compute general pagerank
     * @return  the calculator to compute general pagerank
     * if graph is weighted and and inputData marked as weighted
     * then it will return weighted calculator otherwise unweighted.
     */
    public static ICalculator getGeneralCalculator(final IGraph graph, final CalculatorInputDataSigma inputData) {
        final IStoppingCriterion stopping =
            new OrStoppingCriterion(new IterationNumberStoppingCriterion(inputData.getMaxIter()),
            new DeltaStoppingCriterion(inputData.getEps()));

        return new PowerMethodCalculator(graph, inputData.getAlpha(), stopping) {
            @Override
            public IPageRankIteration createIteration(final IPageRankInputData input) {
                IPageRankIteration iter;
                if (inputData.isWeighted() && graph instanceof IArcWeightedGraph) {
                    iter = GeneralPowerIteration.createForWeightedGraph(inputData.getSigma(), input, 0.0);
                } else {
                    iter = GeneralPowerIteration.createForUnWeightedGraph(inputData.getSigma(), input);
                }
                return iter;
            }
        };
    }


    public static ICalculator getStandardLaplacianCalculator(final IGraph graph, final CalculatorInputData inputData) {
        final IStoppingCriterion stopping =
            new OrStoppingCriterion(new IterationNumberStoppingCriterion(inputData.getMaxIter()),
            new DeltaStoppingCriterion(inputData.getEps()));

        return new PowerMethodCalculator(graph, inputData.getAlpha(), stopping) {
            @Override
            public IPageRankIteration createIteration(final IPageRankInputData input) {
                IPageRankIteration iter;
                if (inputData.isWeighted() && graph instanceof IArcWeightedGraph) {
                    iter = LaplacianPowerIteration.createForWeightedGraph(input, 0.0);
                } else {
                    iter = LaplacianPowerIteration.createForUnWeightedGraph(input);
                }
                return iter;
            }
        };
    }


    public static ICalculator getNormLaplacianCalculator(final IGraph graph, final CalculatorInputData inputData) {
        final IStoppingCriterion stopping =
            new OrStoppingCriterion(new IterationNumberStoppingCriterion(inputData.getMaxIter()),
            new DeltaStoppingCriterion(inputData.getEps()));

        return new PowerMethodCalculator(graph, inputData.getAlpha(), stopping) {
            @Override
            public IPageRankIteration createIteration(final IPageRankInputData input) {
                IPageRankIteration iter;
                if (inputData.isWeighted() && graph instanceof IArcWeightedGraph) {
                    iter = NormLaplacianPowerIteration.createForWeightedGraph(input, 0.0);
                } else {
                    iter = NormLaplacianPowerIteration.createForUnWeightedGraph(input);
                }
                return iter;
            }
        };
    }

    /**
     *@return RageRank calculator
     */
    public static ICalculator getPageRankCalculator(final IGraph graph, final CalculatorInputData inputData) {
        final IStoppingCriterion stopping =
            new OrStoppingCriterion(new IterationNumberStoppingCriterion(inputData.getMaxIter()),
            new DeltaStoppingCriterion(inputData.getEps()));

        return new PowerMethodCalculator(graph, inputData.getAlpha(), stopping) {
            @Override
            public IPageRankIteration createIteration(final IPageRankInputData input) {
                IPageRankIteration iter;
                if (inputData.isWeighted() && graph instanceof IArcWeightedGraph) {
                    iter = PageRankPowerIteration.createForWeightedGraph(input, 0.0);
                } else {
                    iter = PageRankPowerIteration.createForUnWeightedGraph(input);
                }
                return iter;
            }
        };
    }

    /**
     *@return Montecarlo realization of personal pagerank
     */
      public static ICalculator getPageRankCalculatorMonteCarlo(IGraph graph, int max_it, double alpha) {
           return new MontecarloPRCalculator(graph, max_it, alpha);
      }

}
