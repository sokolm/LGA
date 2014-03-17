package fr.inria.maestro.lga.algo.seeds;

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

import fr.inria.maestro.lga.algo.seeds.impl.*;
import fr.inria.maestro.lga.clustering.IClustering;
import fr.inria.maestro.lga.graph.model.IGraph;
import fr.inria.maestro.lga.graph.model.INodeNamer;

/**
 * Chooses seeds according to centrality measure.
 */
public class SeedChooser {

    /**
     * Chooses K seeds in each class by max degree according to expert estimation.
     * @param graph - the graph
     * @param K - the number of seeds in each class
     * @param expert - the expert estimation
     * @return the seeds
     */
    public static IClustering getMaxDegreeSeeds(IGraph graph, int K, IClustering expert) {
        ICoreFinder finder = new DegreeFinder(graph);
        INodeNamer namer = graph.getProperties().getValue(IGraph.NODE_NAMER);
        SeedChooserHelper chooser = new SeedChooserHelper(K, expert, namer, SeedChooserHelper.MODE_NAME, finder, false);
        return chooser.getSeeds();
    }

    /**
     * Chooses K seeds in each class by max PageRank according to expert estimation.
     * @param graph - the graph
     * @param K - the number of seeds in each class
     * @param expert - the expert estimation
     * @return the seeds
     */
    public static IClustering getMaxPRSeeds(IGraph graph, int K, IClustering expert) {
         ICoreFinder finder = new PagerankFinder(graph);
         INodeNamer namer = graph.getProperties().getValue(IGraph.NODE_NAMER);
         SeedChooserHelper chooser = new SeedChooserHelper(K, expert, namer, SeedChooserHelper.MODE_NAME, finder, false);
         return chooser.getSeeds();
    }

    /**
     * Generates seeds randomly according to expert estimations.
     * @param K - the number of seeds in each class
     * @param expert - the expert estimation
     * @return
     */
    public static IClustering getRandomSeeds(int K, IClustering expert) {
         return RandomGenerator.generateSeeds(expert, K);
    }

    /**
     * Chooses K seeds in each class by min degree according to expert estimation.
     * @param graph - the graph
     * @param K - the number of seeds in each class
     * @param expert - the expert estimation
     * @return the seeds
     */
    public static IClustering getMinDegreeSeeds(IGraph graph, int K, IClustering expert) {
        ICoreFinder finder = new DegreeFinder(graph); //PagerankFinder(graph)
        INodeNamer namer = graph.getProperties().getValue(IGraph.NODE_NAMER);
        SeedChooserHelper chooser = new SeedChooserHelper(K, expert, namer, SeedChooserHelper.MODE_NAME, finder, true);
        return chooser.getSeeds();
    }

    /**
     * Chooses K seeds in each class by min PageRank according to expert estimation.
     * @param graph - the graph
     * @param K - the number of seeds in each class
     * @param expert - the expert estimation
     * @return the seeds
     */
    public static IClustering getMinPRSeeds(IGraph graph, int K, IClustering expert) {
        ICoreFinder finder = new PagerankFinder(graph);
        INodeNamer namer = graph.getProperties().getValue(IGraph.NODE_NAMER);
        SeedChooserHelper chooser = new SeedChooserHelper(K, expert, namer, SeedChooserHelper.MODE_NAME, finder, true);
        return chooser.getSeeds();
    }

}
