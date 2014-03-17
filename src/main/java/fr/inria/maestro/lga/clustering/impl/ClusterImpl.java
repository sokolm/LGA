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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import fr.inria.maestro.lga.clustering.ICluster;
import fr.inria.maestro.lga.clustering.IClusterEntry;
import fr.inria.maestro.lga.clustering.IPropertiesHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents class in the classification.
 */
public class ClusterImpl implements ICluster {
    private final String name;
    private final ImmutableList<IClusterEntry> entries;
    private final IPropertiesHolder holder;
    private final ImmutableSet<String> entryNames;

    /**
     * Creates class of classification.
     * @param name - the name of class
     * @param entries  - the entries of class
     * @param holder  - the properties of class
     */
    public ClusterImpl(final String name, final ImmutableList<IClusterEntry> entries, final IPropertiesHolder holder) {
        this.name = name;
        this.entries = entries;
        this.holder = holder;

        final Builder<String> builder = ImmutableSet.builder();
        for (final IClusterEntry entry : entries) {
            builder.add(entry.getName());
        }

        this.entryNames = builder.build();
    }

    /**
     * Returns the name of class.
     * @return the name of class
     */
    @NotNull
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the list of entries.
     * @return the list of entries.
     */
    @NotNull
    @Override
    public ImmutableList<IClusterEntry> getEntries() {
        return entries;
    }

    /**
     * Returns the set of entries names.
     * @return the set of entries names
     */
    @NotNull
    @Override
    public ImmutableSet<String> getEntriesNames() {
        return entryNames;
    }

    /**
     * Returns the class properties.
     * @return the class properties
     */
    @NotNull
    @Override
    public IPropertiesHolder getProperties() {
        return holder;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClusterImpl cluster = (ClusterImpl) o;
        return name.equals(cluster.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
