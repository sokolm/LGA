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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import fr.inria.maestro.lga.clustering.ICluster;
import fr.inria.maestro.lga.clustering.IClusterEntry;
import fr.inria.maestro.lga.clustering.IClustering;
import fr.inria.maestro.lga.clustering.IPropertiesHolder;
import fr.inria.maestro.lga.utils.io.Encoding;
import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 * This class load and save files from/to file in txt fomat.
 * The first line in the file is any name of the claserization.
 * The secound line should be empty.
 * The fird line is the name of th first cluster.
 * The nextInt lines is the sequenses of instances in the cluster.
 * All clusters separated from each other with empty line.
 * Any instance could contain the properties, they are separated with'\t'.
 * Example:
 * The_name_of_the_instance '\t' property1 '\t' property2
 *
 * It will be counted the same as without properties instances.
 */
public class ClusteringUtils {

    /**
     * Stores classification to file.
     * @param clustering - the classification
     * @param file - the file
     * @throws IOException
     */
    public static void saveToFile(final IClustering clustering, final File file) throws IOException {
        final PrintWriter pw = new PrintWriter(file, Encoding.DEFAULT_CHARSET_NAME);
        try {
            pw.println(clustering.getName());
            pw.println();
            for (final ICluster cluster : clustering.getClusters()) {
                pw.print(cluster.getName());
                pw.print("\t");
                printPropeprtyHolder(cluster.getProperties(), pw);
                pw.println();

                for (final IClusterEntry clusterEntry : cluster.getEntries()) {
                    pw.print(clusterEntry.getName());
                    pw.print("\t");
                    printPropeprtyHolder(clusterEntry.getProperties(), pw);
                    pw.println();
                }
                pw.println();
            }
        } finally {
            pw.close();
        }
    }

    /**
     * Loads classification from file.
     * @param file - the file
     * @return  the classification
     * @throws IOException
     */
    public static IClustering loadFromFile(final File file) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Encoding.DEFAULT_CHARSET_NAME));
        try {
            final LoadHelper helper = new LoadHelper(reader);
            final String clusteringName = helper.nextNonEmpty();
            final ImmutableList.Builder<ICluster> clusters = ImmutableList.builder();

            String curCluster;
            while((curCluster = helper.nextNonEmpty()) != null) {
                final ImmutableList.Builder<IClusterEntry> clusterEntries = ImmutableList.builder();
                final String[] curClusterSeparated = curCluster.split("\\t");
                final IPropertiesHolder clusterProperties = constructPropertyHolder(curClusterSeparated, 1);

                String clusterEntry;
                while((clusterEntry = helper.nextLine()) != null && !clusterEntry.isEmpty()) {
                    final String []entries = clusterEntry.split("\\t");

                    final String name = entries[0];
                    final IPropertiesHolder entryProperties = constructPropertyHolder(entries, 1);
                    clusterEntries.add(new ClusterEntryImpl(name, entryProperties));
                }

                clusters.add(new ClusterImpl(curClusterSeparated[0], clusterEntries.build(), clusterProperties));
            }

            return new ClusteringImpl(clusteringName, clusters.build());

        } finally {
            reader.close();
        }
    }


    private static void printPropeprtyHolder(final IPropertiesHolder holder, final PrintWriter pw) throws IOException {
        for (final String str : Sets.newTreeSet(holder.getKeys())) {
            final String value = holder.getStringValue(str);
            pw.print(str);
            pw.print("\t");
            pw.print((value != null) ? value : "");
            pw.print("\t");
        }

    }

    private static IPropertiesHolder constructPropertyHolder(final String[] keyAndValue, final int startFrom) {
        final Builder<String, String> mapBuilder = ImmutableMap.builder();
        for (int i = startFrom; i < keyAndValue.length; i += 2) {
            mapBuilder.put(keyAndValue[i], (i + 1 < keyAndValue.length) ? keyAndValue[i + 1] : "");
        }

        return new PropertiesHolderImpl(mapBuilder.build());
    }

    private static class LoadHelper {
        private final BufferedReader reader;

        public LoadHelper(final BufferedReader reader) {
            this.reader = reader;
        }

        @Nullable
        public String nextLine() throws IOException {
            final String line =  reader.readLine();
            return (line != null) ? line.trim() : null;
        }

        @Nullable
        public String nextNonEmpty() throws IOException {
            String line;
            while ((line = reader.readLine()) != null && line.isEmpty()) { }
            return line;
        }
    }

    private ClusteringUtils() { }
}
