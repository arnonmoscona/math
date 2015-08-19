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
import com.moscona.util.StringHelper;

/**
 * A bin for use in collections of bins like histograms.
 * Created for use in RangeHistogram
 * Created by Arnon Moscona on 11/21/2014.
 */
public class Bin<T extends Comparable<T>> implements Comparable<Bin<T>> {
    private T from;
    private T to;
    private int count;

    public Bin(T from, T to, int count) throws InvalidArgumentException {
        if (from == null || to == null) {
            throw new InvalidArgumentException("from and to arguments may not be null");
        }

        if (from.compareTo(to) > 0) {
            throw new InvalidArgumentException("from may not be larger than to.");
        }

        this.from = from;
        this.to = to;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public T getFrom() {
        return from;
    }

    public T getTo() {
        return to;
    }

    public String getBinLabel(String separator) {
        if (isNumeric()) {
            // pretty print
            if (isReal()) {
                return StringHelper.prettyPrint(((Number) from).doubleValue()) + separator +
                        StringHelper.prettyPrint(((Number)to).doubleValue());
            }
            else {
                return StringHelper.prettyPrint(((Number)from).longValue()) + separator +
                        StringHelper.prettyPrint(((Number)to).longValue());
            }
        }
        else {
            return from.toString() + separator + to.toString();
        }
    }

    public boolean isReal() {
        return Double.class.isAssignableFrom(from.getClass()) || Float.class.isAssignableFrom(from.getClass());
    }

    public boolean isNumeric() {
        return Number.class.isAssignableFrom(from.getClass());
    }


    /**
     * Compares strictly for use in the histogram. Compares only based on the from variable and assumes a set of
     * strictly ordered bins.
     * This is not particularly good for double bins with sloppy construction. No provision for comparison granularity
     * @param o the Bin to compare to
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Bin<T> o) {
        if (!Bin.class.isAssignableFrom(o.getClass())) {
            return 0;
        }
        Bin<T> other = (Bin<T>)o;
        return (from.compareTo(other.from));

        // FIXME floating types need a comparison granularity feature
    }

    public boolean contains(T value) {
        return value.compareTo(from) >= 0 && value.compareTo(to) <= 0;
    }

    public boolean overlaps(Bin<T> bin) {
        return bin.contains(from) || bin.contains((to)) || contains(bin.from) || contains(bin.to);
    }

    @Override
    public boolean equals(Object o) {
        // FIXME just like compare() should support equality granularity for real bins
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bin bin = (Bin) o;

        if (count != bin.count) return false;
        if (!from.equals(bin.from)) return false;
        if (!to.equals(bin.to)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        // FIXME just like compare() should support hash value equality granularity for real bins
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return "Bin{" +
                "from=" + from +
                ", to=" + to +
                ", count=" + count +
                '}';
    }
}
