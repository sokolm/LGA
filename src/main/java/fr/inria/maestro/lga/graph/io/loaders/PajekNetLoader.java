package fr.inria.maestro.lga.graph.io.loaders;

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
import fr.inria.maestro.lga.graph.model.INodeNamer;
import fr.inria.maestro.lga.graph.model.impl.nodenamer.NodeNamerImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Loads files from Pajek Net Format.
 */
public class PajekNetLoader implements ILoader {
    private final static String VERTICES_PREFIX = "*Vertices";
    private final static String EDGES_PREFIX = "*Edges";
    private final static String ARCS_PREFIX = "*arcs";

@Override
    public IGraph load(final File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        int numNodes = 0;
        //vertex number
        line = br.readLine();
        if (line.startsWith(VERTICES_PREFIX)) {
             numNodes = Integer.parseInt(line.substring(VERTICES_PREFIX.length()).trim());
        } else {
            throw new RuntimeException("Wrong Pajek NET file format.");
        }
        //check if file contains vertex names
        INodeNamer namer = null;
        if (!isEdgeMarker(line)) {
            String[] names = new String[numNodes];
            while (!isEdgeMarker(line = br.readLine())) {
                String[] parts = line.replace("\t", " ").split(" ");
                int id = Integer.parseInt(parts[0].trim())-1;
                String name = parts[1].trim();
                names[id] = name;
            }
            namer = NodeNamerImpl.create(Arrays.asList(names));
        }
        //line contains edges start marker
        IGraph graph = LoaderUtils.parseEdges(numNodes, br);
        if (namer != null) {
            graph.getProperties().setValue(IGraph.NODE_NAMER, namer);
        }
        return graph;
    }


    private boolean isEdgeMarker(String line) {
        return line.startsWith(EDGES_PREFIX) || line.startsWith(ARCS_PREFIX);
    }
}
