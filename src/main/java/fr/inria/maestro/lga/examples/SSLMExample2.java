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

import fr.inria.maestro.lga.algo.ppr.CalculatorFactory;
import fr.inria.maestro.lga.algo.ppr.ICalculator;
import fr.inria.maestro.lga.algo.ppr.params.CalculatorInputDataSigma;
import fr.inria.maestro.lga.algo.seeds.SeedChooser;
import fr.inria.maestro.lga.algo.sslm.ClusteringConverter;
import fr.inria.maestro.lga.algo.sslm.ISeedsInput;
import fr.inria.maestro.lga.algo.sslm.SemiSupervisedLearningMethod;
import fr.inria.maestro.lga.clustering.IClustering;
import fr.inria.maestro.lga.graph.model.IArcWeightedGraph;
import fr.inria.maestro.lga.graph.model.IGraph;
import fr.inria.maestro.lga.graph.model.INodeNamer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Marina
 * Date: 2/20/14
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class SSLMExample2 {

    public static void main(String[] args) throws IOException{
        //load graph
        int numberOfSeeds = 1;
        double alpha = 0.85;
        double sigma = 0.0;
        String shortLink = "/graph/bvg/";
        URL url = SSLMExample2.class.getResource(shortLink);
        String dir =url.getPath();
        String name = "LesMisrablesWeighted";
        String namer = "LesMisrablesNames.txt";
        String expertName = "LesMiserablesExpert.txt";
        final File expertFile = FileUtils.toFile(SSLMExample.class.getResource(shortLink+expertName));

        IClustering expert =  Data.loadClustering(expertFile);
        IGraph graph = Data.loadGraphBVG(dir, name, namer);
        System.out.println("nodes="+graph.numNodes()+ " edges="+graph.numArcs());
        System.out.println("WeightedGraph:" + (graph instanceof IArcWeightedGraph));
        IClustering seeds = SeedChooser.getMaxPRSeeds(graph, numberOfSeeds, expert);

        System.out.println("\n***Estimations for weighted graph:");
        IClustering algoCl = doClassificationWeightedGraph(graph, seeds, sigma, alpha);
        SSLMExample.doEsteem(graph, expert, algoCl);

        System.out.println("\n***Estimations for unweighted graph:");
        IClustering algoCl2 = SSLMExample.doClassification(graph, seeds, sigma, alpha);
        SSLMExample.doEsteem(graph, expert, algoCl2);
    }

    public static IClustering doClassificationWeightedGraph(IGraph graph, IClustering seeds, double sigma, double alpha) {
        INodeNamer namer = graph.getProperties().getValue(IGraph.NODE_NAMER);
        CalculatorInputDataSigma cids = new CalculatorInputDataSigma(sigma, alpha);

        //by default the classification always for unweighted graph
        //it should be marked as weighted
        cids.setWeighted(true);

        ICalculator pc = CalculatorFactory.getGeneralCalculator(graph, cids);
        //convert seedsInput to personal vector
        ISeedsInput seedsInput = ClusteringConverter.clustering2SeedsInput(seeds, namer, graph.numNodes());

        //launch ss method
        SemiSupervisedLearningMethod sslm = new SemiSupervisedLearningMethod();
        int[] nodeToClassId = sslm.run(seedsInput, pc);
        IClustering resultCl = ClusteringConverter.array2Custering(nodeToClassId, namer, seeds);
        return resultCl;
    }

}
