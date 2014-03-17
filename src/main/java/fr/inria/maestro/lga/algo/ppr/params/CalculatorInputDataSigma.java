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
 * Class to store setups for SSCalculator for SSL methods.
 */
public class CalculatorInputDataSigma extends CalculatorInputData {
    private final double sigma;

    /**
     * Creates the setups for methods.
     * @param sigma - the sigma parameter for SSL methods
     */
    public CalculatorInputDataSigma(double sigma) {
        this(sigma, Constants.DFACTOR_PR);
    }

    /**
     * Creates the setups for methods.
     * @param sigma - the sigma parameter for SSL methods
     * @param alpha - the damping factor in ssl methods
     */
    public CalculatorInputDataSigma(double sigma, double alpha) {
         this(sigma, alpha, Constants.EPSILON_PR);
    }

    /**
     * Creates the setups for methods.
     * @param sigma - the sigma parameter for SSL methods
     * @param alpha - the damping factor in ssl methods
     * @param eps  - delta stopping criteria (norm L1)
     */
    public CalculatorInputDataSigma(double sigma, double alpha, double eps) {
         this(sigma, alpha, eps, Constants.MAX_ITER_PR);
    }

    /**
     * Creates the setups for methods.
     * @param sigma - the sigma parameter for SSL methods
     * @param alpha - the damping factor in ssl methods
     * @param eps  - delta stopping criteria (norm L1)
     * @param maxIter - maximum number of iterations
     */
    public CalculatorInputDataSigma(double sigma, double alpha, double eps, int maxIter) {
        super(alpha, eps, maxIter);
        this.sigma = sigma;
    }

    /**
     * Returns sigma.
     * @return the sigma parameter for SSL methods
     */
    public double getSigma() {
        return sigma;
    }

}

