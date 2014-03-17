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

import fr.inria.maestro.lga.graph.model.IGraph;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The PageRank input data and setups.
 */
public interface IPageRankInputData {
    /**
     * Returns the graph for calculations.
     * @return the graph
     */
    @NotNull
    IGraph getGraph();

    /**
     * Returns the stochastic personal vector for SSL methods.
     * @return the personal vector
     */
    @Nullable
    double[] getPersonalVector();

    /***
     * Returns the damping factor.
     * @return the damping factor
     */
    double getDampingFactor();
}
