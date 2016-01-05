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

import Model.Model.ACClass;
import Model.interfaces.IField;
import Model.interfaces.IFullName;
import Model.interfaces.IMethod;
import Model.interfaces.IModifiers;
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
public class ACClassTest
{
    @Mock
    IFullName _Name = createMock(IFullName.class),
              _extend = createMock(IFullName.class),
              _implement = createMock(IFullName.class);
    @Mock
    IField _Field = createMock(IField.class);
    @Mock
    IVisibility _Visibility = createMock(IVisibility.class);
    @Mock
    IModifiers _Modifiers = createMock(IModifiers.class);
    @Mock
    IMethod _Method = createMock(IMethod.class);
    @TestSubject
    ACClass _Class;
    
    public ACClassTest()
    {
        List<IField> fields = new ArrayList<>();
        fields.add(_Field);
        List<IMethod> methods = new ArrayList<>();
        methods.add(_Method);
        List<IFullName> el = new ArrayList<>();
        el.add(_extend);
        List<IFullName> il = new ArrayList<>();
        il.add(_implement);
        _Class = new ACClass(_Name, _Visibility, _Modifiers, methods, el, fields, il, false, false, true, 0,0);
    }

    @Test
    public void testGetFields()
    {
        System.out.println("GetFields");
        ACClass instance = _Class;
        ListProperty<IField> result = instance.GetFields();
        assertEquals(_Field, result.get(0));
        assertTrue(result.size()==1);
    }

    @Test
    public void testGetImplements()
    {
        System.out.println("GetImplements");
        ACClass instance = _Class;
        ListProperty<IFullName> result = instance.GetImplements();
        assertEquals(_implement, result.get(0));
        assertTrue(result.size()==1);
    }


    
}
