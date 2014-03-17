package fr.inria.maestro.lga.graph.io.utils;

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

import it.unimi.dsi.webgraph.labelling.FixedWidthIntLabel;
import it.unimi.dsi.webgraph.labelling.Label;

class WebGraphLabelsFactory {
    private static final int PRECISION = 10000;
    private static final int BIT_WIDTH = 14;

    private static final String KEY_NAME = "width";

    private final static Label[] LABELS = new Label[PRECISION + 1];


    public synchronized static Label getLabel(final double val) {
        if (val > 1. || val < 0.) {
            throw new IllegalArgumentException("supported values only from 0 to 1");
        }
        final int intValue = (int )(val * PRECISION);
        if (LABELS[intValue] == null) {
            LABELS[intValue] = new MyLabel(intValue);
        }

        return LABELS[intValue];
    }

    public synchronized static Label getLabel(final int intValue) {
        if (intValue > PRECISION || intValue < 0) {
            throw new IllegalArgumentException("supported values only from 0 to 1");
        }

        if (LABELS[intValue] == null) {
            LABELS[intValue] = new MyLabel(intValue);
        }

        return LABELS[intValue];
    }


    public static class MyLabel extends FixedWidthIntLabel {
        public MyLabel(final int value) {
            super(KEY_NAME, BIT_WIDTH, value);
        }

        public MyLabel() {
            super(KEY_NAME, BIT_WIDTH);
        }

        @Override
        public double getDouble() {
            return getInt() / (1. * PRECISION);
        }

        @Override
        public float getFloat(final String key) {
            return getInt() / (1.f * PRECISION);
        }

        @Override
        public Label copy() {
            return new MyLabel(value);
        }

        @Override
        public String toSpec() {
            return this.getClass().getName() + "(" + ")";
        }
    }
}
