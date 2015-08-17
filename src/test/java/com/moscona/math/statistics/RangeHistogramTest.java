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

import com.moscona.exceptions.InvalidStateException;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.List;

import static org.junit.Assert.*;

/**
 * RangeHistogram Tester.
 *
 * @author Arnon Moscona
 * @version 1.0
 * @since <pre>Nov 25, 2014</pre>
 */
public class RangeHistogramTest {
    private RangeHistogram<Integer> intHistogram;
    private RangeHistogram<String> stringHistogram;

    @Before
    public void before() throws Exception {
        intHistogram = new RangeHistogram<>();
        stringHistogram = new RangeHistogram<>();
    }

    @After
    public void after() throws Exception {
        intHistogram = null;
        stringHistogram = null;
    }

    /**
     * Method: addBin(T from, T to, int count)
     */
    @Test
    public void testAddBin() throws Exception {
        intHistogram.addBin(1, 10, 1).addBin(11, 20, 0); // no exception
        stringHistogram.addBin("1", "10", 1).addBin("11", "20", 0); // no exception
    }

    /**
     * Method: addBin(T from, T to, int count)
     */
    @Test(expected = InvalidStateException.class)
    public void testExceptionAddBin() throws Exception {
        intHistogram.addBin(1, 10, 1).addBin(2, 20, 0); // exception
    }

    /**
     * Method: getBins()
     */
    @Test
    public void testGetBins() throws Exception {
        intHistogram.addBin(1, 10, 1).addBin(11, 20, 0);
        List<Bin<Integer>> bins = intHistogram.getBins();
        assertEquals("list length should be 2", 2, bins.size());
        assertTrue("the first bin should start from 1", bins.get(0).equals(new Bin<>(1,10,1)));
        assertEquals("the second bin should start from 11", 11, (int)bins.get(1).getFrom());
    }

    /**
     * Method: findBin(T value)
     */
    @Test
    public void testFindBin() throws Exception {
        intHistogram.addBin(1, 10, 1).addBin(11, 20, 0).addBin(21, 30, 9);
        assertEquals("found incorrect bin", new Bin<>(11, 20, 0), intHistogram.findBin(15));
    }

    /**
     * Method: contains(T value)
     */
    @Test
    public void testContains() throws Exception {
        intHistogram.addBin(1, 10, 1).addBin(11, 20, 0);
        assertTrue("should contain 10", intHistogram.contains(10));
        assertFalse("should not contain 0", intHistogram.contains(0));
    }

} 
