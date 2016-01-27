/*
 * The MIT License
 *
 * Copyright 2016 Liu.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Utility;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Liu
 */
public class GetArrayListTest
{
    
    public GetArrayListTest()
    {
    }

    @Test
    public void testGet()
    {
        System.out.println("Get");
        int[] items = new int[0];
        GetArrayList<Integer> instance = new GetArrayList<Integer>();
        List<Integer> expResult = new ArrayList();
        List<Integer> result = instance.Get();
        assertTrue(result.isEmpty());
        assertTrue(result instanceof ArrayList);
        items = new int[]{1,2,3};
        result = instance.Get(1,2,3);
        assertTrue(result.size() == 3);
        assertTrue(result.get(0) == 1);
        assertTrue(result.get(1) == 2);
        assertTrue(result.get(2) == 3);
    }

    @Test
    public void testCopy()
    {
        System.out.println("Copy");
        GetArrayList instance = new GetArrayList();
        List<Integer> input = new ArrayList<>();
        input.add(1);input.add(2);
        List<Integer> result = instance.Copy(input);
        assertEquals(input.size(), result.size());
        assertTrue(input.get(0) == result.get(0));
        assertTrue(input.get(1) == result.get(1));
    }
    
}
