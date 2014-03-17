package fr.inria.maestro.lga.algo.seeds.impl;

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

/**
 * Stores node id with centrality measure.
 */
public class MeasuredNode implements Comparable {
    private int nodeId;
    private double measure;

    MeasuredNode(int id, double measure) {
        this.nodeId = id;
        this.measure = measure;
    }

    public int compareTo(Object o) {
        MeasuredNode wl = (MeasuredNode) o;
        if (measure < wl.measure) return 1;
        if (measure > wl.measure) return -1;
            return 0;
    }

    int getId() {
        return nodeId;
    }

    double getMeasure() {
        return measure;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof MeasuredNode) {
            MeasuredNode objScoredNode = (MeasuredNode) obj;
            if (nodeId == objScoredNode.nodeId && measure == objScoredNode.measure) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return (int)(nodeId * measure + nodeId);
    }

     public String toString() {
        return "" + nodeId + "  "+measure;
     }

}
