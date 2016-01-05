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

import Model.Model.MethodLine;
import Model.interfaces.IFunction;
import Model.interfaces.IFunctionInput;
import Model.interfaces.ILineLabel;
import Model.interfaces.ILineNumber;
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
public class MethodLineTest
{
    @Mock
    IFunction _Function = createMock(IFunction.class);
    @Mock
    IVariable _Variable = createMock(IVariable.class);
    @Mock
    ILineLabel _LineLabel = createMock(ILineLabel.class);
    @Mock
    ILineNumber _LineNumber = createMock(ILineNumber.class);
    @Mock
    IFunctionInput _FunctionInput = createMock(IFunctionInput.class);
    
    @TestSubject
    MethodLine _MethodLine;
    
    public MethodLineTest()
    {
        List<IFunctionInput> functionInputs = new ArrayList<>();
        functionInputs.add(_FunctionInput);
        List<ILineLabel> labels = new ArrayList<>();
        labels.add(_LineLabel);
        _MethodLine = new MethodLine(_Function, _Variable, functionInputs, labels, _LineNumber);
    }

    @Test
    public void testGetFunction()
    {
        System.out.println("GetFunction");
        MethodLine instance = _MethodLine;
        IFunction expResult = _Function;
        IFunction result = instance.GetFunction();
        assertEquals(expResult, result);
        
    }

    @Test
    public void testGetOutputVariable()
    {
        System.out.println("GetOutputVariable");
        MethodLine instance = _MethodLine;
        IVariable expResult = _Variable;
        IVariable result = instance.GetOutputVariable();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetInputs()
    {
        System.out.println("GetInputs");
        MethodLine instance = _MethodLine;
        ListProperty<IFunctionInput> result = instance.GetInputs();
        assertEquals(1, result.size());
        assertTrue(result.contains(_FunctionInput));
    }

    @Test
    public void testGetLineLabels()
    {
        System.out.println("GetLineLabels");
        MethodLine instance = _MethodLine;
        ListProperty<ILineLabel> result = instance.GetLineLabels();
        assertTrue(result.contains(_LineLabel));
        assertEquals(1, result.size());
    }

    @Test
    public void testGetLineCount()
    {
        System.out.println("GetLineCount");
        MethodLine instance = _MethodLine;
        ILineNumber expResult = _LineNumber;
        ILineNumber result = instance.GetLineNumber();
        assertEquals(expResult, result);
    }

    
}
