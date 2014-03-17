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
import fr.inria.maestro.lga.graph.model.impl.matrix.MatrixGraph;
import fr.inria.maestro.lga.graph.model.impl.matrix.MatrixWeightedGraph;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Helps to parse part of a file  in format "id1 id2 weight".
 */
class LoaderUtils {
    /**
     * Read edges in format "id1 id2 w3"* or "id1 id2" up to the end of file.
     * Closes buffer reader.
     */
    public static IGraph parseEdges(int numNodes, BufferedReader br) throws IOException {
        IntList[] nodes = new IntList[numNodes];
        DoubleList[] weightsList = new DoubleList[numNodes];
        String line = null;
        //Initialization of IntList and weigthsList
        for (int i = 0; i < numNodes; i++) {
            nodes[i] = new IntArrayList();
            weightsList[i] = new DoubleArrayList();
        }

        while ((line = br.readLine()) != null) {
            String[] splitArray = line.replace("\t", " ").split(" ");
            int node1 = Integer.parseInt(splitArray[0]);
            int node2 = Integer.parseInt(splitArray[1]);
            nodes[node1 - 1].add(node2 - 1); // Numeration in matLab file begins from 1, whether in java array from 0
            if (splitArray.length == 2) {
                weightsList[node1 - 1].add(1);
            } else {
                weightsList[node1 - 1].add(Double.parseDouble(splitArray[2]));
            }
        }
        br.close();
        boolean isWeighted = false;
        int[][] matrix = new int[numNodes][];
        double[][] weights = new double[numNodes][];
        for (int i = 0; i < numNodes; i++) {
            matrix[i] = new int[nodes[i].size()];
            weights[i] = new double[weightsList[i].size()];
            IntIterator intIterator = nodes[i].iterator();
            int count = 0;
            while (intIterator.hasNext()) {
                matrix[i][count] = intIterator.next();
                count++;
            }
            count = 0;
            DoubleIterator doubleIterator = weightsList[i].iterator();
            while (doubleIterator.hasNext()) {
                weights[i][count] = doubleIterator.next();
                if (weights[i][count] != 1) {
                    isWeighted = true;
                }
                count++;
            }
        }
        return isWeighted ? new MatrixWeightedGraph(matrix, weights): new MatrixGraph(matrix);
    }

}
