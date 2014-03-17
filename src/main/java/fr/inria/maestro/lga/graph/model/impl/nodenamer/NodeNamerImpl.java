package fr.inria.maestro.lga.graph.model.impl.nodenamer;

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

import fr.inria.maestro.lga.graph.model.INodeNamer;
import fr.inria.maestro.lga.utils.collection.CollectionUtils;
import fr.inria.maestro.lga.utils.io.Encoding;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides an access to node name and vise versa.
 */
public class NodeNamerImpl implements INodeNamer {
    private static final String NAMES_SUFFIX = "_node_names.txt";

    private final Object2IntMap<String> label2Id;
    private final Int2ObjectMap<String> id2Label;

    /**
     * creates a File reference to bvgGraph_node_names.txt when the path is given
     *
     * @param bvgGraphPath
     * @return File
     */
    public static File makeNodeNamesFile(final String bvgGraphPath) {
        return new File(bvgGraphPath + NAMES_SUFFIX);
    }

    /**
     * return the Iloaded(created) graph from a given file path
     *
     * @param graphFilePath
     * @return INodeNamer
     * @throws IOException
     */
    public static INodeNamer loadForGraph(final String graphFilePath) throws IOException {
        return NodeNamerImpl.load(makeNodeNamesFile(graphFilePath));
    }

    public static void saveForGraph(final String graphFilePath, final List<String> lines) throws IOException {
        save(makeNodeNamesFile(graphFilePath), lines);
    }

    public static void save(File saveTo, final INodeNamer nodenamer, int numNodes) throws IOException {
        final List<String> lines = new LinkedList<String>();
        for (int i = 0; i < numNodes; i++) {
            lines.add(nodenamer.getNodeName(i));
        }

        save(saveTo, lines);
    }

    /**
     * writes the file from a given List
     *
     * @param file
     * @param lines
     * @throws IOException
     */
    public static void save(final File file, final List<String> lines) throws IOException {
        final PrintWriter os = new PrintWriter(file, Encoding.DEFAULT_CHARSET_NAME);    //default charset is utf-8
        try {
            for (final String line : lines) {
                os.println(line);
            }
        } finally {
            os.close();
        }
    }

    /**
     * Instantiate NodeNamerImple and return it from a given list
     *
     * @param lines
     * @return INodeNamer
     */
    public static INodeNamer create(final List<String> lines) {
        return new NodeNamerImpl(lines);
    }

    /**
     * List of lines are generated from a given file. then this list is used to generate a NodeNamerImpl
     *
     * @param file
     * @return INodeNamer
     * @throws IOException
     */
    public static INodeNamer load(final File file) throws IOException {
        final int numNodes = countLines(file, Encoding.DEFAULT_CHARSET_NAME);

        final Int2ObjectOpenHashMap<String> id2Label = new Int2ObjectOpenHashMap<String>(numNodes);
        final Object2IntOpenHashMap<String> label2Id = new Object2IntOpenHashMap<String>(numNodes);

        final LineIterator it = FileUtils.lineIterator(file, Encoding.DEFAULT_CHARSET_NAME);
        try {
            int curId = 0;
            while(it.hasNext()) {
                final String nodeName = it.next();
                id2Label.put(curId, nodeName);
                label2Id.put(nodeName, curId);
                curId++;
            }

            return new NodeNamerImpl(id2Label, label2Id);
        } finally {
            it.close();
        }
    }

    /**
     * Private constructor of NodeNamerImpl
     * @param lines
     */
    private NodeNamerImpl(final List<String> lines) {
        this.id2Label = new Int2ObjectOpenHashMap<String>(lines.size());
        this.label2Id = new Object2IntOpenHashMap<String>(lines.size());

        int count = 0;
        for (final String label : lines) {
            id2Label.put(count, label);
            label2Id.put(label, count);
            count++;
        }

    }


    /**
     * private constructor for NodeNamerImple
     *
     * @param id2Label
     * @param label2Id
     */
    private NodeNamerImpl(final Int2ObjectMap<String> id2Label, final Object2IntMap<String> label2Id) {
        this.id2Label = id2Label;
        this.label2Id = label2Id;
    }

    /*getters and setters*/

    @NotNull
    public String getNodeName(final Integer key) {
        return CollectionUtils.forceNotNull(id2Label.get(key));
    }

    @NotNull
    public Integer getKey(final String value) {
        if (!label2Id.containsKey(value)) {
            throw new NullPointerException("Undefined id for: " + value);
        }
        return CollectionUtils.forceNotNull(label2Id.get(value));
    }

    @NotNull
    @Override
    public String getNodeName(final int node) {
        return CollectionUtils.forceNotNull(id2Label.get(node));
    }

    @Override
    public int getNodeId(final String name) {
        if (!label2Id.containsKey(name)) {
            return -1;
        }

        return label2Id.get(name);
    }

    private static int countLines(final File file, final String encoding) throws IOException {
        int count = 0;
        final LineIterator it = FileUtils.lineIterator(file, encoding);
        try {
            while(it.hasNext()) {
                it.next();
                count++;
            }
        } finally {
            it.close();
        }

        return count;
    }
}
