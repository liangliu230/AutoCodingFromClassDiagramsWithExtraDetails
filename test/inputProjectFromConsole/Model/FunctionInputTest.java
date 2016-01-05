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

import Model.Model.FunctionInput;
import Model.interfaces.IFunction;
import Model.interfaces.IVariable;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;
import org.easymock.TestSubject;
/**
 *
 * @author Liu
 */
public class FunctionInputTest
{
    @Mock
    IFunction _Func;
    @Mock
    IVariable _V1,_V2;
    @TestSubject
    FunctionInput _FunctionInput;    
    
    public FunctionInputTest()
    {
        _Func = createMock(IFunction.class);
        _V1 = createMock(IVariable.class);
        _V2 = createMock(IVariable.class);
        List<IVariable> list = new ArrayList<>();
        list.add(_V1);
        list.add(_V2);
        _FunctionInput = new FunctionInput(_Func, list);
    }

    @Test
    public void testGetFunction()
    {
        IFunction result = _FunctionInput.GetFunction();
        assertEquals(result, _Func);
    }

    @Test
    public void testGetInputVariables()
    {
        System.out.println("GetInputVariables");
        ListProperty<IVariable> result = _FunctionInput.GetInputVariables();
        assertEquals(2, result.size());
        assertTrue(result.contains(_V1));
        assertTrue(result.contains(_V2));
    }

    
}
