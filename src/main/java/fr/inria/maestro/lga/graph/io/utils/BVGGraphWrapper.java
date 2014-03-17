package fr.inria.maestro.lga.graph.io.utils;

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
import it.unimi.dsi.webgraph.ImmutableGraph;

/**
 * Wraps the graph.
 * Useful to store graph in BVG format.
 */
public class BVGGraphWrapper extends ImmutableGraph {
    private IGraph graph;

    public BVGGraphWrapper(IGraph graph) {
        this.graph = graph;
    }

    public int numNodes() {
        return graph.numNodes();
    }

    public boolean randomAccess() {
        return true;
    }

    public ImmutableGraph copy() {
        return this;
    }

    public int outdegree(int x) {
        return graph.outdegree(x);
    }

    public int[] successorArray(int x) {
        return graph.successorArray(x);
    }

    public CharSequence basename() {
        return "basename";
    }
}
