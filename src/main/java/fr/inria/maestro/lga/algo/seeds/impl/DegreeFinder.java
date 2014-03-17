package fr.inria.maestro.lga.algo.seeds.impl;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Finds degree and sorts nodes by it in decreasing order.
 */
public class DegreeFinder implements ICoreFinder {
    protected IGraph myGraph;
    //cashed value
    private double[] myDegrees;
    private List<MeasuredNode> myAllCores;


    public DegreeFinder(IGraph graph) {
        this.myGraph = graph;
        this.myDegrees = new double[graph.numNodes()];
        myAllCores = new ArrayList<MeasuredNode>(graph.numNodes());
    }

    public double[] getCentralityMeasure() {
    	return myDegrees;
    }

    /***
     * Returns a list of nodes sorted by degree.
     * @return list of sorted degrees
     */
    public List<MeasuredNode> getCores() {
    	return Collections.unmodifiableList(myAllCores);
    }

    /**
     * Finds and returns a list of nodes sorted by degree.
     * @return list of sorted degrees
     * */
    public List<MeasuredNode> findCores() {
        IGraph graph = myGraph;
        for (int nodeID = 0; nodeID < graph.numNodes(); nodeID++) {
            this.myDegrees[nodeID] = graph.outdegree(nodeID);
            myAllCores.add(new MeasuredNode(nodeID, myDegrees[nodeID]));
        }
        //sort in decrease
        Collections.sort(myAllCores);
        return myAllCores;
    }
}
