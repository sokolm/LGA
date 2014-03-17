package fr.inria.maestro.lga.clustering.impl;

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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import fr.inria.maestro.lga.clustering.ICluster;
import fr.inria.maestro.lga.clustering.IClustering;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * Represents the classification.
 */
public class ClusteringImpl implements IClustering {
    private final String name;
    private final ImmutableList<ICluster> clusters;

    /**
     * Creates classification.
     * @param name - the name of classification.
     * @param clusters - the list of classes.
     */
    public ClusteringImpl(final String name, final ImmutableList<ICluster> clusters) {
        this.name = name;
        this.clusters = clusters;

        final Set<String> clusterNames = Sets.newHashSetWithExpectedSize(clusters.size());
        for (final ICluster cluster : clusters) {
            clusterNames.add(cluster.getName());
        }
    }

    /**
     * Returns the name of classification.
     * @return the name of classification
     */
    @NotNull
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the list of classes.
     * @return the list of classes
     */
    @NotNull
    @Override
    public List<ICluster> getClusters() {
        return clusters;
    }
}
