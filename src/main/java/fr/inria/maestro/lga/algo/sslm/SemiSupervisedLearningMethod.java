package fr.inria.maestro.lga.algo.sslm;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Provides an implementation of semi-supervised learning method.
 * 1)Computes general semi-supervised method for all classes.
 * 2)Assigns node to class according to max value of general semi-supervised method results.
 * If values are the same for all classes, then marked -1 as unclassified item.
 */
public class SemiSupervisedLearningMethod {

    /**
     * Classifies nodes by semi-supervised learning method.
     * If possible runs method computations in parallel threads.
     * @param seeds - seeds of the classes
     * @param pc - semi-supervised computation method
     * @return the result of classification, index corresponds to node id, value corresponds to class id.
     */
    public int[] run(ISeedsInput seeds, ICalculator pc) {
        final int N =  seeds.numClasses();
        int numAvailableProcessors = Math.max(Runtime.getRuntime().availableProcessors()-1, 1);
        int numThreads = Math.min(numAvailableProcessors, N);
        return ((numThreads==1) ? runSequentially(seeds, pc) : runInParallel(seeds, pc, numThreads));
    }

    /**
     * Runs computation in parallel threads.
     * @param seeds - seeds of the classes
     * @param pc - semi-supervised computation method
     * @param numThreads - predefined number of threads
     * @return the result of classification, index corresponds to node id, value corresponds to class id
     */
    private int[] runInParallel( ISeedsInput seeds, ICalculator pc, int numThreads) {
        final int N =  seeds.numClasses();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future<double[]>> futurePageRanks = new ArrayList<Future<double[]>>(N);
        List<double[]> personalPageranks = new ArrayList<double[]>(N);
        //compute personal general PageRank for all classes
        for (int i = 0; i < N; i++) {
            double[] tmpPV = seeds.getSeedInput(i).getPersonalVector();
            futurePageRanks.add(executorService.submit(new SSLComputation(pc, tmpPV)));
        }

        for (int i = 0; i < N; i++) {
            Future<double[]> result = futurePageRanks.get(i);
            try {
                double[] resultData = result.get();
                personalPageranks.add(resultData);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        executorService.shutdown();
        return assign(personalPageranks);
    }


    /**
     * Sequentially runs computation in one thread.
     * @param seeds - seeds of the classes
     * @param pc - semi-supervised computation method
     * @return the result of classification, index corresponds to node id, value corresponds to class id
     */
    public int[] runSequentially( ISeedsInput seeds, ICalculator pc) {
        final int N =  seeds.numClasses();
        List<double[]> personalPageranks = new ArrayList<double[]>(N);
        //compute personal general PageRank for all classes
        for (int i = 0; i < N; i++) {
            double[] tmpPV = seeds.getSeedInput(i).getPersonalVector();
            personalPageranks.add(pc.compute(tmpPV));
        }
        return assign(personalPageranks);
    }



    //to compute each sslm method by diff thread
    private static class SSLComputation implements Callable<double[]> {
        private final  double[] tmpPV;
        private final ICalculator pc;

        private SSLComputation(ICalculator pc, double[] tmpPV) {
            this.pc =pc;
            this.tmpPV = tmpPV;
        }

        public double[] call() {
            return pc.compute(tmpPV);
         }
    }

    //assign item to class with maximum value of pagerank
    protected int[] assign(List<double[]> personalPageranks) {
        int numNodes = personalPageranks.get(0).length;
        AssignerHelper assigner = new AssignerHelper(numNodes);
        for (int i=0; i < personalPageranks.size(); i++) {
            assigner.identifyToClassHelper(personalPageranks.get(i), i);
        }
        return assigner.getNodeToClassId();
    }

    //helps to assign nodes to classes
    protected static class AssignerHelper {
        private double[] pastMaxPPR;
        private int[] nodeToClassId;

        public AssignerHelper(int numNodes) {
            pastMaxPPR = new double[numNodes];
            nodeToClassId = new int[numNodes];
            Arrays.fill(pastMaxPPR, 0);
            Arrays.fill(nodeToClassId, -1);
        }

        protected void identifyToClassHelper(double[] personalPagerank, int class_id) {
    	//Identify which class
            for (int nodeID = 0; nodeID < personalPagerank.length; nodeID++) {
                double pastPr = pastMaxPPR[nodeID];
                double tmpPr = personalPagerank[nodeID];

                if (pastPr < tmpPr) {
                    pastMaxPPR[nodeID] = tmpPr;
                    nodeToClassId[nodeID] = class_id;
                }
            }
        }

        public int[] getNodeToClassId() {
            return nodeToClassId;
        }
    }
}
