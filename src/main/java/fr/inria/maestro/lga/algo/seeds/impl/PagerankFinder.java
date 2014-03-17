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

import fr.inria.maestro.lga.algo.ppr.CalculatorFactory;
import fr.inria.maestro.lga.algo.ppr.ICalculator;
import fr.inria.maestro.lga.algo.ppr.params.CalculatorInputData;
import fr.inria.maestro.lga.graph.model.IGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates list of nodes sorted by pagerank
 **/
public class PagerankFinder implements ICoreFinder {
    protected IGraph myGraph;
    //cashed value
    private double[] myPagerank;
    private List<MeasuredNode> myAllCores = new ArrayList<MeasuredNode>();
    //parameters to compute pr
    private CalculatorInputData inputDataCalculator;

    public PagerankFinder(IGraph graph) {
        this.myGraph = graph;
        inputDataCalculator = new CalculatorInputData();
    }

    public PagerankFinder(IGraph graph, CalculatorInputData id) {
        this.myGraph = graph;
        this.inputDataCalculator = id;
    }

    public double[] getCentralityMeasure() {
    	return myPagerank;
    }

    public List<MeasuredNode> getCores() {
    	return myAllCores;
    }

    /**
     * @return list of sorted pagerank
     * */
    public List<MeasuredNode> findCores() {
        IGraph graph = myGraph;
        int numNodes = graph.numNodes();
        //compute pagerank
        ICalculator pagerankCalculator = CalculatorFactory.getPageRankCalculator(graph, inputDataCalculator);
        myPagerank = pagerankCalculator.compute();

        for (int nodeID = 0; nodeID < numNodes; nodeID++) {
            double pr = myPagerank[nodeID];
            myAllCores.add(new MeasuredNode(nodeID, pr));
        }
        Collections.sort(myAllCores);
        return myAllCores;
    }
}
