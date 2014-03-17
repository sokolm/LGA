package fr.inria.maestro.lga.clustering;

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

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents cluster. Contains name, entries (i.e. nodes) and properties.
 */
public interface ICluster {
    /**
     * Returns the name of the class.
     * @return the name of the class
     */
    @NotNull
    String getName();

    /**
     * Returns the entries list.
     * @return the entries list
     */
    @NotNull
    List<IClusterEntry> getEntries();

    /**
     * Returns the set of entries names in class.
     * @return the of entries names
     */
    @NotNull
    ImmutableSet<String> getEntriesNames();

    /**
     * The properties of the classification.
     * @return the properties.
     */
    @NotNull
    IPropertiesHolder getProperties();
}   