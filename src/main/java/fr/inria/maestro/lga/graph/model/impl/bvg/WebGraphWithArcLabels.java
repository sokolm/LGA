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
import fr.inria.maestro.lga.graph.model.IArcWeightedGraph;
import fr.inria.maestro.lga.graph.model.impl.AbstractGraph;
import fr.inria.maestro.lga.graph.model.property.PropertyKey;
import it.unimi.dsi.webgraph.labelling.ArcLabelledImmutableGraph;
import it.unimi.dsi.webgraph.labelling.Label;

/**
 * Represents a weighted graph as BVGraph.
 * Thread safe wrapper under the graph object.
 */
public class WebGraphWithArcLabels extends AbstractGraph implements IArcWeightedGraph {
    private final ArcLabelledImmutableGraph myGraph;

    public WebGraphWithArcLabels(final ArcLabelledImmutableGraph graph) {
        super(ImmutableMap.<PropertyKey<?>, Object>of(UNDERLYING_GRAPH, graph));
        this.myGraph = graph;
    }

    synchronized  public long numArcs() {
        return myGraph.numArcs();
    }

    synchronized public int numNodes() {
        return myGraph.numNodes();
    }

    synchronized public  int outdegree(final int node) {
        return myGraph.outdegree(node);
    }

    synchronized public int[] successorArray(final int node) {
        return myGraph.successorArray(node);
    }

    synchronized public double[] arcWeights(final int node) {
        final Label[] labels = this.myGraph.labelArray(node);
        final double[] weights = new double[outdegree(node)];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = labels[i].getDouble();
        }
        return weights;
    }

}
