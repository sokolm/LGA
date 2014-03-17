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

import fr.inria.maestro.lga.clustering.IClusterEntry;
import fr.inria.maestro.lga.clustering.IPropertiesHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents entry item.
 */
public class ClusterEntryImpl implements IClusterEntry {
    private final String name;
    private final IPropertiesHolder holder;

    /**
     * Creates cluster entry.
     * @param name - the name of entry
     * @param holder - the property of entry
     */
    public ClusterEntryImpl(final String name, final IPropertiesHolder holder) {
        this.name = name;
        this.holder = holder;
    }

    /**
     * Returns the name of entry.
     * @return the name of entry
     */
    @NotNull
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the properties.
     * @return the properties holder.
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
        final ClusterEntryImpl that = (ClusterEntryImpl) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
