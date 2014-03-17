package fr.inria.maestro.lga.algo.ppr.params;

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

import fr.inria.maestro.lga.algo.Constants;


/**
 * Class to store setups for SSCalculator for SSL methods with predefined sigma.
 */
public class CalculatorInputData {
    private final double alpha;
    private final double epsilon;
    private final int maxIter;
    //to compute like for weighted graph
    private boolean isWeighted = false;

    /**
     * Creates the default setups for methods.
     */
    public CalculatorInputData() {
        this(Constants.DFACTOR_PR);
    }

    /**
     * Creates the setups for methods.
     * @param alpha - the dumping factor in ssl methods
     */
    public CalculatorInputData(double alpha) {
         this(alpha, Constants.EPSILON_PR);
    }

    /**
     * Creates the setups for methods.
     * @param alpha - the damping factor in ssl methods
     * @param eps  - delta stopping criteria (norm L1)
     */
    public CalculatorInputData(double alpha, double eps) {
         this(alpha, eps, Constants.MAX_ITER_PR);
    }

    /**
     * Creates the setups for methods.
     * @param alpha - the damping factor in ssl methods
     * @param eps - delta stopping criteria (norm L1)
     * @param maxIter - maximum number of iterations
     */
    public CalculatorInputData(double alpha, double eps, int maxIter) {
        this.alpha = alpha;
        this.epsilon = eps;
        this.maxIter = maxIter;
    }

    /**
     * Returns damping factor.
     * @return damping factor
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Returns max number of iterations.
     * @return max namber of iterations
     */
    public int getMaxIter() {
        return maxIter;
    }

    /**
     * Returns delta stopping criteria (norm L1).
     * @return delta stippping criteria
     */
    public double getEps() {
        return epsilon;
    }

    /***
     * Returns the mode of computation: weighted or unweighted.
     * @return true if computation like for weighted graphs, false by default.
     */
    public boolean isWeighted() {
        return isWeighted;
    }

    /**
     * Sets the computation mode: weighted or unweighted.
     * Possible to make computation for weighted graph in unweighted mode.
     * @param weighted - the computation mode.
     */
    public void setWeighted(boolean weighted) {
        isWeighted = weighted;
    }
}
