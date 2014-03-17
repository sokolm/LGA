package fr.inria.maestro.lga.algo.seeds.impl;

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
 * Created by IntelliJ IDEA.
 * User: Marina
 * Date: 2/19/14
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */

import java.util.List;

/**
 * Computes centrality measure. Sorts nodes according to its measure.
 */
public interface ICoreFinder {

    /**
     * Finds and returns the sorted list by centrality measure in decreasing order.
     * @return the list sorted by centrality measure in decreasing order.
    */
    List<MeasuredNode> findCores();

    /**
     * Return the precomputed sorted list by centrality measure in decreasing order.
     * @return the precomputed sorted list by centrality measure in decreasing order.
     */
    List<MeasuredNode> getCores();

    /**
     * Returns the centrality measure of nodes in node id order.
     * @return the centrality measure of nodes
     */
    double[] getCentralityMeasure();
}
