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

import fr.inria.maestro.lga.graph.model.IArcWeightedGraph;
import fr.inria.maestro.lga.graph.model.IGraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class saves graph to matlab format without weights or
 * with weightes. The enumeration in matlab file starts from 1.
 * An example of the file:
 * nodeId1 nodeId2 weight1
 * or
 * nodeId1 nodeId2
 */
public class MatLabSaver implements ISaver {

    @Override
    public void save(final IGraph graph, final String fileName) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        int numNodes = graph.numNodes();
        if (graph instanceof IArcWeightedGraph) {
            for (int i = 0; i < numNodes; i++) {
                int[] successors = graph.successorArray(i);
                if (successors != null) {
                    for (int j = 0; j < graph.outdegree(i); j++) {
                        bw.write(i + 1 + " " + (successors[j] + 1) + " " + ((IArcWeightedGraph) graph).arcWeights(i)[j]);
                        bw.newLine();
                    }
                }
            }

        } else {
            for (int i = 0; i < numNodes; i++) {
                int[] successors = graph.successorArray(i);
                if (successors != null) {
                    for (int j = 0; j < graph.outdegree(i); j++) {
                        bw.write(i + 1 + " " + (successors[j] + 1) + " " + 1);
                        bw.newLine();
                    }
                }
            }
        }
        bw.close();
    }
}
