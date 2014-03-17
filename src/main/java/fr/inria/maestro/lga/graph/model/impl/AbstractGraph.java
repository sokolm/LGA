package fr.inria.maestro.lga.graph.model.impl;

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

import fr.inria.maestro.lga.graph.model.IGraph;
import fr.inria.maestro.lga.graph.model.property.IProperties;
import fr.inria.maestro.lga.graph.model.property.PropertyKey;
import fr.inria.maestro.lga.graph.model.property.impl.PropertiesImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

/**
 * Provides access to graph properties.
 */
public abstract class AbstractGraph implements IGraph {
    private final IProperties properties;
   
    protected AbstractGraph(final IProperties properties) {
        this.properties = properties;
    }

    protected AbstractGraph() {
        this.properties = new PropertiesImpl(Collections.<PropertyKey<?>, Object>emptyMap());
    }

    protected AbstractGraph(final Map<PropertyKey<?>, Object> properties) {
        this.properties = new PropertiesImpl(properties);
    }

    /**
     *@return a property of the graph
     */
    @NotNull
    public IProperties getProperties() {
        return properties;
    }
}
