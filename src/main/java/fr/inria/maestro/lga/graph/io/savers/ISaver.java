package fr.inria.maestro.lga.graph.io.savers;

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

import java.io.IOException;

/**
 * Save graph in the file.
 */
public interface ISaver {

    /***
     * Save graph in the file
     * @param graph - the graph representation
     * @param fileName - the file to store the graph
     * @throws IOException
     */
    void save(IGraph graph, String fileName) throws IOException;
    
}
