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

/**
 * Simple structure to store pairs of objects.
 * @param <U> the first object type
 * @param <V> the second object type
 */
public final class Pair<U, V> {
    private final U first;
    private final V second;

    private Pair(final U first, final V second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first object.
     * @return the first object
     */
    public U getFirst() {
        return first;
    }

    /**
     * Returns the second object.
     * @return the second object
     */
    public V getSecond() {
        return second;
    }

    /**
     * Creates the pair of two objects.
     * @param u - first object
     * @param v - second object
     * @param <U>
     * @param <V>
     * @return
     */
    public static <U, V> Pair<U, V> create(final U u, final V v) {
        return new Pair<U, V>(u, v);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Pair pair = (Pair) o;

        return areEqual(first, pair.first) && areEqual(second, pair.second);
    }

    @Override
    public int hashCode() {
        int result;
        result = (first != null ? first.hashCode() : 0);
        result = 29 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "(" + getFirst() + ", " + getSecond() + ")";
    }

    private static <T> boolean areEqual(final T t1, final T t2) {
        return t1 != null ? t1.equals(t2) : t2 == null;
    }
}
