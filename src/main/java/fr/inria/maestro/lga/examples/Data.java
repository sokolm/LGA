package fr.inria.maestro.lga.examples;

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

import fr.inria.maestro.lga.clustering.IClustering;
import fr.inria.maestro.lga.clustering.impl.ClusteringUtils;
import fr.inria.maestro.lga.graph.io.loaders.BVGGraphLoader;
import fr.inria.maestro.lga.graph.io.loaders.MatLabLoader;
import fr.inria.maestro.lga.graph.model.IGraph;
import fr.inria.maestro.lga.graph.model.INodeNamer;
import fr.inria.maestro.lga.graph.model.impl.nodenamer.IdenticalIdNodeNamer;
import fr.inria.maestro.lga.graph.model.impl.nodenamer.NodeNamerImpl;

import java.io.File;
import java.io.IOException;

/**
 * Example on how to load graph, clustering and store classification.
 */
public class Data {

    public static IGraph loadGraphBVG(String dir, String name) {
        return loadGraphBVG(dir, name, null);
    }

    public static IGraph loadGraphMatlab(File graphFile) {
        IGraph graph = null;
        try {
            graph = new MatLabLoader().load(graphFile);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return graph;
    }

    public static IClustering loadClustering(File file) {
        IClustering clustering = null;
        try {
            clustering = ClusteringUtils.loadFromFile(file);
        } catch(IOException ex) {
              ex.printStackTrace();
        }
        return clustering;
    }

    public static IGraph loadGraphBVG(String dirG, String nameG, String namerG) {
        IGraph graph = null;
        try {
           graph = new BVGGraphLoader().load(new File(dirG + nameG));
           if (namerG != null) {
               INodeNamer namer = NodeNamerImpl.load(new File(dirG + namerG));
               graph.getProperties().setValue(IGraph.NODE_NAMER, namer);
           } else {
               graph.getProperties().setValue(IGraph.NODE_NAMER, new IdenticalIdNodeNamer());
           }
        } catch( IOException ex) {
            ex.printStackTrace();
        }
        return graph;
    }

}
