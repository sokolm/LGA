package fr.inria.maestro.lga.graph.model;

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

import fr.inria.maestro.lga.graph.model.property.IProperties;
import fr.inria.maestro.lga.graph.model.property.PropertyKey;
import org.jetbrains.annotations.NotNull;

/**
 * An immutable structure to represent the graph.
 * This class provide a simple way to access the data.
 */
public interface IGraph {
    /**
     * @return thr number of nodes in the graph
     */
	int numNodes();

    /**
     * @return the number of arcs in the graph
     */
    public long numArcs();

    /**
     * @param node the node id of the graph
     * @return the number of neighbours for the given node
     */
	int outdegree(int node);

    /**
     * @return an array of neighbour's ids, should be sorted in increasing order.
     * The length of an array could be wrong.
     * Use {@linkplain #outdegree(int node) outdegree} method to take the number of neighbours.
     */
    @NotNull
	int[] successorArray(int node);

    /**
     * @return a property of the graph
     */
    @NotNull
    IProperties getProperties();

    //retrieve methods for node name and id
    public final static PropertyKey<INodeNamer> NODE_NAMER = PropertyKey.create(INodeNamer.class);

    //the underlying graph, could be as another representation of a graph
    public final static PropertyKey<Object> UNDERLYING_GRAPH = PropertyKey.create(Object.class);
}
