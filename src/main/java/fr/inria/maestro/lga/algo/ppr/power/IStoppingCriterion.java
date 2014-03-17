package fr.inria.maestro.lga.algo.ppr.power;

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
 * The Stopping criterion for general pagerank iterations.
 * based on it.unimi.dsi.law.rank.*
 */
public interface IStoppingCriterion {

    boolean shouldStop(int curIter, double []curPagerank, double []prevPagerank);



    public static class IterationNumberStoppingCriterion implements IStoppingCriterion {
        private final int maxIter;

        public IterationNumberStoppingCriterion(final int maxIter) {
            this.maxIter = maxIter;
        }

        public boolean shouldStop(int curIter, double []curPagerank, double []prevPagerank) {
            return curIter >= maxIter;
        }
    }


    public static class DeltaStoppingCriterion implements IStoppingCriterion {
        private final double threshold;

        public DeltaStoppingCriterion(final double threshold) {
            this.threshold = threshold;
        }

        public boolean shouldStop(int curIter, double []curPagerank, double []prevPagerank) {
            return threshold > computeNormL1(curPagerank, prevPagerank);
        }

        private static double computeNormL1( double[] a, double[] b ) {
            if (b != null && a.length != b.length) {
                throw new IllegalArgumentException( "The two vectors have different sizes." );
            }

            double normL1 = 0.0, c = 0.0, y, t;
            if (b == null) {
                for ( int i = a.length; i-- != 0; ) {
                    y = Math.abs(a[i]) - c;
                    t = normL1 + y;
                    c = (t - normL1) - y;
                    normL1 = t;
                }
            }
            else {
                for (int i = a.length; i-- != 0; ) {
                    y = Math.abs(a[i] - b[i]) - c;
                    t = normL1 + y;
                    c = (t - normL1) - y;
                    normL1 = t;
                }
            }
            return normL1;
        }
    }

    public static class OrStoppingCriterion implements IStoppingCriterion {
        private final IStoppingCriterion cr1;
        private final IStoppingCriterion cr2;

        public OrStoppingCriterion(final IStoppingCriterion cr1, final IStoppingCriterion cr2) {
            this.cr1 = cr1;
            this.cr2 = cr2;
        }

        @Override
        public boolean shouldStop(final int curIter, final double[] curPagerank, final double[] prevPagerank) {
            return cr1.shouldStop(curIter, curPagerank, prevPagerank) || cr2.shouldStop(curIter, curPagerank, prevPagerank);
        }
    }

    public static class AndStoppingCriterion implements IStoppingCriterion {
        private final IStoppingCriterion cr1;
        private final IStoppingCriterion cr2;

        public AndStoppingCriterion(final IStoppingCriterion cr1, final IStoppingCriterion cr2) {
            this.cr1 = cr1;
            this.cr2 = cr2;
        }

        @Override
        public boolean shouldStop(final int curIter, final double[] curPagerank, final double[] prevPagerank) {
            return cr1.shouldStop(curIter, curPagerank, prevPagerank) && cr2.shouldStop(curIter, curPagerank, prevPagerank);
        }
    }
}
