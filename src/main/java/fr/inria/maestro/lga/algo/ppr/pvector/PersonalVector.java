package fr.inria.maestro.lga.algo.ppr.pvector;

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

import java.util.List;

/**
 * Personal vector for page rank (count for every cluster)
 * Vector with (0,0,..,1/m,...,1/m,0)
 * value of index i is 1/m if this node in the cluster 
 * m-- number nodes in the cluster
 */
public class PersonalVector implements IPersonalVector {
	private final int myNumberOfNodes;
	private double[] personalVector;

	public PersonalVector(List<Integer> clusterNodes, int num) {
		myNumberOfNodes = num;
        initPersonalVector(clusterNodes);
 	}
	
	/**
     * @return personal vector for page rank
     */
    public double[] getPersonalVector() {
        return personalVector;
    }

    private void initPersonalVector(List<Integer> clusterNodes) {
		personalVector = new double[myNumberOfNodes];
		int msize = 0;
		
		if (clusterNodes != null) {
			msize = clusterNodes.size();
		} else {
			fillVector(personalVector);
		}
		
		for (int i = 0; i < msize; i++) {
			personalVector[clusterNodes.get(i)] = 1.0/msize;
		}
	}
	
	private void fillVector(double[] personalVector) {
		double value = 1.0/myNumberOfNodes;
		for (int i = 0; i < myNumberOfNodes; i++) {
			personalVector[i] = value;
		}
	}

}
