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
package inputProjectFromConsole.Model;

import Model.Model.Parameter;
import Model.Model.FullName;
import Model.Model.Name;
import Model.interfaces.IFullName;
import Model.interfaces.IName;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Liu
 */
public class ParameterTest
{

    FullName _FullName;
    IName _N1 = new Name("p1"), _N2 = new Name("p2");
    Name _N=new Name("c1"); String _Name = "pp";
    Parameter _P;
    public ParameterTest()
    { 
        IName[] list = new IName[]{_N1,_N2};
        _FullName = new FullName(list,_N);
        _P = new Parameter(_Name,_FullName);
    }

    @Test
    public void testGetType()
    {
        System.out.println("GetType");
        
        Parameter instance = _P;
        IFullName expResult = _FullName;
        IFullName result = instance.GetType();
        assertEquals(expResult, result);
    }

    
}
