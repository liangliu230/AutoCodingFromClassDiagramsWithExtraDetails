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

import Model.Model.Field;
import Model.interfaces.IFullName;
import Model.interfaces.IFunctionInput;
import Model.interfaces.IModifiers;
import Model.interfaces.IName;
import Model.interfaces.IVisibility;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;
import org.easymock.TestSubject;

/**
 *
 * @author Liu
 */
public class FieldTest
{
    @Mock
    IName _Name = createMock(IName.class);
    @Mock
    IFullName _Type = createMock(IFullName.class);
    @Mock
    IVisibility _Visibility = createMock(IVisibility.class);
    @Mock
    IModifiers _Modifiers = createMock(IModifiers.class);
    @Mock
    IFunctionInput _DefaultValue = createMock(IFunctionInput.class);
    @TestSubject
    Field _Field;
    
    public FieldTest()
    {
        _Field = new Field(_Name, _Type, _Modifiers, _Visibility, _DefaultValue);
        
    }

    @Test
    public void testGetDefaultValue()
    {
        System.out.println("GetDefaultValue");
        Field instance = _Field;
        IFunctionInput expResult = _DefaultValue;
        IFunctionInput result = instance.GetDefaultValue();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetModifiers()
    {
        System.out.println("GetModifiers");
        Field instance = _Field;
        IModifiers expResult = _Modifiers;
        IModifiers result = instance.GetModifiers();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetVisibility()
    {
        System.out.println("GetVisibility");
        Field instance = _Field;
        IVisibility expResult = _Visibility;
        IVisibility result = instance.GetVisibility();
        assertEquals(expResult, result);
    }
}
