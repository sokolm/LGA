package fr.inria.maestro.lga.graph.io.savers;

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

import fr.inria.maestro.lga.graph.io.utils.ArcLaballedGraph;
import fr.inria.maestro.lga.graph.io.utils.BVGGraphWrapper;
import fr.inria.maestro.lga.graph.io.utils.SimpleAlgorithmes;
import fr.inria.maestro.lga.graph.model.IArcWeightedGraph;
import fr.inria.maestro.lga.graph.model.IGraph;
import it.unimi.dsi.webgraph.BVGraph;
import it.unimi.dsi.webgraph.labelling.BitStreamArcLabelledImmutableGraph;

import java.io.IOException;

/**
 * Saves graph to BVG format without weights
 * {@link fr.inria.maestro.lga.graph.model.impl.bvg.WebGraphBVG WebGraphBVG} or
 * with weights {@link fr.inria.maestro.lga.graph.model.impl.bvg.WebGraphWithArcLabels}.
 * The info about bvg format see at {@see it.unimi.dsi.webgraph.BVGraph}.
 */
public class BVGGraphSaver implements ISaver {
    private final static String GRAPH_SUFFIX = ".graph";
    private final static String GRAPH_SUFFIX_WEIGHTS = "labels";

    @Override
    public void save(final IGraph graph, final String fileName) throws IOException {
       helperSaver(false, fileName, graph);
    }

    public void bigGraphSaver(IArcWeightedGraph sortedGraph ,String fileName) throws IOException {
        String normalPath = fileName.replace(GRAPH_SUFFIX, "").replace("\\", "//");
        BVGGraphWrapper bvgw = new BVGGraphWrapper(sortedGraph);
        BVGraph.store(bvgw, normalPath);
        ArcLaballedGraph arcLab = (ArcLaballedGraph)sortedGraph.getProperties().getValue(IGraph.UNDERLYING_GRAPH);
        BitStreamArcLabelledImmutableGraph.store(arcLab, (normalPath + GRAPH_SUFFIX_WEIGHTS), normalPath);
    }

    public void saveSortedGraph(final IGraph graph, final String fileName) throws IOException {
       helperSaver(true, fileName, graph);
    }

    private void helperSaver(boolean isGraphSorted, String fileName, IGraph graph) throws IOException  {
        String normalPath = fileName.replace(GRAPH_SUFFIX, "").replace("\\", "//");
        IGraph sortedGraph = graph;
        if (isGraphSorted == false) {
            sortedGraph = SimpleAlgorithmes.sortSuccessors(graph);
        }
        BVGGraphWrapper bvgw = new BVGGraphWrapper(sortedGraph);
        BVGraph.store(bvgw, normalPath);

        //BVGGraphWrapper is a wrapper class

        if (graph instanceof IArcWeightedGraph) {

            int[][] matrix = new int[graph.numNodes()][];
            double[][] weights = new double[graph.numNodes()][];
            for (int i = 0; i < graph.numNodes(); i++) {
                matrix[i] = sortedGraph.successorArray(i);
                weights[i] = ((IArcWeightedGraph) sortedGraph).arcWeights(i);
            }
            BitStreamArcLabelledImmutableGraph.store(
                    new ArcLaballedGraph(matrix, weights), (normalPath + GRAPH_SUFFIX_WEIGHTS), normalPath);
       }
    }
}
