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
import fr.inria.maestro.lga.clustering.analysis.impl.ClusteringPreprocessor;
import fr.inria.maestro.lga.clustering.analysis.impl.Modularity;
import fr.inria.maestro.lga.clustering.analysis.impl.PrecisionQuantity;
import fr.inria.maestro.lga.graph.model.IGraph;
import fr.inria.maestro.lga.graph.model.INodeNamer;
import fr.inria.maestro.lga.graph.model.impl.nodenamer.IdenticalIdNodeNamer;
import fr.inria.maestro.lga.utils.collection.Pair;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.PrintWriter;

/**
 * Loads graph in sparse matlab format: 2010 nodes and 19580 edges.
 * Loads expert estimation of the graph.
 * Chooses seeds by max degree according to expert estimation.
 * Classifies graph by SSL method with predefined sigma and alpha.
 * Computes micro-precision and micro-recall according to expert estimation.
 * Computes modularity for classification obtained from SSL method.
 */
public class SSLMExample {

    public static void main(String[] args) {
        final File graphFile = FileUtils.toFile(SSLMExample.class.getResource("/graph/matlab/graph.txt"));
        final File expertFile = FileUtils.toFile(SSLMExample.class.getResource("/graph/matlab/clusters.txt"));
        //System.out.println(graphFile.getAbsolutePath());
        double sigma = 0.5;
        double alpha = 0.85;
        int numberOfSeeds = 5;
        IGraph graph = Data.loadGraphMatlab(graphFile);
        System.out.println("nodes:" + graph.numNodes() + " arcs:" + graph.numArcs());

        graph.getProperties().setValue(IGraph.NODE_NAMER, new IdenticalIdNodeNamer());
        IClustering expert =  Data.loadClustering(expertFile);
        IClustering seeds = SeedChooser.getMaxDegreeSeeds(graph, numberOfSeeds, expert);

        IClustering algoCl = doClassification(graph, seeds, sigma, alpha);
        doEsteem(graph, expert, algoCl);
    }


    public static IClustering doClassification(IGraph graph, IClustering seeds, double sigma, double alpha) {
        INodeNamer namer = graph.getProperties().getValue(IGraph.NODE_NAMER);

        ICalculator pc = CalculatorFactory.getGeneralCalculator(graph,
                new CalculatorInputDataSigma(sigma, alpha));

        //convert seedsInput to personal vector
        ISeedsInput seedsInput = ClusteringConverter.clustering2SeedsInput(seeds, namer, graph.numNodes());

        //launch ss method
        SemiSupervisedLearningMethod sslm = new SemiSupervisedLearningMethod();
        int[] nodeToClassId = sslm.run(seedsInput, pc);
        IClustering resultCl = ClusteringConverter.array2Custering(nodeToClassId, namer, seeds);
        return resultCl;
    }

    public static void doEsteem(IGraph graph, IClustering expert, IClustering algoCl) {
        //compute precision and recall
        PrecisionQuantity quantity = new PrecisionQuantity(new ClusteringPreprocessor(expert));
        ClusteringPreprocessor cpAlgoCl = new ClusteringPreprocessor(algoCl);
        Pair<Double, Double> precision =
                        quantity.getPrecisionRecall(cpAlgoCl);
        System.out.println("Precision:"+precision.getFirst()+" Recall:"+precision.getSecond());

        quantity.printConfusionMatrix(cpAlgoCl, new PrintWriter(System.out));
        INodeNamer namer = graph.getProperties().getValue(IGraph.NODE_NAMER);

        //compute modularity
        Modularity modularity = new Modularity(graph, namer);
        double modularity_res =  modularity.getQuantity(algoCl);

        System.out.println("Modularity:"+modularity_res);

    }
}
