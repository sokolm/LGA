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

import fr.inria.maestro.lga.clustering.ICluster;
import fr.inria.maestro.lga.clustering.IClusterEntry;
import fr.inria.maestro.lga.clustering.IClustering;
import fr.inria.maestro.lga.clustering.impl.ClusteringBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomGenerator {
     private final static Random rnd = new Random();

     public static IClustering generateSeeds(IClustering expertCl, int K) {
        ClusteringBuilder builderRandomSeeds = new ClusteringBuilder("Random seeds");

        List<ICluster> listOfClusters = expertCl.getClusters();
        for (ICluster cluster: listOfClusters) {

            String name = cluster.getName();
            builderRandomSeeds.addCluster(name);
            List<IClusterEntry> listEntry = cluster.getEntries();
            Set<Integer> randomSeeds = new HashSet<Integer>();
            while (randomSeeds.size() != K && randomSeeds.size() != listEntry.size()) {
                randomSeeds.add(rnd.nextInt(listEntry.size()));
            }

            for (Integer index : randomSeeds) {
                IClusterEntry entry = listEntry.get(index);
                builderRandomSeeds.addEntry(name, entry);
            }
        }
        return builderRandomSeeds.build();
    }
}