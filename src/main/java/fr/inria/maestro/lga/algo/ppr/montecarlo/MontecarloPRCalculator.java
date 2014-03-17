package fr.inria.maestro.lga.algo.ppr.montecarlo;

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

import fr.inria.maestro.lga.algo.ppr.ICalculator;
import fr.inria.maestro.lga.graph.model.IGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
/**
 * Implementation of monte carlo personal pagerank calculation.
 * */
public class MontecarloPRCalculator implements ICalculator {
	private int myMaxIter;
	private IGraph myGraph;
	private double myC = 0.5;
    private Random rnd = new Random();

	/**
	 * @param graph - graph for which computer personal pagerank
	 * @param maxIt - max iteration of randomseeds walk
	 * @param dmpFactor - damping factor (by default 0.5)
	 */
	public MontecarloPRCalculator(IGraph graph, int maxIt, double dmpFactor) {
		this.myGraph = graph;
		this.myMaxIter = maxIt;
		this.myC = dmpFactor;
	}

	/**
	 * @param curNode - the node for which computed personal pagerank
	 */
	public double[] compute(int curNode) {
		double[] ppagerank = new double[myGraph.numNodes()];
		int currentNode;
		double[] pagerank = new double[myGraph.numNodes()];

		for (int i = 0; i < myMaxIter; i++) {
			currentNode = curNode;
			pagerank[curNode] = pagerank[curNode] + 1;
			while ( rand() <= myC) {

				int outdegree = this.myGraph.outdegree(currentNode);
				if (outdegree == 0) {
					currentNode = curNode;
				} else {
				    int[] successors = this.myGraph.successorArray(currentNode);
				    int index = (int)(rand()*outdegree);
				    currentNode = successors[index];
                    pagerank[currentNode] += 1;
				}

			}
		}
        pagerank =  normalizePPR(pagerank);

		return  pagerank;
	}



    private double[] monte_carlo(int curNode, int maxIt, double[] pagerank) {
        int currentNode;
	    for (int i = 0; i < maxIt; i++) {
			currentNode = curNode;
			pagerank[curNode] = pagerank[curNode] + 1;
			while ( rand() <= myC) {

				int outdegree = this.myGraph.outdegree(currentNode);
				if (outdegree == 0) {
					currentNode = curNode;
				} else {
				    int[] successors = this.myGraph.successorArray(currentNode);
				    int index = (int)(rand()*outdegree);
				    currentNode = successors[index];
                    pagerank[currentNode] += 1;
				}

			}
		}
        return pagerank;
	}        

	
	//randomseeds value from 0 till 1
	private double rand() {
		return rnd.nextDouble();
	}
	
	//index-number of node value - pr value 
	public double[] compute() {
		return null;
	}
	
	/**
	 * @return array of page rank
	 * index - node's id
	 * value - page rank of this node 
	 */
	public double[] compute(double[] personalPR) {
        double[] ppr = null;
        List<PRStructure> list = new LinkedList<PRStructure>();
        for (int i = 0; i < personalPR.length; i++)  {
             if (personalPR[i] != 0) {
                 list.add(new PRStructure(i, personalPR[i]));
             }
        }

        if (list.size() == 1) {
             ppr = compute(list.get(0).nodeId);
        } else {
            ppr = new double[myGraph.numNodes()];
            for (int i = 0; i < list.size(); i++) {
                PRStructure prst = list.get(i);
                int tmp_it = (int)Math.round(prst.prWeight * myMaxIter);
                monte_carlo(prst.nodeId, tmp_it, ppr);
            }
        }
        ppr =  normalizePPR(ppr);
        return ppr;
	}


    private class PRStructure {
        int nodeId;
        double prWeight;

        PRStructure(int node, double prw) {
             this.nodeId = node;
            this.prWeight = prw;
        }
    }


    private double[] normalizePPR(double[] ppr) {
        double summ = 0.0;
        for (int i = 0; i < ppr.length; i++) {
            summ += ppr[i];
        }
        for (int i = 0; i < ppr.length; i++) {
            ppr[i] = ppr[i]/summ;
        }

        return ppr;
    }
}
