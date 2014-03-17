package fr.inria.maestro.lga.utils.formatters;

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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Formatter for doubles.
 */
public class DoubleFormatter {
    private static final DecimalFormat decimalFormatter = new DecimalFormat("###.#####", new DecimalFormatSymbols(Locale.US));

    private DoubleFormatter() {
    }

    public static String formatDouble(final double value) {
        return decimalFormatter.format(value);
    }

    public static double parseDouble(final String value) {
        return Double.parseDouble(value);
    }

    public static double formatDoubleToDouble(final double value) {
         return Double.parseDouble(decimalFormatter.format(value));
    }
}
