/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.sort;

import org.helyx.app.j2me.getabike.lib.comparator.Comparator;
import org.helyx.app.j2me.getabike.lib.sort.FastQuickSortException;

public class FastQuickSort {

	private Comparator comparator;
	
	public FastQuickSort(Comparator comparator) {
		super();
		this.comparator = comparator;
	}

	public void sort(Object[] a) {
		try {
			if (a == null || a.length <= 1) {
				return ;
			}
			quickSort(a, 0, a.length - 1);
			insertionSort(a, 0, a.length - 1);
		}
		catch(Throwable t) {
			throw new FastQuickSortException(t);
		}
	}

	/**
	 * This is a generic version of C.A.R Hoare's Quick Sort algorithm. This
	 * will handle arrays that are already sorted, and arrays with duplicate
	 * keys.<BR>
	 * 
	 * If you think of a one dimensional array as going from the lowest index on
	 * the left to the highest index on the right then the parameters to this
	 * function are lowest index or left and highest index or right. The first
	 * time you call this function it will be with the parameters 0, a.length -
	 * 1.
	 * 
	 * @param a
	 *            an integer array
	 * @param lo0
	 *            left boundary of array partition
	 * @param hi0
	 *            right boundary of array partition
	 */
	private void quickSort(Object[] a, int l, int r) throws Exception {
		int M = 4;
		int i;
		int j;
		Object v;

		if ((r - l) > M) {
			i = (r + l) / 2;
			if (comparator.compare(a[l], a[i]) > 0) {
				swap(a, l, i);
			}
			if (comparator.compare(a[l], a[r]) > 0) {
				swap(a, l, r);
			}
			if (comparator.compare(a[i], a[r]) > 0) {
				swap(a, i, r);
			}

			j = r - 1;
			swap(a, i, j);
			i = l;
			v = a[j];
			for (;;) {
				while (comparator.compare(a[++i], v) < 0);
				while (comparator.compare(a[--j], v) > 0);
				if (j < i) {
					break;
				}
				swap(a, i, j);
			}
			swap(a, i, r - 1);
			quickSort(a, l, j);
			quickSort(a, i + 1, r);
		}
	}

	private void swap(Object[] a, int i, int j) {
		Object T;
		T = a[i];
		a[i] = a[j];
		a[j] = T;
	}

	private void insertionSort(Object[] a, int lo0, int hi0) throws Exception {
		int i;
		int j;
		Object v;

		for (i = lo0 + 1; i <= hi0; i++) {
			v = a[i];
			j = i;
			while ((j > lo0) && (comparator.compare(a[j - 1], v) > 0)) {
				a[j] = a[j - 1];
				j--;
			}
			a[j] = v;
		}
	}

}