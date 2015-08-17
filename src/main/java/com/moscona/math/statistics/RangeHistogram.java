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

import com.moscona.exceptions.InvalidArgumentException;
import com.moscona.exceptions.InvalidStateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created: 5/17/11 3:55 PM
 * By: Arnon Moscona
 * A simple representation of a histogram
 */
public class RangeHistogram<T extends Comparable<T>> implements ImmutableRangeHistogram<T> {
    private ArrayList<Bin<T>> bins;

    public RangeHistogram() {
        bins = new ArrayList<>();
    }

    @Override
    public List<Bin<T>> getBins() {
        return Collections.unmodifiableList(bins);
    }

    @Override
    public Bin<T> findBin(T value) {
        for (Bin<T> bin: bins) {
            if (bin.contains(value)) {
                return bin;
            }
        }
        return null;
    }

    @Override
    public boolean contains(T value) {
        return findBin(value) != null;
    }

    public RangeHistogram<T> addBin(T from, T to, int count) throws InvalidArgumentException, InvalidStateException {
        bins.add(new Bin<T>(from,to,count));
        Collections.sort(bins);
        validate();
        return this;
    }

    private void validate() throws InvalidStateException {
        Bin<T> previous = null;
        for (Bin<T> bin: bins) {
            if (previous != null) {
                String separator = "-";
                if (bin.isReal()) {
                    // allow equality at the boundaries
                    if (bin.getFrom().compareTo(previous.getTo()) < 0) {
                        // this comparison is sufficient as the bins are already sorted at this point
                        throw new InvalidStateException("The bin '"+bin.getBinLabel(separator)+" overlaps with the bin "+previous.getBinLabel(separator));
                    }
                }
                else {
                    // discreet - must be mutually exclusive
                    if (previous.overlaps(bin)) {
                        throw new InvalidStateException("The bin '"+bin.getBinLabel(separator)+" overlaps with the bin "+previous.getBinLabel(separator));
                    }
                }
            }
            previous = bin;
        }
    }

}
