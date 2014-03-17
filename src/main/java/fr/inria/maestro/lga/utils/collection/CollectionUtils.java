package fr.inria.maestro.lga.utils.collection;

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

import org.jetbrains.annotations.NotNull;

/**
 * Checks if element id is null.
 */
public class CollectionUtils {
    /**
     * Checks if object is not null, otherwise throws exception.
     * @param t - the object
     * @param <T>
     * @return the not null object
     */
    @NotNull
    public static <T> T forceNotNull(final T t) {
        if (t == null) {
            throw new IllegalArgumentException("Value can't to be null!");
        }

        return t;
    }

    private CollectionUtils() { }
}
