package fr.inria.maestro.lga.clustering.analysis.impl;

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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import fr.inria.maestro.lga.clustering.ICluster;
import fr.inria.maestro.lga.clustering.IClusterEntry;
import fr.inria.maestro.lga.clustering.IClustering;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * This class for fast operation, could be used for small classification.
 * For example take  all classes for an entry. 
 */
public class ClusteringPreprocessor {
    private final IClustering clustering;
    private final Multimap<String, ICluster> clusterEntryName2Cluster;
    private final Multimap<String, IClusterEntry> clusterEntryName2ClusterEntry;
    private final Map<String, ICluster> clusterName2Cluster;

    public ClusteringPreprocessor(final IClustering clustering) {
        this.clustering = clustering;
        this.clusterEntryName2Cluster = HashMultimap.create();
        this.clusterName2Cluster = Maps.newHashMap();
        this.clusterEntryName2ClusterEntry = HashMultimap.create();

        for (final ICluster cluster : clustering.getClusters()) {
            for(final IClusterEntry clusterEntry : cluster.getEntries()) {
                clusterEntryName2Cluster.put(clusterEntry.getName(), cluster);
                clusterEntryName2ClusterEntry.put(clusterEntry.getName(), clusterEntry);
            }

            clusterName2Cluster.put(cluster.getName(), cluster);
        }
    }

    @NotNull
    public IClustering getClustering() {
        return clustering;
    }

    @NotNull
    public Collection<ICluster> getClustersByEntryName(final String name) {
        return Collections.unmodifiableCollection(clusterEntryName2Cluster.get(name));
    }

    @Nullable
    public ICluster getClusterByName(final String name) {
        return clusterName2Cluster.get(name);    
    }

    @NotNull
    public Collection<IClusterEntry> getClusterEntryByName(final String name) {
        return  Collections.unmodifiableCollection(clusterEntryName2ClusterEntry.get(name));   
    }
}
