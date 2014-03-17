package fr.inria.maestro.lga.graph.model.impl.bvg;

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

import com.google.common.collect.ImmutableMap;
import fr.inria.maestro.lga.graph.model.IGraph;
import fr.inria.maestro.lga.graph.model.impl.AbstractGraph;
import fr.inria.maestro.lga.graph.model.property.PropertyKey;
import it.unimi.dsi.webgraph.ImmutableGraph;

/**
 * Represents an unweighted graph as BVGraph.
 * Thread safe wrapper under the graph object.
 */
public class WebGraphBVG extends AbstractGraph implements IGraph {
    protected final ImmutableGraph myGraph;

    public WebGraphBVG(final ImmutableGraph myGraph) {
        super(ImmutableMap.<PropertyKey<?>, Object>of(UNDERLYING_GRAPH, myGraph));
        this.myGraph = myGraph;
    }

    @Override
    synchronized public long numArcs() {
        return myGraph.numArcs();
    }

    @Override
    synchronized public int numNodes() {
        return myGraph.numNodes();
    }

    @Override
    synchronized public int outdegree(int node) {
        return myGraph.outdegree(node);
    }

    @Override
    synchronized public int[] successorArray(int node) {
        return myGraph.successorArray(node);
    }
}
