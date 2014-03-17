package fr.inria.maestro.lga.graph.io.loaders;

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
import fr.inria.maestro.lga.graph.model.impl.bvg.WebGraphBVG;
import fr.inria.maestro.lga.graph.model.impl.bvg.WebGraphWithArcLabels;
import it.unimi.dsi.webgraph.BVGraph;
import it.unimi.dsi.webgraph.labelling.ArcLabelledImmutableGraph;

import java.io.File;
import java.io.IOException;

/**
 * Loads file from bvg format to
 * unweighted {@link fr.inria.maestro.lga.graph.model.impl.bvg.WebGraphBVG WebGraphBVG} object or
 * weighted {@link fr.inria.maestro.lga.graph.model.impl.bvg.WebGraphWithArcLabels} object.
 * The info about bvg format see at {@see it.unimi.dsi.webgraph.BVGraph}.
 */
public class BVGGraphLoader implements ILoader {
    private final static String GRAPH_SUFFIX = ".graph";
    private final static String GRAPH_SUFFIX_WEIGHTS = "labels";
    private final static String GRAPH_SUFFIX_PROPERTIES_WEIGHTS = "labels.properties";


    @Override
    public IGraph load(final File file) throws IOException {
        final String normalPath = file.getCanonicalPath().replace(GRAPH_SUFFIX, "");
        boolean isWeighted = false;

        File weightedBVGFile = new File(normalPath + GRAPH_SUFFIX_PROPERTIES_WEIGHTS);
        if (weightedBVGFile.exists()) {
            isWeighted = true;
        }

        IGraph bvgGraph = null;
        if (isWeighted) {
            bvgGraph = new WebGraphWithArcLabels(ArcLabelledImmutableGraph.load(normalPath + GRAPH_SUFFIX_WEIGHTS));
        } else {
            bvgGraph = new WebGraphBVG(BVGraph.load(normalPath));
        }
        return bvgGraph;
    }
}
