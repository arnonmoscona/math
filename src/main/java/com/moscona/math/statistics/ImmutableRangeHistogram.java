/*
 * Copyright (c) 2015. Arnon Moscona
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.moscona.math.statistics;

import java.util.List;

/**
 * Immutable representation of a RangeHistogram
 * Created by Arnon Moscona on 11/21/2014.
 */
public interface ImmutableRangeHistogram<T extends Comparable<T>> {
    List<Bin<T>> getBins();

    Bin<T> findBin(T value);

    boolean contains(T value);

    // FIXME equals
    // FIXME hash
}
