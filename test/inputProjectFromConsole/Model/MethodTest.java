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

import Model.Model.Method;
import Model.Model.Name;
import Model.interfaces.IFullName;
import Model.interfaces.IFunctionInput;
import Model.interfaces.IMethodLine;
import Model.interfaces.IModifiers;
import Model.interfaces.IName;
import Model.interfaces.IParameter;
import Model.interfaces.IVisibility;
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
public class MethodTest
{
    IName _Name = new Name("name");
    @Mock
    IFullName _output = createMock(IFullName.class);
    @Mock
    IParameter _Parameter = createMock(IParameter.class);
    @Mock
    IMethodLine _MethodLine = createMock(IMethodLine.class);
    @Mock
    IFunctionInput _Annotation = createMock(IFunctionInput.class);
    @Mock
    IVisibility _Visibility = createMock(IVisibility.class);
    @Mock
    IModifiers _Modifiers = createMock(IModifiers.class);
    @TestSubject
    Method _Method;
    
    public MethodTest()
    {
        List<IParameter> ps = new ArrayList<>();ps.add(_Parameter);
        List<IMethodLine> ms = new ArrayList<>();ms.add(_MethodLine);
        List<IFunctionInput> as = new ArrayList<>();as.add(_Annotation);
        _Method = new Method(_Name, _output, ps, ms, as, _Visibility, _Modifiers,false,new ArrayList<>());
        
    }

    @Test
    public void testGetOutputType()
    {
        System.out.println("GetOutputType");
        Method instance = new Method(_Name, _output, new ArrayList<IParameter>(), new ArrayList<IMethodLine>(), new ArrayList<IFunctionInput>(), _Visibility, _Modifiers,false,new ArrayList<>());
        IFullName expResult = _output;
        IFullName result = instance.GetOutputType();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetInputParameters()
    {
        System.out.println("GetInputParameters");
        Method instance = new Method(_Name, _output, new ArrayList<IParameter>(), new ArrayList<IMethodLine>(), new ArrayList<IFunctionInput>(), _Visibility, _Modifiers,false,new ArrayList<>());

        ListProperty<IParameter> result = instance.GetInputParameters();
        assertEquals(result.size(), 0);
        instance = _Method;
        result = instance.GetInputParameters();
        assertEquals(1, result.size());
        assertTrue(result.contains(_Parameter));
    }

    @Test
    public void testGetMethodBody()
    {
        System.out.println("GetMethodBody");
        Method instance = new Method(_Name, _output, new ArrayList<IParameter>(), new ArrayList<IMethodLine>(), new ArrayList<IFunctionInput>(), _Visibility, _Modifiers,false,new ArrayList<>());

        ListProperty<IMethodLine> result = instance.GetMethodBody();
        assertEquals(0, result.size());
        result = _Method.GetMethodBody();
        assertEquals(1, result.size());
        assertTrue(result.contains(_MethodLine));
    }

    @Test
    public void testGetAnnotations()
    {
        System.out.println("GetAnnotations");
        Method instance = new Method(_Name, _output, new ArrayList<IParameter>(), new ArrayList<IMethodLine>(), new ArrayList<IFunctionInput>(), _Visibility, _Modifiers,false,new ArrayList<>());

        ListProperty<IFunctionInput> result = instance.GetAnnotations();
        assertEquals(0, result.size());
        result = _Method.GetAnnotations();
        assertEquals(1, result.size());
        assertTrue(result.contains(_Annotation));
    }

    @Test
    public void testGetVisibility()
    {
        System.out.println("GetVisibility");
        Method instance = _Method;
        IVisibility expResult = _Visibility;
        IVisibility result = instance.GetVisibility();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetModifiers()
    {
        System.out.println("GetModifiers");
        Method instance = _Method;
        IModifiers expResult = _Modifiers;
        IModifiers result = instance.GetModifiers();
        assertEquals(expResult, result);
    }

    
}
