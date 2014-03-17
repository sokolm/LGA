package fr.inria.maestro.lga.graph.model.property.impl;

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

import com.google.common.collect.Maps;
import fr.inria.maestro.lga.graph.model.property.IProperties;
import fr.inria.maestro.lga.graph.model.property.PropertyKey;

import java.util.Map;

/**
 * Stores a map of the graph properties.
 */
public class PropertiesImpl implements IProperties {
    private final Map<PropertyKey<?>, Object> propertiesMap;

    public PropertiesImpl(final Map<PropertyKey<?>, Object> propertiesMap) {
        for (final Map.Entry<PropertyKey<?>, Object> entry : propertiesMap.entrySet()) {
            if (!entry.getKey().getKeyClazz().isAssignableFrom(entry.getValue().getClass())) {
                throw new IllegalArgumentException("Key clazz and value class must be same!");
            }
        }

        this.propertiesMap = Maps.newHashMap();
        this.propertiesMap.putAll(propertiesMap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(final PropertyKey<T> propertyKey) {
        return (T) propertiesMap.get(propertyKey);
    }

   @Override
   public <T> void setValue(final PropertyKey<T> propKey, T value) {
         propertiesMap.put(propKey, value);
   }
}
