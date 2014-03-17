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

import com.google.common.collect.*;
import com.google.common.collect.ImmutableList.Builder;
import fr.inria.maestro.lga.clustering.ICluster;
import fr.inria.maestro.lga.clustering.IClusterEntry;
import fr.inria.maestro.lga.clustering.IClustering;
import fr.inria.maestro.lga.clustering.IPropertiesHolder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builds classification.
 */
public class ClusteringBuilder {
    private final String clusteringName;
    private final List<String> clustersNamesList;
    private final Set<String> clustersNamesSet;

    private final Multimap<String, IClusterEntry> cluster2Entries;
    private final Map<String, IPropertiesHolder> cluster2PropertiesHolder;

    /**
     * Creates the empty classification.
     * @param clusteringName - the name of classification
     */
    public ClusteringBuilder(final String clusteringName) {
        this.clusteringName = clusteringName;
        this.clustersNamesList = Lists.newArrayList();
        this.clustersNamesSet = Sets.newHashSet();
        this.cluster2Entries = HashMultimap.create();
        this.cluster2PropertiesHolder = Maps.newHashMap();
    }

    /**
     * Adds cluster to classification.
     * @param name - the name of cluster
     * @return the builder object
     */
    public ClusteringBuilder addCluster(final String name) {
        if (clustersNamesSet.contains(name)) {
            throw new IllegalArgumentException("such cluster yet exist");
        }

        clustersNamesSet.add(name);
        clustersNamesList.add(name);

        return this;
    }

    /**
     * Adds entry to class.
     * @param cluster - the class name
     * @param entry - the entry object
     * @return the builder object
     */
    public ClusteringBuilder addEntry(final String cluster, final IClusterEntry entry) {
        checkClusterExists(cluster);
        cluster2Entries.put(cluster, entry);
        return this;
    }

    /**
     * Adds entry to class.
     * @param cluster - the class name
     * @param entryName - the entry name
     * @return the builder object
     */
    public ClusteringBuilder addEntry(final String cluster, final String entryName) {
       return addEntry(cluster, new ClusterEntryImpl(entryName, new PropertiesHolderImpl(ImmutableMap.<String, String>of())));
    }

    /**
     * Adds entry to class.
     * @param cluster - the class name
     * @param entryName - the entry name
     * @param entryProperties - the entry property
     * @return the builder object
     */
    public ClusteringBuilder addEntry(final String cluster, final String entryName, final IPropertiesHolder entryProperties) {
       return addEntry(cluster, new ClusterEntryImpl(entryName, entryProperties));
    }

    /**
     *  Adds property to cluster.
     * @param cluster - the cluster name
     * @param propertiesHolder - the property for cluster
     * @return
     */
    public ClusteringBuilder addPropertyToCluster(final String cluster, final IPropertiesHolder propertiesHolder) {
        checkClusterExists(cluster);
        cluster2PropertiesHolder.put(cluster, propertiesHolder);
        return this;
    }

    private void checkClusterExists(final String cluster) throws IllegalArgumentException {
        if (!clustersNamesSet.contains(cluster)) {
            throw new IllegalArgumentException("such cluster not  yet exist");
        }
    }

    /**
     *  Builds the classification from build object.
     * @return the classification
     */
    public IClustering build() {
        final Builder<ICluster> clustering = ImmutableList.builder();
        for (final String clusterName : clustersNamesList) {
            final Collection<IClusterEntry> entries = cluster2Entries.get(clusterName);
            final IPropertiesHolder clustersProperties = cluster2PropertiesHolder.containsKey(clusterName) ?
                    cluster2PropertiesHolder.get(clusterName) : PropertiesHolderImpl.createEmpty();


            final ImmutableList<IClusterEntry> notNullEntries;
            if (entries == null) {
                notNullEntries = ImmutableList.of();            
            } else {
                notNullEntries = ImmutableList.copyOf(entries);
            }

            clustering.add(new ClusterImpl(clusterName, notNullEntries, clustersProperties));
        }

        return new ClusteringImpl(clusteringName, clustering.build());
    }
}
