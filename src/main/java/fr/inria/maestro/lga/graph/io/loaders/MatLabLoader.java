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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class loads matLab graph file to unweighted {@link fr.inria.maestro.lga.graph.model.impl.matrix.MatrixGraph MatrixGraph} or
 * weighted {@link fr.inria.maestro.lga.graph.model.impl.matrix.MatrixWeightedGraph MatrixWeightedGraph} object.
 * The enumeration in matlab file starts from 1.
 * An example of the file:
 * nodeId1 nodeId2 weight1
 * or
 * nodeId1 nodeId2
 */
public class MatLabLoader implements ILoader {

    @Override
    public IGraph load(final File file) throws IOException {
        // Using class to find maxID and know isWeighted
        ComputeHelper helper = preprocessor(file);
        int maxID = helper.getMaxId();
        BufferedReader br = new BufferedReader(new FileReader(file));
        return LoaderUtils.parseEdges(maxID, br);
    }



    // Inner class which helps to find maxId and check isWeightrd
    private class ComputeHelper {
        public int getMaxId() {
            return maxId;
        }

        public void setMaxId(final int maxId) {
            this.maxId = maxId;
        }

        private int maxId;
    }

    // Returns maxId and check if graph is weighted
    private ComputeHelper preprocessor(final File fileName) throws IOException {
        ComputeHelper helper = new ComputeHelper();
        int maxID = 0;
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = line.replace("\t", " ");
            String[] splitArray = line.split(" ");
            int node1 = Integer.parseInt(splitArray[0].trim());
            int node2 = Integer.parseInt(splitArray[1].trim());
            if (node1 > maxID) {
                maxID = node1;
            }
            if (node2 > maxID) {
                maxID = node2;
            }
        }
        br.close();
        helper.setMaxId(maxID);
        return helper;
    }
}
