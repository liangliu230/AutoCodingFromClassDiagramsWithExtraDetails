/*
 * The MIT License
 *
 * Copyright 2015 Liu.
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
package Model.Model;

import Model.interfaces.IName;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Liu
 */
public class FullNameTest
{
    
    public FullNameTest()
    {
    }

    @Test
    public void testGetPackageNames()
    {
        System.out.println("GetPackageNames");
        FullName instance = new FullName();   
        IName[] result = instance.GetPackageNames();
        assertTrue(result.length == 0);
        IName name = new Name();
        IName[] arr = new IName[]{name};
        instance = new FullName(arr,new Name());
        assertArrayEquals(instance.GetPackageNames(), arr);
    }

    @Test
    public void testGetClassName()
    {
        System.out.println("GetClassName");
        FullName instance = new FullName();
        IName result = instance.GetClassName();
        assertEquals("", result.GetName().getValue());
        instance = new FullName(new IName[0], new Name("abc"));
        result = instance.GetClassName();
        assertEquals("abc", result.GetName().getValue());
    }

    @Test
    public void testSetPackageNames()
    {
        System.out.println("SetPackageNames");
        IName[] packageNames = new IName[0];
        FullName instance = new FullName();
        instance.SetPackageNames(packageNames);
        IName[] result = instance.GetPackageNames();
        assertArrayEquals(result, packageNames);
    }

    @Test
    public void testToString()
    {
        System.out.println("toString");
        FullName instance = new FullName();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        instance = new FullName(new IName[]{new Name("123"), new Name("456")}, new Name("789"));
        assertEquals("123.456.789", instance.toString());
    }
    
}
