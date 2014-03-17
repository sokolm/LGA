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

import com.google.common.collect.ImmutableMap;
import fr.inria.maestro.lga.clustering.IPropertiesHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Stores a map of clustering item properties.
 */
public class PropertiesHolderImpl implements IPropertiesHolder {
    private final ImmutableMap<String, String>  map;

    public static IPropertiesHolder createEmpty() {
        return new PropertiesHolderImpl(ImmutableMap.<String, String>of());
    }

    public PropertiesHolderImpl(final ImmutableMap<String, String> map) {
        this.map = map;
    }

    @NotNull
    @Override
    public Set<String> getKeys() {
        return map.keySet();
    }

    @Override
    public String getStringValue(final String key) {
        return map.get(key);
    }
}
