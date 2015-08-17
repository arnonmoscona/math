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
import com.moscona.math.statistics.Bin;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Bin Tester.
 *
 * @author Arnon Moscona
 * @version 1.0
 * @since <pre>Nov 21, 2014</pre>
 */
public class BinTest {
    Bin<Integer> integerBin;
    Bin<Long> longBin;
    Bin<Short> shortBin;
    Bin<Float> floatBin;
    Bin<Double> doubleBin;
    Bin<String> stringBin;

    Bin[] numericBins;
    Bin[] realBins;
    Bin[] nonRealBins;

    @Before
    public void before() throws Exception {
        integerBin = new Bin<>(1, 2, 3);
        longBin = new Bin<>(4L, 5L, 6);
        shortBin = new Bin<>((short)7, (short)8, 9);
        floatBin = new Bin<>(1.1F, 2.1F, 3);
        doubleBin = new Bin<>(4.1, 5.1, 6);
        stringBin = new Bin<>("one", "two", 3);

        numericBins = new Bin[] {integerBin, longBin, shortBin, floatBin, doubleBin};
        realBins = new Bin[] {floatBin, doubleBin};
        nonRealBins = new Bin[] {stringBin, shortBin, integerBin, longBin};
    }

    @After
    public void after() throws Exception {
        integerBin = null;
        longBin = null;
        shortBin = null;
        floatBin = null;
        doubleBin = null;
        stringBin = null;
        numericBins = null;
        realBins = null;
    }

    @Test(expected = InvalidArgumentException.class)
    public void nullArgumentConstructorTest() throws Exception {
        new Bin<Integer>(null, 2, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void nullArgument2ConstructorTest() throws Exception {
        new Bin<Integer>(1, null, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void outOfOrderRangeConstructorTest() throws Exception {
        new Bin<Integer>(3, 2, 0);
    }


    /**
     * Method: getCount()
     */
    @Test
    public void testGetCount() throws Exception {
        assertEquals("integerBin count incorrect", 3, integerBin.getCount());
    }

    /**
     * Method: getFrom()
     */
    @Test
    public void testGetFrom() throws Exception {
        assertEquals("integerBin from incorrect", 1, (int)integerBin.getFrom());
    }

    /**
     * Method: getTo()
     */
    @Test
    public void testGetTo() throws Exception {
        assertEquals("to incorrect", 2, (int)integerBin.getTo());
    }

    /**
     * Method: getBinLabel(String separator)
     */
    @Test
    public void testGetBinLabel() throws Exception {
        assertEquals("bin label incorrect", "1 to 2", integerBin.getBinLabel(" to "));
    }

    /**
     * Method: getBinLabel(String separator)
     */
    @Test
    public void testGetBinLabelDoubles() throws Exception {
        Bin<Double> bin = new Bin<>(1000.0, 10000.0, 1234);
        assertEquals("bin label incorrect", "1,000.00 to 10,000.00", bin.getBinLabel(" to "));
    }

    /**
     * Method: isReal()
     */
    @Test
    public void testIsReal() throws Exception {
        for (Bin bin: realBins) {
            assertTrue("real bin not marked real", bin.isReal());
        }
    }

    /**
     * Method: isReal()
     */
    @Test
    public void testIsRealNotReals() throws Exception {
        for (Bin bin: nonRealBins) {
            assertFalse("non-real bin marked real", bin.isReal());
        }
    }

    /**
     * Method: compareTo(Bin<T> o)
     */
    @Test
    public void testCompareTo() throws Exception {
        Bin<Integer> other = new Bin<>(4,5,6);
        assertEquals("incorrect bin comparison: should be smaller than other based on from element", -1, integerBin.compareTo(other));
        other = new Bin<>(-2,-1,-0);
        assertEquals("incorrect bin comparison: should be larger than other based on from element", 1, integerBin.compareTo(other));
        other = new Bin<>(1,2,4);
        assertEquals("incorrect bin comparison: same range considered equal", 0, integerBin.compareTo(other));
        other = new Bin<>(1,3,5);
        assertEquals("a bin starting from the same value is equal even if the ending value is different", 0, integerBin.compareTo(other));
        // FIXME the following does not pass
//        Bin<Double> doubleOther = new Bin<>(12.3/3, 4.2, 0);
//        assertEquals("the two double bins should be considered equal", 0, doubleBin.compareTo(doubleOther));
    }

    /**
     * Method: contains(T value)
     */
    @Test
    public void testContains() throws Exception {
        assertTrue("double range should contain 4.1", doubleBin.contains(4.1));
        assertTrue("double range should contain 4.2", doubleBin.contains(4.2));
        assertTrue("double range should contain 5.1", doubleBin.contains(5.1));
        assertFalse("double range should not contain 5.2", doubleBin.contains(5.2));
        assertFalse("double range should not contain 4.09", doubleBin.contains(4.09));
        assertTrue("string bin should contain \"one and something\"", stringBin.contains("one and something"));
        assertFalse("string bin should not contain \"two and something\"", stringBin.contains("two and something"));
        assertFalse("string bin should not contain \"1\"", stringBin.contains("1"));
    }

    /**
     * Method: overlaps(Bin<T> bin)
     */
    @Test
    public void testOverlaps() throws Exception {
        Bin<Double> other = new Bin<>(5.0, 5.5, 0);
        assertTrue(doubleBin.overlaps(other));
        assertTrue(other.overlaps(doubleBin));

        other = new Bin<>(9.0,10.0,1);
        assertFalse(doubleBin.overlaps(other));
        assertFalse(other.overlaps(doubleBin));
    }

    /**
     * Method: equals(Object o)
     */
    @Test
    public void testEquals() throws Exception {
        assertTrue(integerBin.equals(new Bin<>(1,2,3)));
        assertFalse("equals() takes the count into account, as opposed to compare()", integerBin.equals(new Bin<>(1, 2, 0)));
        // FIXME just like compare() should support equality granularity for real bins
    }

    /**
     * Method: hashCode()
     */
    @Test
    public void testHashCode() throws Exception {
        assertTrue("hash value should be the same if values are the same", integerBin.hashCode() == new Bin<Integer>(1,2,3).hashCode());
        assertFalse("hash value should be different if from are the different", integerBin.hashCode() == new Bin<Integer>(0, 2, 3).hashCode());
        assertFalse("hash value should be different if to are the different", integerBin.hashCode() == new Bin<Integer>(1, 4, 3).hashCode());
        assertFalse("hash value should be different if count are the different", integerBin.hashCode() == new Bin<Integer>(1, 2, 0).hashCode());

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {
        assertEquals("Bin{from=7, to=8, count=9}", shortBin.toString());
        assertEquals("Bin{from=1, to=2, count=3}", integerBin.toString());
        assertEquals("Bin{from=1000, to=1000001, count=0}", new Bin<>(1000,1000001,0).toString());
        assertEquals("Bin{from=4, to=5, count=6}", longBin.toString());
        assertEquals("Bin{from=1.1, to=2.1, count=3}", floatBin.toString());
        assertEquals("Bin{from=4.1, to=5.1, count=6}", doubleBin.toString());
        assertEquals("Bin{from=one, to=two, count=3}", stringBin.toString());
    }


    /**
     * Method: isNumeric()
     */
    @Test
    public void testIsNumeric() throws Exception {
        for (Bin bin: numericBins) {
            assertTrue("supposed to be numeric, but not...", bin.isNumeric());
        }
        assertFalse("Bin<String> is not numeric, but resolves as numeric", stringBin.isNumeric());
    }

} 
