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

/**
 * Computes pagerank and personal pagerank.
 */
public interface ICalculator {

	/**
     * Computes method.
     * Gives to all nodes the same priority.
     * @return pagerank
     */
    double[] compute();

    /***
     * Computes method from the personal vector.
     * @param personalV - contains all nodes of the graphs with their start weights
     * (weights could be zero), should be a stohastic vector
     * @return personal pagerank
     */
	double[] compute(double[] personalV);

    /**
     * Computes personal pagerank based on a particular node.
     * @param currentNodeID the choosen node
     * @return personal pagerank
     */
    double[] compute(int currentNodeID);
}
