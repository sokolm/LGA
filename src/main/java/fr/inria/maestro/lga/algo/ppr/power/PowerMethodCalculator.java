package fr.inria.maestro.lga.algo.ppr.power;

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

import fr.inria.maestro.lga.algo.ppr.ICalculator;
import fr.inria.maestro.lga.graph.model.IGraph;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import org.jetbrains.annotations.NotNull;

/**
 * Computes power iterations for general pagerank methods.
 */
public abstract class PowerMethodCalculator implements ICalculator {

    private final IGraph graph;
    private final double dampingFactor;
    private final IStoppingCriterion stoppingCriterion;


    protected PowerMethodCalculator(final IGraph graph, final double dumpingFactor, final IStoppingCriterion stoppingCriterion) {
        this.graph = graph;
        this.dampingFactor = dumpingFactor;
        this.stoppingCriterion = stoppingCriterion;
    }

    public double[] runProcess(final IPageRankInputData prInput, final IStoppingCriterion stoppingCriterion) {
        final double[] personalVector = prInput.getPersonalVector();
        final int numNodes = prInput.getGraph().numNodes();

        final IPageRankIteration iter = createIteration(prInput);

        double[] curPR = new double[numNodes];
        double[] prevPR = new double[numNodes];

        if ( personalVector != null ) {
            System.arraycopy(personalVector, 0, curPR, 0, numNodes);
        } else {
            DoubleArrays.fill(curPR, 1.0 / numNodes);
        }

        int iterNum = 0;
        do {

            iterNum++;
            iter.step(prevPR, curPR);
            final double tmp[] = prevPR;
            prevPR = curPR;
            curPR = tmp;
        } while (!stoppingCriterion.shouldStop(iterNum, curPR, prevPR));

        return curPR;
    }

    @Override
    public double[] compute() {
        return runProcess(
                new PageRankInputDataImpl(graph, dampingFactor), stoppingCriterion);
    }

    @Override
    public double[] compute(final double[] personalPR) {
        return runProcess(
                new PageRankInputDataImpl(graph, personalPR, dampingFactor), stoppingCriterion);
    }

    @Override
    public double[] compute(final int currentNodeID) {
        return runProcess(
                new PageRankInputDataImpl(graph, currentNodeID, dampingFactor), stoppingCriterion);
    }

    public abstract IPageRankIteration createIteration(IPageRankInputData input);

    public static class PageRankInputDataImpl implements IPageRankInputData {

        private final IGraph graph;
        private final double[] personalVector;
        private final double dampingFactor;

        public PageRankInputDataImpl(final IGraph graph, final double[] personalVector, final double dampingFactor) {
            this.graph = graph;
            this.personalVector = personalVector;
            this.dampingFactor = dampingFactor;
        }

        public PageRankInputDataImpl(final IGraph graph, final int seedNode, final double dampingFactor) {
            this.graph = graph;
            this.personalVector = new double[graph.numNodes()];
            this.dampingFactor = dampingFactor;

            this.personalVector[seedNode] = 1.;
        }

        public PageRankInputDataImpl(final IGraph graph, final double dampingFactor) {
            this.graph = graph;
            this.personalVector = null;
            this.dampingFactor = dampingFactor;
        }

        @NotNull
        @Override
        public IGraph getGraph() {
            return graph;
        }

        @Override
        public double[] getPersonalVector() {
            return personalVector;
        }

        @Override
        public double getDampingFactor() {
            return dampingFactor;
        }
    }
}
